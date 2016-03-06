
package hillbillies.model;
import java.util.*;

import be.kuleuven.cs.som.annotate.*;
//import be.kuleuven.cs.som.taglet.*;
/**
 * 
 * @author Ruben
 *
 */
public class Unit {
	
	public Unit(String name,double x,double y,double z,int str,int wgt,int agl,int tgh) throws IllegalArgumentException{
		HashMap<String, Integer> firstStats = new HashMap<String, Integer>();
		firstStats.put("str", str);
		firstStats.put("wgt", wgt);
		firstStats.put("agl", agl);
		firstStats.put("tgh", tgh);
			
		this.setName(name);
		this.setPrimStats(firstStats);
		this.setMaxHp((int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50)));
		this.setMaxStam((int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50)));
		this.setHp(this.getMaxHp());	
		this.setStam(this.getMaxStam());
		this.setPosition(x, y, z);
		this.setTheta(Math.PI/2);
		this.setTarget(this.getPosition());
		this.setBaseSpeed(1.5*((double)(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))/(2*this.getPrimStats().get("wgt")));
		this.setState(State.IDLE);
		
	}

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
			this.setState(this.stateList.get(idx));
			if (this.getState() == State.WALKING){
				this.moveTo(Math.random()*50, Math.random()*50, Math.random()*50);
			}
			if (this.getState()== State.WORKING){
				this.setWorkTime(500/this.getPrimStats().get("str"));
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
			this.setRestTime(0);
			if (this.getHp()<this.getMaxHp()){
				double newHp = this.getHp() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.2);
				if(!this.isValidHp(newHp)){
					newHp = this.maxHp;
				}
				this.setHp(newHp);
			}
			else{
				if (this.getStam()<this.getMaxStam()){
					double newStam = this.getStam() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.1);
					if(!this.isValidHp(newStam)){
						newStam = this.maxStam;	
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
					this.setPosition(this.getPosition().get(0), this.getPosition().get(1), this.getPosition().get(2));
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
	 * 
	 * @param newname
	 * @post Changes the name of the unit to a valid name.
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
	 * 
	 * @param name
	 * @return Returns whether the given name is a valid name. A valid name only contains
	 * 			letters and valid characters, has the defined minimal length and
	 * 			starts with a capital letter.
	 * 			| checker == true
	 * 			| for (character in name){
	 * 			| 	if(!Character.isLetter(character)){
	 * 			|		if(! getValidChars().contains(character)){
	 * 			|			checker == false
	 * 			|		}
	 * 			|	}
	 * 			|}
	 * 			| result == (checker) && (name.length() >= getMinNameLength()) && (name.isUpperCase(name.charAt(0)))
	 */
	private boolean isValidName(String name){
		boolean checker = true;
		for (int i=0; i<name.length();i++){
			if (!Character.isLetter(name.charAt(i)))
				if (! this.getValidChars().contains(String.valueOf(name.charAt(i))))
					checker = false;
		}
		return Character.isUpperCase(name.charAt(0)) && name.length() >= this.getMinNameLength() && checker;
	}
	/**
	 * 
	 * Returns a set containing the valid characters, excluding letters.
	 */
	@Basic
	private Set<String> getValidChars(){
		return new HashSet<String>(VALIDCHARS);
	}
	/**
	 * 
	 * Returns the minimal length for a valid name.
	 */
	@Basic
	private int getMinNameLength(){
		return NAMELENGTH_MIN;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
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
	 * 
	 * @param pos
	 * @return Returns whether the given position is within the boundaries.
	 * 			| result == true
	 * 			| for (element in pos){
	 * 			| 	if (element > 50 && element < 0){
	 * 			|		result == false
	 * 			|	}
	 * 			|}
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
	 * 
	 * @param primStats
	 * @post Returns a map containing the primary stats laying between 1 and 200 inclusively
	 * 			and the weight being larger than (strength + agility)/2. When a unit is created,
	 * 			the primary stats lay between 25 and 100 inclusively.
	 * 
	 * 			| for (key in primStats.keySet()){
	 * 			|	if(this.getPrimStats().isEmpty()){
	 *			|		if(primStats.get(key) < 25){
	 *			|			primStats.put(key, 25)
	 *			|		}
	 *			|		if(primStats.get(key) > 100){
	 *			|			primStats.put(key, 100)
	 *			|		}
	 *			|	}	
	 * 			|	if(primStats.get(key) < 1){
	 * 			|		primStats.put(key, 1)
	 * 			|	}
	 * 			|	if (primStats.get(key) > 200){
	 *			|		primStats.put(key, 200)
	 *			|	}
	 *			| }
	 *			|	if (primStats.get("wgt") < (primStats.get("str") + primStats.get("agl")) / 2){
	 *			|		primStats.put("wgt", (primStats.get("str") + primStats.get("agl")) / 2)
	 *			|	}
	 *			| new.getPrimStats() = primStats
	 */
	public void setPrimStats(Map<String, Integer> primStats){
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
	 * @param primStats
	 * @return Returns whether the given primary stats lay between 1 and 200 inclusively.
	 * 			The weight of the unit also needs to be lower than the sum of the strength
	 * 			and the agility, divided by two.
	 * 			| result == true
	 * 			| for (key in primStats){
	 * 			|	if((primStats.get(key) <= 0) || (primStats.get(key) > 200)){
	 * 			|		result == false
	 * 			|	}
	 * 			| }
	 * 			| if (primStats.get("wgt") < primStats.get("str") + primStats.get("agl")/2){
	 * 			| 	result == false
	 * 			|}
	 */
//	public boolean isValidPrimStats(Map<String, Integer> primStats){
//		Iterator<String> itr = primStats.keySet().iterator();
//		boolean checker = true;
//		while(itr.hasNext()){
//			String key = itr.next();
//			if (primStats.get(key) <= 0 || primStats.get(key)> 200)
//				checker = false;
//		}
//		if (primStats.get("wgt")< Math.ceil(((double)primStats.get("str")+(double)primStats.get("agl")) / 2))
//			checker = false;
//		
//		return checker;
//	}
	
	/**
	 * 
	 * Returns the orientation of this unit in the x-y plane in radians.
	 */
	@Basic
	public Double getTheta(){
		return this.theta;
	}
	/**
	 * 
	 * @param angle
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
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 * 
	 * @post	Nothing happens if the unit is moving
	 * 			| if (this.isMoving())
	 * 			|	return
	 * @effect  A new target is set to the center of the given adjacent block.
	 * 			| this.setTarget(this.getBlockPosition() + <dx, dy, dz>)
	 * @effect	A new velocity vector is created, pointing in the direction of the target.
	 * 			| this.setV_Vector()
	 * @effect	The orientation of the unit is set in the direction of the velocity vector.
	 * 			| this.setTheta(arctan(this.getV_Vector.get(1)/this.getV_Vector(0)))
	 * @effect	When going down, the walking speed is set to 1.2*BaseSpeed.
	 * 			When going up, the walking speed is set to 0.5*BaseSpeed.
	 * 			Otherwise the walking speed is set to BaseSpeed.
	 * 			|this.setWalkSpeed(this.getBaseSpeed())
	 * 			| if(dz == -1)
	 * 			|	this.setWalkSpeed(1.2*this.getBaseSpeed())
	 * 			| if(dz == 1)
	 * 			|	this.setWalkSpeed(0.5*this.getBaseSpeed())
	 * 			|
	 * @post	If the unit is in combat, nothing happens
	 * 			| if (this.getState() == State.COMBAT)
	 * 			|	return
	 * @effect	If the unit isn't sprinting, the unit is set to walk.
	 * 			|if(this.getState() != State.SPRINTING)
	 *			| 	this.setState(State.WALKING)
	 * 
	 * @throws IllegalArgumentException
	 * 			Throw an exception when the given block is not adjacent to the block the unit is standing in.
	 * 			| if ((|dx| > 1) || (|dy| > 1 )|| (|dz| > 1))
	 *			|	throw new IllegalArgumentException
	 */
	public void moveToAdjacent(int dx, int dy, int dz)throws IllegalArgumentException{
		if (this.isMoving() && !this.getTarget().equals(this.getPosition()))
			return;
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1 || Math.abs(dz) > 1){
			throw new IllegalArgumentException("parameters must lay between -1 and 1!");
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
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * 
	 * @effect	Tries to create a new FinalTarget using the given coordinates.
	 * 			If it fails to create one, nothing happens.
	 * 			| try{
	 * 			|	this.setFinTarget(<floor(x) + 0.5, floor(y) + 0.5, floor(z) + 0.5>)
	 *			| }
	 * 			| catch(IllegalArgumentException exc){
	 * 			|	return
	 *			| }
	 * @effect	Tries to move to an adjacent block that is closer to FinalTarget than the current block position,
	 * 			until it has reached FinalTarget. If it fails, nothing happens.
	 * 			|blockx == this.getBlockPosition().get(0)
	 * 			|blocky == this.getBlockPosition().get(1)
	 * 			|blockz == this.getBlockPosition().get(2)
	 * 			|x'== floor(x) + 0.5
	 * 			|y'== floor(y) + 0.5
	 * 			|z'== floor(z) + 0.5
	 * 			|if (blockx > x')
	 * 			|	newx == -1
	 * 			|else if (blockx < x')
	 * 			|	newx == 1
	 * 			|else 
	 * 			|	newx == 0
	 * 			|if (blocky > y')
	 * 			|	newy == -1
	 * 			|else if (blocky < y')
	 * 			|	newy == 1
	 * 			|else 
	 * 			|	newy == 0
	 * 			|if (blockz > z')
	 * 			|	newz == -1
	 * 			|else if (blockz < z')
	 * 			|	newz == 1
	 * 			|else 
	 * 			|	newz == 0
	 * 			|moveToAdjacent(newx, newy, newz)
	 */
	public void moveTo(double x, double y, double z){
		if (this.getPosition().equals(this.finTarget)){
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
	 * @post 	If the unit hasn't reached its target, a velocity vector is created pointing in the direction of the target.
	 * 			|
	 */
	private void setV_Vector(){
		double distance = 0;
		if(!this.getTarget().equals(this.getPosition()))
			distance = Math.sqrt(Math.pow(this.pos.get(0)-this.target.get(0), 2) + Math.pow(this.pos.get(1)-this.target.get(1), 2) + Math.pow(this.pos.get(2)-this.target.get(2), 2));
			for (int i = 0; i<3; i++){
				this.v_vector.set(i, this.getSpeed()*(this.target.get(i)-this.pos.get(i))/distance);
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
	 * 	
	 * @param defender
	 * @post	Nothing happens if the unit hasn't rested long enough,
	 * 			is moving or if the defender is out of range.
	 * 			| if(this.getMinRestTime() > 0 || !this.inRange(defender) || this.isMoving())
	 *			|	return
	 * @effect	Tries to add the defender to the unit's set of combatants.
	 * 			If an IllegalArgumentException is caught, nothing happens
	 * 			| try{
	 *			| this.addCombatant(defender)
	 *			| }
	 *			| catch(IllegalArgumentException exc){
	 *			|	return
	 *			| }
	 * @effect	The orientation of this unit and the defender are set so that they
	 * 			face each other.
	 * 			| this.new.getTheta() ==
	 * 			|	Math.atan2(defender.getPosition().get(1)-this.getPosition().get(1),
	 * 			|				defender.getPosition().get(0)-this.getPosition().get(0)) &&
	 * 			| 	defender.new.getTheta() == this.getTheta() + Math.PI
	 * @post	The attack cooldown is set to 1 second.
	 * 			| new.getAttackCooldown == 1
	 * @post	The states of both units are set to COMBAT.
	 * 			| this.new.getState() == State.COMBAT &&
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
		//defender.setTheta(Math.atan2(-dy, -dx));
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
	public boolean isAttackInitiated(){
		return this.attackInitiated;
	}
	/**
	 * @post	The value of isAttackInitiated() is set to its opposite.
	 * 			| new.isAttackInititiated() == !this.isAttackInitiated()
	 */
	public void toggleAttackInitiated(){
		this.attackInitiated = !this.isAttackInitiated();
	}
	/**
	 * 
	 * @param defender
	 * @effect	The attack get's initiated if it hasn't been initiated yet.
	 * 			|if(!this.isAttackInitiated())
	 *			|	this.initiateAttack(defender) &&
	 *			|		return
	 * @post	Nothing happens if the unit hasn't finished its attack yet.
	 * 			| if(this.getAttackCooldown() > 0)
	 * 			|	return
	 * @effect	If the defender is out of range, the defender is removed from
	 * 			the set of combatants, this unit's state is set to IDLE
	 * 			and the value of isAttackInitiated is set to false.
	 * 			| if (!this.inRange(defender))
	 *			|	this.removeCombatant(defender) &&
	 *			|		this.setState(State.IDLE) &&
	 *			|		new.isAttackInitiated() == false &&
	 *			|		return
	 * @effect	If the unit performs an attack, the defender will try to defend the attack.	
	 * 			| defender.defend(this)
	 * 			
	 * 
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
	 * 
	 * @param attacker
	 * @return Returns the probability of dodging an attack. The probability equals
	 * 			0.2 times the ratio of the units agility to the attacker's agility.
	 * 			|result == 0.2*this.getPrimStats().get("agl")/attacker.getPrimStats().get("agl")
	 */
	public double getDodgeProb(Unit attacker){
		return ((double)(0.2*this.getPrimStats().get("agl")))/attacker.getPrimStats().get("agl");
	}
	/**
	 * 
	 * @param attacker
	 * @return True if the randomly generated number between 0 and 1 is less than
	 *			the probability to dodge.
	 *			|result == (Math.random() <= this.getDodgeProb(attacker))
	 */
	public boolean hasDodged(Unit attacker){
		return (Math.random() <= this.getDodgeProb(attacker));
	}
	/**
	 * 
	 * @param attacker
	 * @effect	If the unit successfully dodged the attack, it will randomly select an adjacent block. 
	 * 			It try to set the position of the unit in that block. If it fails, it will
	 * 			randomly select an adjacent block until it succeeds to move the unit.
	 * 			| dx == 0 &&
	 *			| dy == 0 &&
	 *			| dz == 0 &&
	 *			| checker == true
	 *			| while (dx == 0 && dy == 0 && dz == 0 && checker){
	 *			|	dx == this.randInt() &&
	 *			|	dy == this.randInt() &&
	 *			|	dz == this.randInt() &&
	 *			|	try{
	 *			|		this.setPosition(this.getPosition() + <dx, dy, dz>) &&
	 *			|		checker == false
	 *			|	} &&
	 *			|	catch (IllegalArgumentException exc){
	 *			|		checker == true
	 *			|	}
	 *			| }
	 * @effect	If the unit is out of range after it has dodged the attack,
	 * 			the unit is removed from the set of combatants of the attacker
	 * 			and vice versa.
	 * 			| if (! this.inRange(attacker))
	 *			| 	this.removeCombatant(attacker) &&
	 *			| 	attacker.removeCombatant(this)
	 * @post	If the unit dodged the attack, the target of the unit is set to its position.
	 * 			| new.getTarget() == this.getPosition()
	 * @effect The unit will move to its final target if the unit hasn't reached it yet.
	 * 			| if (!this.getFinTarget().isEmpty())
	 *			|	this.moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2))
	 */
	public void dodge(Unit attacker){
		if (hasDodged(attacker)){
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
	}
	
	/**
	 * 
	 * @param attacker
	 * @return Returns the probability of blocking an attack. The probability equals
	 * 			0.25 times the ratio of the sum the units agility and strength
	 * 			to the sum of the attacker's agility and strength.
	 * 			|result == 0.25*(this.getPrimStats().get("agl")+this.getPrimStats("str"))
	 * 			|			/ (attacker.getPrimStats().get("agl") + this.getPrimStats("str"))
	 */
	public double getBlockProb(Unit attacker){
		return ((double)(0.25*(this.getPrimStats().get("agl")+ this.getPrimStats().get("str"))))
				/(attacker.getPrimStats().get("agl") + attacker.getPrimStats().get("str"));
	}
	
	/**
	 * 
	 * @param attacker
	 * @return True if the randomly generated number between 0 and 1 is less than
	 *			the probability to block.
	 *			|result == (Math.random() <= this.getBlockProb(attacker))
	 */
	public boolean hasBlocked(Unit attacker){
		return(Math.random() <= this.getBlockProb(attacker));
	}
	

	/**
	 * 
	 * @param attacker
	 * @post	If the attacker isn't in the set of combatants of this unit, nothing happens
	 * 			| if(!this.getCombatants().contains(attacker))
	 * 			|	return
	 * @effect	The unit attempts to dodge the attack. If it succeeds, it won't take any damage.
	 * 			| if(this.hasDodged(attacker)
	 * 			|	this.dodge(attacker)
	 * 			|	return
	 * 			| new.getHp() == this.getHp()
	 * @post	If the unit failed to dodge the attack, it will attempt to block the attack.
	 * 			If it succeeds, it won't take any damage.
	 *			| if(this.hasBlocked(attacker))
	 *			|	return
	 *			| new.getHp() == this.getHp()
	 * @effect	If the unit neither dodged nor blocked the attack it will lose an amount of hitpoints
	 * 			equal to the attacker's strength divided by ten. If this amount is bigger than the current
	 * 			amount of hitpoints of the unit, its hitpoints will be set to zero.
	 * 			| damage == attacker.getPrimStats().get("str")/10
	 * 			| if (!this.isValidHp(this.getHp() - damage))
	 *			|	this.setHp(0)
	 *			| else
	 *			|	this.setHp(this.getHp() - damage)
	 */
	public void defend(Unit attacker){
		this.setState(State.IDLE);
		attacker.setState(State.IDLE);
		double damage = ((double)(attacker.getPrimStats().get("str")))/10;
		//INSERT HERE
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
	
//	double dodgeprob = ((double)(0.2*this.getPrimStats().get("agl")))/attacker.getPrimStats().get("agl");
//	double defprob = ((double)(0.25*(this.getPrimStats().get("str")+this.getPrimStats().get("agl"))))/(attacker.getPrimStats().get("str")+attacker.getPrimStats().get("agl"));
//	double dodgeroll = Math.random();
//	double defroll = Math.random();
//	int dx = 0;
//	int dy = 0;
//	int dz = 0;
//	if (dodgeroll <= dodgeprob){
//		boolean checker = true;
//		while (dx == 0 && dy == 0 && dz == 0 && checker){
//			dx = this.randInt();
//			dy = this.randInt();
//			dz = this.randInt();
//			try{
//				this.setPosition(this.getPosition().get(0)+dx,this.getPosition().get(1)+dy , this.getPosition().get(2)+dz);
//				checker  = false;
//			}
//			catch (IllegalArgumentException exc){
//				checker = true;
//			}
//		}
//		
//		this.setTarget(this.getPosition());
//		if (! this.inRange(attacker)){
//			this.removeCombatant(attacker);
//			attacker.removeCombatant(this);
//			if (!this.getFinTarget().isEmpty())
//				this.moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
//			return;
//		}
//	}
//	if (defroll <= defprob){
//		return;
//	}
	/**
	 * 
	 * Returns true the unit is set to the default behaviour.
	 */
	@Basic
	public boolean DefaultOn(){
		return this.Default;
	}
	
	/**
	 * @post 	The unit is set to default behaviour.
	 * 			| new.DefaultOn() == true
	 */
	public void startDefault(){
		this.Default = true;
	}
	/**
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
	 * 
	 * @param hp
	 * 
	 * 
	 * @pre  The given amount of hitpoints is a valid amount
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
	 * 
	 * @param stam
	 * 
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
	 * 
	 * @param hp
	 * @return Returns whether the given amount of hitpoints lays between 0 and
	 * 			the maximum amount of hitpoints inclusively.
	 * 			|result == (hp>=0) && (hp<=this.getMaxHp())
	 */
	@Basic
	public boolean isValidHp(double hp){
		return (hp>=0 && hp<=this.maxHp);
	}
	/**
	 * 
	 * @param stam
	 * @return Returns whether the given amount of stamina points lays between 0 and
	 * 			the maximum amount of stamina points inclusively.
	 * 			|result == (stam>=0) && (stam<=this.getMaxStam())
	 */
	@Basic
	private boolean isValidStam(double stam){
		return (stam>=0 && stam<=this.maxStam);
	}
	/**
	 * 
	 * Returns the maximum allowed amount of hitpoints of this unit.
	 */
	@Basic
	public int getMaxHp(){
		return this.maxHp;
	}
	/**
	 * 
	 * @param maxHp
	 * 
	 * @pre  The maximum allowed amount of hitpoints needs to be (strength * weight / 50).
	 * 			|maxHp == (this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50
	 * @post The maximum allowed amount of hitpoints is set to the given amount.
	 * 			|new.getMaxHp() == maxHp
	 * 
	 */
	private void setMaxHp(int maxHp){
		assert(maxHp == (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50)));
		this.maxHp = maxHp;
	}

	/**
	 * 
	 * Returns the maximum allowed amount of stamina points of this unit.
	 */
	@Basic
	public int getMaxStam(){
		return this.maxStam;
	}
	/**
	 * 
	 * @param maxStam
	 * 
	 * @pre  The maximum allowed amount of stamina points needs to be (strength * weight / 50).
	 * 			|maxStam == (this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50
	 * @post The maximum allowed amount of stamina points is set to the given amount.
	 * 			|new.getMaxStam() == maxStam
	 * 
	 */
	private void setMaxStam(int maxStam){
		assert(maxStam == (int)(Math.ceil((double)(this.getPrimStats().get("str") * this.getPrimStats().get("wgt"))/50)));
		this.maxStam = maxStam;
	}
	
	/**
	 * 
	 * Returns the current target.
	 */
	@Basic
	public ArrayList<Double> getTarget(){
		return this.target;
	}
	/**
	 * 
	 * @param target
	 * @post	Sets the target to the given target
	 * 			|new.getTarget() = target
	 */
	private void setTarget(ArrayList<Double> target){
		this.target = new ArrayList<Double>(target);
	}
	/**
	 * 
	 * Returns the FinalTarget of this unit.
	 */
	@Basic
	public ArrayList<Double> getFinTarget(){
		return this.finTarget;
	}
	/**
	 * 
	 * @param target
	 * @post	Sets FinalTarget to the given target if the target is valid.
	 * 				|if (this.isValidPosition(target))
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
	 * Returns the basespeed of this unit.
	 */
	@Basic
	public double getBaseSpeed(){
		return this.v_base;
	}
	/**
	 * 
	 * @param velocity
	 * @post	Sets BaseSpeed to the given velocity 
	 * 			|new.getBaseSpeed() == velocity
	 * @throws	IllegalArgumentException
	 * 			|velocity != 1.5*(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))/(2*this.getPrimStats().get("wgt")
	 */
	private void setBaseSpeed(double velocity)throws IllegalArgumentException{
		if (velocity != 1.5*((double)(this.getPrimStats().get("str")+this.getPrimStats().get("agl")))/(2*this.getPrimStats().get("wgt")))
			throw new IllegalArgumentException("Invalid basespeed!");
		this.v_base = velocity;
	}
	
	
	//Random integer between -1 and 1
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
	 * 
	 * @param unit
	 * @post	Unit is added to the set of combatants.
	 * 			|new.getCombatants().contains(unit)
	 * @throws	IllegalArgumentException
	 * 			|unit == this
	 */
	private void addCombatant(Unit unit) throws IllegalArgumentException{
		if (unit == this){
			throw new IllegalArgumentException("You are not allowed to fight with yourself!");
		}
		this.combatants.add(unit);
	}
	
	/**
	 * 
	 * @param unit
	 * @post	Unit is removed from the set of combatants.
	 * 			|!new.getCombatants().contains(unit)
	 */
	private void removeCombatant(Unit unit){
		this.combatants.remove(unit);
	}
	/**
	 * 
	 * @return	Returns the walking speed of this unit if its state is walking.
	 * 			Returns the walking speed of this unit times two if its state is sprinting.
	 * 			Returns 0 otherwise.
	 * 			|if (this.getState() == State.WALKING)
	 *			|	result == this.getWalkSpeed();
	 *			|if (this.getState() == State.SPRINTING)
	 *			|	result == this.getWalkingSpeed() * 2
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
	 * 
	 * @param v
	 * 
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
	 * 
	 * @param state
	 * @post 	The state of the unit is set to the given state
	 * 			|new.getState() == state
	 * @post	The state of the unit remains unchanged if it hasn't rested long enough and is not in combat.
	 * 			|if(this.getMinRestTime() > 0 && state != State.COMBAT)
	 *			|	return
	 *@effect	The velocity vector is updated
	 *			|this.setV_Vector()
	 */
	public void setState(State state){
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
	 * 
	 * @return True if the unit is either walking or sprinting
	 * 			| (this.getState() == State.WALKING) || (this.getState() == State.SPRINTING)
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
	 * 
	 * @param newTime
	 * @post  	The remaining amount of work time needed is set to the given time.
	 * 			|new.getWorkTime() == newTime
	 */
	private void setWorkTime(double newTime){
		this.workTime = newTime;
	}
	/**
	 * @post	Nothing happens if the unit is moving
	 * 			| if (this.isMoving())
	 * 			|	return
	 * @effect 	If the unit isn't working, it is set to work and the work time is set to 500/strength.
	 * 			|if(this.getState() != State.WORKING)
	 * 			|	this.setState(State.WORKING)
	 *			|	this.setWorkTime(500/this.getPrimStats().get("str"));
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
	 * 
	 * @param unit
	 * @return Returns whether the given unit is in the same or adjacent block as this unit.
	 * 			|dx = this.getBlockPosition().get(0) - unit.getBlockPosition().get(0)
	 * 			|dy = this.getBlockPosition().get(1) - unit.getBlockPosition().get(1)
	 * 			|dz = this.getBlockPosition().get(2) - unit.getBlockPosition().get(2)
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
	 * 
	 * @param newTime
	 * @post 	The RestTime is set to the given time
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
	 * 
	 * @param newTime
	 * @post	The cooldown is set to the given time
	 * 			|new.getAttackCooldown() == newTime
	 */
	private void setAttackCooldown(double newTime){
		this.attcooldown = newTime;
	}
	/**
	 * 
	 * Returns the time the unit needs to rest until it can perform any other actions
	 */
	@Basic
	public double getMinRestTime(){
		return this.minRestTime;
	}
	/**
	 * 
	 * @param newtime
	 * @post	The minimum resting time is set to the given time.
	 * 			|new.getMinRestTime() == newTime
	 */
	private void setMinRestTime(double newtime){
		this.minRestTime = newtime;
	}
	/**
	 * @post	Nothing happens if the unit is moving
	 * 			| if (this.isMoving())
	 * 			| 	return
	 * @effect If the unit isn't resting, its state is set to resting
	 * 			and its minimum resting time is set to 40/toughness.
	 * 			| if (this.getState() != State.RESTING)
	 * 			|	this.setState(State.RESTING)
	 * 			|	this.setMinRestTime(40.0/this.getPrimStats().get("tgh"))
	 */
	public void rest(){
		if (this.isMoving() || this.getState() == State.COMBAT)
			return;
		if(this.getState() != State.RESTING){
			this.setState(State.RESTING);
			this.setMinRestTime(40.0/this.getPrimStats().get("tgh"));
		return;
		}
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
	
	private int maxHp;
	
	private int maxStam;
	
	private State state;
	
	private double workTime = 0;
	
	private Set<Unit> combatants = new HashSet<Unit>();
	
	private double restTime = 0;
	
	private boolean Default = false;
	
	private double minRestTime = 0;
	
	private boolean attackInitiated = false;
	
	private final ArrayList<State> stateList;{
		this.stateList = new ArrayList<State>();
		this.stateList.add(State.WALKING);
		this.stateList.add(State.RESTING);
		this.stateList.add(State.WORKING);
		
	}
	
}
