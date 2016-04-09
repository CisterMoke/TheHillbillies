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

/**
 * A class representing a Hillbilly World.
 * @author 	Joost Croonen & Ruben Dedoncker
 * 
 * @invar	A World can contain up to 100 Units.
 * 
 * @invar	A World can contain up to 5 Factions.
 *
 */
public class World {
	
	/**
	 * Initialize a World with a given size in the x-, y-, and z-direction,
	 * 	a map containing the Blocks of the World, listed by their position
	 * 	and a TerrainChangeListener used to notify when the World's geographical
	 * 	layout has changed.
	 * @param 	sizeX
	 * 			The given size in the x-direction.
	 * @param 	sizeY
	 * 			The given size in the y-direction.
	 * @param 	sizeZ
	 * 			The given size in the z-direction.
	 * @param 	terrain
	 * 			The given map containing all the Blocks
	 * 			of this world.
	 * @param 	modelListener
	 * 			The given TerrainChangeListener
	 * @post	The given terrain is set as the gameWorld.
	 * @post	The borders of this World are set using the
	 * 			given size of the World.
	 * @post	The terrain is used to create a set containing
	 * 			all the solid Blocks.
	 * @effect	The World is checked for collapse using a set
	 * 			containing all the solid blocks directly adjacent
	 * 			to a passable block.
	 * @post	A list containing all the positions of the Blocks
	 * 			is created.
	 * @post	This World's modelListener is set to the given
	 * 			TerrainChangeListener.
	 */
	public World(int sizeX, int sizeY, int sizeZ, Map<ArrayList<Integer>, Block> terrain, TerrainChangeListener modelListener){
		this.WORLD_BORDER = new ArrayList<Integer>(Arrays.asList(sizeX, sizeY, sizeZ));
		this.setGameWorld(terrain);
		System.out.println("for start");
		Set<Block> checkSet = new HashSet<Block>();
		for(ArrayList<Integer> key : terrain.keySet()){
			Block block = terrain.get(key);
			if(block.isSolid()){
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
		this.positionList = new ArrayList<ArrayList<Integer>>(terrain.keySet());
		this.modelListener=modelListener;
	}
	
	/**
	 * Update the Units, Boulders and Logs of this World using the given time interval.
	 * @param 	dt
	 * 			The given time interval.
	 * @effect	The Unit's, Boulder's and Log's advanceTime methods are called.
	 * @throws IllegalArgumentException
	 * 			An exception is thrown if the given time interval is smaller than
	 * 			0 or bigger than 0.2 seconds.
	 */
	public void advanceTime(double dt){
		if (dt<0 || dt>0.2)
			throw new IllegalArgumentException("Invalid time interval!");
		for(Unit unit : this.getUnits())
			unit.advanceTime(dt);
		for(Boulder boulder : this.getBoulders())
			boulder.advanceTime(dt);
		for(Log log : this.getLogs())
			log.advanceTime(dt);
	}
	
	/**
	 * Return a boolean stating whether or not the given position
	 * 	lies within the boundaries.
	 * @param 	pos
	 * 			The position the be checked.
	 * @return 	True if every coordinate lies within the borders of this World.
	 */
	public boolean isValidPosition(ArrayList<Integer> pos){
		boolean checker = true;
		for(int i=0; i<3; i++){
			if (pos.get(i)>= this.getBorders().get(i) || pos.get(i) <0)
				checker = false;
		}
		return checker;
	}
	
	/**
	 * Return a boolean stating whether or not the given position Vector
	 * 	lies within the boundaries.
	 * @param 	pos
	 * 			The position Vector the be checked.
	 * @return 	True if every coefficient of the given Vector
	 * 			lies within the borders of this World.
	 */
	public boolean isValidPosition(Vector pos){
		boolean checker = true;
		for(int i=0; i<3; i++){
			if (pos.getCoeff().get(i)>= this.getBorders().get(i) || pos.getCoeff().get(i) <0)
				checker = false;
		}
		return checker;
	}
	
	/**
	 * 
	 * Return the gameWorld. A gameWorld is a map
	 * 	containing all the Blocks, listed by their position.
	 */
	@Basic
	public Map<ArrayList<Integer>, Block> getGameWorld(){
		return this.gameWorld;
	}

	/**
	 * Set the gameWorld to the given gameWorld.
	 * @param newWorld
	 * 			The given gameWorld to be set as the new gameWorld.
	 * 
	 * @post 	The new gameWorld equals the given gameWorld.
	 * @throws	IllegalArgumentException
	 * 			An exception is thrown if this World already has
	 * 			a gameWorld or if the World of a Block of the given
	 * 			gameWorld can't be set to this one.
	 */
	public void setGameWorld(Map<ArrayList<Integer>, Block> newWorld)
			throws IllegalArgumentException{
		if(!this.gameWorld.isEmpty())
			throw new IllegalArgumentException("This World already has a gameWorld!");
		for (Map.Entry<ArrayList<Integer>, Block> entry : newWorld.entrySet()){
			try{
				entry.getValue().setWorld(this);
			}
			catch(IllegalArgumentException exc){
				this.gameWorld.clear();
				throw new IllegalArgumentException("The given World contains invalid Blocks!");
			}
			this.gameWorld.put(entry.getKey(), entry.getValue());
		}
	}
//	/**
//	 * 
//	 * @param block
//	 */
//	private void addBlock(Block block){
//		block.setWorld(this);
//		if(block.isSolid())
//			this.addSolidBlock(block);
//	}
	
	/**
	 * Switch a Block from a solid to a passable
	 * 	and check whether this results in a collapse.
	 * @param 	block
	 * 			Block which should be set to passable
	 * @post 	The BlockType of the given Block is set
	 * 			to AIR.
	 * @post	All the solid Blocks that are unstable are
	 * 			added to the collapse set. All other solid
	 * 			Blocks are added to the stable set.
	 * @effect	The collapse is initiated.
	 */
	public void setToPassable(Block block){
		block.setBlockType(BlockType.AIR);
		if(this.modelListener != null)
			this.modelListener.notifyTerrainChanged((int)(block.getLocation().getX()), (int)(block.getLocation().getY()), (int)(block.getLocation().getZ()));
		this.stableSet.clear();
		for(Block adjacent : this.getDirectlyAdjacent(block)){
			if(adjacent.isSolid() && !this.stableSet.contains(adjacent)){
				this.updateCollapseAt(adjacent);
			}
		}
		Set<Block> newStableSet = this.getSolidBlocks();
		newStableSet.removeAll(this.collapseSet);
		this.setStableSet(newStableSet);
		this.collapse();
	}

	/**
	 * Collapse all the Blocks in the collapse set.
	 * @post	All the BlockTypes of the Blocks in
	 * 			the current collapse set are set to AIR.
	 * @effect	For every Block that collapses, there is a
	 * 			one in four chance that a Boulder or Log
	 * 			is spawned, depending on the BlockType of
	 * 			that Block.
	 * @effect	All the Blocks in the current collapse set
	 * 			are removed from the set containing all the
	 * 			solid Blocks.
	 * @effect	For all the Units that contain a Block of the
	 * 			collapse set in their path, their path gets
	 * 			removed and they will move to their final target.
	 * @post	The new collapse set is empty.
	 */
	public void collapse(){
		double spawnChance = 0.25;
		for (Block entry : collapseSet){
			double spawnRoll = Math.random();
			if (spawnRoll<spawnChance){
				this.spawnObject(entry);
			}
			entry.setBlockType(BlockType.AIR);
			if(this.modelListener != null)
				this.modelListener.notifyTerrainChanged(
						(int)(entry.getLocation().getX()), (int)(entry.getLocation().getY()), (int)(entry.getLocation().getZ()));	
		}
//		this.solidBlocks.removeAll(this.collapseSet);
		for (Unit unit : this.getUnits()){
			for(Block step : unit.getPath())
				if(this.collapseSet.contains(step)){
					unit.clearPath();
					unit.move2(unit.getFinTarget());
			}
		}
		this.collapseSet.clear();
	}
	
	/**
	 * Spawn a Boulder or Log in the given Block.
	 * @param 	block
	 * 			The given Block in which a Boulder or
	 * 			Log needs to be spawned.
	 * @post	If the given Block has BlockType ROCK, a
	 * 			Boulder is spawned in its centre.
	 * @post	If the given Block has BlockType WOOD, a
	 * 			Log is spawned in its centre.
	 */
	public void spawnObject(Block block){
		int weight = (int) (41*Math.random()) + 10;
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
	 * Return the Block located at the given position.
	 * @param 	pos
	 * 			The given position.
	 */
	@Basic
	public Block getBlockAtPos(ArrayList<Integer> pos){
		return this.gameWorld.get(pos);
	}
	
	/**
	 * Return the Block located at the given position Vector.
	 * @param 	pos
	 * 			The given position Vector.
	 */
	public Block getBlockAtPos(Vector pos){
		ArrayList<Integer> key = new ArrayList<Integer>();
		for(double coeff : pos.getCoeff()){
			key.add((int)(coeff));
		}
		return this.gameWorld.get(key);
	}
	
	
	
	/**
	 * Return a set of all the Blocks directly adjacent
	 * 	to the given Block.
	 * @param block
	 * 			The given Block for which the
	 * 			directly adjacent Blocks must be found.
	 */
	public ArrayList<Block> getDirectlyAdjacent(Block block){		
		ArrayList<Block> adjacent = new ArrayList<Block>();
		ArrayList<Integer> clone1 = new ArrayList<Integer>();
		ArrayList<Integer> clone2 = new ArrayList<Integer>();
		for(double coord : block.getLocation().getCoeff()){
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
	
	/**
	 * Return a set containing all the Blocks adjacent
	 * 	to the given one.
	 * @param 	block
	 * 			The given Block of which the adjacent
	 * 			Blocks must be found.
	 */
	public ArrayList<Block> getAdjacent(Block block){
		ArrayList<Block> adjacent = new ArrayList<Block>();
		for (int i=-1; i<2; i++){
			for (int j=-1; j<2; j++){
				for (int k=-1; k<2; k++){
					Vector pos = block.getLocation().add(new Vector(i, j, k));
					if(!(i==0 && j==0 && k==0) && this.isValidPosition(pos)){
						adjacent.add(this.getBlockAtPos(pos));
					}
				}
			}
		}
		return adjacent;
	}
	
	/**
	 * Return a set of solid Blocks that
	 * are known to be connected to the border.
	 */
	@Basic
	public Set<Block> getStableSet(){
		return new HashSet<Block>(this.stableSet);
	}
	
	/**
	 * Change the current stableSet to the given set.
	 * @param 	newstable
	 * 			The given set to set as the new stableSet.
	 * @post 	The new stableSet equals the given set.
	 */
	@Basic
	public void setStableSet(Set<Block> newstable){
		this.stableSet = newstable;
	}
	
	 /**
	  * Add given Block to stableSet.
	  * @param 	block
	  * 		The Block to be added.
	  * @post 	The new stableSet contains the given Block.
	  */
	@Basic
	public void addToStableSet(Block block){
			this.stableSet.add(block);
	}
	
	/**
	 * Return a set with solid Blocks that are
	 * 	not connected to the border and therefore must collapse.
	 */
	@Basic
	public Set<Block> getCollapseSet(){
		return new HashSet<Block>(this.collapseSet);
	}
	
	/**
	 * Change the current collapseSet to the given set.
	 * @param 	newcollapse 
	 * 			The given set to be set as the new collapseSet.
	 * @post 	The new collapseSet equals the given set.
	 */
	@Basic
	public void setCollapseSet(Set<Block> newcollapse){
		collapseSet = newcollapse;
	}
	
	/**
	  * Add a given Block to collapseSet
	  * @param 	block
	  * 		The given Block to be added.
	  * @post 	The new collapseSet contains the given Block.
	  */
	@Basic
	public void addCollapseSet(Block block){
		this.collapseSet.add(block);
	}
	
	/**
	 * Return a set containing all the Units of
	 * 	this World.
	 */
	@Basic
	public Set<Unit> getUnits(){
		return new HashSet<Unit>(this.units);
	}
	
	/**
	 * Add a Unit to the set of Units in this World.
	 * @param 	unit
	 * 			The given Unit to be added.
	 * @post	If the given Unit is not allowed to have
	 * 			this World as its World, nothing happens.
	 * @effect	This World is set as the Unit's World,
	 * 			this Unit is added to the set of Units
	 * 			in this World and the Unit is added to
	 * 			the Block this Unit is in.
	 * @effect	Tries to add the Faction of the newly created
	 * 			Unit to the list of Factions. If an IllegalArgumentException
	 * 			is caught, the given Unit is added to the Faction with the
	 * 			smallest amount of Units. Otherwise the given
	 * 			Unit's Faction is added to the set of Factions
	 * 			of this World.
	 * @throws	IllegalArgumentException
	 * 			An exception is thrown if the given Unit can't have this
	 * 			World as its World or if the maximum amount of units has been reached.
	 */
	public void addUnit(Unit unit){
		if(!unit.canHaveAsWorld(this) || this.getUnits().size() >= World.MAX_UNITS)
			throw new IllegalArgumentException("Invalid Unit!");
		unit.setWorld(this);
		this.units.add(unit);
		this.getBlockAtPos(unit.getPosition()).addUnit(unit);
		unit.getFaction().setName("Faction " + Integer.toString(this.factions.size() + 1));
		try{
			this.addFaction(unit.getFaction());
		}
		catch(IllegalArgumentException exc){
			Faction smallestFaction = this.getFactions().get(0);
			for(Faction faction : this.getFactions()){
				if(faction.getUnits().size() < smallestFaction.getUnits().size())
					smallestFaction = faction;
			}
			unit.removeFaction();
			unit.setFaction(smallestFaction);
			if(unit.getFaction() != this.getFactions().get(0))
				unit.startDefault();
		}
			
	}
	
	/**
	 * Change the set of Units of this World to the given set.
	 * @param 	newSet
	 * 			The given set.
	 * @effect	After the current set of Units is cleared, it tries to add
	 * 			every Unit to this World. If an IllegalArgumentException
	 * 			is caught, the set of Units is cleared again.
	 */
	public void setUnits(Set<Unit> newSet){
		this.units.clear();
		for(Unit unit : newSet)
			try{
			this.addUnit(unit);
			} catch(IllegalArgumentException exc){
				this.units.clear();
				return;
			}
	}
	
	/**
	 * Remove a given Unit from this World.
	 * @param 	unit
	 * 			The given Unit to be removed.
	 * @effect	The given Unit's World is removed and
	 * 			the given Unit is removed from the set
	 * 			of Units in this World.
	 */
	public void removeUnit(Unit unit){
		unit.removeWorld();
		this.units.remove(unit);
	}
	
	/**
	 * Return a list containing the Factions of this World.
	 */
	public ArrayList<Faction> getFactions(){
		return new ArrayList<Faction>(this.factions);
	}
	
	/**
	 * Add a given Faction to the list of Factions of this World.
	 * @param 	faction
	 * 			The given Faction to be added.
	 * @effect	The given Faction is added to he list of Factions
	 * 			of this World and this World is set as the World 
	 * 			of the given Faction.
	 * @throws 	IllegalArgumentException
	 * 			An exception is thrown if the given Faction can't
	 * 			have this World as its world or the amount of Factions
	 * 			of this World has reached the maximum allowed amount.
	 */
	public void addFaction(Faction faction){
		if(!faction.canHaveAsWorld(this) || this.getFactions().size() == World.MAX_FACTIONS)
			throw new IllegalArgumentException("Invalid Faction!");
		this.factions.add(faction);
		faction.setWorld(this);
	}
	
	/**
	 * Change the list of Factions of this World to the
	 * 	given list of Factions.
	 * @effect	After the current list of Faction is emptied,
	 * 			every Faction of the given list is added to the
	 * 			list of Factions of this World.
	 */
	public void setFactions(ArrayList<Faction> newList){
		this.factions.clear();
		for(Faction faction : newList)
			try {
				this.addFaction(faction);
			} catch (IllegalArgumentException exc) {
				this.factions.clear();
				return;
			}
	}
	
	/**
	 * Remove a given Faction from the list of Factions
	 * 	of this World.
	 * @param 	faction
	 * 			The given Faction to be removed.
	 * @effect	All the Units from this Faction are
	 * 			removed from this World.
	 * @post	The World of the given Faction is removed.
	 * @post	The new list of Factions of this World
	 * 			doens't contain the given Faction.
	 */
	public void removeFaction(Faction faction){
		for(Unit unit : faction.getUnits())
			this.removeUnit(unit);
		faction.removeWorld();
		this.removeFaction(faction);
	}
	
	/**
	 * Return a boolean stating whether or not a given Block
	 * 	is at located at the border of this World.
	 * @param 	block
	 * 			The given Block to be checked.
	 * @return False if none of the coordinates of the Block
	 * 			are either zero or the biggest integer smaller
	 * 			than the size of this World in the direction of
	 * 			that coordinate.
	 */
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
	
	/**
	 * Return a boolean stating whether or not the given Block
	 * 	is walkable.
	 * @param 	block
	 * 			The given Block to be checked.
	 * @return	True if and only if there exists a solid Block
	 * 			adjacent to the given Block.
	 */
	public boolean isWalkable(Block block){
		if(block.isSolid()){
			return false;
		}
		if(block.getLocation().getZ() == 0)
			return true;
		for(int dx = -1; dx<2; dx++){
			for(int dy = -1; dy<2; dy++){
				for(int dz = -1; dz<2; dz++){
					Vector adjacent = block.getLocation().add(dx, dy, dz);
					if(isValidPosition(adjacent) && this.getBlockAtPos(adjacent).isSolid())
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Spawn a Unit in this World. The Units has random primary
	 * 	stats with a value lying between 25 and 100 inclusively
	 * 	and the Unit's position is set in a randomly chosen walkable
	 * 	block.
	 * @return Returns null if no walkable Blocks are found in this World
	 * 			or if it fails to add a Unit to this World.
	 */
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
		int strength = (int)(Math.random()*76 + 25);
		int weight = (int)(Math.random()*76 + 25);
		int toughness = (int)(Math.random()*76 + 25);
		int agility = (int)(Math.random()*76 + 25);
		Unit unit = new Unit("Billie", position.get(0) + 0.5, position.get(1) + 0.5, position.get(2) + 0.5,
				strength, weight, agility, toughness);
		try{
			this.addUnit(unit);
		} catch(IllegalArgumentException exc){
			return null;
		}
		return unit;
	}
	
	/**
	 * Return the set of Boulders of this World.
	 */
	@Basic
	public Set<Boulder> getBoulders(){
		return new HashSet<Boulder>(this.boulders);
	}
	
	/**
	 * Add a given Boulder to this World.
	 * @param 	boulder
	 * 			The given Boulder to be added.
	 * @effect	Tries to set the given Boulder's World to
	 * 			this World. If it succeeds then the given
	 * 			Boulder is added to the set of Boulders of
	 * 			this World and the Boulder is added to the
	 * 			Block in which it is located.
	 */
	public void addBoulder(Boulder boulder){
		try {
			boulder.setWorld(this);
			this.boulders.add(boulder);
			this.getBlockAtPos(boulder.getPosition()).addBoulder(boulder);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	/**
	 * Change the set of Boulders of this World to the
	 * 	given set.
	 * @param 	newSet
	 * 			The given set.
	 * @effect	After the current set of Boulders is emptied,
	 * 			every Boulder of the given set is added to the
	 * 			set of Boulders of this World.
	 */
	protected void setBoulders(Set<Boulder> newSet){
		this.boulders.clear();
		for(Boulder boulder : newSet)
			this.addBoulder(boulder);
	}
	
	/**
	 * Remove a given Boulder from this World.
	 * @param 	boulder
	 * 			The given Boulder to be removed.
	 * @post	The given Boulder is removed from the set of
	 * 			Boulders and the World of the given Boulder is
	 * 			removed and it is removed from the Block it is
	 * 			located in.
	 */
	public void removeBoulder(Boulder boulder){
		this.boulders.remove(boulder);
		boulder.removeWorld();
		this.getBlockAtPos(boulder.getPosition()).removeBoulder(boulder);
	}
	
	/**
	 * Return the set of Logs of this World.
	 */
	@Basic
	public Set<Log> getLogs(){
		return new HashSet<Log>(this.logs);
	}
	
	/**
	 * Add a given Log to this World.
	 * @param 	log
	 * 			The given Log to be added.
	 * @effect	Tries to set the given Log's World to
	 * 			this World. If it succeeds then the given
	 * 			Log is added to the set of Logs of
	 * 			this World and the Log is added to the
	 * 			Block in which it is located.
	 */
	public void addLog(Log log){
		try {
			log.setWorld(this);
			this.logs.add(log);
			this.getBlockAtPos(log.getPosition()).addLog(log);
		} catch (IllegalArgumentException exc) {
			return;
		}
	}
	
	/**
	 * Change the set of Logs of this World to the
	 * 	given set.
	 * @param 	newSet
	 * 			The given set.
	 * @effect	After the current set of Logs is emptied,
	 * 			every Log of the given set is added to the
	 * 			set of Logs of this World.
	 */
	protected void setLogs(Set<Log> newSet){
		this.logs.clear();
		for(Log log : newSet)
			this.addLog(log);
	}
	
	/**
	 * Remove a given Log from this World.
	 * @param 	log
	 * 			The given Log to be removed.
	 * @post	The given Log is removed from the set of
	 * 			Logs and the World of the given Log is
	 * 			removed and it is removed from the Block it is
	 * 			located in.
	 */
	public void removeLog(Log log){
		this.logs.remove(log);
		log.removeWorld();
		this.getBlockAtPos(log.getPosition()).removeLog(log);
	}
	
	/**
	 * Return a list containing the borders of this world in
	 * 	the x-, y-, and z-direction.
	 */
	@Basic
	public ArrayList<Integer> getBorders(){
		return new ArrayList<Integer>(this.WORLD_BORDER);
	}
	
	/**
	 * Update the collapseSet and the stableSet locally starting
	 * 	at the given Block.
	 * @param 	startBlock
	 * 			The given Block at which the collapseSet and
	 * 			stableSet need to be updated.
	 * @post	Nothing happens if the given Block isn't solid.
	 * @post	If a path between from the given Block a solid
	 * 			Block connected to the border can be formed
	 * 			containing only solid blocks directly adjacent to each other,
	 * 			then all the Blocks in that path are added to the stableSet.
	 * 			Otherwise all the solid Blocks, for which a path can be formed
	 * 			between that Block and the given Blockcontaining only solid
	 * 			Blocks directly adjacent to each other, are added to the
	 * 			collapseSet.
	 */
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
	
	/**
	 * Returns a list containing all the locations of the
	 * 	Blocks of this World.
	 */
	@Basic
	public ArrayList<ArrayList<Integer>> getPositionList(){
		return new ArrayList<ArrayList<Integer>>(this.positionList);
	}
	
	/**
	 * Return a set containing all the solid Blocks of this World.
	 */
	@Basic
	public Set<Block> getSolidBlocks(){
		return new HashSet<Block>(this.solidBlocks);
	}
	
	/**
	 * Add a given solid Block to the set of solid Blocks.
	 * @param 	block
	 * 			The given Block to be added.
	 * @post	The given Block is added to the set of solid
	 * 			Blocks.
	 * @throws 	IllegalArgumentException
	 * 			An exception is thrown if the given Block is not
	 * 			solid or if the Block's World isn't equal to this World.
	 */
	public void addSolidBlock(Block block) throws IllegalArgumentException{
		if(!block.isSolid() || block.getWorld() != this)
			throw new IllegalArgumentException("Non-solid block!");
		this.solidBlocks.add(block);
	}
	
	/**
	 * Remove a given Block from the set of solid Blocks.
	 * @param 	block
	 * 			The given Block to be removed.
	 * @post	The new set of solid Blocks doesn't contain
	 * 			the given Block.
	 */
	@Basic
	public void removeSolidBlock(Block block){
		this.solidBlocks.remove(block);
	}
	
	/**
	 * Change the set of solid Blocks to the given set.
	 * @param 	newSet
	 * 			The given set.
	 * @effect	After the set of solid Blocks is cleared, every
	 * 			Block of the given set is added to the set of 
	 * 			solid Blocks.
	 */
	public void setSolidBlocks(Set<Block> newSet){
		this.solidBlocks.clear();
		for(Block block : newSet)
			this.addSolidBlock(block);
	}
	
	/**
	 * Check this World for possible cave-ins using a given set
	 * 	of Blocks.
	 * @param 	checkSet
	 * 			The given set of Blocks.
	 * @effect	The collapseSet is updated locally at every Block
	 * 			of the given set.
	 * @post	The new stableSet contains all the solid Blocks excluding
	 * 			this in the collapseSet.
	 */
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
	
	/**
	 * Return a boolean stating whether or not the given Block
	 * 	is stable.
	 * @param 	block
	 * 			The given Block to be checked.
	 * @return	True if and only if this World's stableSet contains
	 * 			the given Block.
	 */
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
	private ArrayList<ArrayList<Integer>> positionList;
	private final ArrayList<Integer> WORLD_BORDER;
	private Set<Boulder> boulders = new HashSet<Boulder>();
	private Set<Log> logs = new HashSet<Log>();
	private Set<Block> solidBlocks = new HashSet<Block>();
	

	
}