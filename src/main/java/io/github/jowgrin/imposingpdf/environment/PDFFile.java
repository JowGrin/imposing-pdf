package io.github.jowgrin.imposingpdf.environment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import java.io.File;
import java.io.IOException;

public class PDFFile {
	private PDDocument file;
	private String name;
	private boolean saved;

	public PDFFile(String name, PDDocument file, boolean saved) {
		this.name = name;
		this.file = file;
		this.saved = saved;
	}
	
	public PDFFile(String name, PDDocument file) {
		this(name, file, false);
	}
	
	public PDFFile(File file, boolean saved) throws IOException{
		this(file.getName(), Loader.loadPDF(file), saved);
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public PDDocument getFile() {
		return file;
	}

//	public void setFile(PDDocument file) {
//		this.file = file;
//	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public void close() throws IOException {
		file.close();
	}
}
