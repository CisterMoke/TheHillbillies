
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
 *
 */
public class Unit {
	/**
	 * Initializes this new Hillbilly unit with the given name, position and primary stats.
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
	 * @effect	The position of the unit is set to the given position.
	 * 			| setPosition(x, y, z)
	 * @effect	The orientation of the unit is set to pi/2.
	 * 			| setTheta(Math.PI/2)
	 * @post	The base speed of the unit equals
	 * 			1.5 * (strength + agility)/(2*totalWeight).
	 * 			| new.getBaseSpeed() ==
	 * 			|	1.5 * (getPrimStats().get("str")+getPrimStats().get("agl"))
	 * 			|			/(2*getTotalWeight())
	 * @post	The state of the unit is IDLE.
	 * 			| new.getState() == State.IDLE
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
		
	}
	/**
	 * Updates the unit's state, position, attack cooldown, rest time, minimum required rest time,
	 * 	work time, hitpoints and stamina points based on the given time interval.
	 * @param dt
	 * 			The time interval in seconds.
	 * @post	If the minimum required rest time is bigger than zero,
	 * 			the given time interval is subtracted from it.
	 * 			The method then ends here.
	 * @post	If the attack cooldown is bigger than zero,
	 * 			the given time interval is subtracted from it.
	 * 			The method then ends here.
	 * @post	If the unit's state is COMBAT, it will attack all the units
	 * 			in its set of combatants.
	 * 			The method then ends here.
	 * @post	If the unit's state is RESTING and hasn't fully recovered its hitpoints,
	 * 			an amount of (toughness/200 * dt/0.2) is added. If the new
	 * 			amount of hitpoints is invalid, it will be set to the maximum
	 * 			allowed amount.
	 * 			The method then ends here.
	 * @post	If the unit's state is RESTING and has fully recovered its hitpoints but 
	 * 			hasn't fully recovered its stamina points, an amount of 
	 * 			(toughness/100 * dt/0.2) will be added. If the new
	 * 			amount of stamina points is invalid, it will be set to the maximum
	 * 			allowed amount.
	 * 			The method then ends here.
	 * @post	If the unit's state is WORKING and its work time is bigger than zero,
	 * 			the given time interval will be subtracted from the work time.
	 * 			The method then ends here.
	 * @effect	If the unit's state isn't IDLE and it has reached its final target, its
	 * 			state will be set to IDLE.
	 * 			The method then ends here.
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
		if (dt<0 || dt>0.2)
			throw new IllegalArgumentException("Invalid time interval!");
		if (this.isDead()){
			this.drop();
			this.getWorld().removeUnit(this);
			return;
		}
		if (this.getMinRestTime() > 0){
			this.setMinRestTime(this.getMinRestTime()- dt);
		}
		if (this.getAttackCooldown() > 0){
			this.setAttackCooldown(this.getAttackCooldown() - dt);
		}
		if(this.getState() == State.FALLING){
			Vector newPos = this.getPosition();
			Vector distance = this.getV_Vector();
			distance.multiply(dt);
			newPos.add(distance);
			this.setPosition(newPos);
			if(!this.shouldFall())
				this.land();
			return;
		}
		if(this.shouldFall()){
			this.fall();
			return;
		}
		
		if(this.DefaultOn())
			this.defaultBehaviour();
			
		
		if(this.getState() == State.COMBAT){
			Set<Unit> combatantsCopy = new HashSet<Unit>(this.combatants);
			for (Unit unit : combatantsCopy){
				this.attack(unit);
			}
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
				this.work();
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
			Vector nextPos = this.getPosition();
			Vector stepSize = this.getV_Vector();
			stepSize.multiply(dt);
			nextPos.add(stepSize);
			Vector distance = new Vector(this.getTarget());
			distance.add(nextPos.getOpposite());
			distance.normalize();
			Vector currentDirection = this.getV_Vector();
			currentDirection.normalize();
			distance.add(currentDirection.getOpposite());
			boolean moved = false;
			if(distance.getLength() > 0.5){
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
					Vector newTarget = this.getBlockCentre();
					this.setTarget(newTarget);
					
				}
				moved = true;
			}
		}
	}
	
	/**
	 * Returns the name of this unit.
	 * 
	 */
	@Basic
	public String getName(){
		return this.name;
	}
	
	/**
	 * Changes the name of the unit to a valid name.
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
	 * Returns whether or not the given name is a valid name.
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
	 * Returns a set containing the valid characters, excluding letters.
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
	 * Sets the position of the unit to the given position.
	 * @param x
	 * 			The x-coordinate of the new position.
	 * @param y
	 * 			The y-coordinate of the new position.
	 * @param z
	 * 			The z-coordinate of the new position.
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
			this.getBlock().removeUnit(this);;
			this.pos = position;
			this.getBlock().addUnit(this);
		}
		else this.pos = position;
	}
	/**
	 * Returns the vector representing the current position of the unit.
	 * 
	 */
	@Basic
	public Vector getPosition(){
		return new Vector(this.pos);
	}
	/**
	 * Returns whether or not the given position lies within the boundaries.
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
	 * 
	 * Returns a map containing the primary stats of the unit.
	 */
	@Basic
	public Map<String, Integer> getPrimStats(){
		return new HashMap<String, Integer>(this.primStats);
	}
	/**
	 * Sets the primary stats of the unit to the given stats.
	 * @param primStats
	 * 			A map containing the new primary stats.
	 * @post	Nothing happens if the given map of primary stats is of the wrong format.
	 * 			| if(!primStats.keySet().equals({"str", "wgt", "agl", "tgh"}))
	 * 			|	return
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
	 * @post	The new weight of the unit is bigger than or equal to
	 * 			the sum of the new strength and the new agility, divided by two.
	 * 			| new.getPrimStats().get("wgt")
	 * 			|	 >= (new.getPrimStats().get("str") + new.getPrimStats("agl")) / 2)
	 * @post	The new primary stats only contains the unit's strength, agility,
	 * 			weight and toughness.
	 * 			| new.getPrimStats.keySet().equals({"str", "agl", "wgt", "tgh"})
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
		if (!this.isValidWeight(primStats)){
			primStats.put("wgt", (int)(Math.ceil(((double)(primStats.get("str"))+(double)(primStats.get("agl"))) / 2)));
		}		
		 this.primStats = new HashMap<String, Integer>(primStats);
	}
		 
	/**
	 * 
	 * Returns the orientation of this unit in the x-y plane in radians.
	 */
	@Basic
	public Double getTheta(){
		return this.theta;
	}
	/**
	 * Sets the orientation of this unit to the given angle.
	 * @param angle
	 * 			The new angle of orientation of the unit.
	 * 
	 * @post The orientation of the unit is set to the given angle.
	 * 			|new.getTheta() == angle
	 */
	public void setTheta (Double angle){
		this.theta = angle;
	}
	
	/**
	 * Returns the vector of the center of the block in which the unit is located.
	 * 
	 */
	public Vector getBlockCentre(){
		double blockX = Math.floor(this.pos.getX()) + 0.5;
		double blockY = Math.floor(this.pos.getY()) + 0.5;
		double blockZ = Math.floor(this.pos.getZ()) + 0.5;
			
		return  new Vector(blockX, blockY, blockZ);
	}
	/**
	 *  Initiates the movement of the unit to a given adjacent block.
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
	 * 			| newTarget == this.getBlockPosition
	 * 			| newTarget.add(dx + 0.5, dy + 0.5, dz + 0.5)
	 * 			| setTarget(newTarget)
	 * @effect	A new velocity vector is created, pointing in the direction of the target.
	 * 			| setV_Vector()
	 * @effect	The orientation of the unit is set in the direction of the velocity vector.
	 * 			| this.setTheta(arctan(this.getV_Vector.get(1)/this.getV_Vector(0)))
	 * @effect	When going down, the walking speed is set to 1.2*BaseSpeed.
	 * 			When going up, the walking speed is set to 0.5*BaseSpeed.
	 * 			Otherwise the walking speed is set to BaseSpeed.
	 * 			|setWalkSpeed(getBaseSpeed())
	 * 			| if(dz == -1)
	 * 			|	then setWalkSpeed(1.2*this.getBaseSpeed())
	 * 			| else if(dz == 1)
	 * 			|	then setWalkSpeed(0.5*this.getBaseSpeed())
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
		Vector target = this.getBlockCentre();
		target.add(dx, dy, dz);
		this.setTarget(target);
	}
	/**
	 * Initiates the movement of a unit to a given position.
	 * 	The position is set to the centre of the block containing
	 * 	this position.
	 * @param x
	 * 			The x-coordinates of the given position.
	 * @param y
	 * 			The y-coordinates of the given position.
	 * @param z
	 * 			The z-coordinates of the given position.
	 * 
	 * @post	The final target is removed if the unit's position equals
	 * 			the position of the final target. The method then ends here
	 * @effect	Tries to create a new final target using the given coordinates.
	 * 			If it fails to create one, nothing happens.
	 * @effect	Tries to move to the centre of an adjacent block that is on the path towards the Final Target.
	 * 			If it fails, nothing happens.
	 */
	public void move2(Vector pos){
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
			Vector newTarget = new Vector(newBlock.getLocation());
			newTarget.add(0.5, 0.5, 0.5);
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
		Vector newpos = new Vector(this.getPath().get(0).getLocation());
		newpos.add(this.getWorld().getBlockAtPos(this.getTarget()).getLocation().getOpposite());
		try{
			this.moveToAdjacent((int)(newpos.getX()),(int) (newpos.getY()),(int) (newpos.getZ()));
		}
		catch(IllegalArgumentException exc){
			System.out.println("??????");
			return;
		}
	}
	
	
	
	
	
	/**
	 * Updates the unit's velocity vector.
	 * @effect 	A velocity vector is created pointing in the direction of the target.
	 * 			| tempV_Vector == this.getPosition()
	 * 			| tempV_Vector.add(this.getTarget().getOpposite())
	 * 			| tempV_Vector.normalize()
	 * 			| tempV_Vector.multiply(this.getSpeed())
	 * 			| new.getV_Vector() == tempV_Vector
	 */
	private void setV_Vector(){
		if(this.getState() == State.FALLING){
			this.v_vector = new Vector(0, 0, -3);
			return;
		}
		this.v_vector = this.getTarget();
		this.v_vector.add(this.getPosition().getOpposite());
		this.v_vector.normalize();
		this.v_vector.multiply(this.getSpeed());
	}
	/**
	 * 
	 * Returns the velocity vector of this unit.
	 */
	@Basic
	public Vector getV_Vector(){
		return new Vector(this.v_vector);
	}
	
	/**
	 * 	Initiates the attack of this unit on the given defending unit.
	 * @param defender
	 * 			The defending unit against which the attack is initiated.
	 * @post	Nothing happens if the unit hasn't rested long enough,
	 * 			is moving or if the defender is out of range.
	 * 			| if(getMinRestTime() > 0 || !inRange(defender) || isMoving())
	 *			|	then return
	 * @effect	Tries to add the defender to the unit's set of combatants.
	 * 			If an IllegalArgumentException is caught, nothing happens
	 * 			| if(addCombatant(defender) throws IllegalArgumentException)
	 *			| 	then return
	 *			| else addCombatant(defender)
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
		if(this.getMinRestTime() > 0 || !this.inRange(defender) || this.isMoving()){
			return;
		}
		try{
			this.addCombatant(defender);
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
	 * 
	 * Returns true if the attack is initiated.
	 */
	@Basic
	private boolean isAttackInitiated(){
		return this.attackInitiated;
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
	 * The unit performs an attack on the defender.
	 * @param defender
	 * 			The defender that get's attacked by the unit.
	 * @effect 	Nothing happens if both units are from the same faction.
	 * 			| if (getFaction == defender.getFaction)
	 * 			|	then return
	 * @effect	The attack get's initiated if it hasn't been initiated yet.
	 * 			The method then ends here.
	 * 			| if(!isAttackInitiated())
	 *			|	then initiateAttack(defender) && return	
	 * @post	Nothing happens if the unit hasn't finished its attack yet.
	 * 			| if(this.getAttackCooldown() > 0)
	 * 			|	return
	 * @post	Unit is set to Idle if the defender is dead, and nothing else happens.
	 * 			| if(defender.isDead())
	 * 			| 	this.setState(IDLE)
	 * 			|	return
	 * @effect	If the defender is out of range, the defender is removed from
	 * 			the set of combatants, this unit's state is set to IDLE
	 * 			and the value of isAttackInitiated is set to false.
	 * 			The method then ends here.
	 * 			| if (!this.inRange(defender))
	 *			|	then removeCombatant(defender)
	 *			|		&& setState(State.IDLE)
	 *			|		&& new.isAttackInitiated() == false
	 *			|		&& return
	 * @effect	The initiation of the attack is toggled.
	 * 			| toggleAttackInitiated()
	 * @effect	The defender is removed from the set of combatants.
	 * 			| removeCombatant(defender)
	 * @effect	If the unit performs an attack, the defender will try to defend the attack.	
	 * 			| defender.defend(this)
	 */
	public void attack(Unit defender){
		if (defender == null || defender.isDead()){
			this.setState(State.IDLE);
			return;
		}
		if(this.getState() == State.FALLING || defender.getState() == State.FALLING)
			return;
		if(this.getFaction() == defender.getFaction() && this.getFaction() != null)
			return;
		if(!this.isAttackInitiated()){
			this.initiateAttack(defender);
			return;
		}
		
		if (this.getAttackCooldown() > 0){
			return;
		}
		
		if (!this.inRange(defender)){
			this.removeCombatant(defender);
			this.setState(State.IDLE);
			this.toggleAttackInitiated();
			return;
		}
		this.toggleAttackInitiated();
		this.removeCombatant(defender);
		defender.defend(this);
	}
	/**
	 * 
	 * returns true if a unit's Hp is 0, meaning it is dead, and false otherwise.
	 */
	public boolean isDead(){
		if (Math.ceil(this.getHp())==0){
			return true;
		}
		return false;
	}
	/**
	 * Returns the probability of dodging an attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @return The probability equals 0.2 times the ratio of the units agility to the attacker's agility.
	 * 			|result == 0.2*this.getPrimStats().get("agl")/attacker.getPrimStats().get("agl")
	 */
	private double getDodgeProb(Unit attacker){
		return ((double)(0.2*this.getPrimStats().get("agl")))/attacker.getPrimStats().get("agl");
	}
	/**
	 * Returns whether or not then unit successfully dodged the attack.
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
	 * The unit attempts to dodge the attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @effect	The unit will randomly select an adjacent block. It tries to 
	 * 			set the position of the unit in that block. If it fails, it
	 * 			randomly select an adjacent block until it succeeds to move the unit.
	 *			| while(new.getPosition.equals(old.getPosition()) : (
	 *			| 	newPosition == Vector(oldPosition)
	 *			|	newPosition.add(randInt(), randInt(), 0)
	 *			|	if(!(setPosition(newPosition) throws IllegalArgumentException))
	 *			|		then setPosition(newPosition)
	 * @post	If the unit dodged the attack, the target of the unit is set to its position.
	 * 			| new.getTarget() == getPosition()
	 * @effect The unit will move to its final target if the unit hasn't reached it yet.
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
				Vector distance = new Vector(dodgeBlocks.get(0).getLocation());
				distance.add(this.getBlock().getLocation().getOpposite());
				Vector nextPos = this.getPosition();
				nextPos.add(distance);
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
	 * Returns the probability of blocking an attack.
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
	 * Returns whether or not the unit has blocked the attack of the attacker.
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
	 * @effect	The unit attempts to dodge the attack. If it succeeds, it won't take any damage.
	 * 			The method ends here.
	 * 			| if(this.hasDodged(attacker))
	 * 			|	then dodge(attacker) && new.getHp() == getHp()
	 * 			|	&& return
	 * @post	If the unit failed to dodge the attack, it will attempt to block the attack.
	 * 			If it succeeds, it won't take any damage.
	 *			| if(this.hasBlocked(attacker))
	 *			| then new.getHp() == this.getHp() && return
	 * @effect	If the unit neither dodged nor blocked the attack it will lose an amount of hitpoints
	 * 			equal to the attacker's strength divided by ten. If this amount is bigger than the current
	 * 			amount of hitpoints of the unit, its hitpoints will be set to zero.
	 * 			| damage == attacker.getPrimStats().get("str")/10
	 * 			| if (!isValidHp(getHp() - damage))
	 *			|	then setHp(0)
	 *			| else
	 *			|	setHp(this.getHp() - damage)
	 */
	private void defend(Unit attacker){
		this.setState(State.IDLE);
		attacker.setState(State.IDLE);
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
	 * 
	 * Returns whether or not the unit is set to the default behaviour.
	 */
	@Basic
	public boolean DefaultOn(){
		return this.Default;
	}
	
	/**
	 * Sets the behaviour of the unit to the default behaviour.
	 * @post 	The unit is set to default behaviour.
	 * 			| new.DefaultOn() == true
	 */
	public void startDefault(){
		this.Default = true;
	}
	/**
	 * Sets the behaviour of the unit from default behaviour to no behaviour.
	 * @post	The unit is set from default behaviour to no behaviour
	 * 			| new.DefaultOn() == false
	 */
	public void stopDefault(){
		this.Default = false;
	}
	
	/**
	 * 
	 * Returns the current amount of hitpoints of this unit.
	 */
	@Basic
	public double getHp(){
		return this.hp;
	}
	
	/**
	 * Sets the amount of hitpoints of the unit to the given amount.
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
	}
	/**
	 * 
	 * Returns the current amount of stamina points of this unit.
	 */
	@Basic
	public double getStam(){
		return this.stam;
	}
	/**
	 * Sets the amount of stamina points of the unit to the given amount.
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
	 * Returns whether or not the given amount of hitpoints is a valid amount.
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
	 * Returns whether or not the given amount of stamina points is a valid amount.
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
	 * 
	 * Returns the maximum allowed amount of hitpoints of this unit.
	 * 		It is always equal to (strength*weight)/50.
	 */
	@Basic
	public int getMaxHp(){
		return (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50));
	}
	/**
	 * 
	 * Returns the maximum allowed amount of stamina points of this unit.
	 * 		It is always equal to (strength*weight)/50.
	 */
	@Basic
	public int getMaxStam(){
		return (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50));
	}
	/**
	 * 
	 * Returns the current target of the unit.
	 * 		The target of a unit is a position at which the unit
	 * 		will directly move towards.
	 */
	@Basic
	public Vector getTarget(){
		return new Vector(this.target);
	}
	/**
	 * Sets the target of the unit to the given target.
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
	 * 
	 * Returns the final target of this unit.
	 * 		The final target of a unit is a position at which
	 * 		the unit will move towards by setting new targets.
	 */
	@Basic
	public Vector getFinTarget(){
		if (this.finTarget == null)
				return null;
		return new Vector(this.finTarget);
	}
	/**
	 * Sets the final target of the unit to the given position.
	 * @param target
	 * 			The given position of the new final target.
	 * @post	The final target of the unit is set to the given position if its is valid.
	 * 				|if (isValidPosition(target))
	 * 				|	then new.getFinTarget() == target
	 * @throws IllegalArgumentException
	 * 			Throws and exception if the target is an invalid position.
	 * 				|!isValidPosition(target)
	 */
	public void setFinTarget(Vector target) throws IllegalArgumentException{
		if(target.equals(this.getTarget()))
			return;
		if (!this.isValidPosition(target) && !this.getWorld().isWalkable(this.getWorld().getBlockAtPos(target)))
			throw new IllegalArgumentException("Invalid Target!");
		this.finTarget = target;
	}
	
	/**
	 * 
	 * Returns the base speed of this unit.
	 */
	@Basic
	public double getBaseSpeed(){
		return 1.5*((double)(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))/(2*this.getTotalWeight());
	}
	
	/**
	 * 
	 * Returns the set containing the units this unit is currently in combat with.
	 */
	@Basic
	public Set<Unit> getCombatants(){
		return this.combatants;
	}
	
	/**
	 * Adds the given unit to the set of combatants.
	 *  	A unit will continue attacking a combatant until it is removed from the set.
	 * @param unit
	 * 			The given unit to be added to the set of combatants.
	 * @post	The given unit is added to the set of combatants.
	 * 			|new.getCombatants().contains(unit)
	 * @throws	IllegalArgumentException
	 * 			An IllegalArgumentException is thrown if the given unit
	 * 			is the unit itself.
	 * 			|unit == this
	 */
	private void addCombatant(Unit unit) throws IllegalArgumentException{
		if (unit == this){
			throw new IllegalArgumentException("You are not allowed to fight with yourself!");
		}
		this.combatants.add(unit);
	}
	
	/**
	 * Removes the given unit from the set of combatants of this unit.
	 * @param unit
	 * 			The given unit to be removed from the set of combatants of this unit.
	 * @post	The given unit is removed from the set of combatants of this unit.
	 * 			|!new.getCombatants().contains(unit)
	 */
	private void removeCombatant(Unit unit){
		this.combatants.remove(unit);
	}
	/**
	 * Returns the current speed of the unit
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
	 * 
	 * Returns the walking speed of this unit.
	 */
	public double getWalkSpeed(){
		return this.v;
	}
	/**
	 * Sets the walking speed of this unit to the given speed.
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
	 * 
	 * @author Joost Croonen & Ruben Dedoncker
	 *
	 */
	public enum State{
		IDLE, COMBAT, WALKING, WORKING, RESTING, SPRINTING, FALLING		
	}
	/**
	 * Sets the state of the unit to the given state.
	 * @param state
	 * 			The given state to be set to the new state.
	 * @post 	The state of the unit is set to the given state.
	 * 			|new.getState() == state
	 * @post	The state of the unit remains unchanged if it hasn't rested long enough and is not in combat.
	 * 			|if(this.getMinRestTime() > 0 && state != State.COMBAT)
	 *			|	return
	 *@effect	The velocity vector is updated
	 *			|this.setV_Vector()
	 */
	private void setState(State state){
		if (state == State.COMBAT){
			this.setMinRestTime(0);
			this.setWorkTime(0);
			
		}
		if(this.getMinRestTime() > 0 && state != State.COMBAT){
			return;
		}
		this.state = state;
		this.setV_Vector();
	}
	/**
	 * 
	 * Returns the current state of the unit.
	 */
	@Basic
	public State getState(){
		return this.state;
	}
	/**
	 * Returns whether or not the unit is moving.
	 * @return True if the unit is either walking or sprinting
	 * 			| result == (getState() == State.WALKING) || (getState() == State.SPRINTING)
	 */
	public boolean isMoving(){
		return (this.getState() == State.WALKING || this.getState() == State.SPRINTING);
	}
	/**
	 * 
	 * Returns the remaining amount of work time needed.
	 */
	@Basic
	public double getWorkTime() {
		return this.workTime;
	}
	/**
	 * Sets the remaining amount of work time needed to the given time.
	 * @param newTime
	 * 			The given time to be set as the new amount of work time needed.
	 * @post  	The remaining amount of work time needed is set to the given time.
	 * 			|new.getWorkTime() == newTime
	 */
	private void setWorkTime(double newTime){
		this.workTime = newTime;
	}
	/**
	 * The unit starts to work.
	 * @post	Nothing happens if the unit is moving.
	 * 			| if (this.isMoving())
	 * 			|	return
	 * @effect 	If the unit isn't working, it is set to work and the work time is set to 500/strength.
	 * 			|if(getState() != State.WORKING)
	 * 			|	setState(State.WORKING)
	 *			|	setWorkTime(500/getPrimStats().get("str"));
	 * 
	 */
	public void work(){
		if (this.isMoving() || this.getState() == State.COMBAT || this.getState() == State.FALLING)
			return;
		if (this.getState() != State.WORKING){
			this.setState(State.WORKING);
			this.setWorkTime(500/this.getPrimStats().get("str"));
		}
	}
	
	public void workAt(Vector target){
		if (this.isMoving() || this.getState() == State.COMBAT || this.getState() == State.FALLING)
			return;
		Vector centerTarget = new Vector((double)Math.floor(target.getX()), (double)Math.floor(target.getY()), (double)Math.floor(target.getZ()));
		centerTarget.add(0.5, 0.5, 0.5);
		Vector distance = new Vector(centerTarget);
		distance.add(this.getBlockCentre().getOpposite());
		if (distance.getLength() > 1.8){
			return;
		}
		Block targetBlock = this.getWorld().getBlockAtPos(centerTarget);
		this.setWorkBlock(targetBlock);
		if (this.getState() != State.WORKING){
			this.setState(State.WORKING);
			this.setWorkTime(100/this.getPrimStats().get("str"));
			this.setTheta(Math.atan2(distance.getY(), distance.getX()));
		}
		
	}
	
	public void workCompleted(){
		System.out.println("done");
		boolean worked = false;
		if (this.getWorkBlock().getBlockType()==BlockType.WORKSHOP){
			if (!this.getWorkBlock().getBouldersInCube().isEmpty() && !this.getWorkBlock().getLogsInCube().isEmpty()){
				System.out.println("workshop");
				this.operateWorkshop(this.getWorkBlock());
				worked = true;
			}
		}
		if ((this.getWorkBlock().getBlockType()==BlockType.ROCK || this.getWorkBlock().getBlockType()==BlockType.WOOD) && worked==false){
			System.out.println("dig");
			this.getWorld().spawnObject(this.getWorkBlock());
			this.getWorld().setToPassable(this.getWorkBlock());
			worked = true;
		}
		if (this.isCarrying() && worked==false){
			System.out.println("drop");
			this.dropAt(this.getWorkBlock().getLocation());
			worked = true;
		}
		System.out.println(this.getWorkBlock().getBouldersInCube().size());
		if ((!this.getWorkBlock().getBouldersInCube().isEmpty() || !this.getWorkBlock().getLogsInCube().isEmpty())&& worked==false){
			System.out.println("pickup");
			this.pickup(this.getWorkBlock());
			worked = true;
		}
		System.out.println("end");
		this.setWorkBlock(null);
		if (worked == true)
			this.addExp(10);
		this.setState(State.IDLE);
	}
	
	public void pickup(Block targetBlock){
		int random = (int) (Math.random()*targetBlock.getBouldersInCube().size());
		int counter = 0;
		for (Boulder boulder : targetBlock.getBouldersInCube()){
			if (counter == random){
				this.setBoulder(boulder);;
				this.getWorld().removeBoulder(boulder);
			}
			counter++;
		}
		
		int random2 = (int) (Math.random()*targetBlock.getBouldersInCube().size());
		int counter2 = 0;
		for (Log log : targetBlock.getLogsInCube()){
			if (counter2 == random2){
				this.setLog(log);
				this.getWorld().removeLog(log);

			}
			counter2++;
		}
		
	}
	
	public void operateWorkshop(Block targetBlock){
		int random = (int) (Math.random()*targetBlock.getBouldersInCube().size());
		int counter = 0;
		for (Boulder boulder : targetBlock.getBouldersInCube()){
			if (counter == random){
				this.getWorld().removeBoulder(boulder);
			}
			counter++;
		}
		int random2 = (int) (Math.random()*targetBlock.getBouldersInCube().size());
		int counter2 = 0;
		for (Log log : targetBlock.getLogsInCube()){
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
		if(!isValidWeight(newStats)){
			newStats.put("wgt", (int) Math.ceil(((double)(primStats.get("str"))+(double)(primStats.get("agl"))) / 2));
		}
		this.setPrimStats(newStats);
		this.setHp(hpRatio*this.getMaxHp());
		this.setStam(stamRatio*this.getMaxStam());
	}
	
	public void dropAt(Vector blockTarget){
		blockTarget.add(new Vector(0.5, 0.5, 0.5));
		if(getBoulder() != null){
			this.getBoulder().setPosition(blockTarget);
			this.getBoulder().removeCarrier();
			this.getWorld().addBoulderAt(this.getWorkBlock(), this.getBoulder());
			this.boulder = null;			
		}
		else{
			this.getLog().setPosition(blockTarget);
			this.getLog().removeCarrier();
			this.getWorld().addLogAt(this.getWorkBlock(), this.getLog());
			this.log = null;
		}
		this.setCarryWeight(0);
	}
	
	/**
	 * Returns whether or not the given unit is in range. Two units are in range if they
	 *	occupy the same block or a block adjacent to the other block.
	 * @param unit
	 * 			The unit to be checked.
	 * @return True if and only if the distance between the units is less than 2 meters.
	 * 			| distance == Vector(this.getBlockPosition)
	 * 			| distance.add(unit.getBlockPosition.getOpposite())
	 * 			| result == distance.getLength() < 2
	 */
	public boolean inRange(Unit unit){
		boolean inRange = false;
		Vector distance = this.getBlockCentre();
		distance.add(unit.getBlockCentre().getOpposite());
		if (distance.getLength() < 1.8)
			inRange = true;
		return inRange;
	}
	/**
	 * 
	 * Returns the time that has passed since this unit last rested.
	 */
	@Basic
	public double getRestTime(){
		return this.restTime;
	}
	/**
	 * Sets the time that has passed since the unit last rested to the given time.
	 * @param newTime
	 * 			The given time to be set to the time that has passed since the unit last rested.
	 * @post 	The time that has passed since the unit last rested is set to the given time.
	 * 			|new.getRestTime() == newTime
	 */
	private void setRestTime(double newTime){
		this.restTime = newTime;
	}
	/**
	 * 
	 * Returns the time until this unit can perform an attack.
	 */
	@Basic
	public double getAttackCooldown(){
		return this.attcooldown;
	}
	/**
	 * Sets the time until this unit can perform an attack to the given time.
	 * @param newTime
	 * 			The given time to be set to the time until this unit can perform an attack.
	 * @post	The cooldown is set to the given time.
	 * 			|new.getAttackCooldown() == newTime
	 */
	private void setAttackCooldown(double newTime){
		this.attcooldown = newTime;
	}
	/**
	 * 
	 * Returns the time the unit needs to rest until it can perform any other actions.
	 */
	@Basic
	public double getMinRestTime(){
		return this.minRestTime;
	}
	/**
	 * Sets the time the unit needs to rest until it can perform any other actions to the given time.
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
	 * @post	Nothing happens if the unit is moving or in combat
	 * 			and if the rest time is bigger than 180 seconds.
	 * 			| if ((isMoving() || getState() == State.COMBAT) && (restTime >= 180) )
	 * 			| 	return
	 * @effect If the unit isn't resting, its state is set to resting
	 * 			and its minimum resting time is set to 40/toughness.
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
	 * 			faction and this unit is added as a member of
	 * 			the given faction.
	 * @effect	If this unit can belong to the given faction,
	 * 			this unit's faction is set to the given faction.
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
	 * 			belonging to any faction.
	 * 			| return getFaction() == null
	 */
	@Basic @Raw
	public boolean canHaveAsFaction(Faction faction) {
		return this.faction == null;
	}
	/**
	 * Remove the unit from it's current faction.
	 * @effect	The unit is removed from the set of units
	 * 			belonging to its faction and the unit's
	 * 			faction is set to null.
	 * 			|this.faction.removeUnit(this) && new.getFaction == null
	 */
	public void removeFaction(){
		this.faction.removeUnit(this);
		this.faction = null;
	}
	
	/**
	 * Return a boolean stating whether or not the unit is carrying
	 * 	something.
	 * @return 	True if the unit is carrying a boulder or a log.
	 * 			|result == (getBoulder() != null || getLog() != null)
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
	 * Drop the prop that this unit is carrying.
	 * 
	 * @effect	Nothing happens if the unit isn't carrying
	 * 			anything.
	 * 			| if(!isCarrying)
	 * 			|	then return
	 * @effect	If the unit is carrying a boulder, the boulder's
	 * 			carrier will be removed and the boulder will be
	 * 			added to the world. The boulder this unit is
	 * 			carrying will also be removed.
	 * 			Otherwise the log's carrier will be removed and
	 * 			the log will be added to the world. The log this
	 * 			unit is carrying will also be removed.
	 * 			| if(getBoulder != null)
	 * 			|	then getBoulder().removeCarrier() &&
	 * 			|	getWorld().addBoulder(getBoulder()) &&
	 * 			|	new.getBoulder() == null &&
	 * 			| else getLog().removeCarrier() &&
	 * 			|	getWorld().addLog(getLog()) &&
	 * 			|	new.getLog() == null
	 * @post	The carry weight is set to zero.
	 * 			| new.getCarryWeight() == 0
	 */
	public void drop(){
		if(!isCarrying())
			return;
		if(getBoulder() != null){
			this.getWorld().addBoulder(this.getBoulder());
			this.getBoulder().removeCarrier();
			this.boulder = null;			
		}
		else{
			this.getWorld().addLog(this.getLog());
			this.getLog().removeCarrier();
			this.log = null;
		}
		this.setCarryWeight(0);
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
		if(getBlockCentre() == boulder.getBlockCentre()){
			try {
				this.setBoulder(boulder);
				this.getWorld().removeBoulder(boulder);
			} catch (IllegalArgumentException exc) {
				return;
			}
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
		if(getBlockCentre() == log.getBlockCentre()){
			try {
				this.setLog(log);
				this.getWorld().removeLog(log);
			} catch (IllegalArgumentException exc) {
				return;
			}
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
	public int getTotalWeight(){
		return this.getPrimStats().get("wgt") + this.getCarryWeight();
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
	
	private void fall(){
		this.setState(State.FALLING);
		this.fallHeight = (int)(this.getPosition().getZ());
	}
	
	private void land(){
		this.setState(State.IDLE);
		this.setTarget(this.getPosition());
		if(this.getFinTarget() != null){
			this.Path.clear();
			this.setState(State.WALKING);
			this.move2(this.getFinTarget());
		}
		int damage = this.fallHeight - (int) (this.getPosition().getZ());
		if(!isValidHp(this.getHp() - damage *10))
			setHp(0);
		else this.setHp(this.getHp() - damage * 10);
	}
	
	public boolean shouldFall(){
		if(this.getWorld() == null)
			return false;
		if(!this.getWorld().isWalkable(this.getBlock()) && !this.getBlock().isSolid())
				return true;
		return false;
	}
	
	public Block getBlock(){
		return this.getWorld().getBlockAtPos(this.getBlockCentre());
	}
	/**
	 * Finds the shortest path from the position it is going to be after the current moveToAdjacent, to the FinalTarget, sets this as Path. 
	 */
	public void pathFinding(){
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
	 * Finds the set of blocks that must be checked surrounding the current block.
	 * @param current : Block for which the next blocks must be found.
	 * @param finalCost : Map containing the cost of all Blocks that have already been given the cost required to move to them. 
	 * 						This Map can therefore also be used to check whether a block has already been checked.
	 * @return A set containing all the blocks that are walkable, adjacent to current and not yet in finalCost. 
	 */
	public Set<Block> getNext(Block current, Map<Block, Double> finalCost){
		Set<Block> next = new HashSet<Block>();
		for (Block block : this.getWorld().getAdjacent(current)){
			if (this.getWorld().isWalkable(block) && !finalCost.containsKey(block)){
				next.add(block);
			}
		}
		return next;
	}
	
	public void setWorkBlock(Block block){
		this.workBlock = block;
	}
	
	public Block getWorkBlock(){
		return this.workBlock;
	}
	
	public ArrayList<Block> getPath(){
		return this.Path;
	}
	
	public Set<Unit> getAdjacentUnits(){
		Set<Unit> adjacentUnits = new HashSet<Unit>();
		for(Block block : this.getWorld().getAdjacent(this.getBlock()))
			adjacentUnits.addAll(block.getUnitsInCube());
		return adjacentUnits;
	}
	
	public void defaultBehaviour(){
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
			if(!this.getPath().isEmpty())
				System.out.println(this.getPath().isEmpty());
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
			this.initiateAttack(this.getEnemyInRange());
			}
	}
	
	
	
	
	public Unit getEnemyInRange(){
		Set<Unit> unitsInRange = new HashSet<Unit>(this.getAdjacentUnits());
		unitsInRange.addAll(this.getBlock().getUnitsInCube());
		unitsInRange.remove(this);
		for(Unit enemy : unitsInRange){
			if(enemy.getFaction() != this.getFaction())
				return enemy;
		}
		return null;
	}
	
	public int getExp(){
		return this.experience;
	}
	
	public void addExp(int exp){
		if(exp <= 0)
			return;
		this.experience += exp;
		while(this.experience >= 10){
			this.experience -= 10;
			this.levelUp();
		}
	}
	
	private void levelUp(){
		ArrayList<String> statList = new ArrayList<String>(this.getPrimStats().keySet());
		Collections.shuffle(statList);
		Map<String, Integer> newStats = this.getPrimStats();
		double hpRatio = this.getHp()/this.getMaxHp();
		double stamRatio = this.getStam()/this.getMaxStam();
		for(String stat : statList){
			if(this.getPrimStats().get(stat) != 200 ){
				newStats.put(stat, newStats.get(stat) + 1);
				if(!isValidWeight(newStats)){
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
	
	public boolean isValidWeight(Map<String, Integer> primStats){
		if(!primStats.keySet().equals(Unit.primStatSet))
			return false;
		if (primStats.get("wgt") < Math.ceil(((double)(primStats.get("str"))+(double)(primStats.get("agl"))) / 2))
			return false;
		return true;
	}
	
	public void updateFinTarget(){
		if(!this.getWorld().isWalkable(this.getWorld().getBlockAtPos(this.getFinTarget()))){
			this.finTarget = null;
			this.Path.clear();
		}
	}
	
	private double theta;
	
	private ArrayList<Block> Path = new ArrayList<Block>();
	
	private Map<String, Integer> primStats = new HashMap<String, Integer>();
	
	private Vector pos;
	
	private Vector target;
	
	private Vector finTarget = null;
	
	private String name;
	
	private double v;
	
	private Vector v_vector = new Vector(0.0, 0.0, 0.0);
	
	private static final int NAMELENGTH_MIN = 2;
	
	private static final Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	private double attcooldown = 0;
	
	private double hp;
	
	private double stam;
	
	private State state;
	
	private double workTime = 0;
	
	private Set<Unit> combatants = new HashSet<Unit>();
	
	private double restTime = 0;
	
	private boolean Default = false;
	
	private double minRestTime = 0;
	
	private boolean attackInitiated = false;
	
	private static final Set<String> primStatSet = new HashSet<String>(Arrays.asList("str", "wgt", "agl", "tgh"));

	private Faction faction = null;
	
	private static final ArrayList<State> stateList = new ArrayList<State>(Arrays.asList(State.COMBAT, State.WALKING, State.RESTING, State.WORKING));

	private Boulder boulder = null;
	
	private Log log = null;
	
	private int carryWeight = 0;

	private World world;
	
	private int fallHeight;
	
	private int experience = 0;
	
	private Block workBlock=null;
}
