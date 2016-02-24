package hillbillies.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import be.kuleuven.cs.som.*;

public class Unit {
	
	public Unit(String name,double x,double y,double z,double theta,int str,int wgt,int agl,int tgh){ //throws IllegalArgumentException{
		HashMap<String, Integer> firstStats = new HashMap<String, Integer>();
		firstStats.put("str", str);
		firstStats.put("wgt", wgt);
		firstStats.put("agl", agl);
		firstStats.put("tgh", tgh);
		
		this.setName(name);
		this.setStats(firstStats);
		this.setPos(x, y, z);
		this.setAngle(theta);
		
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
	
	public void setStats(Map<String, Integer> stats) throws IllegalArgumentException{
		 if (!isValidStats(this.stats))
			 throw new IllegalArgumentException("Invalid stats!");
		 this.stats = new HashMap<String, Integer>(stats);
		 
	}
	public boolean isValidStats(Map<String, Integer> stats){
		Iterator<String> itr = stats.keySet().iterator();
		boolean checker = true;
		while(itr.hasNext()){
			String key = itr.next();
			if (stats.get(key) <= 0 || stats.get(key)> 200)
				checker = false;
		}
		if (stats.get("wgt")< Math.ceil(((double)stats.get("str")+(double)stats.get("agl")) / 2))
			checker = false;
		
		return checker;
	}
	
	private Map<String, Integer> stats;
	
	private ArrayList<Double> pos;
	
	private String name;
	
	private static int NAMELENGTH_MIN = 2;
	
	private static Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
	
	
}
