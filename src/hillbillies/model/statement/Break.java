package hillbillies.model.statement;

public class Break extends Statement{

	@Override
	public void execute() {
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		this.getSuperStatement().stopLoop();
		super.setCompleted(true);
	}

}
