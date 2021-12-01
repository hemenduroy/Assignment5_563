import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class Panel extends JPanel implements Observer {
    
	protected JTable table;


    public Panel() {
		this.table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
    	setLayout(new BorderLayout());
    	
    	Dimension dimension = new Dimension();
		dimension.setSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.5, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5);
		setPreferredSize(dimension);
		
		Dimension screen = new Dimension();
		screen.setSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    	setSize(screen);
		scrollPane.setSize(screen);
    	add(scrollPane);
    	
    }
    
    @Override
    public void update(Observable o, Object arg) {
    	String[][] data = ((RosterDatabase)o).getTableData();

		String[] headersArr = new String[RosterDatabase.headers.size()];
		int i = 0;
		for (String s : RosterDatabase.headers) {
			headersArr[i] = s;
			i++;
		}
		String[] headers = headersArr;

    	for(String[] arr : data) {
    		System.out.println(Arrays.toString(arr));
    	}
    	this.table.setModel(new DefaultTableModel(data, headers));
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}