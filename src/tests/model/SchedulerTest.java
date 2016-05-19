package tests.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.expression.Expression;
import hillbillies.model.statement.Statement;
import hillbillies.part3.programs.TaskParser;

public class SchedulerTest {
	
	private TaskFactory factory = new TaskFactory();
	private Unit testUnit;
	private TaskParser<Expression<?>, Statement, Task> parser = new TaskParser<>(factory);
	private Task task;


	@Before
	public void setup(){
		this.testUnit = new Unit("Billie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit);
		testUnit.stopDefault();
		String text = "name : \"test\" priority : 1000 activities : print this; ";
		List<int[]> list = new ArrayList<int[]>();
		Optional<List<Task>> parseResult = parser.parseString(text, list);
		task = (Task) parseResult.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(task);
		
	}
	
	@Test
	public void testGetScheduler(){
		assertTrue(testUnit.getFaction().getScheduler()!=null);
		assertTrue(testUnit.getFaction().getScheduler() instanceof Scheduler);
	}
	
	@Test
	public void testScheduleTask(){
		String text = "name : \"test\" priority : 1000 activities : print this; ";
		List<int[]> list = new ArrayList<int[]>();
		Optional<List<Task>> parseResult = parser.parseString(text, list);
		Task testTask = parseResult.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(testTask);
		assertTrue(testUnit.getFaction().getScheduler().getTasks().size()==2);
		assertTrue(testUnit.getFaction().getScheduler().getTasks().contains(testTask));
	}
	
	@Test
	public void testGetTask(){
		assertTrue(testUnit.getFaction().getScheduler().getTasks().size()==1);
		assertTrue(testUnit.getFaction().getScheduler().getTasks().contains(task));
	}
	
	@Test
	public void testRemoveTask(){
		testUnit.getFaction().getScheduler().removeTask(task);
		assertTrue(testUnit.getFaction().getScheduler().getTasks().size()==0);
		assertFalse(testUnit.getFaction().getScheduler().getTasks().contains(task));
	}
	
	@Test
	public void testGetHightestPriority(){
		String text = "name : \"test\" priority : 500 activities : print this; ";
		List<int[]> list = new ArrayList<int[]>();
		Optional<List<Task>> parseResult = parser.parseString(text, list);
		Task testTask = parseResult.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(testTask);
		assertEquals(task, testUnit.getFaction().getScheduler().getHighestPriority());
		String text2 = "name : \"test\" priority : 2000 activities : print this; ";
		List<int[]> list2 = new ArrayList<int[]>();
		Optional<List<Task>> parseResult2 = parser.parseString(text2, list2);
		Task testTask2 = parseResult2.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(testTask2);
		assertEquals(testTask2, testUnit.getFaction().getScheduler().getHighestPriority());
		testUnit.pickTask(testTask2);
		assertEquals(task, testUnit.getFaction().getScheduler().getHighestPriority());
	}
	
	@Test
	public void testInTaskSet(){
		Collection<Task> c = new HashSet<Task>();
		c.add(task);
		String text = "name : \"test\" priority : 1000 activities : print this; ";
		List<int[]> list = new ArrayList<int[]>();
		Optional<List<Task>> parseResult = parser.parseString(text, list);
		Task testTask = parseResult.get().get(0);
		Collection<Task> d = new HashSet<Task>();
		d.add(testTask);
		assertTrue(testUnit.getFaction().getScheduler().inTaskSet(c));
		assertFalse(testUnit.getFaction().getScheduler().inTaskSet(d));
		assertTrue(testUnit.getFaction().getScheduler().inTaskSet(task));
		assertFalse(testUnit.getFaction().getScheduler().inTaskSet(testTask));
	}
	
	@Test
	public void testIterator(){
		String text = "name : \"test\" priority : 500 activities : print this; ";
		List<int[]> list = new ArrayList<int[]>();
		Optional<List<Task>> parseResult = parser.parseString(text, list);
		Task testTask = parseResult.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(testTask);
		String text2 = "name : \"test\" priority : 2000 activities : print this; ";
		List<int[]> list2 = new ArrayList<int[]>();
		Optional<List<Task>> parseResult2 = parser.parseString(text2, list2);
		Task testTask2 = parseResult2.get().get(0);
		testUnit.getFaction().getScheduler().scheduleTask(testTask2);
		assertTrue(testUnit.getFaction().getScheduler().iterator().hasNext());
		assertTrue(testUnit.getFaction().getScheduler().iterator().next().getPriority() == 2000);	
	}

}
