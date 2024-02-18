package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "encoder testing for roadrunner")
public class encoderTesting extends LinearOpMode
{
    DcMotor RFMotor;
    DcMotor RBMotor;

    DcMotor LFMotor;
    DcMotor LBMotor;

    double rightPodCurrPos = 0;
    double rightPodPastPos;
    double leftPodCurrPos = 0;
    double leftPodPastPos;
    double centerPodCurrPos = 0;
    double centerPodPastPos;

    String message;

    private String positivity(double currPos, double pastPos)
    {
        if (currPos < pastPos)
        {
            message = "Negative";
        }
        else if(currPos > pastPos)
        {
            message = "Positive";
        }

        return message;
    }
    @Override
    public void runOpMode()
    {

        RFMotor = hardwareMap.dcMotor.get("RFMotor");
        RBMotor = hardwareMap.dcMotor.get("RBMotor");
        LFMotor = hardwareMap.dcMotor.get("LFMotor");
        LBMotor = hardwareMap.dcMotor.get("LBMotor");
        LBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RFMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        RBMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // Right pod deadwheel
        RFMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Left pod deadwheel
        LFMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // center pod deadwheel
        LBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);






        waitForStart();

        while( opModeIsActive())
        {
            rightPodPastPos = rightPodCurrPos;

            leftPodPastPos = leftPodCurrPos;

            centerPodPastPos = centerPodCurrPos;
            RFMotor.setPower(0.3);
            LFMotor.setPower(0.3);
            RBMotor.setPower(0.3);
            LBMotor.setPower(0.3);

            sleep(3000);
            RFMotor.setPower(0);
            LFMotor.setPower(0);
            RBMotor.setPower(0);
            LBMotor.setPower(0);
            leftPodCurrPos = RFMotor.getCurrentPosition();
            centerPodCurrPos = RFMotor.getCurrentPosition();
            rightPodCurrPos = RFMotor.getCurrentPosition();
            sleep(3000);

            telemetry.addData("RightPod positivity, value", positivity(rightPodCurrPos, rightPodPastPos) + ", " + rightPodCurrPos);
            telemetry.addData("LeftPod positivity, value", positivity(leftPodCurrPos, leftPodPastPos) + ", " + leftPodCurrPos);
            telemetry.addData("CenterPod positivity, value", positivity(centerPodCurrPos, rightPodPastPos) + ", " + rightPodCurrPos);
            telemetry.update();
        }
        

    }
}
