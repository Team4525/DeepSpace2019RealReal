package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 *
 */
public class ArmsCommand extends Command {

	public ArmsCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.arms);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		System.out.println(Robot.arms.getEncoder());
		if (Robot.oi.drive.getBumper(Hand.kRight)) {
			Robot.arms.top();
		} else if (Robot.oi.drive.getBumper(Hand.kLeft)) {
			Robot.arms.bottom();
		} else if (Robot.oi.drive.getPOV() == 180) { 
			Robot.arms.climb();
		} else {
			Robot.arms.stop();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
