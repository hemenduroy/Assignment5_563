import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


public class WindowClass extends JPanel implements Observer {
	protected JTable jTable;
    public WindowClass() {
		this.jTable = new JTable();
		//https://stackoverflow.com/questions/10466079/import-csv-to-jtable/35471186
		JScrollPane scrollPane = new JScrollPane(jTable);
    	setLayout(new BorderLayout());
    	
    	Dimension dimension1 = new Dimension();
		dimension1.setSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.5, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5);
		setPreferredSize(dimension1);
		
		Dimension dimension2 = new Dimension();
		//https://stackoverflow.com/questions/28128035/how-to-add-table-header-and-scrollbar-in-jtable-java
		dimension2.setSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    	setSize(dimension2);
		scrollPane.setSize(dimension2);
    	add(scrollPane);
    	
    }
    
    @Override
    public void update(Observable observable, Object o) {
    	String[][] data = ((RosterData)observable).getTableData();
    	//https://stackoverflow.com/questions/20359228/create-a-two-dimensional-string-array-anarray22

		String[] listHeaders = new String[RosterData.csvHeadersList.size()];
		int i = 0;
		for (String s : RosterData.csvHeadersList) {
			listHeaders[i] = s;
			i++;
		}
    	this.jTable.setModel(new DefaultTableModel(data, listHeaders));
    }
}