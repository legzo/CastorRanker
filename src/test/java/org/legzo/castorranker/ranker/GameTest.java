package org.legzo.castorranker.ranker;

import java.util.Date;

import org.junit.Test;
import org.legzo.castorranker.ranker.Game;
import org.legzo.castorranker.ranker.Player;


public class GameTest {

	@Test
	public void testRank() {
		Date date = new Date();
		
		Game tournament = new Game();
		Player p1 = new Player("P1", 1200);
		Player p2 = new Player("P2", 1200);
		Player p3 = new Player("P3", 1200);
		Player p4 = new Player("P4", 1200);
		Player p5 = new Player("P5", 1200);
		Player p6 = new Player("P6", 1200);
		Player p7 = new Player("P7", 1200);
		Player p8 = new Player("P8", 1200);

		tournament.addPlayer(p1);
		tournament.addPlayer(p2);
		tournament.addPlayer(p3);
		tournament.addPlayer(p4);
		tournament.addPlayer(p5);
		tournament.addPlayer(p6);
		tournament.addPlayer(p7);
		tournament.addPlayer(p8);
		
		tournament.rankPlayers(date);
		tournament.displayResults();
		
		tournament = new Game();
		
		tournament.addPlayer(p8);
		tournament.addPlayer(p7);
		tournament.addPlayer(p6);
		tournament.addPlayer(p5);
		tournament.addPlayer(p4);
		tournament.addPlayer(p3);
		tournament.addPlayer(p2);
		tournament.addPlayer(p1);
		
		tournament.rankPlayers(date);
		tournament.displayResults();
	}
	
}
