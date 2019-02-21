package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.BoxCommand;

/**
 *
 */
public class SquezeBox extends Subsystem {

    private DoubleSolenoid box;

	public SquezeBox() {
        box = new DoubleSolenoid(1, 4, 5);
    }

	public void in() {
        box.set(Value.kForward);
    }
    
    public void out() {
        box.set(Value.kReverse);
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new BoxCommand());
	}
}