package hillbillies.model.statement;

public class Break extends Statement{

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		super.setCompleted(true);
	}

}
