package hillbillies.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.som.*;

public class Unit {
	
	public Unit(String name,double x,double y,double z,double theta,int str,int wgt,int agl,int tgh){ //throws IllegalArgumentException{
		HashMap<String, Integer> firstStats = new HashMap<String, Integer>();
		firstStats.put("str", str);
		firstStats.put("wgt", wgt);
		firstStats.put("agl", agl);
		firstStats.put("tgh", tgh);
			
		this.setName(name);
		this.setPrimStats(firstStats);
		this.setHp((int)(Math.ceil(200*((double)(str*wgt))/10000)));	
		this.setStam((int)(Math.ceil(200*((double)(str*wgt))/10000)));
		this.setPosition(x, y, z);
		this.setTheta(theta);
		this.setTarget(this.getPosition());
		this.setBaseVelocity(1.5*((double)(str+agl))/(2*wgt));
		
	}

	public void advanceTime(double dt){
		ArrayList<Double> nextPos = new ArrayList<Double>(this.getPosition());
		if (this.isInCombat()){
			for (int i = 0; i<this.getCombatants().size(); i++){
				this.attack(this.getCombatants().get(i));
			}
		}
		else{
			if (this.target != this.pos){
				for (int i = 0; i<3; i++){
					nextPos.set(i, nextPos.get(i) + this.v_vector.get(i)*dt);
				}
				if (Math.signum(nextPos.get(0)-this.getTarget().get(0)) == Math.signum(this.v_vector.get(0))){
					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
				}
				if (Math.signum(nextPos.get(1)-this.getTarget().get(1)) == Math.signum(this.v_vector.get(1))){
					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
				}
				if (Math.signum(nextPos.get(2)-this.getTarget().get(2)) == Math.signum(this.v_vector.get(2))){
					this.setPosition(this.target.get(0), this.target.get(1), this.target.get(2));
				}
				else{
					this.setPosition(nextPos.get(0), nextPos.get(1), nextPos.get(2));
				}
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
		this.v = this.v_base;
		if (dz == 1)
			this.v = this.v_base*1.2;
		if (dz == -1)
			this.v = this.v_base*0.5;
		if (isSprinting())
			this.v = this.v*2;		
		
		ArrayList<Double> target = new ArrayList<Double>(this.getBlockPosition());
		target.set(0, target.get(0) + dx);
		target.set(1, target.get(1) + dy);
		target.set(2, target.get(2) + dz);
		this.setTarget(target);
		this.setV_Vector();
		this.setTheta(Math.atan2(v_vector.get(1),v_vector.get(0)));
	}
	
	public void setV_Vector(){
		double distance = 0;
		if(this.getTarget() != this.getPosition())
			distance = Math.sqrt(Math.pow(this.pos.get(0)-this.target.get(0), 2) + Math.pow(this.pos.get(1)-this.target.get(1), 2) + Math.pow(this.pos.get(2)-this.target.get(2), 2));
			for (int i = 0; i<3; i++){
				this.v_vector.set(i, this.v*(this.target.get(i)-this.pos.get(i))/distance);
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
			System.out.println("test1");
			return;
		}
		if (this.attcooldown < 1){
			System.out.println("test2");
			return;
		}
		if (defender.getBlockPosition() != this.getBlockPosition()){
			System.out.println("test3");
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
	
	public int getHp(){
		return this.hp;
	}
	
	public void setHp(int hp){
		assert isValidSecondaryStat(hp);
		this.hp = hp;
	}
	
	
	public int getStam(){
		return this.stam;
	}
	
	public void setStam(int stam){
		assert isValidSecondaryStat(stam);
		this.stam = stam;
	}
	
	public boolean isValidSecondaryStat(int stat){
		return (stat>0 && stat<=(int)(Math.ceil(200*((double)(this.getPrimStats().get("str")*this.getPrimStats().get("wgt")))/10000)));
	}
	
	public ArrayList<Double> getTarget(){
		return this.target;
	}
	
	public void setTarget(ArrayList<Double> target){
		this.target = new ArrayList<Double>(target);
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
	
	private boolean sprinting = false;
	
	private boolean combat = false;
	
	private Map<String, Integer> primStats;
	
	private ArrayList<Double> pos;
	
	private ArrayList<Double> target;
	
	private String name;
	
	private Double theta;
	
	private double v_base;
	
	private double v;
	
	private ArrayList<Double> v_vector = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	
	private static final int NAMELENGTH_MIN = 2;
	
	private static final Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	private int attcooldown = 1;
	
	private int hp;
	
	private int stam;
	
	private ArrayList<Unit> combatants;
	
}
