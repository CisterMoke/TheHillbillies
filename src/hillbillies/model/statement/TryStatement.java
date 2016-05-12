package hillbillies.model.statement;

import hillbillies.model.expression.BooleanLiteral;
import hillbillies.model.expression.Expression;

public class TryStatement {
	
	public static <O> void main(String[] args){
		
		BooleanLiteral cond = new BooleanLiteral(false);
		
		Expression<O> text = (Expression<O>) new BooleanLiteral(true);	
		
		Expression<O> text2 = (Expression<O>) new BooleanLiteral(false);	
				
		Print<O> ifbody = new Print<O>(text);	
		
		Print<O> elsebody = new Print<O>(text2);
				
				
		If w = new If(cond, ifbody, elsebody);	
		
		w.execute();
		
		System.out.println(text);
		System.out.println(text2);
		
		
	}
}
