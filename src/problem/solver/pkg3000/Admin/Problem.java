/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.pkg3000.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.sql.Timestamp;

/**
 *
 * @author Karl
 */
public class Problem {
    private int id;
    private int man;
    private int cli;
    private int cat;
    private int pri;
    private String status;
    private String com;
    
    ArrayList<Task> tasks = new ArrayList<>();
    //Problem related information
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getMan() { return man; }
    public void setMan(int man) { this.man = man; }

    public int getCli() { return cli; }
    public void setCli(int cli) { this.cli = cli; }
    
    public int getCat() { return cat; }
    public void setCat(int cat) { this.cat = cat; }

    public int getPri() { return pri; }
    public void setPri(int pri) { this.pri = pri; }
    
    public String getCom() { return com; }
    public void setCom(String com) { this.com = com; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    /*@Override
    public String toString(){
        return this.fname + " " + this.lname + " " + this.ideal;
        
    }*/
    public String addProblem() throws SQLException {
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
                throw new SQLException("Uppkoppling mot databas saknas!");
            }
            //specificerar vilket SQL-uttryck som ska köras mot db
            PreparedStatement stmt = cn.prepareStatement("INSERT INTO PROBLEM "
                    + "(CLIENT,MANAGER,REGISTERED,CATEGORY,PRIORITY,COMMENTS)"
                            + "VALUES (?,?,?,?,?,?)");
            //Create timestamp for problem
            Date d = new Date();
            stmt.setInt(1,       getCli());
            stmt.setInt(2,       getMan());
            stmt.setTimestamp(3, new Timestamp(d.getTime()));
            stmt.setInt(4,       getCat());
            stmt.setInt(5,       getPri());
            stmt.setString(6,    getCom());
            //Kör SQL-uttryck
            int i = stmt.executeUpdate();
            //Kontrollera resultat av SQL-uttryck
            if (i > 0) 
                sRet = "success";
            return sRet; //returnera status från SQL-exekvering
        }
        catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Problem med db:" + ex.getMessage());
        }finally{
            if (cn!=null) 
                cn.close();
        }
    }
    public int newProblem() {
        int problemID = 0;
        //Läs in information om db-uppkoppling
        Connection cn = null;
        try{
            //Laddar db-library (derby) till applikationens minne
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //specificerar uppgifter som behövs för att koppla upp sig mot databasen
            cn = DriverManager.getConnection("jdbc:derby://localhost:1527/ps3;create=true;user=ps3;password=ps3"); 
            //Kontrollera uppkoppling mot db
            if (cn == null){
                throw new SQLException("Couldn´t connect to database!");
            }
            PreparedStatement stmt = cn.prepareStatement("SELECT ID FROM PROBLEM ORDER BY id DESC FETCH FIRST ROW ONLY");
            //Kör SQL-uttryck och ladda upp posterna i ResultSet variabel rs 
            ResultSet rs = stmt.executeQuery();
            //Ladda lista med data från ResultSet (rs)
            while (rs.next()){
                //För varje post i db skapas ett student-objekt
                problemID = rs.getInt("ID");
            }
        }catch (ClassNotFoundException | SQLException ex) {
            try {
                throw new SQLException("Problem with db:" + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if (cn!=null) 
                try {
                    cn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return problemID;
    }
    public ArrayList<Problem> getProblems(int id) throws SQLException{
        //Läs in information om db-uppkoppling
        Connection cn = null;
        //håller en lista med ett student-objekt för varje post i db
        ArrayList problemList = new ArrayList<>();
        try{
            //Laddar db-library (derby) till applikationens minne
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //specificerar uppgifter som behövs för att koppla upp sig mot databasen
            cn = DriverManager.getConnection("jdbc:derby://localhost:1527/ps3;create=true;user=ps3;password=ps3"); 
            //Kontrollera uppkoppling mot db
            if (cn == null){
                throw new SQLException("Couldn´t connect to database!");
            }
            PreparedStatement stmt = cn.prepareStatement("SELECT * FROM PROBLEM WHERE MANAGER=?");
            stmt.setInt(1, id);            
            //Kör SQL-uttryck och ladda upp posterna i ResultSet variabel rs 
            ResultSet rs = stmt.executeQuery();
            //Ladda lista med data från ResultSet (rs)
            while (rs.next()){
                //För varje post i db skapas ett student-objekt
                Problem p = new Problem();
                p.setMan(id);
                p.setId(rs.getInt("ID"));
                p.setCli(rs.getInt("CLIENT"));
                p.setPri(rs.getInt("PRIORITY"));
                p.setCat(rs.getInt("CATEGORY"));
                p.setCom(rs.getString("COMMENTS"));
                p.setStatus(rs.getString("STATUS"));
                Task t = new Task();
                p.tasks = t.getTasks(rs.getInt("ID"));
                //Lägg till student-objekt i arraylist
                problemList.add(p);
            }
            return problemList;
        }catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Problem with db:" + ex.getMessage());
        }finally{
            if (cn!=null) 
                cn.close();
        }
        
    }
    public String updateProblem(Problem save) throws SQLException {
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
                throw new SQLException("Couldn´t connect to database!");
            }
            else {
                    //specificerar vilket SQL-uttryck som ska köras mot db
                PreparedStatement stmt = cn.prepareStatement("UPDATE PROBLEM "
                        + "SET MANAGER=?,PRIORITY=?,COMMENTS=?,STATUS=? WHERE ID=?");
                stmt.setInt(1,       save.getMan());
                stmt.setInt(2,       save.getPri());
                stmt.setString(3,    save.getCom());
                stmt.setString(4,    save.getStatus());
                stmt.setInt(5,       save.getId());
                //Kör SQL-uttryck
                int i = stmt.executeUpdate();
                //Kontrollera resultat av SQL-uttryck
                if (i > 0) {
                    sRet = "success";
                    for(Task t: save.tasks){
                        t.updateTask(t);
                    }
                }    
                return sRet; //returnera status från SQL-exekvering
            }
        }
        catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Problem with db:" + ex.getMessage());
        }finally{
            if (cn!=null) 
                cn.close();
        }
    }
}
