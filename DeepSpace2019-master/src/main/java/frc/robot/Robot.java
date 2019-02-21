package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.subsystems.Arms;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchExtender;
import frc.robot.subsystems.IntakeShaft;
import frc.robot.subsystems.PunchBottom;
import frc.robot.subsystems.PunchTop;
import frc.robot.subsystems.SquezeBox;

public class Robot extends TimedRobot {
  public static IntakeShaft intakeShaft = new IntakeShaft();
  public static Drive drive = new Drive();
  public static Arms arms = new Arms();
  public static Elevator elevator = new Elevator();
  public static Climb climb = new Climb();
  public static PunchTop punchTop = new PunchTop();
  public static PunchBottom punchBottom = new PunchBottom();
  public static HatchExtender hatchExtender = new HatchExtender();
  public static SquezeBox box = new SquezeBox();
  public static OI oi;
  //
  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  @Override
  public void robotInit() {
    oi = new OI();

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
