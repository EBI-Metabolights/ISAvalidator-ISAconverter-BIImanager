package org.isatools.isatab.mapping;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.Metabolite;

import org.isatools.tablib.utils.HeaderCsvReader;
import org.junit.Test;

public class MetabolitesMapperTest {

	@Test
	public void testAddMetabolitesToAssayGroup() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMetabolitesFileName() {
		
		String maf = MetabolitesMapper.getMetabolitesFileName("./target/test-classes/test-data/isatab/isatab_v1_200810/metabolights_201203/a_live_mtbl1_rms_metabolite profiling_NMR spectroscopy.txt");
		
		assertEquals("a_live_mtbl1_rms_metabolite profiling_NMR spectroscopy_maf.csv", maf);

	}

	@Test
	public void testGetListOfMetaboliteMembersToPopulate(){

		List<String> membersExpected = new ArrayList<String>(Arrays.asList(
				"setIdentifier",
				"setChemical_formula",
				"setDescription",
				"setMass_to_charge",
				"setCharge",
				"setRetention_time",
				"setDatabase",
				"setDatabase_version",
				"setReliability",
				"setUri",
				"setSearch_engine",
				"setSearch_engine_score",
				"setModifications",
				"setSmallmolecule_abundance_sub",
				"setSmallmolecule_abundance_stdev_sub",
				"setSmallmolecule_abundance_std_error_sub",
				"setUnit_id", 
				"setChemical_shift", 
				"setMultiplicity", 
				"setFragmentation"));

		List<String> membersActuals = MetabolitesMapper.getListOfMetaboliteMembersToPopulate();
		
		System.out.println(membersActuals);
		
		assertArrayEquals(membersExpected.toArray(), membersActuals.toArray());
	
	
	}
	@Test
	public void testGetterToField(){
		
		assertEquals("hola", MetabolitesMapper.getterToField("getHola"));
	}
	
	@Test
	public void testAddMetabolites(){
	
		AssayGroup ag = new AssayGroup("dada");
		
		MetabolitesMapper.addMetabolites(ag, "./target/test-classes/test-data/isatab/isatab_v1_200810/metabolights_201203/test_maf.csv");
		
		// Now assay group must have the metabolites populated
		assertEquals(17, ag.getMetabolites().size());
		
		Metabolite CHEBI28358 = null;
		
		for (Metabolite met:ag.getMetabolites()){
			if (met.getIdentifier().equals("CHEBI:28358")){
				CHEBI28358 = met;
				continue;
			}
		}
		
		// Now we have got LMGP01010629, test some of its properties
		assertEquals("CHEBI:28358", CHEBI28358.getIdentifier());
		assertEquals("lactic acid", CHEBI28358.getDescription());
	
	}
	@Test
	public void testLineToMetabolite() throws Exception{
	
		AssayGroup ag = new AssayGroup("dada");
		
	  	// Get a reader of the assay file
    	BufferedReader reader;
    	
		// Reader with the metabolite identification file
		reader = new BufferedReader(new FileReader(new File("./target/test-classes/test-data/isatab/isatab_v1_200810/metabolights_201203/test_maf.csv")));

		// Create a generic CSV reader for Tabular format
    	HeaderCsvReader hcsvr = new HeaderCsvReader((Reader)reader , '\t' , '"' , 0);
	
    	Metabolite met = MetabolitesMapper.LineToMetabolite(new String[]{""}, hcsvr,ag);
	    
		// Now we have got , test some of its properties
		assertEquals(null, met);
		
		met = MetabolitesMapper.LineToMetabolite(new String[]{"identifier", "H2O"}, hcsvr,ag);
	    
		assertEquals("identifier", met.getIdentifier());
		assertEquals("H2O", met.getChemical_formula());
		
		// Test description only lines generates metabolites
		met = MetabolitesMapper.LineToMetabolite(new String[]{"", "CH4", "Methane"}, hcsvr,ag);
	    
		assertEquals("", met.getIdentifier());
		assertEquals("CH4", met.getChemical_formula());
		assertEquals("Methane", met.getDescription());
		
	
	}

}
