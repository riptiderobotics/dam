package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.VisionPipelines.ActuallyContourPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Manual Autonomous (Only use if RR isn't working)")
public class manualAutonomous extends LinearOpMode {
    DcMotor RFMotor;
    DcMotor RBMotor;
    DcMotor LFMotor;
    DcMotor LBMotor;
    DcMotorEx IntakeMotor;



    /*
        Autonomous modes:
        1 = right
        2 = centere
        3 = left
        automatically set to left b/c we only check for right and center and use process of elim to deduce left.
     */

    int autonMode = 3;

    // why does Android studio have a spell checker
    // webcamdude is the webcam. I'm not naming it anything else
    OpenCvWebcam Webcam = null;

    int[] lastCentroid = {0, 0};

    private void runForward(DcMotor rightFront, DcMotor leftFront, DcMotor rightBack, DcMotor leftBack)
    {
        rightFront.setPower(0.5);
        leftFront.setPower(0.5);
        rightBack.setPower(0.5);
        leftBack.setPower(0.5);
        sleep(3500);
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);
    }

    public void runOpMode() {
        //Motor init
        RFMotor = hardwareMap.dcMotor.get("RFMotor");
        RBMotor = hardwareMap.dcMotor.get("RBMotor ");
        LFMotor = hardwareMap.dcMotor.get("LFMotor");
        LBMotor = hardwareMap.dcMotor.get("LBMotor");
        IntakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");
        LBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LFMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Camera Init
            // Acquire the camera ID
            WebcamName webcamname = hardwareMap.get(WebcamName.class, "Webcam 1");

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

                @Override public void onError(int errorCode) {
                    telemetry.addLine("Webcam not working");
                }});

        int[] centroid;
        for (int x = 0; x < 1000; x++) {
            centroid = ActuallyContourPipeline.getCentroid();
            if (centroid[0] != 0 && centroid[1] != 0) {
                lastCentroid = centroid;
                telemetry.addData("x coord:", lastCentroid[0]);
                telemetry.addData("Y coord", lastCentroid[1]);
            }
            if (lastCentroid[1] > 100) {
                if (lastCentroid[0] < 200) {
                    // do something
                    autonMode = 1;

                } else if (lastCentroid[0] > 200) {
                    // do something else
                    autonMode = 2;

                }

                telemetry.addData("Detected", autonMode);
                telemetry.update();
                break;
            }
        }

        waitForStart();

        while(opModeIsActive())
        {
                //3500 @ 0.5
            runForward(RFMotor,LFMotor,RBMotor,LBMotor);

            /*if (autonMode == 1)
            {

            } else if( autonMode == 3) {

            }*/

            IntakeMotor.setPower(0.5);
            sleep(1);
            IntakeMotor.setPower(0);


        }
    }
}