package hillbillies.part1.facade;

import java.util.Map;

import hillbillies.model.Unit;
import hillbillies.model.Unit.State;
import ogp.framework.util.ModelException;

public class Facade implements IFacade {

	@Override
	public Unit createUnit(String name, int[] initialPosition, int weight, int agility, int strength, int toughness,
			boolean enableDefaultBehavior) throws ModelException {
		// TODO Auto-generated method stub
		Unit unit = new Unit(name, (double)(initialPosition[0])+0.5 ,(double) (initialPosition[1]) + 0.5,(double) (initialPosition[2]) + 0.5, strength, weight, agility, toughness );
		return unit;
	}

	@Override
	public double[] getPosition(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		double[] pos = new double[]{unit.getPosition().get(0), unit.getPosition().get(1), unit.getPosition().get(2)};
		return pos;
	}

	@Override
	public int[] getCubeCoordinate(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		int[] cubepos = new int[]{unit.getBlockPosition().get(0).intValue(), unit.getBlockPosition().get(1).intValue(), unit.getBlockPosition().get(2).intValue()};
		return cubepos;
	}

	@Override
	public String getName(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		String name = unit.getName();
		return name;
	}

	@Override
	public void setName(Unit unit, String newName) throws ModelException {
		// TODO Auto-generated method stub
		try{
			unit.setName(newName);
		}
		catch (IllegalArgumentException exc){
			throw new ModelException();
		}
	}

	@Override
	public int getWeight(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getPrimStats().get("wgt");
	}

	@Override
	public void setWeight(Unit unit, int newValue) throws ModelException {
		// TODO Auto-generated method stub
		Map<String, Integer> oldStats = unit.getPrimStats();
		oldStats.put("wgt", newValue);
		unit.setPrimStats(oldStats);
		
	}

	@Override
	public int getStrength(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getPrimStats().get("str");
	}

	@Override
	public void setStrength(Unit unit, int newValue) throws ModelException {
		// TODO Auto-generated method stub
		Map<String, Integer> oldStats = unit.getPrimStats();
		oldStats.put("str", newValue);
		unit.setPrimStats(oldStats);
		
	}

	@Override
	public int getAgility(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getPrimStats().get("agl");
	}

	@Override
	public void setAgility(Unit unit, int newValue) throws ModelException {
		// TODO Auto-generated method stub
		Map<String, Integer> oldStats = unit.getPrimStats();
		oldStats.put("agl", newValue);
		unit.setPrimStats(oldStats);
	}

	@Override
	public int getToughness(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getPrimStats().get("tgh");
	}

	@Override
	public void setToughness(Unit unit, int newValue) throws ModelException {
		// TODO Auto-generated method stub
		Map<String, Integer> oldStats = unit.getPrimStats();
		oldStats.put("tgh", newValue);
		unit.setPrimStats(oldStats);
	}

	@Override
	public int getMaxHitPoints(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getMaxHp();
	}

	@Override
	public int getCurrentHitPoints(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (int)(unit.getHp());
	}

	@Override
	public int getMaxStaminaPoints(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getMaxStam();
	}

	@Override
	public int getCurrentStaminaPoints(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (int)(unit.getStam());
	}

	@Override
	public void advanceTime(Unit unit, double dt) throws ModelException {
		// TODO Auto-generated method stub
		unit.advanceTime(dt);
		
	}

	@Override
	public void moveToAdjacent(Unit unit, int dx, int dy, int dz) throws ModelException {
		// TODO Auto-generated method stub
		unit.moveToAdjacent(dx, dy, dz);
	}

	@Override
	public double getCurrentSpeed(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getSpeed();
	}

	@Override
	public boolean isMoving(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (unit.isMoving());
	}

	@Override
	public void startSprinting(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		if (unit.getState() == State.WALKING)
			unit.setState(State.SPRINTING);
	}

	@Override
	public void stopSprinting(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		if(unit.getState() == State.SPRINTING)
			unit.setState(State.IDLE);
		
	}

	@Override
	public boolean isSprinting(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (unit.getState() == State.SPRINTING);
	}

	@Override
	public double getOrientation(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.getTheta();
	}

	@Override
	public void moveTo(Unit unit, int[] cube) throws ModelException {
		// TODO Auto-generated method stub
		unit.moveTo(cube[0], cube[1], cube[2]);
	}

	@Override
	public void work(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		unit.work();
		
	}

	@Override
	public boolean isWorking(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (unit.getState() == State.WORKING);
	}

	@Override
	public void fight(Unit attacker, Unit defender) throws ModelException {
		// TODO Auto-generated method stub
		attacker.attack(defender);
		
	}

	@Override
	public boolean isAttacking(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (unit.getState() == State.COMBAT);
	}

	@Override
	public void rest(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		unit.rest();
	}

	@Override
	public boolean isResting(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return (unit.getState() == State.RESTING);
	}

	@Override
	public void setDefaultBehaviorEnabled(Unit unit, boolean value) throws ModelException {
		// TODO Auto-generated method stub
		if (value){
			unit.startDefault();
		}
		else{
			unit.stopDefault();
		}
		
	}

	@Override
	public boolean isDefaultBehaviorEnabled(Unit unit) throws ModelException {
		// TODO Auto-generated method stub
		return unit.DefaultOn();
	}

}
