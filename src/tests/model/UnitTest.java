package tests.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.Block.BlockType;
import hillbillies.model.Unit.State;
import hillbillies.part2.listener.TerrainChangeListener;

public class UnitTest {
	private Unit testUnit;
	private World testWorld;
	@Before
	public void setup(){
		this.testUnit = new Unit("Billie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit);
		testUnit.stopDefault();
		Map<ArrayList<Integer>, Block> testmap = new HashMap<ArrayList<Integer>, Block>();
		for (int i=0; i<50;i++){
			for (int j=0; j<50;j++){
				for (int k=0; k<50;k++){
					ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, k));
					Vector v = new Vector (i, j, k);
					Block B = new Block(v, BlockType.AIR);
					testmap.put(locationArray, B);
				}
			}
		}
		TerrainChangeListener modelLinstener = null;
		this.testWorld = new World(50, 50, 50, testmap, modelLinstener);
		testWorld.addUnit(testUnit);
	}
	
	@Test
	public void testSetPositionUpperOutOfBounds(){
		boolean checker = false;
		try{
			testUnit.setPosition(new Vector(-1.0, -1.0, -1.0));
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
			testUnit.setPosition(new Vector(51, 51, 51));
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
			testUnit.setPosition(new Vector(5.5, 5.5, 5.5));
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		ArrayList<Double> correct = new Vector(5.5, 5.5, 5.5).getCoeff();
		assertEquals(correct, testUnit.getPosition().getCoeff());
		
	}
	
	
	@Test
	public void testSetIllegalPrimStats(){
		HashMap<String, Integer> Stats = new HashMap<String, Integer>();
		Stats.put("str", 201);
		Stats.put("wgt", 3);
		Stats.put("agl", 51);
		Stats.put("tgh", 0);
		testUnit.setPrimStats(Stats);
		HashMap<String, Integer> CorrectStats = new HashMap<String, Integer>();
		CorrectStats.put("str", 200);
		CorrectStats.put("wgt", 126);
		CorrectStats.put("agl", 51);
		CorrectStats.put("tgh", 1);
				
		assertEquals(testUnit.getPrimStats(), CorrectStats);
	}
	@Test
	public void testSetAllowedPrimStats(){
		HashMap<String, Integer> Stats = new HashMap<String, Integer>();
		Stats.put("str", 100);
		Stats.put("wgt", 150);
		Stats.put("agl", 50);
		Stats.put("tgh", 75);
		testUnit.setPrimStats(Stats);
		assertEquals(Stats, testUnit.getPrimStats());
	}
	@Test
	public void testIllegalInitialPrimStats(){
		Unit testUnit2 = new Unit("Dummie", 0, 0, 0, 20, 20, 120, 120);
		HashMap<String, Integer> CorrectStats = new HashMap<String, Integer>();
		CorrectStats.put("str", 25);
		CorrectStats.put("wgt", 63);
		CorrectStats.put("agl", 100);
		CorrectStats.put("tgh", 100);
		assertEquals(testUnit2.getPrimStats(), CorrectStats);
	}
	@Test
	public void testAllowedInitialPrimStats(){
		Unit testUnit2 = new Unit("Dummie", 0, 0, 0, 75, 50, 25, 60);
		HashMap<String, Integer> Stats = new HashMap<String, Integer>();
		Stats.put("str", 75);
		Stats.put("wgt", 50);
		Stats.put("agl", 25);
		Stats.put("tgh", 60);
		assertEquals(Stats, testUnit2.getPrimStats());
	}
	
	
	@Test
	public void testNoUpperCaseName(){
		boolean checker = false;
		try{
			testUnit.setName("billie");
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
			testUnit.setName("Billie1");
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
			testUnit.setName("B");
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
			testUnit.setName("Bob");
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		assertEquals("Bob", testUnit.getName());
	}
	
	
	@Test
	public void testWorkState(){
		testUnit.workAt(testUnit.getPosition());
		assertEquals(State.WORKING, testUnit.getState());
	}
	@Test
	public void testWorkTime(){
		testUnit.workAt(testUnit.getPosition());
		assertEquals(10.0, testUnit.getWorkTime(), 0.0000000001);
		testUnit.advanceTime(0.1);
		assertEquals(9.9, testUnit.getWorkTime(), 0.0000000001);
	}
	@Test
	public void testStopWorking(){
		testUnit.workAt(testUnit.getPosition());
		for (int idx = 1; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		}
		assertEquals(State.WORKING, testUnit.getState());
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		assertEquals(State.IDLE, testUnit.getState());
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(0, checkLvlUp);
	}
	@Test
	public void testOperateWorkShop(){
		Vector v = new Vector (1, 0, 0);
		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(1, 0, 0));
		testWorld.getBlockAtPos(pos).setBlockType(BlockType.WORKSHOP);
		Log log = new Log(v.getX(), v.getY(), v.getZ(), (int) 30);
		testWorld.addLog(log);
		Boulder boulder = new Boulder(v.getX(), v.getY(), v.getZ(), (int)30);
		testWorld.addBoulder(boulder);
		testUnit.workAt(v);
		for (int idx = 0; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		}
		boolean checkTgh = false;
		if (testUnit.getPrimStats().get("tgh")==55||testUnit.getPrimStats().get("tgh")==56)
			checkTgh = true;
		boolean checkWgt = false;
		if (testUnit.getPrimStats().get("wgt")==55||testUnit.getPrimStats().get("wgt")==56)
			checkWgt = true;
		assertTrue(checkTgh);
		assertTrue(checkWgt);
		assertTrue(testWorld.getBlockAtPos(v).getBouldersInBlock().isEmpty());
		assertTrue(testWorld.getBlockAtPos(v).getLogsInBlock().isEmpty());
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(11, checkLvlUp);
	}
	@Test
	public void testPickupLog(){
		Vector v = new Vector (1, 0, 0);
		Log log = new Log(v.getX(), v.getY(), v.getZ(), (int) 30);
		testWorld.addLog(log);
		testUnit.workAt(v);
		for (int idx = 0; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		} 
		assertTrue(testWorld.getBlockAtPos(v).getLogsInBlock().isEmpty());
		assertTrue(testUnit.getLog()==log);
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(1, checkLvlUp);
	}
	@Test
	public void testPickupBoulder(){
		Vector v = new Vector (1, 0, 0);
		Boulder boulder = new Boulder(v.getX(), v.getY(), v.getZ(), (int) 30);
		testWorld.addBoulder(boulder);
		testUnit.workAt(v);
		for (int idx = 0; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		} 
		assertTrue(testWorld.getBlockAtPos(v).getBouldersInBlock().isEmpty());
		assertTrue(testUnit.getBoulder()==boulder);
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(1, checkLvlUp);
	}
	@Test
	public void testDrop(){
		Vector w = new Vector (0, 0, 0);
		Vector v = new Vector (1, 0, 0);
		Boulder boulder = new Boulder(w.getX(), w.getY(), w.getZ(), (int) 30);
		testUnit.lift(boulder);
		testUnit.workAt(v);
		for (int idx = 0; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		} 
		assertTrue(testWorld.getBlockAtPos(v).getBouldersInBlock().contains(boulder));
		assertTrue(testUnit.getBoulder()==null);
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(1, checkLvlUp);
	}
	@Test
	public void testDigRock(){
		Vector v = new Vector (1, 0, 0);
		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(1, 0, 0));
		testWorld.getBlockAtPos(pos).setBlockType(BlockType.ROCK);
		testUnit.workAt(v);
		for (int idx = 0; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		}
		assertEquals(BlockType.AIR, testWorld.getBlockAtPos(v).getBlockType());
		assertFalse(testWorld.getBlockAtPos(v).getBouldersInBlock().isEmpty());
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(1, checkLvlUp);
	}
	@Test
	public void testChopWood(){
		Vector v = new Vector (1, 0, 0);
		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(1, 0, 0));
		testWorld.getBlockAtPos(pos).setBlockType(BlockType.WOOD);
		testUnit.workAt(v);
		for (int idx = 0; idx<11 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		}
		assertEquals(BlockType.AIR, testWorld.getBlockAtPos(v).getBlockType());
		assertFalse(testWorld.getBlockAtPos(v).getLogsInBlock().isEmpty());
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(1, checkLvlUp);
	}
	@Test
	public void testTerminate(){
		Vector w = new Vector (0, 0, 0);
		Boulder boulder = new Boulder(w.getX(), w.getY(), w.getZ(), (int) 30);
		testUnit.lift(boulder);
		testUnit.setHp(0);
		assertFalse(testWorld.getUnits().contains(testUnit));
		assertTrue(testWorld.getBlockAtPos(w).getBouldersInBlock().contains(boulder));
		assertTrue(testUnit.isTerminated());
	}
	@Test
	public void testIsCarrying(){
		Vector w = new Vector (0, 0, 0);
		Boulder boulder = new Boulder(w.getX(), w.getY(), w.getZ(), (int) 30);
		testUnit.lift(boulder);
		assertTrue(testUnit.isCarrying());
	}
	@Test
	public void testCarryWeightSlowDown(){
		Vector v = new Vector (1, 0, 0);
		Vector w = new Vector (0, 0, 0);
		Boulder boulder = new Boulder(w.getX(), w.getY(), w.getZ(), (int) 30);
		testUnit.lift(boulder);
		assertEquals(30, testUnit.getCarryWeight());
		testUnit.move2(v);
		testUnit.advanceTime(0.1);
		assertEquals(1.5*((double)(testUnit.getPrimStats().get("str")+testUnit.getPrimStats().get("agl")))/(2*(testUnit.getPrimStats().get("wgt")+30)), testUnit.getBaseSpeed(), 0.0000001);
	}
	@Test
	public void testGetXP(){
		testUnit.moveToAdjacent(1, 0, 0);
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		assertEquals(1, testUnit.getExp());
		
	}
	@Test
	public void testStepLevelUp(){
		Vector v = new Vector (10, 0, 0);
		testUnit.move2(v);
		for (int idx = 0; idx<10 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
		}
		int checkLvlUp = -200;
		for (Map.Entry<String, Integer> entry : testUnit.getPrimStats().entrySet()){
			checkLvlUp+=entry.getValue();
		}
		assertEquals(1, checkLvlUp);
	}
	@Test
	public void testFaction(){
		Faction faction = testUnit.getFaction();
		assertEquals(1, faction.getUnits().size());
	}
	
	@Test
	public void testRestWhenFull(){
		testUnit.rest();
		assertEquals(State.IDLE, testUnit.getState());
	}
	@Test
	public void testRestWhenInjured(){
		testUnit.setHp(testUnit.getHp() - 1.0);
		testUnit.rest();
		assertEquals(State.RESTING, testUnit.getState());
	}
	@Test
	public void testRestWhenExhausted(){
		testUnit.setHp(testUnit.getStam() - 1);
		testUnit.rest();
		assertEquals(State.RESTING, testUnit.getState());
	}
	@Test
	public void testMinRestTime(){
		testUnit.setHp(testUnit.getHp() - 2);
		testUnit.rest();
		assertEquals(40.0/testUnit.getPrimStats().get("tgh"), testUnit.getMinRestTime(), 0.000000001);
		testUnit.advanceTime(0.1);
		assertEquals(40.0/testUnit.getPrimStats().get("tgh")-0.1, testUnit.getMinRestTime(), 0.000000001);
		testUnit.move2(new Vector(1.0, 1.0, 1.0));
		testUnit.advanceTime(0.1);
		assertEquals(State.RESTING, testUnit.getState());
	}
	@Test
	public void testAutoRest(){
		Vector V = new Vector(10.0, 10.0, 0);
		testUnit.move2(V);
		testUnit.toggleSprint();
		for (int idx = 1; idx<181 ; idx++){
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			testUnit.advanceTime(0.2);
			assertEquals((double)(idx), testUnit.getRestTime(), 0.000001);
		}
		assertEquals(State.IDLE, testUnit.getState());
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		assertEquals(State.RESTING, testUnit.getState());
	}
	@Test
	public void testOrientationMoveTo(){
		testUnit.move2(new Vector(2, 2, 0));
		assertEquals(Math.PI/4, testUnit.getTheta(), 0.0000000001);
	}
	@Test
	public void testFinPosOutBoundsMoveTo(){
		Vector startPos = testUnit.getPosition();
		testUnit.move2(new Vector(-1, -1, -1));
		testUnit.advanceTime(0.1);
		assertTrue(startPos.equals(testUnit.getPosition()));
	}
	@Test
	public void testMoveToSolid(){
		Vector v = new Vector(1, 0, 0);
		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(1, 0, 0));
		testWorld.getBlockAtPos(pos).setBlockType(BlockType.ROCK);
		Vector startPos = testUnit.getPosition();
		testUnit.move2(v);
		testUnit.advanceTime(0.1);
		assertTrue(startPos.equals(testUnit.getPosition()));
	}
	@Test
	public void testMoveToNonWalkable(){
		Vector startPos = testUnit.getPosition();
		testUnit.move2(new Vector(0, 0, 1));
		testUnit.advanceTime(0.1);
		assertTrue(startPos.equals(testUnit.getPosition()));
	}
	@Test
	public void testInCentreOfBlockAfterMoveTo(){
		testUnit.setPosition(new Vector(1, 1, 0));
		testUnit.move2(new Vector(0, 0, 0));
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		testUnit.advanceTime(0.2);
		ArrayList<Double> Correct = new Vector(0.5, 0.5, 0.5).getCoeff();
		assertEquals(Correct, testUnit.getPosition().getCoeff());
	}
	@Test
	public void testMoveToAdjacentOutOfRange(){
		boolean checker = false;
		try{
			testUnit.moveToAdjacent(2, 0, 0);
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertTrue(checker);	
	}
	@Test
	public void testMoveToCorrectBaseSpeed(){
		testUnit.move2(new Vector(1, 1, 0));
		testUnit.advanceTime(0.1);
		assertEquals(1.5*((double)(testUnit.getPrimStats().get("str")+testUnit.getPrimStats().get("agl")))/(2*testUnit.getPrimStats().get("wgt")), testUnit.getBaseSpeed(), 0.000000001);
		
	}
	@Test
	public void testMoveToCorrectWalkSpeedUp(){
		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(1, 0, 0));
		testWorld.getBlockAtPos(pos).setBlockType(BlockType.ROCK);
		testUnit.moveToAdjacent(0, 0, 0);
		testUnit.move2(new Vector(0, 0, 1));
		testUnit.advanceTime(0.1);
		assertEquals(testUnit.getBaseSpeed()*0.5, testUnit.getSpeed(), 0.0000000001);
		
	}
	@Test
	public void testMoveToCorrectWalkSpeedDown(){
		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(1, 0, 0));
		testWorld.getBlockAtPos(pos).setBlockType(BlockType.ROCK);
		testUnit.setPosition(new Vector(0.5, 0.5, 1.5));
		testUnit.moveToAdjacent(0, 0, 0);
		testUnit.move2(new Vector(0, 0, 0));
		testUnit.advanceTime(0.1);
		assertEquals(testUnit.getBaseSpeed()*1.2, testUnit.getSpeed(), 0.0000000001);
	}
	@Test
	public void testIsMoving(){
		testUnit.move2(new Vector(1, 1, 0));
		testUnit.advanceTime(0.1);
		assertTrue(testUnit.isMoving());
		
	}
	@Test
	public void testIsSprinting(){
		testUnit.move2(new Vector(1, 1, 0));
		testUnit.toggleSprint();
		assertEquals(State.SPRINTING, testUnit.getState());
		
	}
	@Test
	public void testSprintSpeed(){
		testUnit.move2(new Vector(1, 1, 0));
		testUnit.toggleSprint();
		assertEquals(testUnit.getBaseSpeed()*2, testUnit.getSpeed(), 0.00000000000001);
		
	}
	@Test
	public void testStopSprintingAtZeroStam(){
		Vector V = new Vector(5.0, 5.0, 0.0);
		testUnit.move2(V);
		testUnit.setStam(2);
		testUnit.toggleSprint();
		testUnit.advanceTime(0.2);
		assertEquals(State.SPRINTING, testUnit.getState());
		testUnit.advanceTime(0.2);
		assertEquals(State.WALKING, testUnit.getState());
		
	}
	@Test
	public void testSetTooLowHp(){
		boolean checker = false;
		try{
			testUnit.setHp(-1);
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
			testUnit.setHp(testUnit.getMaxHp()+1);
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
			testUnit.setHp(testUnit.getMaxHp());
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		assertEquals(testUnit.getMaxHp(), testUnit.getHp(), 0.0000000000001);
	}
	@Test
	public void testSetTooLowStam(){
		boolean checker = false;
		try{
			testUnit.setStam(-1);
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
			testUnit.setStam(testUnit.getMaxStam()+1);
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
			testUnit.setStam(testUnit.getMaxStam());
		}
		catch(AssertionError AE){
			AE.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
		assertEquals(testUnit.getMaxStam(), testUnit.getStam(), 0.0000000000001);
	}
	
	
	@Test
	public void testRestWhenMoving(){
		testUnit.move2(new Vector(10, 0, 0));
		testUnit.advanceTime(0.1);
		testUnit.rest();
		assertEquals(State.WALKING, testUnit.getState());
	}
	@Test
	public void testWorkWhenMoving(){
		testUnit.move2(new Vector(10, 0, 0));
		testUnit.advanceTime(0.1);
		testUnit.workAt(testUnit.getPosition());
		assertEquals(State.WALKING, testUnit.getState());
	}

	
	@Test
	public void testAdvanceTimeTooLong(){
		boolean checker = false;
		try{
			testUnit.advanceTime(1);;
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
			testUnit.advanceTime(-1);;
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
			testUnit.advanceTime(0.1);;
		}
		catch(IllegalArgumentException exc){
			exc.printStackTrace();
			checker = true;
		}
		assertFalse(checker);
	}
	
	@Test
	public void testThetaAllowed(){
		testUnit.setTheta(0.0);
		assertEquals(0.0, testUnit.getTheta(), 0.000000001);
	}
	
	@Test
	public void testStartDefault(){
		testUnit.startDefault();
		assertTrue(testUnit.DefaultOn());
	}
	@Test
	public void testStopDefault(){
		testUnit.stopDefault();
		assertFalse(testUnit.DefaultOn());
	}
	
	@Test
	public void testAttackSelf(){
		testUnit.attack(testUnit);
		assertEquals(State.IDLE, testUnit.getState());
	}
	@Test
	public void testAttackCooldown(){
		Unit testUnit2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit2);
		Unit testUnit3 = new Unit("Bob", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit3);
		testUnit.attack(testUnit2);
		testUnit.advanceTime(0.1);
		assertEquals(0.9, testUnit.getAttackCooldown(), 0.0000000001);
		testUnit.attack(testUnit3);
		assertEquals(State.IDLE, testUnit3.getState());
	}
	@Test
	public void testAttackOutOfRange(){
		Unit testUnit2 = new Unit("Dummie", 0.5, 0.5, 2.5, 50, 50, 50, 50);
		new Faction(testUnit2);
		testUnit.attack(testUnit2);
		assertEquals(State.IDLE, testUnit.getState());
		assertEquals(State.IDLE, testUnit2.getState());
	}
	@Test
	public void testAttackAllowed(){
		Unit testUnit2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit2);
		testUnit.attack(testUnit2);
		assertEquals(State.COMBAT, testUnit.getState());
		assertEquals(State.COMBAT, testUnit2.getState());
	}
	@Test
	public void testChangeStateInCombat(){
		Unit testUnit2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		new Faction(testUnit2);
		testUnit.attack(testUnit2);
		testUnit.advanceTime(0.1);
		testUnit.move2(new Vector(1.0, 1.0, 1.0));
		testUnit.advanceTime(0.1);
		assertEquals(State.COMBAT, testUnit.getState());
		testUnit.workAt(testUnit.getPosition());
		testUnit.advanceTime(0.1);
		assertEquals(State.COMBAT, testUnit.getState());
		testUnit.rest();
		testUnit.advanceTime(0.1);
		assertEquals(State.COMBAT, testUnit.getState());
	}
	@Test
	public void testAttackSameFaction(){
		Unit testUnit2 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		testWorld.addUnit(testUnit2);
		Unit testUnit3 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		testWorld.addUnit(testUnit3);
		Unit testUnit4 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		testWorld.addUnit(testUnit4);
		Unit testUnit5 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		testWorld.addUnit(testUnit5);
		Unit testUnit6 = new Unit("Dummie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
		testWorld.addUnit(testUnit6);
		testUnit.attack(testUnit6);
		testWorld.advanceTime(0.1);
		System.out.println(testUnit.getState());
		assertEquals(State.IDLE, testUnit.getState());
	}
	@Test
	public void testFall(){
		double BaseLine = testUnit.getHp();
		testUnit.setPosition(new Vector(10, 10, 3));
		testWorld.advanceTime(0.2);
		assertEquals(State.FALLING, testUnit.getState());
		testWorld.advanceTime(0.2);
		assertEquals(State.FALLING, testUnit.getState());
		testWorld.advanceTime(0.2);
		assertEquals(State.FALLING, testUnit.getState());
		testWorld.advanceTime(0.2);
		assertEquals(State.IDLE, testUnit.getState());
		assertEquals(BaseLine-30, testUnit.getHp(), 0.0000001);
	}
}
	


