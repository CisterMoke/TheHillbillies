package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import hillbillies.model.Block.BlockType;

public class TryStuff {
	
	public static void main(String[] args){
		System.out.println("online");
		Map<ArrayList<Integer>, Block> testmap = new HashMap<ArrayList<Integer>, Block>();
		for (int i=0; i<50;i++){
			for (int j=0; j<50;j++){
				for (int k=0; k<50;k++){
					ArrayList<Unit> units = new ArrayList<Unit>();
					ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, k));
					Block B = new Block(locationArray, BlockType.AIR, false, units);
					testmap.put(locationArray, B);
				}
			}
		}
		System.out.println("build");
		World world = new World(testmap);		
		
		
		System.out.println("start");
		
//		world.checkWorldSuspension();
		
		for (int i=20; i<31; i++){
			for (int j=20; j<31; j++){
				for (int k=0; k<25; k++){
					ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, k));
					world.getBlockAtPos(locationArray).setBlockType(BlockType.ROCK);
				}
			}
		}
		for (int i=20; i<31; i++){
			for (int j=20; j<31; j++){
				ArrayList<Integer> locationArray = new ArrayList<Integer>(Arrays.asList(i, j, 0));
				world.setToPassable(world.getBlockAtPos(locationArray));
			}
		}
//		ArrayList<Integer> pos = new ArrayList<Integer>(Arrays.asList(20, 20, 0));
//		world.setToPassable(world.getBlockAtPos(pos));
//		System.out.println(world.getCollapseSet().size());
		
		Set<Block> solidBlocks = new HashSet<Block>();
		for (Block element : world.getGameWorld().values()){
			if (element.getSolid()){
				solidBlocks.add(element);
			}
		}
		System.out.println(world.getCollapseSet().size());
		System.out.println(solidBlocks.size());
		
			
		
//		ArrayList<Integer> V = new ArrayList<Integer>(Arrays.asList(25, 25, 25));
//		
//		ArrayList<Block> Adjacent = new ArrayList<Block>(world.getDirectlyAdjacent(world.getBlockAtPos(V)));
//		for (Block element : Adjacent){
//			System.out.println(element.getLocation());
//			world.setToPassable(element);
//		}
		
		ArrayList<ArrayList<Integer>> locationArray = new ArrayList<ArrayList<Integer>>();
		for (Block element : world.getCollapseSet()){
			locationArray.add(element.getLocation());
		}
		System.out.println(locationArray);
		System.out.println(world.getCollapseSet().size());
		System.out.println("expected = 2904");
//		for (Block element : world.getDirectlyAdjacent(world.getBlockAtPos(pos))){
//			System.out.println(world.inStableSet(element));
//		}
//		
		
		System.out.println("offline");
	}
}
