package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.PunchBottomCommand;

public class PunchBottom extends Subsystem {

    private DoubleSolenoid punch;

	public PunchBottom() {
        punch = new DoubleSolenoid(1, 6, 7);
    }

	public void in() {
        punch.set(Value.kForward);
    }
    
    public void out() {
        punch.set(Value.kReverse);
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new PunchBottomCommand());
	}
}