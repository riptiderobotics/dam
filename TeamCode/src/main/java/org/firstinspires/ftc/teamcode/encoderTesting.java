package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "encoder testing for roadrunner")
public class encoderTesting extends LinearOpMode
{

    // People wired the robot bad so don't mind the inconsistent telemetry/variable names.
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
        // Center pod deadwheel
        LBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);






        waitForStart();

        while( opModeIsActive())
        {
            //Left pod is right pod
            //Center pod is left pod
            rightPodPastPos = rightPodCurrPos;

            leftPodPastPos = leftPodCurrPos;

            centerPodPastPos = centerPodCurrPos;

            sleep(3000);
            RFMotor.setPower(0);
            LFMotor.setPower(0);
            RBMotor.setPower(0);
            LBMotor.setPower(0);
            leftPodCurrPos = RFMotor.getCurrentPosition();
            centerPodCurrPos = LFMotor.getCurrentPosition();
            rightPodCurrPos = LBMotor.getCurrentPosition();
            sleep(3000);

            telemetry.addData("CenterPod positivity, value", positivity(rightPodCurrPos, rightPodPastPos) + ", " + rightPodCurrPos);
            telemetry.addData("RightPod positivity, value", positivity(leftPodCurrPos, leftPodPastPos) + ", " + leftPodCurrPos);
            telemetry.addData("LeftPod positivity, value", positivity(centerPodCurrPos, centerPodCurrPos) + ", " + centerPodCurrPos);
            telemetry.update();
        }
        

    }
}
