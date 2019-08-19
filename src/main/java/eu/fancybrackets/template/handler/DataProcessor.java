package eu.fancybrackets.template.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

public class DataProcessor {
	
	/**
	 * Constructor for the class DataProcessor. Creates an instance of the class, and sets the tank
	 * number to the given tank number.
	 * The attributes stopped and running are default set to false. The tankHeight is default set to 
	 * 24 cm.
	 * @param tankNb
	 * 		  an integer that represents the number of the tank. Each tank has a unique tank number.
	 * @throws IllegalArgumentException
	 * 		   throws this exception if the tank number is already in use by another tank.
	 */
	public DataProcessor(int tankNb) throws IllegalArgumentException {
		this.setTankNb(tankNb);
		this.dataArray = new float[] {0, 0, 0, 0, 0};
		this.stopped = false;
		this.running = false;
		this.tankHeight = 24;
	}
	
	/**
	 * Constructor for the class DataProcessor. Creates an instance of the class, and sets the tank
	 * number to the given tank number. Sets the tank height to the given height.
	 * @param tankNb
	 * 		  an integer that represents the number of the tank. Each tank has a unique tank number.
	 * @param height
	 * 		  a float number that represents the height of the tank.
	 * @throws IllegalArgumentException
	 * 		   throws this exception if the tank number is already in use by another tank.
	 */
	public DataProcessor(int tankNb, float height) throws IllegalArgumentException {
		this.setTankNb(tankNb);
		this.dataArray = new float[] {0, 0, 0, 0, 0};
		this.stopped = false;
		this.running = false;
		this.tankHeight = height;
	}
	
	/**
	 * a float number that represents the height of the tank in cm, from the lowest point 
	 * that the fluid can be, to the point where the sensor is measuring. 
	 */
	private float tankHeight;
	
	/**
	 * a boolean variable that represents the state of the tank. If the fluid is running
	 * out of the tank, running is set to true. If the tank is idle, running is set to false.
	 */
	private boolean running;
	
	/**
	 * a boolean value that is set to true if the tank was automatically stopped because the 
	 * water level was too low. Set to false otherwise. 
	 * This is not the same as this.running:
	 * this.stopped can be false while the tank is idle if the tank was stopped by an external
	 * event, like someone that pushes a button to stop the tank.
	 */
	public boolean stopped;
	
	/**
	 * a float number that represents the minimum water level of the tank in cm. If the
	 * water level is below this value, the tank does not have enough fluid and will stop
	 * automatically.
	 */
	private final float minLevel = 5;
	
	/**
	 * an array that contains the last 5 water level measurements.
	 */
	private float[] dataArray;
	
	/**
	 * the number of the tank. Each tank has a unique number.
	 */
	private int tankNb;
	
	/**
	 * a set that contains all the tank numbers of the existing tanks. Since each tank
	 * has a unique tank number, the set can not contain duplicates.
	 */
	private static Set<Integer> tankNbs = new HashSet<Integer>(Arrays.asList());
	
	/**
	 * a float number that represents the maximum difference in cm between 2 consecutive 
	 * measurements. 
	 */
	private static float maxHeightDiff = 5;
	
	
	/**
	 * Smoothen the measured water levels. The sensor does not accurately measure each water level,
	 * this function corrects the measurements. It takes into account a few rules:
	 * 		rule 1: a new measurement can not differ more with the previous measurement than
	 * 				maxHeightDiff. 
	 * 					--> if it differs too much, replace new measurement value with the value of 
	 * 						the previous measurement.
	 * 		rule 2: it is not possible for a measurement to be higher/lower than their previous AND 
	 * 				subsequent measurment. 
	 * 					--> if this is the case, replace the value of the incorrect measurement with 
	 * 						the mathematical average of the two adjacent measurements.
	 * 		rule 3: If all measurements except one, which is not the oldest and not the newest measurement,
	 * 				are in descending/ascending order, then this one measurement is incorrect.
	 * 					--> if this is the case, replace the value of the incorrect measurement with
	 * 						the mathematical average of the two adjacent measurements.
	 * @param distance
	 * 		  The measurement value in cm that matches the distance from the sensor to the surface of the 
	 * 		  water. This parameter is directly converted to the water level in the tank in the beginning 
	 * 		  of the function, so the corrections do not happen directly on this parameter.
	 * @return a float number that represents a corrected value of the water level in the tank.
	 */
	public float SmoothenData(float distance) {
		
		float waterlevel = this.convertToWaterLevel(distance);
		
		//shift elements 
		for (int i = 0; i < 4; i++) {
			this.dataArray[i] = this.dataArray[i+1];
		}
		
		//rule 1
		if (this.checkWithinBoundaries(waterlevel) || (this.stopped && waterlevel > this.minLevel)) {
			
			//add new data to array
			this.dataArray[4] = waterlevel;
			/*
			//rule 2
			if(this.checkRightDirection(waterlevel) && !this.stopped) {
				float smoothedValue = (this.dataArray[3] + this.dataArray[4])/2;
				this.dataArray[3] = smoothedValue;
				this.dataArray[4] = smoothedValue;
			}
			
			//rule 3:
			//haal eerst nulelementen eruit, vermenigvuldig dan
			float[] list = {0,0,0,0};
			list[0] = this.dataArray[0] - this.dataArray[1];
			list[1] = this.dataArray[1] - this.dataArray[2];
			list[2] = this.dataArray[2] - this.dataArray[4];
			list[3] = this.dataArray[2] - this.dataArray[3];
			float result = 1;
			
			int a = 0;
			for (float i : list) {
				if (i == 0) {
					list = ArrayUtils.removeElement(list, i);
					a ++;
				}
				else {
					result = result * i;
				}
			}
			
			if (result <0) {
				for (int i = 0; i<a; i++) {
					result = result * -1;
				}
			}
			
			if (result < 0 && !this.stopped) {
				this.dataArray[3] = (this.dataArray[3] + this.dataArray[4])/2;
			}
			*/			
		}
		else {
			if (this.dataArray[0] == 0) {
				this.dataArray[4] = waterlevel;
			}
			else {
				this.dataArray[4] = this.dataArray[3];
			}
		}
		
		System.out.println(Integer.toString(this.tankNb) + ": " + this.dataArray[0] + ", " + this.dataArray[1] + ", " + this.dataArray[2]+ ", " + this.dataArray[3]+ ", " + this.dataArray[4]);
		if (waterlevel < this.minLevel) {
			if (this.checkUnderMinLevel()) {
				this.setStopped();
				try {
					Runtime.getRuntime().exec("python IntershopBestellingPlaatsen.py");
				} catch (IOException e) {
					System.out.println("Exception from putting order: " + e);
					e.printStackTrace();
				}
				return -1;
			}
		}
		this.setStarted();
		return this.dataArray[4];
	}
	
	/**
	 * Converts the given distance into the water level of the tank.
	 * @param distance
	 * 		  The measured distance between the sensor and the surface of the water.
	 * @return this.tankHeight - distance
	 */
	private float convertToWaterLevel (float distance) {
		return this.tankHeight - distance;
	}
	
	/**
	 * Checks if the water level is lower than the minimal water level of the tank.
	 * @return true if all the 5 last measured water levels are below the minimal level, 
	 * 		   false otherwise.
	 */
	private boolean checkUnderMinLevel() {
		for (int i = 0; i < 5; i++) {
			if (this.dataArray[i] >= this.minLevel) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the given water level is within boundaries, according to the previously measured 
	 * water level.
	 * @param waterlevel
	 * 		  measured water level of the tank, in cm.
	 * @return true if measured water level doesn't differ more than maxHeightDiff 
	 * 		   with the previous measurement, false otherwise.
	 */
	private boolean checkWithinBoundaries(float waterlevel) {
		return (Math.abs(this.dataArray[3] - waterlevel) <= 1.1 * maxHeightDiff);
	}
	
	/**
	 * Checks if the given water level is in the same order (ascending/descending) as the other
	 * measured water levels.
	 * @param waterlevel
	 * 		  measured water level of the tank, in cm.
	 * @return true if all the water levels are ascending/descending, false otherwise.
	 */
	private boolean checkRightDirection(float waterlevel) {
		return (Math.abs(this.dataArray[2] - this.dataArray[3]) > 0.55 * maxHeightDiff )
				&& (Math.abs(this.dataArray[2] - this.dataArray[4]) > 0.55 * maxHeightDiff )
				&& ((this.dataArray[2] - this.dataArray[3]) * (this.dataArray[2] - this.dataArray[4]) < 0 );
	}
	
	/**
	 * Sets the tank number of the tank to the given tank number.
	 * @param tankNb
	 * 		  an integer that represents the tank number.
	 * @throws IllegalArgumentException
	 * 		   throws exception if the given tank number already exists.
	 */
	private void setTankNb(int tankNb) throws IllegalArgumentException {
		if (tankNbs.contains(tankNb)) {
			throw new IllegalArgumentException("tank number already exists");
		}
		tankNbs.add(tankNb);
		this.tankNb = tankNb;
	}
	
	/**
	 * Sets the running attribute of the tank to true. 
	 */
	public void setRunning() {
		this.running = true;
	}
	
	/**
	 * Sets the running attribute of the tank to false.
	 */
	public void setIdle() {
		this.running = false;
	}
	
	/**
	 * Sets the stopped attribute of the tank to true.
	 */
	public void setStopped() {
		this.stopped = true;
	}
	
	/**
	 * Sets the stopped attribute of the tank to false.
	 */
	private void setStarted() {
		this.stopped = false;
	}
	
	/**
	 * Checks if the tank is stopped by the program as a result of its water level being too low.
	 * @return true if the tank is stopped for this reason, false otherwise.
	 */
	public boolean isStopped() {
		return this.stopped;
	}
	
	/**
	 * Returns an integer 1 or 0, depending on the state of the tank.
	 * @return	1 if the tank is running, 0 if the tank is idle.
	 */
	public int intIsRunning() {
		if (this.running) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
