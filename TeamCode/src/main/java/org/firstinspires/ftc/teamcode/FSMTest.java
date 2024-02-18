package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
//note -- we need to motion profile our slides, particularly given they're chunkier at the bottom than the top
@TeleOp(name = "FSMTest", group = "Tests")
@Config
public class FSMTest extends LinearOpMode{

    public enum OuttakeStates{
        START,
        LIFT,
        DEPOSIT_SETUP,
        DESPOSIT,
        DROP
    }
    public static String varName = "FSMTest";

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
    DcMotor slides;
    DcMotor PullUp1;
    DcMotor PullUp2;
    Servo outtakeFlip1;
    Servo outtakeFlip2;
    Servo outtakeRelease;
    Servo droneLauncher;
    public static int liftTime = 0;
    public static int dropTime = 0;

    boolean disableFlip = false;


    @Override
    public void runOpMode() throws InterruptedException {
        double multiplier = 1;
        RFMotor = hardwareMap.dcMotor.get("RFMotor");
        RBMotor = hardwareMap.dcMotor.get("RBMotor");
        LFMotor = hardwareMap.dcMotor.get("LFMotor");
        LBMotor = hardwareMap.dcMotor.get("LBMotor");
        IntakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");
        slides = hardwareMap.dcMotor.get("Spool");
        PullUp1 = hardwareMap.dcMotor.get("PullUpLeft");
        PullUp2 = hardwareMap.dcMotor.get("PullUpRight");
        outtakeFlip1 = hardwareMap.servo.get("OuttakeFlip1");
        outtakeFlip2 = hardwareMap.servo.get("OuttakeFlip2");
        outtakeRelease = hardwareMap.servo.get("OuttakeRelease");
        droneLauncher = hardwareMap.servo.get("droneLauncher");
        PullUp1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        PullUp2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        PullUp1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PullUp2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double tracker = hardwareMap.voltageSensor.iterator().next().getVoltage();
        //Initialize servos to required position
        outtakeFlip1.setPosition(1);
        outtakeFlip2.setPosition(0);
        outtakeRelease.setPosition(0.4);
        LBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LFMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        /*
        slides.setTargetPosition(0);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(1);
        */

        while (opModeIsActive()) {

            switch (outtakeStates) {
                //START case is the inital case, where the outake hasn't sprung out
                case START:
                    if(!disableFlip) {
                        System.out.println("not disabled.");
                        outtakeFlip1.setPosition(0.35);
                        outtakeFlip2.setPosition(0.65);
                        outtakeRelease.setPosition(0.4);
                    }


                    if (gamepad2.x) {

                        outtakeStates = OuttakeStates.LIFT;
                        disableFlip = false;
                    }
                    break;

                case LIFT:
                    // at least run this using getEncoder() and check when you hit a particular position
                    /*
                    slides.setPower(0.6);
                    sleep(liftTime);
                    */

                    if(gamepad2.x)
                        outtakeStates = OuttakeStates.DEPOSIT_SETUP;
                    if(gamepad2.y)
                        outtakeStates = OuttakeStates.START;
                //DEPOSIT_SETUP is second state where the outake gets ready to drop the pixel
                case DEPOSIT_SETUP:
                    if (!disableFlip) {
                        outtakeFlip1.setPosition(0);
                        outtakeFlip2.setPosition(1);
                    }
                    if (gamepad2.x) {
                        outtakeStates = OuttakeStates.DESPOSIT;
                    }
                    if (gamepad2.y) {
                        outtakeStates = OuttakeStates.START;
                    }
                    break;
                //DROP is the final state where the pixel is dropped
                case DESPOSIT:
                    if (!disableFlip)
                    outtakeRelease.setPosition(0);
                    if (gamepad2.x) {
                        outtakeStates = OuttakeStates.DROP;
                    }
                    if(gamepad2.y)
                        outtakeStates = OuttakeStates.START;
                    break;

                case DROP:
                    slides.setPower(-0.2);
                    sleep(dropTime);
                    if(gamepad2.x)
                        outtakeStates = OuttakeStates.START;
            }
            switch (intakeStates) {
                case START:
                        IntakeMotor.setPower(0);

                    if (gamepad2.a) {
                        intakeStates = IntakeStates.INTAKING;
                    }
                    break;
                case INTAKING:
                    //Need to tune the current value
                        IntakeMotor.setPower(0.9);
                    if (gamepad2.dpad_right) {

                        intakeStates = IntakeStates.FINISH;

                    }
                    if(gamepad2.b){
                        intakeStates = IntakeStates.START;
                    }
                    break;
                case FINISH:
                        IntakeMotor.setPower(0);

                    if (gamepad2.dpad_left) {

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



            telemetry.update();
            if(gamepad1.square)
            {
                PullUp1.setTargetPosition(-2130);
                PullUp2.setTargetPosition(2130);
                PullUp1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                PullUp2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                PullUp2.setPower(0.7);
                PullUp1.setPower(0.7);
            }
            if(gamepad1.circle)
            {
                PullUp1.setTargetPosition(-1000);
                PullUp2.setTargetPosition(1000);
            }

            if(gamepad2.dpad_up)
            {
               //slides.setTargetPosition(slides.getCurrentPosition() + 50);
                slides.setPower(1);
            }
            else if(gamepad2.dpad_down)
            {
                slides.setPower(-0.2);
            }
            else {
                slides.setPower(0.2);
            }
            if(gamepad2.left_trigger > 0.35)
            {
                disableFlip = true;
                outtakeFlip1.setPosition(0.35);
                outtakeFlip2.setPosition(0.65);
            }
            else if (gamepad2.right_trigger > 0.35)
            {
                disableFlip = true;
                outtakeFlip1.setPosition(1);
                outtakeFlip2.setPosition(0);
            }

            if(gamepad1.triangle)
            {
                multiplier = 0.5;
            }
            else if(gamepad1.cross)
            {
                multiplier = 0.25;
            }
            else
            {
                multiplier = 1;
            }

            if(gamepad1.dpad_up)
                outtakeRelease.setPosition(0);
            if(gamepad1.dpad_down)
                outtakeRelease.setPosition(0.4);
            if(gamepad1.dpad_left)
                droneLauncher.setPosition(0.72);
            LFMotor.setPower(frontLeftPower * multiplier);
            LBMotor.setPower(backLeftPower * multiplier);
            RFMotor.setPower(frontRightPower * multiplier);
            RBMotor.setPower(backRightPower * multiplier);
        }
    }
}
