package hillbillies.model;

import java.util.ArrayList;
import java.util.List;


import hillbillies.model.expression.*;
import hillbillies.model.statement.*;
import hillbillies.part3.programs.ITaskFactory;
import hillbillies.part3.programs.SourceLocation;

public class TaskFactory implements ITaskFactory<Expression<?>, Statement, Task> {
	
	public TaskFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Task> createTasks(String name, int priority, Statement activity, List<int[]> selectedCubes) {
		List<Task> list = new ArrayList<Task>();
		if(selectedCubes.isEmpty())
			list.add(new Task(name, priority, activity, null));
		else{
			for (int[] selected : selectedCubes){
				Vector selectedVector = new Vector(selected[0], selected[1], selected[2]);
				Task task = new Task(name, priority, activity, selectedVector);
				System.out.println(task);
				list.add(task);
			}
		}
		return list;
	}

	@Override
	public Assignment createAssignment(String variableName, Expression<?> value, SourceLocation sourceLocation) {
		return new Assignment(variableName, value);
	}

	@Override
	public While createWhile(Expression<?> condition, Statement body, SourceLocation sourceLocation) {
		try{
			return new While((Expression<?>)condition, body);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public If createIf(Expression<?> condition, Statement ifBody, Statement elseBody,
			SourceLocation sourceLocation) {
		System.out.println("If gemaakt");
		try{
			return new If((Expression<?>)condition, ifBody, elseBody);		
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Break createBreak(SourceLocation sourceLocation) {
		return new Break();
	}

	@Override
	public Print createPrint(Expression<?> value, SourceLocation sourceLocation) {
		return new Print(value);
	}

	@Override
	public Sequence createSequence(List<Statement> statements, SourceLocation sourceLocation) {
		return new Sequence(statements);
	}

	@Override
	public MoveTo createMoveTo(Expression<?> position, SourceLocation sourceLocation) {
		System.out.println("MoveTo position: " + position);
		try{		
			return new MoveTo((Expression<?>)position);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Work createWork(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Work((Expression<?>)position);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Follow createFollow(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Follow((Expression<?>)unit);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Attack createAttack(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Attack((Expression<?>)unit);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Read createReadVariable(String variableName, SourceLocation sourceLocation) {
		return new Read(variableName);
	}

	@Override
	public Is_solid createIsSolid(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Is_solid((Expression<?>) position);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Is_passable createIsPassable(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Is_passable((Expression<?>) position);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Is_friend createIsFriend(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Is_friend((Expression<?>) unit);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Is_enemy createIsEnemy(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Is_enemy((Expression<?>) unit);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Is_alive createIsAlive(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Is_alive((Expression<?>) unit);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Carries_item createCarriesItem(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Carries_item((Expression<?>) unit); 
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public And createAnd(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		try{
			return new And((Expression<?>)left, (Expression<?>)right);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Or createOr(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		try{
			return new Or((Expression<?>)left, (Expression<?>)right);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Not createNot(Expression<?> expression, SourceLocation sourceLocation) {
		try{
			return new Not((Expression<?>) expression);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}
	
	@Override
	public Here createHerePosition(SourceLocation sourceLocation) {
		return new Here();
	}

	@Override
	public LogLiteral createLogPosition(SourceLocation sourceLocation) {
		return new LogLiteral();
	}

	@Override
	public BoulderLiteral createBoulderPosition(SourceLocation sourceLocation) {
		return new BoulderLiteral();
	}

	@Override
	public WorkshopLiteral createWorkshopPosition(SourceLocation sourceLocation) {
		return new WorkshopLiteral();
	}

	@Override
	public Selected createSelectedPosition(SourceLocation sourceLocation) {
		return new Selected();
	}

	@Override
	public Next_to createNextToPosition(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Next_to((Expression<?>) position);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public Position_of createPositionOf(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Position_of((Expression<?>) unit);
		}
		catch(ClassCastException exc){
			throw new ClassCastException("Incorrect type error at line: " + sourceLocation.getLine());
		}
	}

	@Override
	public PositionLiteral createLiteralPosition(int x, int y, int z, SourceLocation sourceLocation) {
		return new PositionLiteral(new Vector(x, y, z));
	}

	@Override
	public This createThis(SourceLocation sourceLocation) {
		return new This();
	}

	@Override
	public Friend createFriend(SourceLocation sourceLocation) {
		return new Friend();
	}

	@Override
	public Enemy createEnemy(SourceLocation sourceLocation) {
		return new Enemy();
	}

	@Override
	public Any createAny(SourceLocation sourceLocation) {
		return new Any();
	}

	@Override
	public BooleanLiteral createTrue(SourceLocation sourceLocation) {
		return new BooleanLiteral(true);
	}

	@Override
	public BooleanLiteral createFalse(SourceLocation sourceLocation) {
		return new BooleanLiteral(false);
	}

}
