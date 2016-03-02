package hillbillies.model;
import java.util.*;

import be.kuleuven.cs.som.*;

public class Unit {
	
	public Unit(String name,double x,double y,double z,int str,int wgt,int agl,int tgh) throws IllegalArgumentException{
		HashMap<String, Integer> firstStats = new HashMap<String, Integer>();
		firstStats.put("str", str);
		firstStats.put("wgt", wgt);
		firstStats.put("agl", agl);
		firstStats.put("tgh", tgh);
		for(String key : firstStats.keySet()){
			if (firstStats.get(key)<25 || firstStats.get(key)>100)
				throw new IllegalArgumentException();
		}
			
		this.setName(name);
		this.setPrimStats(firstStats);
		this.setMaxHp((int)(Math.ceil(200*((double)(str*wgt))/10000)));
		this.setMaxStam((int)(Math.ceil(200*((double)(str*wgt))/10000)));
		this.setHp((int)(Math.ceil(200*((double)(str*wgt))/10000)));	
		this.setStam((int)(Math.ceil(200*((double)(str*wgt))/10000)));
		this.setPosition(x, y, z);
		this.setTheta(Math.PI/2);
		this.setTarget(this.getPosition());
		this.setBaseVelocity(1.5*((double)(str+agl))/(2*wgt));
		this.setState(State.IDLE);
		
	}

	public void advanceTime(double dt){
		if (this.getAttackCooldown() < 1){
			this.setAttackCooldown(this.getAttackCooldown() + dt);
		}
		if (this.getState() == State.COMBAT){
			if (this.getCombatants().isEmpty()){
				this.setState(State.IDLE);
			}
			else{
				for (Unit unit : this.combatants){
					this.attack(unit);
				}
				return;
			}
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
			this.setState(State.RESTING);
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
					double newStam = this.getStam() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.2);
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
		}
		if (this.getTarget().equals(this.getPosition()) && this.getFinTarget().isEmpty() && this.getState() != State.IDLE){
			this.setState(State.IDLE);
			return;
		}
		
		if (this.getState() == State.WALKING || this.getState() == State.SPRINTING){
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
				this.setPosition(nextPos.get(0), nextPos.get(1), nextPos.get(2));
				moved = true;
			}
		}
	}
	
	
	public String getName(){
		return this.name;
	}
	
	
	public void setName(String newname) throws IllegalArgumentException{
		if (! isValidName(newname)){
			throw new IllegalArgumentException("Invalid name!");
		}
		else{
			this.name = newname;
		}
	}
	
	public boolean isValidName(String name){
		boolean checker = true; 
		for (int i=0; i<name.length();i++){
			if (!Character.isLetter(name.charAt(i)))
				if (! VALIDCHARS.contains(String.valueOf(name.charAt(i))))
					checker = false;
		}
		return Character.isUpperCase(name.charAt(0)) && name.length() >= NAMELENGTH_MIN && checker;
	}
	
	public void setPosition(double x, double y, double z)throws IllegalArgumentException{
		ArrayList<Double> pos= new ArrayList<Double>();
		pos.add(x);
		pos.add(y);
		pos.add(z);
		if (!isValidPosition(pos))
			throw new IllegalArgumentException("Out of bounds");
		this.pos = new ArrayList<Double>(pos);
	}
	
	public ArrayList<Double> getPosition(){
		return this.pos;
	}
	
	public boolean isValidPosition(ArrayList<Double> pos){
		boolean checker = true;
		for(int i=0; i<3; i++){
			if (pos.get(i)>50 || pos.get(i)<0)
				checker = false;
		}
		return checker;
	}
	
	public Map<String, Integer> getPrimStats(){
		return new HashMap<String, Integer>(this.primStats);
	}
	
	public void setPrimStats(Map<String, Integer> primStats) throws IllegalArgumentException{
		 if (!isValidPrimStats(primStats))
			 throw new IllegalArgumentException("Invalid stats!");
		 this.primStats = new HashMap<String, Integer>(primStats);
		 
	}
	public boolean isValidPrimStats(Map<String, Integer> primStats){
		Iterator<String> itr = primStats.keySet().iterator();
		boolean checker = true;
		while(itr.hasNext()){
			String key = itr.next();
			if (primStats.get(key) <= 0 || primStats.get(key)> 200)
				checker = false;
		}
		if (primStats.get("wgt")< Math.ceil(((double)primStats.get("str")+(double)primStats.get("agl")) / 2))
			checker = false;
		
		return checker;
	}
	
	public Double getTheta(){
		return this.theta;
	}
	
	public void setTheta (Double angle){
		this.theta = angle;
	}
	
	public ArrayList<Double> getBlockPosition(){
		ArrayList<Double> blockpos = new ArrayList<Double>(this.pos);
		for (int i = 0; i<3; i++){
			
			blockpos.set(i, Math.floor(this.pos.get(i)) + 0.5);
		}
		return blockpos;
	}
	
	public void moveToAdjacent(int dx, int dy, int dz){
		if (this.getState() == State.COMBAT)
			return;
		if(this.getState() != State.SPRINTING)
			this.setState(State.WALKING);
		this.v = this.v_base;
		if (dz == -1)
			this.v = this.v_base*1.2;
		if (dz == 1)
			this.v = this.v_base*0.5;
		
		ArrayList<Double> target = new ArrayList<Double>(this.getBlockPosition());
		target.set(0, target.get(0) + dx);
		target.set(1, target.get(1) + dy);
		target.set(2, target.get(2) + dz);
		this.setTarget(target);
		this.setV_Vector();
		this.setTheta(Math.atan2(v_vector.get(1),v_vector.get(0)));
	}
	
	public void moveTo(double x, double y, double z){
		if (this.getPosition().equals(this.finTarget)){
			this.finTarget.clear();
			return;
		}
		ArrayList<Double> newBlockPos = new ArrayList<Double>();
		newBlockPos.add(Math.floor(x)+0.5);
		newBlockPos.add(Math.floor(y)+0.5);
		newBlockPos.add(Math.floor(z)+0.5);
		if (!newBlockPos.equals(this.getFinTarget()))
			this.setFinTarget(newBlockPos);
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
		this.moveToAdjacent((int)(x),(int) (y),(int) (z));
	}
	
	public void setV_Vector(){
		double distance = 0;
		if(!this.getTarget().equals(this.getPosition()))
			distance = Math.sqrt(Math.pow(this.pos.get(0)-this.target.get(0), 2) + Math.pow(this.pos.get(1)-this.target.get(1), 2) + Math.pow(this.pos.get(2)-this.target.get(2), 2));
			for (int i = 0; i<3; i++){
				this.v_vector.set(i, this.getVelocity()*(this.target.get(i)-this.pos.get(i))/distance);
			}
	}
	public ArrayList<Double> getV_Vector(){
		return new ArrayList<Double>(this.v_vector);
	}
	
	public void attack(Unit defender){
		if (defender == this){
			return;
		}
		if (this.getAttackCooldown() < 1){
			return;
		}
		if (!this.inRange(defender)){
			this.removeCombatant(defender);
			defender.removeCombatant(this);
			return;
		}
		this.addCombatants(defender);
		defender.addCombatants(this);
		double dy = defender.getPosition().get(1)-this.getPosition().get(1);
		double dx = defender.getPosition().get(0)-this.getPosition().get(0);
		this.setTheta(Math.atan2(dy, dx));
		defender.setTheta(Math.atan2(-dy, -dx));
		if (this.getState() != State.COMBAT){
			this.setState(State.COMBAT);
		}
			
		if (defender.getState() != State.COMBAT){
			defender.setState(State.COMBAT);
		}
			
		defender.defend(this);
		this.setAttackCooldown(0);
	}

	public void defend(Unit attacker){
		double damage = ((double)(attacker.getPrimStats().get("str")))/10;
		double dodgeprob = ((double)(0.2*this.getPrimStats().get("agl")))/attacker.getPrimStats().get("agl");
		double defprob = ((double)(0.25*(this.getPrimStats().get("str")+this.getPrimStats().get("agl"))))/(attacker.getPrimStats().get("str")+attacker.getPrimStats().get("agl"));
		double dodgeroll = Math.random();
		double defroll = Math.random();
		int dx = 0;
		int dy = 0;
		int dz = 0;
		if (dodgeroll <= dodgeprob){
			while (dx == 0 && dy == 0 && dz == 0){
				dx = this.randInt();
				dy = this.randInt();
				dz = this.randInt();
			}
			this.setPosition(this.getPosition().get(0)+dx,this.getPosition().get(1)+dy , this.getPosition().get(2)+dz);
			this.setTarget(this.getPosition());
			if (! this.inRange(attacker)){
				this.removeCombatant(attacker);
				attacker.removeCombatant(this);
				if (!this.getFinTarget().isEmpty())
					this.moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
				return;
			}
		}
		if (defroll <= defprob){
			return;
		}
		if (!this.isValidHp(this.getHp() - damage)){
			this.setHp(0);
		}
		else{
			this.setHp(this.getHp() - damage);
		}
		
	}
	
	public void startDefault(){
		this.Default = true;
	}
	
	public void stopDefault(){
		this.Default = false;
	}
	
	public boolean DefaultOn(){
		return this.Default;
	}
	
	public double getHp(){
		return this.hp;
	}
	
	public void setHp(double hp){
		assert isValidHp(hp);
		this.hp = hp;
	}
	
	
	public double getStam(){
		return this.stam;
	}
	
	public void setStam(double stam){
		assert isValidStam(stam);
		this.stam = stam;
	}
	
	public boolean isValidHp(double hp){
		return (hp>=0 && hp<=this.maxHp);
	}
	
	public boolean isValidStam(double stam){
		return (stam>=0 && stam<=this.maxStam);
	}
	
	public ArrayList<Double> getTarget(){
		return this.target;
	}
	
	public void setTarget(ArrayList<Double> target){
		this.target = new ArrayList<Double>(target);
	}
	
	public ArrayList<Double> getFinTarget(){
		return this.finTarget;
	}
	
	public void setFinTarget(ArrayList<Double> target){
		this.finTarget = new ArrayList<Double>(target);
	}
	
	public double getBaseVelocity(){
		return this.v_base;
	}
	
	public void setBaseVelocity(double velocity){
		this.v_base = velocity;
	}
	
	
	//Random integer between -1 and 1
	private int randInt(){
		double random = Math.random();
		while (random == 0){
			random = Math.random();
		}
		return (int)(Math.ceil(random*3)-2);
	}
	
	public Set<Unit> getCombatants(){
		return this.combatants;
	}
	
	public void addCombatants(Unit unit) {
		this.combatants.add(unit);
	}
	
	public void removeCombatant(Unit unit){
		this.combatants.remove(unit);
	}
	
	public int getMaxHp(){
		return this.maxHp;
	}
	
	public void setMaxHp(int maxHp){
		this.maxHp = maxHp;
	}
	
	public int getMaxStam(){
		return this.maxStam;
	}
	
	public void setMaxStam(int maxStam){
		this.maxStam = maxStam;
	}
	
	public double getVelocity(){
		if (this.getState() == State.WALKING)
			return this.v;
		if (this.getState() == State.SPRINTING)
			return this.v * 2;
		return 0;
	}
	
	public enum State{
		IDLE, COMBAT, WALKING, WORKING, RESTING, SPRINTING		
	}
	
	public void setState(State state){
		this.state = state;
		this.setV_Vector();
	}
	
	public State getState(){
		return this.state;
	}
	
	public double getWorkTime() {
		return this.workTime;
	}
	
	public void setWorkTime(double newTime){
		this.workTime = newTime;
	}
	
	public void work(){
		if (this.getState() != State.WORKING){
			this.setState(State.WORKING);
			this.workTime = 500/this.getPrimStats().get("str");
		}
	}
	
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
	
	public double getRestTime(){
		return this.restTime;
	}
	
	public void setRestTime(double newTime){
		this.restTime = newTime;
	}
	
	public double getAttackCooldown(){
		return this.attcooldown;
	}
	
	public void setAttackCooldown(double newTime){
		this.attcooldown = newTime;
	}
	
	private double theta;
	
	private Map<String, Integer> primStats;
	
	private ArrayList<Double> pos;
	
	private ArrayList<Double> target;
	
	private ArrayList<Double> finTarget = new ArrayList<Double>();
	
	private String name;
		
	private double v_base;
	
	private double v;
	
	private ArrayList<Double> v_vector = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	
	private static final int NAMELENGTH_MIN = 2;
	
	private static final Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	private double attcooldown = 1;
	
	private double hp;
	
	private double stam;
	
	private int maxHp;
	
	private int maxStam;
	
	private State state;
	
	private double workTime = 0;
	
	private Set<Unit> combatants = new HashSet<Unit>();
	
	private double restTime = 0;
	
	private boolean Default = true;
	
	private final ArrayList<State> stateList;{
		this.stateList = new ArrayList<State>();
		this.stateList.add(State.WALKING);
		this.stateList.add(State.RESTING);
		this.stateList.add(State.WORKING);
	}
	
}
