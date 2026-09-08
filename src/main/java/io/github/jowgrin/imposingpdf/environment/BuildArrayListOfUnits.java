package io.github.jowgrin.imposingpdf.environment;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;

public class BuildArrayListOfUnits implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Unit> units;
	
	public BuildArrayListOfUnits() {
		units = new ArrayList<>();
	}
	
	public void addSize(Unit size) {
		units.add(size);
	}
	
	public void removeSize(Unit size) {
		units.remove(size);
	}
	
	public Iterator<Unit> iterator(){
		return units.iterator();
	}
	
	public void save() {
		try{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("UnitsArrayList.dll"));
			output.writeObject(this);
			output.flush();
			output.close();
			System.out.println("Your file of sizes saved successfully");
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Unit> getSizesArrayList(){
		return units;
	}
	
	public Unit[] getUnits() {
		Unit[] tmpUnit = new Unit[units.size()];
		for (int i = 0; i < tmpUnit.length; i++) {
			tmpUnit[i] = units.get(i);
		}
		return tmpUnit;
	}
	
	public String[] getNamesOfUnits() {
		String[] names = new String[units.size()];
		for(int i = 0; i < names.length; i++) {
			names[i] = units.get(i).toString();
		}
		return names;
	}
	
	public void replace(int i, int j) {
		if (i < 0 || i >= units.size() || j < 0 || j >= units.size())
			return;
		Unit p = units.get(j);
		units.set(j, units.get(i));
		units.set(i, p);
	}
	
	public static void main(String[] args) {
		
		Unit ps1 = Unit.MILIMETER;
		Unit ps2 = Unit.CENTIMETER;
		Unit ps3 = Unit.INCH;
		
//		Unit ps1 = new Unit("Milimeter", "mm", 1);
//		Unit ps2 = new Unit("Centimeter", "Cm", 10);
//		Unit ps3 = new Unit("Inch", "In", 25.4);
		
		BuildArrayListOfUnits arr = new BuildArrayListOfUnits();
		arr.addSize(ps1);
		arr.addSize(ps2);
		arr.addSize(ps3);
		arr.save();
	}
}
