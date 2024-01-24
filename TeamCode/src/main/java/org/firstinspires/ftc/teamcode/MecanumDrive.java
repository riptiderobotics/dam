package org.firstinspires.ftc.teamcode;

//All the imported files
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Meet 3 Drive", group = "linear opmode")
public class MecanumDrive extends LinearOpMode {



    public void runOpMode() throws InterruptedException{


        DcMotor RFMotor = hardwareMap.dcMotor.get("LFMotor");
        DcMotor RBMotor = hardwareMap.dcMotor.get("RBMotor");
        DcMotor LFMotor = hardwareMap.dcMotor.get("RFMotor");
        DcMotor LBMotor = hardwareMap.dcMotor.get("LBMotor");
        Servo drone = hardwareMap.servo.get("drone");

        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();


        // Technically this is the default, however specifying it is clearer
        // Without this, data retrieving from the IMU throws an exception
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_x; // Remember, Y stick value is reversed
            double x = gamepad1.right_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.left_stick_y;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            if (gamepad1.x)
            {
                y *= 0.3;
                x *= 0.3;
                rx *= 0.3;
            }
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            LFMotor.setPower(frontLeftPower);
            LBMotor.setPower(backLeftPower);
            RFMotor.setPower(frontRightPower);
            RBMotor.setPower(backRightPower);
            if (gamepad1.a)
            {
                drone.setPosition(0);
            }
            else if(gamepad1.b)
            {
                drone.setPosition(1);
            }
        }


        }
    }

