package org.isatools.isatab.mapping;

import static org.junit.Assert.*;

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
	public void testAddMetabolites() {
		fail("Not yet implemented");
	}

	@Test
	public void testLineToArray() {
		fail("Not yet implemented");
	}

}
