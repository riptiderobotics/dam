package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//note -- we need to motion profile our slides, particularly given they're chunkier at the bottom than the top
@TeleOp(name = "Outreach Event Code", group = "Tests")
@Config
public class OutreachEventProgram extends LinearOpMode{




    public enum robotOuttakeStates{
        START,
        FLIP,
        DROP
    }


    robotOuttakeStates outtakeStates = robotOuttakeStates.START;
    DcMotor rightFrontWheel;
    DcMotor rightBackWheel;
    DcMotor leftFrontWheel;
    DcMotor leftBackWheel;
    DcMotorEx intake;
    DcMotor slides;
    DcMotor pullUpLeft;
    DcMotor pullUpRight;
    Servo outtakeFlip1;
    Servo outtakeFlip2;
    Servo outtakeRelease;
    Servo droneLauncher;

    private ElapsedTime runtime = new ElapsedTime();
    private LynxModule cHub;
    private Collection<Blinker.Step> steps;

    public static double droneLaunchPostion = 0;




    @Override
    public void runOpMode() throws InterruptedException {
        double multiplier = 0.4;
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        steps = new ArrayList<Blinker.Step>();
        rightFrontWheel = hardwareMap.dcMotor.get("RFMotor");
        rightBackWheel = hardwareMap.dcMotor.get("RBMotor");
        leftFrontWheel = hardwareMap.dcMotor.get("LFMotor");
        leftBackWheel = hardwareMap.dcMotor.get("LBMotor");
        intake = hardwareMap.get(DcMotorEx.class, "Intake");
        slides = hardwareMap.dcMotor.get("Spool");
        pullUpLeft = hardwareMap.dcMotor.get("PullUpLeft");
        pullUpRight = hardwareMap.dcMotor.get("PullUpRight");
        outtakeFlip1 = hardwareMap.servo.get("OuttakeFlip1");
        outtakeFlip2 = hardwareMap.servo.get("OuttakeFlip2");
        outtakeRelease = hardwareMap.servo.get("OuttakeRelease");
        droneLauncher = hardwareMap.servo.get("droneLauncher");
        pullUpLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pullUpRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pullUpLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pullUpRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double tracker = hardwareMap.voltageSensor.iterator().next().getVoltage();
        //Initialize servos to required position
        outtakeFlip1.setPosition(1);
        outtakeFlip2.setPosition(0);
        outtakeRelease.setPosition(0.4);
        leftBackWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        droneLauncher.setPosition(0.72);



        waitForStart();


        // For slides, negative power is positive, and viceversa.
        while (opModeIsActive()) {




            switch (outtakeStates) {
                case START:
                    outtakeFlip1.setPosition(1);
                    outtakeFlip2.setPosition(0);
                    outtakeRelease.setPosition(0.29);
                    if(gamepad2.x){
                        outtakeStates = robotOuttakeStates.FLIP;
                    }
                    break;
                case FLIP:
                    outtakeFlip1.setPosition(0.35);
                    outtakeFlip2.setPosition(0.65);
                    if(gamepad2.y){
                        outtakeStates = robotOuttakeStates.DROP;
                    }
                    if(gamepad2.options){
                        outtakeStates = robotOuttakeStates.START;
                    }
                    break;
                case DROP:
                    outtakeRelease.setPosition(0);
                    if(gamepad2.options){
                        outtakeStates = robotOuttakeStates.START;
                    }
                    break;

            }

            if(gamepad1.left_stick_y > 0.5)
            {

                for (LynxModule hub : allHubs) {
                    hub.setPattern(steps);
                }
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






            if(gamepad1.dpad_up)
            {
                pullUpLeft.setTargetPosition(-2130);
                pullUpRight.setTargetPosition(2130);
                pullUpLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                pullUpRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                pullUpRight.setPower(0.7);
                pullUpLeft.setPower(0.7);
            }
            if(gamepad1.dpad_down)
            {
                pullUpLeft.setTargetPosition(-1000);
                pullUpRight.setTargetPosition(1000);
                pullUpRight.setPower(-0.5);
                pullUpLeft.setPower(-0.5);
            }
            if(gamepad1.back)
            {
                pullUpLeft.setTargetPosition(0);
                pullUpRight.setTargetPosition(0);
                pullUpRight.setPower(-0.5);
                pullUpLeft.setPower(-0.5);
            }

            if(gamepad2.a){
                intake.setPower(0.9);
            }
            else{
                intake.setPower(0);
            }
            if(gamepad2.b){
                intake.setPower(-0.9);
            }
            else{
                intake.setPower(0);
            }

            if(gamepad1.left_trigger > 0.2){
                multiplier = 0.5;
            }
            if(gamepad1.left_trigger > 0.2){
                multiplier = 0.2;
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


            if(gamepad1.a) {
                droneLauncher.setPosition(0.5);
            }

            leftFrontWheel.setPower(frontLeftPower * multiplier);
            leftBackWheel.setPower(backLeftPower * multiplier);
            rightFrontWheel.setPower(frontRightPower * multiplier);
            rightBackWheel.setPower(backRightPower * multiplier);
            telemetry.addData("Encoder Value Slides: ", slides.getCurrentPosition());
            telemetry.update();

        }
    }
}

