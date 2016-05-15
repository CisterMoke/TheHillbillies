package hillbillies.model.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hillbillies.model.expression.Expression;

public class Sequence extends WrapStatement{
	
	public Sequence(List<Statement> Seq){
		this.setStatementSequence(Seq);
	}

	@Override
	public void execute() {
		System.out.println(this.getClass());
		if (super.getCompleted() || super.getTask().getCounter()<1)
			return;
		if(hasNullExpressions()){
			getTask().getActivity().setCompleted(true);
			return;
		}
		for (Statement stat : this.getStatementSequence()){
			if (super.task.getCounter()<1)
				return;			
			stat.execute();
			if(!stat.getCompleted())
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
		for (Statement stat : newlist){
			this.Substatements.add(stat);
		}
	}
	
	public void removeFromStatementSequence(Statement S){
		statementSequence.remove(S);
	}
	
	public Map<String, Expression<?>> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Expression<?>> variables) {
		this.variables = variables;
	}
	
	@Override
	public void addVariable(String name, Expression<?> value){
		this.variables.put(name, value);
	}
	
	@Override
	public Expression<?> readVariable(String name){
		if (this.getVariables().containsKey(name))
			return this.getVariables().get(name);
		else {
			if (this.getWrapStatement()!=null)
				return this.getWrapStatement().readVariable(name);
			else {
				System.out.println("Unassigned variable: " + name);
				return null;
			}
		}
	}

	private Map<String, Expression<?>> variables = new HashMap<String, Expression<?>>();
	
	private List<Statement> statementSequence;

}
