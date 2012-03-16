package org.isatools.isatab.mapping;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.io.Reader;

import org.isatools.tablib.utils.HeaderCsvReader;


import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.MZTab;

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

    public static void addMetabolites(AssayGroup ag, String fileName) {

        // Create a Hashmap with the index of the columns we want to index (
        HashMap<String, Integer> column_indexes = new HashMap<String, Integer>();
        
        // Hash with the metabolites found
        ArrayList<MZTab> metabolites = new ArrayList<MZTab>();

        // For each field to index
        for (String aCOLUMNS_TO_INDEX : COLUMNS_TO_INDEX) {
            column_indexes.put(aCOLUMNS_TO_INDEX, null);
        }

        // Read the first line
        try {

            // Get the index based on the first row
            File file = new File(fileName);

            // Open the file
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";
            int linecount = 1;

            //Go through the file
            while ((line = reader.readLine()) != null) {

                String[] lineArray = lineToArray(line);

                // Get the indexes for the columns descriptions
                if (linecount == 1) {

                    // For each field in the line
                    for (int i = 0; i < lineArray.length; i++) {

                        // Get the field
                        String field = lineArray[i];

                        // Check if it is in the hash
                        if (column_indexes.containsKey(field)) {

                            // Add the index
                            column_indexes.put(field, i);
                            
                        }
                    }

                } else {

                        MZTab metabolite = new MZTab(ag,"","");
                        boolean isRowEmpty = true;

                        // Get the values
                    // For each column to index
                    for (Entry<String, Integer> ci : column_indexes.entrySet()) {

                        // Get the value
                        String value = lineArray[ci.getValue()];
                        
                        // If value is NOT empty
                        if (!value.isEmpty())  isRowEmpty = false;

                    	
                    	// If its the description...
                    	if (ci.getKey().equals(COLUMNS_TO_INDEX[0])){
                    		metabolite.setDescription(value);
                    		
                    	// If its the id
                    	}else if (ci.getKey().equals(COLUMNS_TO_INDEX[1])) {
							metabolite.setIdentifier(value);
						}

                        
                    }
                    
                    // If we have found values add the string to the metabolites array
                    if (!isRowEmpty) ag.getMzTabs().add(metabolite);
                }


                linecount++;
            }

            //Close the reader
            reader.close();


        } catch (Exception e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

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


}
