package hillbillies.model.statement;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends ComposedStatement{
	
	public Sequence(List<Statement> Seq){
		this.setStatementSequence(Seq);
	}

	@Override
	public void execute() {
		if (super.getCompleted())
			return;
		for (Statement stat : this.getStatementSequence()){
			stat.setTask(this.getTask());
			stat.execute();
			super.task.countDown();
			if (super.task.getCounter()<1)
				return;
		}
		this.setCompleted(true);
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
