package io.github.jowgrin.imposingpdf.environment;
import java.io.Serializable;

public class Unit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String nickname;
	private double manyMmInUnit;
	
	public static final Unit MILIMETER = new Unit("Milimeter", "mm", 1);
	public static final Unit CENTIMETER = new Unit("Centimeter", "Cm", 10);
	public static final Unit INCH = new Unit("Inch", "In", 25.4);
	
	public Unit(String name, String nickname, double manyMmInUnit) {
		this.name = name;
		this.manyMmInUnit = manyMmInUnit;
		this.nickname = nickname;
	}
	
	public Unit(String name, double manyMmInUnit) {
		this(name, name.substring(0, 2) ,manyMmInUnit);
	}
	
	public double getManyMmInUnit() {
		return manyMmInUnit;
	}

//	public void setManyMmInUnit(double manyMmInUnit) {
//		this.manyMmInUnit = manyMmInUnit;
//	}

	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}
	
	public String getFullname() {
		return name + "-" + nickname + ": is " + manyMmInUnit + " mm";
	}

//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}
	
	public String getFullName() {
		return name + "(" + nickname + ")";
	}
	
	public float getPoints() {
		return (float)(this.manyMmInUnit * (72.0 / 25.4));
	}
	
	public float convertToPoints(double unit) {
		return (float)(getPoints()*unit);
	}
	
	public String toString() {
		return nickname;
	}
}
