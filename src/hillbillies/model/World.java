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
	
	public World(Map<ArrayList<Integer>, Block> intialGameWorld){
		this.setGameWorld(intialGameWorld);
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
			if (this.getBlockAtPos(Adjacent.get(i)).getSolid()==true && this.inChecked(Adjacent.get(i)) == false && this.inCollapseSet(Adjacent.get(i))==false){
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
				if (this.getBlockAtPos(Adjacent.get(i)).getSolid() == true && this.inCurrentChecked(Adjacent.get(i))==false){
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
	 * this checks the entire gameworld for collapses
	 * NB: this takes a shit load of time to complete and might currently not work because I switched Vectors to ArrayList<Integer>
	 * @return
	 */
	public ArrayList<Block> checkTotalSuspension2(){
		this.emptyChecked();
		for (Map.Entry<ArrayList<Integer>, Block> entry : this.getGameWorld().entrySet()){
			entry.getValue().setSuspended(false);
		}
		for (Map.Entry<ArrayList<Integer>, Block> entry : this.getGameWorld().entrySet()){
			this.emptyCurrentGroup();
			if (this.inChecked(entry.getKey()) == false && this.getBlockAtPos(entry.getKey()).getSolid() == true){
				this.makeGroup(entry.getKey());
				this.checkSuspensionGroup();
			}
		}
		ArrayList<Block> collapseList = new ArrayList<Block>();
		for (Map.Entry<ArrayList<Integer>, Block> entry : this.getGameWorld().entrySet()){
			if (this.getBlockAtPos(entry.getKey()).getSuspended() == false && this.getBlockAtPos(entry.getKey()).getSolid() == true){
				collapseList.add(this.getBlockAtPos(entry.getKey()));
			}
		}
		return collapseList;
	}
	
	/**
	 * this checks if the currentGroup is connected to the border or not, and if not adds the contents of currentgroup to collapseSet
	 */
	public void checkSuspensionGroup(){
		boolean suspended = false;
		for (int i=0; i<currentGroup.size(); i++){
			ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacent(currentGroup.get(i)));
			for (int j=0; j<6; j++){
				if (this.isValidPosition(Adjacent.get(j))==false){
					suspended = true;
				}
			}
		}
		for (int i=0; i<currentGroup.size(); i++){
			this.addToChecked(currentGroup.get(i));
			if (suspended == true){
				this.getBlockAtPos(currentGroup.get(i)).setSuspended(true);
			}
		}
	}

	/**
	 * makes a group containing the positions of all blocks in connection with the block at position V
	 * @param V
	 */
	public void makeGroup(ArrayList<Integer> V){
		currentGroup.add(V);
		ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacent(V));
		for (int i=0; i<6; i++){
			if (this.isValidPosition(Adjacent.get(i)) == true){
				if (this.getBlockAtPos(Adjacent.get(i)).getSolid() == true && this.currentGroupContains(Adjacent.get(i))==false){
					makeGroup(Adjacent.get(i));
				}
			}
		}
		System.out.println(currentGroup);
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
	
//	private ArrayList<ArrayList<Integer>> currentGroup = new ArrayList<ArrayList<Integer>>();
	private Set<ArrayList<Integer>> collapseSet = new HashSet<ArrayList<Integer>>();
	protected Map<ArrayList<Integer>, Block> gameWorld = new HashMap<ArrayList<Integer>, Block>();
	private Set<ArrayList<Integer>> checked = new HashSet<ArrayList<Integer>>();
	private Set<ArrayList<Integer>>  currentChecked = new HashSet<ArrayList<Integer>>();
	

	
}
