package hillbillies.model.statement;

public abstract class BasicStatement extends Statement{

	public abstract void executeSpecific();
	
	@Override	
	public void execute(){
		if(hasNullExpressions()){
			getTask().getActivity().setCompleted(true);
			return;
		}
//		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		super.task.countDown();
		this.executeSpecific();
		this.setCompleted(true);
	}
}
