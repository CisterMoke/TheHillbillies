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
		Position pos = new Position(x, y, z);
		this.pos=pos;
		this.setTheta(theta);
	}

	
	public String getName(){
		return this.name;
	}
	
	
	public void setName(String newname) throws IllegalArgumentException{
		if (! isValidName(newname))
			throw new IllegalArgumentException("Invalid name!");
		else
			this.name = newname;
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
	
	public void moveToAdjecent(double dx, double dy, double dz){
		this.setTheta(Math.atan2(dx,dy));
		this.pos.setPosition(this.pos.getPosition(1), this.pos.getPosition(2), this.pos.getPosition(3));
	}
	
	private Map<String, Integer> primStats;
	
	private Position target;
	
	private Position pos;
	
	private String name;
	
	private Double theta;
	
	private static int NAMELENGTH_MIN = 2;
	
	private static Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	
}
