package hillbillies.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class sandbox {
	public static void main(String[] args){
		String name = new String("Lklo");
		System.out.println(Character.isUpperCase(name.charAt(0)));
		Set<String> set = new HashSet<String>(Arrays.asList(" ", "\"", "\'"));
		System.out.println(set);
		HashSet<String> set2 = new HashSet<String>(set);
		set2.add("hallo");
		System.out.println(set);
		System.out.println(set2);
		Unit billie = new Unit ("Billie", 0, 0, 0, 0, 100, 100, 50, 20);
		System.out.println(billie.getName());
		System.out.println(billie.getPrimStats());
	}
}
