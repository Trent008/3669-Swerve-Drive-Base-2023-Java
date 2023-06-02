package frc.robot.TeleopControllers;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.translations2D.Pose;
import frc.robot.translations2D.Vector;
import frc.robot.translations2D.Angle;

public class XBOXController {
    private Joystick joy;
    private double nominalSpeed = 0.5;
    private double nominalRotationRate = 0.3;
    private double speed = 0.8;
    private double rotationSpeed = 0.4;
    private double deadband = 0.03;
    private double a;

    public void applyDeadband()
    {
        if (Math.abs(a) < deadband)
        {
            a = 0;
        }
    }

    public XBOXController(Joystick joy)
    {
        this.joy = joy;
    }

    private double getSpeed()
    {
        return nominalSpeed + joy.getRawAxis(3) * (speed - nominalSpeed);
    }

    private double getRotationSpeed()
    {
        return nominalRotationRate + joy.getRawAxis(3) * (rotationSpeed - nominalRotationRate);
    }

    private double getLX()
    {
        a = getSpeed() * joy.getRawAxis(0);
        applyDeadband();
        return a;
    }

    private double getLY()
    {
        a = getSpeed() * -joy.getRawAxis(1);
        applyDeadband();
        return a;
    }

    // get Z Rotate axis
    private double getRX()
    {
        a = getRotationSpeed() * joy.getRawAxis(4);
        applyDeadband();
        return a;
    }

    public double getRY()
    {
        a = getSpeed() * -joy.getRawAxis(5);
        applyDeadband();
        return a;
    }

    public Pose getFieldVelocity()
    {
        return new Pose(new Vector(getLX(), getLY()), new Angle(getRX()));
    }

    public boolean abxyAllTrue()
    {
        return joy.getRawButton(1) && joy.getRawButton(2) && joy.getRawButton(3) && joy.getRawButton(4);
    }
}
