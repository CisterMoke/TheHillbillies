package hillbillies.model;

import java.util.ArrayList;

public class Vector {
	
	public Vector (double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public double getZ(){
		return this.z;
	}
	
	public ArrayList<Double> getCoords(){
		ArrayList<Double> coords = new ArrayList<Double>();
		coords.add(this.x);
		coords.add(this.y);
		coords.add(this.z);
		return coords;
	}
	
	public boolean equals(Vector vector){
		return((this.x == vector.getX()) && (this.y == vector.getY()) && equals(this.z == vector.getZ()));
	}
	
	public double getLength(){
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	public void normalize(){
		this.x /= this.getLength();
		this.y /= this.getLength();
		this.z /= this.getLength();
	}
	
	public void add(Vector vector){
		this.x += vector.getX();
		this.y += vector.getY();
		this.z += vector.getZ();
	}
//	
//	public void add(double x, double y, double z){
//		this.x += x;
//		this.y += y;
//		this.z += z;
//	}
	
	public double scalarProduct(Vector vector){
		return (this.x*vector.getX() + this.y*vector.getY() + this.z*vector.getZ());
	}
	
	public Vector crossProduct(Vector vector){
		double newX = this.y*vector.getZ() - this.z*vector.getX();
		double newY = this.z*vector.getX() - this.x*vector.getZ();
		double newZ = this.x*vector.getY() - this.y*vector.getX();
		
		return new Vector(newX, newY, newZ);
	}
	
	public void multiply(double factor){
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
	}
	
	private double x;
	private double y;
	private double z;

}
