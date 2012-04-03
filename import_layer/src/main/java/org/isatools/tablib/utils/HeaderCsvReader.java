package org.isatools.tablib.utils;

import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

	/**
	 * A version of {@link CsvIterator} which reads the headers from the first line and allow to reuse them later to retrieve
	 * field values.
	 * <p/>
	 * <dl><dt>Date:</dt><dd>March 16, 2012</dd></dl>
	 *
	 * @author pconesa
	 */
public class HeaderCsvReader {

	/**
     * Every key is initialized with the corresponding header on the first line of the CSV, every value is the index of
     * that header.
     */
    protected Map<String, Integer> headers = new HashMap<String, Integer>();
    private String hline[];
    private CSVReader csvr;
    
    
	public HeaderCsvReader(Reader reader) throws IOException {
			
    	this(reader, CSVReader.DEFAULT_SEPARATOR , CSVReader.DEFAULT_QUOTE_CHARACTER, 0);	
	}
	public HeaderCsvReader(Reader reader, char separator, char quotechar, int line) throws IOException{
		
		csvr = new CSVReader(reader, separator, quotechar, line);
		readHeaders();
	}
   
    /**
     * First read the headers line, then call super.initFirstOject()
     */
    protected void readHeaders() throws IOException {
        hline = csvr.readNext();
        if (hline == null) {
            return;
        }
        for (int icol = 0; icol < hline.length; icol++) {
            headers.put(hline[icol], icol);
        }
        
    }
    public String[] readNext() throws IOException{
    	return csvr.readNext();
    }
    

    /**
     * Gets the line value corresponding to the given header
     */
    public String getValue(String[] line, String header) {
        Integer icol = headers.get(header);
        if (icol == null) {
            return null;
        }
        if (icol >= line.length) {
            return null;
        }
        return line[icol];
    }
    public Map<String, Integer> getHeaders(){
    	return headers;
    }
    public String[] getHLines(){
    	return hline;
    }
}
