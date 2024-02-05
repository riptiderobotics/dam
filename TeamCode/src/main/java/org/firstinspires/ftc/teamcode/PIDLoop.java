package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDLoop {
    public double integralSum = 0;
    public double kP;
    public double kI;
    public double kD;
    private double lastError = 0;

    ElapsedTime timer = new ElapsedTime();

    public PIDLoop(double proportional, double integral, double derivative){
        kP = proportional;
        kI = integral;
        kD = derivative;
    }

    public double PIDControl(double reference, double state){
        double error = reference - state;
        integralSum += error * timer.seconds();

        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error * kP) + (derivative * kD) + (integralSum * kI);
        return output;





    }

}
