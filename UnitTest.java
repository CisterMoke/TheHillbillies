package hillbillies.tests.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import hillbillies.model.Unit;
import hillbillies.model.Unit.State;

public class UnitTest {
	Unit test = new Unit("Billie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
	
	@Test
	public void testSetPositionUpperOutOfBounds(){
		boolean checker = false;
		try{
			test.setPosition(-1.0, -1.0, -1.0);
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testSetPositionLowerOutOfBounds() {
		boolean checker = false;
		try{
			test.setPosition(51, 51, 51);
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testSetPostionAllowed(){
		boolean checker = false;
		try{
			test.setPosition(5.5, 5.5, 5.5);
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		ArrayList<Double> correct = new ArrayList<Double>(Arrays.asList(5.5, 5.5, 5.5));
		assertEquals(correct, test.getPosition());
		
	}
	
	
	@Test
	public void testSetIllegalPrimStats(){
		HashMap<String, Integer> Stats = new HashMap<String, Integer>();
		Stats.put("str", 201);
		Stats.put("wgt", 3);
		Stats.put("agl", 51);
		Stats.put("tgh", 0);
		test.setPrimStats(Stats);
		HashMap<String, Integer> CorrectStats = new HashMap<String, Integer>();
		CorrectStats.put("str", 200);
		CorrectStats.put("wgt", 126);
		CorrectStats.put("agl", 51);
		CorrectStats.put("tgh", 1);
				
		assertEquals(test.getPrimStats(), CorrectStats);
	}
	@Test
	public void testSetAllowedPrimStats(){
		HashMap<String, Integer> Stats = new HashMap<String, Integer>();
		Stats.put("str", 100);
		Stats.put("wgt", 150);
		Stats.put("agl", 50);
		Stats.put("tgh", 75);
		test.setPrimStats(Stats);
		assertEquals(Stats, test.getPrimStats());
	}
	@Test
	public void testIllegalInitialPrimStats(){
		Unit test2 = new Unit("Dummie", 0, 0, 0, 20, 20, 120, 120);
		HashMap<String, Integer> CorrectStats = new HashMap<String, Integer>();
		CorrectStats.put("str", 25);
		CorrectStats.put("wgt", 63);
		CorrectStats.put("agl", 100);
		CorrectStats.put("tgh", 100);
		assertEquals(test2.getPrimStats(), CorrectStats);
	}
	@Test
	public void testAllowedInitialPrimStats(){
		Unit test2 = new Unit("Dummie", 0, 0, 0, 75, 50, 25, 60);
		HashMap<String, Integer> Stats = new HashMap<String, Integer>();
		Stats.put("str", 75);
		Stats.put("wgt", 50);
		Stats.put("agl", 25);
		Stats.put("tgh", 60);
		assertEquals(Stats, test2.getPrimStats());
	}
	
	
	@Test
	public void testNoUpperCaseName(){
		boolean checker = false;
		try{
			test.setName("billie");
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testIllegalCharacterName(){	
		boolean checker = false;
		try{
			test.setName("Billie1");
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testTooShortName(){
		boolean checker = false;
		try{
			test.setName("B");
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testAllowedName(){
		boolean checker = false;
		try{
			test.setName("Bob");
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		assertEquals("Bob", test.getName());
	}
	
	
	@Test
	public void testWorkState(){
		test.work();
		assertEquals(State.WORKING, test.getState());
	}
	@Test
	public void testWorkTime(){
		test.work();
		assertEquals(10.0, test.getWorkTime(), 0.0000000001);
		test.advanceTime(0.1);
		assertEquals(9.9, test.getWorkTime(), 0.0000000001);
	}
	@Test
	public void testStopWorking(){
		test.work();
		for (int idx = 1; idx<11 ; idx++){
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			test.advanceTime(0.2);
		}
		assertEquals(State.WORKING, test.getState());
		test.advanceTime(0.2);
		test.advanceTime(0.2);
		assertEquals(State.IDLE, test.getState());
	}
	
	
	@Test
	public void testRestState(){
		test.rest();
		assertEquals(State.RESTING, test.getState());
	}
	@Test
	public void testMinRestTime(){
		test.rest();
		assertEquals(40.0/test.getPrimStats().get("tgh"), test.getMinRestTime(), 0.000000001);
		test.advanceTime(0.1);
		assertEquals(40.0/test.getPrimStats().get("tgh")-0.1, test.getMinRestTime(), 0.000000001);
		test.moveTo(1.0, 1.0, 1.0);
		test.advanceTime(0.1);
		assertEquals(State.RESTING, test.getState());
	}
	@Test
	public void testAutoRest(){
		test.moveTo(10.0, 10.0, 10.0);
		test.setState(State.SPRINTING);
		for (int idx = 1; idx<181 ; idx++){
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			test.advanceTime(0.2);
			assertEquals((double)(idx), test.getRestTime(), 0.000001);
		}
		assertEquals(State.IDLE, test.getState());
		test.advanceTime(0.2);
		test.advanceTime(0.2);
		assertEquals(State.RESTING, test.getState());
	}
	
	
	@Test
	public void testOrientationMoveTo(){
		test.moveTo(2, 2, 2);
		assertEquals(Math.PI/4, test.getTheta(), 0.0000000001);
	}
	@Test
	public void testFinPosOutBoundsMoveTo(){
		test.moveTo(-1, -1, -1);
		test.advanceTime(0.1);
		assertEquals(test.getBlockPosition(), test.getPosition());
	}
	@Test
	public void testInCentreOfBlockAfterMoveTo(){
		test.setPosition(1, 1, 1);
		test.moveTo(0, 0, 0);
		test.advanceTime(0.2);
		test.advanceTime(0.2);
		test.advanceTime(0.2);
		test.advanceTime(0.2);
		test.advanceTime(0.2);
		ArrayList<Double> Correct = new ArrayList<Double>(Arrays.asList(0.5, 0.5, 0.5));
		assertEquals(Correct, test.getPosition());
	}
	@Test
	public void testMoveToAdjacentOutOfRange(){
		boolean checker = false;
		try{
			test.moveToAdjacent(2, 0, 0);
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);	
	}
	@Test
	public void testMoveToCorrectBaseSpeed(){
		test.moveTo(1, 1, 1);
		test.advanceTime(0.1);
		assertEquals(1.5*((double)(test.getPrimStats().get("str")+test.getPrimStats().get("agl")))/(2*test.getPrimStats().get("wgt")), test.getBaseSpeed(), 0.000000001);
		
	}
	@Test
	public void testMoveToCorrectWalkSpeedUp(){
		test.moveTo(0, 0, 1);
		test.advanceTime(0.1);
		assertEquals(test.getBaseSpeed()*0.5, test.getSpeed(), 0.0000000001);
		
	}
	@Test
	public void testMoveToCorrectWalkSpeedDown(){
		test.setPosition(0.5, 0.5, 1.5);
		test.moveTo(0, 0, 0);
		test.advanceTime(0.1);
		assertEquals(test.getBaseSpeed()*1.2, test.getSpeed(), 0.0000000001);
	}
	@Test
	public void testIsMoving(){
		test.moveTo(1, 1, 1);
		test.advanceTime(0.1);
		assertTrue(test.isMoving());
		
	}
	@Test
	public void testIsSprinting(){
		test.moveTo(1, 1, 0);
		test.setState(State.SPRINTING);
		assertEquals(State.SPRINTING, test.getState());
		
	}
	@Test
	public void testSprintSpeed(){
		test.moveTo(1, 1, 0);
		test.setState(State.SPRINTING);
		assertEquals(test.getBaseSpeed()*2, test.getSpeed(), 0.00000000000001);
		
	}
	@Test
	public void testStopSprintingAtZeroStam(){
		test.moveTo(5, 5, 0);
		test.setStam(2);
		test.setState(State.SPRINTING);
		test.advanceTime(0.2);
		assertEquals(State.SPRINTING, test.getState());
		test.advanceTime(0.2);
		assertEquals(State.WALKING, test.getState());
		
	}
	@Test
	public void testSetTooLowHp(){
		boolean checker = false;
		try{
			test.setHp(-1);
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testSetTooHighHp(){
		boolean checker = false;
		try{
			test.setHp(test.getMaxHp()+1);
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testSetAllowedHp(){
		boolean checker = false;
		try{
			test.setHp(test.getMaxHp());
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		assertEquals(test.getMaxHp(), test.getHp(), 0.0000000000001);
	}
	@Test
	public void testSetTooLowStam(){
		boolean checker = false;
		try{
			test.setStam(-1);
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testSetTooHighStam(){
		boolean checker = false;
		try{
			test.setStam(test.getMaxStam()+1);
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testSetAllowedStam(){
		boolean checker = false;
		try{
			test.setStam(test.getMaxStam());
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		assertEquals(test.getMaxStam(), test.getStam(), 0.0000000000001);
	}
	
	
	@Test
	public void testRestWhenMoving(){
		test.moveTo(0, 0, 0);
		test.advanceTime(0.1);
		test.rest();
		assertEquals(State.WALKING, test.getState());
	}
	@Test
	public void testWorkWhenMoving(){
		test.moveTo(0, 0, 0);
		test.advanceTime(0.1);
		test.work();
		assertEquals(State.WALKING, test.getState());
	}

	
	@Test
	public void testAdvanceTimeTooLong(){
		boolean checker = false;
		try{
			test.advanceTime(1);;
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testAdvanceTimeNegative(){
		boolean checker = false;
		try{
			test.advanceTime(-1);;
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);
	}
	@Test
	public void testAdvanceTimeAllowed(){
		boolean checker = false;
		try{
			test.advanceTime(0.1);;
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
	}
	
	@Test
	public void testThetaAllowed(){
		test.setTheta(0.0);
		assertEquals(0.0, test.getTheta(), 0.000000001);
	}
	
	@Test
	public void testStartDefault(){
		test.startDefault();
		assertTrue(test.DefaultOn());
	}
	@Test
	public void testStopDefault(){
		test.stopDefault();
		assertFalse(test.DefaultOn());
	}
	
	@Test
	public void testAttackSelf(){
		test.attack(test);
		assertEquals(State.IDLE, test.getState());
	}
	@Test
	public void testAttackCooldown(){
		Unit test2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		Unit test3 = new Unit("Bob", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		test.attack(test2);
		test.advanceTime(0.1);
		assertEquals(0.9, test.getAttackCooldown(), 0.0000000001);
		test.attack(test3);
		assertEquals(State.IDLE, test3.getState());
	}
	@Test
	public void testAttackOutOfRange(){
		Unit test2 = new Unit("Dummie", 0.5, 0.5, 2.5, 50, 50, 50, 50);
		test.attack(test2);
		assertEquals(State.IDLE, test.getState());
		assertEquals(State.IDLE, test2.getState());
	}
	@Test
	public void testAttackAllowed(){
		Unit test2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		test.attack(test2);
		assertEquals(State.COMBAT, test.getState());
		assertEquals(State.COMBAT, test2.getState());
	}
	@Test
	public void testToggleAttackInitiated(){
		test.toggleAttackInitiated();
		assertTrue(test.isAttackInitiated());
		test.toggleAttackInitiated();
		assertFalse(test.isAttackInitiated());
	}
	@Test
	public void testChangeStateInCombat(){
		Unit test2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		test.attack(test2);
		test.advanceTime(0.1);
		test.moveTo(1.0, 1.0, 1.0);
		test.advanceTime(0.1);
		assertEquals(State.COMBAT, test.getState());
		test.work();
		test.advanceTime(0.1);
		assertEquals(State.COMBAT, test.getState());
		test.rest();
		test.advanceTime(0.1);
		assertEquals(State.COMBAT, test.getState());
	}
}
	


