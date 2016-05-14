package hillbillies.model.statement;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends WrapStatement{
	
	public Sequence(List<Statement> Seq){
		this.setStatementSequence(Seq);
	}

	@Override
	public void execute() {
		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		this.initSupers();
		boolean prevCompleted = true;
		for (Statement stat : this.getStatementSequence()){
//			stat.setTask(this.getTask());
			if (super.task.getCounter()<1 || prevCompleted == false)
				return;			
//			stat.setSuperStatment(this);
			stat.execute();
			prevCompleted = stat.getCompleted();
		}
		this.setCompleted(prevCompleted);
	}
	
//	@Override 
//	public void reset(){
//		for (Statement stat : this.getStatementSequence())
//			stat.reset();
//		this.setCompleted(false);
//	}
	
	public List<Statement> getStatementSequence(){
		List<Statement> list = new ArrayList<Statement>();
		for (Statement element : statementSequence){
			list.add(element); 
		}
		return list;
	}
	
	public void setStatementSequence(List<Statement> newlist){
		this.statementSequence = newlist;
		for (Statement stat : newlist){
			this.Substatements.add(stat);
		}
	}
	
	public void removeFromStatementSequence(Statement S){
		statementSequence.remove(S);
	}
	
	private List<Statement> statementSequence;

}
