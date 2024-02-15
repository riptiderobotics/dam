package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
// blah blah blah



@TeleOp(name="servoTester", group="Linear Opmode")
// decorator so the robot knows what mode to operate in
public class ServoToLimits extends LinearOpMode {
    // Declare teleOp members.
    private ElapsedTime runtime = new ElapsedTime();
    public static int onOff = 1;
    Servo outtakeFlip1;
    Servo outakeFlip2;
    Servo outakeRelease;
    Servo droneLauncher;
    @Override
    // related to inheritance: runOpMode is a necessary function as you NEED to override the runOpMode in the superclass
    public void runOpMode() {
        //System.out on the phones = telemetry
        outtakeFlip1 = hardwareMap.servo.get("OuttakeFlip1");
        outakeFlip2 = hardwareMap.servo.get("OutakeFlip2");
        outakeRelease = hardwareMap.servo.get("OutakeRelease");
        droneLauncher = hardwareMap.servo.get("droneLauncher");

        // int encoderValue = 0;
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        //motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //  slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //  slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // slides.setPower(0.5);

        waitForStart();

        if (isStopRequested())
            return;

        while (opModeIsActive()) {
            if(gamepad1.a){
                outtakeFlip1.setPosition(0);
            }
            if(gamepad1.b){
                outtakeFlip1.setPosition(1);
            }
            if(gamepad1.x){
                outakeFlip2.setPosition(0);
            }
            if(gamepad1.y){
                outakeFlip2.setPosition(1);
            }
            if(gamepad1.dpad_up)
                outakeRelease.setPosition(0);
            if(gamepad1.dpad_down)
                outakeRelease.setPosition(1);
            if(gamepad1.dpad_left)
                droneLauncher.setPosition(0);
            if(gamepad1.dpad_right)
                droneLauncher.setPosition(1);
        }
    }
}