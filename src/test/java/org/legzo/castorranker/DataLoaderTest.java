package org.legzo.castorranker;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class DataLoaderTest {

	@Test
	public void testLoadData() {
		DataLoader loader = new DataLoader();

		loader.setDataFile("src/test/resources/scores-full.csv");
		loader.setResultDataFile("target/scores-evol.csv");

		loader.loadData();

		loader.dumpResults();
	}

	@Test
	public void testFormatDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.parse("2009/03/18");
	}

}
