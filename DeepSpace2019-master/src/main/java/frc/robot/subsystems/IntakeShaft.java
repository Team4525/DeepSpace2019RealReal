package frc.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.IntakeShaftCommand;

/**
 *
 */
public class IntakeShaft extends Subsystem {

	private VictorSP shaft;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public IntakeShaft() {
		shaft = new VictorSP(0);//0
	}

	public void intake() {
		shaft.set(-0.80);
	}

	public void stop() {
		shaft.set(0);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new IntakeShaftCommand());
	}
}
