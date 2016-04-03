package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.model.Block.BlockType;

public class World {
	
	/**
	 * Creates a new World
	 * @param intialGameWorld : Map containing the Blocks of each position in the to be created world
	 */
	public World(Map<ArrayList<Integer>, Block> intialGameWorld){
		this.setGameWorld(intialGameWorld);
	}
	
	/**
	 * Returns whether or not the given position lies within the boundaries.
	 * @param pos
	 * 			The position the be checked.
	 * @return True if every coordinate lies 
	 * 			| result == true
	 * 			| for each element in pos : (
	 * 			| 	if (element > 50 && element < 0)
	 * 			|		then result == false)
	 */
	public boolean isValidPosition(ArrayList<Integer> pos){
		boolean checker = true;
		for(int i=0; i<3; i++){
			if (pos.get(i)>49 || pos.get(i)<0)
				checker = false;
		}
		return checker;
	}

	/**
	 * 
	 * @param newWorld
	 * 			The new GameWorld
	 * 
	 * @post 	GameWorld equals newWorld
	 */
	public void setGameWorld(Map<ArrayList<Integer>, Block> newWorld){
		for (Map.Entry<ArrayList<Integer>, Block> entry : newWorld.entrySet()){
			this.gameWorld.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * switches a block from a solid to a passable, and checks whether this would causes any collapsing
	 * @param V : Block which should be set to passable
	 * @post The BlockType of V is set to AIR and any block that should collapse is added to collapseSet
	 */
	public synchronized void setToPassable(Block V){
		V.setBlockType(BlockType.AIR);
		this.getStableSet().clear();
		ArrayList<Block> Adjacent = new ArrayList<Block>(this.getDirectlyAdjacent(V));
		for (Block element : Adjacent){
			if (element.getSolid()==true && this.inStableSet(element) == false && this.inCollapseSet(element)==false){
				toBeChecked.clear();
				this.getCheckedSet().clear();
				if (this.stillSuspended(element) == false ){
					for (Block item: getCheckedSet()){
						this.addCollapseSet(item);
					}
				}
			}
		}
	//	this.collapse();
	}
	
	public synchronized void checkWorldSuspension(){
		this.getStableSet().clear();
		Set<Block> solidBlocks = new HashSet<Block>();
		for (Block element : this.getGameWorld().values()){
			if (element.getSolid()){
				solidBlocks.add(element);
			}
		}
		for (Block entry : solidBlocks){
			if (this.inStableSet(entry) == false && this.inCollapseSet(entry)==false){
				this.getCheckedSet().clear();
				if (this.stillSuspended(entry) == false ){
					for (Block item: getCheckedSet()){
						this.addCollapseSet(item);
					}
				}
			}
		}
	//	this.collapse();
	}
	
	/**
	 * checks whether a given Block is connected to the edge of the world
	 * @param V : Block for which the check must be done
	 * returns true if V is connected to the border and false otherwise
	 */
	public synchronized boolean stillSuspended(Block V){
		this.addToCheckedSet(V);
		if ((this.maxBlockCoord(V)==49) == false && (this.minBlockCoord(V)==0) == false){
			ArrayList<Block> Adjacent = new ArrayList<Block>(this.getDirectlyAdjacent(V));
			for (Block element : Adjacent){
				if(this.inStableSet(element)==true){
					return true;
				}
				if (element.getSolid() == true && this.inCheckedSet(element)==false){
					toBeChecked.add(element);
					boolean checker = this.stillSuspended(element);
					if (checker == true){
						return true;
					}
					if (checker == false){
						if (toBeChecked.isEmpty() == true){
							return false;
						}
						if(toBeChecked.isEmpty() == false){
							return this.stillSuspended(toBeChecked.get(0));
						}
					}
				}
			}
		}
		if ((this.maxBlockCoord(V)==49) == true || (this.minBlockCoord(V)==0) == true){
			return true;
		}
		toBeChecked.remove(V);
		return false;
	}
	
	
//	public synchronized boolean stillSuspended(Block V){
//		System.out.println("a");
//		this.addToCheckedSet(V);
//		if ((this.maxBlockCoord(V)==49) == false && (this.minBlockCoord(V)==0) == false){
//			ArrayList<Block> Adjacent = new ArrayList<Block>(this.getDirectlyAdjacent(V));
//			for (Block element : Adjacent){
//				if(this.inStableSet(element)==true){
//					return true;
//				}
//				if (element.getSolid() == true && this.inCheckedSet(element)==false){
//					toBeChecked.add(element);
//					return this.stillSuspended(element);
//				}
//			}
//		}
//		if ((this.maxBlockCoord(V)==49) == true || (this.minBlockCoord(V)==0) == true){
//			return true;
//		}
//		toBeChecked.remove(V);
//		if (toBeChecked.isEmpty() == true){
//			return false;
//		}
//		if(toBeChecked.isEmpty() == false){
//			return this.stillSuspended(toBeChecked.get(0));
//		}
//		return false;
//	}
	
	
	
	
	
	/**
	 * Find the coordinate with the highest absolute value of a given block
	 * @param V : Block for which the largest coordinate must be found
	 * @return integer value of the coordinate of the given block with highest absolute value
	 */
	public int maxBlockCoord(Block V){
		int max = -1;
		for (int i=0;i<3;i++){
			if (V.getLocation().get(i)>max == true){
				max = V.getLocation().get(i);
			}
		}
		return max;
	}
	/**
	 * Find the coordinate with the lowest absolute value of a given block
	 * @param V : Block for which the smallest coordinate must be found
	 * @return integer value of the coordinate of the given block with lowest absolute value
	 */
	public int minBlockCoord(Block V){
		int min = 50;
		for (int i=0;i<3;i++){
			if (V.getLocation().get(i)<min){
				min = V.getLocation().get(i);
			}
		}
		return min;
	}
	
	public void collapse(){
		double spawnChance = 0.25;
//		duration function
		for (Block entry : collapseSet){
			entry.setBlockType(BlockType.AIR);
			double spawnRoll = Math.random();
			if (spawnRoll<spawnChance){
				int weight = (int) (40*Math.random()) + 10;
				if (entry.getBlockType()==BlockType.ROCK){
					double x = entry.getLocation().get(0)+0.5;
					double y = entry.getLocation().get(1)+0.5;
					double z = entry.getLocation().get(2)+0.5;
					Boulder boulder = new Boulder(x, y, z, weight);
				}
				if (entry.getBlockType()==BlockType.WOOD){
					double x = entry.getLocation().get(0)+0.5;
					double y = entry.getLocation().get(1)+0.5;
					double z = entry.getLocation().get(2)+0.5;
					Log log = new Log(x, y, z, weight);
				}
			}
		}
		this.getCollapseSet().clear();
	}
	
	/**
	 * 
	 * @param pos
	 * returns the block at the given position
	 */
	public Block getBlockAtPos(ArrayList<Integer> pos){
		return this.getGameWorld().get(pos);
	}
	
	/**
	 * 
	 * returns the map gameWorld 
	 */
	@Basic
	public Map<ArrayList<Integer>, Block> getGameWorld(){
		return this.gameWorld;
	}
	
	/**
	 * 
	 * @param V
	 * 			Block for which the adjacent Blocks must be found
	 * @return	
	 * 			List of Blocks that are directly adjacent to the given Block V
	 */
	public ArrayList<Block> getDirectlyAdjacent(Block V){		
		ArrayList<Block> adjacent = new ArrayList<Block>();
		for(int i=0; i<3; i++){
			ArrayList<Integer> clone1 = new ArrayList<Integer>(V.getLocation());
			clone1.set(i, clone1.get(i)+1);
			if (this.isValidPosition(clone1)){
				adjacent.add(this.getBlockAtPos(clone1));
			}
			ArrayList<Integer> clone2 = new ArrayList<Integer>(V.getLocation());
			clone2.set(i, clone2.get(i)-1);
			if (this.isValidPosition(clone2)){
				adjacent.add(this.getBlockAtPos(clone2));
			}

		}
		return adjacent;
	}
	
	/**
	 * 
	 * returns a set with Blocks that are known to be connected to the border
	 */
	public Set<Block> getStableSet(){
		return this.stableSet;
	}
	
	/**
	 * changes stableSet to the given set
	 * @param newstable
	 * @post The new stableSet will equal the given newChecked
	 */
	public void setStableSet(Set<Block> newstable){
		this.stableSet = newstable;
	}
	 /**
	  * Add given element to stableSet
	  * @param v : element to be added
	  * @post stableSet contains V
	  */
	public void addToStableSet(Block v){
		if(this.inStableSet(v)==false){
			this.getStableSet().add(v);
		}
	}
	
	/**
	 * checks whether a given element is in the set stableSet or not
	 * @param element
	 * returns true if V is in the set stableSet and false if V is not in the set stableSet
	 */
	public boolean inStableSet(Block element){
		return stableSet.contains(element);
	}
	
	/**
	 * 
	 * returns a set with Blocks that have already been checked
	 */
	public Set<Block> getCheckedSet(){
		return checkedSet;
	}
	
	/**
	 * changes checkedSet to the given set
	 * @param newchecked : the new checkedSet
	 * @post The new checkedSet will equal the given newchecked
	 */
	public void setCheckedSet(Set<Block> newchecked){
		checkedSet = newchecked;
	}
	/**
	  * Add given element to checkedSet
	  * @param v : element to be added
	  * @post checkedSet contains V
	  */
	public synchronized void addToCheckedSet(Block v){
		checkedSet.add(v);
	}
	/**
	 * checks whether a given element is in the set checkedSet or not
	 * @param V : Block to be checked
	 * returns true if V is in the set checkedSet and false if V is not in the set checkedSet
	 */
	public boolean inCheckedSet(Block V){
		return this.getCheckedSet().contains(V);
	}
	
	/**
	 * 
	 * returns a set with Blocks that are not connected to the border and therefore must collapse
	 */
	public Set<Block> getCollapseSet(){
		return this.collapseSet;
	}
	/**
	 * changes collapseSet to the given set
	 * @param newcollapse : the new checkedSet
	 * @post The new collapseSet will equal the given newcollapse
	 */
	public void setCollapseSet(Set<Block> newcollapse){
		collapseSet = newcollapse;
	}
	/**
	  * Add given element to collapseSet
	  * @param v : element to be added
	  * @post collapseSet contains V
	  */
	public void addCollapseSet(Block V){
		this.getCollapseSet().add(V);
	}
	/**
	 * checks whether a given element is in the set collapseSet or not
	 * @param V : Block to be checked
	 * returns true if V is in the set collapseSet and false if V is not in the set collapseSet
	 */
	public Boolean inCollapseSet(Block V){
		return this.collapseSet.contains(V);
	}
	
	private Set<Block> collapseSet = new HashSet<Block>();
	protected Map<ArrayList<Integer>, Block> gameWorld = new HashMap<ArrayList<Integer>, Block>();
	private Set<Block> stableSet = new HashSet<Block>();
	private Set<Block>  checkedSet = new HashSet<Block>();
	private ArrayList<Block>  toBeChecked = new ArrayList<Block>();

	
}