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
@TeleOp(name = "FSMTest", group = "Tests")
@Config
public class FSMTest extends LinearOpMode{

    public int colorPicker(int r, int g, int b)
    {
        return r * 256 * 256 + g * 256 + b;
    }




    public static String varName = "FSMTest";


    public enum robotOuttakeStates{
        START,
        FLIP,
        DROP
    }


    robotOuttakeStates outtakeStates = robotOuttakeStates.START;
    DcMotor RFMotor;
    DcMotor RBMotor;
    DcMotor LFMotor;
    DcMotor LBMotor;
    DcMotor IntakeMotor;
    DcMotor slides;
    DcMotor PullUp1;
    DcMotor PullUp2;
    Servo outtakeFlip1;
    Servo outtakeFlip2;
    Servo outtakeRelease;
    Servo droneLauncher;



    boolean disableFlip = false;
    private ElapsedTime runtime = new ElapsedTime();
    private LynxModule cHub;
    private Collection<Blinker.Step> steps;
    String state = "None";




    @Override
    public void runOpMode() throws InterruptedException {
        double multiplier = 1;
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        steps = new ArrayList<Blinker.Step>();
        RFMotor = hardwareMap.dcMotor.get("RFMotor");
        RBMotor = hardwareMap.dcMotor.get("RBMotor");
        LFMotor = hardwareMap.dcMotor.get("LFMotor");
        LBMotor = hardwareMap.dcMotor.get("LBMotor");
        IntakeMotor = hardwareMap.dcMotor.get("Intake");
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
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
                    if(gamepad2.start){
                        outtakeStates = robotOuttakeStates.START;
                    }
                    break;
                case DROP:
                    outtakeRelease.setPosition(0);
                    if(gamepad2.start){
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
                PullUp1.setTargetPosition(-2130);
                PullUp2.setTargetPosition(2130);
                PullUp1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                PullUp2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                PullUp2.setPower(0.7);
                PullUp1.setPower(0.7);
            }
            if(gamepad1.dpad_down)
            {
                PullUp1.setTargetPosition(-1000);
                PullUp2.setTargetPosition(1000);
                PullUp2.setPower(-0.5);
                PullUp1.setPower(-0.5);
            }
            if(gamepad1.ps) {
                PullUp1.setTargetPosition(0);
                PullUp2.setTargetPosition(0);
                PullUp2.setPower(-0.5);
                PullUp1.setPower(-0.5);
            }

            if(gamepad2.a){
                IntakeMotor.setPower(0.9);
            }


            if(gamepad2.b){
                IntakeMotor.setPower(0);
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


            if(gamepad1.cross)
                droneLauncher.setPosition(0.72);

            LFMotor.setPower(frontLeftPower * multiplier);
            LBMotor.setPower(backLeftPower * multiplier);
            RFMotor.setPower(frontRightPower * multiplier);
            RBMotor.setPower(backRightPower * multiplier);
            telemetry.addData("Encoder Value Slides: ", slides.getCurrentPosition());
            telemetry.addData("Robot state:", state);
            telemetry.addData("Intake Power: ", IntakeMotor.getPower());
            telemetry.update();

        }
    }
}

