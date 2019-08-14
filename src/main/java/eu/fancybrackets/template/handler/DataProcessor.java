package eu.fancybrackets.template.handler;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

public class DataProcessor {
	
	/**
	 * 
	 * @param tankNb
	 * @throws IllegalArgumentException
	 */
	public DataProcessor(int tankNb) throws IllegalArgumentException {
		this.tankNbs = new HashSet<Integer>();
		this.setTankNb(tankNb);
		this.dataArray = new float[] {0, 0, 0, 0, 0};
		this.stopped = false;
		this.running = false;
	}
	
	/**
	 * 
	 */
	private boolean running;
	
	/**
	 * 
	 */
	public boolean stopped;
	
	/**
	 * 
	 */
	private final float minLevel = 30;
	
	/**
	 * 
	 */
	private float[] dataArray;
	
	/**
	 * 
	 */
	private int tankNb;
	
	/**
	 * 
	 */
	private Set<Integer> tankNbs;
	
	/**
	 * 
	 */
	private static float maxHeightDiff = 2;
	
	
	/**
	 * 
	 * @param waterlevel
	 * @return
	 */
	public float SmoothenData(float waterlevel) {
		
		//shift elements 
		for (int i = 0; i < 4; i++) {
			this.dataArray[i] = this.dataArray[i+1];
		}
		
		//condition 1
		if (this.checkWithinBoundaries(waterlevel) || (this.stopped && waterlevel > this.minLevel)) {
			
			//add new data to array
			this.dataArray[4] = waterlevel;
			
			//voorwaarde 3
			if(this.checkRightDirection(waterlevel) && !this.stopped) {
				float smoothedValue = (this.dataArray[3] + this.dataArray[4])/2;
				this.dataArray[3] = smoothedValue;
				this.dataArray[4] = smoothedValue;
			}
			
			//voorwaarde 4:
			//haal eerst nulelementen eruit, vermenigvuldig dan
			float[] list = {0,0,0,0};
			list[0] = this.dataArray[0] - this.dataArray[1];
			list[1] = this.dataArray[1] - this.dataArray[2];
			list[2] = this.dataArray[2] - this.dataArray[4];
			list[3] = this.dataArray[2] - this.dataArray[3];
			float result = 1;
			
			for (float i : list) {
				if (i == 0) {
					list = ArrayUtils.removeElement(list, i);
				}
				else {
					result = result * i;
				}
			}
			
			if (result < 0 && !this.stopped) {
				this.dataArray[3] = (this.dataArray[3] + this.dataArray[4])/2;
			}
						
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
				return -1;
			}
		}
		this.setStarted();
		return this.dataArray[4];
	}
	
	/**
	 * 
	 * @return
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
	 * 
	 * @param waterlevel
	 * 		  measured water level of the tank, in cm.
	 * @return true if measured water level doesn't differ more than maxHeightDiff 
	 * 		   with the previous measurement, false otherwise.
	 */
	private boolean checkWithinBoundaries(float waterlevel) {
		return (Math.abs(this.dataArray[3] - waterlevel) <= 1.1 * maxHeightDiff);
	}
	
	/**
	 * 
	 * @param waterlevel
	 * @return
	 */
	private boolean checkRightDirection(float waterlevel) {
		return (Math.abs(this.dataArray[2] - this.dataArray[3]) > 0.55 * maxHeightDiff )
				&& (Math.abs(this.dataArray[2] - this.dataArray[4]) > 0.55 * maxHeightDiff )
				&& ((this.dataArray[2] - this.dataArray[3]) * (this.dataArray[2] - this.dataArray[4]) < 0 );
	}
	
	/**
	 * 
	 * @param tankNb
	 * @throws IllegalArgumentException
	 */
	private void setTankNb(int tankNb) throws IllegalArgumentException {
		if (tankNbs.contains(tankNb)) {
			throw new IllegalArgumentException("tank number already exists");
		}
		this.tankNb = tankNb;
	}
	
	/**
	 * 
	 */
	public void setRunning() {
		this.running = true;
	}
	
	/**
	 * 
	 */
	public void setIdle() {
		this.running = false;
	}
	
	/**
	 * 
	 */
	public void setStopped() {
		this.stopped = true;
	}
	
	/**
	 * 
	 */
	private void setStarted() {
		this.stopped = false;
	}
	
	/**
	 * 
	 */
	public boolean isStopped() {
		return this.stopped;
	}
	
	/**
	 * 
	 * @return
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
