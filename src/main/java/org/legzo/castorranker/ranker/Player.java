package org.legzo.castorranker.ranker;

import java.util.Date;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jfree.date.DateUtilities;

public class Player implements Comparable<Player> {

	private String name;

	// A new player is given 1200 points for a start
	private long ranking = 1200;
	private int score;

	// Maps to keep historic
	private SortedMap<Date, Integer> scores = new TreeMap<Date, Integer>();
	private SortedMap<Date, Long> rankings = new TreeMap<Date, Long>();

	/**
	 * Only way to instanciate a player
	 * 
	 * @param name
	 * @param ranking
	 */
	public Player(String name, long ranking) {
		this.name = name;
		this.ranking = ranking;

		// FIXME ??
		this.rankings.put(DateUtilities.createDate(2009, 03, 11), ranking);
	}

	public void beats(Player otherPlayer, int pointDiff) {
		long diff = EloModRanker.getDiff(ranking, otherPlayer.getRanking(),
				true, pointDiff);
		ranking += diff;
		otherPlayer.setRanking(otherPlayer.getRanking() - diff);
	}

	public void ties(Player otherPlayer) {
		long diff = EloModRanker.getDiff(ranking, otherPlayer.getRanking(),
				false, 0);
		ranking += diff;
		otherPlayer.setRanking(otherPlayer.getRanking() - diff);
	}

	public long getLastRankingVariation() {
		return getRankingVariationForDate((Date) rankings.keySet().toArray()[rankings
				.keySet().toArray().length - 1]);
	}

	public long getRankingVariationForDate(Date d) {
		if (rankings.size() < 2) {
			return 0;
		}
		SortedSet<Date> dates = new TreeSet<Date>();

		dates.addAll(rankings.keySet());

		Date previousDate = null;

		for (Date date : dates) {
			if (d == date) {
				break;
			}
			previousDate = date;
		}

		if (previousDate == null) {
			return 0;
		}

		return rankings.get(d) - rankings.get(previousDate);
	}

	public long getRanking() {
		return ranking;
	}

	public void setRanking(long ranking) {
		this.ranking = ranking;
	}

	public String getName() {
		return name;
	}

	public SortedMap<Date, Long> getRankings() {
		return rankings;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int compareTo(Player o) {
		return -((Integer) this.getScore()).compareTo(o.getScore());
	}

	public void setScoreForDate(int playerScore, Date dateOfLine) {
		scores.put(dateOfLine, playerScore);
	}
}
