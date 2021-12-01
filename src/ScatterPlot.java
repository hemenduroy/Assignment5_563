import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.*;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class ScatterPlot extends JFrame {

    public ScatterPlot(String title) {
        super(title);

        // https://www.boraji.com/jfreechart-scatter-chart-example
        List<LocalDate> dates = new ArrayList<LocalDate>();

        // https://www.javatpoint.com/jfreechart-scatter-chart
        for (int i = 6; i < RosterDatabase.headers.size(); i++) {
            dates.add(LocalDate.parse(RosterDatabase.headers.get(i)));
        }

        //https://www.codejava.net/java-se/graphics/using-jfreechart-to-draw-xy-line-chart-with-xydataset
        XYSeriesCollection dataset1 = new XYSeriesCollection();

        for (LocalDate date : dates) {
            List<Double> xAxis1 = new ArrayList();

            for (Student student : RosterDatabase.roster) {
                if (student.getDateAttendance(date) >= 75) {
                    xAxis1.add(100.0);
                } else {
                    double percentage1 = student.getDateAttendance(date) / 75.0 * 100;
                    xAxis1.add(percentage1);
                }
            }
            List<Double> xAxis = xAxis1;
            XYSeries classData = new XYSeries(date.toString());

            for (int i = 0; i < xAxis.size(); i++) {
                int percentage = xAxis.get(i).intValue();
                int count = 1;

                // Count the number of duplicates
                for (int j = i + 1; j < xAxis.size(); j++) {
                    if (percentage == xAxis.get(j).intValue()) {
                        count++;
                        xAxis.remove(j);
                        j--;
                    }
                }
                classData.add(percentage, count);
            }
            dataset1.addSeries(classData); // Add data set for each date
        }

        XYDataset dataset = dataset1; // Get data

        // Create scatter plot from JFreeChart
        JFreeChart chart =
                ChartFactory.createScatterPlot(
                        "Attendance", "Percent of Attendance", "Count", dataset);

        // Change background color of plot
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 228, 196));

        // Set x-axis range
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setRange(-5, 105);
        domain.setTickUnit(new NumberTickUnit(10));
        domain.setVerticalTickLabels(true);

        // Set y-axis range
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setRange(0, RosterDatabase.roster.size() + 0.5);
        range.setTickUnit(new NumberTickUnit(1));
        range.setVerticalTickLabels(true);

        // Create panel to display plot
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

}
