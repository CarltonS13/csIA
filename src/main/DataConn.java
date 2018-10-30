/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

/**
 *
 * @author SEGBEFIA_c
 */
public class DataConn {
    
    private static final String database ="jdbc:mysql://localhost:3306/test2";
    //the link to the databse itself
    
    private static final String user ="root" ;
    //user name used to connect to database. Default is root
    
    private static final String password = "";
    //password uses to connect to database
    
    //to change the databse,user or password just change the above strings
    
    public static void query(String query){
        //connects to the database and runs the string provided as a query
        
        try (Connection myData = DriverManager.getConnection(database,user,password)) {
             //creating connection to database
        Statement stmt = myData.createStatement();
        
        stmt.execute(query);
        // creating a statement and using it to execute the query given when method called
       
        } catch (Exception e) {
            System.out.println(e);
            //if error output information
        }
       
    } 
    
    public static ResultSet returnQuery(String query) {
        try {
            //runs query but returns the result set
            
            Connection myData = DriverManager.getConnection(database,user,password);
            Statement stmt = myData.createStatement();
            
            ResultSet  rs = stmt.executeQuery(query);
            
            
            
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(DataConn.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return null;
    } 
    
    public static Boolean isQueryResult(String query){
        //connects to the database and runs the string provided as a query and returns true if full
        // and false if empty
        Boolean result = true;
        
        try {
            ResultSet rs = returnQuery(query);
            //uses the return query method
            if (rs.first() == false) {
                result = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
      
    }
    
    public static int returnID(String query){
        int  mtnID = 0;
        try {
            ResultSet ResMtnID = main.DataConn.returnQuery(query);
            ResMtnID.first();
            mtnID = ResMtnID.getInt(1);
           
        } catch (SQLException ex) {
            Logger.getLogger(DataConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return mtnID;
    }
    
    
    public static String returnRow(String query){
        String row = "";
        try {
            ResultSet ResRow = main.DataConn.returnQuery(query);
            ResRow.first();
            
            row = row +  "ID: " + ResRow.getString(1) + "\n" ; 
            row = row + "Name: " + ResRow.getString(2) + "\n" ; 
            row = row + "Date is: " + ResRow.getString(3) + "\n" ; 
            row = row + "Starts at: " + ResRow.getString(4) + "\n" ; 
            row = row + "Ends at: " + ResRow.getString(5) + "\n" ; 
            row = row + "The Length is: " + ResRow.getString(6) + " hours\n" ;
            row = row + "Description: " + ResRow.getString(7) + "\n" ; 
            row = row + "Created by: " + ResRow.getString(8) + "\n" ;        
         
//            for (int i = 1; i < 9; i++) {
//                row = row + ResRow.getString(i) + "\n";
//            }
//            
        } catch (SQLException ex) {
            Logger.getLogger(DataConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return row;
    }    

    public static String returnComments(String query){
        String Comment = "";
        
        try {
            ResultSet ResRow = main.DataConn.returnQuery(query);
            ResRow.first();
             Comment = ResRow.getString(1);
 
        } catch (SQLException ex) {
            Logger.getLogger(DataConn.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            if (Comment == null) {
            Comment= "";
        }
            return Comment;
    }
}

