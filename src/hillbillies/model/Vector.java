package hillbillies.model;

import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.*;
/**
 * Class representing a three-dimensional vector having real coefficients.
 * @author Joost Croonen & Ruben Dedoncker
 *
 */
@Value
public final class Vector {
	/**
	 * Initialize a three-dimensional vector having real coefficients. 
	 * @param 	x
	 * 			The given x-coordinate of the vector.
	 * @param 	y
	 * 			The given y-coordinate of the vector.
	 * @param 	z
	 * 			The given z-coordinate of the vector.
	 * @post	The coefficients of the vector are set to the given coefficients.
	 */
	public Vector (double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * Create a clone of the given vector
	 * @param 	vector
	 * 			The given vector to be cloned.
	 * @post	This vector has the same coefficients as the given vector.
	 */
	public Vector (Vector vector){
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
	
	/**
	 * Return the x-coordinate of the vector.
	 */
	@Basic
	@Immutable
	public double getX(){
		return this.x;
	}

	/**
	 * Return the y-coordinate of the vector.
	 */
	@Basic
	@Immutable
	public double getY(){
		return this.y;
	}
	
	/**
	 * Return the z-coordinate of the vector.
	 */
	@Basic
	@Immutable	
	public double getZ(){
		return this.z;
	}
	
	/**
	 * Return a list containing the coefficients of this vector.
	 */
	@Immutable
	public ArrayList<Double> getCoeff(){
		ArrayList<Double> coeff = new ArrayList<Double>();
		coeff.add(this.x);
		coeff.add(this.y);
		coeff.add(this.z);
		return coeff;
	}
	
	/**
	 * Addition of a given vector to this vector.
	 * @param	vector
	 * 			The given vector to be added.
	 * @return	The sum of the two vectors is returned.
	 */
	public Vector add(Vector vector){
		return new Vector(this.x + vector.getX(), this.y + vector.getY(),
				this.z + vector.getZ());
	}
	/**
	 * Addition this vector with the given coefficients.
	 * @param 	x
	 * 			The given x-coordinate to be added to the
	 * 			x-coordinate of this vector.
	 * @param 	y
	 * 			The given x-coordinate to be added to the
	 * 			y-coordinate of this vector.
	 * @param 	z
	 * 			The given x-coordinate to be added to the
	 * 			z-coordinate of this vector.
	 * @return	Return the sum of this vector with the given
	 * 			coefficients
	 */
	public Vector add(double x, double y, double z){
		return new Vector(this.x + x, this.y + y, this.z + z);
	}
	
	/**
	 * Return a boolean stating whether this vector is equal to the
	 * 	given vector.
	 * @param 	vector
	 * 			The vector to be checked.
	 * @return	True if and only if the all the coefficients of
	 * 			this vector are equal to the coefficients of the
	 * 			given vector.
	 */
	public boolean equals(Vector vector){
		if (this == null || vector == null)
			return false;
		return((this.x == vector.getX()) && (this.y == vector.getY()) && (this.z == vector.getZ()));
	}
	/**
	 * Return the length of this vector.
	 */
	@Immutable
	public double getLength(){
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	/**
	 * Calculation of the distance between this vector and a given vector.
	 * @param 	target
	 * 			The given vector.
	 * @return Return the distance between this vector and the given vector.
	 */
	public double distance(Vector target){
		Vector distanceVector = this.add(target.getOpposite());
		double distance = distanceVector.getLength();
		return distance;
	}
	
	/**
	 * Return the normalized vector.
	 */
	public Vector normalize(){
		if (this.getLength() == 0)
				return this;
		double length = this.getLength();
		return new Vector(this.x / length, this.y / length, this.z / length);
	}
	
	
	/**
	 * Scalar multiplication of this vector with a given vector.
	 * @param 	vector
	 * 			The given vector with which the scalar product
	 * 			is carried out. 
	 * @return	Return the result of the scalar product.
	 */
	public double scalarProduct(Vector vector){
		return (this.x*vector.getX() + this.y*vector.getY() + this.z*vector.getZ());
	}
	
	/**
	 * Cross product of this vector with a given vector.
	 * @param 	vector
	 * 			The given vector with which the cross product
	 * 			is carried out.
	 * @return	Return the result of the cross product.
	 */
	public Vector crossProduct(Vector vector){
		double newX = this.y*vector.getZ() - this.z*vector.getX();
		double newY = this.z*vector.getX() - this.x*vector.getZ();
		double newZ = this.x*vector.getY() - this.y*vector.getX();
		
		return new Vector(newX, newY, newZ);
	}
	
	/**
	 * Multiplication the coefficients of this vector with a real factor.
	 * @param 	factor
	 * 			The given factor.
	 * @return Return the product of this vector with the given factor.
	 */
	@Basic
	public Vector multiply(double factor){
		return new Vector(this.x*factor, this.y*factor, this.z*factor);
	}
	
	/**
	 * Return a vector which is opposite to this one.
	 */
	@Immutable
	public Vector getOpposite(){
		Vector opposite = this.multiply(-1);
		return opposite;
	}
	
	@Immutable
	public Vector floor(){
		return new Vector((int) this.x, (int) this.y, (int) this.z);
	}
	
	private final double x;
	private final double y;
	private final double z;
	

}
