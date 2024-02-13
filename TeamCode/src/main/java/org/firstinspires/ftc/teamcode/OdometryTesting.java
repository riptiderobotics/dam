package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.kinematics.HolonomicOdometry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.arcrobotics.ftclib.geometry.Pose2d;


@TeleOp(name="odotest", group = "linear opmode")
public class OdometryTesting extends LinearOpMode {


    private MotorEx leftEncoder, rightEncoder, perpEncoder;
    private HolonomicOdometry odometry;

    public static Pose2d robotPose;
    //Measurements are in inches
    //Track width is the distance from one horizontal pod to the other horizontal pod
    public static final double TRACKWIDTH = 7.91339272;
    public static final double CENTER_WHEEL_OFFSET = 0.477;
    public static final double WHEEL_DIAMETER = 1.88976;
    // if needed, one can add a gearing term here
    //Check Gobilda, it said 2000 countable events per revolution
    public static final double TICKS_PER_REV = 2000;
    public static final double DISTANCE_PER_PULSE = Math.PI * WHEEL_DIAMETER / TICKS_PER_REV;
    @Override
    public void runOpMode() throws InterruptedException{

        leftEncoder = new MotorEx(hardwareMap, "left odometer");
        rightEncoder = new MotorEx(hardwareMap, "right odometer");
        perpEncoder = new MotorEx(hardwareMap, "center odometer");

        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        perpEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

        odometry = new HolonomicOdometry(
                leftEncoder::getDistance,
                rightEncoder::getDistance,
                perpEncoder::getDistance,
                TRACKWIDTH,
                CENTER_WHEEL_OFFSET
        );

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            // run autonomous

            // update positions
            odometry.updatePose();
            robotPose = odometry.getPose();
            telemetry.addData("Robot Location X:", robotPose.getX());
            telemetry.addData("Robot Location Y:", robotPose.getY());
            telemetry.addData("Robot Heading:", robotPose.getHeading());
            telemetry.addData("Hey", "Test");
            telemetry.update();
        }
    }

}
