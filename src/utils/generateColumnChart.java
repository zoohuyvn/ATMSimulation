package utils;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import bases.variables;

/**
 * @author zoohuy
 * 28 thg 12, 2023
 */

public class generateColumnChart extends JPanel {
	public ChartPanel chartPanel;
	private CategoryDataset dataset;
	private static ArrayList dataChart;
	public JFreeChart chart;
	
	public void gnrt(String title, ArrayList<String[]> dataChartInput) {
		dataChart = dataChartInput;
        dataset = createDataset();
        chart = ChartFactory.createBarChart(title, "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
        chartPanel = new ChartPanel(chart);
        applyChartTheme(chart);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setOutlineVisible(false);
        renderer.setGradientPaintTransformer(null);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, Color.decode(variables.primaryColorLight));
        renderer.setSeriesPaint(1, Color.decode(variables.primaryColorDark));
        renderer.setSeriesPaint(2, Color.decode(variables.primaryColor));
    }

    private static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = dataChart.size();
        for (int i = 0; i < count; ++i) {
        	String[] arr = (String[]) dataChart.get(i);
        	String date = arr[0];
        	double rechargeSum = Double.parseDouble((arr[1].isEmpty()) ? "0" : arr[1]);
            double receiveSum = Double.parseDouble((arr[3].isEmpty()) ? "0" : arr[3]);
            double transferSum = Double.parseDouble((arr[2].isEmpty()) ? "0" : arr[2]);
            dataset.addValue(rechargeSum, "Recharge", date);
            dataset.addValue(transferSum, "Transfer", date);
            dataset.addValue(receiveSum, "Receive", date);
        }
        return dataset;
    }
    
    public static void applyChartTheme(JFreeChart chart) {
        final StandardChartTheme chartTheme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();
        final Font extraLargeFont = new Font("Roboto", Font.PLAIN, 19);
        final Font largeFont = new Font("Roboto", Font.PLAIN, 17);
        final Font regularFont = new Font("Roboto", Font.PLAIN, 16);
        final Font smallFont = new Font("Roboto", Font.PLAIN, 15);
        chartTheme.setExtraLargeFont(extraLargeFont);
        chartTheme.setLargeFont(largeFont);
        chartTheme.setRegularFont(regularFont);
        chartTheme.setSmallFont(smallFont);
        chartTheme.apply(chart);
    }
    
    public static void exportToPng(JFreeChart chartInput) {
    	String chartSavePath = "C:\\Users\\USER\\eclipse-workspace\\ATMSimulation\\src\\assets\\chart.png";
    	try {
			ChartUtils.saveChartAsPNG(new File(chartSavePath), chartInput, 1040, 800);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}