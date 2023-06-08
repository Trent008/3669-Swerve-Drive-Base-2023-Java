// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import java.util.ArrayList;
import frc.robot.Swerve.*;
import frc.robot.translations2D.*;
import frc.robot.TeleopControllers.*;
import frc.robot.Constants.SwerveConstants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public int i;
  public ArrayList<RobotPreset> setpoints = new ArrayList<>();
  XBOXController xboxC = new XBOXController(new Joystick(0));

  // swerve drive object to control the 4-SwerveModule array
  SwervePoseController swerveController;

  @Override
  public void robotInit() {
    setpoints.add(new RobotPreset(new SwervePreset(new Pose(new Vector(0, 0), new Angle(0))), 0));
    setpoints.add(new RobotPreset(new SwervePreset(new Pose(new Vector(0, 20), new Angle(0))), 0));
    setpoints.add(new RobotPreset(new SwervePreset(new Pose(new Vector(0, 0), new Angle(0))), 0));

    SwerveConstants.swerve.initialize();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {
    // move the swerve drive towards the next setpoint
    swerveController.setReferencePose(setpoints.get(i).swervePreset);
    // go to next setpoint if this setpoint has been reached
    if (swerveController.poseReached(3, 5) && (i < 14)) {
      i++;
    }
  }

  @Override
  public void teleopInit() {
    SwerveConstants.swerve.initialize();
  }

  @Override
  public void teleopPeriodic() {
    SwerveConstants.swerve.set(xboxC.getFieldVelocity(), false);

    if (xboxC.abxyAllTrue()) {
      SwerveConstants.swerve.zeroYaw();
    }
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
