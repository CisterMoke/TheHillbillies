package hillbillies.model;

import be.kuleuven.cs.som.annotate.*;

/** Class representing a log in the game.
 * @invar  The weight of each Log must be a valid weight for any
 *         Log.
 *       | isValidWeight(getWeight())
 * @invar  The position of each Log must be a valid position for any
 *         Log.
 *       | isValidPosition(getPosition())
 */
public class Log {	
	/**
	 * Initialize this new Log with given weight.
	 *
	 * @param	x
	 * 			The x-coordinate for the new Log.
	 * @param	y
	 * 			The y-coordinate for the new Log.
	 * @param	z
	 * 			The z-coordinate for the new Log.
	 * @param  	weight
	 *         	The weight for this new Log.
	 * @effect	The weight of this new Log is set to
	 *         	the given weight.
	 * @effect	The coordinates of position of this new Log
	 * 			are set to the given coordinates.
	 * @throws	IllegalArgumentException
	 * 			An exception is thrown if the given coordinates are invalid
	 * 			or if the given weight is invalid.
	 */
	public Log(double x, double y, double z, int weight)
			throws IllegalArgumentException {
		this.setWeight(weight);
		this.setPosition(new Vector(x, y, z));
	}
	
	
	/**
	 * Return the weight of this Log.
	 */
	@Basic @Raw
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * Check whether the given weight is a valid weight for
	 * any Log.
	 *  
	 * @param  	weight
	 *         	The weight to check.
	 * @return 	True if and only if the given weight lies between
	 * 			10 and 50 inclusively.
	*/
	public static boolean isValidWeight(int weight) {
		return (weight >= 10 || weight <= 50);
	}
	
	/**
	 * Set the weight of this Log to the given weight.
	 * 
	 * @param  weight
	 *         The new weight for this Log.
	 * @post   The weight of this new Log is equal to
	 *         the given weight.
	 *       | new.getWeight() == weight
	 * @throws IllegalArgumentException
	 *         The given weight is not a valid weight for any
	 *         Log.
	 *       | ! isValidWeight(getWeight())
	 */
	@Raw
	public void setWeight(int weight) 
			throws IllegalArgumentException {
		if (! isValidWeight(weight))
			throw new IllegalArgumentException("Invalid weight!");
		this.weight = weight;
	}


	/**
	 * Return the position of this Log.
	 */
	@Basic @Raw
	public Vector getPosition() {
		return new Vector(this.position);
	}
	
	/**
	 * Check whether the given position is a valid position for
	 * any Log.
	 *  
	 * @param  	position
	 *         	The position to check.
	 * @return 	True if and only if every coordinate lies within
	 * 			the world's boundary.
	*/
	public static boolean isValidPosition(Vector position) {
		boolean checker = true;
		for(double coord : position.getCoeff()){
			if((coord < 0) || (coord > 50))
				checker = false;
		}
		return checker;
	}
	
	/**
	 * Set the position of this Log to the given position.
	 * 
	 * @param  position
	 *         The new position for this Log.
	 * @post   The position of this new Log is equal to
	 *         the given position.
	 *       | new.getPosition() == position
	 * @throws IllegalArgumentException
	 *         The given position is not a valid position for any
	 *         Log.
	 *       | ! isValidPosition(getPosition())
	 */
	@Raw
	public void setPosition(Vector position) 
			throws IllegalArgumentException {
		if (! isValidPosition(position))
			throw new IllegalArgumentException();
		this.position = position;
	}
	
	@Basic
	public Unit getCarrier(){
		return this.carrier;
	}
	
	public void setCarrier(Unit unit){
		if(canHaveAsCarrier(unit)){
			this.carrier = unit;
			unit.setLog(this);
		}
		return;
	}
	
	public boolean canHaveAsCarrier(Unit unit){
		return((this.carrier == null) && !unit.isCarrying());
	}
	
	public void removeCarrier(){
		if(this.carrier == null)
			return;
		setPosition(getCarrier().getPosition());
		this.carrier = null;
	}
	
	/**
	 * Variable registering the position of this Log.
	 */
	private Vector position;
		
	/**
	 * Variable registering the weight of this Log.
	 */
	private int weight;
	
	private Unit carrier = null;
}
