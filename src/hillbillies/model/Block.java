package hillbillies.model;

import java.util.ArrayList;
import java.util.Map;

public class Block extends World{
	
	public Block (Vector V, BlockType type, boolean sus, ArrayList<Unit> unitlist){
		this.setLocation(V);
		this.setBlockType(type);
		this.setSuspended(sus);
		this.setSolid();
		this.setUnitsInCube(unitlist);
	}
	
	/**
	 * 
	 * @param newlocation
	 * 
	 * @post new.location == newlocation
	 */
	public void setLocation(Vector newlocation){
		this.location = newlocation;
	}
	
	/**
	 * 
	 * returns location of this block
	 */
	public Vector getLocation(){
		return this.location;
	}
	/**
	 * 
	 * returns whether the current block is suspended or not
	 */
	public boolean getSuspended(){
		return Suspended;
	}
	/**
	 * 
	 * @param isSuspendedTrue
	 * 
	 * @post 
	 */
	public void setSuspended(Boolean isSuspendedTrue){
		this.Suspended = isSuspendedTrue;
	}
	
	public boolean getSolid(){
		this.setSolid();
		return Solid;
		
	}
	
	public void setSolid(){
		if (this.getBlockType() == BlockType.WOOD ||this.getBlockType() ==BlockType.ROCK){
			this.Solid = true;
		}
		else{
			this.Solid = false;
		}
	}
	
	public void setUnitsInCube(ArrayList<Unit> unitlist){
		this.unitsInCube = unitlist;
	}
	
	public ArrayList<Unit> getUnitsInCube(){
		return this.unitsInCube;
	}
	
	public boolean unitsPresent(){
		if (this.unitsInCube.isEmpty() == true){
			return false;
		}
		return true;
	}
	
	public void addUnits(Unit unit){
		this.unitsInCube.add(unit);
	}
	
	public BlockType getBlockType(){
		return this.blocktype;
	}
	
	public void setBlockType(BlockType newtype){
		this.blocktype = newtype;
	}

	public enum BlockType{
		AIR, WORKSHOP, ROCK, WOOD
	}
	
	private Vector location;
	
	private BlockType blocktype;
	
	private boolean Suspended = false;
	
	private boolean Solid = false;
	
	private ArrayList<Unit> unitsInCube = new ArrayList<Unit>();
	
	
	//private Item itemInCube
}
