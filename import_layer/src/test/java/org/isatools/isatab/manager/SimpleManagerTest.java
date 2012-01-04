package org.isatools.isatab.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Test;

public class SimpleManagerTest {

	@Test
	public void testStudy2Set() {
		SimpleManager sm = new SimpleManager();
		
		Set<String> studies = sm.Study2Set("HOLA");
		assertEquals( "Cointains HOLA", true, studies.contains("HOLA"));
		assertEquals( "Not Cointains HOLO", false, studies.contains("HOLO"));
		assertEquals( "One item", 1, studies.size());
		
		
		
		studies = sm.Study2Set("HELLO|BYE");
		assertEquals( "Cointains HELLO", true, studies.contains("HELLO"));
		assertEquals( "Contains BYE", true, studies.contains("BYE"));
		assertEquals( "Two items", 2, studies.size());

	}

	@Test
	public void testUpload(){
		
		SimpleManager sm = new SimpleManager("src/test/resources/config/");
		
		assertEquals("src/test/resources/config/", sm.getDBConfigPath());
		
		// TODO: use a different user or create one on the fly
		try {
			sm.loadISAtab(new File("src/test/resources/test-data/isatab/metabolights/").getAbsolutePath(), "conesa@ebi.ac.uk");
		} catch (Exception e) {
			e.printStackTrace();
			fail("There has been an exception: " + e.getMessage());
		}
		
		
		
	}
}
