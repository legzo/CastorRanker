package org.legzo.castorranker.ranker;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class EloModRankerTest {

	@Test
	public void testRanking() {
		int playerRating = 2000;

		for (int i = 0; i < 100; ++i) {
			long diff = EloModRanker.getDiff(playerRating, 2300, true, 20);
			playerRating += diff;

			// System.out.println(playerRating + "(+" + diff + ")");
		}
	}
	
	@Test
	public void testPonderate() {
		EloModRanker.setJ(0.02f);
		
		for (int i = 0; i < 100; i += 10) {
			System.out.println(i + " " + EloModRanker.getDiff(2000, 2300, true, i));
		}
	}
	
	
	@Test
	public void testRank() {
		Player p1 = new Player("P1", 1200);
		Player p2 = new Player("P2", 1200);

		for (int i = 0; i <= 10; i++) {
			p1.beats(p2, 10);
			System.out.println(p1.getRanking());
		}
		
	}
}
