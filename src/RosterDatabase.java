import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class RosterDatabase extends Observable {

    public static List<Student> roster;
    public static List<String> headers;
    public static int studentsAdded = 0;
    public static LinkedHashMap<String, Integer> newStudents;
    public static List<LocalDate> dates;

    public static final String delimiter = ",";
    public static final int baseHeaders = 6;

    public RosterDatabase() {
        headers = new ArrayList();
        headers.add("ID");
        headers.add("First Name");
        headers.add("Last Name");
        headers.add("Program");
        headers.add("Level");
        headers.add("ASURITE");
        newStudents = new LinkedHashMap();
        dates = new ArrayList<>();
    }


    public void read(String csvFile) {
        List<Student> studentList = new ArrayList();

        try {

            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            String[] studentAttributes;

            line = br.readLine();
            studentAttributes = line.split(delimiter);

            if (studentAttributes[0].equals("ID")) {
                headers.addAll(Arrays.asList(studentAttributes).subList(headers.size(), studentAttributes.length));
            } else {
                studentList.add(createStudent(studentAttributes));
            }

            while ((line = br.readLine()) != null) {
                studentAttributes = line.split(delimiter);
                studentList.add(createStudent(studentAttributes));
            }

            br.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        roster = studentList;
    }


    public void load(String csvInputFile) {

        headers = headers.subList(0, baseHeaders); // reset to default headers

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
        String ASURITE = attributes[5];

        Student stu = new Student(ID, firstName, lastName, program, level, ASURITE);
        for (int i = baseHeaders; i < attributes.length; i++) {
            stu.addAttendance(LocalDate.parse(headers.get(i)), Integer.parseInt(attributes[i]));
        }

        return stu;
    }

    public boolean save(String saveFilePath) {

        try {
            FileWriter csvWriter = new FileWriter(saveFilePath);

            if (!headers.isEmpty()) csvWriter.append(String.join(",", headers));

            List<List<String>> tableData = new ArrayList();

            String[][] arrTableData = getTableData();

            for (int i = 0; i < arrTableData.length; i++) {
                List<String> tableRow = Arrays.asList(arrTableData[i]);
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
            return false;
        }

        return true;
    }


    public String[][] getTableData() {

        String[][] tableData = new String[roster.size()][];

        for (int i = 0; i < roster.size(); i++) {
            String[] stuAttributes = new String[headers.size()];
            stuAttributes[0] = roster.get(i).getID();
            stuAttributes[1] = roster.get(i).getFirstName();
            stuAttributes[2] = roster.get(i).getSurName();
            stuAttributes[3] = roster.get(i).getCourse();
            stuAttributes[4] = roster.get(i).getLevel();
            stuAttributes[5] = roster.get(i).getASUID();

            int studentIndex = baseHeaders;
            for (Map.Entry<LocalDate, Integer> e : roster.get(i).getAttendancedat().entrySet()) {
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

            String line = "";
            String ASURITE = "";
            int time = 0;

            if (!headers.contains(date.toString())) {
                headers.add(date.toString());
            }

            while ((line = br.readLine()) != null) { // Read all lines of csv file
                ASURITE = line.split(delimiter)[0];

                if (line.split(delimiter)[1].equals("")) {
                    time = 0;
                } else {
                    time = Integer.parseInt(line.split(delimiter)[1]);
                }

                newStudents.put(ASURITE, time);

                for (Student student : roster) { // Find student by ASURITE
                    student.addAttendance(date, 0);

                    if (student.getASUID().equals(ASURITE)) {
                        student.addAttendance(date, time);
                        newStudents.remove(ASURITE);
                        studentsAdded++;

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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

}
