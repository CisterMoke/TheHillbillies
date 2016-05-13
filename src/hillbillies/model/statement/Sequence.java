package hillbillies.model.statement;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Statement{
	
	public Sequence(List<Statement> Seq){
		this.setStatementSequence(Seq);
	}

	@Override
	public void execute() {
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		boolean prevCompleted = true;
		for (Statement stat : this.getStatementSequence()){
			if (super.task.getCounter()<1 || prevCompleted == false)
				return;			
			stat.setTask(this.getTask());
			stat.setSuperStatment(this);
			stat.execute();
			prevCompleted = stat.getCompleted();
		}
		this.setCompleted(true);
	}
	
	@Override 
	public void reset(){
		for (Statement stat : this.getStatementSequence())
			stat.reset();
		this.setCompleted(false);
	}
	
	public List<Statement> getStatementSequence(){
		List<Statement> list = new ArrayList<Statement>();
		for (Statement element : statementSequence){
			list.add(element); 
		}
		return list;
	}
	
	public void setStatementSequence(List<Statement> newlist){
		this.statementSequence = newlist;
	}
	
	public void removeFromStatementSequence(Statement S){
		statementSequence.remove(S);
	}
	
	private List<Statement> statementSequence;

}
