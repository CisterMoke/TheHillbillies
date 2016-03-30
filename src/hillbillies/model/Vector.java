package hillbillies.model;

import java.util.ArrayList;

public class Vector {
	
	public Vector (double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector (Vector vector){
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
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
	
	public ArrayList<Double> getCoeff(){
		ArrayList<Double> coeff = new ArrayList<Double>();
		coeff.add(this.x);
		coeff.add(this.y);
		coeff.add(this.z);
		return coeff;
	}
	
	public void setCoeff(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setVector(Vector vector){
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
	
	public boolean equals(Vector vector){
		if (this == null || vector == null)
			return false;
		return((this.x == vector.getX()) && (this.y == vector.getY()) && (this.z == vector.getZ()));
	}
	
	public double getLength(){
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	public void normalize(){
		if (this.getLength() == 0)
				return;
		double length = this.getLength();
		this.x /= length;
		this.y /= length;
		this.z /= length;
	}
	
	public void add(Vector vector){
		this.x += vector.getX();
		this.y += vector.getY();
		this.z += vector.getZ();
	}
	
	public void add(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
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
	
	public Vector getOpposite(){
		Vector opposite = new Vector(this);
		opposite.multiply(-1);
		return opposite;
	}
	
	private double x;
	private double y;
	private double z;

}
