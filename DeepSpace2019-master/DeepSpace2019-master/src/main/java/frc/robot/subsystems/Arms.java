package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ArmsCommand;

/**
 *
 */
public class Arms extends Subsystem {

    private VictorSPX armsLeft;
    private TalonSRX armsRight;

	public Arms() {

        armsLeft = new VictorSPX(3);
        armsRight = new TalonSRX(3);
        //
        armsLeft.follow(armsRight);
        armsRight.configFactoryDefault();
        /* Factory Default all hardware to prevent unexpected behaviour */
        /* Config sensor used for Primary PID [Velocity] */
        armsRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        armsRight.setSensorPhase(true);

        /* Config the peak and nominal outputs */
        armsRight.configNominalOutputForward(0, Constants.kTimeoutMs);
        armsRight.configNominalOutputReverse(0, Constants.kTimeoutMs);
        armsRight.configPeakOutputForward(1, Constants.kTimeoutMs);
        armsRight.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* Config the Velocity closed loop gains in slot0 */
        armsRight.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
        armsRight.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
        armsRight.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
        armsRight.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
        //
        armsRight.setSelectedSensorPosition(0);
    }

	public void top() {
        armsRight.set(ControlMode.Position, -100);
    }
    
    public void bottom() {
        armsRight.set(ControlMode.Position, -200);
    }

	public void climb() {
		armsRight.set(ControlMode.Position, -300);
    }

    public void stop() {
        armsRight.set(ControlMode.PercentOutput, 0);
    }
    
    public double getEncoder() {
        return armsRight.getSelectedSensorPosition();
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ArmsCommand());
	}
}
