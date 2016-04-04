package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

public class Block{
	
	public Block (Vector V, BlockType type){
		this.setLocation(V);
		this.setBlockType(type);
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
		return new Vector(this.location);
	}
		
	public boolean isSolid(){
		return(this.getBlockType() == BlockType.WOOD || this.getBlockType() == BlockType.ROCK);
	}
	
	public void setUnitsInCube(Set<Unit> unitlist){
		this.unitsInCube = unitlist;
	}
	
	public Set<Unit> getUnitsInCube(){
		return new HashSet<Unit>(this.unitsInCube);
	}
	
	protected void addUnit(Unit unit){
		this.unitsInCube.add(unit);
	}
	
	protected void removeUnit(Unit unit){
		this.unitsInCube.remove(unit);
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
	
	public Set<Boulder> getBouldersInCube(){
		return new HashSet<Boulder>(this.bouldersInCube);
	}
	
	public Set<Log> getLogsInCube(){
		return new HashSet<Log>(this.logsInCube);
	}
	
	protected void addBoulder(Boulder boulder){
		this.bouldersInCube.add(boulder);
	}
	
	protected void addLog(Log log){
		this.logsInCube.add(log);
	}
	
	protected void removeBoulder(Boulder boulder){
		this.bouldersInCube.remove(boulder);
	}
	
	protected void removeLog(Log log){
		this.logsInCube.remove(log);
	}
	
	protected void setBouldersInCube(Set<Boulder> newSet){
		this.bouldersInCube = newSet;
	}
	
	protected void setLogsInCube(Set<Log> newSet){
		this.logsInCube = newSet;
	}
	
	private Vector location;
	
	private BlockType blocktype;
	
	private Set<Unit> unitsInCube = new HashSet<Unit>();
	
	private Set<Boulder> bouldersInCube = new HashSet<Boulder>();
	
	private Set<Log> logsInCube = new HashSet<Log>();
	
	
	
	//private Item itemInCube
}
// Wijzigingen: getSolid, setSolid --> isSolid
//				unitsInCube --> Set ipv ArrayList
//				unitsPresent verwijderd (overbodig)
//				suspended verwijderd (overbodig)
//				constructor versimpeld