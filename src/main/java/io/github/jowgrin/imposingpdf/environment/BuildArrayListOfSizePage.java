package io.github.jowgrin.imposingpdf.environment;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;

public class BuildArrayListOfSizePage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<PageSize> sizes;
	
	public BuildArrayListOfSizePage() {
		sizes = new ArrayList<>();
	}
	
	public void addSize(PageSize size) {
		sizes.add(size);
	}
	
	public void removeSize(PageSize size) {
		sizes.remove(size);
	}
	
	public Iterator<PageSize> iterator(){
		return sizes.iterator();
	}
	
	public void save() {
		try{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("SizesArrayList.dll"));
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
	
	public ArrayList<PageSize> getSizesArrayList(){
		return sizes;
	}
	
	public PageSize[] getPageSizes() {
		PageSize[] pageSizes = new PageSize[sizes.size()];
		for (int i = 0; i < pageSizes.length; i++) {
			pageSizes[i] = sizes.get(i);
		}
		return pageSizes;
	}
	
	public String[] getNamesOfSize() {
		String[] names = new String[sizes.size()];
		for(int i = 0; i < names.length; i++) {
			names[i] = sizes.get(i).toString();
		}
		return names;
	}
	
	public void replace(int i, int j) {
		if (i < 0 || i >= sizes.size() || j < 0 || j >= sizes.size())
			return;
		PageSize p = sizes.get(j);
		sizes.set(j, sizes.get(i));
		sizes.set(i, p);
	}
	
	public static void main(String[] args) {
		
		PageSize ps1 = PageSize.A4;
		PageSize ps2 = PageSize.A3;
		PageSize ps3 = PageSize.Letter;
		
//		PageSize ps1 = new PageSize("A4", 29.7, 21);
//		PageSize ps2 = new PageSize("A3", 42, 29.7);
//		PageSize ps3 = new PageSize("Letter", 8.5, 11, Unit.INCH);
		
		BuildArrayListOfSizePage arr = new BuildArrayListOfSizePage();
		arr.addSize(ps1);
		arr.addSize(ps2);
		arr.addSize(ps3);
		arr.save();
	}
}
