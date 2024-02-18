package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
// blah blah blah



@Config
@TeleOp(name="ILT bot tester", group="Linear Opmode")
// decorator so the robot knows what mode to operate in
public class ServoToLimits extends LinearOpMode {
    // Declare teleOp members.
    private ElapsedTime runtime = new ElapsedTime();
    public static double pullPower = 0;
    public static double outtakePower = 0;

    public static double doorPos = 0;
    public static double dlPos = 0;

    public static double outtakeFlipper = 0;
    public static double intakePower = 0;


    Servo outtakeFlip1;
    Servo outtakeFlip2;
    Servo outtakeRelease;
    Servo droneLauncher;

    DcMotor pullup0;
    DcMotor pullup1;

    DcMotor slides;

    DcMotor intake;

    @Override
    // related to inheritance: runOpMode is a necessary function as you NEED to override the runOpMode in the superclass
    public void runOpMode() {

        outtakeFlip1 = hardwareMap.servo.get("OuttakeFlip1");
        outtakeFlip2 = hardwareMap.servo.get("OuttakeFlip2");
        intake = hardwareMap.dcMotor.get("Intake");
        pullup0 = hardwareMap.dcMotor.get("PullUpLeft");
        pullup1 = hardwareMap.dcMotor.get("PullUpRight");
        droneLauncher = hardwareMap.servo.get("droneLauncher");
        slides = hardwareMap.dcMotor.get("Spool");
        outtakeRelease = hardwareMap.servo.get("OuttakeRelease");




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

            droneLauncher.setPosition(dlPos);
            outtakeFlip1.setPosition(1 - outtakeFlipper);
            outtakeFlip2.setPosition(outtakeFlipper);
            pullup0.setPower(-pullPower);
            pullup1.setPower(pullPower);
            slides.setPower(outtakePower);
            intake.setPower(intakePower);
            outtakeRelease.setPosition(doorPos);
        }
    }
}