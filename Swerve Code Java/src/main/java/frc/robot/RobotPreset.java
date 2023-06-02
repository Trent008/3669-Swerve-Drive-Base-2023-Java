package frc.robot;

import frc.robot.Swerve.SwervePreset;
import frc.robot.translations2D.Pose;

public class RobotPreset {
    public SwervePreset swervePreset = new SwervePreset(new Pose(), 0, 0); // stores variables required to drive the robot to a new pose
    public double delay;
    // include any other variables that need to be changed during the autonomous routine

    public RobotPreset(SwervePreset swervePreset, double delay) {
        this.swervePreset = swervePreset;
        this.delay = delay;
    }
}
