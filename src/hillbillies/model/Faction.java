package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;


/**
 * Class representing Faction.
 * @author Joost Croonen & Ruben Dedoncker
 * 
 * @invar 	The amount of Units of this Faction is less
 * 			than or equal to the maximum allowed amount (50).
 * 			| getUnits().size() <= Faction.MAX_UNITS
 * @invar	The members of this Faction are valid Units.
 * 			| for each Unit in getUnits() : isValidUnit(unit)
 * @invar	If a member of this Faction belongs to a World,
 * 			then this Faction's World is the same as the member's
 * 			World.
 * @invar	If this Faction doesn't belong to a World, none of its
 * 			members do either.
 */
public class Faction {

	/**
	 * Initialize this new Faction with given creator.
	 * 
	 * @param  	creator
	 *         	The creator for this new Faction.
	 * @param	name
	 * 			The given name of the Faction.
	 * @effect  If the given creator is a valid creator for any Faction,
	 *         	the creator is added as a member of the Faction and the creator's
	 *          Faction is set to this Faction. Otherwise, this Faction is terminated.
	 * @post	The name of the Faction is set to the given name.
	 * 
	 */
	public Faction(Unit creator, String name) {
		if (!isValidUnit(creator))
			terminate();
		else{
			creator.setFaction(this);
			this.name = name;
		}
	}
	/**
	 * Initialize this new Faction with given creator.
	 * 
	 * @param  creator
	 *         The creator for this new Faction.
	 * @post  If the given creator is a valid creator for any Faction,
	 *         	the creator is added as a member of the Faction and the creator's
	 *          Faction is set to this Faction. Otherwise, this Faction is terminated.
	 * @post	The name of the Faction is set to "Faction".
	 */          
	public Faction(Unit creator){
		if (!isValidUnit(creator))
			terminate();
		else{
			creator.setFaction(this);;
			this.name = "Faction";
		}
	}
	/**
	 * Return the set of Units belonging to this Faction.
	 */
	@Basic
	public Set<Unit> getUnits(){
		return new HashSet<Unit>(this.unitSet);
	}
	/**
	 * Add a Unit to the Faction if possible.
	 * @param 	Unit
	 * 			The given Unit to be added to the Faction.
	 * @post 	If the given Unit is a valid Unit
	 * 			and number of Units of this Faction is less than
	 * 			the maximum allowed number, the Unit is added to
	 * 			the Faction.
	 * 			
	 */
	protected void addUnit(Unit unit){
		if (isValidUnit(unit) && getUnits().size() < Faction.MAX_UNITS)
		this.unitSet.add(unit);
	}
	/**
	 * Return a boolean stating whether or not the given Unit is valid.
	 * @param	Unit
	 * 			The given Unit to be checked.
	 * @return	True of and only if the Unit's Faction can be set
	 * 			to this Faction.
	 */
	protected boolean isValidUnit(Unit unit){
		return unit.canHaveAsFaction(this);
	}
	/**
	 * Return a string containing the name of this Faction.
	 */
	@Basic
	public String getName(){
		return this.name;
	}
	/**
	 * Set the name of the Faction to the given name.
	 * @param 	name
	 * 			The given name to be set as the new name of the Faction.
	 * @post	If the Faction isn't terminated, the given name is the new name of the Faction.
	 * 			Otherwise, nothing happens.
	 */
	
	public void setName(String name){
		if (!isTerminated())
			this.name = name;
	}
	
	/**
	  * Return a boolean indicating whether or not this Faction
	  * is terminated.
	  */
	@Basic @Raw
	public boolean isTerminated(){
		return this.terminated;
	}
	/**
	 * Remove a Unit from the set of Units belonging to this Faction.
	 * @param 	Unit
	 * 			The given Unit to be removed from the set of Units
	 * 			belonging to this Faction.
	 * @post	The Unit is removed if it can be removed.
	 */
	protected void removeUnit(Unit unit){
		if (canBeRemoved(unit))
			this.unitSet.remove(unit);
	}
	/**
	 * Return a boolean stating whether or not a given Unit
	 * 	can be removed from the set of Units belonging to this Faction.
	 * @param	Unit
	 * 			The given Unit to be checked.
	 * @return	True if and only if the Unit's Faction does not equal
	 * 			this Faction.
	 */
	@Basic
	protected boolean canBeRemoved(Unit unit){
		return unit.getFaction() != this;
	}
	/**
	 * Return the World this Faction belongs to.
	 */
	@Basic
	protected World getWorld(){
		return this.world;
	}
	/**
	 * Set the World of this Faction to the given World.
	 * @param 	world
	 * 			The given World to be set as this Faction's World.
	 * @post	The given World equals the World of this Faction.
	 */
	@Basic
	protected void setWorld(World world){
		if(!canHaveAsWorld(world) || world == null)
			return;
		this.world = world;
	}
	/**
	 * Remove this Faction's World.
	 * @post	The World of this Faction is null.
	 */
	@Basic
	protected void removeWorld(){
		for(Unit unit : this.getUnits())
			unit.removeWorld();
		this.world = null;
	}
	/**
	 * Return a boolean stating whether or not this Faction
	 * 	can belong to the given World.
	 * @param 	world
	 * 			The given World to be checked.
	 * @return	True if and only if this Faction is not terminated
	 * 			and this Faction doesn't belong to a World.
	 */
	protected boolean canHaveAsWorld(World world){
		return(this.getWorld() == null && !this.isTerminated());
	}
	/**
	 * Return a boolean stating whether or not this Faction is terminated.
	 * 	A Faction can be terminated if it contains no Units.
	 */
	@Basic
	protected boolean canBeTerminated(){
		return getUnits().isEmpty();
	}
	/**
	 * Terminate this Faction.
	 *
	 * @post   This Faction is terminated if it is allowed.
	 */
	protected void terminate(){
		if (canBeTerminated())
			this.terminated = true;
		return;
	}
	/**
	 * Set of all the units in this faction
	 */
	private Set<Unit> unitSet = new HashSet<Unit>();
	/**
	 * Maximum number of units allowed in this faction.
	 * This is a static variable and cannot be changed.
	 */
	private static final int MAX_UNITS = 50;
	/**
	 * Name of this faction
	 */
	private String name;
	/**
	 * Boolean indicating whether this faction has been terminated or not.
	 */
	private boolean terminated = false;
	/**
	 * World this faction is part of
	 */
	private World world = null;
	}
