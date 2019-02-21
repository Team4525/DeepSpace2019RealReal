package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ElevatorCommand;

/**
 *
 */
public class Elevator extends Subsystem {

    private TalonSRX elevator;

	public Elevator() {
        elevator = new TalonSRX(4);
        //
        elevator.configFactoryDefault();
        /* Factory Default all hardware to prevent unexpected behaviour */
        /* Config sensor used for Primary PID [Velocity] */
        elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        elevator.setSensorPhase(true);

        /* Config the peak and nominal outputs */
        elevator.configNominalOutputForward(0, Constants.kTimeoutMs);
        elevator.configNominalOutputReverse(0, Constants.kTimeoutMs);
        elevator.configPeakOutputForward(1, Constants.kTimeoutMs);
        elevator.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* Config the Velocity closed loop gains in slot0 */
        elevator.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);
        elevator.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
        elevator.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
        elevator.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
        //
        elevator.setSelectedSensorPosition(0);
    }

	public void top() {
        elevator.set(ControlMode.Position, 0);
    }
    
    public void bottom() {
        elevator.set(ControlMode.Position, -200);
    }

	public void climb() {
		elevator.set(ControlMode.Position, -300);
    }

    public void moveUp() {
        elevator.set(ControlMode.PercentOutput, 0.1);
    }

    public void moveDown() {
        elevator.set(ControlMode.PercentOutput, -0.1);
    }

    public void stop() {
        elevator.set(ControlMode.PercentOutput, 0);
    }
    
    public double getEncoder() {
        return elevator.getSelectedSensorPosition();
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ElevatorCommand());
	}
}
