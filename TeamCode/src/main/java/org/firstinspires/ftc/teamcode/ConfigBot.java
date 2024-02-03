package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Testing Config", group = "cool stuff")
public class ConfigBot extends LinearOpMode{
    public void runOpMode() throws InterruptedException{

        DcMotor LFMotor = hardwareMap.dcMotor.get("LFMotor");
        DcMotor LBMotor = hardwareMap.dcMotor.get("LBMotor");
        DcMotor RFMotor = hardwareMap.dcMotor.get("RFMotor");
        DcMotor RBMotor = hardwareMap.dcMotor.get("RBMotor");
        DcMotor Intake = hardwareMap.dcMotor.get("Intake");

    waitForStart();
    while (opModeIsActive()){
        if(gamepad1.dpad_up){
            LFMotor.setPower(0.5);
        }
        else
            LFMotor.setPower(0);

        if (gamepad1.dpad_down)
            LBMotor.setPower(0.5);
        else
            LBMotor.setPower(0);

        //replace LB and RF
        //replace LF with RB
        //replace RF with LF
        //replace LB with RB
        //replace intake with spool

        if(gamepad1.dpad_left)
            RFMotor.setPower(0.5);
        else
            RFMotor.setPower(0);

        if (gamepad1.dpad_right)
            RBMotor.setPower(0.5);
        else
            RBMotor.setPower(0);


        if (gamepad1.b)
            Intake.setPower(0.5);
        else
            Intake.setPower(0);
    }

    }
}
