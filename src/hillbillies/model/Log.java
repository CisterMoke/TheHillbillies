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
	
	public void advanceTime(double dt){
		
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
	 * Return the coordinates of the cube
	 * 	this Log is currently in.
	 */
	public Vector getBlockPosition(){
		double blockX = Math.floor(getPosition().getX());
		double blockY = Math.floor(getPosition().getY());
		double blockZ = Math.floor(getPosition().getZ());
		return new Vector(blockX, blockY, blockZ);
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
	 * @param  	position
	 *         	The new position for this Log.
	 * @effect  If this log belongs to a world, this log will be removed
	 * 			from the block it was previously in and will be added to the
	 * 			new block it is in.
	 * @post	 The new position is a valid position.
	 * 			| new.getPosition() == position && isValidPosition(new.getPosition())
	 * @throws 	IllegalArgumentException
	 *         	The given position is not a valid position for any
	 *         	Log.
	 */
	@Raw
	public void setPosition(Vector position) 
			throws IllegalArgumentException {
		if (!isValidPosition(position))
			throw new IllegalArgumentException();
		if(this.getWorld() != null){
			this.getWorld().getBlockAtPos(this.getPosition()).removeLog(this);
			this.position = position;
			this.getWorld().getBlockAtPos(this.getPosition()).addLog(this);
		}
		else this.position = position;
	}

	/**
	 * Return the carrier of this Log.
	 */
	@Basic
	public Unit getCarrier(){
		return this.carrier;
	}
	/**
	  * Set the carrier of this Log to a given Unit.
	  * @param	unit
	  * 		The given Unit to be set as the carrier
	  * 		of this Log.
	  * @post	If this Log can have the given Unit
	  * 		as a carrier, the Unit is set as the carrier
	  * 		of this Log.
	  * @throws	IllegalArgumentException
	  * 		An exception is thrown if the given unit is an
	  * 		invalid carrier.
	  */
	protected void setCarrier(Unit unit) 
			throws IllegalArgumentException{
		if(!canHaveAsCarrier(unit))
			throw new IllegalArgumentException("Invalid carrier!");
		this.carrier = unit;
		return;
	}
	
	/**
	 * Return a boolean stating whether or not the given
	 * 	Unit can be the carrier of this Log.
	 * @param 	unit
	 * 			The given Unit to be checked.
	 * @return	True if and only if this Log has no
	 * 			current carrier and the given Unit isn't
	 * 			carrying anything.
	 */
	public boolean canHaveAsCarrier(Unit unit){
		return((this.carrier == null) && !unit.isCarrying());
	}
	
	/**
	 * Remove the current carrier.
	 * @post	Tries to set the Log's position to the position
	 * 			of its carrier and remove the carrier of this Log.
	 * 			If a NullPointerException is caught, nothing happens.
	 */
	protected void removeCarrier(){
		try {
			setPosition(getCarrier().getPosition());
			this.carrier = null;
		} catch (NullPointerException exc) {
			return;
		} 
//		catch (IllegalArgumentException exc){
//			System.out.println("??????");
//		}
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public boolean canHaveAsWorld(World world){
		return (this.getWorld() == null);
	}
	
	public void setWorld(World world)
				throws IllegalArgumentException{
		if(!canHaveAsWorld(world))
			throw new IllegalArgumentException("Invalid world!");
		this.world = world;
	}
	
	public void removeWorld(){
		this.world = null;
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
	
	private World world = null;
}
