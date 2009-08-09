package org.legzo.castorranker.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import junit.framework.Assert;

import org.jfree.date.DateUtilities;
import org.junit.Test;
import org.legzo.castorranker.DataLoader;
import org.legzo.castorranker.ranker.Player;


public class ChartMakerTest {
	@Test
	public void testLoadData() {
		DataLoader loader = new DataLoader();

		loader.setDataFile("src/test/resources/scores-full.csv");
		loader.setResultDataFile("target/scores-evol.csv");

		loader.loadData();

		ChartMaker writer = new ChartMaker();
		for (int i = 1; i < 30; i++) {
			writer.printChart(loader.getLoadedPlayers().values(), loader
					.getAllDates(), "target/scores-evol"
					+ System.currentTimeMillis() + ".png", i);
		}

	}
	
	@Test
	public void testGetRankingForDate(){
		ChartMaker cm = new ChartMaker();
		
		Player p1= new Player("JTE", 1400);
		Player p2= new Player("MVI", 1200);
		Player p3= new Player("VBO", 1300);
		Player p4= new Player("MRI", 1000);
		
		Collection<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		
		Date date = DateUtilities.createDate(2009, 03, 11);
		
		Assert.assertEquals(1, cm.getRankingForDate(p1, players, date));
		Assert.assertEquals(3, cm.getRankingForDate(p2, players, date));
		Assert.assertEquals(2, cm.getRankingForDate(p3, players, date));
		Assert.assertEquals(4, cm.getRankingForDate(p4, players, date));
	}
}
