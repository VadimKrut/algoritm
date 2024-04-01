package com.vado.sic.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChartUtil {

    public static void createChartAndSave(List<Integer> dataSizes, List<Long> encryptTimes, List<Long> decryptTimes, List<Long> totalTimes, String chartTitle, String filename) {
        XYSeries encryptSeries = new XYSeries("Время шифрования");
        XYSeries decryptSeries = new XYSeries("Время дешифрования");
        XYSeries totalSeries = new XYSeries("Время в целом");

        for (int i = 0; i < dataSizes.size(); i++) {
            encryptSeries.add(dataSizes.get(i), encryptTimes.get(i));
            decryptSeries.add(dataSizes.get(i), decryptTimes.get(i));
            totalSeries.add(dataSizes.get(i), totalTimes.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(encryptSeries);
        dataset.addSeries(decryptSeries);
        dataset.addSeries(totalSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                "Размер информации (байты)",
                "Время (мс)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        saveChart(chart, filename);
    }

    private static void saveChart(JFreeChart chart, String filename) {
        try {
            File directory = new File("src/main/resources/graf");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, filename + ".png");
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 600));
            ChartUtils.saveChartAsPNG(file, chart, 800, 600);
            System.out.println("Chart saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}