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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener ;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import main.Store;


/**
 * FXML Controller class
 *
 * @author SEGBEFIA_c
 */
public class viewFrmCntrlr implements Initializable {
    
    @FXML
    private Agenda mtnAgenda;
    
    @FXML
    private TextArea viewCom;
    
    @FXML
    private TextArea enterCom;
    
    @FXML
    private Button submitCom;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ListView mtnList;
    
    @FXML
    private TextArea mtnDetails;
    
    @FXML
    private Button crtForm;
            
    @FXML
    private Button mtnOut;
            
    @FXML
    private Button tblForm;
            
    @FXML
    private Button edtForm;
    
    @FXML
    private Button searchBtn;
    
    @FXML
    private Button deleteBtn;
    
    @FXML
    private Label mtnWelcome;
             
    @FXML
    private Button cancelSearch;
    
    @FXML
    private TextField searchFld;
   
    private ResultSet usrList;
    
    List<String> usrMtns = new ArrayList<>();
    ListProperty<String> listProperty = new SimpleListProperty<>();
    // declare and initialize a array lsit and list property
    
    
    @FXML
    private void populateAgenda(){
        
        
        
        AgendaSkinSwitcher agendaSwitch = new AgendaSkinSwitcher(mtnAgenda);
        
//         anchorPane.getChildren().add(agendaSwitch);
        
        mtnAgenda.setAllowDragging(false);
        mtnAgenda.setAllowResize(false);
        mtnAgenda.appointments().clear();
        
        
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
            
    @FXML
    public void swich(ActionEvent event){
        crt();
    
}
    
    public void logOut(){
        try {
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(login.LgnFrmCtrlr.class.getResource("LgnFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void list(){
        
        usrMtns.clear();
        
        try {
            
            while ( usrList.next()){
                
            
                if ( mtnClash(usrList.getInt(1), usrList.getDate(3), usrList.getTime(4), usrList.getTime(5))) {
                    
                     usrMtns.add(usrList.getString(1) + " " + usrList.getString(2) + "(Clash)"  );
                    
                } else {
                     usrMtns.add(usrList.getString(1) + " " + usrList.getString(2)) ;
                }
                  
               
            }
            
             mtnList.itemsProperty().bind(listProperty);
          listProperty.set(FXCollections.observableArrayList(usrMtns));
          
            usrList.beforeFirst();
            populateAgenda();
     
   
        } catch (SQLException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    private void loadComments(){
         String query = "SELECT `Comments` FROM `meetings` WHERE `MeetingID` = " + getMtnID();
         
                if (main.DataConn.isQueryResult(query)) {
                viewCom.setText(main.DataConn.returnComments(query));  
        }


    }
    
    @FXML
    private void submitComment(ActionEvent event){

      String comments =  main.DataConn.returnComments("SELECT "
              + "`Comments` FROM `meetings` WHERE `MeetingID` = " + getMtnID());
      
      String comment = Store.getCreator() + ": "+ enterCom.getText() + "\n";
      comments = comments + comment;
      
      String query = "UPDATE `meetings` SET `Comments`='"+comments+"' "
              + "WHERE `MeetingID` =" + getMtnID();
      
      main.DataConn.query(query);
      
      loadComments();
    }
    
    
    @FXML
    private void search(ActionEvent event ){
        String searchItem = searchFld.getText();
                
        String query ;
        
        if (Store.getPrivelage()) {
            
            query  ="SELECT * FROM meetings WHERE Name LIKE '%"+searchItem+"%'";
            
        } else {
            query  ="SELECT * FROM meetings m JOIN user_meeting um ON "
                + "m.MeetingID = um.MeetingID JOIN users u ON um.UserID = "
                + "u.UserID WHERE UserName = '"+main.Store.getCreator()
                +"' AND Name LIKE '%"+searchItem+"%'";
        }
        
        //bellow checks if the user is searching for a member
         if (searchItem.contains("_")) {
             
            if (Store.getPrivelage()) {
            
                query  ="SELECT * FROM meetings WHERE "
                        + "Name IN (SELECT `Name` FROM meetings m JOIN "
                        + "user_meeting um ON m.MeetingID = um.MeetingID "
                        + "JOIN users u ON um.UserID = u.UserID WHERE "
                        + "Creator LIKE '%"+searchItem+"%')";
            
            } else {
                
                query ="SELECT DISTINCT * FROM meetings m JOIN user_meeting"
                    + " um ON m.MeetingID = um.MeetingID JOIN users u ON "
                    + "um.UserID = u.UserID WHERE UserName "
                    + "= '"+main.Store.getCreator()+"' AND "
                    + "Name IN (SELECT `Name` FROM meetings m JOIN "
                    + "user_meeting um ON m.MeetingID = um.MeetingID "
                    + "JOIN users u ON um.UserID = u.UserID WHERE "
                    + "Creator LIKE '%"+searchItem+"%')";
        }
                
            }
        
        ResultSet rs = main.DataConn.returnQuery(query);
        
        try {
            if (rs.first() == true) {
                
                rs.beforeFirst();
                usrList = rs;
            }
        } catch (SQLException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
 
                
        
        cancelSearch.setDisable(false);
        list();
    }
    
    private void origList(){
        
        if (main.Store.getPrivelage()) {
            
           String query = "SELECT * FROM meetings ORDER BY `Date` DESC";
           usrList = main.DataConn.returnQuery(query);
           
        } else {
            
           String query = "SELECT * FROM meetings m JOIN user_meeting um ON m.MeetingID "
             + "= um.MeetingID JOIN users u ON um.UserID = u.UserID WHERE"
             + " UserName = '"+main.Store.getCreator()+"' ";
           usrList = main.DataConn.returnQuery(query); 
        
        }
    
    list();
    
    cancelSearch.setDisable(true);
    }
    
    @FXML
    private void cancelBtn(ActionEvent event){
        origList();
    }
    
    public void mtnSelected(){
        String details;
        String query;
        int mtnID = getMtnID();
        if (mtnID != 0) {
   
            query = "SELECT * FROM `meetings` WHERE `MeetingID` = " + mtnID;
            details = main.DataConn.returnRow(query);
            mtnDetails.setText(details);
            
                   loadComments();
            }

        
        
    }

    private int getMtnID() {
        int mtnID = 0;
        
        try {
            int i = mtnList.getSelectionModel().getSelectedIndex() +1;
            System.out.println(i);
              
            usrList.beforeFirst();
            while (usrList.next()) {
                
                
                if (usrList.getRow() == i) {
                    mtnID = usrList.getInt(1);
                    System.out.println(mtnID);    
                }
                
            }
                  
            

        } catch (SQLException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
     return mtnID;    
    }
    
    private boolean mtnClash(int mtnID, Date mtnDate , Time start, Time end ){     

    
        Boolean isClash = false;

        
        try {
            String query = "SELECT * FROM meetings m JOIN user_meeting um ON"
                    + " m.MeetingID = um.MeetingID JOIN users u ON "
                    + "um.UserID = u.UserID WHERE Date = '"+mtnDate+"' "
                    + "AND u.UserName = '"+Store.getCreator()+"'";
           
            Time dbStart;
            Time dbEnd;
            int dbID;
            
            ResultSet conMtns = main.DataConn.returnQuery(query);
            //gets all concurrent mettings aka meetings on that day
            
            
            conMtns.beforeFirst();
            
            while(conMtns.next()){
                
                dbStart = conMtns.getTime(4);
                dbEnd = conMtns.getTime(5);
                dbID = conMtns.getInt(1);
                
                if (dbID != mtnID) {   
                if ((dbStart.before(start) && dbEnd.before(start)) || (dbStart.after(end) && dbEnd.after(end))) {
                    
                        
                } else {
                    System.out.println("checking member clash for mtnID "+ conMtns.getInt(1));
                   isClash= true;     
                }
                 
                }
            

            }
   }    catch (SQLException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return isClash;
   }
      
   
      

    
      
    @FXML
    private void deleteBtnPressed( ActionEvent event){
       
        
        String query = "SELECT * FROM `meetings` WHERE "
                + "`MeetingID` = "+getMtnID()+" AND"
                + " `Creator` = '"+main.Store.getCreator()+"'";
        
        if (main.DataConn.isQueryResult(query) || Store.getPrivelage()) {
                      
        int mtnID = getMtnID();
        deleteMtn(mtnID);
            
        } else {
            
              
            mtnDetails.setText("You can not delete this meeting.\n"
                    + "A meeting can only be deleted by \nits creator or admin");
            

            
        }
        
        
    }
    
    private void deleteMtn(int mtnID){
        
        String query1;
        String query2;
        
        query1 = "DELETE FROM `meetings` WHERE `MeetingID` = "+mtnID ; 
        query2 = "DELETE FROM `user_meeting` WHERE `MeetingID` = "+mtnID ; 
        
        
        
        main.DataConn.query(query1);
        main.DataConn.query(query2);
        
        origList();
    }
    
    private void deleteMsg(int mtnID){
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Are you sure you want to delete this meeting?");
        alert.setHeaderText("Delete Confirm");
        alert.showAndWait();
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
          // ... user chose OK
            deleteMsg(mtnID);
        } else {
           // ... user chose CANCEL or closed the dialog
        }
        
   }
    
    public void crt(){
    try {
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(create.CrtFrmController.class.getResource("crtFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void month(){
    try {
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(view.viewFrmCntrlr.class.getResource("viewTblFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(viewFrmCntrlr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void edit(){
    try {
        String query = "SELECT * FROM `meetings` WHERE "
                + "`MeetingID` = "+getMtnID()+" AND"
                + " `Creator` = '"+main.Store.getCreator()+"'";
        
        
        if (main.DataConn.isQueryResult(query) || Store.getPrivelage()) {
            
            main.Store.setMtn(getMtnID());
            Stage stage;
            Parent root;
            
            stage = (Stage) crtForm.getScene().getWindow();
            
            root = FXMLLoader.load(edit.EdtFrmController.class.getResource("edtFrm.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            
        } else {

            mtnDetails.setText("You can not edit this meeting.\n"
                    + "A meeting can only be edited by \nits creator");            
            
        }
        
            
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
        mtnWelcome.setText("Welcome " + main.Store.getCreator()+"!");
           mtnList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            mtnSelected();
    }
});
        
        
    }    
    
}
