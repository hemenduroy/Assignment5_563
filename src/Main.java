import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
		
	protected static RosterDatabase rosterDatabase;

	public Main()
	{   
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem about = new JMenuItem("About");
        menuBar.add(about);

        //file menu items
        JMenuItem loadRoster = new JMenuItem("Load a Roster");
        file.add(loadRoster);

        JMenuItem addAttendance = new JMenuItem("Add Attendance");
        file.add(addAttendance);

        JMenuItem saveRoster = new JMenuItem("Save");
        file.add(saveRoster);

        JMenuItem plotData = new JMenuItem("Plot Data");
        file.add(plotData);

        add(menuBar);
        setJMenuBar(menuBar);
        
        setTitle("Attendance System");
        
        Panel panel = new Panel();
        add(panel, BorderLayout.CENTER);
        
        rosterDatabase = new RosterDatabase();
        rosterDatabase.addObserver(panel);
        
        
        
       
        loadRoster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath;
                try {
                    filePath = new jFileChooser().getOpenFile().getAbsolutePath();
                } catch(NullPointerException e1) {
                    filePath = null;
                }
                if (filePath != null) {
                    rosterDatabase.load(filePath);
                }
            }
        });
        
        addAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RosterDatabase.roster != null) {
                    String inputFilepath;
                    try {
                        inputFilepath = new jFileChooser().getOpenFile().getAbsolutePath();
                    } catch(NullPointerException e1) {
                        inputFilepath = null;
                    }
                    if (inputFilepath != null) {
                        DateSelect dateSelect = new DateSelect();
                        dateSelect.dateSelect(inputFilepath);
                    }
                } else {
                    // Display error for when the roster has not been loaded
                    JFrame frame = new JFrame();
                    JDialog dialog = new JDialog(frame, "Error");
                    JPanel panel1 = new JPanel();
                    JLabel message = new JLabel("ERROR: Roster must be loaded first");

                    panel1.add(message);
                    dialog.add(panel1);

                    dialog.setSize(300, 70);
                    dialog.setVisible(true);
                }
            }
        });
        
        saveRoster.addActionListener(e -> {
            if(RosterDatabase.roster != null) {
                String inputFilepath;
                try {
                    inputFilepath = new jFileChooser().getSaveFile().getAbsolutePath();
                } catch(NullPointerException e1) {
                    inputFilepath = null;
                }
                if(inputFilepath != null) {
                    rosterDatabase.save(inputFilepath);
                }
            }
            else {
                // Display error for when the roster has not been loaded
                JFrame frame = new JFrame();
                JDialog dialog = new JDialog(frame, "Error");
                JPanel panel1 = new JPanel();
                JLabel message = new JLabel("ERROR: Roster must be loaded first");

                panel1.add(message);
                dialog.add(panel1);

                dialog.setSize(300, 70);
                dialog.setVisible(true);
            }
        });
        
        plotData.addActionListener(e -> {
            if(RosterDatabase.roster != null) {
                SwingUtilities.invokeLater(
                        () -> {
                            ScatterPlot sp = new ScatterPlot("Plot Data");
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

                dialog.setSize(300, 70);
                dialog.setVisible(true);
            }
        });
        
        about.addActionListener(e -> {
            JFrame frame = new JFrame();
            JDialog dialog = new JDialog(frame, "About");
            JPanel panel1 = new JPanel();


            String teamInfo ="Team - Hemendu Roy, Het Vallabhbhai Mendpara, Shivam Mathur, Vijay Maddineni";
            JLabel teamInfoLabel = new JLabel(teamInfo);

            panel1.add(teamInfoLabel);
            dialog.add(panel1);


            dialog.setSize(900, 250);
            dialog.setVisible(true);
        });

	}

	public static void main(String[] args) {
        Main main = new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(main.getPreferredSize());
        main.setVisible(true);

	}

}
