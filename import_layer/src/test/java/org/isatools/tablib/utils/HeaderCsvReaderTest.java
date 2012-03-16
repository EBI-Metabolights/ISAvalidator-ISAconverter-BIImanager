package org.isatools.tablib.utils;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class HeaderCsvReaderTest {

	final String TSV_FILE = "./target/test-classes/test-data/tablib/utils/sample_w_header.csv";
	
	@Test
	public void testHeaderCsvReaderReader() throws IOException {
		
		
	   	// Get a reader of the assay file
    	BufferedReader reader = new BufferedReader(new FileReader(new File(TSV_FILE)));
    	
		HeaderCsvReader hcsvr = new HeaderCsvReader(reader);

		// Test default properties
		//assertEquals(CSVReader.DEFAULT_SEPARATOR, actual)
		
	}

	@Test
	public void testHeaderCsvReaderReaderCharCharInt() throws IOException {
	   	// Get a reader of the assay file
    	BufferedReader reader = new BufferedReader(new FileReader(new File(TSV_FILE)));
    	
		HeaderCsvReader hcsvr = new HeaderCsvReader(reader, '\t', '"', 0);

		// Test there are three headers
		assertEquals(3, hcsvr.headers.size());
		
		// Test the headers content
		assertEquals(true,hcsvr.headers.containsKey("date"));
		assertEquals(0,(int)hcsvr.headers.get("date"));
		
		assertEquals(true,hcsvr.headers.containsKey("title"));
		assertEquals(1,(int)hcsvr.headers.get("title"));
		
		assertEquals(true,hcsvr.headers.containsKey("abs"));
		assertEquals(2,(int)hcsvr.headers.get("abs"));
		
	}

	

	@Test
	public void testGetValue() throws IOException {
		
	   	// Get a reader of the assay file
    	BufferedReader reader = new BufferedReader(new FileReader(new File(TSV_FILE)));
    	
		HeaderCsvReader hcsvr = new HeaderCsvReader(reader, '\t', '"', 0);
		
		String[] line = hcsvr.readNext();
		
		assertEquals("06/02/04 00:00",hcsvr.getValue(line, "date"));
		assertEquals("Scatta un'altra denuncia contro il chirurgo plastico",hcsvr.getValue(line, "title"));
		assertEquals("LUCCA - Un altro esposto contro il medesimo chirurgo plastico giÃ  accusato nei giorni scorsi da due donne di aver rovinato loro il seno con un intervento sbagliato di mastectomia. A presentare la denuncia Ã¨ stata una signora lucchese di 59 anni, M.S.L., che si Ã¨ rivolta ne...",hcsvr.getValue(line, "abs"));
		
		line = hcsvr.readNext();
		
		assertEquals("06/03/06 00:00",hcsvr.getValue(line, "date"));
		
		int lineCnt = 3;
		
		while (hcsvr.readNext()!= null){
			
			lineCnt++;
		}

		assertEquals(72, lineCnt);
		
		
		
		
	}

}
