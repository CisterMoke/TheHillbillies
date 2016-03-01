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
		this.setTheta(0.0);
		this.setTarget(this.getPosition());
		this.setBaseVelocity(1.5*((double)(str+agl))/(2*wgt));
		this.setState(State.IDLE);
		
	}

	public void advanceTime(double dt){
		ArrayList<Double> nextPos = new ArrayList<Double>(this.getPosition());
		if (this.getState() == State.COMBAT){
			for (int i = 0; i<this.getCombatants().size(); i++){
				this.attack(this.getCombatants().get(i));
			}
			return;
		}
		if (this.getState() == State.RESTING){
			if (this.getHp()<this.getMaxHp())
				this.setHp(this.getHp() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.2));
			else{
				if (this.getStam()<this.getMaxStam())
					this.setStam(this.getStam() + (double)(this.getPrimStats().get("tgh"))*dt/(200*0.2));
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
		
		//if (!this.getTarget().equals(this.getPosition())){
		if (this.getState() == State.WALKING || this.getState() == State.SPRINTING){
			if (this.getState() == State.SPRINTING){
				if (this.getStam()  <= 0)
					this.setState(State.WALKING);
				else{
					this.setStam(this.getStam() - dt/0.1);
					
				}
			}
			for (int i = 0; i<3; i++){
				nextPos.set(i, nextPos.get(i) + this.v_vector.get(i)*dt);
			}
			boolean moved = false;
			int idx = 0;
			while (!moved && idx<3){
				if(Math.signum(nextPos.get(idx)-this.getTarget().get(idx)) == Math.signum(this.v_vector.get(idx)) && (Math.signum(this.v_vector.get(idx)) != 0)){
					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
					if (!this.getFinTarget().isEmpty())
						moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
					moved = true;
				}
				idx += 1;
			}
			
//				if (Math.signum(nextPos.get(0)-this.getTarget().get(0)) == Math.signum(this.v_vector.get(0)) && (Math.signum(this.v_vector.get(0)) != 0)){
//					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
//					//moved = true;
//					//moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
//				}
//				if (Math.signum(nextPos.get(1)-this.getTarget().get(1)) == Math.signum(this.v_vector.get(1)) && (Math.signum(this.v_vector.get(1)) != 0)){
//					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
//					//moved = true;
//					//moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
//				}
//				if (Math.signum(nextPos.get(2)-this.getTarget().get(2)) == Math.signum(this.v_vector.get(2)) && (Math.signum(this.v_vector.get(2)) != 0)){
//					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
//					//moved = true;
//					//moveTo(this.getFinTarget().get(0), this.getFinTarget().get(1), this.getFinTarget().get(2));
//				}
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
//		if (this.getState() == State.SPRINTING)
//			this.v = this.v*2;		
		
		ArrayList<Double> target = new ArrayList<Double>(this.getBlockPosition());
		target.set(0, target.get(0) + dx);
		target.set(1, target.get(1) + dy);
		target.set(2, target.get(2) + dz);
		this.setTarget(target);
		this.setV_Vector();
		this.setTheta(Math.atan2(v_vector.get(1),v_vector.get(0)));
	}
	
	public void moveTo(double x, double y, double z){
		if (this.target.equals(this.finTarget)){
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
	
	public boolean isSprinting(){
		return this.sprinting;
	}
	
	public void toggleSpringting(){
		this.sprinting = !this.sprinting;
	}
	
	public boolean isInCombat(){
		return this.combat;
	}
	
	public void toggleCombat(){
		this.combat = !this.combat;
	}
	
	public void attack(Unit defender){
		if (defender == this){
			return;
		}
		if (this.attcooldown < 1){
			return;
		}
		if (!defender.getBlockPosition().equals(this.getBlockPosition())){
			return;
		}
		this.addCombatants(defender);
		defender.addCombatants(this);
		double dy = defender.getPosition().get(1)-this.getPosition().get(1);
		double dx = defender.getPosition().get(0)-this.getPosition().get(0);
		this.setTheta(Math.atan2(dy, dx));
		defender.setTheta(Math.atan2(-dy, -dx));
		if (! this.isInCombat()){
			this.toggleCombat();
		}
			
		if (! defender.isInCombat()){
			defender.toggleCombat();
		}
			
		defender.defend(this);
		this.attcooldown = 0;
	}

	public void defend(Unit attacker){
		double damage = ((double)(attacker.getPrimStats().get("str")))/10;
		double dodgeprob = ((double)(0.20*this.getPrimStats().get("agl")))/attacker.getPrimStats().get("agl");
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
			this.toggleCombat();
			attacker.toggleCombat();
			return;
		}
		if (defroll <= defprob){
			return;
		}
		this.setHp(this.getHp() - (int)(damage));
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
		return (hp>0 && hp<=this.maxHp);
	}
	
	public boolean isValidStam(double stam){
		return (stam>0 && stam<=this.maxStam);
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
	public ArrayList<Unit> getCombatants(){
		return this.combatants;
	}
	
	public void addCombatants(Unit unit) {
		this.combatants.add(unit);
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
		IDLE, COMBAT,WALKING, WORKING, RESTING, SPRINTING		
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
	
	private boolean sprinting = false;
	
	private boolean combat = false;
	
	private Map<String, Integer> primStats;
	
	private ArrayList<Double> pos;
	
	private ArrayList<Double> target;
	
	private ArrayList<Double> finTarget = new ArrayList<Double>();
	
	private String name;
	
	private Double theta;
	
	private double v_base;
	
	private double v;
	
	private ArrayList<Double> v_vector = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	
	private static final int NAMELENGTH_MIN = 2;
	
	private static final Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	private int attcooldown = 1;
	
	private double hp;
	
	private double stam;
	
	private int maxHp;
	
	private int maxStam;
	
	private State state;
	
	private double workTime = 0;
	
	private ArrayList<Unit> combatants = new ArrayList<Unit>();
	
	
	
}
