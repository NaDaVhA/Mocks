package parsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class yagoMiner {
	
	static boolean qaqa = true;
	
	
	/**
	 * 
	 * @param filename
	 * @param attribute
	 * @param indexInLine
	 * @param attributes
	 * @return
	 */
	public static RawDataUnit mineDataUnit(String filename, String[] attributes, int indexInLine){
		
		RawDataUnit dataUnit = new RawDataUnit();
		
		LinkedList<String[]> lines = lineMiner(filename, attributes, indexInLine);
		
		for(String[] line : lines)	
			dataUnit.addLine(line);
	
		return dataUnit;
		
	}
	
	
	/**
	 * 
	 * @param inputFileName
	 * @param outputFileName
	 * @param attribute
	 * @param indexInLine between 0 and line.size-1
	 */
	private static LinkedList<String[]> lineMiner(String inputFileName, String[] attributes, int indexInLine){
				
		LinkedList<String[]> listOfLines = new LinkedList<String[]>();
				
		try
		{
			FileInputStream in = new FileInputStream(inputFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while((strLine = br.readLine())!= null){
		  		
				//if(qaqa) System.out.println(strLine);
				
				for(String attribute : attributes){
					
					if(strLine.contains(attribute)){
			  			
			  			//Some lines might start with tabs, so we drop it
						if(strLine.startsWith("\t"))
							strLine = strLine.substring(1);
			  			
			  			String[] array = parseClosesTabs(strLine);
			  			
			  			if(indexInLine>=array.length){
			  				System.out.println("parseFileByAttribute: error: indexInLine >= array.length!");
			  				continue;
			  			}
			  			
			  			if(array[indexInLine].contains(attribute)){
			  				listOfLines.add(array);
			  			}
			  				
			  			
			  			break;
					}
				}
				
			}
			
			br.close();
			in.close();
		 
		}catch(Exception e){
			System.out.println(e);
		}
		
		return listOfLines;
	}
	

	/**
	 * Splits line around "\t", returns "closes", in lowercase.
	 * @param Line
	 * @return
	 */
	private static String[] parseClosesTabs(String Line){
		
		String[] array = Line.split("\t");
		
		for(int i=0; i<array.length; i++){
			array[i] = array[i].toLowerCase();
		}
		
		return array;
	}
	
		
	
	
	

}
