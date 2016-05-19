package tests.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import hillbillies.model.Faction;
import hillbillies.model.Task;
import hillbillies.model.TaskFactory;
import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.model.statement.Statement;
import hillbillies.part3.programs.TaskParser;

public class TaskFactoryTest {

	private TaskFactory factory = new TaskFactory();
	private Unit testUnit;
	private TaskParser<Expression<?>, Statement, Task> parser = new TaskParser<>(factory);
	private Task task;
	private List<int[]> selected;
	private String text;


	@Before
	public void setup(){
		testUnit = new Unit("Billie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit);
		testUnit.stopDefault();
		selected = new ArrayList<int[]>();
	}
	
	@Test
	public void testInvalidTypeMoveTo() {
		text = "name : \"test\" priority : 500 activities : moveTo this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeMoveTo() {
		text = "name : \"test\" priority : 500 activities : moveTo here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeWork() {
		text = "name : \"test\" priority : 500 activities : work this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeWork() {
		text = "name : \"test\" priority : 500 activities : work here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeAttack() {
		text = "name : \"test\" priority : 500 activities : attack here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeAttack() {
		text = "name : \"test\" priority : 500 activities : attack this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeFollow() {
		text = "name : \"test\" priority : 500 activities : follow here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeFollow() {
		text = "name : \"test\" priority : 500 activities : follow this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeWhile() {
		text = "name : \"test\" priority : 500 activities : while here do print true; done ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeWhile() {
		text = "name : \"test\" priority : 500 activities : while true do print true; done ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeIf() {
		text = "name : \"test\" priority : 500 activities : if here then print true; fi ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeIf() {
		text = "name : \"test\" priority : 500 activities : if true then print true; fi ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeAnd() {
		text = "name : \"test\" priority : 500 activities : print (this && there); ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeAnd() {
		text = "name : \"test\" priority : 500 activities : print (true && is_alive this); ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeOr() {
		text = "name : \"test\" priority : 500 activities : print (this || there); ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeOr() {
		text = "name : \"test\" priority : 500 activities : print (true || is_alive this); ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeNot() {
		text = "name : \"test\" priority : 500 activities : print !this ; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeNot() {
		text = "name : \"test\" priority : 500 activities : print !true ; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypePosition_of() {
		text = "name : \"test\" priority : 500 activities : print position_of true; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypePosition_of() {
		text = "name : \"test\" priority : 500 activities : print position_of this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeNext_to() {
		text = "name : \"test\" priority : 500 activities : print next_to true; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeNext_to() {
		text = "name : \"test\" priority : 500 activities : print next_to here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeIs_alive() {
		text = "name : \"test\" priority : 500 activities : print is_alive here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeIs_alive() {
		text = "name : \"test\" priority : 500 activities : print is_alive this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeIs_enemy() {
		text = "name : \"test\" priority : 500 activities : print is_enemy here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeIs_enemy() {
		text = "name : \"test\" priority : 500 activities : print is_enemy this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeIs_friend() {
		text = "name : \"test\" priority : 500 activities : print is_friend here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeIs_friend() {
		text = "name : \"test\" priority : 500 activities : print is_friend this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeIs_passable() {
		text = "name : \"test\" priority : 500 activities : print is_passable this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeIs_passable() {
		text = "name : \"test\" priority : 500 activities : print is_passable here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeIs_solid() {
		text = "name : \"test\" priority : 500 activities : print is_solid this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeIs_solid() {
		text = "name : \"test\" priority : 500 activities : print is_solid here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testInvalidTypeCarries_item() {
		text = "name : \"test\" priority : 500 activities : print carries_item true; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertTrue(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testValidTypeCarries_item() {
		text = "name : \"test\" priority : 500 activities : print carries_item this; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		assertFalse(parseResult.equals(Optional.empty()));
	}
	
	@Test
	public void testBreakOutsideWhile() {
		text = "name : \"test\" priority : 500 activities : break; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		task = (Task) parseResult.get().get(0);
		assertFalse(task.isWellFormed());
	}
	
	@Test
	public void testUnassignedVariable(){
		text = "name : \"test\" priority : 500 activities : moveTo v; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		task = (Task) parseResult.get().get(0);
		assertFalse(task.isWellFormed());
	}
	
	@Test
	public void testValidTask(){
		text = "name : \"test\" priority : 500 activities : moveTo here; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		task = (Task) parseResult.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(task);
		testUnit.pickTask(task);
		assertNotNull(testUnit.getTask());
	}
	
	@Test
	public void testNullExceptionStatement(){
		text = "name : \"test\" priority : 500 activities : moveTo selected; ";
		Optional<List<Task>> parseResult = parser.parseString(text, selected);
		task = (Task) parseResult.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(task);
		testUnit.pickTask(task);
		assertNotNull(testUnit.getTask());
		testUnit.advanceTime(0.1);
		assertNull(testUnit.getTask());
	}

}
