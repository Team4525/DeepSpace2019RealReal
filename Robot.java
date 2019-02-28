/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.CrossLine;
import frc.robot.auto.FarHatch;
import frc.robot.commands.*;

public class Robot extends TimedRobot {

  // Controllers
  XboxController driver;
  XboxController mech;

  // Motors
  // Drive
  public static TalonSRX leftDrive1;
  public static VictorSPX leftDrive2;
  public static TalonSRX rightDrive1;
  public static VictorSPX rightDrive2;

  // ARM
  TalonSRX armRight;
  VictorSPX armLeft;

  // Elevator
  Elevator elevator;

  // Cargo intake
  VictorSP cargoIntake;

  // Solenoids
  // Climb
  Solenoid climbUp;
  Solenoid climbDown;

  // Hatch Extender
  Solenoid hatchExtendOut;
  Solenoid hatchExtendIn;

  // Hatch Punchers
  Solenoid hatchPunchTopOut;
  Solenoid hatchPunchTopIn;

  Solenoid hatchPunchBottomOut;
  Solenoid hatchPunchBottomIn;

  // Box
  Solenoid boxClose;
  Solenoid boxOpen;

  // Constants
  // Elevator PID
  double elevatorP = 0;
  double elevatorI = 0;
  double elevatorD = 0;
  double elevatorF = 0;

  // Elevator Heights
  double elevatorTop = 0;
  double elevatorBottom = -200; // copied from old code
  double elevatorClimb = -300;

  // ARM PID
  double armP = 0;
  double armI = 0;
  double armD = 0;
  double armF = 0;

  // Arm Heights
  double armTop = -100;
  double armBottom = -200;
  double armClimb = -300;

  // Punch timer code
  long timeStartedPunching;
  long timeToPunch = 2000; // 2000ms or 2 seconds
  long timeSincePunched;
  boolean startedPunching = false;

  // Auto timing code
  long timeSinceAutoStarted;
  long autoStartTime;

  // Sensors
  DigitalInput photoSensor;
  Ultrasonic sonic;
  public static ADXRS450_Gyro gyro;

  // auto stuff
  Controller auto;
  CommandManager commands;
  TalonSRXConfiguration _config = new TalonSRXConfiguration();

  // Climber sequence code
  long timeStartedClimb;
  long timeSinceClimbStarted;
  boolean startedClimb = false;



  @Override
  public void robotInit() {

    // initialize everything to the correct ports
    this.driver = new XboxController(0);
    this.mech = new XboxController(1);

    // CAN Stuff
    this.leftDrive1 = new TalonSRX(5);
    this.leftDrive2 = new VictorSPX(1);
    this.rightDrive1 = new TalonSRX(2);
    this.rightDrive2 = new VictorSPX(6);

    this.armRight = new TalonSRX(7);
    this.armLeft = new VictorSPX(3);

    elevator = new Elevator();

    // RIO PWM Stuff
    this.cargoIntake = new VictorSP(0); // this is okay because its pwm

    // Solenoid stuff
    this.climbDown = new Solenoid(0, 2); // in PCM 1 port 0
    this.climbUp = new Solenoid(0, 3); // PCM 1 port 1

    this.hatchPunchTopIn = new Solenoid(0, 4); // PCM 0 port 4
    this.hatchPunchTopOut = new Solenoid(0, 5); // PCM 0 port 5

    this.hatchExtendIn = new Solenoid(1, 0); // PCM 0 port 6
    this.hatchExtendOut = new Solenoid(1, 1); // PCM 0 port 7

    this.boxClose = new Solenoid(1, 2); // PCM 1 port 4
    this.boxOpen = new Solenoid(1, 3); // PCM 1 port 5

    this.hatchPunchBottomIn = new Solenoid(0, 7); // PCM 1 port 6
    this.hatchPunchBottomOut = new Solenoid(0, 6); // PCM 1 port 74

    photoSensor = new DigitalInput(0);
    sonic = new Ultrasonic(1, 2);
    gyro = new ADXRS450_Gyro();
    gyro.calibrate();
    gyro.reset();

    configureSpeedControllers();

    commands = CommandManager.getInstance();

  }

  public void configureSpeedControllers() {
    // Set the drive victors to follow their talons
    this.leftDrive2.follow(this.leftDrive1);
    this.rightDrive2.follow(this.rightDrive1);
    _config.primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative;
    _config.neutralDeadband = 0.001; /* 0.1 % super small for best low-speed control */
    _config.slot0.kF = 1023.0/6800.0;
    _config.slot0.kP = 1.0;
    _config.slot0.kI = 0.0;
    _config.slot0.kD = 0.0;
    _config.slot0.integralZone = (int) 400;
    _config.slot0.closedLoopPeakOutput = 1.00;
    // _config.slot0.allowableClosedloopError // left default for this example
    // _config.slot0.maxIntegralAccumulator; // left default for this example
    // _config.slot0.closedLoopPeriod; // left default for this example
    leftDrive1.configAllSettings(_config);
    rightDrive1.configAllSettings(_config);

    leftDrive1.setSensorPhase(true);
    leftDrive1.setInverted(false);

    rightDrive1.setInverted(false);
    rightDrive1.setSensorPhase(true); 

    this.armLeft.follow(this.armRight);

    // configure the arms pid constants
    this.armRight.selectProfileSlot(0, 0);
    this.armRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 20);
    this.armRight.setSensorPhase(true); // make false if encoder is backwards
    this.armRight.config_kP(0, 0.49951172, 30);
    this.armRight.config_kI(0, 0, 30);
    this.armRight.config_kD(0, 49.9511719, 30);
    this.armRight.config_kF(0, 0, 30);
    this.armRight.setSelectedSensorPosition(0); // reset the encoder

  }

  @Override
  public void autonomousInit() {
  //  this.autoStartTime = System.currentTimeMillis();
    auto = new FarHatch();
    auto.start();
    commands.start();
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() { // Main teleop loop

    // Calculate Drive Output
    double x = this.driver.getY(Hand.kLeft);
    double y = this.driver.getX(Hand.kRight);

    this.setDrive(y + x, y - x);
    System.out.println(gyro.getAngle());
    SmartDashboard.putNumber("Encoder", leftDrive1.getSelectedSensorPosition());

    if(!this.startedClimb){
      this.setDrive(y + x, y - x);

    }


    // Cargo intake code
    double output = this.driver.getTriggerAxis(Hand.kRight);
    if(!this.startedClimb){  //only do this when not climbing
      if (output > 0.2) {
        this.cargoIntake.set(1.0); // change to output for variable speed
      } else {
        this.cargoIntake.set(0);
      }
  }

    // Hatch intake code

    // extender
    boolean b = false;
	if (this.mech.getBButton()) { // only extend when b is pressed
      this.setHatchExtender(true);
    } else {
      this.setHatchExtender(b);
    }

    // box
    if (this.mech.getAButton()) { // toggle closed
      this.setBox(true);
    } else if (this.mech.getYButton()) { // toggle to open
      this.setBox(b);
    }

    // Puncher
    if (this.mech.getTriggerAxis(Hand.kLeft) > 0.2) { // we want to punch
      setPunchBottom(true);

    } else {
      setPunchBottom(b);
    }

    if (this.mech.getTriggerAxis(Hand.kRight) > 0.2) { // we want to punch
      setPunchTop(true);

    } else {
      setPunchTop(b);
    }
    // elevator code.
    // Basic manual control
    // System.out.println(elevator.getEncoder());
    if (!this.mech.getBackButton()) {
      if (this.mech.getPOV() == 0) {
        elevator.top();
      } else if (this.mech.getPOV() == 180) {
        elevator.bottom();
      } else if (this.mech.getPOV() == 90) {
        elevator.middle();
      } else {
        elevator.stop();
      }
    } else if (this.mech.getBackButton()) {
      if (this.mech.getPOV() == 0) {
        elevator.moveUp();
        ;
      } else if (this.mech.getPOV() == 180) {
        elevator.moveDown();
        ;
      } else if (this.mech.getPOV() == 90) {

      } else {
        elevator.stop();
      }

      // Arm code
      // sets pid target based on which button is pressed

      //we added this stuff
      if (this.driver.getBumper(Hand.kRight)) {
        this.armRight.set(ControlMode.Position, 50000);
      } else if (this.driver.getBumper(Hand.kLeft)) {
        this.armRight.set(ControlMode.Position, 10000);
      } else {
        this.armRight.set(ControlMode.PercentOutput, 0);
      }

    if(!this.startedClimb){
      if (driver.getBumper(Hand.kRight)) {
        this.setArmPID(this.armTop);
      } else if (driver.getBumper(Hand.kLeft)) {
        this.setArmPID(this.armBottom);
      } else if (driver.getPOV() == 100) {
        this.setArmPID(this.armClimb);
      } else {
        this.setArmManual(0.0);
      }
    }

      // Climber code
      // we added this stuff too
      if (this.mech.getStartButton()) {
        this.setClimber(b);
      } else {
        this.setClimber(true);
      }
    }
  
      if(this.mech.getStartedButton()){
        if(!this.startedClimb) { //if we weren't climbing before
         this.timeStartedClimb = System.currentTimeMillis(); //set timer
         this.startClimb = true;
       }
      } else {
       this.startedClimb = false;

         }

  @Override
  public void disabledInit() {

  }

  @Override
  public void disabledPeriodic() {

  }

  // Functions to make our lives easier

  // Drive
  public void setDrive(double leftOut, double rightOut) {
    this.leftDrive1.set(ControlMode.PercentOutput, leftOut);
    this.rightDrive1.set(ControlMode.PercentOutput, rightOut);
  }

  // Auto
  public void setAutoDrive(double left, double right, double distance) {

  }

  // Arm
  public void setArmManual(double output) {
    this.armRight.set(ControlMode.PercentOutput, output); // not using PID
  }

  public void setArmPID(double targetValue) { // will use pid to get to the target
    this.armRight.set(ControlMode.Position, targetValue);
  }

  public double getArmPosition() { // returns the arm height in ticks
    return this.armRight.getSelectedSensorPosition();
  }

  public void setPunchTop(boolean out) { // true for out, false for in
    this.hatchPunchTopIn.set(!out);
    this.hatchPunchTopOut.set(out);
  }

  public void setPunchBottom(boolean out) { // true for out, false for in
    this.hatchPunchBottomIn.set(!out);
    this.hatchPunchBottomOut.set(out);
  }

  public void setHatchExtender(boolean out) {
    this.hatchExtendIn.set(!out);
    this.hatchExtendOut.set(out);
  }

  public void setBox(boolean closed) {
    this.boxClose.set(closed);
    this.boxOpen.set(!closed);
  }

  public void setClimber(boolean up) {
    this.climbUp.set(up);
    this.climbDown.set(!up);
  }

  public static void drive(double power, double off) {
    double l;
    double r;
    if (off > 0) {
      if (power < 0) { // We want to go left
        l = off - power; // powerset the left drive motors
        r = Math.max(off, power); // Maximize off on the right drive
                                  // motors
      } else {
        l = Math.max(off, -power);
        r = off + power; // could overflow 1
      }
    } else {
      if (power >= 0) {
        l = -Math.max(-off, power);
        r = off + power;
      } else {
        l = off - power;
        r = -Math.max(-off, -power);
      }
    }
    leftDrive1.set(ControlMode.PercentOutput, l);
    rightDrive1.set(ControlMode.PercentOutput, r);
  }

    if (this.startedClimb) {
      this.timeSinceClimbStarted = System.currentTimeMillis() - this.timeStartedClimb;
      if (this.time)

  }

}