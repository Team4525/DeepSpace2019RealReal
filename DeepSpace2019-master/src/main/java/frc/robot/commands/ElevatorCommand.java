package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 *
 */
public class ElevatorCommand extends Command {

	public ElevatorCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.elevator);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		System.out.println(Robot.elevator.getEncoder());
		if (Robot.oi.mech.getPOV() == 0){
			Robot.elevator.moveUp();
		} else if (Robot.oi.mech.getPOV() == 180){
			Robot.elevator.moveDown();
		} else{
			Robot.elevator.stop();
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
