/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.internal.scene.control.skin.agenda.AgendaMonthSkin;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;


/**
 * FXML Controller class
 *
 * @author SEGBEFIA_c
 */
public class ViewTblFrmCntrlr implements Initializable {
    
//    @FXML
//    private TableView mtntbl;

    @FXML
    private Agenda mtnAgenda;
    
        @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TextArea mtnDetails;
    
    @FXML
    private Button crtForm;
            
    @FXML
    private Button tblForm;
            
    @FXML
    private Button edtForm;
    
    @FXML
    private Button nxtMonth;
            
    @FXML
    private Button prvMonth;
    
    @FXML
    private Button searchBtn;
    
    @FXML
    private Button deleteBtn;
             
    @FXML
    private Button cancelSearch;
    
    @FXML
    private TextField searchFld;
   
    private ResultSet usrList;
    

    
    @FXML
    private void populateAgenda(){
        AgendaSkinSwitcher agendaSwitch = new AgendaSkinSwitcher(mtnAgenda);
        
//        anchorPane.getChildren().add(agendaSwitch);
        
        mtnAgenda.setAllowDragging(false);
        mtnAgenda.setAllowResize(false);
        
        LocalDate lTodayLocalDate = LocalDate.now();
        Calendar start;
        Calendar end;
        String summary;
        String description;
        String style = "group7";
        
        try {
            while( usrList.next()){
                
                start = toCalendar(usrList.getDate(3),usrList.getTime(4));
                end = toCalendar(usrList.getDate(3),usrList.getTime(5));
                
                summary = usrList.getString(2);
                description = usrList.getString(7);
                
                
                
                mtnAgenda.appointments().add(
                        new Agenda.AppointmentImpl()
                                .withStartTime(start)
                                .withEndTime(end)
                                .withSummary(summary)
                                .withDescription(description)
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"))
                        
                );
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewTblFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
      
       
    }
    
    
    
    private Calendar toCalendar(Date date, Time time){ 

  // Construct date and time objects
  Calendar dateCal = Calendar.getInstance();
  dateCal.setTime(date);
  Calendar timeCal = Calendar.getInstance();
  timeCal.setTime(time);

  // Extract the time of the "time" object to the "date"
  dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
  dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
  dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

  
    return dateCal;
  
    }
    
    private void origList(){
        
        if (main.Store.getPrivelage()) {
            
           String query = "SELECT * FROM meetings";
           usrList = main.DataConn.returnQuery(query);
           
        } else {
            
           String query = "SELECT * FROM meetings m JOIN user_meeting um ON m.MeetingID "
             + "= um.MeetingID JOIN users u ON um.UserID = u.UserID WHERE"
             + " UserName = '"+main.Store.getCreator()+"' ";
           usrList = main.DataConn.returnQuery(query); 
        
        }
    
    populateAgenda();
    
    cancelSearch.setDisable(true);
    }
    
    @FXML
    private void search(ActionEvent event ){
   
    }
    
    @FXML
    private void previous(ActionEvent event ){
 
    }
    
    @FXML
    private void next(ActionEvent event ){

    }
    
     @FXML
    private void deleteBtnPressed(ActionEvent event ){

    }
    
     @FXML
    private void cancelBtn(ActionEvent event){
       
    }
    
    public void crt(){
    try {
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(create.CrtFrmController.class.getResource("crtFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void list(){
    try {
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(view.viewFrmCntrlr.class.getResource("viewFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void edit(){
    try {
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(edit.EdtFrmController.class.getResource("edtFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        origList();
         
//        mtnAgenda.actionCallback().set( (appointment) -> {
//          System.out.println("Action was triggered on " + appointment.getDescription());
//      });
    }    
    
}
