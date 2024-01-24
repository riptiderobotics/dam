package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "test", group = "linear opmode")
public class TurnOn extends LinearOpMode{

    public void runOpMode() {
        DcMotor intake = hardwareMap.dcMotor.get("intake");
        while(!isStarted() && !isStopRequested()){
            intake.setPower(1);
        }
    }
}
