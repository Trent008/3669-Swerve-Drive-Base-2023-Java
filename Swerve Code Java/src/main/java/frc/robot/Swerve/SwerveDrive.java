package frc.robot.Swerve;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.Constants.SwerveConstants;
import frc.robot.translations2D.Angle;
import frc.robot.translations2D.Pose;
import frc.robot.translations2D.Vector;

import java.util.List;
import java.util.ArrayList;

/**
 * creates a swerve drive object that controls
 * an array of swerve drive modules
 * */
public class SwerveDrive
{
    private List<SwerveModule> modules;
    private Pose robotRate; // stores the target robot-centric drive rate then the actual robot-cnetric drive rate
    private Pose fieldRate = new Pose(); // field-centric rate after acceleration
    private double moduleWheelSpeed;         // stores the velocity of each module in turn
    private double fastestModule;            // fastest module velocity to be limited to 1
    private Vector averagePositionChange;    // average module position change
    private Vector fieldDisplacement = new Vector();      // field location in inches from the starting point
    private AHRS navx = new AHRS(SPI.Port.kMXP); // NavX V2 object
    private Angle navXAngle;
    private Angle fieldAngle;

    public SwerveDrive() {
        modules = new ArrayList<>();
        modules.add(new SwerveModule(11, 31, 21, new Vector(-17.75, 25)));
        modules.add(new SwerveModule(12, 32, 22, new Vector(-17.75, -25)));
        modules.add(new SwerveModule(13, 33, 23, new Vector(17.75, 25)));
        modules.add(new SwerveModule(14, 34, 24, new Vector(17.75, -25)));
    }
    /**
     * runs the swerve modules using the values from the motion controller
     **/
    public void set(Pose driveRate, boolean isAutonomous)
    {
        navXAngle = new Angle(navx.getYaw());
        fieldAngle = navXAngle.getAdded(SwerveConstants.startingAngle);
        robotRate = driveRate.getRotatedCW(-fieldAngle.value); // robot orient the drive rate

        fastestModule = 1;
        for (int i = 0; i < 4; i++) // compare all of the module velocities to find the largest
        {
            moduleWheelSpeed = modules.get(i).getModuleVector(driveRate).getMagnitude();
            if (moduleWheelSpeed > fastestModule)
            {
                fastestModule = moduleWheelSpeed;
            }
        }
        driveRate.divide(fastestModule);                         // limit the drive rate to keep all velocities below 1
        if (!isAutonomous)
        {
            fieldRate.moveToward(driveRate, SwerveConstants.robotPercentChangePerCycle); // accelerate toward the drive rate target
        }
        else
        {
            fieldRate = driveRate;
        }
        robotRate = fieldRate.getRotatedCW(-fieldAngle.value);    // robot orient the drive rate

        averagePositionChange = new Vector(); // reset the average to zero before averaging again
        for (int i = 0; i < 4; i++)       // loop through the module indexes again
        {
            modules.get(i).set(robotRate);                                                            // set each module using the accelerated robot rate
            averagePositionChange.add(modules.get(i).getwheelPositionChange().getRotatedCW(fieldAngle.value)); // add the wheel velocity to the total sum
        }
        averagePositionChange.divide(4); // find the average position change
        averagePositionChange.scale(SwerveConstants.driveMotorInchesPerRotation); // find the average and convert to inches
        fieldDisplacement.add(averagePositionChange); // adds the distance traveled this cycle to the total distance to find the position
    }

    public void initialize()
    {
        for (int i = 0; i<4; i++)
        {
            modules.get(i).initialize();
        }
        zeroYaw();
    }

    public void zeroYaw()
    {
        navx.zeroYaw();
    }

    public Pose getFieldPose()
    {
        return new Pose(fieldDisplacement, fieldAngle);
    }
}