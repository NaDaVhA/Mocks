package parsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class yagoMiner {
	
	static boolean qaqa = true;
	static boolean normalize;
	
	/**
	 * Extracts data from the given file. 
	 * @param filename
	 * @param attribute
	 * @param indexInLine
	 * @param attributes
	 * @return RawDataUnit containing the extracted data, or null in case of a failure.
	 */
	public static RawDataUnit mineDataUnit(String filename, String[] attributes, int indexInLine){
		
		RawDataUnit dataUnit = new RawDataUnit();
		
		LinkedList<String[]> lines = lineMiner(filename, attributes, indexInLine);
		
		if(lines == null){
			return null;
		}
		
		for(String[] line : lines)	
			dataUnit.addLine(line);
	
		return dataUnit;
		
	}
	
	
	/**
	 * Reads data from input file. 
	 * Parses each line into words and insert each work into String array.
	 * @param inputFileName
	 * @param attributes
	 * @param indexInLine
	 * @return LinkedList<String[]> of parsed lines, or null if failed.
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
		 
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find YAGO file: " + inputFileName + ". Verify path and start application again.");
			e.printStackTrace();
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if(normalize)
				array[i] = normalizeDataWord(array[i]);
		}
		
		return array;
	}
	
		
	
	public static String normalizeDataWord(String word){
		
		
		if(word.startsWith("<")&&word.endsWith(">")){
			
			//crop edges
			word = word.substring(1, word.length()-1);
			
			//remove '_'s
			word = word.replace('_', ' ');
			
			//Bring to lowercase form
			word = word.toLowerCase();
			
			
		}
		
		return word;
	}
	
	

}
