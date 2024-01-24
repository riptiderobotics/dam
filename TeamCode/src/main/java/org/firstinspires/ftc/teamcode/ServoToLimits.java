package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
// blah blah blah



@TeleOp(name="movement teleop", group="Linear Opmode")
// decorator so the robot knows what mode to operate in
public class ServoToLimits extends LinearOpMode {
    // Declare teleOp members.
    private ElapsedTime runtime = new ElapsedTime();
    public static int onOff = 1;
    @Override
    // related to inheritance: runOpMode is a necessary function as you NEED to override the runOpMode in the superclass
    public void runOpMode() {
        //System.out on the phones = telemetry
        DcMotor motor0 = hardwareMap.dcMotor.get("motor0");

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
            if (onOff == 0) {
                motor0.setPower(0);
            }
            else{
                motor0.setPower(0.9);
            }
        }
    }
}