package frc.robot;

import frc.robot.Swerve.SwerveDrive;
import frc.robot.translations2D.Angle;
import frc.robot.translations2D.Pose;
import frc.robot.translations2D.Vector;

public final class Constants {
    public static final class SwerveConstants {
        public static final double robotAccelMetersPerSecondSquared = 6; // acceleration rate of the robot pose on the field
        public static final double ampsForRobotAccel = 30;
        public static final double wheelDiameter = 3.9;
        public static final double driveMotorInchesPerRotation = (Math.PI * wheelDiameter / 6.75);
        public static final double falconMaxRotationsPerSecond = 101;
        public static final double swerveMaxRotationRate = falconMaxRotationsPerSecond * driveMotorInchesPerRotation / Math.hypot(17.75, 25) * 180 / Math.PI;
        public static final double maxRobotMetersPerSecond = falconMaxRotationsPerSecond * driveMotorInchesPerRotation * 0.0254;
        public static final double robotPercentChangePerCycle = (robotAccelMetersPerSecondSquared/maxRobotMetersPerSecond/50);
        public static final double defaultAutoMaxDriveRate = 0.2;
        public static final double defaultAutoMaxRotationRate = 0.2;

        // swerve presets
        public static final Vector startingPosition = new Vector(0, 0);
        public static final Angle startingAngle = new Angle(0);
        public static final Pose startingPose = new Pose(startingPosition, startingAngle);
        public static final SwerveDrive swerve = new SwerveDrive();
    }
}

