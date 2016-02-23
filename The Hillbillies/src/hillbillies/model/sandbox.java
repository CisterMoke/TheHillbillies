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
		String[] fruit = {"APPLE", "ORANGES", "GRAPES"};
		fruit.contains("APPLE");
		System.out.println();
	}
}
