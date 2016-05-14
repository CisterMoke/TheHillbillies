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
		return this.getUnit() != null;
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
		if (this.getActivity().getCompleted()){
			for (Scheduler s : this.getSchedulers()){
				s.removeTask(this);
			}
			this.setCompleted(true);
		}
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
	
//	public int getPriority(){
//		return this.priority;
//	}
	
//	public void setPriority(int priority){
//		for(Scheduler scheduler : schedulers){
////			scheduler.getTaskMap().remove(getPriority());
////			scheduler.getTaskMap().put(priority, this);
//		}
//		this.priority = priority;
//	}
	
	public Set<Scheduler> getSchedulers(){
		return new HashSet<Scheduler>(this.schedulers);
	}
	
	public boolean inSchedulerSet(Scheduler scheduler){
		return schedulers.contains(scheduler);
	}
	
	public boolean inSchedulerSet(Collection<Scheduler> c){
		return schedulers.containsAll(c);
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
		if (completed)
			System.out.println("completed");
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
	
	public Integer getPriority(){
		return this.priority;
	}

	
	private Statement activity;
	
	private int counter;
	
	private boolean completed;
	
	private String name;
	
	private Integer priority; 
	
	private Unit unit = null;
	
	private Set<Scheduler> schedulers = new HashSet<Scheduler>();
	
	private Vector selectedPosition;


	

}
