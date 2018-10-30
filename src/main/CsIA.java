/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author SEGBEFIA_c
 */
public class CsIA extends Application {
    //runs the login form and is main class
    
    public static void test() {
        // tests both methods in the DataConn Class
        try {
            DataConn.query("INSERT INTO `students`(`User Name`, `Password`) VALUES ('YEs','YEs')");
            
            ResultSet rs = DataConn.returnQuery("Select `User Name`, `Password` FROM "
                    + "students WHERE `User Name` = 'Carlton'");
            
            if (rs.getString(2).equals("YEs")) {
                System.out.println("Welcome");
            } else {
                System.out.println("Your password is incorrect");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
                  
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        //loads the form 
        Parent root = FXMLLoader.load(login.LgnFrmCtrlr.class.getResource("LgnFrm.fxml"));
       
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
        //test();
    }
    
}
