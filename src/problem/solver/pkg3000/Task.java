/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.pkg3000;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Karl
 */
public class Task {
    
    private int taskParent;
    private int taskCat;
    private int taskEst;
    private int taskPrio;
    private int taskTech;
    private String taskComments;
    
    //Task related information
    public int gettaskParent() {
        return taskParent;
    }

    public void settaskParent(int taskParent) {
        this.taskParent = taskParent;
    }    
    
    public int gettaskCat() {
        return taskCat;
    }

    public void settaskCat(int taskCat) {
        this.taskCat = taskCat;
    }
    
    public int gettaskEst() {
        return taskEst;
    }
    
    public void settaskEst(int taskEst) {
        this.taskEst = taskEst;
    }

    public int gettaskPrio() {
        return taskPrio;
    }

    public void settaskPrio(int taskPrio) {
        this.taskPrio = taskPrio;
    }

    public int gettaskTech() {
        return taskTech;
    }

    public void settaskTech(int taskTech) {
        this.taskTech = taskTech;
    }
    
    public String gettaskCom() {
        return taskComments;
    }

    public void settaskCom(String taskCom) {
        this.taskComments = taskCom;
    }
    
    /*@Override
    public String toString(){
        return this.fname + " " + this.lname + " " + this.ideal;
        
    }*/
    public String addTasks(ArrayList<Task> tasks) throws SQLException {
        //konrollera status på SQL-exekveringen
        String sRet = "failure";
        Connection cn = null;
        try{
            //Laddar db-library (derby) till applikationens minne
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //specificerar uppgifter som behövs för att koppla upp sig mot databasen
            cn = DriverManager.getConnection("jdbc:derby://localhost:1527/ps3;create=true;user=ps3;password=ps3");            
            //Kontrollera uppkoppling mot db
            if (cn == null){
                throw new SQLException("No database connection!");
            }
            for(Task t: tasks){
                //specificerar vilket SQL-uttryck som ska köras mot db
                PreparedStatement stmt = cn.prepareStatement("INSERT INTO TASK "
                        + "(PROBLEM_ID,CATEGORY,TECH_ID,ESTTIME,PRIORITY,COMMENTS)"
                                + "VALUES (?,?,?,?,?,?)");
                stmt.setInt(1, t.gettaskParent());
                stmt.setInt(2, t.gettaskCat());
                stmt.setInt(3, t.gettaskTech());
                stmt.setInt(4, t.gettaskEst());
                stmt.setInt(5, t.gettaskPrio());
                stmt.setString(6, t.gettaskCom());
                //Kör SQL-uttryck
                int i = stmt.executeUpdate();
                //Kontrollera resultat av SQL-uttryck
                if (i > 0) 
                    sRet += "The task was successfully added to the problem!\n";
            }
        }
        catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Problem with db:" + ex.getMessage());
        }finally{
            if (cn!=null) 
                cn.close();
        }
        return sRet; //returnera status från SQL-exekvering
    }
    public ArrayList<Task> getTasks() throws SQLException{
        //Läs in information om db-uppkoppling
        Connection cn = null;
        //håller en lista med ett student-objekt för varje post i db
        ArrayList studentList = new ArrayList<>();
        try{
            //Laddar db-library (derby) till applikationens minne
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //specificerar uppgifter som behövs för att koppla upp sig mot databasen
            cn = DriverManager.getConnection("jdbc:derby://localhost:1527/ps3;create=true;user=ps3;password=ps3"); 
            //Kontrollera uppkoppling mot db
            if (cn == null){
                throw new SQLException("Uppkoppling mot databas saknas!");
            }
            PreparedStatement stmt = cn.prepareStatement("SELECT CLIENT,MANAGER,COMMENTS FROM PROBLEM ");
            //Kör SQL-uttryck och ladda upp posterna i ResultSet variabel rs 
            ResultSet rs = stmt.executeQuery();
            //Ladda lista med data från ResultSet (rs)
            while (rs.next()){
                //För varje post i db skapas ett student-objekt
                Task t = new Task();
                t.settaskCat(rs.getInt("CATEGORY"));
                t.settaskEst(rs.getInt("ESTTIME"));
                t.settaskPrio(rs.getInt("PRIORITY"));
                t.settaskTech(rs.getInt("TECH"));
                //Lägg till student-objekt i arraylist
                studentList.add(t);
            }
            return studentList;
        }catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Problem med db:" + ex.getMessage());
        }finally{
            if (cn!=null) 
                cn.close();
        }
        
    }
}

