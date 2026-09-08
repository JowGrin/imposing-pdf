package io.github.jowgrin.imposingpdf.environment;

import java.io.*;

public class PageSize implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double height;
	private double width;
	private Unit unit;
	
	public static final PageSize A3 = new PageSize("A3", 42, 29.7);
	public static final PageSize A4 = new PageSize("A4", 29.7, 21);
	public static final PageSize A5 = new PageSize("A5", 21, 14.8);
	public static final PageSize A6 = new PageSize("A6", 14.8, 10.5);
	public static final PageSize Letter = new PageSize("Letter", 11, 8.5, Unit.INCH);
	
	public PageSize(String name, double height, double width, Unit unit) {
		if (height > 0 || width > 0) {
			this.height = height;
			this.width = width;
		}
		this.name = name;
		this.unit = unit;
	}
	
	public PageSize(String name, double height, double width) {
		this(name, height, width, Unit.CENTIMETER);
	}
	
	public PageSize(double height, double width) {
		this("size" + Math.random()*50, height, width);
	}

	public double getWidth() {
		return width;
	}

//	public void setWidth(double width) {
//		this.width = width;
//	}

	public double getHeight() {
		return height;
	}

//	public void setHeight(double heigh) {
//		this.height = heigh;
//	}
	
	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}
	
	public float getPointWidth() {
		return unit.convertToPoints(width);
	}
	
	public float getPointHeight() {
		return unit.convertToPoints(height);
	}
	
	
	public String toString() {
		return name + " (" + width + "x" + height + ") " + unit;
	}
}
