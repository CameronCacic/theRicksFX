package edu.gatech.cs2340.thericks.utils;

/**
 * Keeps a running average of the values passed to it. Only the running
 * average and the total amount of elements passed to it are stored:
 * individual elements are absorbed into the running average
 * @author Cameron
 *
 */
public class RunningAverage {
	
	private int numElements;
	private double currAvg;

	/**
	 * Creates a RunningAverage object with the first element
	 * @param elem1 the first element
	 */
	public RunningAverage(double elem1) {
		numElements = 1;
		currAvg = elem1;
	}
	
	/**
	 * Creates an empty RunningAverage object
	 */
	public RunningAverage() {
		numElements = 0;
		currAvg = 0;
	}
	
	/**
	 * Adds the passed element into the running average
	 * @param element the element to add to the running average
	 * @return the new running average
	 */
	public double add(double element) {
		numElements++;
		currAvg = (currAvg * (numElements - 1) + element) / numElements;
		return currAvg;
	}

	/**
	 * Returns the current running average
	 * @return the current running average
	 */
	public double getCurrentAverage() {
		return currAvg;
	}
}
