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
            bufferedReader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        stuRoster = studentList;
    }
    public void load(String csvInputFile) {
        csvHeadersList = csvHeadersList.subList(0, headers2); // reset to default headers
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[][] getTableData() {
        String[][] tableData = new String[stuRoster.size()][];
        for (int i = 0; i < stuRoster.size(); i++) {
            String[] stuAttributes = new String[csvHeadersList.size()];
            stuAttributes[0] = stuRoster.get(i).getID();
            stuAttributes[1] = stuRoster.get(i).getFirstName();
            stuAttributes[2] = stuRoster.get(i).getSurName();
            stuAttributes[3] = stuRoster.get(i).getCourse();
            stuAttributes[4] = stuRoster.get(i).getLevel();
            stuAttributes[5] = stuRoster.get(i).getASUID();
            int studentIndex = headers2;
            for (Map.Entry<LocalDate, Integer> e : stuRoster.get(i).getAttendancedat().entrySet()) {
                stuAttributes[studentIndex] = Integer.toString(e.getValue());
                studentIndex++;
            }
            tableData[i] = stuAttributes;
        }
        return tableData;
    }
    public void recAttendance(LocalDate date, String filepath) {
        try {
            File file = new File(filepath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String ASURITE;
            int time;
            if (!csvHeadersList.contains(date.toString())) {
                csvHeadersList.add(date.toString());
            }
            while ((line = br.readLine()) != null) { // Read all lines of csv file
                ASURITE = line.split(delimiter)[0];
                if (line.split(delimiter)[1].equals("")) {
                    time = 0;
                } else {
                    time = Integer.parseInt(line.split(delimiter)[1]);
                }
                newStudents.put(ASURITE, time);
                for (Student student : stuRoster) { // Find student by ASURITE
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
            br.close();
            setChanged();
            notifyObservers();
        } catch (IOException | NumberFormatException ioe) {
            ioe.printStackTrace();
        }
    }
}
