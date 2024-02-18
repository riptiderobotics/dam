package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
@TeleOp(name="Blink Pattern Test", group="Linear OpMode")

public class BlinkPatternTest extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private LynxModule cHub;
    private Collection<Blinker.Step> steps;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //change deviceName below to match the name of your control hub in your robot configuration
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        steps = new ArrayList<Blinker.Step>();

        for(int i = 0; i < 100; i++)
        {
            steps.add(new Blinker.Step(colorPicker((int)(Math.sin((double)i/100) * 256), (int)(Math.sin(2 * (double)i/100) * 256), (int)(Math.sin(3 * (double)i/100) * 256)), 100, TimeUnit.MILLISECONDS));
        }
        for (LynxModule hub : allHubs) {
            hub.setPattern(steps);
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            sleep(5000);
        }

    }
    public int colorPicker(int r, int g, int b)
    {
        return r * 256 * 256 + g * 256 + b;
    }
}