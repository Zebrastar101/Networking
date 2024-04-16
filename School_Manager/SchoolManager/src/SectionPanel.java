import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.*;

public class SectionPanel extends JPanel{

    JLabel panelTitleLabel = new JLabel("Sections");
    JLabel teacherLabel = new JLabel("Teacher: ");
    ArrayList<ArrayList<Object>> fullData;
    ArrayList<String>tableList;
    JLabel courseLabel = new JLabel("Course: ");
    JScrollPane jscrollEnroll;

    JComboBox<String> teachersDropDown = new JComboBox<String>();
    JComboBox<String> coursesDropDown = new JComboBox<String>();
    JComboBox<String> studentsDropDown = new JComboBox<String>();

    JTable sectionTable;

    JScrollPane jScrollPane;
    ArrayList<String> dropList;

    JButton newButton = new JButton("New");
    JButton saveButton = new JButton("Save");
    JButton deleteButton = new JButton("Delete");
    JButton rosterButton = new JButton("Roster");
    JButton newStudent = new JButton("ADD Student");
    Section sec;

    Connection con;
    Statement stm;
    ResultSet teacherResultSet;
    ResultSet courseResultSet;
    JTable enrollment=new JTable();
    JLabel studentLab= new JLabel("Student: ");
    

    public SectionPanel() throws SQLException {


        setLayout(null);
        setBounds(15, 40, 950, 630);
        setBorder(BorderFactory.createLineBorder(Color.black));

        panelTitleLabel.setBounds(15, 5, 100, 35);
        panelTitleLabel.setFont(new Font("Calibri", Font.BOLD, 23));
        add(panelTitleLabel);

        teacherLabel.setBounds(120, 80, 250, 20);
        teacherLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        add(teacherLabel);

        teachersDropDown.setBounds(260, 80, 230, 20);
        add(teachersDropDown);

        courseLabel.setBounds(120, 110, 250, 20);
        courseLabel.setFont(new Font("Calibri", Font.BOLD, 15));
        add(courseLabel);


        studentLab.setBounds(630, 330, 70,50);
        studentLab.setFont(new Font("Calibri", Font.BOLD, 15));
        add(studentLab);

        coursesDropDown.setBounds(260, 110, 230, 20);
        add(coursesDropDown);

        studentsDropDown.setBounds(720, 345, 150, 20);
        add(studentsDropDown);



        //buttons




        newButton.setBounds(120, 140, 70, 20);
        newButton.setFont(new Font("Calibri", Font.BOLD, 10));
        add(newButton);
        newButton.addActionListener(e -> {
            try {
                newSection((String) teachersDropDown.getSelectedItem(), (String) coursesDropDown.getSelectedItem());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        newStudent.setBounds(720, 400, 70, 20);
        newStudent.setFont(new Font("Calibri", Font.BOLD, 10));
        add(newStudent);
        newStudent.addActionListener(e -> {
            try {
                addStudent((String) studentsDropDown.getSelectedItem(),(int) sectionTable.getValueAt(sectionTable.getSelectedRow(), 0));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveButton.setBounds(210, 140, 70, 20);
        saveButton.setFont(new Font("Calibri", Font.BOLD, 10));
        add(saveButton);
        saveButton.addActionListener(e -> {
            try {
                if(!sectionTable.getSelectionModel().isSelectionEmpty()){
                    saveSectionChanges((String) teachersDropDown.getSelectedItem(), (String) coursesDropDown.getSelectedItem(), (Integer) sectionTable.getValueAt(sectionTable.getSelectedRow() , 0));
                }
                else{
                    int errorMessage = JOptionPane.showConfirmDialog(null, "No section was selected", "Error", JOptionPane.OK_CANCEL_OPTION);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        deleteButton.setBounds(300, 140, 70, 20);
        deleteButton.setFont(new Font("Calibri", Font.BOLD, 10));
        add(deleteButton);
        deleteButton.addActionListener(e-> {
            try {
                if(!sectionTable.getSelectionModel().isSelectionEmpty()){
                    delSection((Integer) sectionTable.getValueAt(sectionTable.getSelectedRow() , 0));
                }
                else{
                    int errorMessage = JOptionPane.showConfirmDialog(null, "No section was selected", "Error", JOptionPane.OK_CANCEL_OPTION);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


        sec = new Section(Main.myConn);
        sectionTable=sec.getSectionTable();
        //below from https://www.tabnine.com/code/java/methods/javax.swing.JTable/getSelectedRow
        fullData=new ArrayList<ArrayList<Object>>();
        fullData=makeFullData(fullData);
        sectionTable.addMouseListener(new MouseAdapter() {
            //Idk how to get the selected values to pop up for this one
            public void mouseClicked(MouseEvent e) {
                teachersDropDown.setSelectedItem((String)sectionTable.getValueAt(sectionTable.getSelectedRow() , 1));
                teachersDropDown.setSelectedItem((String)sectionTable.getValueAt(sectionTable.getSelectedRow() , 2));
                int secID=(int) sectionTable.getValueAt(sectionTable.getSelectedRow(), 0);
                ArrayList<String> tb= new ArrayList<>();
                for(int x=0; x<fullData.size();x++) {
                    if (secID == (int) fullData.get(x).get(0)) {
                        for(int z=1; z<fullData.get(x).size(); z++){
                            tb.add((String) fullData.get(x).get(z));
                        }
                        break;
                    }
                }
                enrollment=buildEnrollMentTable(tb);
                reloadStudentsTable(enrollment, tb);
                jscrollEnroll.setViewportView(enrollment);



            }
        });
        jScrollPane = new JScrollPane(sectionTable);
        jScrollPane.setBounds(50,190,500, 400);
        add(jScrollPane);


         jscrollEnroll= new JScrollPane(enrollment);
        jscrollEnroll.setBounds(630,80,250, 200);
        add(jscrollEnroll);


    }

    public void reload()
    {
        con = Main.myConn;
        try{
            stm=con.createStatement();
            teachersDropDown.removeAllItems();
            coursesDropDown.removeAllItems();
            teacherResultSet=stm.executeQuery("Select*from teachers WHERE id >=1");
            while(teacherResultSet!=null && teacherResultSet.next()){
                String teacher = teacherResultSet.getObject(2) + " " + teacherResultSet.getObject(3)+ "("+teacherResultSet.getObject(1)+")";
                teachersDropDown.addItem(teacher);
            }
            courseResultSet=stm.executeQuery("Select*from courses WHERE id >=1");
            while(courseResultSet!=null && courseResultSet.next()){
                String course = String.valueOf(courseResultSet.getObject(2))+" ("+courseResultSet.getObject(1)+") ";
                coursesDropDown.addItem(course);
            }

        }catch(SQLException e){
            e.printStackTrace();

        }

    }


    public void newSection(String teacher, String course) throws SQLException {
        sectionTable=sec.addSection(teacher, course);
        jScrollPane.setViewportView(sectionTable);
    }


    public void saveSectionChanges(String teacher, String course, int id) throws SQLException {
        sectionTable=sec.saveSection(teacher, course, id);
        jScrollPane.setViewportView(sectionTable);
    }

    public void delSection(int id) throws SQLException {
        sectionTable=sec.deleteSection(id);
        jScrollPane.setViewportView(sectionTable);
    }



    public void purge() throws SQLException {
        sec.purgeSection();
    }

    //ALLL THE ENROLLMENT STUFF
    public void reloadStudentsTable( JTable enrollment,ArrayList<String> tb)
    {
        con = Main.myConn;

        try{
            stm=con.createStatement();
            studentsDropDown.removeAllItems();
            ResultSet studentRS=stm.executeQuery("Select*from students WHERE id >=1");
            ArrayList<String> studs=new ArrayList<>();
            while(studentRS!=null && studentRS.next()){
                studs.add(studentRS.getObject(2) + " " + studentRS.getObject(3)+ "("+studentRS.getObject(1)+")");

            }
            //Collections.sort(studs);
            //Collections.sort(tb);

            dropList=new ArrayList<>();
            int same=0;
            for(int x=0; x<studs.size();x++){
                String val= studs.get(x);
                same=0;
                for(int z=0; z<tb.size(); z++){
                    if(val==tb.get(z)){
                        same+=1;
                    }
                }
                if(same==0){
                    dropList.add(val);
                }

            }
            for(int c=0; c<dropList.size(); c++){
                String student = dropList.get(c);
                studentsDropDown.addItem(student);
            }





        }catch(SQLException e){
            e.printStackTrace();

        }
    }
    public void addStudent(String student, int sectionID) throws SQLException {
         ArrayList<String> tb=new ArrayList<>();
        for(int x=0; x<fullData.size();x++){
            if(sectionID==(int) fullData.get(x).get(0)){
                fullData.get(x).add(student);
                for(int z=1; z<fullData.get(x).size(); z++){
                    tb.add((String) fullData.get(x).get(z));
                }
                break;
            }
        }
        enrollment=buildEnrollMentTable(tb);
        reloadStudentsTable(enrollment, tb);
        jscrollEnroll.setViewportView(enrollment);
    }
    public ArrayList<String> getTableData (JTable table) {

        int nRow = table.getRowCount();
        ArrayList<String> tableData=new ArrayList<>();
            for (int j = 0 ; j < nRow ; j++)
                tableData.add(String.valueOf(table.getValueAt(j,0)));
        return tableData;
    }
    public JTable buildEnrollMentTable( ArrayList<String> tb){
        String[][] dataArray= new String[tb.size()][1];
        String[] colNames={"Students"};
            for(int c=0; c<dataArray.length;c++){
                dataArray[c][0] = tb.get(c);
                //dataArray[r][c]=data.get(r).get(c);

            }




        JTable table = new JTable(dataArray,colNames);


        return table;


    }
    public ArrayList<ArrayList<Object>> makeFullData(ArrayList<ArrayList<Object>> fd) throws SQLException {
        con = Main.myConn;
         stm=con.createStatement();
        ResultSet sectionRS=stm.executeQuery("Select*from sections WHERE id >=1");
        ArrayList<Object> perRow = new ArrayList<>();
        while (sectionRS != null && sectionRS.next()) {

            for (int z = 1; z <=1 ; z++) {
                perRow.add(sectionRS.getObject(z));
            }
            fd.add(perRow);


            perRow = new ArrayList<>();
        }
        return fd;
    }


}

