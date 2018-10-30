/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.DataConn;

/**
 *
 * @author SEGBEFIA_c
 */
public class LgnFrmCtrlr implements Initializable {
    public String userName;
    
    @FXML
    private Label error;
    
    @FXML
    private TextField user;
    
    @FXML
    private TextField pass;
    
    
    
    @FXML
    private void goBtnPressed(ActionEvent event) {
        DataConn dt = new DataConn();
        userName = user.getText();
        String query = "Select `UserName`, `Password` FROM users"
                    + " WHERE `UserName` = '"+userName+"'";
        
        try { 
            
            ResultSet rs = DataConn.returnQuery(query);
           
            if (DataConn.isQueryResult(query)) {
                 rs.next();
                System.out.println(rs.getString(2));
                 if (rs.getString(2).equals(pass.getText())) {
                
                error.setText("Welcome");
                error.setTextAlignment(TextAlignment.CENTER);
                
                main.Store.setCreator(userName);
                switchView();
                
            } else {
                error.setText("Your password is incorrect");
                error.setTextAlignment(TextAlignment.CENTER);
            }
            } else {
                error.setText("Your username is not in database");
            }
           
        } catch (SQLException ex) {
            System.out.println(ex);
            error.setText("There is a problem with the connection to the database");
        }
    }
    
    private void switchView(){
        try {
            Stage stage;
            Parent root;
            
            stage = (Stage) error.getScene().getWindow();
            
            root = FXMLLoader.load(view.viewFrmCntrlr.class.getResource("viewFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LgnFrmCtrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
