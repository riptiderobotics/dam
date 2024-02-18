package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.VisionPipelines.ActuallyContourPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "webcam testing!")
public class webcamTesting extends LinearOpMode {
    DcMotor RBMotor = hardwareMap.dcMotor.get("RBMotor");
    DcMotor LFMotor = hardwareMap.dcMotor.get("LFMotor");
    DcMotor LBMotor = hardwareMap.dcMotor.get("LBMotor");

    DcMotorEx RFMotor = hardwareMap.get(DcMotorEx.class, "RFMotor");



    // why does Android studio have a spell checker
    // webcamdude is the webcam. I'm not naming it anything else
    OpenCvWebcam Webcam = null;

    int[] lastCentroid = {0, 0};


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

            while(opModeIsActive())
            {
            int[] centroid;
            for (int x = 0; x < 100; x++) {
                centroid = ActuallyContourPipeline.getCentroid();
                if (centroid[0] != 0 && centroid[1] != 0) {
                    lastCentroid = centroid;
                }

                if (lastCentroid[0] < 100) {
                    // do something
                    LBMotor.setPower(0.25);
                    RFMotor.setPower(0.25);
                    RBMotor.setPower(0.25);
                    LFMotor.setPower(0.25);
                    sleep(10000);

                } else if (lastCentroid[0] < 300) {
                    // do something else
                    LBMotor.setPower(0.25);
                    RFMotor.setPower(0.25);
                    RBMotor.setPower(0.25);
                    LFMotor.setPower(0.25);
                    sleep(5000);

                } else {
                    // do another thing
                    LBMotor.setPower(0.25);
                    RFMotor.setPower(0.25);
                    RBMotor.setPower(0.25);
                    LFMotor.setPower(0.25);
                    sleep(7000);
                }
                LBMotor.setPower(0);
                RFMotor.setPower(0);
                RBMotor.setPower(0);
                LFMotor.setPower(0);
                sleep(300000);
            }
        }
    }
}