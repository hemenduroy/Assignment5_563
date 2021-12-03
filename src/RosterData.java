import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class RosterData extends Observable {

    public static List<Student> stuRoster;
    public static List<String> csvHeadersList;
    public static final int headers2 = 6;
    public static int addStudent = 0;
    public static LinkedHashMap<String, Integer> newStudents;
    public static final String delimiter = ",";
    public static List<LocalDate> dates;

    //https://www.geeksforgeeks.org/linkedhashmap-class-java-examples/
    //https://stackoverflow.com/questions/26623129/when-to-use-linkedhashmap-over-hashmap-in-java
    public RosterData() {
        csvHeadersList = new ArrayList<>();
        csvHeadersList.add("ID");
        csvHeadersList.add("First Name");
        csvHeadersList.add("Last Name");
        csvHeadersList.add("Program");
        csvHeadersList.add("Level");
        csvHeadersList.add("ASURITE");
        newStudents = new LinkedHashMap<>();
        dates = new ArrayList<>();
    }

    public void read(String csvFile) {
        List<Student> studentList = new ArrayList<>();
        try {
            File file = new File(csvFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //https://stackoverflow.com/questions/11990211/reading-csv-file-using-bufferedreader-resulting-in-reading-alternative-lines
            String line;
            String[] studentAttributes;
            line = bufferedReader.readLine();
            studentAttributes = line.split(delimiter);
            if (studentAttributes[0].equals("ID")) {
                csvHeadersList.addAll(Arrays.asList(studentAttributes).subList(csvHeadersList.size(), studentAttributes.length));
            } else {
                studentList.add(createStudent(studentAttributes));
            }
            while ((line = bufferedReader.readLine()) != null) {
                studentAttributes = line.split(delimiter);
                studentList.add(createStudent(studentAttributes));
            }
            //https://www.codeproject.com/Questions/1106420/Getting-exception-on-reading-a-file
            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        stuRoster = studentList;
    }
    public void load(String csvInputFile) {
        //https://stackoverflow.com/questions/28133529/observer-observable-in-a-gui
        //https://coderanch.com/t/617548/java/Observer-Observable
        csvHeadersList = csvHeadersList.subList(0, headers2);
        this.read(csvInputFile);
        setChanged();
        notifyObservers();
    }
    public Student createStudent(String[] attributes) {
        String ID = attributes[0];
        String firstName = attributes[1];
        String lastName = attributes[2];
        String program = attributes[3];
        String level = attributes[4];
        String ASUID = attributes[5];
        Student stu = new Student(ID, firstName, lastName, program, level, ASUID);
        for (int i = headers2; i < attributes.length; i++) {
            stu.addAttendance(LocalDate.parse(csvHeadersList.get(i)), Integer.parseInt(attributes[i]));
        }
        return stu;
    }
    public void save(String saveFilePath) {
        try {
            FileWriter csvWriter = new FileWriter(saveFilePath);
            if (!csvHeadersList.isEmpty()) csvWriter.append(String.join(",", csvHeadersList));
            List<List<String>> tableData = new ArrayList<>();
            String[][] arrTableData = getTableData();
            //https://stackoverflow.com/questions/51118768/how-to-add-delimiter-while-writing-csv-file-using-opencsv/51118838
            for (String[] arrTableDatum : arrTableData) {
                List<String> tableRow = Arrays.asList(arrTableDatum);
                tableData.add(tableRow);
            }
            for (List<String> studentInfo : tableData) {
                csvWriter.append("\n");
                csvWriter.append(String.join(",", studentInfo));
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public String[][] getTableData() {
        //https://www.geeksforgeeks.org/how-to-iterate-linkedhashmap-in-java/
        String[][] rows = new String[stuRoster.size()][];
        for (int i = 0; i < stuRoster.size(); i++) {
            String[] details = new String[csvHeadersList.size()];
            details[0] = stuRoster.get(i).getID();
            details[1] = stuRoster.get(i).getFirstName();
            details[2] = stuRoster.get(i).getSurName();
            details[3] = stuRoster.get(i).getCourse();
            details[4] = stuRoster.get(i).getLevel();
            details[5] = stuRoster.get(i).getASUID();
            int stuno = headers2;
            for (Map.Entry<LocalDate, Integer> e : stuRoster.get(i).getAttendancedat().entrySet()) {
                details[stuno] = Integer.toString(e.getValue());
                stuno++;
            }
            rows[i] = details;
        }
        return rows;
    }
    public void recAttendance(LocalDate date, String filepath) {
        try {
            File file = new File(filepath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String ASURITE;
            int time;
            if (!csvHeadersList.contains(date.toString())) {
                csvHeadersList.add(date.toString());
            }
            while ((line = bufferedReader.readLine()) != null) {
                ASURITE = line.split(delimiter)[0];
                if (line.split(delimiter)[1].equals("")) {
                    time = 0;
                } else {
                    time = Integer.parseInt(line.split(delimiter)[1]);
                }
                newStudents.put(ASURITE, time);
                //https://github.com/arnaudroger/SimpleFlatMapper/issues/511
                for (Student student : stuRoster) {
                    student.addAttendance(date, 0);
                    if (student.getASUID().equals(ASURITE)) {
                        student.addAttendance(date, time);
                        newStudents.remove(ASURITE);
                        addStudent++;
                        boolean result = false;
                        for (LocalDate date1 : dates) {
                            if ((date1).equals(date)) {
                                result = true;
                                break;
                            }
                        }
                        if (!result) {
                            dates.add(date);
                        }
                    }
                }
            }
            bufferedReader.close();
            //https://stackoverflow.com/questions/41367350/observable-notifyobservers-nullpointerexception
            setChanged();
            notifyObservers();
        } catch (IOException | NumberFormatException exception) {
            exception.printStackTrace();
        }
    }
}
