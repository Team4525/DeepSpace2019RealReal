package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 *
 */
public class DriveCommand extends Command {

	boolean invertPressed;
	int driveInvert;

	public DriveCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		invertPressed = false;
		driveInvert = 1;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		if (Robot.oi.drive.getX(Hand.kRight) < 0.2) {
			if (Robot.oi.drive.getBumper(Hand.kRight) && !invertPressed) {
				driveInvert = driveInvert * -1;
				invertPressed = true;
			} else if (!Robot.oi.drive.getBumper(Hand.kRight)) {
				invertPressed = false;
			}
		}
		double leftYstick = Robot.oi.drive.getY(Hand.kLeft) * -1;
		double rightXstick = Robot.oi.drive.getX(Hand.kRight);

		if (Math.abs(leftYstick) > 0.2) {
            if (leftYstick > 0.0) {
                leftYstick = (leftYstick - 0.2) / (1.0 - 0.2);
            } else {
                leftYstick = (leftYstick + 0.2) / (1.0 - 0.2);
            }
        } else {
            leftYstick = 0.0;
        }

        if (Math.abs(rightXstick) > 0.2) {
            if (rightXstick > 0.0) {
                rightXstick = (rightXstick - 0.2) / (1.0 - 0.2);
            } else {
                rightXstick = (rightXstick + 0.2) / (1.0 - 0.2);
            }
        } else {
            rightXstick = 0.0;
        }


		double targetVelocity = leftYstick * 500.0 * 4096 / 600;
		double targetTurn =  rightXstick * 500.0 * 4096 / 600;
		Robot.drive.drive(targetVelocity * driveInvert, targetTurn);
		
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
