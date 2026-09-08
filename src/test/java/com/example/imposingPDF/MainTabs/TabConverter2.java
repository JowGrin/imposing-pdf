package com.example.imposingPDF.MainTabs;
import javafx.util.StringConverter;
import javafx.scene.control.Tab;

public class TabConverter2 extends StringConverter<Tab> {

	@Override
	public Tab fromString(String st) {
		return null;
	}

	@Override
	public String toString(Tab tab) {
		return tab != null ? tab.getText() : "";
	}
}