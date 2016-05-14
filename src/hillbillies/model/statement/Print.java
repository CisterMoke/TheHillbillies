package hillbillies.model.statement;

import hillbillies.model.expression.Expression;

public class Print extends BasicStatement{
	
	public Print(Expression<?> T){
		this.setText(T);
	}

	@Override
	public void executeSpecific() {
//		if (super.getCompleted() || super.getTask().getCounter()<1)
//			return;
//		super.task.countDown();
		System.out.println(this.getText().getValue());
//		super.setCompleted(true);
	}
	
	public Expression<?> getText() {
		return Text;
	}

	public void setText(Expression<?> text) {
		this.Expressions.add(text);
		Text = text;
	}

	private Expression<?> Text;

}
