import com.ctre.phoenix;
import com.ctre.phoenixpro.StatusCode;
import com.ctre.phoenixpro.configs.TalonFXConfiguration;
import com.ctre.phoenixpro.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenixpro.hardware.TalonFX;

import rev.CANSparkMax;

class SwerveModule {
    private Vector turnVector;         // vector corresponding to the way the rotation rate adds to the swerve module velocity
    private Vector moduleVelocity;     // stores this modules velocity vector
    private Angle error;
    private double wheelAngle;
    private double lastPosition = 0;
    private double currentPosition;
    private TalonFX driveMotor;
    private VelocityTorqueCurrentFOC driveMotorCTRL;
    private CANSparkMax steeringMotor;
    private CANCoder wheelEncoder;
    private Vector wheelPositionChange;

    /**
     * parameters posX and posY set the position of
     * the module relative to the center of the robot
     */
    public SwerveModule(int driveMotorID, int steeringMotorID, int wheelEncoderID, Vector position) {
        driveMotor = new TalonFX(driveMotorID, "rio");
        driveMotorCTRL = new VelocityTorqueCurrentFOC(0, 0, 1, false);

        steeringMotor = new CANSparkMax(steeringMotorID, rev::CANSparkMax::MotorType::kBrushless);
        
        wheelEncoder = new CANCoder(wheelEncoderID);

        turnVector = position;
        turnVector.divide(abs(turnVector));
        turnVector.rotateCW(90);
    }

    public void initialize() {   
        TalonFXConfiguration configs = new TalonFXConfiguration();
        /* Torque-based velocity does not require a feed forward, as torque will accelerate the rotor up to the desired velocity by itself */
        configs.Slot1.kP = 5; // An error of 1 rotation per second results in 5 amps output
        configs.Slot1.kI = 0.1; // An error of 1 rotation per second increases output by 0.1 amps every second
        configs.Slot1.kD = 0.001; // A change of 1000 rotation per second squared results in 1 amp output
        configs.TorqueCurrent.PeakForwardTorqueCurrent = parameters.ampsForRobotAccel;  // Peak output of 40 amps
        configs.TorqueCurrent.PeakReverseTorqueCurrent = -parameters.ampsForRobotAccel; // Peak output of 40 amps
        driveMotor.getConfigurator().apply(configs);
        driveMotor.setRotorPosition(0);

        steeringMotor.setInverted(true);
    }



    public Vector getModuleVector(Pose robotRate) {

        return robotRate.vector.getAdded(turnVector.getScaled(robotRate.angle.value));
    }

    public void Set(Pose robotRate) {
        wheelAngle = wheelEncoder.GetAbsolutePosition();
        moduleVelocity = getModuleVector(robotRate);
        error = new Angle(moduleVelocity.getAngle());
        error.subtract(wheelAngle);
        double frictionTorque = 1;
        double rotationRate = abs(moduleVelocity) * parameters.falconMaxRotationsPerSecond;
        if (error.getMagnitude() > 90) {
            frictionTorque = -frictionTorque;
            rotationRate = -rotationRate;
            error.add(180);
        }
        driveMotor.setControl(driveMotorCTRL.WithVelocity(rotationRate).WithFeedForward(frictionTorque));
        
        steeringMotor.Set(error.value / 180);
    
        currentPosition = driveMotor.getPosition();
        wheelPositionChange = new Vector(0, currentPosition - lastPosition);
        wheelPositionChange.rotateCW(wheelAngle);
        lastPosition = currentPosition;
    }

    public Vector getwheelPositionChange() {
        return wheelPositionChange;
    }
};