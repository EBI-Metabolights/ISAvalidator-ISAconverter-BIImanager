/*
 * __________
 * CREDITS
 * __________
 *
 * Team page: http://isatab.sf.net/
 * - Marco Brandizi (software engineer: ISAvalidator, ISAconverter, BII data management utility, BII model)
 * - Eamonn Maguire (software engineer: ISAcreator, ISAcreator configurator, ISAvalidator, ISAconverter,  BII data management utility, BII web)
 * - Nataliya Sklyar (software engineer: BII web application, BII model,  BII data management utility)
 * - Philippe Rocca-Serra (technical coordinator: user requirements and standards compliance for ISA software, ISA-tab format specification, BII model, ISAcreator wizard, ontology)
 * - Susanna-Assunta Sansone (coordinator: ISA infrastructure design, standards compliance, ISA-tab format specification, BII model, funds raising)
 *
 * Contributors:
 * - Manon Delahaye (ISA team trainee: BII web services)
 * - Richard Evans (ISA team trainee: rISAtab)
 *
 *
 * ______________________
 * Contacts and Feedback:
 * ______________________
 *
 * Project overview: http://isatab.sourceforge.net/
 *
 * To follow general discussion: isatab-devel@list.sourceforge.net
 * To contact the developers: isatools@googlegroups.com
 *
 * To report bugs: http://sourceforge.net/tracker/?group_id=215183&atid=1032649
 * To request enhancements: �http://sourceforge.net/tracker/?group_id=215183&atid=1032652
 *
 *
 * __________
 * License:
 * __________
 *
 * Reciprocal Public License 1.5 (RPL1.5)
 * [OSI Approved License]
 *
 * Reciprocal Public License (RPL)
 * Version 1.5, July 15, 2007
 * Copyright (C) 2001-2007
 * Technical Pursuit Inc.,
 * All Rights Reserved.
 *
 * http://www.opensource.org/licenses/rpl1.5.txt
 *
 * __________
 * Sponsors
 * __________
 * This work has been funded mainly by the EU Carcinogenomics (http://www.carcinogenomics.eu) [PL 037712] and in part by the
 * EU NuGO [NoE 503630](http://www.nugo.org/everyone) projects and in part by EMBL-EBI.
 */

package org.isatools.tablib.parser;

import org.isatools.tablib.schema.*;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static java.lang.System.out;
import static org.junit.Assert.assertNotNull;


/**
 * June 14, 2007
 *
 * @author brandizi
 */
public class TabLoaderTest {
	private TabLoader loader;
	private FormatSet schema;


	private FormatInstance load() throws IOException {
		InputStream input = new BufferedInputStream(this.getClass().getResourceAsStream("/test-data/tablib/foo_format_def.xml"));
		schema = SchemaBuilder.loadFormatSetFromXML(input);
		assertNotNull("I didn't get a schema", schema);

		// out.println( schema.toString ( true ) );

		loader = new TabLoader(schema);
		FormatInstance formatInstance = loader.parse(
				null,
				new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/test-data/tablib/foo_format_sample.csv"))),
				"fooFormat"
		);

		// Searching of instances will be tested too
		loader.getFormatSetInstance().addFormatInstance(formatInstance);

		return formatInstance;
	}


	/**
	 * Test simple TAB/TSV, loads only one section
	 */
	@Test
	public void testFooTab() throws IOException {
		out.println("--- Testing TabLoader class ---");

		FormatInstance formatInstance = load();

		SectionInstance sectionInstance = formatInstance.getSectionInstance("news");
		List<Record> records = sectionInstance.getRecords();
		assertNotNull("I have some records inside the schema", records);

		out.println("--- Testing TabLoader class, results: ---");

		for (Record rec : records) {
			out.println(rec);
		}

		out.println("--- Testing TabLoader class, end ---\n\n");
	}

	/**
	 * Test simple TAB/TSV, loads 2 sections
	 */
	@Test
	public void testFooTabAdvanced() throws IOException {
		// TODO: common initialization and separated test cases

		out.println("--- Testing TabLoader class, adding a second section: ---");
		load();

		FormatSetInstance formatSetInstance = loader.getFormatSetInstance();
		FormatInstance formatInstance = formatSetInstance.getFormatInstance("fooFormat");
		SectionInstance sectionInstance = formatInstance.getSectionInstance("pubs");
		List<Record> records = sectionInstance.getRecords();
		assertNotNull("I have some records inside the schema", records);

		out.println("--- Testing TabLoader class, second section case, results: ---");

		for (Record rec : records) {
			out.println(rec);
		}

		out.println("--- Testing TabLoader class, second section case, results: ---");
	}

}
