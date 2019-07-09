/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.PowerDrive;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

  /**
   * Testing encoder values for 2 seconds at 0.5 speed Going about 83 inches
   * forward <br/>
   * 2133 right <br/>
   * 2110 left <br/>
   * 2122 average <br/>
   * 25.57 ticks per inch <br/>
   * <br/>
   * 6 seconds at 0.5 speed <br/>
   * Going 255 inches <br/>
   * 6562 right <br/>
   * 6500 left <br/>
   * 6531 average <br/>
   * 25.61 ticks per inch <br/>
   * <br/>
   * Same <br/>
   * Drifted to the left <br/>
   * 255 inches <br/>
   * 6587 right <br/>
   * 6483 left <br/>
   * 6535 average <br/>
   * 25.62 ticks per inch <br/>
   * 25.6 ticks per inch average of all 3 tests <br/>
   * 
   * DistanceDrive for 240 inches, went 244 inches
   * 6281 right
   * 6209 left
   */

public class Drive extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  static public final double TICKS_PER_INCH = 25.6;

  final public WPI_TalonSRX rightMaster = new WPI_TalonSRX(RobotMap.rightMaster),
      rightSlave1 = new WPI_TalonSRX(RobotMap.rightSlave1), rightSlave2 = new WPI_TalonSRX(RobotMap.rightSlave2),
      leftMaster = new WPI_TalonSRX(RobotMap.leftMaster), leftSlave1 = new WPI_TalonSRX(RobotMap.leftSlave1),
      leftSlave2 = new WPI_TalonSRX(RobotMap.leftSlave2);

  public Drive() {
    super();
    // constructs and configures all six drive motors
    // restore everything to known factory default state
    rightMaster.configFactoryDefault();
    rightSlave1.configFactoryDefault();
    rightSlave2.configFactoryDefault();
    leftMaster.configFactoryDefault();
    leftSlave1.configFactoryDefault();
    leftSlave2.configFactoryDefault();
    // now configure them
    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    rightSlave1.setInverted(InvertType.FollowMaster);
    rightSlave2.setInverted(InvertType.FollowMaster);
    leftSlave1.setInverted(InvertType.FollowMaster);
    leftSlave2.setInverted(InvertType.FollowMaster);
    setNeutralMode(NeutralMode.Brake);
    rightMaster.setInverted(InvertType.InvertMotorOutput);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new PowerDrive());
  }

  public void setNeutralMode(NeutralMode mode) {
    // method to easily set the neutral mode of all of the driveTrain motors
    rightMaster.setNeutralMode(mode);
    rightSlave1.setNeutralMode(mode);
    rightSlave2.setNeutralMode(mode);
    leftMaster.setNeutralMode(mode);
    leftSlave1.setNeutralMode(mode);
    leftSlave2.setNeutralMode(mode);
  }

  // 2 commands to simply set the power of the left and right side of the robot
  public void setPower(double rightPower, double leftPower) {
    rightMaster.set(rightPower);
    leftMaster.set(leftPower);
  }

  public void setPower(double power) {
    rightMaster.set(power);
    leftMaster.set(power);
  }

  // Drive the robot using a joystick
  public void setPowerArcade(double forward, double turn) {
    double max = Math.abs(forward) + Math.abs(turn);
    double scale = (max <= 1.0) ? 1.0 : (1.0 / max);
    rightMaster.set(scale * (forward + turn));
    leftMaster.set(scale * (forward - turn));
  }

  public double getRightPosition() {
    return rightMaster.getSelectedSensorPosition();
  }

  public double getLeftPosition() {
    return leftMaster.getSelectedSensorPosition();
  }

  public void resetEncoders() {
    rightMaster.setSelectedSensorPosition(0, 0, 10);
    leftMaster.setSelectedSensorPosition(0, 0, 10);
  }
}
