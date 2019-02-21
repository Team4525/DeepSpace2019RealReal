package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ClimbCommand;

/**
 *
 */
public class Climb extends Subsystem {

    private DoubleSolenoid climb;

	public Climb() {
        climb = new DoubleSolenoid(1, 0, 1);
    }

	public void in() {
        climb.set(Value.kForward);
    }
    
    public void out() {
        climb.set(Value.kReverse);
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new ClimbCommand());
	}
}