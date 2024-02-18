package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.VisionPipelines.ActuallyContourPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "auton red", group = "linear opmode")
public class ILTautonRed extends LinearOpMode{

    // not quadrant but trident hehe get it?
    private int trident()
    {
        //if
        return 1;
    }
    OpenCvWebcam Webcam = null;
    @Override
    public void runOpMode() {

        while (!isStarted() && !isStopRequested()) {
            // Acquire the camera ID
            WebcamName webcamname = hardwareMap.get(WebcamName.class, "Webcam");

            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            //set the cam name and id to the webcam.
            Webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamname, cameraMonitorViewId);

            //set webcam Pipeline
            Webcam.setPipeline(new ActuallyContourPipeline());

            // 0 for red team, 1 for blue team
            ActuallyContourPipeline.setTeamFilter(0);

            //You're creating an instance of the AsyncCameraOpenListener class. The class contains the two methods, onOpened and onError, which you are overriding with your code. That instance is passed to the openCameraDeviceAsync method as a parameter.
            Webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    Webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
                }

                @Override
                public void onError(int errorCode) {
                    telemetry.addLine("Webcam not working");
                }
            });
        }


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
