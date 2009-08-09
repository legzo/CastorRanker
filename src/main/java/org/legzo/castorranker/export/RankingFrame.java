package org.legzo.castorranker.export;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.legzo.castorranker.DataLoader;
import org.legzo.castorranker.ranker.EloModRanker;

public class RankingFrame extends JFrame {

	private static final long serialVersionUID = 8736024574119379284L;
	private ChartPanel chartPanel;
	private JFreeChart chart;

	private JLabel kLabel = new JLabel("k : ");
	private JTextField kTextField = new JTextField("10");
	private JLabel jLabel = new JLabel("j : ");
	private JTextField jTextField = new JTextField("0.1");

	private JButton redrawButton = new JButton("redraw");

	public RankingFrame() {
		super("Ranking Frame");

		setSize(1000, 600);

		setDefaultCloseOperation(2);
		chartPanel = new ChartPanel(chart);
		
		redrawButton.addActionListener(new ClickButonEvent());

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new VerticalLayout());
		
		JPanel toolbarPanel = new JPanel();

		toolbarPanel.setLayout(new FlowLayout());

		toolbarPanel.add(kLabel);
		toolbarPanel.add(kTextField);
		toolbarPanel.add(jLabel);
		toolbarPanel.add(jTextField);
		toolbarPanel.add(redrawButton);

		contentPane.add(toolbarPanel);
		contentPane.add(chartPanel);

		setContentPane(contentPane);
	}

	 public class ClickButonEvent implements ActionListener {
		int cpt = 0;
		
		 public void actionPerformed(ActionEvent e) {

			EloModRanker.setK(Integer.parseInt(kTextField.getText()));
			EloModRanker.setJ(Float.parseFloat(jTextField.getText()));

			DataLoader loader = new DataLoader();

			loader.setDataFile("src/test/resources/scores-full.csv");
			loader.setResultDataFile("target/scores-evol.csv");

			loader.loadData();

			ChartMaker writer = new ChartMaker();
			JFreeChart chart = writer.generateChart(loader.getLoadedPlayers()
					.values(), loader.getAllDates(), 9999);

			
			chartPanel.setChart(chart);
			chartPanel.validate();
		}
	}

}
