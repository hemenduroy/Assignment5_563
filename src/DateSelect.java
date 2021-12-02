import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class DateSelect {

    public void dateSelect(String filepath) {
        JFrame jFrame = new JFrame("Calendar");
        JPanel jPanel = new JPanel();
        JButton jButton = new JButton("Confirm");
        JLabel jLabel = new JLabel("Select date of attendance:");

        jFrame.setBounds(300, 300, 300, 150);

        //https://stackoverflow.com/questions/11736878/display-calendar-to-pick-a-date-in-java
        JXDatePicker jxDatePicker = new JXDatePicker();
        jxDatePicker.setDate(Calendar.getInstance().getTime());
        jxDatePicker.setFormats(new SimpleDateFormat("MM/dd/yyyy"));

        jPanel.add(jLabel);
        jPanel.add(jxDatePicker);
        jPanel.add(jButton);
        jFrame.getContentPane().add(jPanel);
        jFrame.setVisible(true);

        jButton.addActionListener(
                e -> {
                    Date date = jxDatePicker.getDate();
                    int gMonth = date.getMonth() + 1;
                    int gDay = date.getDate();
                    int gYear = date.getYear() + 1900;

                    LocalDate localDate = LocalDate.of(gYear, gMonth, gDay);
                    Main.rosterData.recAttendance(localDate, filepath);

                    JFrame jFrame1 = new JFrame();
                    JDialog jDialog = new JDialog(jFrame1, "Message");

                    String msgld = "Data loaded for " + RosterData.addStudent + " users in the roster.";
                    String msgp =  RosterData.newStudents.size()  + " additional attendee(s) was found\n";

                    JPanel panel1 = new JPanel();
                    JLabel msgldLabel = new JLabel(msgld);
                    JLabel msgpLabel = new JLabel(msgp);

                    panel1.setLayout(new FlowLayout());
                    panel1.add(msgldLabel);
                    panel1.add(msgpLabel);

                    String newmsg;

                    if (!RosterData.newStudents.isEmpty()) {
                        for (Map.Entry<String, Integer> i : RosterData.newStudents.entrySet()) {
                            newmsg = i.getKey() + ", connected for " + i.getValue() + " minute(s)\n";
                            JLabel additionalLabel = new JLabel(newmsg);
                            panel1.add(additionalLabel);
                        }
                    }


                    //https://stackoverflow.com/questions/2452694/jtable-with-horizontal-scrollbar
                    jDialog.add(new JScrollPane(panel1));


                    jDialog.setSize(800, 100);
                    jDialog.setVisible(true);

                    RosterData.newStudents.clear();
                    RosterData.addStudent = 0;
                    jFrame.dispose(); // Close date picker GUI
                });
    }
}
