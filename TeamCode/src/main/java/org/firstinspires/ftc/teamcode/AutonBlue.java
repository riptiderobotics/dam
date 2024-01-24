package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "autonblue", group = "linear opmode")
public class AutonBlue extends LinearOpMode{
    @Override
    public void runOpMode() {
        DcMotor RFMotor = hardwareMap.dcMotor.get("RFMotor");
        DcMotor RBMotor = hardwareMap.dcMotor.get("RBMotor");
        DcMotor LFMotor = hardwareMap.dcMotor.get("LFMotor");
        DcMotor LBMotor = hardwareMap.dcMotor.get("LBMotor");
        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LFMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        // Technically this is the default, however specifying it is clearer
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters);
        double offset = 0;
        while(opModeIsActive()){
            LBMotor.setPower(0.25);
            RFMotor.setPower(0.25);
            RBMotor.setPower(0.25);
            LFMotor.setPower(0.25);
            sleep(5000);
            LBMotor.setPower(-0.25);
            RFMotor.setPower(-0.25);
            RBMotor.setPower(0.25);
            LFMotor.setPower(0);

        }
    }
}
