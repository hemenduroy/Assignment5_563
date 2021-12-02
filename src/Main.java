import javax.swing.*;
import java.awt.*;

//https://stackoverflow.com/questions/15867148/why-do-we-need-to-extend-jframe-in-a-swing-application
public class Main extends JFrame {
		
	protected static RosterData rosterData;

	public Main()
	{
	    //https://stackoverflow.com/questions/31245320/how-to-add-a-button-to-a-jframe-gui/54291440
        setLayout(new BorderLayout());

        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenuBar.add(jMenu);
        JMenuItem about = new JMenuItem("About");
        jMenuBar.add(about);
        JMenuItem load_a_roster = new JMenuItem("Load a Roster");
        jMenu.add(load_a_roster);
        JMenuItem add_attendance = new JMenuItem("Add Attendance");
        jMenu.add(add_attendance);
        JMenuItem save = new JMenuItem("Save");
        jMenu.add(save);
        JMenuItem plot_data = new JMenuItem("Plot Data");
        jMenu.add(plot_data);
        add(jMenuBar);
        setJMenuBar(jMenuBar);
        setTitle("Attendance System");
        
        WindowClass panel = new WindowClass();
        //https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html
        add(panel, BorderLayout.CENTER);

        //http://www.javaquizplayer.com/blogposts/java-swing-example-using-observer-and-observable-7.html
        rosterData = new RosterData();
        rosterData.addObserver(panel);

        load_a_roster.addActionListener(e -> {
            String filePath;
            try {
                filePath = new FileSelect().getFile().getAbsolutePath();
            } catch(NullPointerException e1) {
                filePath = null;
            }
            if (filePath != null) {
                rosterData.load(filePath);
            }
        });
        
        add_attendance.addActionListener(e -> {
            if (RosterData.stuRoster != null) {
                String inputFilepath;
                try {
                    inputFilepath = new FileSelect().getFile().getAbsolutePath();
                } catch(NullPointerException e1) {
                    inputFilepath = null;
                }
                if (inputFilepath != null) {
                    DateSelect dateSelect = new DateSelect();
                    dateSelect.dateSelect(inputFilepath);
                }
            } else {
                JFrame frame = new JFrame();
                JDialog dialog = new JDialog(frame, "ERROR!");
                JPanel panel1 = new JPanel();
                JLabel message = new JLabel("Please load the Roster!");

                panel1.add(message);
                dialog.add(panel1);

                //https://stackoverflow.com/questions/20293220/swing-set-a-fixed-window-size-for-jdialog
                dialog.setSize(400, 100);
                //https://stackoverflow.com/questions/25583002/jdialog-not-showing
                dialog.setVisible(true);
            }
        });
        
        save.addActionListener(e -> {
            if(RosterData.stuRoster != null) {
                String inputFilepath;
                try {
                    inputFilepath = new FileSelect().getFileToSave().getAbsolutePath();
                } catch(NullPointerException e1) {
                    inputFilepath = null;
                }
                if(inputFilepath != null) {
                    rosterData.save(inputFilepath);
                }
            }
            else {
                // Display error for when the roster has not been loaded
                JFrame frame = new JFrame();
                JDialog dialog = new JDialog(frame, "ERROR!");
                JPanel panel1 = new JPanel();
                JLabel message = new JLabel("Please load the Roster!");

                panel1.add(message);
                dialog.add(panel1);

                //https://stackoverflow.com/questions/20293220/swing-set-a-fixed-window-size-for-jdialog
                dialog.setSize(400, 100);
                //https://stackoverflow.com/questions/25583002/jdialog-not-showing
                dialog.setVisible(true);
            }
        });
        
        plot_data.addActionListener(e -> {
            if(RosterData.stuRoster != null) {
                SwingUtilities.invokeLater(
                        () -> {
                            Plotter sp = new Plotter("Plot Data");
                            sp.setSize(800, 400);
                            sp.setLocationRelativeTo(null);
                            sp.setVisible(true);
                        });
            }
            else {
                // Display error for when the roster has not been loaded
                JFrame frame = new JFrame();
                JDialog dialog = new JDialog(frame, "Error");
                JPanel panel1 = new JPanel();
                JLabel message = new JLabel("ERROR: Roster must be loaded first");

                panel1.add(message);
                dialog.add(panel1);

                //https://stackoverflow.com/questions/20293220/swing-set-a-fixed-window-size-for-jdialog
                dialog.setSize(300, 70);
                dialog.setVisible(true);
            }
        });
        
        about.addActionListener(e -> {
            JFrame frame = new JFrame();
            JDialog dialog = new JDialog(frame, "About");
            JPanel panel1 = new JPanel();
            String teamList ="Team - Hemendu Roy, Het Vallabhbhai Mendpara, Shivam Mathur, Vijay Maddineni";
            JLabel teamBox = new JLabel(teamList);
            panel1.add(teamBox);
            dialog.add(panel1);
            dialog.setSize(800, 200);
            dialog.setVisible(true);
        });

	}

	public static void main(String[] args) {
        Main main = new Main();
        //https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe/1235994
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(main.getPreferredSize());
        main.setVisible(true);

	}

}
