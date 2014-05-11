package parsing;

import java.util.Iterator;
import java.util.LinkedList;


public class RawDataUnit implements Iterable<String[]>{
	
	
	private String[] attributes;
	private LinkedList<String[]> lines;
	private int size;
	
	//Constructor
	RawDataUnit(String[] attrs){
		this.attributes = attrs;
		this.lines = new LinkedList<String[]>();
		this.size = 0;
	}
	
	
	//Getters
	public String[] getAttributes(){
		return this.attributes;
	}
	
	
	@Override
	public Iterator<String[]> iterator() {
		
		return this.lines.iterator();
	}
	
	
	//Setters
	public void addLine(String[] line){
		lines.add(line);
		this.size++;
	}
	
	
	
}
