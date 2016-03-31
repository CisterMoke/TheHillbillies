package hillbillies.part1.facade;

import java.util.Map;

import hillbillies.model.Unit;
import hillbillies.model.Unit.State;
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
		unit.moveTo(cube[0], cube[1], cube[2]);
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

}
