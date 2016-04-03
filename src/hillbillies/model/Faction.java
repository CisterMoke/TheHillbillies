package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;


/**
 * Class representing faction.
 * @author Joost Croonen & Ruben Dedoncker
 * 
 * @invar 	The amount of units of this faction is less
 * 			than or equal to the maximum allowed amount (50).
 * 			| getUnits().size() <= Faction.MAX_UNITS
 * @invar	The members of this faction are valid units.
 * 			| for each unit in getUnits() : isValidUnit(unit)
 */
public class Faction {

	/**
	 * Initialize this new faction with given creator.
	 * 
	 * @param  	creator
	 *         	The creator for this new faction.
	 * @param	name
	 * 			The given name of the faction.
	 * @effect  If the given creator is a valid creator for any faction,
	 *         	the creator is added as a member of the faction and the creator's
	 *          faction is set to this faction. Otherwise, this faction is terminated.
	 * @post	The name of the faction is set to the given name.
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
	 * Initialize this new faction with given creator.
	 * 
	 * @param  creator
	 *         The creator for this new faction.
	 * @post  If the given creator is a valid creator for any faction,
	 *         	the creator is added as a member of the faction and the creator's
	 *          faction is set to this faction. Otherwise, this faction is terminated.
	 * @post	The name of the faction is set to "Faction".
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
	 * Returns the set of units belonging to this faction.
	 */
	@Basic
	public Set<Unit> getUnits(){
		return new HashSet<Unit>(this.unitSet);
	}
	/**
	 * Adds a unit to the faction if possible.
	 * @param 	unit
	 * 			The given unit to be added to the faction.
	 * @post 	If the given unit is a valid unit
	 * 			and number of units of this faction is less than
	 * 			the maximum allowed number, the unit is added to
	 * 			the faction.
	 * 			
	 */
	protected void addUnit(Unit unit){
		if (isValidUnit(unit) && getUnits().size() < Faction.MAX_UNITS)
		this.unitSet.add(unit);
	}
	/**
	 * Returns a boolean stating whether or not the given unit is valid.
	 * @param	unit
	 * 			The given unit to be checked.
	 * @return	True of and only if the unit's faction can be set
	 * 			to this faction.
	 */
	public boolean isValidUnit(Unit unit){
		return unit.canHaveAsFaction(this);
	}
	/**
	 * Returns a string containing the name of this faction.
	 */
	@Basic
	public String getName(){
		return this.name;
	}
	/**
	 * Sets the name of the faction to the given name.
	 * @param 	name
	 * 			The given name to be set as the new name of the faction.
	 * @post	If the faction isn't terminated, the given name is the new name of the faction.
	 * 			Otherwise, nothing happens.
	 */
	
	public void setName(String name){
		if (!isTerminated())
			this.name = name;
	}
	
	/**
	  * Return a boolean indicating whether or not this faction
	  * is terminated.
	  */
	@Basic @Raw
	public boolean isTerminated(){
		return this.terminated;
	}
	/**
	 * Remove a unit from the set of units belonging to this faction.
	 * @param 	unit
	 * 			The given unit to be removed from the set of units
	 * 			belonging to this faction.
	 * @post	The unit is removed if it can be removed.
	 */
	public void removeUnit(Unit unit){
		if (canBeRemoved(unit))
			this.unitSet.remove(unit);
	}
	/**
	 * Return a boolean stating whether or not a given unit
	 * 	can be removed from the set of units belonging to this faction.
	 * @param	unit
	 * 			The given unit to be checked.
	 * @return	True if and only if the unit's faction does not equal
	 * 			this faction.
	 */
	@Basic
	public boolean canBeRemoved(Unit unit){
		return unit.getFaction() != this;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	protected void setWorld(World world){
		this.world = world;
	}
	
	protected void removeWorld(){
		this.world = null;
	}
	/**
	 * Return a boolean stating whether or not this faction is terminated.
	 */
	@Basic
	public boolean canBeTerminated(){
		return getUnits().isEmpty();
	}
	/**
	 * Terminate this faction.
	 *
	 * @post   This faction is terminated if it is allowed.
	 */
	public void terminate(){
		if (canBeTerminated())
			this.terminated = true;
		return;
	}
	
	private Set<Unit> unitSet = new HashSet<Unit>();
	private static final int MAX_UNITS = 50;
	private String name;
	private boolean terminated = false;
	private World world = null;
	}
