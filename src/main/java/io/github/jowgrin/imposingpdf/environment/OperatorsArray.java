package io.github.jowgrin.imposingpdf.environment;
import java.io.*;
import java.util.Iterator;

import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.DefinePagesSizeController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.MixPagesController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.NUPController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.RotationPagesController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.SplitPagesController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.StepAndRepeatController;
import io.github.jowgrin.imposingpdf.sidebarOptions.editPages.TrimAndShiftController;

import java.util.ArrayList;

public class OperatorsArray implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Operator> operators;
	
	public OperatorsArray() {
		operators = new ArrayList<>();
	}
	
	public void addOperator(Operator op) {
		operators.add(op);
	}
	
	public void removeOperator(Operator op) {
		operators.remove(op);
	}
	
	public Iterator<Operator> iterator(){
		return operators.iterator();
	}
	
	public Operator[] getOperators() {
		Operator[] tmpOps = new Operator[operators.size()];
		for (int i = 0; i < tmpOps.length; i++) {
			tmpOps[i] = operators.get(i);
		}
		return tmpOps;
	}
	
	public ArrayList<Operator> getOperatorsArrayList(){
		return operators;
	}
	
	public static void main(String[] args) {
		
		Operator op1 = new DefinePagesSizeController();
		Operator op2 = new MixPagesController();
		Operator op3 = new NUPController();
		Operator op4 = new RotationPagesController();
		Operator op5 = new SplitPagesController();
		Operator op6 = new StepAndRepeatController();
		Operator op7 = new TrimAndShiftController();
		
		OperatorsArray arr = new OperatorsArray();
		arr.addOperator(op1);
		arr.addOperator(op2);
		arr.addOperator(op3);
		arr.addOperator(op4);
		arr.addOperator(op5);
		arr.addOperator(op6);
		arr.addOperator(op7);		
		
//		arr.save();
	}
}
