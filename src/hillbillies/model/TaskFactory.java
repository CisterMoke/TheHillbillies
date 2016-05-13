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
		for (int[] selected : selectedCubes){
			Vector selectedVector = new Vector(selected[0], selected[1], selected[2]);
			list.add(new Task(name, priority, activity, selectedVector));
		}
		return list;
	}

	@Override
	public Statement createAssignment(String variableName, Expression<?> value, SourceLocation sourceLocation) {
		return new Assignment(variableName, value);
	}

	@Override
	public Statement createWhile(Expression<?> condition, Statement body, SourceLocation sourceLocation) {
		try{
			return new While((Expression<Boolean>)condition, body);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Statement createIf(Expression<?> condition, Statement ifBody, Statement elseBody,
			SourceLocation sourceLocation) {
		try{
			return new If((Expression<Boolean>)condition, ifBody, elseBody);		
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Statement createBreak(SourceLocation sourceLocation) {
		return new Break();
	}

	@Override
	public Statement createPrint(Expression<?> value, SourceLocation sourceLocation) {
		return new Print(value);
	}

	@Override
	public Statement createSequence(List<Statement> statements, SourceLocation sourceLocation) {
		return new Sequence(statements);
	}

	@Override
	public Statement createMoveTo(Expression<?> position, SourceLocation sourceLocation) {
		try{		
			return new MoveTo((Expression<Vector>)position);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Statement createWork(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Work((Expression<Vector>)position);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Statement createFollow(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Follow((Expression<Unit>)unit);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Statement createAttack(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Attack((Expression<Unit>)unit);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<?> createReadVariable(String variableName, SourceLocation sourceLocation) {
		return new Read(variableName);
	}

	@Override
	public Expression<Boolean> createIsSolid(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Is_solid((Expression<Vector>) position);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createIsPassable(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Is_passable((Expression<Vector>) position);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createIsFriend(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Is_friend((Expression<Unit>) unit);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createIsEnemy(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Is_enemy((Expression<Unit>) unit);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createIsAlive(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Is_alive((Expression<Unit>) unit);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createCarriesItem(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Carries_item((Expression<Unit>) unit); 
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createAnd(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		try{
			return new And((Expression<Boolean>)left, (Expression<Boolean>)right);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createOr(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		try{
			return new Or((Expression<Boolean>)left, (Expression<Boolean>)right);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Boolean> createNot(Expression<?> expression, SourceLocation sourceLocation) {
		try{
			return new Not((Expression<Boolean>) expression);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}
	
	@Override
	public Expression<Vector> createHerePosition(SourceLocation sourceLocation) {
		return new Here();
	}

	@Override
	public Expression<Vector> createLogPosition(SourceLocation sourceLocation) {
		return new LogLiteral();
	}

	@Override
	public Expression<Vector> createBoulderPosition(SourceLocation sourceLocation) {
		return new BoulderLiteral();
	}

	@Override
	public Expression<Vector> createWorkshopPosition(SourceLocation sourceLocation) {
		return new WorkshopLiteral();
	}

	@Override
	public Expression<Vector> createSelectedPosition(SourceLocation sourceLocation) {
		return new Selected();
	}

	@Override
	public Expression<Vector> createNextToPosition(Expression<?> position, SourceLocation sourceLocation) {
		try{
			return new Next_to((Expression<Vector>) position);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Vector> createPositionOf(Expression<?> unit, SourceLocation sourceLocation) {
		try{
			return new Position_of((Expression<Unit>) unit);
		}
		catch(ClassCastException exc){
			System.out.println("Incorrect type error at line: " + sourceLocation.getLine());
		}
		return null;
	}

	@Override
	public Expression<Vector> createLiteralPosition(int x, int y, int z, SourceLocation sourceLocation) {
		return new PositionLiteral(new Vector(x, y, z));
	}

	@Override
	public Expression<Unit> createThis(SourceLocation sourceLocation) {
		return new This();
	}

	@Override
	public Expression<Unit> createFriend(SourceLocation sourceLocation) {
		return new Friend();
	}

	@Override
	public Expression<Unit> createEnemy(SourceLocation sourceLocation) {
		return new Enemy();
	}

	@Override
	public Expression<Unit> createAny(SourceLocation sourceLocation) {
		return new Any();
	}

	@Override
	public Expression<Boolean> createTrue(SourceLocation sourceLocation) {
		return new BooleanLiteral(true);
	}

	@Override
	public Expression<Boolean> createFalse(SourceLocation sourceLocation) {
		return new BooleanLiteral(false);
	}

}
