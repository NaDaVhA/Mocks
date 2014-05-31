package parsing;

import java.util.Iterator;
import java.util.LinkedList;


public class RawDataUnit implements Iterable<String[]>{
	
	private LinkedList<String[]> lines;
	private int size;
	
	//Constructor
	public RawDataUnit(){
		this.lines = new LinkedList<String[]>();
		this.size = 0;
	}
	
	
	//Getters
	
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
