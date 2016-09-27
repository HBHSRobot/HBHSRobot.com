package org.usfirst.frc.team5966.robot;

import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	VictorSP frontRight, frontLeft, backLeft, backRight, arm, arm2, launcherLeft, launcherRight;
	Joystick xbox;
	double rightYAxis, LTrigger, RTrigger;
	int autoLoopCounter;
    CameraServer server;
    public final double upperArmBuffer = 1.0;
    public final double lowerArmBuffer = 0.05;
    public final double upperIntakeBuffer = 0.3;
    public final double upperDischargeBuffer = 0.3;
    public final double lowerLauncherBuffer = 0.01;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture("cam0");
    	frontRight = new VictorSP(1); // real = 1
    	frontLeft = new VictorSP(3); // real = 3
    	backLeft= new VictorSP(0); // real backLeft should be pin 0
    	backRight = new VictorSP(2); // real = 2
    	arm = new VictorSP(4); // real = 4
    	arm2 = new VictorSP(5); // real = 5
    	launcherLeft = new VictorSP(6); // real = 6
    	launcherRight = new VictorSP(7); // real = 7
    	frontRight.setInverted(true);
    	backRight.setInverted(true);
    	frontLeft.setInverted(true);
    	backLeft.setInverted(true);
    	arm.setInverted(true);
    	arm2.setInverted(true);
    	xbox = new Joystick(0);
    	myRobot = new RobotDrive(frontLeft,backLeft,frontRight,backRight);
    
    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 350;
    	System.out.println("Autonomous mode initialized");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
          if(500 < autoLoopCounter && autoLoopCounter<575){
			myRobot.drive(-0.2, 1);
		} else if(650<autoLoopCounter&&autoLoopCounter<750){
			//myRobot.drive(-.3, 0);
		} else if (850<autoLoopCounter&&autoLoopCounter<925){
			myRobot.drive(-.2 , -1);
        } else if (1000<autoLoopCounter&&autoLoopCounter<1025){
            myRobot.drive(-.2,1);


		
		} else {
			launcherLeft.set(0);
			launcherRight.set(0);
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
    	
    	
    	autoLoopCounter++;
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        myRobot.arcadeDrive(xbox);
        if(-xbox.getRawAxis(5)>(upperArmBuffer)){
        	rightYAxis = upperArmBuffer;
        } else if (-xbox.getRawAxis(5)<(-upperArmBuffer)){
        	rightYAxis = -upperArmBuffer;
        } else if ((-xbox.getRawAxis(5)>lowerArmBuffer)||-xbox.getRawAxis(5)<-lowerArmBuffer){
        	rightYAxis = -xbox.getRawAxis(5);
        }
        arm.set(rightYAxis); //set > 0 to put the arm up, set < 0 to put the arm down
        arm2.set(rightYAxis);
        LTrigger = xbox.getRawAxis(2);
        RTrigger = xbox.getRawAxis(3);
        if ((LTrigger>lowerLauncherBuffer)&&(RTrigger<lowerLauncherBuffer)){
        	if (LTrigger>upperIntakeBuffer){
        		launcherLeft.set(upperIntakeBuffer);
        		launcherRight.set(-upperIntakeBuffer);
        	} else {
        		launcherLeft.set(LTrigger);
            	launcherRight.set(-LTrigger);
        	}
        } else if ((RTrigger>lowerLauncherBuffer)&&(LTrigger<lowerLauncherBuffer)){
        	if (RTrigger>upperDischargeBuffer){
        		launcherLeft.set(-upperDischargeBuffer);
        		launcherRight.set(upperDischargeBuffer);
        	} else {
        		launcherLeft.set(-RTrigger);
            	launcherRight.set(RTrigger);
        	}
        } else {
        	launcherLeft.set(0.0);
        	launcherRight.set(0.0);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
