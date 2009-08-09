package org.legzo.castorranker.ranker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.jfree.date.DateUtilities;
import org.junit.Test;

public class PlayerTest {

	@Test
	public void checkBeats() {
		Player p1 = new Player("P1", 1200);
		Player p2 = new Player("P2", 1200);

		Assert.assertEquals(p1.getRanking(), p2.getRanking());

		p1.beats(p2, 10);

		Assert.assertTrue(p1.getRanking() > p2.getRanking());
	}

	@Test
	public void checkSorting() {
		Player p1 = new Player("P1", 1200);
		p1.setScore(120);
		Player p2 = new Player("P2", 1200);
		p2.setScore(150);
		Player p3 = new Player("P3", 1200);
		p3.setScore(100);

		List<Player> myList = new ArrayList<Player>();
		myList.add(p1);
		myList.add(p2);
		myList.add(p3);

		for (Player player : myList) {
			System.out.println(player.getScore());
		}

		Collections.sort(myList);

		for (Player player : myList) {
			System.out.println(player.getScore());
		}
	}
	
	@Test
	public void checkPreviousRankings() {
		Player p1 = new Player("jte", 1200);

		Date before = DateUtilities.createDate(2009, 03, 12);
		Date after = DateUtilities.createDate(2009, 05, 24);
		Date after2 = DateUtilities.createDate(2009, 06, 24);
		
		p1.getRankings().put(after, new Long(1400));
		p1.getRankings().put(before, new Long(1200));
		p1.getRankings().put(after2, new Long(1300));

		Assert.assertEquals(-100, p1.getLastRankingVariation());
	}

	@Test
	public void checkPreviousRankings3() {
		Player p1 = new Player("jte", 1200);

		Date before = DateUtilities.createDate(2009, 03, 12);
		Date after = DateUtilities.createDate(2009, 05, 24);
		Date after2 = DateUtilities.createDate(2009, 06, 24);

		p1.getRankings().put(after, new Long(1400));
		p1.getRankings().put(before, new Long(1200));
		p1.getRankings().put(after2, new Long(1300));

		Assert.assertEquals(-100, p1.getRankingVariationForDate(after2));
	}
	
	@Test
	public void checkPreviousRankings4() {
		Player p1 = new Player("jte", 1200);

		Date before = DateUtilities.createDate(2009, 03, 12);
		Date after = DateUtilities.createDate(2009, 05, 24);
		Date after2 = DateUtilities.createDate(2009, 06, 24);

		p1.getRankings().put(before, new Long(1200));
		p1.getRankings().put(after, new Long(1400));
		p1.getRankings().put(after2, new Long(1300));

		Assert.assertEquals(200, p1.getRankingVariationForDate(after));
	}
	

	@Test
	public void checkPreviousRankings5() {
		Player p1 = new Player("jte", 1200);

		Date before = DateUtilities.createDate(2009, 03, 12);
		Date after = DateUtilities.createDate(2009, 05, 24);
		Date after2 = DateUtilities.createDate(2009, 06, 24);

		p1.getRankings().put(before, new Long(1200));
		p1.getRankings().put(after, new Long(1400));
		p1.getRankings().put(after2, new Long(1300));

		Assert.assertEquals(0, p1.getRankingVariationForDate(before));
	}

	@Test
	public void checkPreviousRankings2() {
		Player p1 = new Player("jte", 1200);

		Date before = DateUtilities.createDate(2009, 03, 12);

		p1.getRankings().put(before, new Long(1200));

		Assert.assertEquals(0, p1.getLastRankingVariation());
	}
}
