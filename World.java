package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;

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
		this.emptyChecked();
		ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacnent(V));
		for (int i=0; i<6; i++){
			this.emptyCurrentChecked();
			if (this.stillSuspended(Adjacent.get(i)) == false ){
				this.makeGroup(Adjacent.get(i));
				for (int j=0; j<currentGroup.size(); j++){
					collapseSet.add(currentGroup.get(j));
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
		ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacnent(V));
		for (int i=0; i<6; i++){
			if (this.isValidPosition(Adjacent.get(i)) == true){
				if (this.getBlockAtPos(Adjacent.get(i)).getSolid() == true && this.inCurrentChecked(Adjacent.get(i))==false){
					return stillSuspended(Adjacent.get(i));
				}
//				if(this.inCurrentChecked(Adjacent.get(i))==true){
//					boolean check = collapseSet.contains(Adjacent.get(i));
//					return false;
//				}
			}
			if (this.isValidPosition(Adjacent.get(i)) == false){
				return true;
			}
		}
		return false;
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
	public ArrayList<ArrayList<Integer>> getDirectlyAdjacnent(ArrayList<Integer> V){		
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
	public ArrayList<ArrayList<Integer>> getChecked(){
		return this.checked;
	}
	
	/**
	 * sets the checked-list to a given list
	 * @param newchecked
	 * @post new.checked == newchecked
	 */
	public void setChecked(ArrayList<ArrayList<Integer>> newchecked){
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
		boolean isIn = false;
		for (int i=0; i<this.getChecked().size(); i++){
			if (this.getChecked().get(i).equals(V)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * clears the list Checked
	 */
	public void emptyChecked(){
		ArrayList<ArrayList<Integer>> emptyChecked = new ArrayList<ArrayList<Integer>>();
		this.setChecked(emptyChecked);
	}
	
	public ArrayList<ArrayList<Integer>> getCurrentChecked(){
		return this.checked;
	}
	
	public void setCurrentChecked(Set<ArrayList<Integer>> newchecked){
		this.currentChecked = newchecked;
	}
	
	public void addToCurrentChecked(ArrayList<Integer> v){
		this.getCurrentChecked().add(v);
	}
	
	public boolean inCurrentChecked(ArrayList<Integer> V){
		return this.getCurrentChecked().contains(V);
	}
	
	public void emptyCurrentChecked(){
		Set<ArrayList<Integer>> emptyChecked = new HashSet<ArrayList<Integer>>();
		this.setCurrentChecked(emptyChecked);
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
			ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacnent(currentGroup.get(i)));
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
		ArrayList<ArrayList<Integer>> Adjacent = new ArrayList<ArrayList<Integer>>(this.getDirectlyAdjacnent(V));
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
	 * @param V
	 * returns whether V is in currentgroup or not
	 * 				currentGroup.contains(V)
	 */
	public boolean currentGroupContains(ArrayList<Integer> V){
		return currentGroup.contains(V);
	}
	
	/**
	 * empty currentgroup
	 */
	public void emptyCurrentGroup(){
		ArrayList<ArrayList<Integer>> empty = new ArrayList<ArrayList<Integer>>();
		this.currentGroup = empty;
	}
	
	/**
	 * 
	 * returns the set containing all the positions of the blocks that should collapse.
	 */
	public Set<ArrayList<Integer>> getCollapseSet(){
		return this.collapseSet;
	}
	
	private ArrayList<ArrayList<Integer>> currentGroup = new ArrayList<ArrayList<Integer>>();
	private Set<ArrayList<Integer>> collapseSet = new HashSet<ArrayList<Integer>>();
	protected Map<ArrayList<Integer>, Block> gameWorld = new HashMap<ArrayList<Integer>, Block>();
//	protected Map<Vector, Block> gameWorld = new HashMap<Vector, Block>();
	private ArrayList<ArrayList<Integer>> checked = new ArrayList<ArrayList<Integer>>();
	private Set<ArrayList<Integer>>  currentChecked = new HashSet<ArrayList<Integer>>();
	
//	public ArrayList<Block> checkTotalSuspension(){
//	ArrayList<Vector> emptyChecked = new ArrayList<Vector>();
//	this.setChecked(emptyChecked);
//	for (Map.Entry<Vector, Block> entry : this.getGameWorld().entrySet()){
//		entry.getValue().setSuspended(false);
//	}
//	ArrayList<Block> collapseList = new ArrayList<Block>();
//	for (Map.Entry<Vector, Block> entry : this.getGameWorld().entrySet()){
//		if (this.getGameWorld().get(entry.getKey()).getSolid() == true && this.getGameWorld().get(entry.getKey()).getSuspended() == false && this.inChecked(entry.getKey())==false){
//			checkSuspension(entry.getKey());
//			this.addToChecked(entry.getKey());
//			if (this.getGameWorld().get(entry.getKey()).getSuspended()==false){
//				collapseList.add(this.getGameWorld().get(entry.getKey()));
//			}
//		}
//	}
//	return collapseList;
//}
//
//public void checkSuspension(Vector V){
//	System.out.println("initiate checkSuspension");
//	System.out.println(V);
//	ArrayList<Vector> Adjacent = new ArrayList<Vector>(this.getDirectlyAdjacnent(V));
//	for (int i=0; i<6; i++){
//		System.out.println(Adjacent.get(i));
//		System.out.println("if 1");
//		if (this.isValidPosition(Adjacent.get(i))==false || this.getBlockAtPos(Adjacent.get(i)).getSuspended() == true){
//			System.out.println("in if 1");
//			this.getBlockAtPos(V).setSuspended(true);
//			this.addToChecked(V);
//		}
//		System.out.println("if 2");
//		if (this.getBlockAtPos(Adjacent.get(i)).getSolid() == true){
//			System.out.println("in if 2");
//			this.addToChecked(V);
//			if (this.inChecked(Adjacent.get(i)) == false)
//				checkSuspension(Adjacent.get(i));
//		}
//		else{
//			System.out.println("in else");
//			this.getBlockAtPos(V).setSuspended(false);
//		}
//	}
//}
	
}
