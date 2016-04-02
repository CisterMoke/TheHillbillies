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
	
	public World(Map<ArrayList<Integer>, Block> intialGameWorld){
		this.setGameWorld(intialGameWorld);
		this.createPositionList();
		this.WORLD_BORDER = new ArrayList<Integer>(Arrays.asList(50, 50, 50));
	}
	public World(){
		
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
	 * @post 	new.getGameWorld() == newWorld
	 */
	public void setGameWorld(Map<ArrayList<Integer>, Block> newWorld){
		for (Map.Entry<ArrayList<Integer>, Block> entry : newWorld.entrySet()){
			this.gameWorld.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * switches a block from a solid to a passable, and checks whether this would causes any collapsing
	 * @param V
	 */
	public void setToPassable(ArrayList<Integer> V){
		this.getBlockAtPos(V).setBlockType(BlockType.AIR);
		this.getChecked().clear();
		ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacent(V));
		for (int i=0; i<6; i++){
			if (this.getBlockAtPos(Adjacent.get(i)).isSolid()==true && this.inChecked(Adjacent.get(i)) == false && this.inCollapseSet(Adjacent.get(i))==false){
				this.getCurrentChecked().clear();
				if (this.stillSuspended(Adjacent.get(i)) == false ){
					for (ArrayList<Integer> item: getCurrentChecked()){
						this.addCollapseSet(item);
					}
				}
			}
		}
	}
	
	/**
	 * checks whether a given Block is connected to the edge of the world
	 * @param V
	 * returns true if V is connected to the border
	 */
	public boolean stillSuspended(ArrayList<Integer> V){
		this.addToCurrentChecked(V);
		if ((this.Max(V)==49) == false && (this.Min(V)==0) == false){
			ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacent(V));
			for (int i=0; i<6; i++){
				if(this.inChecked(Adjacent.get(i))==true){
					return true;
				}
				if (this.getBlockAtPos(Adjacent.get(i)).isSolid() == true && this.inCurrentChecked(Adjacent.get(i))==false){
					return stillSuspended(Adjacent.get(i));
				}
			}
		}
		if ((this.Max(V)==49) == true || (this.Min(V)==0) == true){
			return true;
		}
		return false;
	}
	
	public int Max(ArrayList<Integer> V){
		int max = -1;
		for (int i=0;i<3;i++){
			if (V.get(i)>max){
				max = V.get(i);
			}
		}
		return max;
	}
	
	public int Min(ArrayList<Integer> V){
		int min = 50;
		for (int i=0;i<3;i++){
			if (V.get(i)<min){
				min = V.get(i);
			}
		}
		return min;
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
	 * @param v
	 * 			Position of the cube for which the directly adjacent cubes must be found
	 * @return	
	 * 			List of ArrayList<Integer> indicating the positions of the adjacent cubes of cube at position V
	 */
	public ArrayList<ArrayList<Integer>> getDirectlyAdjacent(ArrayList<Integer> V){		
		ArrayList<ArrayList<Integer>> adjacent = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<3; i++){
			ArrayList<Integer> clone1 = new ArrayList<Integer>(V);
			clone1.set(i, clone1.get(i)+1);
			adjacent.add(clone1);
			ArrayList<Integer> clone2 = new ArrayList<Integer>(V);
			clone2.set(i, clone2.get(i)-1);
			adjacent.add(clone2);

		}
		return adjacent;
	}
	
	
	/**
	 * 
	 * returns the list with postitions that have already been checked for collapsing
	 */
	public Set<ArrayList<Integer>> getChecked(){
		return this.checked;
	}
	
	/**
	 * sets the checked-list to a given list
	 * @param newchecked
	 * @post new.checked == newchecked
	 */
	public void setChecked(Set<ArrayList<Integer>> newchecked){
		this.checked = newchecked;
	}
	 /**
	  * add given element to checked-list
	  * @param v
	  * @post this.getChecked.contains(V)==true
	  */
	public void addToChecked(ArrayList<Integer> v){
		if(this.inChecked(v)==false){
			this.getChecked().add(v);
		}
	}
	
	/**
	 * checks whether a given ellement is in the list Checked
	 * @param V
	 * returns true if V is already in Checked
	 */
	public boolean inChecked(ArrayList<Integer> V){
		return checked.contains(V);
	}
	

	public Set<ArrayList<Integer>> getCurrentChecked(){
		return currentChecked;
	}
	
	public void setCurrentChecked(Set<ArrayList<Integer>> newchecked){
		currentChecked = newchecked;
	}
	
	public void addToCurrentChecked(ArrayList<Integer> v){
		this.getCurrentChecked().add(v);
	}
	
	public boolean inCurrentChecked(ArrayList<Integer> V){
		return this.getCurrentChecked().contains(V);
	}

	
	/**
	 * 
	 * returns the set containing all the positions of the blocks that should collapse.
	 */
	public Set<ArrayList<Integer>> getCollapseSet(){
		return this.collapseSet;
	}
	
	public Boolean inCollapseSet(ArrayList<Integer> V){
		return this.collapseSet.contains(V);
	}
	
	public void setCollapseSet(Set<ArrayList<Integer>> V){
		collapseSet = V;
	}
	
	public void addCollapseSet(ArrayList<Integer> V){
		this.getCollapseSet().add(V);
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
	
//	private ArrayList<ArrayList<Integer>> currentGroup = new ArrayList<ArrayList<Integer>>();
	private Set<ArrayList<Integer>> collapseSet = new HashSet<ArrayList<Integer>>();
	private Map<ArrayList<Integer>, Block> gameWorld = new HashMap<ArrayList<Integer>, Block>();
	private Set<ArrayList<Integer>> checked = new HashSet<ArrayList<Integer>>();
	private Set<ArrayList<Integer>>  currentChecked = new HashSet<ArrayList<Integer>>();
	private ArrayList<Faction> factions = new ArrayList<Faction>();
	private Set<Unit> units = new HashSet<Unit>();
	private static final int MAX_FACTIONS = 5;
	private static final int MAX_UNITS = 100;
	private ArrayList<ArrayList<Integer>> positionList = new ArrayList<ArrayList<Integer>>();
	private final ArrayList<Integer> WORLD_BORDER;
	private Set<Boulder> boulders = new HashSet<Boulder>();
	private Set<Log> logs = new HashSet<Log>();
	

	
}