/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  private double startTime;
  private CANSparkMax leftMotor1;
  private CANSparkMax leftMotor2;
  private CANSparkMax rightMotor1;
  private CANSparkMax rightMotor2;
  private CANSparkMax armMotor;
  private CANSparkMax intakeMotor;

  private Joystick controller;
  private Joystick flightStick;

  @Override
  public void robotInit() {

    leftMotor1 = new CANSparkMax(1, MotorType.kBrushed);
    leftMotor2 = new CANSparkMax(2, MotorType.kBrushed);
    rightMotor1 = new CANSparkMax(3, MotorType.kBrushed);
    rightMotor2 = new CANSparkMax(4, MotorType.kBrushed);

    armMotor = new CANSparkMax(6, MotorType.kBrushless);
    armMotor.setIdleMode(IdleMode.kBrake);
    intakeMotor = new CANSparkMax(7, MotorType.kBrushed);
    
    controller = new Joystick(0);
    flightStick = new Joystick(1);

    leftMotor1.setInverted(false);
    leftMotor2.setInverted(false);

    rightMotor1.setInverted(false);
    rightMotor2.setInverted(false);

  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    double time = Timer.getFPGATimestamp();
    System.out.println(time - startTime);

    // Move forward 3 seconds
    if (time - startTime < 3) {
      leftMotor1.set(0.6);
      leftMotor2.set(0.6);
      rightMotor1.set(-0.6);
      rightMotor2.set(-0.6);
    } else {
      leftMotor1.set(0);
      leftMotor2.set(0);
      rightMotor1.set(0);
      rightMotor2.set(0);
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    // Get speeds
    double speed1 = controller.getRawAxis(1) * 0.5;
    double speed2 = controller.getRawAxis(5) * 0.5;
    double turn1 = controller.getRawAxis(0) * 0.3;
    double turn2 = controller.getRawAxis(4) * 0.3;
  
    double left = speed1 - turn1;
    double right = speed2 - turn2;

    // Move chassis motors
    leftMotor1.set(left);
    leftMotor2.set(left);
    rightMotor1.set(-right);
    rightMotor2.set(-right);

    // Intake
    if (controller.getRawButton(4) || flightStick.getRawButton(4)) {
      // In
      intakeMotor.set(0.2);
    } else if(controller.getRawButton(5) || flightStick.getRawButton(2)) {
      // Out
      intakeMotor.set(-0.2);
    } else {
      // Stop
      intakeMotor.set(0);
    }

    // Arm
    if (controller.getRawAxis(3) > 0.5 || flightStick.getPOV(0) == 0) {
      // In
      armMotor.set(0.2);
    } else if(controller.getRawAxis(2) > 0.5 || flightStick.getPOV(0) == 90) {
      // Out
      armMotor.set(-0.2);
    } else {
      // Stop
      armMotor.set(0);
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}