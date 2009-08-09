package org.legzo.castorranker.export;

import junit.framework.Assert;

import org.junit.Test;
import org.legzo.castorranker.DataLoader;

public class HtmlGeneratorTest {

	@Test
	public void testGenerateIndex() {
		DataLoader loader = new DataLoader();

		loader.setDataFile("src/test/resources/scores-full.csv");

		loader.loadData();

		HtmlGenerator generator = new HtmlGenerator();
		generator.generateSite(loader.getLoadedPlayers().values(), loader
				.getAllDates());

	}

	@Test
	public void testReplaceAll() {
		String str = "1/ jte\n2/amelie";

		str = str.replaceAll("\\\n", "<BR>");

		Assert.assertEquals("1/ jte<BR>2/amelie", str);
	}

}
