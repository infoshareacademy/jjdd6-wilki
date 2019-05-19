package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.properties.WebAppProperties;
import com.infoshareacademy.jjdd6.wilki.DataFromFile;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;
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
    private DownloadCurrentData downloadCurrentData;

    @Inject
    private DownloaderService downloaderService;

    @Inject
    private TickerService tickerService;

    @Inject
    private WebAppProperties webAppProperties;

    @Inject
    private StatsService statsService;

    private static Logger logger = LoggerFactory.getLogger(ChartGenerator.class);

    public String getChart(String ticker, LocalDate fromDate, LocalDate toDate, Double buyPrice) throws MalformedURLException {
        List<DataFromFile> currentData = downloadCurrentData.get(ticker);
        List<DataFromFile> data = downloaderService.getHistoricalData(ticker, fromDate, toDate);
        String title = tickerService.scanTickers(ticker.toUpperCase()) + " (" + ticker.toUpperCase() + ")";
        String filename = pathGenerator(ticker);
        String path = webAppProperties.getSetting("CHART_LOCATION") + "/" + filename;
        generateChart(title, path, currentData, data, buyPrice);
        return filename;
    }

    private void generateChart(String title, String path, List<DataFromFile> currentData, List<DataFromFile> data, Double buyPrice) {
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
        chart.getStyler().setChartTitlePadding(0);
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

        xAxis.add(Date.from(currentData.get(0).getDate().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        yAxis.add(currentData.get(0).getClosingPrice().doubleValue());


        chart.addSeries(title.toUpperCase(), xAxis, yAxis);

        if (buyPrice > 0.0) {
            List<Double> buyPriceData = generateAvgBuyPriceData(yAxis, buyPrice);
            XYSeries buyPriceSeries = chart.addSeries("BuyPrice", xAxis, buyPriceData);
            buyPriceSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        }

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapEncoder.BitmapFormat.PNG, 150);
        } catch (IOException e) {
            logger.info("Writing chart file failed.");
        }
    }

    private List<Double> generateAvgBuyPriceData(List<Double> data, Double avgBuyPrice) {
        List<Double> avgBuyPriceList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            avgBuyPriceList.add(avgBuyPrice);
        }
        return avgBuyPriceList;
    }

    public String getMiniChart(String ticker, LocalDate fromDate) throws MalformedURLException {
        List<DataFromFile> currentData = downloadCurrentData.get(ticker);
        LocalDate toDate = LocalDate.now();
        List<DataFromFile> data = downloaderService.getHistoricalData(ticker, fromDate, toDate);
        int i = 0;
        while (data.size() < Integer.parseInt(webAppProperties.getSetting("MINI_CHART_HOW_MANY_DAYS_BACK"))) {
            data = downloaderService.getHistoricalData(ticker, fromDate.minusDays(i), toDate);
            i++;
        }
        String title = tickerService.scanTickers(ticker.toUpperCase()) + " (" + ticker.toUpperCase() + ")";
        String filename = pathGenerator(ticker);
        String path = webAppProperties.getSetting("CHART_LOCATION") + "/" + filename;
        generateMiniChart(title, path, currentData, data);
        return filename;
    }

    private void generateMiniChart(String title, String path, List<DataFromFile> currentData, List<DataFromFile> data) {
        XYChart chart = new XYChartBuilder().width(600).height(300).title(title).xAxisTitle("").yAxisTitle("Closing price").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setAntiAlias(true);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTicksVisible(false);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setPlotBorderVisible(false);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setXAxisLogarithmic(true);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setSeriesColors(new Color[]{Color.GRAY});
        chart.getStyler().setChartFontColor(Color.BLACK);

        List<Date> xAxis = new ArrayList<>();
        List<Double> yAxis = new ArrayList<>();

        for (DataFromFile datum : data) {
            xAxis.add(Date.from(datum.getDate().atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()));
            yAxis.add(datum.getClosingPrice().doubleValue());
        }

        xAxis.add(Date.from(currentData.get(0).getDate().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        yAxis.add(currentData.get(0).getClosingPrice().doubleValue());

        XYSeries series = chart.addSeries(title.toUpperCase(), xAxis, yAxis);
        series.setFillColor(new Color(114, 115, 125));
        series.setLineColor(new Color(61, 62, 69));

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapEncoder.BitmapFormat.PNG, 150);
        } catch (IOException e) {
            logger.info("Writing chart file failed.");
        }
    }

    public String pathGenerator(String ticker) {
        int random = (int) (Math.random() * 1000000);
        return ticker + "_" + random + "_chart.png";
    }

    public String getMostTradedBuyChart() {

        String title = "Most Traded Stocks in App (buy)";
        String filename = pathGenerator("most_traded_buy");
        String path = webAppProperties.getSetting("CHART_LOCATION") + "/" + filename;
        generatePieChart(title, path, statsService.getMostBoughtStocks());
        return filename;
    }

    public String getMostTradedSellChart() {

        String title = "Most Traded Stocks in App (sell)";
        String filename = pathGenerator("most_traded_sell");
        String path = webAppProperties.getSetting("CHART_LOCATION") + "/" + filename;
        generatePieChart(title, path, statsService.getMostSoldStocks());
        return filename;
    }

    public String getMostTradedWSEChart() {

        String title = "Most Traded Today on WSE (volume)";
        String filename = pathGenerator("most_traded_wse");
        String path = webAppProperties.getSetting("CHART_LOCATION") + "/" + filename;
        generatePieChart(title, path, statsService.getMostTradedOnWse());
        return filename;
    }

    private void generatePieChart(String title, String path, List<String[]> data) {
        PieChart chart = new PieChartBuilder().width(350).height(300).title(title).build();
        chart.getStyler().setChartBackgroundColor(new Color(13, 15, 58));
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAnnotationType(PieStyler.AnnotationType.Label);
        chart.getStyler().setChartFontColor(Color.BLACK);
        chart.getStyler().setChartTitleBoxVisible(true);
        chart.getStyler().setChartTitleBoxBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBorderColor(Color.WHITE);
        for (int i = 0; i < data.size(); i++) {
            chart.addSeries(data.get(i)[0], Double.parseDouble(data.get(i)[1]));
        }

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, path, BitmapEncoder.BitmapFormat.PNG, 150);
        } catch (IOException e) {
            logger.info("Writing chart file failed.");
        }
    }

}