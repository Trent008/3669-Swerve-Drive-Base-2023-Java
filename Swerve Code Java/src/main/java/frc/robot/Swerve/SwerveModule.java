package frc.robot.Swerve;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenixpro.configs.TalonFXConfiguration;
import com.ctre.phoenixpro.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenixpro.hardware.TalonFX;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.SwerveConstants;
import frc.robot.translations2D.Angle;
import frc.robot.translations2D.Pose;
import frc.robot.translations2D.Vector;

class SwerveModule {
    private Vector turnVector;         // vector corresponding to the way the rotation rate adds to the swerve module velocity
    private Vector moduleVelocity;     // stores this modules velocity vector
    private Angle error;
    private Angle wheelAngle;
    private double lastPosition = 0;
    private double currentPosition;
    private TalonFX driveMotor;
    private VelocityTorqueCurrentFOC driveMotorCTRL;
    private CANSparkMax steeringMotor;
    private SparkMaxPIDController steeringPID;
    private RelativeEncoder steeringEncoder;
    private CANCoder wheelEncoder;
    private Vector wheelPositionChange;

    /**
     * parameters posX and posY set the position of
     * the module relative to the center of the robot
     */
    public SwerveModule(int driveMotorID, int steeringMotorID, int wheelEncoderID, Vector position) {
        driveMotor = new TalonFX(driveMotorID, "rio");
        driveMotorCTRL = new VelocityTorqueCurrentFOC(0, 0, 1, false);

        steeringMotor = new CANSparkMax(steeringMotorID, MotorType.kBrushless);
        
        wheelEncoder = new CANCoder(wheelEncoderID);

        turnVector = position;
        turnVector.divide(turnVector.getMagnitude());
        turnVector.rotateCW(90);
    }

    public void initialize() {   
        TalonFXConfiguration configs = new TalonFXConfiguration();
        /* Torque-based velocity does not require a feed forward, as torque will accelerate the rotor up to the desired velocity by itself */
        configs.Slot1.kP = 5; // An error of 1 rotation per second results in 5 amps output
        configs.Slot1.kI = 0.005; // An error of 1 rotation per second increases output by 0.1 amps every second
        configs.Slot1.kD = 0.001; // A change of 1000 rotation per second squared results in 1 amp output
        configs.TorqueCurrent.PeakForwardTorqueCurrent = SwerveConstants.ampsForRobotAccel;  // Peak output of 40 amps
        configs.TorqueCurrent.PeakReverseTorqueCurrent = -SwerveConstants.ampsForRobotAccel; // Peak output of 40 amps
        driveMotor.getConfigurator().apply(configs);
        driveMotor.setRotorPosition(0);
        steeringMotor.setInverted(true);
        steeringEncoder = steeringMotor.getEncoder();
        steeringEncoder.setPositionConversionFactor(360/12.8);
        steeringPID = steeringMotor.getPIDController();
        steeringPID.setP(0.005);
        steeringPID.setOutputRange(-1, 1);
        steeringPID.setPositionPIDWrappingEnabled(true);
        steeringPID.setPositionPIDWrappingMinInput(-180);
        steeringPID.setPositionPIDWrappingMaxInput(180);
        steeringMotor.burnFlash();
    }



    public Vector getModuleVector(Pose robotRate) {

        return robotRate.vector.getAdded(turnVector.getScaled(robotRate.angle.value));
    }

    public void set(Pose robotRate) {
        wheelAngle = new Angle(wheelEncoder.getAbsolutePosition());
        // steeringEncoder.setPosition(wheelAngle.value);
        moduleVelocity = getModuleVector(robotRate);
        Angle wheelAngleSetpoint = new Angle(moduleVelocity.getAngle());
        error = wheelAngleSetpoint.getSubtracted(wheelAngle);
        double frictionTorque = 1;
        double rotationRate = moduleVelocity.getMagnitude() * SwerveConstants.falconMaxRotationsPerSecond;
        if (error.getMagnitude() > 90) {
            frictionTorque = -frictionTorque;
            rotationRate = -rotationRate;
            error.getAdded(new Angle(180));
        }
        driveMotor.setControl(driveMotorCTRL.withVelocity(rotationRate).withFeedForward(frictionTorque));
        
        steeringMotor.set(error.value/180);//steeringPID.setReference(wheelAngleSetpoint.value, CANSparkMax.ControlType.kPosition);
    
        currentPosition = driveMotor.getPosition().getValue();
        wheelPositionChange = new Vector(0, currentPosition - lastPosition);
        wheelPositionChange.rotateCW(wheelAngle.value);
        lastPosition = currentPosition;
    }

    public Vector getwheelPositionChange() {
        return wheelPositionChange;
    }
}