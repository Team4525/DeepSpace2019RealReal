package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.DriveCommand;

public class Drive extends Subsystem {
    TalonSRX leftTalon = new TalonSRX(1);
    VictorSPX leftVictor = new VictorSPX(1);
    TalonSRX rightTalon = new TalonSRX(2);
    VictorSPX rightVictor = new VictorSPX(2);

    public Drive() { 
       
        leftVictor.follow(leftTalon);
        rightVictor.follow(rightTalon);
        /* Factory Default all hardware to prevent unexpected behaviour */
        leftTalon.configFactoryDefault();

        /* Config sensor used for Primary PID [Velocity] */
        leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        leftTalon.setSensorPhase(true);

        /* Config the peak and nominal outputs */
        leftTalon.configNominalOutputForward(0, Constants.kTimeoutMs);
        leftTalon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        leftTalon.configPeakOutputForward(1, Constants.kTimeoutMs);
        leftTalon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* Config the Velocity closed loop gains in slot0 */
        leftTalon.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
        leftTalon.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
        leftTalon.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
        leftTalon.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);

        rightTalon.configFactoryDefault();

        /* Config sensor used for Primary PID [Velocity] */
        rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

        /**
         * Phase sensor accordingly. Positive Sensor Reading should match Green
         * (blinking) Leds on Talon
         */
        rightTalon.setSensorPhase(true);

        /* Config the peak and nominal outputs */
        rightTalon.configNominalOutputForward(0, Constants.kTimeoutMs);
        rightTalon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        rightTalon.configPeakOutputForward(1, Constants.kTimeoutMs);
        rightTalon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* Config the Velocity closed loop gains in slot0 */
        rightTalon.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
        rightTalon.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
        rightTalon.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
        rightTalon.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);

        leftTalon.setNeutralMode(NeutralMode.Brake);
        rightTalon.setNeutralMode(NeutralMode.Brake);
    }
    
    public void drive(double targetVelocity, double targetOffset) {
        
            /* Velocity Closed Loop */

            /**
             * Convert 500 RPM to units / 100ms. 4096 Units/Rev * 500 RPM / 600 100ms/min in
             * either direction: velocity setpoint is in units/100ms
             */
            double targetVelocity_UnitsPer100ms = targetVelocity;
            double TargetTurnVelocity = targetOffset;

            double maxInput = Math.copySign(Math.max(Math.abs(targetVelocity_UnitsPer100ms), Math.abs(TargetTurnVelocity)),targetVelocity_UnitsPer100ms);

            if (targetVelocity_UnitsPer100ms >= 0.0) {
                if (TargetTurnVelocity >= 0.0) {
                    leftTalon.set(ControlMode.Velocity, maxInput);
                    rightTalon.set(ControlMode.Velocity, -(targetVelocity_UnitsPer100ms - TargetTurnVelocity));
                } else {
                    leftTalon.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms + TargetTurnVelocity);
                    rightTalon.set(ControlMode.Velocity, -maxInput);
                }
            } else {
                if (TargetTurnVelocity >= 0.0) {
                    leftTalon.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms + TargetTurnVelocity);
                    rightTalon.set(ControlMode.Velocity, -maxInput);
                } else {
                    leftTalon.set(ControlMode.Velocity, maxInput);
                    rightTalon.set(ControlMode.Velocity, -(targetVelocity_UnitsPer100ms - TargetTurnVelocity));
                }
            }
    }

    public void initDefaultCommand() {

        setDefaultCommand(new DriveCommand());
    }

}