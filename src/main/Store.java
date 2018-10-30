/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SEGBEFIA_c
 */
public class Store {
    //Some setter and getter methods and a method to get user information
    
    static public String creator;
    static public int mtnID;
    static public char credentials;
    
    public static void setCreator(String x){
        creator = x ;
    }
    
    public static String getCreator(){
        
        return creator;
    }
    
    public static void setMtn(int x){
        mtnID = x ;
    }
    
    public static int getMtn(){
        
        return mtnID;
    }

    
    public static boolean getPrivelage(){
        boolean privelage = false;
        String query = "SELECT * FROM `users` WHERE `UserName` = '"+creator+"'";
        try {
            ResultSet info = main.DataConn.returnQuery(query);
            info.first();
            
            privelage = info.getBoolean(4);
        } catch (SQLException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return privelage;
    }
}
