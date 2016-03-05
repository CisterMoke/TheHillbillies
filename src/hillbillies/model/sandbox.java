package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hillbillies.model.Unit.State;


public class sandbox {
	public static void main(String[] args){
		Unit billie = new Unit("Billie", 0, 0, 0, 80, 20, 100, 20);
		Unit bollie = new Unit("Billie", 0, 0, 0, 80, 20, 100, 20);
		billie.attack(bollie);
		System.out.println(billie.getState());
		System.out.println(bollie.getState());
		System.out.println(billie.getAttackCooldown());
		billie.advanceTime(0.2);
		System.out.println(billie.getState());
		System.out.println(bollie.getState());
		System.out.println(billie.getAttackCooldown());
		billie.advanceTime(0.2);
		billie.advanceTime(0.2);
		billie.advanceTime(0.2);
		billie.advanceTime(0.2);
		System.out.println(billie.getState());
		System.out.println(bollie.getState());
		System.out.println(billie.getAttackCooldown());
		
	}
}


