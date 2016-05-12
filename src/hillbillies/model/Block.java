package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
/**
 * Class representing a Block of a Hillbilly World.
 * @author Joost Croonen & Ruben Dedoncker.
 * 
 *
 */
public class Block{
	/**
	 * Initialize a new Block with a given position Vector and BlockType.
	 * @param 	vector
	 * 			The given position Vector.
	 * @param 	type
	 * 			The given BlockType.
	 * @post	The location of the new Block is set to the given Vector.
	 * @post	The BlockType of the new Block is set to the given BlockType.
	 */
	public Block (Vector vector, BlockType type){
		this.setLocation(vector);
		this.setBlockType(type);
	}
	
	/**
	 * Set the location of this Block to the given location.
	 * @param 	newlocation
	 * 			The given location to be set as the new location
	 * 			of this Block.
	 * 
	 * @post 	The location of this Block is equals the given
	 * 			location.
	 */
	@Basic
	protected void setLocation(Vector newlocation){
		this.location = newlocation;
	}
	
	/**
	 * Return location of this Block.
	 */
	@Basic
	public Vector getLocation(){
		return this.location;
	}
	
	/**
	 * Return a boolean stating whether or not this Block is solid.
	 */
	public boolean isSolid(){
		return(this.getBlockType() == BlockType.WOOD || this.getBlockType() == BlockType.ROCK);
	}
	/**
	 * Return a copy of the set containing the Units
	 * 	positioned in this Block.
	 */
	@Basic
	public Set<Unit> getUnitsInCube(){
		return new HashSet<Unit>(this.unitsInBlock);
	}
	
	/**
	 * Set the set of Units positioned in this Block to
	 * 	the given set.
	 * @param 	unitSet
	 * 			The given set of Units.
	 * @post	The set of Units positioned in the Block is
	 * 			set to the given set.
	 */
	@Basic
	protected void setUnitsInCube(Set<Unit> unitSet){
		this.unitsInBlock = unitSet;
	}
	
	/**
	 * Add a given Unit to the set of Units positioned in
	 * 	this Block.
	 * @param 	unit
	 * 			The given Unit to be added.
	 * @post	The new set of Units contains the given
	 * 			Unit.
	 */
	@Basic
	protected void addUnit(Unit unit){
		this.unitsInBlock.add(unit);
	}
	/**
	 * Remove a given Unit from the set of Units
	 * 	positioned in this Block.
	 * @param 	unit
	 * 			The given Unit to be removed.
	 * @post	The given Unit is removed from the
	 * 			set of Units positioned in this Block.
	 */
	@Basic
	protected void removeUnit(Unit unit){
		this.unitsInBlock.remove(unit);
	}
	/**
	 * Return the BlockType of this Block.
	 */
	@Basic
	public BlockType getBlockType(){
		return this.blocktype;
	}
	
	/**
	 * Set the BlockType of this Block to the given
	 * 	BlockType.
	 * @param 	newType
	 * 			The given BlockType.
	 * @post	The BlockType of this Block is set to
	 * 			the given BlockType.
	 * @post	
	 */
	@Basic
	public void setBlockType(BlockType newType){
		this.blocktype = newType;
		if (this.getWorld()!=null){
			if (newType==BlockType.ROCK || newType==BlockType.WOOD)
					this.getWorld().addSolidBlock(this);
			else this.getWorld().removeSolidBlock(this);
		}
	}
	/**
	 * Class representing a BlockType of a Block.
	 * 	The possible BlockTypes are the following: 
		- AIR
	 * 	- WORKSHOP
	 *  - ROCK
	 *  - WOOD
	 * @author Joost Croonen & Ruben Dedoncker.
	 *
	 */
	public enum BlockType{
		AIR, WORKSHOP, ROCK, WOOD
	}
	
	/**
	 * Return a set of Boulders positioned in this Block.
	 */
	@Basic
	public Set<Boulder> getBouldersInBlock(){
		return new HashSet<Boulder>(this.bouldersInBlock);
	}
	/**
	 * Return a set of Boulders positioned in this Log.
	 */
	@Basic
	public Set<Log> getLogsInBlock(){
		return new HashSet<Log>(this.logsInBlock);
	}
	
	/**
	 * Add a given Boulder to the set of Boulders
	 * 	positioned in this Block.
	 * @param 	boulder
	 * 			The given Boulder to be added.
	 * @post	The set of Boulders positioned in this Block
	 * 			contains the given Boulder.
	 */
	@Basic
	protected void addBoulder(Boulder boulder){
		this.bouldersInBlock.add(boulder);
	}
	
	/**
	 * Add a given Log to the set of Logs
	 * 	positioned in this Log.
	 * @param 	boulder
	 * 			The given Log to be added.
	 * @post	The set of Logs positioned in this Block
	 * 			contains the given Log.
	 */
	@Basic
	protected void addLog(Log log){
		this.logsInBlock.add(log);
	}
	 /**
	  * Remove a given Boulder from the set of Boulders
	  *  positioned in this Block.
	  * @param 	boulder
	  * 		The given Boulder to be removed from the
	  * 		set of Boulders positioned in this Block.
	  * @post	The given Boulder is removed from the
	  * 		set of Boulders positioned in this Block.
	  */
	@Basic
	protected void removeBoulder(Boulder boulder){
		this.bouldersInBlock.remove(boulder);
	}
	
	 /**
	  * Remove a given Log from the set of Logs
	  *  positioned in this Block.
	  * @param 	boulder
	  * 		The given Log to be removed from the
	  * 		set of Logs positioned in this Block.
	  * @post	The given Log is removed from the
	  * 		set of Logs positioned in this Block.
	  */	
	@Basic
	protected void removeLog(Log log){
		this.logsInBlock.remove(log);
	}
	
	/**
	 * Set the set of Boulders positioned in this Block
	 * 	to the given set of Boulders.
	 * @param 	newSet
	 * 			The given set of Boulders.
	 * @post	The set of Boulders positioned in the Block
	 * 			equals the given set.
	 */
	@Basic
	protected void setBouldersInCube(Set<Boulder> newSet){
		this.bouldersInBlock = newSet;
	}
	
	/**
	 * Set the set of Logs positioned in this Block
	 * 	to the given set of Logs.
	 * @param 	newSet
	 * 			The given set of Logs.
	 * @post	The set of Logs positioned in the Block
	 * 			equals the given set.
	 */
	@Basic
	protected void setLogsInCube(Set<Log> newSet){
		this.logsInBlock = newSet;
	}
	
	/**
	 * Return the World this Block is in.
	 */
	@Basic
	protected World getWorld(){
		return world;
	}
	
	/**
	 * Set the World of this Block to the given World.
	 * @param 	newWorld
	 * 			The given World.
	 * @post	The World of this Block equals the given World.
	 * @throws	IllegalArgumentException
	 * 			An exception is thrown if this Block already belongs
	 * 			to another world.
	 */
	@Basic
	protected void setWorld(World newWorld)
			throws IllegalArgumentException{
		if(this.getWorld() != null)
			throw new IllegalArgumentException("This Block already belongs to another World!");
		this.world = newWorld;
	}
	
	/**
	 * Remove the current World of this Block.
	 * @post	The current World of this Block is set to null.
	 */
	protected void removeWorld(){
		this.world = null;
	}
	/**
	 * World this block is part of	
	 */
	private World world = null;
	/**
	 * Location of this Block in the world
	 */
	private Vector location;
	/**
	 * The Type of this block.
	 * This can be ROCK, WOOD, WORKSHOP or AIR
	 */
	private BlockType blocktype;
	/**
	 * Set of units in this block
	 */
	private Set<Unit> unitsInBlock = new HashSet<Unit>();
	/**
	 * Set of boulders in this block
	 */
	private Set<Boulder> bouldersInBlock = new HashSet<Boulder>();
	/**
	 * Set of logs in this block
	 */
	private Set<Log> logsInBlock = new HashSet<Log>();
	
}