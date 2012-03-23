package org.isatools.isatab.mapping;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.io.Reader;
import java.lang.reflect.Method;

import org.isatools.tablib.utils.HeaderCsvReader;

import org.apache.commons.lang.StringUtils;


import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.Metabolite;

public class MetabolitesMapper {

    static final String[] COLUMNS_TO_INDEX = {"description", "identifier"};

    public static void addMetabolitesToAssayGroup(AssayGroup ag, String basepath ){
    	
    	// Get the metabolite assignment file name.
    	String assayFilePath = basepath + "/" + ag.getFileName();
    	
    	// Get the full filename
    	String metaboliteFile = getMetabolitesFileName(assayFilePath);

    	// If there is no file, exit.
    	if (metaboliteFile.equals("")){ 
    		System.out.println("Not metabolites identification file found in  " + assayFilePath);
    		return;
    	}
        
    	// Add the base path
    	metaboliteFile = basepath + "/" + metaboliteFile;
    	
    	// Continue then
    	System.out.println("Looking metabolites file " + metaboliteFile);
    	
        // If there is no metabolite file...
        if (metaboliteFile == null) return;
        if (!new File(metaboliteFile).exists()) return ;

        // Add the metabolites to the AssayGroup
        addMetabolites(ag, metaboliteFile);


    }
    /**
     * Returns the full path of the metabolites assignment file if exists.
     * @param assayFilePath
     * @return Metabolite Identification file name.
     */
    		
    public static String getMetabolitesFileName(String assayFilePath) {
    	
    	// Get a reader of the assay file
    	BufferedReader reader;
    	String maf= "";
		try {
			reader = new BufferedReader(new FileReader(new File(assayFilePath)));
	    	// Create a generic CSV reader for Tabular format
	    	HeaderCsvReader hcsvr = new HeaderCsvReader((Reader)reader , '\t' , '"' , 0);
	    	
	    	
	    	
	    	// Look for the metabolight identification file
	    	while (maf.equals("")){
	    		
	    		String[] line = hcsvr.readNext();
	    		maf = hcsvr.getValue(line, "Metabolite Assignment File");
	    	}
	    	
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			//reader.close();
		}

		return maf;
    	
    	
    }

//    public static void addMetabolites(AssayGroup ag, String fileName) {
//
//        // Create a Hashmap with the index of the columns we want to index (
//        HashMap<String, Integer> column_indexes = new HashMap<String, Integer>();
//        
//        // For each field to index
//        for (String aCOLUMNS_TO_INDEX : COLUMNS_TO_INDEX) {
//            column_indexes.put(aCOLUMNS_TO_INDEX, null);
//        }
//
//        // Read the first line
//        try {
//
//            // Get the index based on the first row
//            File file = new File(fileName);
//
//            // Open the file
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//
//            String line = "";
//            int linecount = 1;
//
//            //Go through the file
//            while ((line = reader.readLine()) != null) {
//
//                String[] lineArray = lineToArray(line);
//
//                // Get the indexes for the columns descriptions
//                if (linecount == 1) {
//
//                    // For each field in the line
//                    for (int i = 0; i < lineArray.length; i++) {
//
//                        // Get the field
//                        String field = lineArray[i];
//
//                        // Check if it is in the hash
//                        if (column_indexes.containsKey(field)) {
//
//                            // Add the index
//                            column_indexes.put(field, i);
//                            
//                        }
//                    }
//
//                } else {
//
//                    Metabolite metabolite = new Metabolite(ag,"","");
//                    boolean isRowEmpty = true;
//
//                    // Get the values
//                    // For each column to index
//                    for (Entry<String, Integer> ci : column_indexes.entrySet()) {
//
//                        // Get the value
//                        String value = lineArray[ci.getValue()];
//                        
//                        // If value is NOT empty
//                        if (!value.isEmpty())  isRowEmpty = false;
//
//                    	
//                    	// If its the description...
//                    	if (ci.getKey().equals(COLUMNS_TO_INDEX[0])){
//                    		metabolite.setDescription(value);
//                    		
//                    	// If its the id
//                    	}else if (ci.getKey().equals(COLUMNS_TO_INDEX[1])) {
//							metabolite.setIdentifier(value);
//						}
//
//                        
//                    }
//                    
//                    // If we have found values add the string to the metabolites array
//                    if (!isRowEmpty) ag.getMetabolites().add(metabolite);
//                }
//
//
//                linecount++;
//            }
//
//            //Close the reader
//            reader.close();
//
//
//        } catch (Exception e) {
//            // Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

    public static void addMetabolites(AssayGroup ag, String mafFileName) {
      	
    	// Get a reader of the assay file
    	BufferedReader reader;
    	
		try {
			// Reader with the metabolite identification file
			reader = new BufferedReader(new FileReader(new File(mafFileName)));

			// Create a generic CSV reader for Tabular format
	    	HeaderCsvReader hcsvr = new HeaderCsvReader((Reader)reader , '\t' , '"' , 0);
	    	
	    	String[] line;
	    		    	
	    	// Read the metabolites identification file
	    	do{
	    		// Get the line
	    		line = hcsvr.readNext();
	    		
	    		// Populate the metabolite object with the correspondent values in the metabolites file.
	    		Metabolite metabolite = LineToMetabolite(line, hcsvr, ag);

	    		// If metabolite is not null
	    		if (metabolite != null) {
	    			ag.getMetabolites().add(metabolite);
	    		}
	    	
	    		
	    	} while (line != null);
	    	
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			//reader.close();
		}

	
    }
    /**
     * Populates as much members as possible of a metabolite object from data in the line passed as parameter
     * If not possible a null metabolite will be returned.
     * Only metabolites with either a description or an identifier will be added
     * @param line: Row in metabolite identification file
     * @param reader: Reader with headers
     * @return
     */
    static Metabolite LineToMetabolite(String[] line, HeaderCsvReader reader, AssayGroup ag){
    	
    	
    	// If line is null...
    	if (line == null) return null;
    	
    	// Check if minimum information is there (Description or Identifier)
    	String description = reader.getValue(line, COLUMNS_TO_INDEX[0]);
    	String identifier = reader.getValue(line, COLUMNS_TO_INDEX[1]);
    	
    	if (!StringUtils.isNotBlank(description) && !StringUtils.isNotBlank(identifier)){
    		return null;
    	}
    	
    	// For reflection
    	Class cls = Metabolite.class;
    	
    	Metabolite met = new Metabolite(ag, "", "");
    	
    	// Get the list of members to populate from the file
    	List<String> members = getListOfMetaboliteMembersToPopulate();
    	
    	// Go through the list of members
    	for (String member: members){
    		
    		// Convert the member name to a field name
    		String fieldName = getterToField(member);
    		
    		// Try to get the value from the field
    		String value = reader.getValue(line, fieldName);
    		
    		// If it hasn't returned null...
    		if (value!=null) {
    			
    			// Use reflexion to invoke the setter (member = setter)
	            Method meth;
				
	            try {
	            	
	            	// Search for the member with a one String as parameter
	            	Class partypes[] = new Class[1];
	                partypes[0] = String.class;
					meth = cls.getMethod(member,partypes);
					
					if (meth != null)
						meth.invoke(met, value);
					
					
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
    			
    		}
    	}
    	
    	return met;
    }

    public static String[] lineToArray(String line) {

        if (line.length() > 0) {
            // Remove the first double quote and the last one
            line = line.substring(1, line.length());
            line = line.substring(0, line.length() - 1);
        }

        // Add -1 to get empty strings.
        //return line.split("[^\"|\"\t\"|\"$]", -1);
        return line.split("\"\t\"", -1);
    }
    
    public static ArrayList<String> getListOfMetaboliteMembersToPopulate(){
    	
    	ArrayList<String> members = new ArrayList<String>();
        try {
            Class c = Metabolite.class;
            
            for (Method m: c.getDeclaredMethods()){
            	
            	// Only add Metabolite declared members (setters for string)
            	if (m.getDeclaringClass() == Metabolite.class){
            		
            		// Only add getters
            		if (m.getName().indexOf("set")==0){
            			
            			// Only String types (so getAssays stay out)
            			Class[] param = m.getParameterTypes();
            			if (param.length == 1 && param[0].equals(String.class)){
            				members.add(m.getName());
            			}
            		}
            	}
        	}
         }
         catch (Throwable e) {
            System.err.println(e);
         }
         
         return members;
    	
    }
    public static String getterToField(String getterName){
    	
    	getterName =  getterName.replaceFirst("set", ""); 			// Remove get
		
		
		// Change it to lower case
    	getterName = getterName.toLowerCase();
    	
    	return getterName;
    }


}
