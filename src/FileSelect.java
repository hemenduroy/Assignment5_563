import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelect extends JFileChooser {

    private final JFileChooser selector = new JFileChooser();

    public FileSelect() {
        //https://www.tabnine.com/code/java/classes/javax.swing.filechooser.FileNameExtensionFilter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        selector.setFileFilter(filter);
    }

    public File getFile() {
        File csvFile;
        /*
        https://stackoverflow.com/questions/21372907/how-to-place-the-jfilechooser-on-top-of-the-specific-parent-window-frame
        https://www.tabnine.com/code/java/methods/javax.swing.JFileChooser/showOpenDialog
        */
        int toret = selector.showOpenDialog(getParent());
        csvFile = selector.getSelectedFile();

        if (toret == JFileChooser.APPROVE_OPTION) {
            //http://www.java2s.com/Tutorial/Java/0240__Swing/JFileChooserSelectionOptionJFileChooserAPPROVEOPTIONJFileChooserCANCELOPTION.htm
            String fileName = csvFile.getName();
            if (!fileName.substring(fileName.lastIndexOf('.')).equals(".csv")) {
                csvFile = null;
            }
        } else {
            csvFile = null;
        }
        return csvFile;
    }

    public File getFileToSave() {
        File csvFile;
        //https://stackoverflow.com/questions/22261130/how-to-save-a-file-using-jfilechooser-showsavedialog
        int returnVal = selector.showSaveDialog(getParent());
        csvFile = selector.getSelectedFile();
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            csvFile = null;
        }
        //https://coderanch.com/t/278474/java/Writing-CSV-File-Database-Generates
        assert csvFile != null;
        if (csvFile.getName().indexOf('.') == -1) {
            csvFile = new File(csvFile + ".csv");
        } else if (!csvFile.getName()
                .substring(csvFile.getName().indexOf('.'))
                .equalsIgnoreCase(".csv")) {
            csvFile = null;
        }
        return csvFile;
    }
}
