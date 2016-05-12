package hillbillies.model.statement;

import hillbillies.model.expression.Expression;

public class Print extends SimpleStatement{
	
	public Print(Expression<?> T){
		T.setTask(super.getTask());
		this.setText(T);
	}

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		System.out.println(this.getText());
		super.setCompleted(true);
	}
	
	public Expression<?> getText() {
		return Text;
	}

	public void setText(Expression<?> text) {
		Text = text;
	}

	private Expression<?> Text;

}
