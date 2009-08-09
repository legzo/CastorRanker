package org.legzo.castorranker.export;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

import javax.swing.JTextField;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.legzo.castorranker.ranker.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartMaker {

	private static Logger logger = LoggerFactory.getLogger(ChartMaker.class);

	public JFreeChart generateEvolChart(Collection<Player> players,
			SortedSet<Date> allDates, Date sinceDate) {

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (Player player : players) {

			// Create a simple XY chart
			String name = player.getName();

			if (!hasToBeExcluded(name)) {
				XYSeries series = new XYSeries(name);

				for (Date d : allDates) {
					if (d.after(sinceDate)) {
						series.add(d.getTime(), getRankingForDate(player,
								players, d));
					}
				}
				dataset.addSeries(series);
			}
		}

		// Generate the graph
		JFreeChart chart = createXYChart("", // Title
				"", // x-axis Label
				"", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?

		);
		return chart;
	}

	public JFreeChart printEvolChart(Collection<Player> players,
			SortedSet<Date> allDates, String filePath, Date sinceDate) {
		logger.info("Generating evol chart");
		JFreeChart chart = generateEvolChart(players, allDates, sinceDate);
		try {
			ChartUtilities.saveChartAsPNG(new File(filePath), chart, 1000, 600);
		} catch (IOException e) {
			logger.error("Problem occurred creating chart.", e);
		}

		return chart;
	}

	public JFreeChart printChart(Collection<Player> players,
			SortedSet<Date> allDates, String filePath, int maxItems) {
		logger.info("Generating chart for {} dates", maxItems);
		JFreeChart chart = generateChart(players, allDates, maxItems);
		try {
			ChartUtilities.saveChartAsPNG(new File(filePath), chart, 1000, 600);
		} catch (IOException e) {
			logger.error("Problem occurred creating chart.", e);
		}

		return chart;
	}

	public JFreeChart generateChart(Collection<Player> players,
			SortedSet<Date> allDates, int maxItems) {

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();

		for (Player player : players) {

			// Create a simple XY chart
			String name = player.getName();

			if (!hasToBeExcluded(name)) {
				XYSeries series = new XYSeries(name);
				Map<Date, Long> previousRankings = player.getRankings();
				int i = 0;

				for (Date d : allDates) {
					series.add(d.getTime(), previousRankings.get(d));
					if (i >= maxItems - 1) {
						break;
					}
					i++;
				}
				dataset.addSeries(series);
			}
		}

		// Generate the graph
		JFreeChart chart = createXYSplineChart("", // Title
				"", // x-axis Label
				"", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?

		);
		return chart;
	}

	private boolean hasToBeExcluded(String name) {
		return name.equals("CPU") || name.equals("louis")
				|| name.equals("franck");
	}

	private JFreeChart createXYSplineChart(String title, String xAxisLabel,
			String yAxisLabel, XYDataset dataset, PlotOrientation orientation,
			boolean legend, boolean tooltips, boolean urls) {
		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		DateAxis xAxis = new DateAxis(xAxisLabel);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);
		yAxis.setAutoRange(false);
		yAxis.setLowerBound(750);
		yAxis.setUpperBound(1600);
		XYItemRenderer renderer = new XYSplineRenderer();
		// XYItemRenderer renderer = new XYLineAndShapeRenderer();
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(orientation);
		if (tooltips) {
			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		}
		if (urls) {
			renderer.setURLGenerator(new StandardXYURLGenerator());
		}
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, legend);
		return chart;
	}

	private JFreeChart createXYChart(String title, String xAxisLabel,
			String yAxisLabel, XYDataset dataset, PlotOrientation orientation,
			boolean legend, boolean tooltips, boolean urls) {
		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		DateAxis xAxis = new DateAxis(xAxisLabel);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);
		yAxis.setInverted(true);
		XYItemRenderer renderer = new XYSplineRenderer();
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(orientation);
		if (tooltips) {
			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		}
		if (urls) {
			renderer.setURLGenerator(new StandardXYURLGenerator());
		}
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, legend);
		return chart;
	}

	protected int getRankingForDate(Player p, Collection<Player> players, Date d) {
		int result = 1;

		Long ranking = p.getRankings().get(d);

		for (Player player : players) {
			if (ranking < player.getRankings().get(d)) {
				result++;
			}
		}
		logger.info("return ranking {} for {}, on the {}", new Object[] {
				result, p.getName(), d });

		return result;

	}

	/**
	 * showing jfreechart frame
	 */
	public void showChart(JFreeChart chart) {
		ChartFrame frame1 = new ChartFrame("XYLine Chart", chart);

		JTextField kField = new JTextField();
		kField.setSize(40, 10);
		kField.setText("k value");
		kField.setLocation(5, 5);

		frame1.add(kField);

		frame1.setSize(1000, 400);
		frame1.setVisible(true);
	}

	/**
	 * showing my own version a the frame (with a few textfields)
	 */
	public void showWindow() {
		RankingFrame myFrame = new RankingFrame();
		myFrame.setVisible(true);
	}

}
