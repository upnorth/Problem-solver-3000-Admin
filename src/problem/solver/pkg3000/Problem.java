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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.sql.Timestamp;

/**
 *
 * @author Karl
 */
public class Problem {
    private int probManager;
    private int probClient;
    private int probCat;
    private int probPriority;
    private String probComments;
    
    //Problem related information
    public int getprobManager() {
        return probManager;
    }

    public void setprobManager(int manager) {
        this.probManager = manager;
    }

    public int getprobClient() {
        return probClient;
    }
    
    public void setprobClient(int probClient) {
        this.probClient = probClient;
    }
    
    public int getprobCat() {
        return probClient;
    }
        
    public void setprobCat(int probCat) {
        this.probCat = probCat;
    }

    public int getprobPriority() {
        return probPriority;
    }

    public void setprobPriority(int probPriority) {
        this.probPriority = probPriority;
    }
    
    
    public String getprobCom() {
        return probComments;
    }

    public void setprobCom(String probCom) {
        this.probComments = probCom;
    }
    
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
            stmt.setInt(1,       getprobClient());
            stmt.setInt(2,       getprobManager());
            stmt.setTimestamp(3, new Timestamp(d.getTime()));
            stmt.setInt(4,       getprobCat());
            stmt.setInt(5,       getprobPriority());
            stmt.setString(6,    getprobCom());
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
                throw new SQLException("Uppkoppling mot databas saknas!");
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
                throw new SQLException("Problem med db:" + ex.getMessage());
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
    public ArrayList<Problem> getProblems() throws SQLException{
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
                throw new SQLException("Uppkoppling mot databas saknas!");
            }
            PreparedStatement stmt = cn.prepareStatement("SELECT CLIENT,MANAGER,COMMENTS FROM PROBLEM ");
            //Kör SQL-uttryck och ladda upp posterna i ResultSet variabel rs 
            ResultSet rs = stmt.executeQuery();
            //Ladda lista med data från ResultSet (rs)
            while (rs.next()){
                //För varje post i db skapas ett student-objekt
                Problem p = new Problem();
                p.setprobManager(rs.getInt("MANAGER"));
                p.setprobClient(rs.getInt("CLIENT"));
                p.setprobPriority(rs.getInt("PRIORITY"));
                //Lägg till student-objekt i arraylist
                problemList.add(p);
            }
            return problemList;
        }catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException("Problem med db:" + ex.getMessage());
        }finally{
            if (cn!=null) 
                cn.close();
        }
        
    }
}

