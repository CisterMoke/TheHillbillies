package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		this.createPositionList();
		this.WORLD_BORDER = new ArrayList<Integer>(Arrays.asList(50, 50, 50));
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
	 * @post 	new.getGameWorld() equals newWorld
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
	public void setToPassable(Block V){
		System.out.println(V.getLocation());
		V.setBlockType(BlockType.AIR);
		this.getStableSet().clear();
		ArrayList<Block> Adjacent = new ArrayList<Block>(this.getDirectlyAdjacent(V));
		for (Block element : Adjacent){
			if (element.getSolid()==true && this.inStableSet(element) == false && this.inCollapseSet(element)==false){
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
	
	public void checkWorldSuspension(){
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
	public boolean stillSuspended(Block V){
		System.out.println(V.getLocation());
		this.addToCheckedSet(V);
		if ((this.maxBlockCoord(V)==49) == false && (this.minBlockCoord(V)==0) == false){
			ArrayList<Block> Adjacent = new ArrayList<Block>(this.getDirectlyAdjacent(V));
			for (Block element : Adjacent){
				toBeChecked.add(element);
				if(this.inStableSet(element)==true){
					System.out.println("1 true");
					return true;
				}
				if (element.getSolid() == true && this.inCheckedSet(element)==false){
					boolean checker = stillSuspended(element);
					if (toBeChecked.isEmpty()){
						return checker;
					}
				}
			}
		}
		if ((this.maxBlockCoord(V)==49) == true || (this.minBlockCoord(V)==0) == true){
			System.out.println("2 true");
			return true;
		}
		System.out.println("3 false");
		toBeChecked.remove(V);
		return false;
	}
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
	
	public Block getBlockAtPos(Vector pos){
		ArrayList<Integer> key = new ArrayList<Integer>();
		for(double coeff : pos.getCoeff())
			key.add((int)(coeff));
		return this.getGameWorld().get(key);
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
	public void addToCheckedSet(Block v){
		this.getCheckedSet().add(v);
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
	
	
	public Set<Unit> getUnits(){
		return new HashSet<Unit>(this.units);
	}
	
	public ArrayList<Faction> getFactions(){
		return new ArrayList<Faction>(this.factions);
	}
	
	private void createPositionList(){
		for(int x=0 ; x< this.WORLD_BORDER.get(0) ; x++){
			for(int y=0 ; y<this.WORLD_BORDER.get(1) ; y++){
				for(int z=0 ; z<this.WORLD_BORDER.get(2) ; z++){
					ArrayList<Integer> pos = new ArrayList<Integer>();
					pos.add(x);
					pos.add(y);
					pos.add(z);
					this.positionList.add(pos);
				}
			}
		}
	}
	
	public boolean isAtBorder(Block block){
		Vector position = block.getLocation();
		if(position.getX() == 0 || position.getX() == (this.WORLD_BORDER.get(0) - 1) )
			return true;
		if(position.getY() == 0 || position.getY() == (this.WORLD_BORDER.get(1) - 1) )
			return true;
		if(position.getZ() == 0 || position.getZ() == (this.WORLD_BORDER.get(2) - 1) )
			return true;
		return false;
	}
	
	public boolean isWalkable(Block block){
		if(block.isSolid())
			return false;
		if(this.isAtBorder(block))
			return true;
		boolean checker = false;
		for(Block cube : this.getDirectlyAdjacent(block)){
			if(cube.isSolid())
				return true;
		}
		return false;
	}
	
	public void spawnUnit(){
		if(this.getUnits().size() == World.MAX_UNITS)
			return;
		Collections.shuffle(this.positionList);
		int idx = 0;
		while(!this.isWalkable(this.getBlockAtPos(this.positionList.get(idx)))){
			idx += 1;
			if(idx == this.positionList.size())
				return;
		}
		ArrayList<Integer> position = this.positionList.get(idx);
		int strength = (int)(Math.random()*75 + 25);
		int weight = (int)(Math.random()*75 + 25);
		int toughness = (int)(Math.random()*75 + 25);
		int agility = (int)(Math.random()*75 + 25);
		Unit unit = new Unit("Billie", position.get(0), position.get(1), position.get(2), strength, weight, agility, toughness);
		this.units.add(unit);
		if(this.getFactions().size() < World.MAX_FACTIONS)
			unit.setFaction(new Faction(unit, "Faction" + Integer.toString(this.factions.size() + 1)));
		else{
			Faction smallestFaction = this.getFactions().get(0);
			for(Faction faction : this.getFactions()){
				if(faction.getUnits().size() < smallestFaction.getUnits().size())
					smallestFaction = faction;
			}
			unit.setFaction(smallestFaction);
			if(unit.getFaction() != this.getFactions().get(0))
				unit.startDefault();
		}
	}
	
	protected Set<Boulder> getBoulders(){
		return new HashSet<Boulder>(this.boulders);
	}
	
	public void addBoulder(Boulder boulder){
		try {
			boulder.setWorld(this);
			this.boulders.add(boulder);
			this.getBlockAtPos(boulder.getPosition()).addBoulder(boulder);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	public void removeBoulder(Boulder boulder){
		this.boulders.remove(boulder);
		boulder.removeWorld();
		this.getBlockAtPos(boulder.getPosition()).removeBoulder(boulder);
	}
	
	protected Set<Log> getLogs(){
		return new HashSet<Log>(this.logs);
	}
	
	public void addLog(Log log){
		try {
			log.setWorld(this);
			this.logs.add(log);
			this.getBlockAtPos(log.getPosition()).addLog(log);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	public void removeLog(Log log){
		this.logs.remove(log);
		log.removeWorld();
		this.getBlockAtPos(log.getPosition()).removeLog(log);
	}
	
	public ArrayList<Integer> getBorders(){
		return new ArrayList<Integer>(this.WORLD_BORDER);
	}
	
	public void updateCollapseAt(ArrayList<Integer> startPos){
		if(!this.getBlockAtPos(startPos).isSolid())
			return;
		ArrayList<ArrayList<Integer>> checked = new ArrayList<ArrayList<Integer>>();
		this.
		for(ArrayList<Integer> adjacent : this.getDirectlyAdjacent(startPos)){
			checked.add(adjacent);
			this.updateCollapseAt(adjacent, );
			
		}
	}
	
	private Set<Block> collapseSet = new HashSet<Block>();
	protected Map<ArrayList<Integer>, Block> gameWorld = new HashMap<ArrayList<Integer>, Block>();
	private Set<Block> stableSet = new HashSet<Block>();
	private Set<Block>  checkedSet = new HashSet<Block>();
	private Set<Block>  toBeChecked = new HashSet<Block>();
	private ArrayList<Faction> factions = new ArrayList<Faction>();
	private Set<Unit> units = new HashSet<Unit>();
	private static final int MAX_FACTIONS = 5;
	private static final int MAX_UNITS = 100;
	private ArrayList<ArrayList<Integer>> positionList = new ArrayList<ArrayList<Integer>>();
	private final ArrayList<Integer> WORLD_BORDER;
	private Set<Boulder> boulders = new HashSet<Boulder>();
	private Set<Log> logs = new HashSet<Log>();
	

	
}