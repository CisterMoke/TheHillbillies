
package hillbillies.model;
import java.util.*;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.model.Block.BlockType;
/**
 * Class respresenting a Hillbilly unit of the game.
 * @author Joost Croonen & Ruben Dedoncker
 * 
 * @invar 	The unit has a valid name.
 * 			|unit.isValidName(unit.getName())
 * @invar 	The unit has a valid position.
 * 			|isValidPosition(getPosition())
 * @invar	The unit's primary stats lie between 1 and 200 inclusively
 * 			| for (key in unit.getPrimStats())
 * 			|	(getPrimStats.get(key) >=1) &&
 * 			|	(getPrimStats.get(key) <= 200)
 * @invar	The unit has a valid amount of hitpoints and stamina points
 * 			| isValidHp(getHp()) && isValidStam(getStam())
 * @invar	The weight of the unit is bigger than or equal to
 * 			the sum of the strength and the agility, divided by two.
 * 			| getPrimStats().get("wgt")
 * 			|	 >= (getPrimStats().get("str") + getPrimStats("agl")) / 2)
 * @invar	A unit must always belong to a faction
 * 			| getFaction() != null
 * @invar	If a unit belongs to a world, it must be the same as its faction.
 * 			|if(getWorld() != null)
 * 			|	then getWorld() == getFaction().getWorld()
 *
 */
public class Unit {
	/**
	 * Initialize a new Hillbilly unit with the given name, position and primary stats.
	 * @param name
	 * 			The name of the unit.
	 * @param x
	 * 			The x-coordinate of the unit's position.
	 * @param y
	 * 			The y-coordinate of the unit's position.
	 * @param z
	 * 			The z-coordinate of the unit's position.
	 * @param str
	 * 			The unit's strength.
	 * @param wgt
	 * 			The unit's weight.
	 * @param agl
	 * 			The unit's agility.
	 * @param tgh
	 * 			The unit's toughness.
	 * @effect	The unit's primary stats are set to a valid number, 
	 * 			based on the given stats.
	 * 			|setPrimStats({"str":str, "wgt":wgt, "agl":agl, "tgh":tgh})
	 * @post	The amount of hitpoints and stamina points of the unit
	 * 			is set to the maximum allowed amount.
	 * 			| new.getHp() == getMaxHp() &&
	 * 			| new.getStam() == getMaxStam()
	 * @effect	The position vector of the unit is set to the given position vector.
	 * 			| setPosition(Vector(x, y, z))
	 * @effect	The orientation of the unit is set to pi/2.
	 * 			| setTheta(Math.PI/2)
	 * @post	The base speed of the unit equals
	 * 			1.5 * (strength + agility)/(2*totalWeight).
	 * 			| new.getBaseSpeed() ==
	 * 			|	1.5 * (getPrimStats().get("str")+getPrimStats().get("agl"))
	 * 			|			/(2*getTotalWeight())
	 * @post	The state of the unit is IDLE.
	 * 			| new.getState() == State.IDLE
	 * @effect	The Faction of the unit is set to a new Faction,
	 * 			founded by this Unit.
	 * 			| setFaction(newFaction(this))
	 * @throws IllegalArgumentException
	 * 			An exception is thrown is the name is invalid or if the position is invalid
	 * 			| !isValidName(name) || !isValidPosition(Vector(x, y, z))
	 */
	public Unit(String name,double x,double y,double z,int str,int wgt,int agl,int tgh) throws IllegalArgumentException{
		HashMap<String, Integer> firstStats = new HashMap<String, Integer>();
		firstStats.put("str", str);
		firstStats.put("wgt", wgt);
		firstStats.put("agl", agl);
		firstStats.put("tgh", tgh);
			
		this.setName(name);
		this.setPrimStats(firstStats);
		this.setHp(this.getMaxHp());	
		this.setStam(this.getMaxStam());
		this.setPosition(new Vector(x, y, z));
		this.setTheta(Math.PI/2);
		this.setTarget(this.getPosition());
		this.setState(State.IDLE);
		this.setFaction(new Faction(this));
		
	}
	/**
	 * Update the unit's state, position, target and finalTartget, 
	 * 	cooldown times(such as attackCooldown, workTime,...), and
	 * 	hitpoints and stamina points based on the given time interval.
	 * @param dt
	 * 			The time interval in seconds.
	 * @post	Nothing happens if the Unit is terminated.
	 * @post	If the minimum required rest time is bigger than zero,
	 * 			the given time interval is subtracted from it.
	 * @post	If the attack cooldown is bigger than zero,
	 * 			the given time interval is subtracted from it.
	 * @effect	If this Unit should be falling and it isn't currenlty falling,
	 * 			its state will initiate its fall.
	 * @post	If the Unit's state is FALLING,  its position will be set
	 * 			based on the given time interval. If the Unit shouldn't be
	 * 			falling anymore, it will land on its current position.
	 * @post	This Unit will exhibit default behaviours if it is switched on.
	 * @post	If the unit's state is COMBAT, it will attack its opponent if it has
	 * 			one. If it hasn't got an opponent and its set of attackers is empty,
	 * 			this unit's state will be set to IDLE.
	 * @post	If the unit's state is RESTING and hasn't fully recovered its hitpoints,
	 * 			an amount of (toughness/200 * dt/0.2) is added. If the new
	 * 			amount of hitpoints is invalid, it will be set to the maximum
	 * 			allowed amount.
	 * @post	If the unit's state is RESTING and has fully recovered its hitpoints but 
	 * 			hasn't fully recovered its stamina points, an amount of 
	 * 			(toughness/100 * dt/0.2) will be added. If the new
	 * 			amount of stamina points is invalid, it will be set to the maximum
	 * 			allowed amount.
	 * @post	If the unit's state is WORKING and its work time is bigger than zero,
	 * 			the given time interval will be subtracted from the work time.
	 * 			On the other hand, if the unit's state is WORKING and its work time
	 * 			is smaller than or equal to zero, its work task will be completed.
	 * @effect	If the unit's state is IDLE and it hasn't reached its final target,
	 * 			it will move to its final target.
	 * @post	If the unit's state is SPRINTING and its stamina is bigger than zero,
	 * 			an amount of dt/0.1 will be subtracted from its stamina. If the new
	 * 			amount of stamina points is invalid, it will be set to zero.
	 * @effect	If the unit is moving, its next position is calculated using
	 * 			the given time interval and the unit's current velocity vector.
	 * 			If the next position lies further than the target, the unit's
	 * 			position is set to the target. If it's not the case then
	 * 			the unit's position is set to the next position.
	 * 			If an IllegalArgumentException is caught in this process,
	 * 			the unit's target is set to its current position.
	 * @throws IllegalArgumentException
	 * 			An exception is thrown if the given time interval is smaller than
	 * 			0 or bigger than 0.2 seconds.
	 */
	public void advanceTime(double dt)throws IllegalArgumentException{
		if(this.isTerminated())
			return;
		if (dt<0 || dt>0.2)
			throw new IllegalArgumentException("Invalid time interval!");
		if (this.getMinRestTime() > 0){
			this.setMinRestTime(this.getMinRestTime()- dt);
		}
		if (this.getAttackCooldown() > 0){
			this.setAttackCooldown(this.getAttackCooldown() - dt);
		}
		
		if(this.shouldFall() && this.getState() != State.FALLING){
			this.fall();
		}
		if(this.getState() == State.FALLING){
			Vector stepSize = this.getV_Vector().multiply(dt);
			Vector newPos = this.getPosition().add(stepSize);
			this.setPosition(newPos);
			if(!this.shouldFall())
				this.land();
			return;
		}
		
		if(this.DefaultOn())
			this.defaultBehaviour();
			
		
		if(this.getState() == State.COMBAT){
			if(this.opponent == null){
				if (this.getAttackers().isEmpty())
				this.setState(State.IDLE);
			}
			else this.attack(opponent);
			return;
		}
		if (this.restTime >= 180){
			this.rest();
		}
		else {
			if (this.getState()!=State.RESTING)
				this.setRestTime(this.getRestTime()+dt);
		}
		
		if (this.getState() == State.RESTING){
			this.rest();
			this.setRestTime(0);
			if (this.getHp()<this.getMaxHp()){
				double newHp = this.getHp() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.2);
				if(!this.isValidHp(newHp)){
					newHp = this.getMaxHp();
				}
				this.setHp(newHp);
			}
			else{
				if (this.getStam()<this.getMaxStam()){
					double newStam = this.getStam() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.1);
					if(!this.isValidHp(newStam)){
						newStam = this.getMaxStam();	
					}
					this.setStam(newStam);
				}
				else{
					this.setState(State.IDLE);
				}
			}
			return;
		}
		if (this.getState() == State.WORKING){
			if(this.getWorkTime() > 0){
				this.setWorkTime(this.getWorkTime() - dt);
				return;
			}
			else{
				this.workCompleted();
			}
				
			
		}
		if (this.getState() == State.IDLE){
			if(this.getFinTarget() != null){
				move2(this.getFinTarget());
			}
			if(!this.getTarget().equals(this.getPosition())){
				this.setState(State.WALKING);
				this.setTheta(Math.atan2(v_vector.getY(),v_vector.getX()));
			}
		}
		if (this.getTarget().equals(this.getPosition()) && this.getFinTarget() == null && this.getState() != State.IDLE){
			this.setState(State.IDLE);
			return;
		}
		
		if (this.isMoving()){
			if (this.getState() == State.SPRINTING){
				if (this.getStam()  <= 0)
					this.setState(State.WALKING);
				else{
					double newStam = this.getStam() - dt/0.1;
					if (!this.isValidStam(newStam)){
						newStam = 0;
					}
					this.setStam(newStam);
				
				}
			}
			Vector stepSize = this.getV_Vector().multiply(dt);
			Vector nextPos = this.getPosition().add(stepSize);
			Vector nextDirection = this.getTarget().add(nextPos.getOpposite()).normalize();
			Vector currentDirection = this.getV_Vector().normalize();
			
			Vector subtraction = nextDirection.add(currentDirection.getOpposite());
			boolean moved = false;
			if(subtraction.getLength() > 0.5){
				this.setPosition(this.getTarget());
				this.addExp(1);
				moved = true;
				if (this.getFinTarget() != null){
					this.move2(this.getFinTarget());
				}
			}
			if (!moved){
				try{
					this.setPosition(nextPos);
				}
				catch (IllegalArgumentException exc){
					this.setTarget(this.getBlockCentre());
					
				}
				moved = true;
			}
		}
	}
	
	/**
	 * Return the name of this unit.
	 * 
	 */
	@Basic
	public String getName(){
		return this.name;
	}
	
	/**
	 * Change the name of the unit to a valid name.
	 * @param newname
	 * 			The new name of the unit.
	 * @post The given name is the new name of the unit
	 * 			| new.getName() == newname
	 * @throws IllegalArgumentException
	 * 			Throws an exception when the given name is invalid.
	 * 			| ! isValidName(newname)
	 */
	public void setName(String newname) throws IllegalArgumentException{
		if (! isValidName(newname)){
			throw new IllegalArgumentException("Invalid name!");
		}
		else{
			this.name = newname;
		}
	}
	/**
	 * Return a boolean stating whether or not the given name is a valid name.
	 * @param name
	 * 			The name to be checked.
	 * @return True if the name only contains letters and valid characters,
	 * 			has the defined minimal length and starts with a capital letter.
	 * 			| checker == true
	 * 			| for each character in name : (
	 * 			| 	if(!Character.isLetter(character) && !getValidChars().contains(character))
	 * 			|		then checker == false)
	 * 			| result == (checker) && (name.length() >= getMinNameLength()) && (name.isUpperCase(name.charAt(0)))
	 */
	private boolean isValidName(String name){
		boolean checker = true;
		for (int i=0; i<name.length();i++){
			if (!Character.isLetter(name.charAt(i)) && !this.getValidChars().contains(String.valueOf(name.charAt(i))))
				checker = false;
		}
		return Character.isUpperCase(name.charAt(0)) && name.length() >= this.getMinNameLength() && checker;
	}
	/**
	 * 
	 * Return a set containing the valid characters, excluding letters.
	 */
	@Basic @Immutable
	private Set<String> getValidChars(){
		return new HashSet<String>(Unit.VALIDCHARS);
	}
	/**
	 * 
	 * Returns the minimal length for a valid name.
	 */
	@Basic @Immutable
	private int getMinNameLength(){
		return Unit.NAMELENGTH_MIN;
	}
	
	/**
	 * Set the position vector of the unit to the given position vector.
	 * @param 	position
	 * 			The given position vector.
	 * @throws 	IllegalArgumentException
	 * 			Throws an exception when an illegal position is given.
	 * 			| !isValidPosition(Vector(x, y, z))
	 * @effect 	If the unit exists in a world, this unit will be removed
	 * 			from the block it was previously in and will be added to the
	 * 			new block it is in.
	 * 			| if(getWorld() != null)
	 * 			|	then !getWorld().getBlockAtPos(old.getPosition()).getUnits().contains(this) &&
	 * 			|	getWorld().getBlockAtPosition(new.getPosition()).getUnits().contains(this)
	 * @post	 The new position is a valid position.
	 * 			| new.getPosition() == Vector(x, y, z) && isValidPosition(new.getPosition())
	 * 
	 */
	public void setPosition(Vector position)throws IllegalArgumentException{
		if (!isValidPosition(position))
			throw new IllegalArgumentException("Out of bounds");
		if(this.getWorld() != null){
			this.getBlock().removeUnit(this);
			this.pos = position;
			this.getBlock().addUnit(this);
		}
		else this.pos = position;
	}
	/**
	 * Return the vector representing the current position of the unit.
	 * 
	 */
	@Basic
	public Vector getPosition(){
		return this.pos;
	}
	/**
	 * Return a boolean stating whether or not the given position lies within the boundaries.
	 * @param pos
	 * 			The position vector the be checked.
	 * @return True if and only if every coefficient of the given position vector lies between 0 and 50 inclusively.
	 * 			| result == for each coefficient in pos.getCoeff() : (coefficient >= 0 && coefficient =< 50)
	 */
	private boolean isValidPosition(Vector pos){
		if(this.getWorld() == null)
			return true;
		ArrayList<Double> coords = pos.getCoeff();
		for(int i=0; i<3; i++){
			if (coords.get(i) >= this.getWorld().getBorders().get(i) || coords.get(i)<0)
				return false;
		}
		if(this.getWorld().getBlockAtPos(pos).isSolid())
			return false;
		return true;
	}
	/**
	 * Return a set containing the keys of a valid map
	 * 	of primary stats.
	 */
	public static Set<String> getPrimStatSet(){
		return new HashSet<String>(Unit.primStatSet);
	}
	/**
	 * 
	 * Returns a map containing the primary stats of the unit.
	 */
	@Basic
	public Map<String, Integer> getPrimStats(){
		return new HashMap<String, Integer>(this.primStats);
	}
	/**
	 * Set the primary stats of the unit to the given stats.
	 * @param primStats
	 * 			A map containing the new primary stats.
	 * @post	Nothing happens if the given map of primary stats is of the wrong format.
	 * 			| if(!primStats.keySet().equals(getPrimStatSet()))
	 * 			|	then return
	 * @post	The values of the new primary stats are set to the values of the given map
	 * 			containing the new primary stats.
	 * 			| new.getPrimStats() == primStats
	 * @post	The values of the new primary stats lie between 1 and 200 inclusively.
	 * 			| for each key in new.getPrimStats().keySet():(
	 * 			|	(new.getPrimStats().get(key) >= 1) &&
	 * 			|	(new.getPrimStats().get(key) <= 200))
	 * @post	If there are no current primary stats, the new primary stats lie between
	 * 			25 and 100 inclusively.
	 * 			| if(getPrimStats.isEmpty())
	 * 			|	then for each key in new.getPrimStats.keySet(): (
	 * 			|		(new.getPrimStats().get(key) >= 25) &&
	 * 			|		(new.getPrimStats().get(key) <= 100))
	 * @post	The new weight of the unit is a valid weight.
	 * 			| hasValidWeight(new.getPrimStats())
	 * @post	The new primary stats only contains the unit's strength, agility,
	 * 			weight and toughness.
	 * 			| new.getPrimStats.keySet().equals(getPrimStatSet())
	 */
	public void setPrimStats(Map<String, Integer> primStats){
		if (!primStats.keySet().equals(Unit.primStatSet)){
			return;
		}
		for(String key : primStats.keySet()){
			if(this.getPrimStats().isEmpty()){
				if(primStats.get(key) < 25){
					primStats.put(key, 25);
				}
				if(primStats.get(key) > 100){
					primStats.put(key, 100);
				}
			}
			if (primStats.get(key) < 1){
				primStats.put(key, 1);
			}
			if (primStats.get(key) > 200){
				primStats.put(key, 200);
			}
		}
		if (!this.hasValidWeight(primStats)){
			primStats.put("wgt", (int)(Math.ceil(((double)(primStats.get("str"))+(double)(primStats.get("agl"))) / 2)));
		}		
		 this.primStats = new HashMap<String, Integer>(primStats);
	}
		 
	/**
	 * Return the orientation of this unit in the x-y plane in radians.
	 */
	@Basic
	public Double getTheta(){
		return this.theta;
	}
	/**
	 * Set the orientation of this unit to the given angle.
	 * @param angle
	 * 			The new angle of orientation of the unit.
	 * 
	 * @post The orientation of the unit is set to the given angle.
	 * 			|new.getTheta() == angle
	 */
	@Basic
	public void setTheta (Double angle){
		this.theta = angle;
	}
	
	/**
	 * Return the vector of the center of the block in which the unit is located.
	 */
	public Vector getBlockCentre(){
		double blockX = Math.floor(this.pos.getX()) + 0.5;
		double blockY = Math.floor(this.pos.getY()) + 0.5;
		double blockZ = Math.floor(this.pos.getZ()) + 0.5;
			
		return  new Vector(blockX, blockY, blockZ);
	}
	/**
	 *  Initiate the movement of the unit to a given adjacent block.
	 * @param dx
	 * 			The difference between the x-coordinate of the selected adjacent block
	 * 			and the block the unit is currently standing in.
	 * @param dy
	 * 			The difference between the y-coordinate of the selected adjacent block
	 * 			and the block the unit is currently standing in.
	 * @param dz
	 * 			The difference between the z-coordinate of the selected adjacent block
	 * 			and the block the unit is currently standing in.
	 * 
	 * @post	Nothing happens if the unit is moving and the unit hasn't reached its target.
	 * 			| if (isMoving()) && (!getTarget().equals(getPosition()))
	 * 			|	then return
	 * @post	If the unit is in combat, nothing happens.
	 * 			| if (getState() == State.COMBAT)
	 * 			|	then return
	 * @effect  A new target is set to the center of the given adjacent block.
	 * 			| newTarget == this.getBlockPosition().add(dx + 0.5, dy + 0.5, dz + 0.5)
	 * 			| setTarget(newTarget)
	 * @effect	A new velocity vector is created, pointing in the direction of the target.
	 * 			| setV_Vector()
	 * @effect	The orientation of the unit is set in the direction of the velocity vector.
	 * 			| this.setTheta(arctan(this.getV_Vector.get(1)/this.getV_Vector(0)))
	 * @effect	When going down, the walking speed is set to 1.2*BaseSpeed.
	 * 			When going up, the walking speed is set to 0.5*BaseSpeed.
	 * 			Otherwise the walking speed is set to BaseSpeed.
	 * 			| if(dz == -1)
	 * 			|	then setWalkSpeed(1.2*getBaseSpeed())
	 * 			| else if(dz == 1)
	 * 			|	then setWalkSpeed(0.5*getBaseSpeed())
	 * 			| else setWalkSpeed(getBaseSpeed())
	 * 			|
	 * @effect	If the unit isn't sprinting, the unit is set to walk.
	 * 			|if(getState() != State.SPRINTING)
	 *			| 	then setState(State.WALKING)
	 * 
	 * @throws IllegalArgumentException
	 * 			Throw an exception when the given block is not adjacent to the block the unit is standing in.
	 * 			| if ((|dx| > 1) || (|dy| > 1 )|| (|dz| > 1))
	 *			|	then throw new IllegalArgumentException
	 */
	public void moveToAdjacent(int dx, int dy, int dz)throws IllegalArgumentException{
		if (this.isMoving() && !this.getTarget().equals(this.getPosition())){
			return;
		}
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1 || Math.abs(dz) > 1){
			throw new IllegalArgumentException("parameters must lie between -1 and 1!");
		}
		if (this.getState() == State.COMBAT)
			return;
		if(this.getState() != State.SPRINTING)
			this.setState(State.WALKING);
		try{
			this.setWalkSpeed(this.getBaseSpeed());
			if (dz == -1)
				this.setWalkSpeed(this.getBaseSpeed()*1.2);
			if (dz == 1)
				this.setWalkSpeed(this.getBaseSpeed()*0.5);
		}
		catch (IllegalArgumentException exc){
			this.setWalkSpeed(-this.getBaseSpeed());
			if (dz == -1)
				this.setWalkSpeed(-this.getBaseSpeed()*1.2);
			if (dz == 1)
				this.setWalkSpeed(-this.getBaseSpeed()*0.5);
		}
		Vector target = this.getBlockCentre().add(dx, dy, dz);
		this.setTarget(target);
	}
	/**
	 * Initiate the movement of a unit to a given position vector.
	 * 	The position is set to the centre of the block containing
	 * 	this position.
	 * @param 	pos
	 * 			The given position vector.
	 * @post	Nothing happens if the given position is invalid.
	 * 			| if(!isValidPosition(pos))
	 * 			|	return
	 * @post	The final target and the path are removed if the unit's position equals
	 * 			the position of the final target. The method then ends here
	 * 			|if(getPosition.equals(getFinTarget())
	 * 			|	then new.getFinTarget() == null &&
	 * 			|	new.getPath().isEmpty() &&
	 * 			|	return
	 * @post	The current step of the path is removed if the unit has reached
	 * 			the block of the current step and the path isn't empty.
	 * 			|if(!getPath().isEmpty() && getBlock == getPath.get(0))
	 * 			|	then !new.getPath().contains(this.getPath().get(0))
	 * @effect	Tries to create a new final target using the given coordinates.
	 * 			The method will return here if it fails to create one.
	 * 			| newBlock == getWorld().getBlockAtPos(pos)
	 * 			| newTarget == newBlock().getLocation().add(0.5, 0.5, 0.5)
	 * 			| if(setFinTarget(newTarget) throws IllegalArgumentException)
	 * 			|	then return
	 * 			| else setFinTarget(newTarget)
	 * @effect	If the unit's position equals its target and it's not the case
	 * 			that there is a path and that the block containing the final target
	 * 			is equal to the last step of the path, a new path is made.
	 * 			| targetBlock == getWorld.getBlockAtPosition(getTarget())
	 * 			| if(getPosition().equals(getTarget()) && 
	 * 			|	!(getPath().isEmpty && targetBlock.equals(getPath().get(getPath.size() - 1))))
	 * 			|	then pathFinding()
	 * @effect	The method returns at this point if the current path is empty.
	 * 			| if(getPath().isEmpty())
	 * 			|	then return	
	 * @effect	Tries to move to the centre of a block adjacent to the target
	 * 			and on the path towards the final target. If an IllegalArgumentException
	 * 			is caught, the method returns.	
	 * 			| targetBlockPos == getWorld().getBlockAtPos(getTarget).getLocation()
	 * 			| nextBlockPos == getPath.get(0).getLocation()
	 * 			| newBlockPos == nextBlock.add(targetBlock.getOpposite())
	 * 			| if(moveToAdjacent(newBlock.getX(), newBlock.getY(), newBlock.getZ())
	 * 			|		throws IllegalArgumentException)
	 * 			|	then return
	 * 			| else moveToAdjacent(newBlock.getX(), newBlock.getY(), newBlock.getZ()
	 */
	public void move2(Vector pos){
		if(!isValidPosition(pos))
			return;
		if (this.getPosition().equals(this.getFinTarget())){
			this.finTarget = null;
			this.Path.clear();
			return;
		}
		if(!this.getPath().isEmpty() && this.getBlock() == this.getPath().get(0)){
			this.Path.remove(0);
		}
		Block newBlock = this.getWorld().getBlockAtPos(pos);
		try{
			Vector newTarget = newBlock.getLocation().add(0.5, 0.5, 0.5);
			this.setFinTarget(newTarget);
		}
		catch(IllegalArgumentException exc){
			return;
		}
		if (this.getPosition().equals(this.getTarget())){
			if (this.getPath().isEmpty() ||
					!newBlock.equals(this.getPath().get(this.getPath().size() -1))){
				this.pathFinding();
			}
		}
		if (this.getPath().isEmpty())
			return;
		Vector currentStep = this.getWorld().getBlockAtPos(this.getTarget()).getLocation();
		Vector nextStep = this.getPath().get(0).getLocation();
		Vector newPos = nextStep.add(currentStep.getOpposite());
		try{
			this.moveToAdjacent((int)(newPos.getX()),(int) (newPos.getY()),(int) (newPos.getZ()));
		}
		catch(IllegalArgumentException exc){
			System.out.println("??????");
			return;
		}
	}
	
	
	
	
	
	/**
	 * Update the unit's velocity vector.
	 * @effect 	A velocity vector is created pointing in the direction of the target.
	 * 			| subtraction == this.getPosition().add(this.getTarget().getOpposite())
	 * 			| newV_Vector == subtraction.normalize().multiply(this.getSpeed())
	 * 			| new.getV_Vector() == newV_Vector
	 */
	private void setV_Vector(){
		if(this.getState() == State.FALLING){
			this.v_vector = new Vector(0, 0, -3);
			return;
		}
		this.v_vector = this.getTarget().add(this.getPosition().getOpposite());
		this.v_vector = this.v_vector.normalize().multiply(this.getSpeed());
	}
	/**
	 * Return the velocity vector of this unit.
	 */
	@Basic
	private Vector getV_Vector(){
		return this.v_vector;
	}
	
	/**
	 * 	Initiate the attack of this unit on the given defending unit.
	 * @param defender
	 * 			The defending unit against which the attack is initiated.
	 * @post	Nothing happens if the unit hasn't rested long enough or
	 * 			if it's moving.
	 * 			| if(getMinRestTime() > 0 || isMoving())
	 *			|	then return
	 * @effect	Tries to set the defender as the unit's opponent and
	 * 			add this unit to the defender's set of attackers.
	 * 			If an IllegalArgumentException is caught, nothing happens
	 * 			| if(defender.addAttacker(this) || setOpponent(defender) throws IllegalArgumentException)
	 *			| 	then return
	 *			| else defender.addAttacker(this) &&
	 *			|	setOpponent(defender)
	 * @effect	The orientation of this unit and the defender are set so that they
	 * 			face each other.
	 * 			| new.getTheta() ==
	 * 			|	Math.atan2(defender.getPosition().getY()-getPosition().getY(),
	 * 			|				defender.getPosition().getX()-getPosition().getX())
	 * 			| 	&& defender.new.getTheta() == getTheta() + Math.PI
	 * @post	The attack cooldown is set to 1 second.
	 * 			| new.getAttackCooldown == 1
	 * @post	The states of both units are set to COMBAT.
	 * 			| new.getState() == State.COMBAT &&
	 * 			| defender.new.getState() == State.COMBAT
	 * @post	The attack on the defender is initiated.
	 * 			| new.isAttackInitiated() == true
	 */ 
	private void initiateAttack(Unit defender){
		if(this.getMinRestTime() > 0 || this.isMoving()){
			return;
		}
		try{
			this.setOpponent(defender);
			defender.addAttacker(this);
		}
		catch(IllegalArgumentException exc){
			return;
		}
		double dy = defender.getPosition().getY()-this.getPosition().getY();
		double dx = defender.getPosition().getX()-this.getPosition().getX();
		this.setTheta(Math.atan2(dy, dx));
		defender.setTheta(this.getTheta() + Math.PI);
		this.setAttackCooldown(1);
		this.setState(State.COMBAT);
		defender.setState(State.COMBAT);
		if(!this.isAttackInitiated()){ 
			this.toggleAttackInitiated();
		}
	}
	/**
	 * Return a boolean stating whether or not the attack is initiated.
	 */
	@Basic
	private boolean isAttackInitiated(){
		return this.attackInitiated;
	}
	/**
	 * Terminate the attack of this unit.
	 * @post 	If no attack is initiated, nothing happens.
	 * 			| if(!isAttackInitiated)
	 * 			|	return
	 * @post	The attack is not initiated anymore.
	 * 			| new.isAttackInitiated == false
	 * @effect	This unit is removed from its opponent's
	 * 			set of attackers.
	 * 			| getOpponent().removeAttacker(this)
	 * @post	This unit has no opponent.
	 * 			| new.getOpponent() == null
	 * @post	This unit's state is set to IDLE is this unit
	 * 			isn't currently falling.
	 * 			|if(getState() != State.FALLING)
	 * 			| new.getState() == State.IDLE
	 */
	private void terminateAttack(){
		if(!this.isAttackInitiated())
			return;
		this.toggleAttackInitiated();
		this.getOpponent().removeAttacker(this);
		this.opponent = null;
		if(this.getState() != State.FALLING)
			this.setState(State.IDLE);
	}
	/**
	 * Toggles whether or not the attack is initiated.
	 * @post	The value of isAttackInitiated() is set to its opposite.
	 * 			| new.isAttackInititiated() == !this.isAttackInitiated()
	 */
	private void toggleAttackInitiated(){
		this.attackInitiated = !this.isAttackInitiated();
	}
	/**
	 * Perform an attack from this unit on the defender.
	 * @param defender
	 * 			The defender that gets attacked by this unit.
	 * @post	This unit is set to IDLE if no defender is given.
	 * 			|if(defender == null)
	 * 			|	new.getState() == State.IDLE
	 * @effect	If this unit or the defender is falling, the attack gets terminated.
	 * 			|if(getState() == FALLING || defender.getState() == FALLING)
	 * 			|	then terminateAttack() && return
	 * @effect 	The attack gets terminated if both units belong to the same
	 * 			faction.
	 * 			| if (getFaction() == defender.getFaction() && getFaction() != null)
	 * 			|	then terminateAttack() && return
	 * @effect	The attack gets terminated if the defender is out of range.
	 * 			| if(!inRange(defender))
	 * 			|	then terminateAttack() && return
	 * @effect	The attack gets initiated if it hasn't been initiated yet.
	 * 			The method then ends here.
	 * 			| if(!isAttackInitiated())
	 *			|	then initiateAttack(defender) && return	
	 * @post	Nothing happens if the unit hasn't finished its attack yet.
	 * 			| if(getAttackCooldown() > 0)
	 * 			|	then return
	 * @post	The attack is initiated.
	 * 			| new.isAttackInitiated() == true
	 * @effect	This unit's opponent is removed and this unit is removed from
	 * 			the defender's set of attackers.
	 * 			| setOpponent(null) && removeCombatant(defender)
	 * @effect	If the unit performs an attack, the defender will attempt to defend the attack.	
	 * 			| defender.defend(this)
	 */
	public void attack(Unit defender){
		if (defender == null){
			this.setState(State.IDLE);
			return;
		}
		if(this.getState() == State.FALLING || defender.getState() == State.FALLING){
			this.terminateAttack();
			return;
		}
		if(this.getFaction() == defender.getFaction() && this.getFaction() != null){
			this.terminateAttack();
			return;
		}
		if (!this.inRange(defender)){
			this.terminateAttack();
			return;
		}
		if(!this.isAttackInitiated()){
			this.initiateAttack(defender);
			return;
		}
		if (this.getAttackCooldown() > 0){
			return;
		}
		this.toggleAttackInitiated();
		this.opponent = null;
		defender.removeAttacker(this);
		defender.defend(this);
	}
	/**
	 * Return a boolean stating whether or not a unit is terminated.
	 */
	public boolean isTerminated(){
		return this.terminated;
	}
	/**
	 * Return the probability of dodging an attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @return The probability equals 0.2 times the ratio of the units agility to the attacker's agility.
	 * 			|result == 0.2*this.getPrimStats().get("agl")/attacker.getPrimStats().get("agl")
	 */
	private double getDodgeProb(Unit attacker){
		return ((double)(0.2*this.getPrimStats().get("agl")))/attacker.getPrimStats().get("agl");
	}
	/**
	 * Return a boolean stating whether or not then unit successfully dodged the attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @return True if the randomly generated number between 0 and 1 is less than
	 *			the probability to dodge.
	 *			|result == (Math.random() <= this.getDodgeProb(attacker))
	 */
	private boolean hasDodged(Unit attacker){
		return (Math.random() <= this.getDodgeProb(attacker));
	}
	/**
	 * Dodge an incoming attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @post	This unit will jump to a walkable block that is adjacent
	 * 			in the x- and y-direction if there exists one.
	 * 			| if(for some block in getWorld.getAdjacent(block) : (
	 * 			|	(block.getLocation().getZ() == getBlock().getLocation.getZ()) &&
	 * 			|	getWorld().isWalkable(block))
	 * 			|	then new.getPosition() == block.getLocation().add(
	 * 			|		getBlock().getLocation().getOpposite())
	 * @post	The target of the unit is set to its position and the path is removed.
	 * 			| new.getTarget() == getPosition() &&
	 * 			| new.getPath().isEmpty()
	 * @effect 	The unit will move to its final target if the unit hasn't reached it yet.
	 * 			| if (getFinTarget() != null)
	 *			|	then moveTo(getFinTarget())
	 */
	private void dodge(Unit attacker){
		ArrayList<Block> dodgeBlocks = new ArrayList<Block>();
		for(Block block : this.getWorld().getAdjacent(this.getBlock())){
			if(block.getLocation().getZ() == this.getBlock().getLocation().getZ())
				dodgeBlocks.add(block);
		}
		Collections.shuffle(dodgeBlocks);
		boolean moved = false;
		while(!dodgeBlocks.isEmpty() && !moved){
			if(this.getWorld().isWalkable(dodgeBlocks.get(0))){
				Vector subtraction = dodgeBlocks.get(0).getLocation().add(this.getBlock().getLocation().getOpposite());
				Vector nextPos = this.getPosition().add(subtraction);
				this.setPosition(nextPos);
				moved = true;
			}
			dodgeBlocks.remove(0);
		}
		this.setTarget(this.getPosition());
		this.Path.clear();
		if (this.getFinTarget() != null){
			this.move2(this.getFinTarget());
		}
	}
	
	/**
	 * Return the probability of blocking an attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @return  The probability equals 0.25 times the ratio of the sum the units agility and strength
	 * 			to the sum of the attacker's agility and strength.
	 * 			|result == 0.25*(getPrimStats().get("agl")+getPrimStats("str"))
	 * 			|			/ (attacker.getPrimStats().get("agl") + getPrimStats("str"))
	 */
	private double getBlockProb(Unit attacker){
		return ((double)(0.25*(this.getPrimStats().get("agl")+ this.getPrimStats().get("str"))))
				/(attacker.getPrimStats().get("agl") + attacker.getPrimStats().get("str"));
	}
	
	/**
	 * Return a boolean stating whether or not the unit has blocked
	 * 	the attack of the attacker.
	 * @param attacker
	 * 			The attacking unit.
	 * @return True if the randomly generated number between 0 and 1 is less than
	 *			the probability to block.
	 *			|result == (Math.random() <= getBlockProb(attacker))
	 */
	private boolean hasBlocked(Unit attacker){
		return(Math.random() <= this.getBlockProb(attacker));
	}
	

	/**
	 * The unit attempts to defend itself from the incoming attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @effect	The unit attempts to dodge the attack. If it succeeds, it won't take any damage
	 * 			and it will gain 20 experience points. The method then ends here.
	 * 			| if(this.hasDodged(attacker))
	 * 			|	then dodge(attacker) && new.getHp() == getHp() &&
	 * 			|	addExp(20) && return
	 * @post	If the unit failed to dodge the attack, it will attempt to block the attack.
	 * 			If it succeeds, it won't take any damage and will gain 20 experience points.
	 *			| if(this.hasBlocked(attacker))
	 *			| 	then new.getHp() == this.getHp() && 
	 *			|	addExp(20) && return
	 * @effect	If the unit neither dodged nor blocked the attack it will lose an amount of hitpoints
	 * 			equal to the attacker's strength divided by ten. If this amount is bigger than the current
	 * 			amount of hitpoints of the unit, its hitpoints will be set to zero.
	 * 			| damage == attacker.getPrimStats().get("str")/10
	 * 			| if (!isValidHp(getHp() - damage))
	 *			|	then setHp(0)
	 *			| else
	 *			|	setHp(this.getHp() - damage)
	 * @effect	If the unit neither dodged nor blocked the attack, the attacker gains
	 * 			20 experience points.
	 * 			| attacker.addExp(20)
	 */
	private void defend(Unit attacker){
		double damage = ((double)(attacker.getPrimStats().get("str")))/10;
		if(this.hasDodged(attacker)){
			this.dodge(attacker);
			this.addExp(20);
			return;
		}
		if(this.hasBlocked(attacker)){
			this.addExp(20);
			return;
		}
		if (!this.isValidHp(this.getHp() - damage)){
			this.setHp(0);
		}
		else{
			this.setHp(this.getHp() - damage);
		}
		attacker.addExp(20);
	}
	
	/**
	 * Return a boolean stating whether or not the unit is set to the default behaviour.
	 */
	@Basic
	public boolean DefaultOn(){
		return this.Default;
	}
	
	/**
	 * Set the behaviour of the unit to the default behaviour.
	 * @post 	The unit is set to default behaviour.
	 * 			| new.DefaultOn() == true
	 */
	public void startDefault(){
		this.Default = true;
	}
	/**
	 * Set the behaviour of the unit from default behaviour to no behaviour.
	 * @post	The unit is set from default behaviour to no behaviour
	 * 			| new.DefaultOn() == false
	 */
	public void stopDefault(){
		this.Default = false;
	}
	
	/**
	 * Return the current amount of hitpoints of this unit.
	 */
	@Basic
	public double getHp(){
		return this.hp;
	}
	
	/**
	 * Set the amount of hitpoints of the unit to the given amount.
	 * @param hp
	 * 			The given amount of new hitpoints of the unit. 
	 * @pre  The given amount of hitpoints is a valid amount.
	 * 			|isValidHp(hp)
	 * @post The amount of hitpoints of this unit is set to the given hp.
	 * 			|new.getHp() == hp
	 */
	@Basic
	public void setHp(double hp){
		assert isValidHp(hp);
		this.hp = hp;
		if (this.getHp() == 0)
			this.terminate();
	}
	/**
	 * Return the current amount of stamina points of this unit.
	 */
	@Basic
	public double getStam(){
		return this.stam;
	}
	/**
	 * Set the amount of stamina points of the unit to the given amount.
	 * @param stam
	 * 			The given amount of new stamina points of the unit. 
	 * @pre  The given amount of stamina points is a valid amount.
	 * 			|isValidStam(stam)
	 * @post The amount of stamina points of this unit is set to
	 * 		 the given amount.
	 * 			|new.getStam() == stam
	 */
	public void setStam(double stam){
		assert isValidStam(stam);
		this.stam = stam;
	}
	/**
	 * Return a boolean stating whether or not the given amount of hitpoints is a valid amount.
	 * @param hp
	 * 			The amount of hitpoints to be checked.
	 * @return True if the given amount of hitpoints lies between 0 and
	 * 			the maximum amount of hitpoints inclusively.
	 * 			|result == (hp>=0) && (hp<=getMaxHp())
	 */
	@Basic
	private boolean isValidHp(double hp){
		return (hp>=0 && hp<=this.getMaxHp());
	}
	/**
	 * Return a boolean stating whether or not the given amount of stamina points is a valid amount.
	 * @param stam
	 * 			The amount of stamina points to be checked.
	 * @return True if the given amount of stamina points lies between 0 and
	 * 			the maximum amount of stamina points inclusively.
	 * 			|result == (stam>=0) && (stam<=getMaxStam())
	 */
	@Basic
	private boolean isValidStam(double stam){
		return (stam>=0 && stam<=this.getMaxStam());
	}
	/**
	 * Return the maximum allowed amount of hitpoints of this unit.
	 * 		It is always equal to (strength*weight)/50.
	 */
	@Basic
	public int getMaxHp(){
		return (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50));
	}
	/**
	 * Return the maximum allowed amount of stamina points of this unit.
	 * 		It is always equal to (strength*weight)/50.
	 */
	@Basic
	public int getMaxStam(){
		return (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50));
	}
	/**
	 * Return the current target of the unit.
	 * 		The target of a unit is a position at which the unit
	 * 		will directly move towards.
	 */
	@Basic
	private Vector getTarget(){
		return this.target;
	}
	/**
	 * Set the target of the unit to the given target.
	 * @param target
	 * 			The given position of the new target.
	 * @post	The target of the unit is set to the given position.
	 * 			|new.getTarget() == target
	 */
	private void setTarget(Vector target){
		this.target = target;
		if(!target.equals(this.getPosition())){
			this.setV_Vector();
			this.setTheta(Math.atan2(v_vector.getY(),v_vector.getX()));
		}
	}
	/**
	 * Return the final target of this unit.
	 * 		The final target of a unit is a position at which
	 * 		the unit will move towards by setting new targets.
	 */
	@Basic 
	public Vector getFinTarget(){
		if (this.finTarget == null)
				return null;
		return this.finTarget;
	}
	/**
	 * Set the final target of the unit to the given position.
	 * @param target
	 * 			The given position of the new final target.
	 * @post	The final target of the unit is set to the given position if its is valid
	 * 			and if the given position doesn't equal the unit's target.
	 * 				|if (isValidPosition(target) && !target.equals(getTarget()))
	 * 				|	then new.getFinTarget() == target
	 * @throws IllegalArgumentException
	 * 			Throws and exception if the target is an invalid position.
	 * 				|!isValidPosition(target)
	 */
	private void setFinTarget(Vector target) throws IllegalArgumentException{
		if(target.equals(this.getTarget()))
			return;
		if (!this.isValidPosition(target) && !this.getWorld().isWalkable(this.getWorld().getBlockAtPos(target)))
			throw new IllegalArgumentException("Invalid Target!");
		this.finTarget = target;
	}
	
	/**
	 * Return the base speed of this unit. It is always equal to
	 *	1.5*(strength + agility) / (2*totalWeight).
	 */
	@Basic
	public double getBaseSpeed(){
		return 1.5*((double)(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))/(2*this.getTotalWeight());
	}
	
	/**
	 * Return the set containing the units this unit is currently attacked by.
	 */
	@Basic
	private Set<Unit> getAttackers(){
		return this.attackers;
	}
	
	/**
	 * Add the given unit to the set of attackers.
	 *  	A unit will stay in combat if it is being attacked.
	 * @param unit
	 * 			The given unit to be added to the set of attackers.
	 * @post	The given unit is added to the set of attackers.
	 * 			|new.getAttackers().contains(unit)
	 * @throws	IllegalArgumentException
	 * 			An IllegalArgumentException is thrown if the given unit
	 * 			is the unit itself.
	 * 			|unit == this
	 */
	private void addAttacker(Unit unit) throws IllegalArgumentException{
		if (unit == this){
			throw new IllegalArgumentException("You can't get attacked by yourself!");
		}
		this.attackers.add(unit);
	}
	
	/**
	 * Remove the given unit from the set of attackers of this unit.
	 * @param unit
	 * 			The given unit to be removed from the set of attackers of this unit.
	 * @post	The given unit is removed from the set of attackers of this unit.
	 * 			|!new.getAttackers().contains(unit)
	 */
	private void removeAttacker(Unit unit){
		this.attackers.remove(unit);
	}
	
	/**
	 * Return the opponent of this unit. The opponent is
	 * 	the unit that this unit is attacking. 
	 */
	private Unit getOpponent(){
		return this.opponent;
	}
	/**
	 * Set this unit's opponent to the given opponent.
	 * @param 	opponent
	 * 			The given opponent.
	 * @post	This unit's opponent is set to the given opponent.
	 * @throws	IllegalArgumentException
	 * 			An exception is thrown if the given opponent is the unit
	 * 			itself and if the this unit's opponent and the given opponent
	 * 			are different from null.
	 * 			| (opponent == this) && (getOpponent() != null) &&
	 * 			|	(opponent != null)
	 * 			
	 */
	private void setOpponent(Unit opponent){
		if(opponent == this)
			throw new IllegalArgumentException("You can't attack yourself!");
		if(this.opponent != null && opponent != null)
			throw new IllegalArgumentException("You're already attacking someone!");
		this.opponent = opponent;
	}
	/**
	 * Return the current speed of the unit
	 * @return	Returns the walking speed of this unit if its state is walking.
	 * 			|if (this.getState() == State.WALKING)
	 *			|	result == getWalkSpeed();
	 * 			Returns the walking speed of this unit times two if its state is sprinting.
	 *			|if (this.getState() == State.SPRINTING)
	 *			|	result == getWalkingSpeed() * 2
	 * 			Returns 0 otherwise.
	 *			|else
	 *			|	result == 0
	 */
	public double getSpeed(){
		if (this.getState() == State.FALLING)
			return 3;
		if (this.getState() == State.WALKING)
			return this.getWalkSpeed();
		if (this.getState() == State.SPRINTING)
			return this.getWalkSpeed() * 2;
		return 0;
	}
	/**
	 *  Return the walking speed of this unit.
	 */
	private double getWalkSpeed(){
		return this.v;
	}
	/**
	 * Set the walking speed of this unit to the given speed.
	 * @param v
	 * 			The given speed to be set to the new walking speed.
	 * @post 	The walking speed of this unit is set to the given speed.
	 * 			|new.getWalkingSpeed() == v
	 * 
	 * @throws IllegalArgumentException
	 * 			An exception is thrown if the given speed is negative.
	 * 			|(v < 0)
	 */
	private void setWalkSpeed(double v) throws IllegalArgumentException{
		if (v < 0){
			throw new IllegalArgumentException();
		}
		this.v = v;
	}
		
	/**
	 * An enumeration of possible states
	 * 	The possible states of the unit are:
	 * 	- IDLE
	 * 	- COMBAT
	 * 	- WALKING
	 * 	- WORKING
	 * 	- RESTING
	 * 	- SPRINTING
	 *  - FALLING
	 * 
	 * @author Joost Croonen & Ruben Dedoncker
	 *
	 */
	public enum State{
		IDLE, COMBAT, WALKING, WORKING, RESTING, SPRINTING, FALLING		
	}
	/**
	 * Set the state of the unit to the given state.
	 * @param state
	 * 			The given state to be set to the new state.
	 * @post	If the given state is COMBAT or FALLING, the minimum rest time and
	 * 			the work time will be reset. Otherwise, if the minimum rest time hasn't
	 * 			reached zero yet, nothing happens.
	 * 			| if(state == State.COMBAT || state == State.FALLING)
	 * 			|	then new.getMinRestTime() == 0 &&
	 * 			|	new.getWorkTime() == 0
	 * 			| else if(getMinRestTime() > 0)
	 * 			|	then return
	 * @post 	The state of the unit is set to the given state.
	 * 			|new.getState() == state
	 *@effect	The velocity vector is updated
	 *			|this.setV_Vector()
	 */
	private void setState(State state){
		if (state == State.COMBAT || state == State.FALLING){
			this.setMinRestTime(0);
			this.setWorkTime(0);
		}
		else{
			if(this.getMinRestTime() >0)
				return;
		}
		this.state = state;
		this.setV_Vector();
	}
	/**
	 *  Return the current state of the unit.
	 */
	@Basic
	public State getState(){
		return this.state;
	}
	/**
	 * Return a boolean stating whether or not the unit is moving.
	 * @return True if the unit is either walking or sprinting
	 * 			| result == (getState() == State.WALKING) || (getState() == State.SPRINTING)
	 */
	public boolean isMoving(){
		return (this.getState() == State.WALKING || this.getState() == State.SPRINTING);
	}
	/**
	 *  Return the remaining amount of work time needed.
	 */
	@Basic
	public double getWorkTime() {
		return this.workTime;
	}
	/**
	 * Set the remaining amount of work time needed to the given time.
	 * @param newTime
	 * 			The given time to be set as the new amount of work time needed.
	 * @post  	The remaining amount of work time needed is set to the given time.
	 * 			|new.getWorkTime() == newTime
	 */
	private void setWorkTime(double newTime){
		this.workTime = newTime;
	}
	
	/**
	 * Start to work at the given work block.
	 * @param 	target
	 * 			The position of block in which the unit has to work.
	 * @post	Nothing happens if the unit is moving, in combat or falling.
	 * 			| if(isMoving() || getState() == State.COMBAT || getState() == State.FALLING)
	 * 			|	then return
	 * @post	Nothing happens is the given position of the work block
	 * 			is not adjacent and not equal to the block position of this unit.
	 * 			| targetBlock == getWorld().getBlockAtPos(target)
	 * 			| if(!getWorld().getAdjacent(getBlock()).contains(targetBlock) &&
	 * 			|	targetBlock != getBlock())
	 * 			|	then return
	 * @effect	The given work block is set as the new work block.
	 * 			| setWorkBlock(getWorld().getBlockAtPos(target))
	 * @effect	if the unit isn't currently working, its state will be set to WORKING,
	 * 			its orientation will be set towards the given work block
	 * 			and the work time is set to 500/strength.
	 * 			| targetBlock == getWorld().getBlockAtPos(target)
	 * 			| direction == targetBlock.getLocation().add(getBlock().getLocation().getOpposite())
	 * 			| if(getState() != State.WORKING)
	 * 			|	then setState(State.WORKING) &&
	 * 			|	setWorkTime(500/getPrimStats.get("str")) &&
	 * 			|	setTheta(arctan(direction.getY()/direction.getX()))
	 */
	public void workAt(Vector target){
		if (this.isMoving() || this.getState() == State.COMBAT || this.getState() == State.FALLING)
			return;

		Block targetBlock = this.getWorld().getBlockAtPos(target);
		if(!this.getWorld().getAdjacent(this.getBlock()).contains(targetBlock) && targetBlock != this.getBlock())
			return;
		this.setWorkBlock(targetBlock);
		Vector direction = targetBlock.getLocation().add(this.getBlock().getLocation().getOpposite());
		if (this.getState() != State.WORKING){
			this.setState(State.WORKING);
			this.setWorkTime(1/this.getPrimStats().get("str"));
			this.setTheta(Math.atan2(direction.getY(), direction.getX()));
		}
		
	}
	
	/**
	 * Perform the action of completing a work task at the work block. 
	 * @effect	If the work block is a workshop, and a log and boulder are present, the workshop will be operated
	 * 			and the unit will have completed its work task.
	 * 			| worked == false
	 * 			| if(getWorkBlock().getBlockType()==BlockType.WORKSHOP &&
	 * 			|	(!getWorkBlock.getBouldersInBlock().isEmpty && !getWorkBlock().getLogsInBlock().isEmpty()))
	 * 			|	then operateWorkshop(getWorkBlock()) && 
	 * 			|	worked == true
	 * @effect	Otherwise, If the work block is a solid block, it will be set to passable, an object will be spawned
	 * 			and the unit will have completed its work task.
	 * 			| if ((getWorkBlock().getBlockType() == BlockType.ROCK ||
	 * 			|		getWorkBlock().getBlockType() == BlockType.WOOD) && worked == false)
	 *			|	then getWorld().spawnObject(getWorkBlock()) &&
	 *			|	getWorld().setToPassable(getWorkBlock()) &&
	 *			|	worked == true
	 * @effect	If this unit is carrying an object, it will drop the object in the work block
	 * 			and the unit will have completed its work task.
	 * 			| if(isCarrying() && worked==false)
	 * 			|	then dropAt(getWorkBlock().getLoaction()) &&
	 * 			|	worked == true
	 * @effect	If this unit is not carrying an object and an object is available in the work block,
	 * 			the unit will pick up that object and it will have completed its work task.
	 * 			| if ((!getWorkBlock().getBouldersInBlock().isEmpty() ||
	 * 			|		!getWorkBlock().getLogsInBlock().isEmpty())&& worked==false)
	 *			|	then pickup(this.getWorkBlock()) &&
	 *			|		worked == true
	 * @effect	If the unit has completed any of the work tasks listed above, then
	 * 			10 experience points will be added to this Unit, the work block is set to
	 * 			null and its state is set to IDLE.
	 * 			| if(worked == true)
	 * 			|	then addExp(10) &&
	 * 			|	setWorkBlock(null) &&
	 * 			|	setState(State.IDLE) 
	 */
	private void workCompleted(){
		boolean worked = false;
		if (this.getWorkBlock().getBlockType()==BlockType.WORKSHOP){
			if (!this.getWorkBlock().getBouldersInBlock().isEmpty() && !this.getWorkBlock().getLogsInBlock().isEmpty()){
				this.operateWorkshop(this.getWorkBlock());
				worked = true;
			}
		}
		if ((this.getWorkBlock().getBlockType()==BlockType.ROCK || this.getWorkBlock().getBlockType()==BlockType.WOOD) && worked==false){
			this.getWorld().spawnObject(this.getWorkBlock());
			this.getWorld().setToPassable(this.getWorkBlock());
			worked = true;
		}
		if (this.isCarrying() && worked==false){
			this.dropAt(this.getWorkBlock().getLocation());
			worked = true;
		}
		if ((!this.getWorkBlock().getBouldersInBlock().isEmpty() || !this.getWorkBlock().getLogsInBlock().isEmpty())&& worked==false){
			this.pickup(this.getWorkBlock());
			worked = true;
		}
		this.setWorkBlock(null);
		if (worked == true)
			this.addExp(10);
		this.setState(State.IDLE);
	}
	
	/**
	 * Pickup a boulder or log, present in the targetBlock.
	 * @param 	targetBlock
	 * 			Block from which a boulder or log is picked up.
	 * @effect	A boulder is lifted if present, otherwise a log is lifted
	 * 			if present.
	 * 			| if(!targetBlock.getBouldersInBlock().isEmpty())
	 * 			|	then for one boulder in targetBlock.getBouldersInBlock : (
	 * 			|		lift(boulder)
	 * 			| else if(!targetBlock.getLogsInBlock().isEmpty())
	 * 			|	then for one log in targetBlock.getLogInBlock : (
	 * 			|		lift(log)
	 */

	private void pickup(Block targetBlock){
		int random = (int) (Math.random()*targetBlock.getBouldersInBlock().size());
		int counter = 0;
		for (Boulder boulder : targetBlock.getBouldersInBlock()){
			if (counter == random){
				this.lift(boulder);
				return;
			}
			counter++;
		}
		
		int random2 = (int) (Math.random()*targetBlock.getBouldersInBlock().size());
		int counter2 = 0;
		for (Log log : targetBlock.getLogsInBlock()){
			if (counter2 == random2){
				this.lift(log);

			}
			counter2++;
		}
	}
	
	/**
	 * Improve weight and toughness by consuming one log and one boulder on the workshop block.
	 * @param 	targetBlock
	 * 			Block containing the workshop
	 * @effect	A boulder is removed from the workshop block.
	 * 			| for one boulder in old.targetBlock.getBouldersInBlock() : (
	 * 			|	!new.targetBlock.getLogsInBlock().contains(boulder))
	 * @effect	A log is removed from the workshop block.
	 * 			| for one log in old.targetBlock.getLogsInBlock() : (
	 * 			|	!new.targetBlock.getLogsInBlock().contains(log))
	 * @effect	The primary stats of this unit are set to a new map of primary stats
	 * 			in which the toughness and weight are raised with 5 points.
	 * 			| newPrimStats == getPrimStats()
	 * 			| newPrimStats.put("tgh", newPrimStats.get("tgh") + 5) && 
	 * 			| newPrimStats.put("str", newPrimStats.get("wgt") + 5)
	 * 			| setPrimStats(newPrimStats)
	 * @post	The ratio of the Hp to the MaxHp is maintained.
	 * 			| new.getHp()/new.getMaxHp() == this.getHp()/this.getMaxHp()
	 * @post	The ratio of the Stam to the MaxStam is maintained.
	 * 			| new.getStam()/new.getMaxStam() == this.getStam()/this.getMaxStam() 
	 */

	private void operateWorkshop(Block targetBlock){
		int random = (int) (Math.random()*targetBlock.getBouldersInBlock().size());
		int counter = 0;
		for (Boulder boulder : targetBlock.getBouldersInBlock()){
			if (counter == random){
				this.getWorld().removeBoulder(boulder);
			}
			counter++;
		}
		int random2 = (int) (Math.random()*targetBlock.getBouldersInBlock().size());
		int counter2 = 0;
		for (Log log : targetBlock.getLogsInBlock()){
			if (counter2 == random2){
				this.getWorld().removeLog(log);
			}
			counter2++;
		}
		ArrayList<String> statList = new ArrayList<String>(Arrays.asList("wgt", "tgh"));
		Collections.shuffle(statList);
		Map<String, Integer> newStats = this.getPrimStats();
		double hpRatio = this.getHp()/this.getMaxHp();
		double stamRatio = this.getStam()/this.getMaxStam();
		for(String stat : statList){
			if(this.getPrimStats().get(stat) != 200 ){
				newStats.put(stat, newStats.get(stat) + 5);
			}
		}
		this.setPrimStats(newStats);
		this.setHp(hpRatio*this.getMaxHp());
		this.setStam(stamRatio*this.getMaxStam());
	}
	
	/**
	 * Drop a log or boulder in the targeted block.
	 * @param 	blockTarget
				Block in which the log or boulder must be dropped.
	 * @effect	If this unit is carrying a boulder, set the position of the boulder in the targeted block, 
	 * 			remove the carrier of the boulder and set the boulder of this unit to null.
	 * 			| if(getBoulder() != null)
	 * 			|	then getBoulder().setPosition(blockTarget.add(0.5, 0.5, 0.5)) &&
	 * 			|	getBoulder().removeCarrier() &&
	 * 			|	getWorld().addBoulder(getBoulder()) &&
	 * 			|	new.getBoulder == null
	 * 			
	 * @effect	If this unit is carrying a log, set the position of the log in the targeted block, 
	 * 			remove the carrier of the log, and set the log of this unit to null.| if(getBoulder() != null)
	 * 			|	then getLog().setPosition(blockTarget.add(0.5, 0.5, 0.5)) &&
	 * 			|	getLog().removeCarrier() &&
	 * 			|	getWorld().addBoulder(getLog()) &&
	 * 			|	new.getLog == null
	 * @effect	Set CarryWeight to 0
	 */

	private void dropAt(Vector blockTarget){
		if(this.getBoulder() != null){
			this.getBoulder().setPosition(blockTarget.add(0.5, 0.5, 0.5));
			this.getBoulder().removeCarrier();
			this.getWorld().addBoulder(this.getBoulder());
			this.boulder = null;			
		}
		if(this.getLog() != null){
			this.getLog().setPosition(blockTarget.add(0.5, 0.5, 0.5));
			this.getLog().removeCarrier();
			this.getWorld().addLog(this.getLog());
			this.log = null;
		}
		this.setCarryWeight(0);
	}
	
	/**
	 * Return a boolean stating whether or not the given unit is in range.
	 * 	Two units are in range if they occupy the same block or a block
	 * 	adjacent to the other block.
	 * @param unit
	 * 			The unit to be checked.
	 * @return True if and only if the distance between the units is less than 2 meters.
	 * 			| distance == getBlockPosition.add(unit.getBlockPosition.getOpposite())
	 * 			| result == distance.getLength() < 1.8
	 */
	private boolean inRange(Unit unit){
		Vector distance = this.getBlockCentre().add(unit.getBlockCentre().getOpposite());
		if (distance.getLength() < 1.8)
			return true;
		return false;
	}
	/**
	 * Return the time that has passed since this unit last rested.
	 */
	@Basic
	public double getRestTime(){
		return this.restTime;
	}
	/**
	 * Set the time that has passed since the unit last rested to the given time.
	 * @param newTime
	 * 			The given time to be set to the time that has passed since the unit last rested.
	 * @post 	The time that has passed since the unit last rested is set to the given time.
	 * 			|new.getRestTime() == newTime
	 */
	private void setRestTime(double newTime){
		this.restTime = newTime;
	}
	/**
	 * Return the time until this unit can perform an attack.
	 */
	@Basic
	public double getAttackCooldown(){
		return this.attcooldown;
	}
	/**
	 * Set the time until this unit can perform an attack to the given time.
	 * @param newTime
	 * 			The given time to be set to the time until this unit can perform an attack.
	 * @post	The cooldown is set to the given time.
	 * 			|new.getAttackCooldown() == newTime
	 */
	private void setAttackCooldown(double newTime){
		this.attcooldown = newTime;
	}
	/**
	 * Return the time the unit needs to rest until it can perform any other actions.
	 */
	@Basic
	public double getMinRestTime(){
		return this.minRestTime;
	}
	/**
	 * Set the time the unit needs to rest until it can perform any other actions to the given time.
	 * @param newtime
	 * 			The given time to be set to the minimum resting time.
	 * @post	The minimum resting time is set to the given time.
	 * 			|new.getMinRestTime() == newTime
	 */
	private void setMinRestTime(double newtime){
		this.minRestTime = newtime;
	}
	/**
	 * The unit starts to rest.
	 * @post	Nothing happens if the unit is moving, in combat or falling
	 * 			and if the rest time is bigger than 180 seconds.
	 * 			| if ((isMoving() || getState() == State.COMBAT
	 * 			|		|| getState() == State.FALLING) && (restTime >= 180) )
	 * 			| 	return
	 * @effect If the unit isn't resting and either its hp or stamina hasn't fully
	 * 			replenished yet, its state is set to resting. and its minimum resting
	 * 			time is set to 40/toughness.
	 * 			| if (getState() != State.RESTING)
	 * 			|	setState(State.RESTING)
	 * 			|	setMinRestTime(40.0/getPrimStats().get("tgh"))
	 */
	public void rest(){
		if ((this.isMoving() || this.getState() == State.COMBAT || this.getState() == State.FALLING) && (this.restTime < 180) )
			return;
		if(this.getState() != State.RESTING && (this.getHp() != this.getMaxHp() || this.getStam() != this.getMaxStam())){
			this.setState(State.RESTING);
			this.setMinRestTime(40.0/this.getPrimStats().get("tgh"));
		return;
		}
	}
	/**
	 * The unit starts sprinting.
	 */
	public void toggleSprint(){
		if(this.getState() == State.WALKING){
			this.setState(State.SPRINTING);
			return;
		}
		if(this.getState() == State.SPRINTING)
			this.setState(State.WALKING);
	}
	
	/**
	 * Return the faction to which this unit belongs.
	 */
	@Basic
	public Faction getFaction() {
		return this.faction;
	}
	/**
	 * Set the faction of this unit a given faction.
	 * @param 	faction
	 * 			The given faction to be set to this unit's
	 * 			faction.
	 * @effect	If this unit can belong to the given faction,
	 * 			this unit's faction is set to the given faction
	 * 			and this unit is added as a member of the given faction.
	 * 			| if (canHaveAsFaction(faction))
	 * 			| 	then new.getFaction() == faction &&
	 * 			|	faction.getUnits.contains(this)
	 * 			| else newgetFaction() == getFaction() &&
	 * 			|	!faction.getUnits.contains(this)
	 */
	public void setFaction(Faction faction){
		if (!canHaveAsFaction(faction))
			return;
		faction.addUnit(this);
		this.faction = faction;
	}
	
	/**
	 * Return a boolean stating whether or not this unit can belong
	 * 	to the given faction.
	 * @param 	faction
	 * 			The given faction to be checked.
	 * @return	True if and only if this unit is currently not
	 * 			belonging to any faction, the given faction is not null,
	 * 			and the unit belongs to the same world as the faction's
	 * 			world or no world at all.
	 * 			| return (getFaction() == null && faction != null &&
	 * 			|	(getWorld() == null || getWorld() == faction.getWorld()))
	 */
	@Basic @Raw 
	protected boolean canHaveAsFaction(Faction faction) {
		return (this.faction == null && faction != null &&
				(this.world == null || this.world == faction.getWorld()));
	}
	/**
	 * Remove the unit from it's current faction.
	 * @effect	The unit is removed from the set of units
	 * 			belonging to its faction and the unit's
	 * 			faction is set to null.
	 * 			| faction.removeUnit(this) && new.getFaction == null
	 */
	public void removeFaction(){
		this.faction.removeUnit(this);
		this.faction = null;
	}
	
	/**
	 * Return a boolean stating whether or not the unit is carrying
	 * 	something.
	 * @return 	True if the unit is carrying a boulder or a log.
	 * 			| result == (getBoulder() != null || getLog() != null)
	 */
	public boolean isCarrying() {
		return (this.boulder != null || this.log != null);		
	}
	 /**
	  * Return the boulder this unit is currently carrying.
	  */
	@Basic
	public Boulder getBoulder(){
		return this.boulder;
	}
	
	/**
	  * Set the boulder the unit is carrying to a given boulder.
	  * @param 	boulder
	  * 		The given boulder to be set as the boulder this unit is carrying.
	  * @effect	Tries to set this unit as the carrier of the given boulder,
	  * 		set the given boulder as the boulder this unit is currently carrying
	  * 		and set the weight of the boulder as the carry weight. If an
	  * 		IllegalArgumentException is caught, nothing happens.
	  * 		| if(boulder.setCarrier(this) throws IllegalArgumentException)
	  * 		| 	then return
	  * 		| else new.getBoulder() == boulder &&
	  * 		|	boulder.getCarrier() == this &&
	  * 		|	new.getCarryWeight() == boulder.getWeight()
	  * @throws IllegalArgumentException
	  * 		An exception is thrown this unit is an invalid carrier for
	  * 		the boulder.
	  * 		|!boulder.canHaveAsCarrier(this)
	  */
	protected void setBoulder(Boulder boulder) throws IllegalArgumentException{
				boulder.setCarrier(this);
				this.boulder = boulder;
				this.setCarryWeight(boulder.getWeight());
	}
	
	/**
	  * Return the log this unit is currently carrying.
	  */
	@Basic
	public Log getLog(){
		return this.log;
	}
	
	 /**
	  * Set the log the unit is carrying to a given log.
	  * @param 	log
	  * 		The given log to be set as the log this unit is carrying.
	  * @effect	Tries to set this unit as the carrier of the given log,
	  * 		set the given log as the log this unit is currently carrying
	  * 		and set the weight of the log as the carry weight. If an
	  * 		IllegalArgumentException is caught, nothing happens.
	  * 		| if(log.setCarrier(this) throws IllegalArgumentException)
	  * 		| 	then return
	  * 		| else new.getLog() == log &&
	  * 		|	log.getCarrier() == this &&
	  * 		|	new.getCarryWeight() == log.getWeight()
	  * @throws IllegalArgumentException
	  * 		An exception is thrown this unit is an invalid carrier for
	  * 		the log.
	  * 		|!log.canHaveAsCarrier(this)
	  */
	protected void setLog(Log log) throws IllegalArgumentException{
				log.setCarrier(this);
				this.log = log;
				this.setCarryWeight(log.getWeight());
	}
	
	/**
	 * Lift a boulder lying in the same cube as this Unit.
	 * @param	boulder
	 * 			The given boulder to be lifted.
	 * @effect	If the given boulder and this unit occupy the
	 * 			same cube, it will try to set the given boulder
	 * 			as the boulder this unit is currently carrying. If no
	 * 			IllegalArgumentException is caught the boulder will be
	 * 			set as the boulder that this unit is carrying and the
	 * 			boulder will be removed from the world. Otherwise nothing
	 * 			happens
	 * 			| if(getBlockCentre() == boulder.getBlockCentre() &&
	 * 			|		!(setBoulder(boulder) throws IllegalArgumentException))
	 * 			|	then setBoulder(boulder) &&
	 * 			|	getWorld().removeBoulder(boulder)
	 * 			| else return
	 */
	public void lift(Boulder boulder){
		try {
			this.setBoulder(boulder);
			this.getWorld().removeBoulder(boulder);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	/**
	 * Lift a log lying in the same cube as this Unit.
	 * @param	log
	 * 			The given log to be lifted.
	 * @effect	If the given log and this unit occupy the
	 * 			same cube, it will try to set the given log
	 * 			as the log this unit is currently carrying. If no
	 * 			IllegalArgumentException is caught the log will be
	 * 			set as the log that this unit is carrying and the
	 * 			log will be removed from the world. Otherwise nothing
	 * 			happens
	 * 			| if(getBlockCentre() == log.getBlockCentre() &&
	 * 			|		!(setLog(log) throws IllegalArgumentException))
	 * 			|	then setLog(log) &&
	 * 			|	getWorld().removeLog(log)
	 * 			| else return
	 */
	public void lift(Log log){
		try {
			this.setLog(log);
			this.getWorld().removeLog(log);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	/**
	 * Return the carry weight of this unit.
	 * 	This is the weight of the prop that this
	 * 	unit is currently carrying.
	 */
	@Basic
	public int getCarryWeight(){
		return this.carryWeight;
	}
	 /**
	  * Set the carry weight of this unit to a given weight.
	  * 
	  * @param	weight
	  * 		The given weight to be set as carry weight.
	  * @post	The new carry weight of this unit is set
	  * 		to the given weight.
	  * 		| new.getCarryWeight() == weight.
	  */
	@Basic
	public void setCarryWeight(int weight){
		this.carryWeight = weight;
	}
	
	/**
	 * Return the total weight of this unit.
	 * 	This is the sum of the unit's weight
	 * 	with the carry weight.
	 */
	private int getTotalWeight(){
		return this.getPrimStats().get("wgt") + this.getCarryWeight();
	}
	
	 /**
	  * Return the world of this unit.
	  */
	@Basic
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Set the world of this unit to the given world.
	 * @param 	world
	 * 			The given world.
	 * @post	The unit's world is set to the given world if
	 * 			this unit can have the given world as its world.
	 * 			| if(canHaveAsWorld(world))
	 * 			| 	new.getWorld() == world 
	 */
	protected void setWorld(World world){
		if(!this.canHaveAsWorld(world))
			return;
		this.world = world;
	}
	
	/**
	 * Remove the world of this unit.
	 * @post	The world of this unit is set to null.
	 * 			|new.getWorld() == null
	 */
	protected void removeWorld(){
		this.world = null;
	}
	
	/**
	 * Return a boolean stating whether or not this unit can
	 * 	have the given world as its world.
	 * @param 	world
	 * 			The given world to be checked.
	 * @return	True if the unit isn't terminated and this unit
	 * 			doesn't belong to a world or the given world is
	 * 			the same as its faction's world.
	 * 			| result == (getWorld() == null || world == getFaction().getWorld()) &&
	 *			|			!this.isTerminated())
	 */
	protected boolean canHaveAsWorld(World world){
		return((this.getWorld() == null || world == this.getFaction().getWorld())
				&& !this.isTerminated());
	}
	
	/**
	 * Return the fall height of this unit.
	 */
	private int getFallHeight(){
		return this.fallHeight;
	}
	
	/**
	 * Set the fall height of this unit to the given height.
	 * @param 	newHeight
	 * 			The given height to be set as the new height.
	 * @post	| new.getFallHeight() == newHeight
	 */
	private void setFallHeight(int newHeight){
		this.fallHeight = newHeight;
	}
	 /**
	  * The unit starts to fall.
	  * @effect	If this unit is in combat, the opponents of its
	  * 		attackers are set to null.
	  * 		| if(getState() == State.COMBAT)
	  * 		|	then for each attacker in getAttackers(): (
	  * 		|	attacker.setOpponent(null)
	  * @effect	if this unit is in combat and it has an opponent, 
	  * 		then this unit is removed from its opponent's set
	  * 		of attackers and this unit's opponent is set
	  * 		to null.
	  * 		| if(getState() == State.COMBAT &&
	  * 		|		getOpponent() != null)
	  * 		|	then opponent.removeAttacker(this) &&
	  * 		|	new.getOpponent() == null
	  * @post	This unit's state is set to FALLING and its fall height is set
	  * 		to the biggest integer smaller than its z-coordinate.
	  * 		| new.getState() == State.FALLING &&
	  * 		| new.getFallHeight == floor(getPosition().getZ())
	  */
	private void fall(){
		if(this.getState() == State.COMBAT){
			if(this.opponent != null){
				opponent.removeAttacker(this);
				this.opponent = null;
			}
		}
			for(Unit attacker : new ArrayList<Unit>(this.getAttackers()))
				attacker.terminateAttack();
		this.setState(State.FALLING);
		this.setFallHeight((int)(this.getPosition().getZ()));
	}
	
	/**
	 * The unit lands on its position.
	 * @effect 	This unit's state is set to IDLE  and its
	 * 			target is set to its position.
	 * 			| setState(State.IDLE) && 
	 * 			| setTarget(getPosition())
	 * @post	This unit loses an amount of hitpoints equal to
	 * 			the fall height subtracted by its current z-level,
	 * 			multiplied by 10. If the new amount of hitpoints is
	 * 			invalid, it will be set to 0.
	 * 			| if(isValidHp(getHp() - 
	 * 			|		(getFallHeight - floor(getPosition().getZ())*10)
	 * 			|	then new.getHp() == this.getHp() - 
	 * 			|		(getFallHeight - floor(getPosition().getZ())*10)
	 * 			| else new.getHp() == null
	 * @effect	If this unit has a final target, its path gets cleared,
	 * 			its state is set to WALKING and it will move to its target.
	 * 			| if(getFinTarget() != null)
	 * 			|	then new.getPath().isEmpty()
	 * 			|	setState(State.WALKING)
	 * 			|	move2(getFinTarget())
	 */
	private void land(){
		this.setState(State.IDLE);
		this.setTarget(this.getPosition());
		if(this.getFinTarget() != null){
			this.Path.clear();
			this.setState(State.WALKING);
			this.move2(this.getFinTarget());
		}
		int damage = this.getFallHeight() - (int) (this.getPosition().getZ());
		if(!isValidHp(this.getHp() - damage *10))
			setHp(0);
		else this.setHp(this.getHp() - damage * 10);
	}
	
	/**
	 * Return a boolean stating whether or not this unit
	 * 	should be falling.
	 * @return	True if the unit's current block isn't walkable.
	 * 			| result == !getWorld().isWalkable(getBlock())
	 */
	private boolean shouldFall(){
		if(this.getWorld() == null)
			return false;
		if(!this.getWorld().isWalkable(this.getBlock()))
				return true;
		return false;
	}
	
	/**
	 * Return the block this unit is positioned in.
	 */
	protected Block getBlock(){
		return this.getWorld().getBlockAtPos(this.getBlockCentre());
	}
	/**
	 * Return a list of blocks representing the shortest path
	 *  from this unit's position to its final target.
	 * @post	Nothing happens if this unit has no final target.
	 * 			| if(getFinTarget == null)
	 * 			|	return
	 * @post	If there doesn't exist a path between the unit and
	 * 			its final target , then the current path and the final
	 * 			path are removed. Otherwise a path is created.
	 * 			| flag == true
	 * 			| toBeChecked.contains(getBlock())
	 * 			| startBlock == getBlock()
	 * 			| checked.isEmpty()
	 * 			| while(flag && !toBeChecked.isEmpty()) (
	 * 			|	toBeChecked.remove(startBlock)
	 * 			|	checked.aad(startBlock)
	 * 			|	for each block in getWorld.getAdjacent(startBlock) : (
	 * 			|		if(getWorld().isWalkable(startBlock)
	 * 			|			then toBeChecked.add(startBlock))
	 * 			|	startBlock == toBeChecked.next()
	 * 			| if(for each block in checked : block != getWorld().getBlockAtPos(getTarget()))
	 * 			|	then new.getPath().isEmpty() &&
	 * 			| 	new.getFinTarget() == null
	 * 			| else !new.getPath().isEmpty()
	 * 
	 */
	protected void pathFinding(){
		if (this.getFinTarget()==null)
			return;
		Block current = this.getBlock();
		Block end = getWorld().getBlockAtPos((this.getFinTarget()));
		Map<Block, ArrayList<Block>> shortestPath = new HashMap<Block, ArrayList<Block>>();
		shortestPath.put(current, new ArrayList<Block>(Arrays.asList(current)));
		Map<Block, Double> finalCost = new HashMap<Block, Double>();
		Set<Block> toBeChecked = new HashSet<Block>();
		for (Block block : this.getNext(current, finalCost)){
			toBeChecked.add(block);
			double newcost = current.getLocation().distance(block.getLocation());
			finalCost.put(block, newcost);
			ArrayList<Block> path = new ArrayList<Block>(Arrays.asList(block));
			shortestPath.put(block, path);
		}
		while (!((current==end) || (toBeChecked.isEmpty()))){
			toBeChecked.remove(current);
			double lowestCost = 9999999;
			for (Block block : toBeChecked){
				if (finalCost.get(block)< lowestCost){
					lowestCost = finalCost.get(block);
					current = block;
				}
			}				
			for (Block block : this.getNext(current, finalCost)){
				toBeChecked.add(block);
				double newcost = lowestCost + current.getLocation().distance(block.getLocation());
				finalCost.put(block, newcost);
				ArrayList<Block> path = new ArrayList<Block>(shortestPath.get(current));
				path.add(block);
				shortestPath.put(block, path);
			}
		}
		ArrayList<Block> finalPath = new ArrayList<Block>(shortestPath.get(current));
		if(current != end){
			finalPath.clear();
			this.finTarget = null;
		}
		this.Path= finalPath;
	}
	/**
	 * Return the set of blocks that must be checked surrounding the current block.
	 * @param	current
	 * 			Block for which the next blocks must be found.
	 * @param 	finalCost
	 * 			Map containing the cost of all Blocks that have already been given
	 * 			the cost required to move to them. This Map can therefore also be
	 * 			used to check whether a block has already been checked.
	 * @return 	A set containing all the blocks that are walkable, adjacent to current and not yet in finalCost.
	 * 			| for each block in result : (
	 * 			|	getWorld().isWalkable(block) &&
	 * 			|	getWorld().getAdjacent(current).contains(block) &&
	 * 			|	!finalCost.containsKey(block))
	 */
	private Set<Block> getNext(Block current, Map<Block, Double> finalCost){
		Set<Block> next = new HashSet<Block>();
		for (Block block : this.getWorld().getAdjacent(current)){
			if (this.getWorld().isWalkable(block) && !finalCost.containsKey(block)){
				next.add(block);
			}
		}
		return next;
	}
	
	/**
	 * Return the block this unit is working on.
	 */
	@Basic
	private Block getWorkBlock(){
		return this.workBlock;
	}
	
	/**
	 * Set the work block of the unit to the given block.
	 * @param 	block
	 * 			The given block.
	 * @post	This unit's work block is set to the given
	 * 			block.
	 * 			| new.getWorkBlock() == block
	 */
	@Basic
	private void setWorkBlock(Block block){
		this.workBlock = block;
	}
	
	/**
	 * Return the path from this unit towards its
	 * 	final target.
	 */
	@Basic
	protected ArrayList<Block> getPath(){
		return new ArrayList<Block>(this.Path);
	}
	
	/**
	 * Remove the path from this unit towards its
	 * 	final target.
	 */
	@Basic
	protected void clearPath(){
		this.Path.clear();
	}
	
	/**
	 * Return a set containing the units adjacent
	 * 	to this one.
	 */
	protected Set<Unit> getAdjacentUnits(){
		Set<Unit> adjacentUnits = new HashSet<Unit>();
		for(Block block : this.getWorld().getAdjacent(this.getBlock()))
			adjacentUnits.addAll(block.getUnitsInCube());
		return adjacentUnits;
	}
	
	/**
	 * Let the unit exhibit its default behaviour.
	 * @effect 	If the unit is walking, there is a one in a thousand
	 * 			chance that it starts sprinting. The method returns here
	 * 			| if(getState() == State.WALKING && Math.random() < 0.001)
	 * 			|	then setState(State.Sprinting) &&
	 * 			|	return
	 * @post	If the unit's state isn't IDLE or the unit has a final
	 * 			target, nothing happens.
	 * 			| if(getState() != State.IDLE || getFinTarget() != null)
	 * 			|	then return
	 * @post	The unit will randomly choose to walk to a new final target,
	 * 			work at a block, attack a unit in range or rest until it is
	 * 			fully recovered.
	 * 			| stateList == ArrayList<State>(Arrays.asList(
	 * 			|	State.COMBAT, State.RESTING, State.WALKING, State.WORKING))
	 * 			| Collection.shuffle(stateList)
	 * 			| state == staseList.get(0)
	 * 			| if (state == State.WALKING)
	 * 			|	then for one position in getWorld().keySet() : (
	 * 			|		move2(getWorld().getBlockAtPos(position).getLocation()))
	 * 			| else if(state == State.COMBAT)
	 * 			|	then attack(getEnemyInRange())
	 * 			| else if(state == State.RESTING)
	 * 			|	then rest()
	 * 			| else if (state == State.WORKING)
	 * 			|	then for one block in getWorld().getAdjacent(getBlock()) : (
	 * 			|	workAt(block)) ||
	 * 			|	workAt(getBlock())
	 */
	protected void defaultBehaviour(){
		if(this.getState() == State.WALKING){
			double sprintRoll = Math.random();
			double sprintChance = 0.001;
			if (sprintRoll < sprintChance){
				this.setState(State.SPRINTING);
			}
			return;
		}
		if(this.getFinTarget() != null)
			return;
		if(this.getState() != State.IDLE)
			return;
		ArrayList<State> stateList = new ArrayList<State>(Unit.stateList);
		if(this.getEnemyInRange()== null)
			stateList.remove(stateList.indexOf(State.COMBAT));
		if(this.getHp() == this.getMaxHp() && this.getStam() == this.getMaxStam())
			stateList.remove(stateList.indexOf(State.RESTING));			
		Collections.shuffle(stateList);
		State state = stateList.get(0);
		if (state == State.WALKING){
			this.setState(State.WALKING);
			ArrayList<ArrayList<Integer>> positions = this.getWorld().getPositionList();
			Collections.shuffle(positions);
			int idx = 0;
			Block finTarget = this.getWorld().getBlockAtPos(positions.get(idx));
			while(this.getPath().isEmpty()){
				boolean flag = true;
				while((!this.getWorld().isWalkable(finTarget) || flag) && idx < positions.size() - 1){
					idx += 1;
					finTarget = this.getWorld().getBlockAtPos(positions.get(idx));
					flag = false;
				}
				this.move2(finTarget.getLocation());
			}
		}
		if (state== State.WORKING){
			ArrayList<Block> surrounding = new ArrayList<Block>();
			for (Block block : this.getWorld().getAdjacent(this.getBlock())){
				surrounding.add(block);
			}
			int random = (int) Math.floor(Math.random()*surrounding.size());
			Block selected = surrounding.get(random);
			this.workAt(selected.getLocation());
		}
		if (state == State.RESTING){
			this.rest();
		}
		if(state == State.COMBAT){
			this.attack(this.getEnemyInRange());
			}
	}
	
	/**
	 * Return an enemy in range of this unit.
	 */
	protected Unit getEnemyInRange(){
		Set<Unit> unitsInRange = new HashSet<Unit>(this.getAdjacentUnits());
		unitsInRange.addAll(this.getBlock().getUnitsInCube());
		unitsInRange.remove(this);
		for(Unit enemy : unitsInRange){
			if(enemy.getFaction() != this.getFaction())
				return enemy;
		}
		return null;
	}
	
	/**
	 * Return the current amount of experience points of this unit.
	 */
	@Basic
	public int getExp(){
		return this.experience;
	}
	
	/**
	 * Add a given amount of experience points to this unit.
	 * @param 	exp
	 * 			The given amount of experience points.
	 * @effect	If the given amount of experience is a strictly positive number, 
	 * 			then the amount will be added to the current amount of
	 * 			experience points. The unit then levels every 10 experience
	 * 			points.
	 * 			| if(exp > 0)
	 * 			|	then tempExp == this.getExperience() + 10 &&
	 * 			|	while(tempExp >= 10) (
	 * 			|		new.tempExp == old.tempExp - 10 &&
	 * 			|		levelUp())
	 * 			|	new.getExperience == tempExp
	 */
	public void addExp(int exp){
		if(exp <= 0)
			return;
		this.experience += exp;
		while(this.experience >= 10){
			this.experience -= 10;
			this.levelUp();
		}
	}
	
	/**
	 * Level the unit.
	 * @post	A randomly chosen primary stat with a value smaller than
	 * 			200 is added with one point. If this results in an invalid
	 * 			weight, then the weight gains one point while the previous
	 * 			primary stat is set to its previous value.
	 * 			| for one stat in getPrimStats().keySet() : (
	 * 			|	new.getPrimStats.get(stat) == this.getPrimStats.get(stat) + 1 &&
	 * 			|	if(!hasValidWeight(new.getPrimStats))
	 * 			|		then stat == "wgt")
	 * @post	The ratios of hp to max hp an stam to max stam are maintained
	 * 			| new.getHp()/new.getMaxHp() == this.getHp()/this.getMaxHp() &&
	 * 			| new.getStam()/new.getMaxStam() == this.getStam()/this.getMaxStam()
	 */
	private void levelUp(){
		ArrayList<String> statList = new ArrayList<String>(this.getPrimStats().keySet());
		Collections.shuffle(statList);
		Map<String, Integer> newStats = this.getPrimStats();
		double hpRatio = this.getHp()/this.getMaxHp();
		double stamRatio = this.getStam()/this.getMaxStam();
		for(String stat : statList){
			if(this.getPrimStats().get(stat) != 200 ){
				newStats.put(stat, newStats.get(stat) + 1);
				if(!hasValidWeight(newStats)){
					newStats.put(stat, this.getPrimStats().get(stat));
					newStats.put("wgt", newStats.get("wgt") + 1);
				}
				this.setPrimStats(newStats);
				this.setHp(hpRatio*this.getMaxHp());
				this.setStam(stamRatio*this.getMaxStam());
				return;
			}
		}
	}
	
	/**
	 * Return a boolean stating whether the given set of
	 * 	primary stats contains a valid weight.
	 * @param 	primStats
	 * 			The given set of primary sets to be checked.
	 * @return	False if the format of the given set of primary
	 * 			stats is invalid and its weight is smaller than
	 * 			(strength + agility)/2.
	 * 			| result == !primStats.keySet().equals(getPrimStatsSet()) &&
	 * 			|	primStats.get("wgt") >= (primStats.get("str") + primStats.get"agl")/2
	 */
	public boolean hasValidWeight(Map<String, Integer> primStats){
		if(!primStats.keySet().equals(Unit.getPrimStatSet()))
			return false;
		if (primStats.get("wgt") < Math.ceil(((double)(primStats.get("str"))+(double)(primStats.get("agl"))) / 2))
			return false;
		return true;
	}
	/**
	 * Update the final target of this unit.
	 * @post 	If the block of the final target is not walkable,
	 * 			then the target and the path are removed.
	 * 			| if(!getWorld().isWalkable(getWorld().getBlockAtPos(getFinTarget)))
	 * 			|	then  new.getFinTarget() == null &&
	 * 			|	new.getPath() == null			
	 */
	protected void updateFinTarget(){
		if(!this.getWorld().isWalkable(this.getWorld().getBlockAtPos(this.getFinTarget()))){
			this.finTarget = null;
			this.Path.clear();
		}
	}
	
	/**
	 * Terminate this unit
	 * @effect	If this unit is attacking another unit,
	 * 			this unit is removed from the opponent's
	 * 			set of attackers and this unit's opponent
	 * 			is set to null.
	 * 			| if(getOpponent != null)
	 * 			|	then getOpponent.removeAttacker(this) &&
	 * 			|	new.getOpponent() == null
	 * @effect	The attackers of this unit have their
	 * 			opponents set to null.
	 * 			| for each attacker in getAttackers : (
	 * 			|	attacker.setOpponent(null)
	 * @effect	The item this unit is carrying is dropped at
	 * 			its position.
	 * 			| dropAt(getPosition)
	 * @effect	This unit is removed from its block.
	 * 			| getBlock.removeUnit(this)
	 * @effect	This unit is removed from its world.
	 * 			| getWorld().removeUnit(this)
	 * @effect	Set terminated to true
	 * 			| isTerminated == true
	 */

	public void terminate(){
		if(this.opponent != null){
			opponent.removeAttacker(this);
			this.opponent = null;
		}
		for(Unit attacker : new ArrayList<Unit>(this.getAttackers())){
			attacker.terminateAttack();
		}
		this.dropAt(this.getBlock().getLocation());
		this.getBlock().removeUnit(this);
		this.getWorld().removeUnit(this);
		this.terminated = true;
	}
	
	public Unit getClosestUnit(){
		if(this.getWorld() == null)
			//TODO exception gooien?
			return null;
		Iterator<Unit> iterator = this.getWorld().getUnits().iterator();
		Unit unit = iterator.next();
		double distance = unit.getPosition().distance(this.getPosition());
		while (iterator.hasNext()){
			Unit tempUnit = iterator.next();
			double tempDistance = tempUnit.getPosition().distance(this.getPosition());
			if(tempDistance < distance)
				unit = tempUnit;
		}
		return unit;
	}
	
	public Unit getClosestEnemy(){
		if(this.getWorld() == null)
			//TODO exception gooien?
			return null;
		Set<Unit> enemies = this.getWorld().getUnits();
		enemies.removeAll(this.getFaction().getUnits());
		if(enemies.isEmpty())
			//TODO exceprions gooien?
			return null;
		Iterator<Unit> iterator = enemies.iterator();
		Unit unit = iterator.next();
		double distance = unit.getPosition().distance(this.getPosition());
		while (iterator.hasNext()){
			Unit tempUnit = iterator.next();
			double tempDistance = tempUnit.getPosition().distance(this.getPosition());
			if(tempDistance < distance)
				unit = tempUnit;
		}
		return unit;
	}
	
	public Unit getClosestFriend(){
		if(this.getWorld() == null)
			//TODO exception gooien?
			return null;
		Set<Unit> friends = this.getFaction().getUnits();
		friends.remove(this);
		if(friends.isEmpty())
			return null;
		Iterator<Unit> iterator = friends.iterator();
		Unit unit = iterator.next();
		double distance = unit.getPosition().distance(this.getPosition());
		while (iterator.hasNext()){
			Unit tempUnit = iterator.next();
			double tempDistance = tempUnit.getPosition().distance(this.getPosition());
			if(tempDistance < distance)
				unit = tempUnit;
		}
		return unit;
	}
	
	public Boulder getClosestBoulder(){
		if(this.getWorld() == null)
			//TODO exception gooien?
			return null;
		Iterator<Boulder> iterator = this.getWorld().getBoulders().iterator();
		Boulder boulder = iterator.next();
		double distance = boulder.getPosition().distance(this.getPosition());
		while (iterator.hasNext()){
			Boulder tempBoulder = iterator.next();
			double tempDistance = tempBoulder.getPosition().distance(this.getPosition());
			if(tempDistance < distance)
				boulder = tempBoulder;
		}
		return boulder;
	}
	
	public Log getClosestLog(){
		if(this.getWorld() == null)
			//TODO exception gooien?
			return null;
		Iterator<Log> iterator = this.getWorld().getLogs().iterator();
		Log log = iterator.next();
		double distance = log.getPosition().distance(this.getPosition());
		while (iterator.hasNext()){
			Log tempLog = iterator.next();
			double tempDistance = tempLog.getPosition().distance(this.getPosition());
			if(tempDistance < distance)
				log = tempLog;
		}
		return log;
	}
	
	public Block getClosestWorkshop(){
		if(this.getWorld() == null)
			//TODO exception gooien?
			return null;
		Iterator<Block> iterator = this.getWorld().getWorkshops().iterator();
		Block workshop = iterator.next();
		double distance = workshop.getLocation().distance(this.getPosition());
		while (iterator.hasNext()){
			Block tempWorkshop = iterator.next();
			double tempDistance = tempWorkshop.getLocation().distance(this.getPosition());
			if(tempDistance < distance)
				workshop = tempWorkshop;
		}
		return workshop;
	}
	
	public void startFollow(Unit unit){
		this.setFollowTarget(unit);
		this.move2(unit.getPosition());
	}
	
	public void follow(){
		if (this.getWorld().getAdjacent(this.getBlock()).contains(this.getFollowTarget().getBlock())){
			this.setFollowTarget(null);
		}
		if (this.getFollowTarget().isTerminated()){
			this.setFollowTarget(null);
		}
	}
	
	private Unit getFollowTarget() {
		return followTarget;
	}
	private void setFollowTarget(Unit followTarget) {
		this.followTarget = followTarget;
	}

	/**
	 * Orientation of the unit in radians
	 */
	private double theta;
	/**
	 * List containing the blocks of the shortest path to the final target
	 */
	private ArrayList<Block> Path = new ArrayList<Block>();
	/**
	 * Map containing the primary stats of this unit
	 */
	private Map<String, Integer> primStats = new HashMap<String, Integer>();
	/**
	 * Position of this unit
	 */
	private Vector pos;
	/**
	 * Center of the adjacent block the unit is currently moving to.
	 */
	private Vector target;
	/**
	 * Center of the block that is it's final target to move towards.
	 */
	private Vector finTarget = null;
	/**
	 * Name of this unit	
	 */
	private String name;
	/**
	 * Speed of this unit
	 */
	private double v;
	/**
	 * Velocity of this unit
	 */
	private Vector v_vector = new Vector(0.0, 0.0, 0.0);
	/**
	 * Minimal name length. This is a static variable and cannot be changed.
	 */
	private static final int NAMELENGTH_MIN = 2;
	/**
	 * Set of characters besides the alphabet that are allowed in the name of this unit.	
	 * This is a static variable and cannot be changed.
	 */
	private static final Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	/**
	 * Time until the next attack can be performed
	 */
	private double attcooldown = 0;
	/**
	 * Hitpoints of this unit
	 */
	private double hp;
	/**
	 * Stamina of this unit
	 */
	private double stam;
	/**
	 * State this unit is in. 
	 * This can be IDLE, COMBAT, FALLING, WORKING, RESTING, WALKING or SPRINTING.
	 */
	private State state;
	/**
	 * Time until the current worktask is completed
	 */
	private double workTime = 0;
	/**
	 * Set containing the units currently attacking this unit
	 */
	private Set<Unit> attackers = new HashSet<Unit>();
	/**
	 * Time until this unit must rest
	 */
	private double restTime = 0;
	/**
	 * Boolean indicating whether this unit is in Default or not.
	 */
	private boolean Default = false;
	/**
	 * Minimal time this unit must rest before other tasks can be assigned.
	 */
	private double minRestTime = 0;
	/**
	 * Boolean indicating whether this unit is attacking.
	 */
	private boolean attackInitiated = false;
	/**
	 * Set containing the keys of the Map primStat
	 * This is a static variable and cannot be changed.
	 */
	private static final Set<String> primStatSet = new HashSet<String>(Arrays.asList("str", "wgt", "agl", "tgh"));
	/**
	 * Faction this unit belongs to
	 */
	private Faction faction;
	/**
	 * List of the states COMBAT, WALKING, RESTING, WORKING.
	 * This is a static variable and cannot be changed.
	 */
	private static final ArrayList<State> stateList = new ArrayList<State>(Arrays.asList(State.COMBAT, State.WALKING, State.RESTING, State.WORKING));
	/**
	 * Boulder this unit is carrying
	 */
	private Boulder boulder = null;
	/**
	 * Log this unit is carrying
	 */
	private Log log = null;
	/**
	 * Weight of the log or boulder this unit is carrying
	 */
	private int carryWeight = 0;
	/**
	 * World this unit belongs to
	 */
	private World world;
	/**
	 * Height this unit is falling from
	 */
	private int fallHeight;
	/**
	 * Current experience this unit has
	 */
	private int experience = 0;
	/**
	 * Block this unit is currently working in
	 */
	private Block workBlock=null;
	/**
	 * Unit this unit is in combat with
	 */
	private Unit opponent = null;
	/**
	 * Boolean indicating whether this unit is dead or not.
	 */
	private boolean terminated = false;
	/**
	 * Unit this unit is currently following
	 */
	private Unit followTarget = null;
}
