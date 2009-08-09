package org.legzo.castorranker.ranker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

	private Logger logger = LoggerFactory.getLogger(Game.class);

	private List<Player> players = new ArrayList<Player>();
	private boolean ranked = false;

	public void rankPlayers(Date date) {
		// sorting all players
		Collections.sort(players);
		
		for (Player player : players) {
			int currentIndex = players.indexOf(player);

			logger.trace("{} - {} ({})", new Object[] { currentIndex + 1,
					player.getName(), player.getScore() });

			for (Player otherPlayer : players) {
				if (players.indexOf(otherPlayer) > currentIndex) {
					if (player.getScore() > otherPlayer.getScore()) {
						logger.trace("{} beats {}", player.getName(),
								otherPlayer.getName());
						player.beats(otherPlayer, player.getScore()
								- otherPlayer.getScore());

					} else {
						logger.trace("{} ties {}", player.getName(),
								otherPlayer.getName());
						player.ties(otherPlayer);
					}
				}
			}

			// resetting score
			player.setScore(0);
		}

		for (Player player : players) {
			player.getRankings().put(date, player.getRanking());
		}

		ranked = true;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public boolean isRanked() {
		return ranked;
	}

	public void displayResults() {
		if (ranked) {
			for (Player player : players) {
				logger.info("{}: {}", player.getName(), player.getRanking());
			}
		} else {
			logger.error("Game has not been ranked yet");
		}
	}

}
