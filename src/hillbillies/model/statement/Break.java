package hillbillies.model.statement;

public class Break extends BasicStatement{
	
	public Break(){
	}

	@Override
	public void executeSpecific() {
//		if (super.getCompleted() || super.getTask().getCounter()<1)
//			return;
//		super.task.countDown();
		this.getWrapStatement().stopLoop();
//		super.setCompleted(true);
	}

}
