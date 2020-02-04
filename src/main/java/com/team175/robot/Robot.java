/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team175.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.hal.sim.PCMSim;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;


/**
 * This is a demo program showing the use of the DifferentialDrive class.
 * Runs the motors with split arcade steering and an Xbox controller.
 */
public class Robot extends TimedRobot {

    private final PCMSim pcm = new PCMSim(0);
    private final WPI_TalonSRX leftDriveSlave = new WPI_TalonSRX(1);
    private final WPI_TalonSRX leftDrive = new WPI_TalonSRX(2);
    private final WPI_TalonSRX rightDriveSlave = new WPI_TalonSRX(3);
    private final WPI_TalonSRX rightDrive = new WPI_TalonSRX(4);
    private final WPI_TalonSRX colorPizza = new WPI_TalonSRX(5);
    private final WPI_VictorSPX ballIntake = new WPI_VictorSPX(6);
    private final WPI_VictorSPX ball2 = new WPI_VictorSPX(8);
    private final WPI_VictorSPX ball3 = new WPI_VictorSPX(9);
    private final WPI_TalonSRX hook = new WPI_TalonSRX(10);
    private final WPI_TalonSRX turret = new WPI_TalonSRX(11);
    private final WPI_VictorSPX shooter = new WPI_VictorSPX(12);
    private final WPI_TalonSRX shooter2 = new WPI_TalonSRX(13);
    private final WPI_VictorSPX lift = new WPI_VictorSPX(14);
    private final WPI_VictorSPX winchClimb = new WPI_VictorSPX(15);

    private final DoubleSolenoid doubleSolenoid1 = new DoubleSolenoid(0, 1);
    private final DoubleSolenoid doubleSolenoid2 = new DoubleSolenoid(2, 3);
    private final DoubleSolenoid doubleSolenoid3 = new DoubleSolenoid(4, 5);
    private final DoubleSolenoid doubleSolenoid4 = new DoubleSolenoid(6, 7);

    private final XboxController driverController = new XboxController(0);
    private final XboxController operatorController = new XboxController(1);

    private final PigeonIMU gyro = new PigeonIMU(leftDriveSlave);
    private final DifferentialDrive robotDrive = new DifferentialDrive(leftDrive, rightDrive);

    private boolean variable01;

    boolean controlPanelStatus = false;
    boolean previousAButton = false;
    boolean currentAButton = false;

    boolean intakeStatus = false;
    boolean previousBButton = false;
    boolean currentBButton = false;

    @Override
    public void robotInit() {
        leftDriveSlave.follow(leftDrive);
        rightDriveSlave.follow(rightDrive);
    }

    @Override
    public void teleopPeriodic() {
        // Drive with split arcade drive.
        // That means that the Y axis of the left stick moves forward
        // and backward, and the X of the right stick turns left and right.
        robotDrive.arcadeDrive(driverController.getTriggerAxis(Hand.kRight) - driverController.getTriggerAxis(Hand.kLeft), driverController.getX(Hand.kRight));
        colorPizza.set(ControlMode.PercentOutput, operatorController.getXButton() ? 1.0 : 0);
        shooter.set(ControlMode.PercentOutput, operatorController.getTriggerAxis(Hand.kRight));
        shooter2.set(ControlMode.PercentOutput, operatorController.getTriggerAxis(Hand.kRight));
        ballIntake.set(ControlMode.PercentOutput, (operatorController.getBButton() ? 1.0 : 0) - operatorController.getTriggerAxis(Hand.kLeft));
        ball2.set(ControlMode.PercentOutput, (operatorController.getBButton() ? 1.0 : 0) - operatorController.getTriggerAxis(Hand.kLeft));
        ball3.set(ControlMode.PercentOutput, operatorController.getTriggerAxis(Hand.kRight));
        lift.set(ControlMode.PercentOutput, driverController.getTriggerAxis(Hand.kRight) - driverController.getTriggerAxis(Hand.kLeft));
        winchClimb.set(ControlMode.PercentOutput, driverController.getTriggerAxis(Hand.kRight) - driverController.getTriggerAxis(Hand.kLeft));
        hook.set(ControlMode.PercentOutput, driverController.getTriggerAxis(Hand.kRight) - driverController.getTriggerAxis(Hand.kLeft));
        turret.set(ControlMode.PercentOutput, getDpadValue());


        /*if (driverController.getAButton()) {
            if (variable01 == false) {
                variable01 = true;
            } else {
                variable01 = false;
            }

            if (variable01 == true) {
                doubleSolenoid1.set(DoubleSolenoid.Value.kForward);
            } else {
                doubleSolenoid1.set(DoubleSolenoid.Value.kReverse);
            }
        }*/

        previousAButton = currentAButton;
        currentAButton = driverController.getAButton();

        if (currentAButton && !previousAButton) {
            controlPanelStatus = !controlPanelStatus;
        }

        doubleSolenoid1.set(controlPanelStatus ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);

        previousBButton = currentBButton;
        currentBButton = driverController.getBButton();

        if (currentBButton && !previousBButton) {
            intakeStatus = !intakeStatus;
        }

        doubleSolenoid2.set(intakeStatus ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);


        /*if (driverController.getXButton()) {
            doubleSolenoid2.set(DoubleSolenoid.Value.kForward);
        } else {
            doubleSolenoid2.set(DoubleSolenoid.Value.kReverse);
        }

        if (driverController.getYButton()) {
            doubleSolenoid3.set(DoubleSolenoid.Value.kForward);
        } else {
            doubleSolenoid3.set(DoubleSolenoid.Value.kReverse);
        }

        if (driverController.getBButton()) {
            doubleSolenoid4.set(DoubleSolenoid.Value.kForward);
        } else {
            doubleSolenoid4.set(DoubleSolenoid.Value.kReverse);
        }*/


            System.out.println(gyro.getFusedHeading());


    }
    private double getDpadValue() {
        int pov = operatorController.getPOV();
        double demand;

        if (pov >= 90 && pov <= 180) {
            demand = 1.0;
        } else if (pov >= 270 && pov <= 360) {
            demand = -1.0;
        } else {
            demand = 0;
        }

        return demand;
    }
}
