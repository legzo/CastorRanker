package org.legzo.castorranker;

import org.legzo.castorranker.export.HtmlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// String dataFile = "scores-full.csv";
		String dataFile = "src/test/resources/scores-full.csv";

		logger.info("Initializing data loader for file {}", dataFile);
		
		DataLoader loader = new DataLoader();

		// loader.setDataFile("src/test/resources/scores-full.csv");

		loader.setDataFile(dataFile);

		loader.loadData();
		
		logger.info("Done initializing, starting dumping");
		

		HtmlGenerator generator = new HtmlGenerator();
		generator.generateSite(loader.getLoadedPlayers().values(), loader
				.getAllDates());

		logger.info("All done, bye");
		
	}

}
