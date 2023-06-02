package frc.robot.Swerve;

import frc.robot.Constants.SwerveConstants;
import frc.robot.translations2D.Pose;

public class SwervePoseController {
    private double positionProportional = 0.023; // rate at which to approach the current position
    private double angleProportional = 0.007;    // rate at which to approach the current angle
    private Pose poseError;              // how fast the robot needs to move to get to its next position setpoint
    private Pose swerveRate;

    public void setReferencePose(SwervePreset setpoint)
    {   
        poseError = setpoint.targetPose.getDifference(SwerveConstants.swerve.getFieldPose());
        swerveRate = poseError;
        swerveRate.scale(positionProportional, angleProportional);
        swerveRate.limit(setpoint.maxDriveRate, setpoint.maxRotationRate);
        SwerveConstants.swerve.set(swerveRate, true);
    }

    public boolean poseReached(double positionTolerance, double angleTolerance)
    {
        return (poseError.vector.getMagnitude() < positionTolerance) && (poseError.angle.getMagnitude() < angleTolerance);
    }
}
