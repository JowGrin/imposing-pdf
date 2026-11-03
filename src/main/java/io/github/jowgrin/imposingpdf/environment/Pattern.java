package io.github.jowgrin.imposingpdf.environment;

public class Pattern {
	/**
	 * 
	 * @param pattern name of file
	 * @return true if it's legal pattern
	 */
	public static boolean isPattern(String pattern) {
    	if (pattern == null)
			return false;
    	if (pattern.contains("{n}"))
    		return true;
    	return false;
    }
	
    /**
     * 
     * @param pattren of string with {n}
     * @return new string replaced all {n} to %d
     */
    public static String ConvertToFormat(String pattren) {
		if (!isPattern(pattren))
			return pattren + "_%d";
		
    	pattren = pattren.replace('{', ',');
		pattren = pattren.replace('}', ',');
    	return pattren.replaceAll(",n,", "%d");
	}
    
    
    
    public static String getNextName(String word, int num) {
    	int index = word.indexOf(".");
    	String name = "";
    	String sofix = "";
    	if (index >= 0) {
    		 name = word.substring(0, index);
    		 sofix = word.substring(index, word.length());
    	}
    	else {
    		name = word;
    	}
    	
    	if (name.contains("_")) {
    		index = name.indexOf("_");
    		name = name.substring(0, index);
    	}
   
		return name + "_" + num + sofix;

    }
    
}
