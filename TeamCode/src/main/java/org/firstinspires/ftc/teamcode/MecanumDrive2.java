package org.firstinspires.ftc.teamcode;

//All the imported files

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name = "Camera Test", group = "linear opmode")
public class MecanumDrive2 extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static int DESIRED_TAG_ID = 1;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTagProcessor;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;
    private AprilTagProcessor.Builder aprilTagProcessorBuilder;
    public enum OuttakeStates{
        START,
        DEPOSIT_SETUP,
        DROP
    }

    public enum IntakeStates{
        START,
        INTAKING,
        FINISH
    }
    OuttakeStates outtakeStates = OuttakeStates.START;
    IntakeStates intakeStates = IntakeStates.START;
    public void runOpMode() throws InterruptedException {

        boolean targetFound = false;

        initCamera();

        if (USE_WEBCAM)
            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

        // Wait for driver to press start
        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
        telemetry.update();

        DcMotor RFMotor = hardwareMap.dcMotor.get("RFMotor");
        DcMotor RBMotor = hardwareMap.dcMotor.get("RBMotor");
        DcMotor LFMotor = hardwareMap.dcMotor.get("LFMotor");
        DcMotor LBMotor = hardwareMap.dcMotor.get("LBMotor");
        DcMotorEx IntakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");
        //Pixel Holder Servo
        Servo outtakeServo1 = hardwareMap.servo.get("Outake1");
        DcMotor SpoolMotor = hardwareMap.dcMotor.get("Spool");
        //Outake Swiweler Servo
        Servo OutakeServo2 = hardwareMap.servo.get("Outake2");
        double tracker = hardwareMap.voltageSensor.iterator().next().getVoltage();
        //Initialize servos to required position
        outtakeServo1.setPosition(1);
        OutakeServo2.setPosition(1);
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
        waitForStart();
        while (opModeIsActive()) {

            switch (outtakeStates){
                //START case is the inital case, where the outake hasn't sprung out
                case START:
                    if(gamepad2.x){
                        OutakeServo2.setPosition(0);
                        outtakeStates = OuttakeStates.DEPOSIT_SETUP;
                    }
                    break;
                    //DEPOSIT_SETUP is second state where the outake gets ready to drop the pixel
                case DEPOSIT_SETUP:
                    if(gamepad2.x){
                        outtakeServo1.setPosition(0);
                        outtakeStates = OuttakeStates.DROP;
                    }
                    if(gamepad2.y){
                        OutakeServo2.setPosition(1);
                        outtakeStates = OuttakeStates.START;
                    }
                    break;
                    //DROP is the final state where the pixel is dropped
                case DROP:
                    if(gamepad2.x){
                        outtakeServo1.setPosition(1);
                        OutakeServo2.setPosition(1);
                        outtakeStates = OuttakeStates.START;
                    }
                    break;
            }
            switch (intakeStates){
                case START:
                    if(gamepad2.a){
                        IntakeMotor.setPower(0.5);
                        intakeStates = IntakeStates.INTAKING;
                    }
                    break;
                case INTAKING:
                    //Need to tune the current value
                    if(IntakeMotor.getCurrent(CurrentUnit.AMPS) > 2){
                        sleep(500);
                        intakeStates = IntakeStates.FINISH;
                    }
                    break;
                case FINISH:
                    if(IntakeMotor.getCurrent(CurrentUnit.AMPS) < 2 && IntakeMotor.getCurrent(CurrentUnit.AMPS) > 1){
                        IntakeMotor.setPower(0);
                        intakeStates = IntakeStates.START;
                    }
                    break;

            }

            List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                for(int i = 1; i <= 3; i++){
                    if(detection.metadata != null && (detection.id == i)){
                        targetFound = true;
                        DESIRED_TAG_ID = i;
                        break;
                    }
                    else
                        DESIRED_TAG_ID = -1;
                }
                if (DESIRED_TAG_ID < 0){
                    targetFound = false; // don't look any further.
                    telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id);
                } else {
                    break;
                }
            }
            // Tell the driver what we see, and what to do.
            if (targetFound) {
                telemetry.addData("Target", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
                telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);

            } else {
                telemetry.addData(">","Drive using joysticks to find valid target\n");
            }

            double botHeading = -imu.getAngularOrientation().firstAngle;
            if (gamepad1.a) {
                offset = -imu.getAngularOrientation().firstAngle;
            }
            if (gamepad1.b) {
                offset = 0;
            }

            double y = 0.55 * gamepad1.left_stick_y; // Remember, this is reversed!
            double x = 0.55 * gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = 0.55 * gamepad1.right_stick_x;

            double rotX = x * Math.cos(botHeading - offset) - y * Math.sin(botHeading - offset);
            double rotY = x * Math.sin(botHeading - offset) + y * Math.cos(botHeading - offset);

            //Powering intake on or off
            if (gamepad2.dpad_up) {
                IntakeMotor.setPower(1);
            } else if (gamepad2.dpad_down) {
                IntakeMotor.setPower(-1);
            } else {
                IntakeMotor.setPower(0);
            }

            //Powering slides on/off
            if (gamepad2.x) {
                SpoolMotor.setPower(0.5);
            } else if (gamepad2.y) {
                SpoolMotor.setPower(-0.5);
            } else {
                SpoolMotor.setPower(0);
            }

            //Servo positioning



            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            LFMotor.setPower(frontLeftPower);
            LBMotor.setPower(backLeftPower);
            RFMotor.setPower(frontRightPower);
            RBMotor.setPower(backRightPower);
            telemetry.addData("y speed:", y);
            telemetry.addData("x speed:", x);
            telemetry.update();
        }
    }

    public void initCamera(){
        aprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        aprilTagProcessorBuilder.setLensIntrinsics(578.272, 578.272, 402.145, 221.506);

        aprilTagProcessorBuilder.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11);
        aprilTagProcessorBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary());

        aprilTagProcessorBuilder.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES);

        aprilTagProcessorBuilder.setNumThreads(3);

        aprilTagProcessorBuilder.setDrawTagID(true);
        aprilTagProcessorBuilder.setDrawTagOutline(true);
        aprilTagProcessorBuilder.setDrawAxes(true);
        aprilTagProcessorBuilder.setDrawCubeProjection(true);

        aprilTagProcessor = aprilTagProcessorBuilder.build();
    }

    private void setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

    }
}

