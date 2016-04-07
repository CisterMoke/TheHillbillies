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
import hillbillies.part2.listener.TerrainChangeListener;

public class World {
	
	/**
	 * Creates a new World
	 * @param intialGameWorld : Map containing the Blocks of each position in the to be created world
	 */
	public World(Map<ArrayList<Integer>, Block> initialGameWorld, TerrainChangeListener listener){
		this.setGameWorld(initialGameWorld);
		this.WORLD_BORDER = new ArrayList<Integer>(Arrays.asList(50, 50, 50));
		for(ArrayList<Integer> key : initialGameWorld.keySet()){
			if(initialGameWorld.get(key).isSolid())
				this.addSolidBlock(initialGameWorld.get(key));
		}
		this.checkWorldForCollapse(this.getSolidBlocks());
		this.createPositionList();
		this.modelListener=listener;
	}
	
	public World(int sizeX, int sizeY, int sizeZ, Map<ArrayList<Integer>, Block> terrain, TerrainChangeListener modelListener2){
		this.setGameWorld(terrain);
		this.WORLD_BORDER = new ArrayList<Integer>(Arrays.asList(sizeX, sizeY, sizeZ));
		System.out.println("for start");
		Set<Block> checkSet = new HashSet<Block>();
		for(ArrayList<Integer> key : terrain.keySet()){
			Block block = terrain.get(key);
			if(block.isSolid()){
				this.addSolidBlock(block);
				if(this.isAtBorder(block))
					this.addToStableSet(block);
			}
			else {
				for(Block adjacent : this.getDirectlyAdjacent(block)){
					if(adjacent.isSolid())
						checkSet.add(adjacent);
				}
			}
		}
		System.out.println("for stop");
		checkSet.removeAll(this.getStableSet());
		this.checkWorldForCollapse(checkSet);
		this.createPositionList();
		this.modelListener=modelListener2;
	}
	
	public void advanceTime(double dt){
		for(Unit unit : this.getUnits())
			unit.advanceTime(dt);
		for(Boulder boulder : this.getBoulders())
			boulder.advanceTime(dt);
		for(Log log : this.getLogs())
			log.advanceTime(dt);
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
			if (pos.get(i)>= this.WORLD_BORDER.get(i) || pos.get(i) <0)
				checker = false;
		}
		return checker;
	}
	
	public boolean isValidPosition(Vector pos){
		boolean checker = true;
		for(int i=0; i<3; i++){
			if (pos.getCoeff().get(i)>= this.WORLD_BORDER.get(i) || pos.getCoeff().get(i) <0)
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
		V.setBlockType(BlockType.AIR);
		this.removeSolidBlock(V);
		this.modelListener.notifyTerrainChanged((int)(V.getLocation().getX()), (int)(V.getLocation().getY()), (int)(V.getLocation().getZ()));
		this.stableSet.clear();
		for(Block block : this.getDirectlyAdjacent(V)){
			if(block.isSolid() && !this.stableSet.contains(block)){
				this.updateCollapseAt(block);
			}
		}
		Set<Block> newStableSet = this.getSolidBlocks();
		newStableSet.removeAll(this.collapseSet);
		this.setStableSet(newStableSet);
		this.collapse();
	}
	/**
	 * Find the coordinate with the highest absolute value of a given block
	 * @param V : Block for which the largest coordinate must be found
	 * @return integer value of the coordinate of the given block with highest absolute value
	 */
	public int maxBlockCoord(Block V){
		int max = -1;
		for (double coord : V.getLocation().getCoeff()){
			if (coord > max){
				max = (int)(coord);
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
		int min = (int)(V.getLocation().getX());
		for (double coord : V.getLocation().getCoeff()){
			if (coord < min){
				min = (int)(coord);
			}
		}
		return min;
	}
	
	public void collapse(){
		double spawnChance = 0.25;
		for (Block entry : collapseSet){
			double spawnRoll = Math.random();
			if (spawnRoll<spawnChance){
				this.spawnObject(entry);
			}
			entry.setBlockType(BlockType.AIR);
			for (Unit unit : this.getUnits()){
				if(unit.getPath().contains(entry)){
					unit.clearPath();
					unit.move2(unit.getFinTarget());
				}
			}
			this.modelListener.notifyTerrainChanged(
					(int)(entry.getLocation().getX()), (int)(entry.getLocation().getY()), (int)(entry.getLocation().getZ()));	
		}
		this.solidBlocks.removeAll(this.collapseSet);
		this.collapseSet.clear();
	}
	
	public void spawnObject(Block block){
		int weight = (int) (40*Math.random()) + 10;
		if (block.getBlockType()==BlockType.ROCK){
			double x = block.getLocation().getX()+0.5;
			double y = block.getLocation().getY()+0.5;
			double z = block.getLocation().getZ()+0.5;
			this.addBoulder(new Boulder(x, y, z, weight));
		}
		if (block.getBlockType()==BlockType.WOOD){
			double x = block.getLocation().getX()+0.5;
			double y = block.getLocation().getY()+0.5;
			double z = block.getLocation().getZ()+0.5;
			this.addLog(new Log(x, y, z, weight));
		}
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
		for(double coeff : pos.getCoeff()){
			key.add((int)(coeff));
		}
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
		ArrayList<Integer> clone1 = new ArrayList<Integer>();
		ArrayList<Integer> clone2 = new ArrayList<Integer>();
		for(double coord : V.getLocation().getCoeff()){
			clone1.add((int)(coord));
			clone2.add((int)(coord));
		}
		for(int i=0; i<3; i++){
			clone1.set(i, clone1.get(i)+1);
			if (this.isValidPosition(clone1)){
				adjacent.add(this.getBlockAtPos(clone1));
			clone1.set(i, clone1.get(i) -1);
			}
			clone2.set(i, clone2.get(i)-1);
			if (this.isValidPosition(clone2)){
				adjacent.add(this.getBlockAtPos(clone2));
				clone2.set(i, clone2.get(i) + 1);
			}

		}
		return adjacent;
	}
	
	public ArrayList<Block> getAdjacent(Block V){
		ArrayList<Block> adjacent = new ArrayList<Block>();
		for (int i=-1; i<2; i++){
			for (int j=-1; j<2; j++){
				for (int k=-1; k<2; k++){
					Vector check = new Vector(i, j, k);
					Vector pos = V.getLocation();
					pos.add(check);
					if(!(i==0 && j==0 && k==0) && this.isValidPosition(pos)){
						adjacent.add(this.getBlockAtPos(pos));
					}
				}
			}
		}
		return adjacent;
	}
	
	/**
	 * 
	 * returns a set with Blocks that are known to be connected to the border
	 */
	public Set<Block> getStableSet(){
		return new HashSet<Block>(this.stableSet);
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
			this.stableSet.add(v);
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
	 * returns a set with Blocks that are not connected to the border and therefore must collapse
	 */
	public Set<Block> getCollapseSet(){
		return new HashSet<Block>(this.collapseSet);
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
		this.collapseSet.add(V);
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
	
	public void addUnit(Unit unit){
		unit.setWorld(this);
		this.units.add(unit);
		this.getBlockAtPos(unit.getPosition()).addUnit(unit);
		if(this.getFactions().size() < World.MAX_FACTIONS && unit.getFaction() == null){
			unit.setFaction(new Faction(unit, "Faction " + Integer.toString(this.factions.size() + 1)));
			this.addFaction(unit.getFaction());
		}
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
	
	public void setUnits(Set<Unit> newSet){
		this.units.clear();
		for(Unit unit : newSet)
			this.addUnit(unit);
	}
	
	public void removeUnit(Unit unit){
		unit.removeWorld();
		this.units.remove(unit);
	}
	
	public ArrayList<Faction> getFactions(){
		return new ArrayList<Faction>(this.factions);
	}
	
	public void addFaction(Faction faction){
		if(this.getFactions().size() == World.MAX_FACTIONS)
			return;
		this.factions.add(faction);
		faction.setWorld(this);
		for(Unit unit : faction.getUnits())
			this.addUnit(unit);
	}
	
	public void setFactions(ArrayList<Faction> newList){
		this.factions.clear();
		for(Faction faction : newList)
			this.addFaction(faction);
	}
	
	public void removeFaction(Faction faction){
		for(Unit unit : faction.getUnits())
			this.removeUnit(unit);
		faction.removeWorld();
		this.removeFaction(faction);
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
		if(block.isSolid()){
			return false;
		}
		if(block.getLocation().getZ() == 0)
			return true;
		for(int dx = -1; dx<2; dx++){
			for(int dy = -1; dy<2; dy++){
				for(int dz = -1; dz<2; dz++){
					Vector adjacent = block.getLocation();
					adjacent.add(dx, dy, dz);
					if(isValidPosition(adjacent) && this.getBlockAtPos(adjacent).isSolid())
						return true;
				}
			}
		}
		return false;
	}
	
	public Unit spawnUnit(){
		if(this.getUnits().size() == World.MAX_UNITS)
			return null;
		Collections.shuffle(this.positionList);
		int idx = 0;
		while(!this.isWalkable(this.getBlockAtPos(this.positionList.get(idx)))){
			idx += 1;
			if(idx == this.positionList.size())
				return null;
		}
		ArrayList<Integer> position = this.positionList.get(idx);
		int strength = (int)(Math.random()*75 + 25);
		int weight = (int)(Math.random()*75 + 25);
		int toughness = (int)(Math.random()*75 + 25);
		int agility = (int)(Math.random()*75 + 25);
		Unit unit = new Unit("Billie", position.get(0) + 0.5, position.get(1) + 0.5, position.get(2) + 0.5,
				strength, weight, agility, toughness);
		this.addUnit(unit);
		return unit;
	}
	
	public Set<Boulder> getBoulders(){
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
	
	public void addBoulderAt(Block block, Boulder boulder){
		try {
			boulder.setWorld(this);
			this.boulders.add(boulder);
			block.addBoulder(boulder);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	protected void setBoulders(Set<Boulder> newSet){
		this.boulders.clear();
		for(Boulder boulder : newSet)
			this.addBoulder(boulder);
	}
	
	public void removeBoulder(Boulder boulder){
		this.boulders.remove(boulder);
		boulder.removeWorld();
		this.getBlockAtPos(boulder.getPosition()).removeBoulder(boulder);
	}
	
	public Set<Log> getLogs(){
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
	
	public void addLogAt(Block block, Log log){
		try {
			log.setWorld(this);
			this.logs.add(log);
			block.addLog(log);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	protected void setLogs(Set<Log> newSet){
		this.logs.clear();
		for(Log log : newSet)
			this.addLog(log);
	}
	
	public void removeLog(Log log){
		this.logs.remove(log);
		log.removeWorld();
		this.getBlockAtPos(log.getPosition()).removeLog(log);
	}
	
	public ArrayList<Integer> getBorders(){
		return new ArrayList<Integer>(this.WORLD_BORDER);
	}
	
	
	public void updateCollapseAt(Block startBlock){
		if(!startBlock.isSolid())
			return;
		Set<Block> checked = new HashSet<Block>();
		Set<Block> toBeChecked = new HashSet<Block>();
		toBeChecked.add(startBlock);
		while(!toBeChecked.isEmpty()){
			boolean changed = false;
			toBeChecked.remove(startBlock);
			checked.add(startBlock);
			if(this.isAtBorder(startBlock)){
				this.stableSet.addAll(checked);
				return;
			}
			for(Block adjacent : this.getDirectlyAdjacent(startBlock)){
				if(this.stableSet.contains(adjacent)){
					this.stableSet.addAll(checked);
					return;					
				}
				if(adjacent.isSolid() && !checked.contains(adjacent)){
					toBeChecked.add(adjacent);
					startBlock = adjacent;
					changed = true;
				}
			}
			if(!changed && !toBeChecked.isEmpty()){
				startBlock = toBeChecked.iterator().next();
				changed = true;
			}
		}
		this.collapseSet.addAll(checked);
	}
	
	public ArrayList<ArrayList<Integer>> getPositionList(){
		return new ArrayList<ArrayList<Integer>>(this.positionList);
	}
		
	public Set<Block> getSolidBlocks(){
		return new HashSet<Block>(this.solidBlocks);
	}
	
	public void addSolidBlock(Block block) throws IllegalArgumentException{
		if(!block.isSolid())
			throw new IllegalArgumentException("Non-solid block!");
		this.solidBlocks.add(block);
	}
	
	public void removeSolidBlock(Block block){
		this.solidBlocks.remove(block);
	}
	
	public void setSolidBlocks(Set<Block> newSet){
		this.solidBlocks = new HashSet<Block>(newSet);
	}
	
	public void checkWorldForCollapse(Set<Block> checkSet){
		System.out.println("check start");
		System.out.println(checkSet.size());
		for(Block block : checkSet){
			if(block.isSolid())
				this.updateCollapseAt(block);
			else{
				for(Block adjacent : this.getDirectlyAdjacent(block)){
					if(!this.collapseSet.contains(adjacent) && !this.stableSet.contains(adjacent))
						this.updateCollapseAt(adjacent);
				}
			}
		}
		System.out.println("Done!");
		Set<Block> newStableSet = this.getSolidBlocks();
		newStableSet.removeAll(this.collapseSet);
		this.setStableSet(newStableSet);
	}
	
	public boolean isStable(Block block){
		return this.stableSet.contains(block);
	}
	
	private TerrainChangeListener modelListener;
	private Set<Block> collapseSet = new HashSet<Block>();
	private Map<ArrayList<Integer>, Block> gameWorld = new HashMap<ArrayList<Integer>, Block>();
	private Set<Block> stableSet = new HashSet<Block>();
	private ArrayList<Faction> factions = new ArrayList<Faction>();
	private Set<Unit> units = new HashSet<Unit>();
	private static final int MAX_FACTIONS = 5;
	private static final int MAX_UNITS = 100;
	private ArrayList<ArrayList<Integer>> positionList = new ArrayList<ArrayList<Integer>>();
	private final ArrayList<Integer> WORLD_BORDER;
	private Set<Boulder> boulders = new HashSet<Boulder>();
	private Set<Log> logs = new HashSet<Log>();
	private Set<Block> solidBlocks = new HashSet<Block>();
	

	
}