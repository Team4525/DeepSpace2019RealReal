package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.HatchExtenderCommand;

/**
 *
 */
public class HatchExtender extends Subsystem {

    private DoubleSolenoid ext;

	public HatchExtender() {
        ext = new DoubleSolenoid(0, 6, 7);
    }

	public void in() {
        ext.set(Value.kForward);
    }
    
    public void out() {
        ext.set(Value.kReverse);
    }

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new HatchExtenderCommand());
	}
}