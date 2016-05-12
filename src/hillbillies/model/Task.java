package hillbillies.model;

import java.util.Map;

import hillbillies.model.expression.*;
import hillbillies.model.statement.*;

public class Task {
	public Task() {
	}
	
	private Unit unit = null;
	
	private Scheduler scheduler;
	
	private Vector selectedPosition;
	
	public boolean isAssigned(){
		return this.getUnit() == null;
	}
	
	public Unit getUnit(){
		return this.unit;
	}
	
	public void setUnit(Unit unit){
		//TODO worldcheck enzo
		this.unit = unit;
	}
	
	public Vector getSelected(){
		return this.selectedPosition;
	}
	
	public void setSelected(Vector pos){
		this.selectedPosition = pos;
	}
	
	public void executeTask(){
		this.getActivity().setTask(this);
		this.getActivity().execute();
	}
	
	
	public void setCounter(Integer dt){
		this.counter = (int) Math.floor(dt/0.001);
	}
	
	public void countDown(){
		this.counter = this.counter -1;
	}
	
	public Integer getCounter(){
		return this.counter;
	}
	
	public Statement getActivity() {
		return activity;
	}

	public void setActivity(Statement activity) {
		this.activity = activity;
	}

	public Map<String, Expression<?>> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Expression<?>> variables) {
		this.variables = variables;
	}
	
	public void addVariable(String name, Expression<?> value){
		this.variables.put(name, value);
	}
	
	public Expression<?> readVariable(String name){
		return this.getVariables().get(name);
	}

	private Map<String, Expression<?>> variables;
	
	private Statement activity;
	
	private Integer counter;

}
