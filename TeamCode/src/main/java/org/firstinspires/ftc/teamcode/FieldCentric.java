package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="GIRLS INC", group="Demos")
public class FieldCentric extends LinearOpMode {
    ElapsedTime clawPos = new ElapsedTime();

    public enum lifestate{
        OPEN,
        CLOSED
    }
    lifestate state = lifestate.OPEN;

    @Override
    public void runOpMode() throws InterruptedException {
        // clawPos.reset();
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("A");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("B");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("C");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("D");
        //DcMotor slides = hardwareMap.dcMotor.get("slides");
        //Servo servo0 = hardwareMap.servo.get("servo0");
        //Servo servo1 = hardwareMap.servo.get("servo1");
        //slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        double offset = 0;

        // Reverse thv e right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Retrieve the IMU from the hardware map
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        // Technically this is the default, however specifying it is clearer
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = 0.65 * gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -0.65 * gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -0.65 * gamepad1.left_stick_x;


            double botHeading = -imu.getAngularOrientation().firstAngle;
            // Read inverse IMU heading, as the IMU heading is CW positive
            double rotX = x * Math.cos(botHeading - offset) - y * Math.sin(botHeading - offset);
            double rotY = x * Math.sin(botHeading - offset) + y * Math.cos(botHeading - offset);
            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            telemetry.addData("y speed:", y);
            telemetry.addData("x speed:", x);
            telemetry.update();
        }
    }
}