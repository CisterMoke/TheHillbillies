package sandbox;

import hillbillies.model.expression.*;

public class sandbox {
	public static void main(String[] args){
		Brackets kek = new Brackets(new BooleanLiteral(true));
		System.out.println(kek.getValue().getValue());
	}

}
