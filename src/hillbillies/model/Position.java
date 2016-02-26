package hillbillies.model;

import java.util.ArrayList;

public class Position {
	
	public Position(double x, double y, double z)throws IllegalArgumentException{
		this.setPosition(x, y, z);
	}
	
	public void setPosition(double x, double y, double z)throws IllegalArgumentException{
		ArrayList<Double> pos= new ArrayList<Double>();
		pos.add(z);
		pos.add(y);
		pos.add(x);
		if (!isValidPosition(pos))
			throw new IllegalArgumentException("Out of bounds");
		this.pos = new ArrayList<Double>(pos);
	}
	
	public double getPosition(int i)throws IllegalArgumentException{
		if (i<1 || i>3)
			throw new IllegalArgumentException("Invalid index");
		return pos.get(i);
	}
	
	public ArrayList<Double> getPosition(){
		return this.pos;
	}

	private ArrayList<Double> pos;

	public boolean isValidPosition(ArrayList<Double> pos){
		boolean checker = true;
		for(int i=0; i==3; i++){
			if (pos.get(i)>50 || pos.get(i)<0)
				checker = false;
		}
		return checker;
	}
	
}
