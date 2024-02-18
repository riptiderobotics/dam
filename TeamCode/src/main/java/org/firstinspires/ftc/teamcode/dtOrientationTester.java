package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name = "test drive motors -- feb 17!")
@Config
public class dtOrientationTester extends LinearOpMode {
    public static double leftFront = 0;
    public static double rightFront = 0;
    public static double leftBack  = 0;
    public static double rightBack = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class, "LFMotor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class, "LBMotor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class, "RFMotor");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class, "RBMotor");


        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.

        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {


            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]


            frontLeftMotor.setPower(leftFront);
            backLeftMotor.setPower(leftBack);
            frontRightMotor.setPower(rightFront);
            backRightMotor.setPower(rightBack);

        }
    }
}