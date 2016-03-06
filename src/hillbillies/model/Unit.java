
package hillbillies.model;
import java.util.*;

import be.kuleuven.cs.som.annotate.*;
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
	 * 			1.5 * (strength + agility)/(2*weight).
	 * 			| new.getBaseSpeed() ==
	 * 			|	1.5 * (getPrimStats().get("str")+getPrimStats().get("agl"))
	 * 			|			/(2*getPrimStats().get("wgt"))
	 * @post	The state of the unit is IDLE.
	 * 			| new.getState() == State.IDLE
	 * @throws IllegalArgumentException
	 * 			An exception is thrown is the name is invalid or if the position is invalid
	 * 			| !isValidName(name) || !isValidPosition(ArrayList<Double>(Arrays.asList(x, y, z))
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
		this.setPosition(x, y, z);
		this.setTheta(Math.PI/2);
		this.setTarget(this.getPosition());
		this.setBaseSpeed(1.5*((double)(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))
				/(2*this.getPrimStats().get("wgt")));
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
		if (this.getMinRestTime() > 0){
			this.setMinRestTime(this.getMinRestTime()- dt);
		}
		if (this.getAttackCooldown() > 0){
			this.setAttackCooldown(this.getAttackCooldown() - dt);
		}
		if(this.getState() == State.COMBAT){
			Set<Unit> combatantsCopy = new HashSet<Unit>(this.combatants);
			for (Unit unit : combatantsCopy){
				this.attack(unit);
			}
			return;
		}
		
		if (this.getState() == State.IDLE && this.DefaultOn()){
			int idx = this.randInt()+1;
			if (this.stateList.get(idx) == State.WALKING){
				this.setState(State.WALKING);
				this.moveTo(Math.random()*50, Math.random()*50, Math.random()*50);
			}
			if (this.stateList.get(idx)== State.WORKING){
				this.work();
			}
			if (this.stateList.get(idx) == State.RESTING){
				this.rest();
			}
		}
		
		if (this.DefaultOn() && this.getState() == State.WALKING){
			double sprintRoll = Math.random();
			double sprintChance = 0.001;
			if (sprintRoll < sprintChance){
				this.setState(State.SPRINTING);
			}
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
				this.setState(State.IDLE);
			}
				
			
		}
		if (this.getState() == State.IDLE && !this.getTarget().equals(this.getPosition())){
			this.setState(State.WALKING);
			this.setTheta(Math.atan2(v_vector.get(1),v_vector.get(0)));
		}
		if (this.getTarget().equals(this.getPosition()) && this.getFinTarget().isEmpty() && this.getState() != State.IDLE){
			this.setState(State.IDLE);
			return;
		}
		
		if (this.isMoving()){
			ArrayList<Double> nextPos = new ArrayList<Double>(this.getPosition());
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
			for (int i = 0; i<3; i++){
				nextPos.set(i, nextPos.get(i) + this.getV_Vector().get(i)*dt);
			}
			boolean moved = false;
			int idx = 0;
			while (!moved && idx<3){
				if(Math.signum(nextPos.get(idx)-this.getTarget().get(idx)) == Math.signum(this.getV_Vector().get(idx)) && (Math.signum(this.getV_Vector().get(idx)) != 0)){
					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
					if (!this.getFinTarget().isEmpty())
						moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
					moved = true;
				}
				idx += 1;
			}
			if (!moved){
				try{
					this.setPosition(nextPos.get(0), nextPos.get(1), nextPos.get(2));
				}
				catch (IllegalArgumentException exc){
					this.setTarget(this.getPosition());
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
	 * @throws IllegalArgumentException
	 * 			Throws an exception when an illegal coordinate is given.
	 * 			| !isValidPosition(ArrayList<Double>(Arrays.asList(x, y, z))))
	 * @post The new position is a valid position
	 * 			| new.getPosition() == ArrayList<Double>(Arrays.asList(x, y, z))
	 * 
	 */
	public void setPosition(double x, double y, double z)throws IllegalArgumentException{
		ArrayList<Double> pos= new ArrayList<Double>();
		pos.add(x);
		pos.add(y);
		pos.add(z);
		if (!isValidPosition(pos))
			throw new IllegalArgumentException("Out of bounds");
		this.pos = new ArrayList<Double>(pos);
	}
	/**
	 * Returns the coordinates of the current position of the unit.
	 * 
	 */
	@Basic
	public ArrayList<Double> getPosition(){
		return this.pos;
	}
	/**
	 * Returns whether or not the given position lies within the boundaries.
	 * @param pos
	 * 			The position the be checked.
	 * @return True if every coordinate lies 
	 * 			| result == true
	 * 			| for each element in pos : (
	 * 			| 	if (element > 50 && element < 0)
	 * 			|		then result == false)
	 */
	private boolean isValidPosition(ArrayList<Double> pos){
		boolean checker = true;
		for(int i=0; i<3; i++){
			if (pos.get(i)>50 || pos.get(i)<0)
				checker = false;
		}
		return checker;
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
		if (primStats.get("wgt") < Math.ceil(((double)(primStats.get("str"))+(double)(primStats.get("agl"))) / 2)){
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
	 * Returns the coordinates of the center of the block in which the unit is located.
	 * 
	 */
	public ArrayList<Double> getBlockPosition(){
		ArrayList<Double> blockpos = new ArrayList<Double>(this.pos);
		for (int i = 0; i<3; i++){
			
			blockpos.set(i, Math.floor(this.pos.get(i)) + 0.5);
		}
		return blockpos;
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
	 * 			|
	 * 			| setTarget(getBlockPosition().get(0) + dx,
	 * 			|				getBlockPosition().get(1) + dy,
	 * 			|				getBlockPosition().get(2) + dz)
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
		if (this.isMoving() && !this.getTarget().equals(this.getPosition()))
			return;
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1 || Math.abs(dz) > 1){
			throw new IllegalArgumentException("parameters must lie between -1 and 1!");
		}
		if (this.getState() == State.COMBAT)
			return;
		if(this.getState() != State.SPRINTING)
			this.setState(State.WALKING);
		try{
			this.setWalkSpeed(v_base);
			if (dz == -1)
				this.setWalkSpeed(v_base*1.2);
			if (dz == 1)
				this.setWalkSpeed(v_base*0.5);
		}
		catch (IllegalArgumentException exc){
			this.setWalkSpeed(-v_base);
			if (dz == -1)
				this.setWalkSpeed(-v_base*1.2);
			if (dz == 1)
				this.setWalkSpeed(-v_base*0.5);
		}
		
		ArrayList<Double> target = new ArrayList<Double>(this.getBlockPosition());
		target.set(0, target.get(0) + dx);
		target.set(1, target.get(1) + dy);
		target.set(2, target.get(2) + dz);
		this.setTarget(target);
		this.setV_Vector();
		this.setTheta(Math.atan2(v_vector.get(1),v_vector.get(0)));
	}
	/**
	 * Initiates the movement of a unit to a given block.
	 * @param x
	 * 			The x-coordinates of the given block.
	 * @param y
	 * 			The y-coordinates of the given block.
	 * @param z
	 * 			The z-coordinates of the given block.
	 * 
	 * @post	The final target is removed if the unit's position equals
	 * 			the position of the final target. The method then ends here
	 * 			|if(getPosition().equals(getFinTarget))
	 * 			|	then new.getFinTarget().isEmpty() && return
	 * @effect	Tries to create a new final target using the given coordinates.
	 * 			If it fails to create one, nothing happens.
	 * 			| if (setFinTarget(ArrayList<Double>(Arrays.asList(floor(x) + 0.5, floor(y) + 0.5, floor(z) + 0.5)))
	 * 			|		throws IllegalArgumentException)
	 * 			|	then return
	 * 			| else setFinTarget(ArrayList<Double>(Arrays.asList(floor(x) + 0.5, floor(y) + 0.5, floor(z) + 0.5)))
	 * @effect	Tries to move to an adjacent block that is closer to the final target than the current block position.
	 * 			If it fails, nothing happens.
	 * 			| blockx == this.getBlockPosition().get(0)
	 * 			| blocky == this.getBlockPosition().get(1)
	 * 			| blockz == this.getBlockPosition().get(2)
	 * 			| x'== Math.floor(x) + 0.5
	 * 			| y'== Math.floor(y) + 0.5
	 * 			| z'== Math.floor(z) + 0.5
	 * 			| if (blockx > x')
	 * 			|	then newx == -1
	 * 			| else if (blockx < x')
	 * 			|	then newx == 1
	 * 			| else 
	 * 			|	then newx == 0
	 * 			| if (blocky > y')
	 * 			|	then newy == -1
	 * 			| else if (blocky < y')
	 * 			|	then newy == 1
	 * 			| else 
	 * 			|	then newy == 0
	 * 			| if (blockz > z')
	 * 			|	then newz == -1
	 * 			| else if (blockz < z')
	 * 			|	then newz == 1
	 * 			| else 
	 * 			|	then newz == 0
	 * 			| if (moveToAdjacent(newx, newy, newz) throws IllegalArgumentException)
	 * 			|	then return
	 * 			| else moveToAdjacent(newx, newy, newz)
	 */
	public void moveTo(double x, double y, double z){
		if (this.getPosition().equals(this.getFinTarget())){
			this.finTarget.clear();
			return;
		}
		ArrayList<Double> newBlockPos = new ArrayList<Double>();
		newBlockPos.add(Math.floor(x)+0.5);
		newBlockPos.add(Math.floor(y)+0.5);
		newBlockPos.add(Math.floor(z)+0.5);
		if (!newBlockPos.equals(this.getFinTarget())){
			try{
				this.setFinTarget(newBlockPos);
			}
			catch(IllegalArgumentException exc){
				return;
			}
		}
		if (this.getBlockPosition().get(0) > newBlockPos.get(0))
			x = -1;
		else{
			if (this.getBlockPosition().get(0) < newBlockPos.get(0))
				x = 1;
			else x = 0;
		}
		if (this.getBlockPosition().get(1) > newBlockPos.get(1))
			y = -1;
		else{
			if (this.getBlockPosition().get(1) < newBlockPos.get(1))
				y = 1;
			else y = 0;
		}
		if (this.getBlockPosition().get(2) > newBlockPos.get(2))
			z = -1;
		else{
			if (this.getBlockPosition().get(2) < newBlockPos.get(2))
				z = 1;
			else z = 0;
		}
		try{
			this.moveToAdjacent((int)(x),(int) (y),(int) (z));
		}
		catch(IllegalArgumentException exc){
			System.out.println("??????");
			return;
		}
	}
	/**
	 * Updates the unit's velocity vector.
	 * @post 	If the unit hasn't reached its target, a velocity vector is created pointing in the direction of the target.
	 * 			| d == Math.sqrt(Math.pow(getPosition().get(0)-getTarget().get(0), 2)
	 *			|				+ Math.pow(getPosition().get(1)-getTarget().get(1), 2)
	 *			|				+ Math.pow(getPosition().get(2)-getTarget().get(2), 2))
	 * 			| if(!get.Target().equals(getPosition())
	 * 			|	then for each integer i == 0..2 : (
	 * 			|		new.getV_Vector().get(i) == getSpeed() / d * (getTarget().get(i) - getPosition().get(i)))
	 */
	private void setV_Vector(){
		if(!this.getTarget().equals(this.getPosition())){
			double distance = Math.sqrt(Math.pow(this.getPosition().get(0)-this.getTarget().get(0), 2)
								+ Math.pow(this.getPosition().get(1)-this.getTarget().get(1), 2)
								+ Math.pow(this.getPosition().get(2)-this.getTarget().get(2), 2));
			for (int i = 0; i<3; i++){
				this.v_vector.set(i, this.getSpeed()*(this.getTarget().get(i)-this.getPosition().get(i))/distance);
			}
		}
	}
	/**
	 * 
	 * Returns the velocity vector of this unit.
	 */
	@Basic
	public ArrayList<Double> getV_Vector(){
		return new ArrayList<Double>(this.v_vector);
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
	 * 			|	Math.atan2(defender.getPosition().get(1)-getPosition().get(1),
	 * 			|				defender.getPosition().get(0)-getPosition().get(0))
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
		double dy = defender.getPosition().get(1)-this.getPosition().get(1);
		double dx = defender.getPosition().get(0)-this.getPosition().get(0);
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
	 * @effect	The attack get's initiated if it hasn't been initiated yet.
	 * 			The method then ends here.
	 * 			| if(!isAttackInitiated())
	 *			|	then initiateAttack(defender) && return	
	 * @post	Nothing happens if the unit hasn't finished its attack yet.
	 * 			| if(this.getAttackCooldown() > 0)
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
	 * The unit attemps to dodge the attack.
	 * @param attacker
	 * 			The attacking unit.
	 * @effect	The unit will randomly select an adjacent block. It tries to 
	 * 			set the position of the unit in that block. If it fails, it
	 * 			randomly select an adjacent block until it succeeds to move the unit.
	 *			| while(new.getPosition.equals(old.getPosition()) : (
	 *			| 	for each integer i == 0..2 : (
	 *			|		new.getPosition.get(i) == old.Position.get(i) + randInt()
	 *			|		if(!(setPosition(new.getPosition()) throws IllegalArgumentException))
	 *			|			then setPosition(new.getPosition()
	 * @post	If the unit dodged the attack, the target of the unit is set to its position.
	 * 			| new.getTarget() == getPosition()
	 * @effect The unit will move to its final target if the unit hasn't reached it yet.
	 * 			| if (!getFinTarget().isEmpty())
	 *			|	then moveTo(getFinTarget().get(0), getFinTarget().get(1), getFinTarget().get(2))
	 */
	private void dodge(Unit attacker){
		int dx = 0;
		int dy = 0;
		int dz = 0;
		boolean checker = true;
		while (dx == 0 && dy == 0 && dz == 0 && checker){
			dx = this.randInt();
			dy = this.randInt();
			dz = this.randInt();
			try{
				this.setPosition(this.getPosition().get(0)+dx,this.getPosition().get(1)+dy , this.getPosition().get(2)+dz);
				checker  = false;
			}
			catch (IllegalArgumentException exc){
				checker = true;
			}
		}
		
		this.setTarget(this.getPosition());
		if (!this.getFinTarget().isEmpty())
		this.moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
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
	public void defend(Unit attacker){
		this.setState(State.IDLE);
		attacker.setState(State.IDLE);
		double damage = ((double)(attacker.getPrimStats().get("str")))/10;
		if(this.hasDodged(attacker)){
			this.dodge(attacker);
			return;
		}
		if(this.hasBlocked(attacker)){
			return;
		}
		if (!this.isValidHp(this.getHp() - damage)){
			this.setHp(0);
		}
		else{
			this.setHp(this.getHp() - damage);
		}
		
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
//	/**
//	 * Sets the maximum amount of hitpoints to the given amount.
//	 * @param maxHp
//	 * 			The given maximum allowed amount of hitpoints.
//	 * @pre  The maximum allowed amount of hitpoints needs to be (strength * weight / 50).
//	 * 			|maxHp == (getPrimStats().get("str") * getPrimStats().get("wgt"))/50
//	 * @post The maximum allowed amount of hitpoints is set to the given amount.
//	 * 			|new.getMaxHp() == maxHp
//	 * 
//	 */
//	public void setMaxHp(int maxHp){
//		assert(maxHp == (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50)));
//		this.maxHp = maxHp;
//	}

	/**
	 * 
	 * Returns the maximum allowed amount of stamina points of this unit.
	 * 		It is always equal to (strength*weight)/50.
	 */
	@Basic
	public int getMaxStam(){
		return (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50));
	}
//	/**
//	 * Sets the maximum amount of stamina points to the given amount.
//	 * @param maxStam
//	 * 			The given maximum allowed amount of stamina points.
//	 * @pre  The maximum allowed amount of stamina points needs to be (strength * weight / 50).
//	 * 			|maxStam == (getPrimStats().get("str") * getPrimStats().get("wgt"))/50
//	 * @post The maximum allowed amount of stamina points is set to the given amount.
//	 * 			|new.getMaxStam() == maxStam
//	 * 
//	 */
//	public void setMaxStam(int maxStam){
//		assert(maxStam == (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50)));
//		this.maxStam = maxStam;
//	}
	
	/**
	 * 
	 * Returns the current target of the unit.
	 * 		The target of a unit is a position at which the unit
	 * 		will directly move towards.
	 */
	@Basic
	public ArrayList<Double> getTarget(){
		return this.target;
	}
	/**
	 * Sets the target of the unit to the given target.
	 * @param target
	 * 			The given position of the new target.
	 * @post	The target of the unit is set to the given position.
	 * 			|new.getTarget() = target
	 */
	private void setTarget(ArrayList<Double> target){
		this.target = new ArrayList<Double>(target);
	}
	/**
	 * 
	 * Returns the final target of this unit.
	 * 		The final target of a unit is a position at which
	 * 		the unit will move towards by setting new targets.
	 */
	@Basic
	public ArrayList<Double> getFinTarget(){
		return this.finTarget;
	}
	/**
	 * Sets the final target of the unit to the given position.
	 * @param target
	 * 			The given position of the new final target.
	 * @post	The final target of the unit is set to the given position if its is valid.
	 * 				|if (isValidPosition(target))
	 * 				|	new.getFinTarget() = target
	 * @throws IllegalArgumentException
	 * 			Throws and exception if the target is an invalid position.
	 * 				|!isValidPosition(target)
	 */
	private void setFinTarget(ArrayList<Double> target) throws IllegalArgumentException{
		if (!this.isValidPosition(target))
			throw new IllegalArgumentException("Target out of bounds!");
		this.finTarget = new ArrayList<Double>(target);
	}
	
	/**
	 * 
	 * Returns the base speed of this unit.
	 */
	@Basic
	public double getBaseSpeed(){
		return this.v_base;
	}
	/**
	 * Sets the base speed of the unit to the given speed.
	 * @param speed
	 * 			The new speed of the unit.
	 * @post	The base speed of the unit is set to the given speed.
	 * 			|new.getBaseSpeed() == speed
	 * @throws	IllegalArgumentException
	 * 			|speed != 1.5*(getPrimStats().get("str")+getPrimStats().get("agl")))/(2*getPrimStats().get("wgt")
	 */
	private void setBaseSpeed(double speed)throws IllegalArgumentException{
		if (speed != 1.5*((double)(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))/(2*this.getPrimStats().get("wgt")))
			throw new IllegalArgumentException("Invalid basespeed!");
		this.v_base = speed;
	}
	
	/**
	 * 
	 * Returns a random integer between -1 and 1.
	 */
	private int randInt(){
		double random = Math.random();
		while (random == 0){
			random = Math.random();
		}
		return (int)(Math.ceil(random*3)-2);
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
		if (this.getState() == State.WALKING)
			return this.v;
		if (this.getState() == State.SPRINTING)
			return this.v * 2;
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
		IDLE, COMBAT, WALKING, WORKING, RESTING, SPRINTING		
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
		if (this.isMoving() || this.getState() == State.COMBAT)
			return;
		if (this.getState() != State.WORKING){
			this.setState(State.WORKING);
			this.setWorkTime(500/this.getPrimStats().get("str"));
		}
	}
	/**
	 * Returns whether or not the given unit is in range.
	 * @param unit
	 * 			The unit to be checked.
	 * @return True if the given unit in the same or adjacent block as this unit.
	 * 			|dx = getBlockPosition().get(0) - unit.getBlockPosition().get(0)
	 * 			|dy = getBlockPosition().get(1) - unit.getBlockPosition().get(1)
	 * 			|dz = getBlockPosition().get(2) - unit.getBlockPosition().get(2)
	 * 			|result == (|dx|<= 1) && (|dy| <=1) && (|dz| <= 1)
	 */
	public boolean inRange(Unit unit){
		boolean inRange = true;
		int idx = 0;
		while (idx <3 && inRange){
			if(Math.abs(unit.getBlockPosition().get(idx) - this.getBlockPosition().get(idx)) > 1.1){
				inRange = false;
			}
			idx += 1;
		}
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
		if ((this.isMoving() || this.getState() == State.COMBAT) && (this.restTime < 180) )
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
	public void sprint(){
		this.setState(State.SPRINTING);
	}
	/**
	 * The unit's state is set to IDLE.
	 */
	public void idle(){
		this.setState(State.IDLE);
	}
	private double theta;
	
	private Map<String, Integer> primStats = new HashMap<String, Integer>();
	
	private ArrayList<Double> pos;
	
	private ArrayList<Double> target;
	
	private ArrayList<Double> finTarget = new ArrayList<Double>();
	
	private String name;
		
	private double v_base;
	
	private double v;
	
	private ArrayList<Double> v_vector = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	
	private static final int NAMELENGTH_MIN = 2;
	
	private static final Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	private double attcooldown = 0;
	
	private double hp;
	
	private double stam;
	
	private State state;
	
	private double workTime = 0;
	
	private Set<Unit> combatants = new HashSet<Unit>();
	
	private double restTime = 0;
	
	private boolean Default = true;
	
	private double minRestTime = 0;
	
	private boolean attackInitiated = false;
	
	private static final Set<String> primStatSet = new HashSet<String>(Arrays.asList("str", "wgt", "agl", "tgh"));
	
	private final ArrayList<State> stateList;{
		this.stateList = new ArrayList<State>();
		this.stateList.add(State.WALKING);
		this.stateList.add(State.RESTING);
		this.stateList.add(State.WORKING);
		
	}
	
}
