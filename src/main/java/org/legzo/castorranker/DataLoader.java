package org.legzo.castorranker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.legzo.castorranker.ranker.Game;
import org.legzo.castorranker.ranker.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLoader {

	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	private String resultDataFile;
	private String dataFile;
	private Map<Integer, Player> loadedPlayers = new HashMap<Integer, Player>();

	public void loadData() {
		try {
			FileInputStream fstream = new FileInputStream(dataFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			int index = 0;

			while ((strLine = br.readLine()) != null) {
				if (index == 0) {
					treatHeader(strLine);
				} else {
					treatGameLine(strLine);
				}

				index++;
			}
			in.close();
		} catch (Exception e) {
			logger.error("Error occured", e);
		}

		fillBlanksInChronology();
	}

	private void treatGameLine(String strLine) throws ParseException {
		StringTokenizer tokenizer = new StringTokenizer(strLine, ";");
		int colIndex = 0;

		Date dateOfLine = null;

		while (tokenizer.hasMoreTokens()) {

			String value = tokenizer.nextToken();
			if (colIndex == 0) {
				dateOfLine = format.parse(value);
			} else {
				Player player = loadedPlayers.get(colIndex);
				int playerScore = Integer.parseInt(value);
				player.setScore(playerScore);
				player.setScoreForDate(playerScore, dateOfLine);
			}
			colIndex++;
		}

		Game currentGame = new Game();

		// players join the game only if they were playing (ie score > 0)
		for (Player p : loadedPlayers.values()) {
			if (p.getScore() > 0) {
				currentGame.addPlayer(p);
			}
		}

		currentGame.rankPlayers(dateOfLine);
	}

	private void treatHeader(String strLine) {
		StringTokenizer tokenizer = new StringTokenizer(strLine, ";");
		int colIndex = 0;

		while (tokenizer.hasMoreTokens()) {
			String value = tokenizer.nextToken();

			if ("date".equals(value)) {
				// do nothing, it's the date column
			} else {
				Player player = new Player(value, 1200);
				loadedPlayers.put(colIndex, player);
			}
			colIndex++;
		}
	}

	public void dumpResults() {
		try {
			Writer writer = new FileWriter(resultDataFile);
			writer.append(generateResultFileContent());
			if (writer != null) {
				writer.close();
			}
		} catch (Exception e) {// Catch exception if any
			logger.error("Error occured", e);
		}
	}

	private String generateResultFileContent() {
		StringBuffer sb = new StringBuffer();

		SortedSet<Date> allDates = getAllDates();

		for (Player p : loadedPlayers.values()) {
			sb.append(p.getName());
			sb.append(";");

			for (Date d : allDates) {
				Long rankingForDate = p.getRankings().get(d);
				if (rankingForDate != null) {
					sb.append(rankingForDate);
				}

				sb.append(";");
			}
			// new line for new player
			sb.append("\n");
		}

		return sb.toString();
	}

	public static String getCurrentGlobalRanking(Collection<Player> players,
			final Date d) {
		StringBuffer sb = new StringBuffer();

		List<Player> ranking = new ArrayList<Player>();

		ranking.addAll(players);

		Collections.sort(ranking, new Comparator<Player>() {
			public int compare(Player o1, Player o2) {
				return -(o1.getRankings().get(d)).compareTo(o2
						.getRankings().get(d));
			}
		});

		for (Player player : ranking) {
			sb.append(ranking.indexOf(player) + 1).append("/ ").append(
					player.getName()).append(": ").append(
					player.getRankings().get(d)).append(" (").append(
					player.getRankingVariationForDate(d)).append(")\n");
		}

		return sb.toString();
	}

	public static String getCurrentGlobalRankingForDate(
			Collection<Player> players, final Date date) {
		StringBuffer sb = new StringBuffer();

		List<Player> ranking = new ArrayList<Player>();

		ranking.addAll(players);

		Collections.sort(ranking, new Comparator<Player>() {
			public int compare(Player o1, Player o2) {
				return -(o1.getRankings().get(date)).compareTo(o2
						.getRankings().get(date));
			}
		});

		for (Player player : ranking) {
			sb.append(ranking.indexOf(player) + 1).append("/ ").append(
					player.getName()).append(": ").append(player.getRanking())
					.append("\n");
		}

		return sb.toString();
	}

	public void fillBlanksInChronology() {
		SortedSet<Date> allDates = getAllDates();

		for (Player p : loadedPlayers.values()) {
			Long lastNotNullRanking = null;

			for (Date d : allDates) {
				Long rankingForDate = p.getRankings().get(d);
				if (rankingForDate == null && lastNotNullRanking != null) {
					p.getRankings().put(d, lastNotNullRanking);
				} else {
					lastNotNullRanking = rankingForDate;
				}
			}
		}
	}

	public void setResultDataFile(String resultDataFile) {
		this.resultDataFile = resultDataFile;
	}

	public Map<Integer, Player> getLoadedPlayers() {
		return loadedPlayers;
	}

	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	public SortedSet<Date> getAllDates() {
		SortedSet<Date> allDates = new TreeSet<Date>();

		for (Player p : loadedPlayers.values()) {
			Map<Date, Long> previousRankings = p.getRankings();
			for (Date entry : previousRankings.keySet()) {
				allDates.add(entry);
			}
		}

		return allDates;
	}
}
