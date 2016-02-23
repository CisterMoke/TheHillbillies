package hillbillies.model;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.*;

public class Unit {

	
	public String getName(){
		return this.name;
	}
	
	
	public void setName(String newname) throws NameException{
		if (! isValidName(newname))
			throw NameException;
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
	
	public void setStrength(int newstrength){
		if (newstrength > 0 && newstrength <=200)
			this.strength=newstrength; 
	}
	
	private int strength;
	
	private int agility;
	
	private int toughness;
	
	private int weight;
	
	private double x_pos;
	
	private double y_pos;
	
	private double z_pos;
	
	private String name;
	
	private static int NAMELENGTH_MIN = 2;
	
	private static Set<String> VALIDCHARS = new HashSet<String>(Arrays.asList(" ", "\"", "\'", "m"));
	
	
}
