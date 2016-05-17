package sandbox;

import java.util.*;

import hillbillies.model.*;
import hillbillies.model.Vector;
import hillbillies.model.Block.BlockType;
import hillbillies.model.expression.*;
import hillbillies.model.statement.*;
import hillbillies.part2.listener.TerrainChangeListener;

public class sandbox {
	public static void main(String[] args){
		Object test = new PositionLiteral(new Vector(1, 2, 3));
		BooleanLiteral tost = (BooleanLiteral) test;
		System.out.println(tost.getValue());
		Scheduler s = new Scheduler();
		s.scheduleTask(new Task("a", 5, new Break(), null));
		s.scheduleTask(new Task("b", 50, new Break(), null));
		s.scheduleTask(new Task("c", -6, new Break(), null));
		s.scheduleTask(new Task("d", 50, new Break(), null));
		Iterator<Task> iter = s.iterator();
		while(iter.hasNext()){
			Task next = iter.next();
			System.out.println(next.getPriority() + " : " + next.getName());
		}
		Unit testUnit = new Unit("Billie", 0.5, 0.5, 0.5, 50, 50, 50, 50);
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
		World testWorld = new World(50, 50, 50, testmap, modelLinstener);
		testWorld.addUnit(testUnit);
		Unit u1 = testWorld.spawnUnit();
		Unit u2 = testWorld.spawnUnit();
		Unit u3 = testWorld.spawnUnit();
		Unit u4 = testWorld.spawnUnit();
		Unit u5 = testWorld.spawnUnit();
		System.out.println(u1.getPosition().getCoeff() + ", "
				+ u2.getPosition().getCoeff() + ", "
						+ u3.getPosition().getCoeff() + ", "
				+ u4.getPosition().getCoeff() + ", "
						+ u5.getPosition().getCoeff());
		System.out.println(u1.getPosition().distance(testUnit.getPosition()) + ", "
		+ u2.getPosition().distance(testUnit.getPosition()) + ", "
				+ u3.getPosition().distance(testUnit.getPosition()) + ", "
		+ u4.getPosition().distance(testUnit.getPosition()) + ", "
				+ u5.getPosition().distance(testUnit.getPosition()));
		System.out.println(testUnit.getClosestEnemy().getPosition().distance(testUnit.getPosition()));
		System.out.println(testUnit.getClosestFriend().getPosition().distance(testUnit.getPosition()));
		System.out.println(testUnit.getClosestUnit().getPosition().distance(testUnit.getPosition()));
		
	}

}
