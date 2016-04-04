package hillbillies.part2.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hillbillies.model.*;
import hillbillies.model.Block.BlockType;
import hillbillies.model.Unit.State;
import hillbillies.part2.listener.TerrainChangeListener;
import ogp.framework.util.ModelException;

public class Facade implements IFacade {

	@Override
	public Unit createUnit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehavior) throws ModelException {
		try{
			Unit unit = new Unit(name, (double)(initialPosition[0]) + 0.5,
									(double) (initialPosition[1]) + 0.5,
									(double) (initialPosition[2]) + 0.5, 
									strength, weight, agility, toughness );
			return unit;
		}
		catch(IllegalArgumentException exc){
			throw new ModelException();
		}
	}

	@Override
	public double[] getPosition(Unit unit) throws ModelException {
		double[] pos = new double[]{unit.getPosition().getX(), unit.getPosition().getY(), unit.getPosition().getZ()};
		return pos;
	}

	@Override
	public int[] getCubeCoordinate(Unit unit) throws ModelException {
		int[] cubepos = new int[]{(int) (unit.getBlockPosition().getX()), (int) (unit.getBlockPosition().getY()), (int)(unit.getBlockPosition().getZ())};
		return cubepos;
	}

	@Override
	public String getName(Unit unit) throws ModelException {
		String name = unit.getName();
		return name;
	}

	@Override
	public void setName(Unit unit, String newName) throws ModelException {
		try{
			unit.setName(newName);
		}
		catch (IllegalArgumentException exc){
			throw new ModelException();
		}
	}

	@Override
	public int getWeight(Unit unit) throws ModelException {
		return unit.getPrimStats().get("wgt");
	}

	@Override
	public void setWeight(Unit unit, int newValue) throws ModelException {
		Map<String, Integer> newStats = unit.getPrimStats();
		newStats.put("wgt", newValue);
		unit.setPrimStats(newStats);
		
	}

	@Override
	public int getStrength(Unit unit) throws ModelException {
		return unit.getPrimStats().get("str");
	}

	@Override
	public void setStrength(Unit unit, int newValue) throws ModelException {
		Map<String, Integer> newStats = unit.getPrimStats();
		newStats.put("str", newValue);
		unit.setPrimStats(newStats);
		
	}

	@Override
	public int getAgility(Unit unit) throws ModelException {
		return unit.getPrimStats().get("agl");
	}

	@Override
	public void setAgility(Unit unit, int newValue) throws ModelException {
		Map<String, Integer> newStats = unit.getPrimStats();
		newStats.put("agl", newValue);
		unit.setPrimStats(newStats);
	}

	@Override
	public int getToughness(Unit unit) throws ModelException {
		return unit.getPrimStats().get("tgh");
	}

	@Override
	public void setToughness(Unit unit, int newValue) throws ModelException {
		Map<String, Integer> newStats = unit.getPrimStats();
		newStats.put("tgh", newValue);
		unit.setPrimStats(newStats);
	}

	@Override
	public int getMaxHitPoints(Unit unit) throws ModelException {
		return unit.getMaxHp();
	}

	@Override
	public int getCurrentHitPoints(Unit unit) throws ModelException {
		return (int)(unit.getHp());
	}

	@Override
	public int getMaxStaminaPoints(Unit unit) throws ModelException {
		return unit.getMaxStam();
	}

	@Override
	public int getCurrentStaminaPoints(Unit unit) throws ModelException {
		return (int)(unit.getStam());
	}

	@Override
	public void advanceTime(Unit unit, double dt) throws ModelException {
		try{
			unit.advanceTime(dt);
		}
		catch(IllegalArgumentException exc){
			throw new ModelException();
		}
		
	}

	@Override
	public void moveToAdjacent(Unit unit, int dx, int dy, int dz) throws ModelException {
		try{
			unit.moveToAdjacent(dx, dy, dz);
		}
		catch (IllegalArgumentException exc){
			throw new ModelException();
		}
	}

	@Override
	public double getCurrentSpeed(Unit unit) throws ModelException {
		return unit.getSpeed();
	}

	@Override
	public boolean isMoving(Unit unit) throws ModelException {
		return (unit.isMoving());
	}

	@Override
	public void startSprinting(Unit unit) throws ModelException {
		if (unit.getState() == State.WALKING)
			unit.sprint();
	}

	@Override
	public void stopSprinting(Unit unit) throws ModelException {
		if(unit.getState() == State.SPRINTING)
			unit.idle();
		
	}

	@Override
	public boolean isSprinting(Unit unit) throws ModelException {
		return (unit.getState() == State.SPRINTING);
	}

	@Override
	public double getOrientation(Unit unit) throws ModelException {
		return unit.getTheta();
	}

	@Override
	public void moveTo(Unit unit, int[] cube) throws ModelException {
		unit.move2(new Vector(cube[0], cube[1], cube[2]));
	}

	@Override
	public void work(Unit unit) throws ModelException {
		unit.work();
		
	}

	@Override
	public boolean isWorking(Unit unit) throws ModelException {
		return (unit.getState() == State.WORKING);
	}

	@Override
	public void fight(Unit attacker, Unit defender) throws ModelException {
		attacker.attack(defender);
		
	}

	@Override
	public boolean isAttacking(Unit unit) throws ModelException {
		return (unit.getState() == State.COMBAT);
	}

	@Override
	public void rest(Unit unit) throws ModelException {
		unit.rest();
	}

	@Override
	public boolean isResting(Unit unit) throws ModelException {
		return (unit.getState() == State.RESTING);
	}

	@Override
	public void setDefaultBehaviorEnabled(Unit unit, boolean value) throws ModelException {
		if (value){
			unit.startDefault();
		}
		else{
			unit.stopDefault();
		}
		
	}

	@Override
	public boolean isDefaultBehaviorEnabled(Unit unit) throws ModelException {
		return unit.DefaultOn();
	}

	@Override
	public World createWorld(int[][][] terrainTypes, TerrainChangeListener modelListener) throws ModelException {
		Map<ArrayList<Integer>, Block> terrain = new HashMap<ArrayList<Integer>, Block>();
		for(int x=0; x < terrainTypes.length; x++){
			for(int y=0; y < terrainTypes[0].length; y++){
				for(int z=0; z < terrainTypes[0][0].length; z++){
					ArrayList<Integer> position = new ArrayList<Integer>();
					position.add(x);
					position.add(y);
					position.add(z);
					terrain.put(position, new Block(new Vector(x, y, z), Facade.typeList.get(terrainTypes[x][y][z])));
				}
			}
		}
		return new World(terrainTypes.length, terrainTypes[0].length, terrainTypes[0][0].length, terrain);
	}

	@Override
	public int getNbCubesX(World world) throws ModelException {
		return world.getBorders().get(0);
	}

	@Override
	public int getNbCubesY(World world) throws ModelException {
		return world.getBorders().get(1);
	}

	@Override
	public int getNbCubesZ(World world) throws ModelException {
		return world.getBorders().get(2);
	}

	@Override
	public void advanceTime(World world, double dt) throws ModelException {
		world.advanceTime(dt);
	}

	@Override
	public int getCubeType(World world, int x, int y, int z) throws ModelException {
		return Facade.typeList.indexOf(world.getBlockAtPos(new Vector(x, y, z)).getBlockType());
	}

	@Override
	public void setCubeType(World world, int x, int y, int z, int value) throws ModelException {
		world.getBlockAtPos(new Vector(x, y, z)).setBlockType(Facade.typeList.get(value));
	}

	@Override
	public boolean isSolidConnectedToBorder(World world, int x, int y, int z) throws ModelException {
		Block block = world.getBlockAtPos(new Vector(x, y, z));
		return (world.isAtBorder(block) && block.isSolid());
	}

	@Override
	public Unit spawnUnit(World world, boolean enableDefaultBehavior) throws ModelException {
		Unit unit = world.spawnUnit();
		if(enableDefaultBehavior)
			unit.startDefault();
		else unit.stopDefault();
		return unit;
	}

	@Override
	public void addUnit(Unit unit, World world) throws ModelException {
		world.addUnit(unit);
	}

	@Override
	public Set<Unit> getUnits(World world) throws ModelException {
		return world.getUnits();
	}

	@Override
	public boolean isCarryingLog(Unit unit) throws ModelException {
		return (unit.isCarrying() && unit.getLog() != null);
	}

	@Override
	public boolean isCarryingBoulder(Unit unit) throws ModelException {
		return (unit.isCarrying() && unit.getBoulder()!= null);
	}

	@Override
	public boolean isAlive(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getExperiencePoints(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void workAt(Unit unit, int x, int y, int z) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public Faction getFaction(Unit unit) throws ModelException {
		return unit.getFaction();
	}

	@Override
	public Set<Unit> getUnitsOfFaction(Faction faction) throws ModelException {
		return faction.getUnits();
	}

	@Override
	public Set<Faction> getActiveFactions(World world) throws ModelException {
		return new HashSet<Faction>(world.getFactions());
	}

	@Override
	public double[] getPosition(Boulder boulder) throws ModelException {
		return new double[]{boulder.getPosition().getX(), boulder.getPosition().getY(), boulder.getPosition().getZ()};
	}

	@Override
	public Set<Boulder> getBoulders(World world) throws ModelException {
		return world.getBoulders();
	}

	@Override
	public double[] getPosition(Log log) throws ModelException {
		return new double[]{log.getPosition().getX(), log.getPosition().getY(), log.getPosition().getZ()};
	}

	@Override
	public Set<Log> getLogs(World world) throws ModelException {
		return world.getLogs();
	}
	
	private static final ArrayList<BlockType> typeList =
			new ArrayList<BlockType>(Arrays.asList(BlockType.AIR, BlockType.ROCK, BlockType.WOOD, BlockType.WORKSHOP));

}
