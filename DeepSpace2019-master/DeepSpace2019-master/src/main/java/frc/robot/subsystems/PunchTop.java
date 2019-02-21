package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.PunchTopCommand;

/**
 *
 */
public class PunchTop extends Subsystem {

    private DoubleSolenoid punch;

	public PunchTop() {
        punch = new DoubleSolenoid(0, 5, 4);
    }

	public void in() {
        punch.set(Value.kForward);
    }
    
    public void out() {
        punch.set(Value.kReverse);
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new PunchTopCommand());
	}
}