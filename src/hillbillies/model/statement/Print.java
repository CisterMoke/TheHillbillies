package hillbillies.model.statement;

import be.kuleuven.cs.som.annotate.Basic;
import hillbillies.model.expression.Expression;

public class Print extends BasicStatement{
	
	public Print(Expression<?> T){
		this.setText(T);
	}

	@Override
	public void executeSpecific() {
		System.out.println(this.getText().getValue());
	}
	
	@Basic
	public Expression<?> getText() {
		return Text;
	}

	public void setText(Expression<?> text) {
		super.addExpression(text);
		Text = text;
	}

	private Expression<?> Text;

}
