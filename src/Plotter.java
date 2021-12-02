import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import javax.swing.*;
import java.time.LocalDate;
import org.jfree.chart.JFreeChart;
import java.util.ArrayList;
import java.util.List;

public class Plotter extends JFrame {

    public Plotter(String title) {
        super(title);

        // https://www.boraji.com/jfreechart-scatter-chart-example
        List<LocalDate> dates = new ArrayList<>();

        // https://www.javatpoint.com/jfreechart-scatter-chart
        for (int i = 6; i < RosterData.csvHeadersList.size(); i++) {
            dates.add(LocalDate.parse(RosterData.csvHeadersList.get(i)));
        }

        //https://www.codejava.net/java-se/graphics/using-jfreechart-to-draw-xy-line-chart-with-xydataset
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (LocalDate date : dates) {
            List<Double> horz = new ArrayList<>();

            for (Student student : RosterData.stuRoster) {
                if (student.getDateAttendance(date) >= 75) {
                    horz.add(100.0);
                } else {
                    double percentage1 = student.getDateAttendance(date) / 75.0 * 100;
                    horz.add(percentage1);
                }
            }
            XYSeries xySeries = new XYSeries(date.toString());

            for (int i = 0; i < horz.size(); i++) {
                int attper = horz.get(i).intValue();
                int counter = 1;

                // check same
                for (int j = i + 1; j < horz.size(); j++) {
                    if (attper == horz.get(j).intValue()) {
                        counter++;
                        horz.remove(j);
                        j--;
                    }
                }
                xySeries.add(attper, counter);
            }
            xySeriesCollection.addSeries(xySeries); // Add data set for each date
        }

        JFreeChart jFreeChart =
                ChartFactory.createScatterPlot(
                        "Attendance", "Percent of Attendance", "Count", xySeriesCollection);

        XYPlot xyPlot = (XYPlot) jFreeChart.getPlot();

        //https://stackoverflow.com/questions/7231824/setting-range-for-x-y-axis-jfreechart
        NumberAxis numberAxis = (NumberAxis) xyPlot.getDomainAxis();
        numberAxis.setRange(-10, 110);
        //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/chart/NumberAxis.html
        //https://stackoverflow.com/questions/35199893/how-to-show-only-numbers-that-are-divisible-by-tick-unit-on-javafx-chart-axis
        numberAxis.setTickUnit(new NumberTickUnit(10));

        NumberAxis plotRangeAxis = (NumberAxis) xyPlot.getRangeAxis();
        plotRangeAxis.setRange(0, RosterData.stuRoster.size() + 0.5);
        plotRangeAxis.setTickUnit(new NumberTickUnit(1));
        plotRangeAxis.setVerticalTickLabels(true);

        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        setContentPane(chartPanel);
    }

}
