import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.Arrays;
import java.util.Observer;
import java.util.Observable;


public class WindowClass extends JPanel implements Observer {
	protected JTable jTable;
    public WindowClass() {
		this.jTable = new JTable();
		//https://stackoverflow.com/questions/10466079/import-csv-to-jtable/35471186
		JScrollPane scrollPane = new JScrollPane(jTable);
    	setLayout(new BorderLayout());
    	
    	Dimension dimension = new Dimension();
		dimension.setSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.5, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5);
		setPreferredSize(dimension);
		
		Dimension screen = new Dimension();
		//https://stackoverflow.com/questions/28128035/how-to-add-table-header-and-scrollbar-in-jtable-java
		screen.setSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    	setSize(screen);
		scrollPane.setSize(screen);
    	add(scrollPane);
    	
    }
    
    @Override
    public void update(Observable o, Object arg) {
    	String[][] data = ((RosterData)o).getTableData();
    	//https://stackoverflow.com/questions/20359228/create-a-two-dimensional-string-array-anarray22

		String[] headersArr = new String[RosterData.csvHeadersList.size()];
		int i = 0;
		for (String s : RosterData.csvHeadersList) {
			headersArr[i] = s;
			i++;
		}

		for(String[] arr : data) {
    		System.out.println(Arrays.toString(arr));
    	}
    	this.jTable.setModel(new DefaultTableModel(data, headersArr));
    }
}