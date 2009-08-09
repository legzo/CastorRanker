package org.legzo.castorranker.export;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import org.jfree.date.DateUtilities;
import org.legzo.castorranker.DataLoader;
import org.legzo.castorranker.ranker.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlGenerator {

	private static Logger logger = LoggerFactory.getLogger(HtmlGenerator.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy/MM/dd");
	private static SimpleDateFormat dateFormatForFile = new SimpleDateFormat(
			"yyyyMMdd");

	public void generateSite(Collection<Player> players,
			SortedSet<Date> allDates) {
		try {
			Writer writer = new FileWriter("target/index.html");
			writer.append(getPageContent(players, allDates));
			if (writer != null) {
				writer.close();
			}
		} catch (Exception e) {// Catch exception if any
			logger.error("Error occured", e);
		}
	}

	private String getPageContent(Collection<Player> players,
			SortedSet<Date> allDates) {

		List<Date> dates = sortDates(allDates);

		StringBuffer sb = new StringBuffer();

		sb.append("<HTML><HEAD/><TITLE>Rankings on the ");
		sb.append(dateFormat.format(dates.get(0)));
		sb.append("</TITLE>");

		appendCharts(players, allDates, dates, sb);

		sb.append("</HTML>");

		return sb.toString();
	}

	private List<Date> sortDates(SortedSet<Date> allDates) {
		List<Date> dates = new ArrayList<Date>();
		dates.addAll(allDates);
		Collections.sort(dates, new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				return -o1.compareTo(o2);
			}
		});
		return dates;
	}

	private void appendCharts(Collection<Player> players,
			SortedSet<Date> allDates, List<Date> dates, StringBuffer sb) {
		ChartMaker writer = new ChartMaker();
		int i = dates.size();

		
		sb.append("<H1>Evol :</H1>");
		writer.printEvolChart(players, allDates, "target/evol.png", DateUtilities.createDate(2009, 4, 18));
		sb.append("<IMG src=\"evol.png\">");
		
		sb.append("<H1>Charts :</H1>");

		for (Date date : dates) {

			String formattedDate = dateFormat.format(date);
			String formattedDateForFile = dateFormatForFile.format(date);

			sb.append("<BR><BR>");
			sb.append("<H2>").append(formattedDate).append("</H2>");
			sb.append(
					DataLoader.getCurrentGlobalRanking(players, date)
							.replaceAll("\\\n", "<BR>"));
			sb.append("<BR>");
			sb.append("<IMG src=\"scores-evol-" + formattedDateForFile
					+ ".png\">");

			File file = new File("target/scores-evol-" + formattedDateForFile
					+ ".png");

			if (!file.exists()) {
				writer.printChart(players, allDates, "target/scores-evol-"
						+ formattedDateForFile + ".png", i);
			}

			i--;
		}
	}

}
