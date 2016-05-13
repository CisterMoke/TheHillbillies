package hillbillies.model;

import java.util.*;

import hillbillies.model.expression.*;
import hillbillies.model.statement.*;

public class Task {
	public Task(String newname, Integer newpriority, Statement newactiviy, Vector newselected) {
		this.setPriority(newpriority);
		this.setName(newname);
		this.setActivity(newactiviy);
		this.setSelected(newselected);
	}
	
	public boolean isAssigned(){
		return this.getUnit() == null;
	}
	
	public Unit getUnit(){
		return this.unit;
	}
	
	protected void setUnit(Unit unit){
		//TODO worldcheck enzo
		this.unit = unit;
	}
	
	public Vector getSelected(){
		return this.selectedPosition;
	}
	
	public void setSelected(Vector pos){
		this.selectedPosition = pos;
	}
	
	public void executeTask(double dt){
		this.setCounter(dt);
		this.getActivity().setTask(this);
		this.getActivity().execute();
		if (this.getActivity().getCompleted())
			this.setCompleted(true);
	}
	
	
	public void setCounter(double dt){
		this.counter = (int) Math.floor(dt/0.001);
	}
	
	public void countDown(){
		this.counter = this.counter -1;
	}
	
	public int getCounter(){
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
	
	public int getPriority(){
		return this.priority;
	}
	
	public void setPriority(int priority){
		for(Scheduler scheduler : schedulers){
			scheduler.getTaskMap().remove(getPriority());
			scheduler.getTaskMap().put(priority, this);
		}
		this.priority = priority;
	}
	
	public Set<Scheduler> getSchedulers(){
		return new HashSet<Scheduler>(this.schedulers);
	}
	
	protected void addScheduler(Scheduler scheduler){
		this.schedulers.add(scheduler);
	}
	
	protected void removeScheduler(Scheduler scheduler){
		this.schedulers.remove(scheduler);
	}
	
	protected void setSchedulers(Set<Scheduler> newSet){
		this.schedulers = newSet;
	}
	
	public boolean canBeAssignedTo(Unit unit){
		return getSchedulers().contains(unit.getFaction().getScheduler()) && unit.getTask() == null
				&& this.getUnit() == null;
	}
	
	public Integer getLoopDepth() {
		return LoopDepth;
	}

	public void setLoopDepth(Integer loopDepth) {
		LoopDepth = loopDepth;
	}
	
	public void reset(){
		this.getActivity().reset();
	}
	
	public void interrupt(){
		this.reset();
		this.setPriority(getPriority());
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public void setName(String newname){
		this.name=newname;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setPriority(Integer newpriority){
		this.priority=newpriority;
	}
	
	public Integer getpriority(){
		return this.priority;
	}

	private Map<String, Expression<?>> variables;
	
	private Statement activity;
	
	private int counter;
	
	private Integer LoopDepth=0;
	
	private boolean completed;
	
	private String name;
	
	private Integer priority; 
	
	private Unit unit = null;
	
	private Set<Scheduler> schedulers = new HashSet<Scheduler>();
	
	private Vector selectedPosition;

	

}
