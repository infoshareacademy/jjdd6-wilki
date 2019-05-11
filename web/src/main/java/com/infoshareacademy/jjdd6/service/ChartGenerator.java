package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.wilki.DataFromFile;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestScoped
public class ChartGenerator {

    @Inject
    DownloadCurrentData downloadCurrentData;

    private static Logger logger = LoggerFactory.getLogger(ChartGenerator.class);

    public String getChart(String ticker, String path) throws MalformedURLException {

        List<DataFromFile> data = downloadCurrentData.getHistoricalData(ticker);
        String title = downloadCurrentData.loadAndScanTickers(ticker.toUpperCase()) + " (" + ticker.toUpperCase() + ")";
        generateChart(title, path, data);
        return ".png";
    }

    public String getChart(String ticker, String path, LocalDate fromDate, LocalDate toDate) throws MalformedURLException {

        List<DataFromFile> data = downloadCurrentData.getHistoricalData(ticker, fromDate, toDate);
        String title = downloadCurrentData.loadAndScanTickers(ticker.toUpperCase()) + " (" + ticker.toUpperCase() + ")";
        generateChart(title, path, data);
        return ".png";
    }

    private void generateChart(String title, String path, List<DataFromFile> data) {
        XYChart chart = new XYChartBuilder().width(600).height(300).title(title).xAxisTitle("").yAxisTitle("Closing price").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        List<Date> xAxis = new ArrayList<>();
        List<Double> yAxis = new ArrayList<>();
        chart.getStyler().setAntiAlias(true);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setChartBackgroundColor(new Color(13, 15, 58));
        chart.getStyler().setChartFontColor(Color.WHITE);
        chart.getStyler().setChartTitlePadding(20);
        chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(new Color(13, 15, 58));
        chart.getStyler().setPlotGridLinesColor(new Color(114, 115, 125));
        chart.getStyler().setDatePattern("dd-MM-YYYY");
        chart.getStyler().setPlotBorderVisible(false);
        chart.getStyler().setXAxisLogarithmic(true);
        chart.getStyler().setMarkerSize(0);

        for (DataFromFile datum : data) {
            xAxis.add(Date.from(datum.getDate().atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()));
            yAxis.add(datum.getClosingPrice().doubleValue());
        }
        chart.addSeries(title.toUpperCase(), xAxis, yAxis);

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            logger.info("Writing chart file failed.");
        }
    }

}
