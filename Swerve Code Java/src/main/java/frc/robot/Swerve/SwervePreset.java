package frc.robot.Swerve;

import frc.robot.Constants.SwerveConstants;
import frc.robot.translations2D.Pose;

public class SwervePreset {
    public Pose targetPose = SwerveConstants.startingPose;                      // target position and angle for the SwervePoseController
    public double maxDriveRate = SwerveConstants.defaultAutoMaxDriveRate;       // max drive rate for SwervePoseController
    public double maxRotationRate = SwerveConstants.defaultAutoMaxRotationRate; // max rotation rate for SwervePoseController

    public SwervePreset(Pose targetPose, double maxDriveRate, double maxRotationRate) {
        this.targetPose = targetPose;
        this.maxDriveRate = maxDriveRate;
        this.maxRotationRate = maxRotationRate;
    }
    public SwervePreset(Pose targetPose) {
        this(targetPose, SwerveConstants.defaultAutoMaxDriveRate, SwerveConstants.defaultAutoMaxRotationRate);
    }
}
