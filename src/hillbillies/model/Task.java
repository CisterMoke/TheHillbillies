package hillbillies.model;

import java.util.*;

import hillbillies.model.statement.*;

/**
 * A class representing a Task.
 * @author Joost Croonen & Ruben Dedoncker
 */
public class Task {
	/**
	 * Initialize a Task with a given name, priority, activity Statement and selected position.
	 * @param 	newname
	 * 			The given name of the Task.
	 * @param 	newpriority
	 * 			The given priority of the Task.
	 * @param 	newactiviy
	 * 			The given activity Statemtent.
	 * @param 	newselected
	 * 			The given selected position.
	 * @post	The name of the Task is set to the given name.
	 * 			| getName() == newname
	 * @post	The priority of the Task is set to the given priority.
	 * 			| getPriority() == newpriority
	 * @post	The activity Statement of the Task is set to the given activity Statement.
	 * 			| getActivity() == newactivity
	 * @post	The selected position of the Task is set to the given position.
	 * 			| getSelected() == newselected
	 * @throws IllegalArgumentException
	 * 			An exception is thrown if the given name, priority or activity Statement
	 * 			is null
	 * 			| newname == null || newpriority == null || newactivity == null
	 * 
	 */
	public Task(String newname, Integer newpriority, Statement newactiviy, Vector newselected)
			throws IllegalArgumentException{
		if(newname == null || newpriority == null || newactiviy == null)
			throw new IllegalArgumentException("The given name, priority or activity can't be null!");
		this.setPriority(newpriority);
		this.setName(newname);
		this.setActivity(newactiviy);
		this.selectedPosition = newselected;
	}
	
	/**
	 * Returns a boolean stating whether or not this Task is assigned
	 * 	to a Unit.
	 * @return 	True if and only if the Unit associated with this Task is
	 * 			not null.
	 * 			| result == (getUnit() != null)
	 */
	public boolean isAssigned(){
		return this.getUnit() != null;
	}
	
	/**
	 * Return the Unit associated with this Task.
	 */
	public Unit getUnit(){
		return this.unit;
	}
	
	/**
	 * Set the Unit associated with this Task to the given Unit
	 * @param 	unit
	 * 			The given Unit to be associated with this Task.
	 * @post	If this Task is already assigned to another Unit, that
	 * 			Unit's Task will be removed.
	 * 			| if(getUnit() != null)
	 * 			|	then getUnit.getTask() == null
	 * @post	If the given Unit's Task will be set to this Task.
	 * 			| unit.getTask() == this
	 * @post	The Unit associated with this Task equals the given Unit.
	 * 			| getUnit() == unit
	 */
	protected void setUnit(Unit unit){
		if(this.unit != null)
			this.unit.setTask(null);
		if(unit != null)
			unit.setTask(this);
		this.unit = unit;
	}
	
	/**
	 * Return the selected position of this Task.
	 */
	public Vector getSelected(){
		return this.selectedPosition;
	}
	
	/**
	 * Execute this Task based on the given time interval.
	 * @param 	dt
	 * 			The given time interval.
	 * @effect	This Task's counter is set using the given interval.
	 * 			| setCounter(dt)
	 * @effect	This Task's activity gets initialized and executed
	 * 			| getActivity().initialise() && getActivity().execute()
	 * @effect	If this Task's acvtivity Stamentent is completed, then it
	 * 			will be removed from all its Schedulers and it will be
	 * 			terminated.
	 * 			| if(getActivity().getCompleted())
	 * 			|	then for each scheduler in old.getSchedulers() :(
	 * 			|		!scheduler.getTasks().contains(this)
	 * 			|		&& ! new.getSchedulers.contains(scheduler))
	 * 			|		&& terminate()
	 * 
	 */
	public void executeTask(double dt){
		System.out.println("exe: " + this + " " + this.getUnit());
		this.setCounter(dt);
		this.getActivity().initialise(this);
		this.getActivity().execute();
		if (this.getActivity().getCompleted()){
			for (Scheduler s : this.getSchedulers()){
				s.removeTask(this);
			}
			this.terminate();
		}
	}
	
	/**
	 * Return a boolean stating whether or not this Task is well formed.
	 * 
	 * @return 	True if and only of the activity Statement is well formed.
	 * 			| result == getActivity().isWellFormed()
	 * 
	 * @effect 	The activity Statement of this Task is initialized.
	 * 			| getActivity().initialise(this)
	 */
	public boolean isWellFormed(){
		this.getActivity().initialise(this);
		return this.getActivity().isWellFormed();
	}
	
	/**
	 * Set the counter of this Task based on the given time interval.
	 * @param 	dt
	 * 			The given time interval.
	 * @post	The counter is set tot the nearest integer bigger than
	 * 			dt/0.001.
	 * 			| getCounter() == Mah.ceil(dt/0.001)
	 */
	public void setCounter(double dt){
		this.counter = (int) Math.ceil(dt/0.001);
	}
	
	/**
	 * Reduce the number of the counter by one.
	 * @post	The counter is reduced by one.
	 * 			| new.getCounter() == old.getCounter() -1
	 */
	public void countDown(){
		this.counter = this.counter -1;
	}
	
	/**
	 * Return the counter of this Task.
	 */
	public int getCounter(){
		return this.counter;
	}
	
	/**
	 * Return the activity Statement of this Task.
	 */
	public Statement getActivity() {
		return activity;
	}
	
	/**
	 * Set the activity Statement of this Task to the given Statement.
	 * @param	activity
	 * 			The given Statement to be set as the activity
	 * 			Statement of this Task.
	 * @post	The given Statement is set as the activity Statement
	 * 			of this Task.
	 * 			| getActivity() == activity
	 */
	public void setActivity(Statement activity) {
		this.activity = activity;
	}
	
	/**
	 * Return a copy of the set of Schedulers in which this
	 * 	Task is scheduled.
	 */
	public Set<Scheduler> getSchedulers(){
		return new HashSet<Scheduler>(this.schedulers);
	}
	
	/**
	 * Return a boolean stating whether or not this Task is
	 * 	scheduled in the given Scheduler.
	 * @param 	scheduler
	 * 			The given Scheduler to be checked.
	 * @return 	True if and only if this Task's set of Schedulers
	 * 			contains the given Scheduler.
	 * 			| result == getSchedulers().contains(scheduler)
	 */
	public boolean inSchedulerSet(Scheduler scheduler){
		return schedulers.contains(scheduler);
	}
	
	/**
	 * Return a boolean stating whether or not this Task is
	 * 	scheduled in all of the elements of the given collection
	 * 	of Schedulers.
	 * @param 	c
	 * 			The given collection of Schedulers to be checked.
	 * @return 	True if and only if this Task's set of Schedulers
	 * 			contains the given collection of Schedulers.
	 * 			| result == getSchedulers().contains(c)
	 */
	public boolean inSchedulerSet(Collection<Scheduler> c){
		return schedulers.containsAll(c);
	}
	
	/**
	 * Add a given Scheduler to this Task's set of Schedulers.
	 * @param	scheduler
	 * 			The given Scheduler to be added.
	 * @post	This Task's set of Schedulers contains the
	 * 			given Scheduler.
	 * 			| getScheduler.contains(scheduler)
	 */
	protected void addScheduler(Scheduler scheduler){
		this.schedulers.add(scheduler);
	}
	
	/**
	 * Remove a given Scheduler from this Task's set of
	 * 	Schedulers.
	 * @param	scheduler
	 * 			The given Scheduler to be removed.
	 * @post	This Task's set of Schedulers doens't contain
	 * 			the given Scheduler.
	 * 			| !getSchedulers().contains(scheduler)
	 */
	protected void removeScheduler(Scheduler scheduler){
		this.schedulers.remove(scheduler);
	}
	
	/**
	 * Set this Task's set of Schedulers to the given set.
	 * @param	newSet
	 * 			The given set of Schedulers.
	 * @post	This Task's set of Schedulers is equal to
	 * 			the given set.
	 * 			| getSchedulers() == newSet
	 */
	protected void setSchedulers(Set<Scheduler> newSet){
		this.schedulers = newSet;
	}
	
	/**
	 * Return a boolean stating whether or not this Task can be
	 * 	assigned to a given Unit.
	 * @param	unit
	 * 			The given Unit to be checked.
	 * @return	True if this Task is scheduled in the Scheduler of the
	 * 			Unit's Faction, if the given Unit hasn't got any assigned Tasks
	 * 			and if this Task isn't already assigned to a Unit.
	 * 			| result == (getSchedulers().contains(unit.getFaction().getScheduler())
	 * 			|	&& unit.getTask() == null && getUnit(). == null)
	 */
	public boolean canBeAssignedTo(Unit unit){
		return schedulers.contains(unit.getFaction().getScheduler()) && unit.getTask() == null
				&& this.getUnit() == null;
	}
	
	/**
	 * Reset this Task.
	 * @effect	This Task's  activity Statement gets initialized with this
	 * 			Task and gets reset afterwards.
	 * 			| getActivity().initialise(this)
	 * 			|	&& getActivity().reset()
	 * @post	The Unit associated to this Task is removed.
	 * 			| getUnit() == null
	 */
	public void reset(){
		this.getActivity().initialise(this);
		this.getActivity().reset();
		this.setUnit(null);
	}
	
	/**
	 * Interrupt this Task.
	 * @effect	This Task gets reset
	 * 			| reset()
	 * @post	This Task's priority is subtracted by 10.
	 * 			| new.getPriority() == old.getPriority() - 10
	 */
	public void interrupt(){
		System.out.println("interupted");
		this.reset();
		this.setPriority(getPriority() - 10);
	}
	
	/**
	 * Terminate this Task.
	 * @post	The Unit associated to this Task is removed.
	 * 			| getUnit() == null
	 * @effect	This Task's activity Statement gets initialized with
	 * 			this Task and it gets terminated afterwards.
	 * 			| getActivity().initialise(this)
	 * 			|	&& getActivity(à.terminate
	 */
	public void terminate(){
		this.setUnit(null);
		this.getActivity().initialise(this);
		this.getActivity().terminate();
	}
	
	/**
	 * Set the name of this Task to a given name.
	 * @param 	newname
	 * 			The given name.
	 * @post	This Task's name is equal to the given name.
	 * 			| getName() == newname
	 */
	public void setName(String newname){
		this.name=newname;
	}
	
	/**
	 * Return the name of this Task.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Set the priority of this Task to the given priority.
	 * @param 	newpriority
	 * 			The given priority.
	 * @post	This Task's priority is equal to the given priority.
	 * 			| getPriority() == newpriority.
	 */
	public void setPriority(Integer newpriority){
		this.priority=newpriority;
	}
	
	/**
	 * Return the priority of this Task.
	 */
	public Integer getPriority(){
		return this.priority;
	}

	
	/**
	 * The activity Statement of this Task.
	 */
	private Statement activity;
	
	/**
	 * The counter of this Task. It represents the allowed amount
	 * 	of Statement executions of this Task for the given time interval.
	 */
	private int counter;
	
	/**
	 * The name of this Task.
	 */
	private String name;
	
	/**
	 * The priority of this Task.
	 */
	private Integer priority; 
	
	/**
	 * The Unit associated to this Task.
	 */
	private Unit unit = null;
	
	/**
	 * The set of Schedulers of this Task. It contains all
	 * 	the Schedulers in which this Task is assigned.
	 */
	private Set<Scheduler> schedulers = new HashSet<Scheduler>();
	
	/**
	 * The selected position of this Task.
	 */
	private Vector selectedPosition;


	

}
