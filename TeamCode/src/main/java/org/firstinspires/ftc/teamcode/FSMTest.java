package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name = "FSMTest", group = "Tests")
public class FSMTest extends LinearOpMode{

    public enum OuttakeStates{
        START,
        DEPOSIT_SETUP,
        DROP
    }

    public enum IntakeStates{
        START,
        INTAKING,
        FINISH
    }
    OuttakeStates outtakeStates = OuttakeStates.START;
    IntakeStates intakeStates = IntakeStates.START;
    DcMotor RFMotor;
    DcMotor RBMotor;
    DcMotor LFMotor;
    DcMotor LBMotor;
    DcMotorEx IntakeMotor;
    DcMotor SpoolMotor;
    DcMotor PullUp1;
    DcMotor PullUp2;
    Servo outtakeFlip1;
    Servo outakeFlip2;
    Servo outakeRelease;
    Servo droneLauncher;

    @Override
    public void runOpMode() throws InterruptedException {
        RFMotor = hardwareMap.dcMotor.get("RFMotor");
        RBMotor = hardwareMap.dcMotor.get("RBMotor");
        LFMotor = hardwareMap.dcMotor.get("LFMotor");
        LBMotor = hardwareMap.dcMotor.get("LBMotor");
        IntakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");
        SpoolMotor = hardwareMap.dcMotor.get("Spool");
        PullUp1 = hardwareMap.dcMotor.get("PullUpLeft");
        PullUp2 = hardwareMap.dcMotor.get("PullUpRight");
        outtakeFlip1 = hardwareMap.servo.get("OuttakeFlip1");
        outakeFlip2 = hardwareMap.servo.get("OutakeFlip2");
        outakeRelease = hardwareMap.servo.get("OutakeRelease");
        droneLauncher = hardwareMap.servo.get("droneLauncher");

        double tracker = hardwareMap.voltageSensor.iterator().next().getVoltage();
        //Initialize servos to required position
        outtakeFlip1.setPosition(1);
        outakeFlip2.setPosition(1);
        outakeRelease.setPosition(1);
        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LFMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            switch (outtakeStates) {
                //START case is the inital case, where the outake hasn't sprung out
                case START:
                    outtakeFlip1.setPosition(1);
                    outakeFlip2.setPosition(1);
                    outakeRelease.setPosition(1);
                    if (gamepad2.x) {

                        outtakeStates = OuttakeStates.DEPOSIT_SETUP;
                    }
                    break;
                //DEPOSIT_SETUP is second state where the outake gets ready to drop the pixel
                case DEPOSIT_SETUP:
                    outtakeFlip1.setPosition(0);
                    outakeFlip2.setPosition(0);
                    if (gamepad2.x) {
                        outtakeStates = OuttakeStates.DROP;
                    }
                    if (gamepad2.y) {
                        outtakeStates = OuttakeStates.START;
                    }
                    break;
                //DROP is the final state where the pixel is dropped
                case DROP:
                    outakeRelease.setPosition(0);
                    if (gamepad2.x) {
                        outtakeStates = OuttakeStates.START;
                    }
                    break;
            }
            switch (intakeStates) {
                case START:
                    IntakeMotor.setPower(0.5);
                    if (gamepad2.a) {
                        intakeStates = IntakeStates.INTAKING;
                    }
                    break;
                case INTAKING:
                    //Need to tune the current value
                    IntakeMotor.setPower(0.5);
                    if (gamepad2.a) {

                        intakeStates = IntakeStates.FINISH;

                    }
                    if(gamepad2.b){
                        intakeStates = IntakeStates.START;
                    }
                    break;
                case FINISH:

                    if (gamepad2.a) {
                        IntakeMotor.setPower(0);
                        intakeStates = IntakeStates.START;
                    }
                    break;
            }

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            LFMotor.setPower(frontLeftPower);
            LBMotor.setPower(backLeftPower);
            RFMotor.setPower(frontRightPower);
            RBMotor.setPower(backRightPower);
        }
    }
}
