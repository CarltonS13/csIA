/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edit;




import create.CrtFrmController;
import java.io.IOException;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.Store;
import org.controlsfx.control.textfield.TextFields;
import view.viewFrmCntrlr;

/**
 * FXML Controller class
 *
 * @author SEGBEFIA_c
 */
public class EdtFrmController implements Initializable {
    //declaration of all form elements
    
    @FXML
    private TextField mtnTitle;
    
    @FXML
    private DatePicker mtnDate;
    
    @FXML
    private TextField mtnStart;
    
    @FXML
    private TextField mtnEnd;
    
    @FXML
    private TextArea mtnMembers;

    @FXML
    private TextField enterMembers;
    
    @FXML
    private Button addMembers;
    
    @FXML
    private TextArea mtnDescription;
    
    @FXML
    private CheckBox mtnClash;
    
    @FXML
    private Button mtnSave;
    
    @FXML
    private ProgressBar mtnProgress;
    
    @FXML
    private TextArea mtnError;
    
    @FXML
    private Label timePrompt;
        
    @FXML
    private Label memberPrompt;
    
    @FXML
    private Button viewFrm;

    
    int mtnID = main.Store.getMtn();
    String creator = main.Store.getCreator();
    String members[] = new String[400];
    int m = 0;
    
   
   private void loadDetails(){
      
        try {
            String query="SELECT * FROM `meetings` WHERE `MeetingID` = " + mtnID;
            ResultSet meeting = main.DataConn.returnQuery(query);
            //get all details of meeting
            
            meeting.first();
            mtnTitle.setText(meeting.getString(2));
            System.out.println("Mtn name is "+ meeting.getString(2));
            mtnStart.setText(meeting.getString(4));
            //To-do add meeting date
            mtnEnd.setText(meeting.getString(5));
            mtnDescription.setText(meeting.getString(7));
            //set specific fields to certain text fields and areas
            
            java.sql.Date sqlDate = meeting.getDate(3);
            mtnDate.setValue(sqlDate.toLocalDate());
            
            
            loadMembers();
            
            showArray();
            //show in text area
        } catch (SQLException ex) {
            Logger.getLogger(EdtFrmController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   private void loadMembers(){
        try {
            int i = 0 ;
            String query = "SELECT * FROM users u JOIN user_meeting um ON "
                    + "u.UserID = um.UserID JOIN meetings m ON m.MeetingID ="
                    + " um.MeetingID WHERE m.MeetingID =" + mtnID;
            
            ResultSet users = main.DataConn.returnQuery(query);
            //get all users with that meeting
            
            
            while (users.next()) {
                members[i] = users.getString(2);
                //store in an array
                
                i=i+1;
                m= m+1;
            }
            
            

        } catch (SQLException ex) {
            Logger.getLogger(EdtFrmController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
   
   private void showArray(){

//       members[0] = main.Store.getCreator();
       mtnMembers.setText("");
       for (int i = 0; i < members.length ; i++) {
           
           if (members[i] != null) {
                mtnMembers.setText(mtnMembers.getText() + members[i]+";");
           }
          
       }
   }
    
   private Time getUserLength() throws ParseException{

    String start = mtnStart.getText();
    String end = mtnEnd.getText();
    
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//    creates format for text
    long sms = sdf.parse(start).getTime();
    long ems = sdf.parse(end).getTime();
//    converts time to milliseconds
    long hour = 60*60*1000;
    Time length = new Time(ems - sms - hour);
//    converts milli seconds back to time
//    removes an hour to make up for an addition by method of conversion
     return length;     
 
   } 
    
   private Time getUserTime( String userTime ) throws ParseException{
   
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//    creates format for text
    long ms = sdf.parse(userTime).getTime();
//    converts time to milliseconds
    Time time = new Time(ms);
//    converts milli seconds back to time
     return time;  
   }
        
   private Date getUserDate(){
       
       LocalDate localDate = mtnDate.getValue();
       Date date = java.sql.Date.valueOf( localDate );
       
       return date;
   }
           
   @FXML
   private void mebmberAdd(ActionEvent event){
       
       
       String member = enterMembers.getText();
       String memberCheck = "SELECT * FROM `users` WHERE"
               + " `UserName`='"+member+"'";
        
        Boolean memberResult = main.DataConn.isQueryResult(memberCheck);
    
            if (memberResult == false ) {
           mtnError.setText("This member is not in database."
                   + " Please enter it in the form lastname_m");
       } else 
          {
              
           if ( inArray(member)) {
               
            mtnError.setText("You've already entered this member");   
            
           } else {
          
      
           members[m] = member;
           m=m+1;
           //add member to array before updated form
               showArray();
           
            }
       }
       } 
  
   private boolean inArray(String st){
       
       for (int i = 0; i < members.length; i++) {
           if (members[i] != null) {
               if (members[i].equals(st)) {
               return true;
           }
           }
           
           
       }
       
       return false;
   }
   
   @FXML
   private void saveBtn(ActionEvent event){
       
       String finalResult = "";
       
               finalResult = syntaxCheck();
       System.out.println(finalResult);
       
       if (finalResult.isEmpty()) {
           System.out.println("checking mtns");
           
//           finalResult = finalResult + mtnCheck();
           
//           System.out.println("checked mtns");
       }
       
       
       if (finalResult.isEmpty()) {
           
        if (mtnClash.isSelected() ) {
            addToDatabase();
       } else {
          mtnClash();
       }
       
       } else {
          
          mtnError.setText(finalResult);
       }
   
   }
   
   private String syntaxCheck(){
    String start = mtnStart.getText();
    String end = mtnEnd.getText();
    String desc = mtnDescription.getText();
    String title = mtnTitle.getText();
    String result = "";
    String Members = mtnMembers.getText();
    String length;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Date startTime = null;
    Date endTime = null;
       
       //no start time entered
       if (start.equals("")) {
         result = result + "Please Enter a Start Time \n";   
       }
       
       //no end time entered 
        if (end.equals("")) {
         result = result +"Please Enter an End Time \n";   
       }

        //no description entered
        if (desc.equals("")) {
         result = result +"Please Enter a Description \n";   
       }
            
        //no title entered
        if (title.equals("")) {
         result = result + "Please Enter a Title \n";   
       }
        
          //no date entered
        if (mtnDate.getValue() == null) {
         result = result +"Please Enter a Date \n";   
       } else {
        
        if (getUserDate().getDay()!=new Date().getDay() && getUserDate().getMonth()!=new Date().getMonth()) {
            if (getUserDate().before(new Date())){
            result = result + "The date has passed \n";
        }
        
            }
       
        }
            
        //no members entered
        if (Members.equals("")) {
         result = result + "Please Enter Members \n";   
       }
       

        Date date = null;
        try {
            date = sdf.parse(start);
            if (!start.equals(sdf.format(date))) {
                startTime = sdf.parse(start);
                 }
                } catch (ParseException ex) {
                       result = result + "Start time is in wrong format,"
                               + " please make HH:mm\n";
            }
        

        try {
            date = sdf.parse(end);
            if (!end.equals(sdf.format(date))) {
                endTime = sdf.parse(end);
                 }
                } catch (ParseException ex) {
                       result = result + "End time is in wrong format,"
                               + " please make HH:mm \n";
            }
            
        try {            
                if (endTime.before(startTime)) {
                result = result + "The meeting ends before it starts \n";
            }   
       } catch (Exception e) {
       }
           

      
        
        
       return result;
   }
   
   
//   private String mtnCheck(){
//       String result = "";
//       
//       String query = "SELECT * FROM `meetings` WHERE `Name` = '"+mtnTitle.getText()+"' AND `Date` = '"+getUserDate()+"' AND `Creator` ='"+creator+"'";
//       
//       if (main.DataConn.isQueryResult(query)) {
//          result = "You have already made a meeting like this"; 
//       }
//       
//     return result;  
//   }
   
   private void addToDatabase(){
       System.out.println("adding to database");
       
    String start = mtnStart.getText();
    String end = mtnEnd.getText();
    String desc = mtnDescription.getText();
    String title = mtnTitle.getText();
    String mtnQuery ;
    String DelQuery;
    Time length = null;
    
  
    
        try {
            length = getUserLength();
        } catch (ParseException ex) {
            Logger.getLogger(CrtFrmController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mtnQuery ="UPDATE `meetings` SET `Name`='"+title+"',"
                + "`Date`='"+getUserDate()+"',`Start Time`='"+start+"',"
                + "`End Time`='"+end+"',`Length`='"+length+"',"
                + "`Description`='"+desc+"', `Creator`='"+creator+"' WHERE "
                + "`MeetingID`=" + mtnID;
               
        
       main.DataConn.query(mtnQuery);
       System.out.println("adding mtn");
       
       DelQuery = "DELETE FROM `user_meeting` WHERE `MeetingID` = " + mtnID;
       main.DataConn.query(DelQuery);
     
        saveMembers(mtnID);
        System.out.println("saving members");
       
       saveMsg();
       view();
       
   }
   
   private void saveMsg(){
     Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Saved!");
        alert.setHeaderText("Your meeting has been saved!");
        alert.showAndWait();
   }
    
   private void saveMembers(int mtnID){
       String saveQuery;
       String userIDQuery;
       int userID;
       for (int i = 0; i < members.length ; i++) {
           if (members[i] != null) {
          userIDQuery ="SELECT * FROM `users` WHERE `UserName` = '"+members[i]+"'";
          userID = main.DataConn.returnID(userIDQuery);
          
          saveQuery = "INSERT INTO `user_meeting`(`UserID`, `MeetingID`)"
                  + " VALUES ("+userID+","+mtnID+")";
          
           
               main.DataConn.query(saveQuery);
           }
       }
       
       
   }   
   
   private void mtnClash(){     
    
        Boolean isClash = false;
        try {
            String query = "SELECT `MeetingID`, `Start Time`, `End Time` "
                    + "FROM `meetings` WHERE `Date` = '"+getUserDate()+"'";
            
            
            Time start = getUserTime(mtnStart.getText());
            Time end = getUserTime(mtnEnd.getText());
            
            Time dbStart;
            Time dbEnd;
            
            ResultSet conMtns = main.DataConn.returnQuery(query);
            //gets all concurrent mettings aka meetings at that time
            System.out.println(conMtns);
            
            conMtns.beforeFirst();
            
            while(conMtns.next()){
                
                dbStart = conMtns.getTime(2);
                dbEnd = conMtns.getTime(3);
                
                System.out.println(dbStart +","+dbEnd);
                
                if (conMtns.getInt(1) != mtnID) {
                    if ((dbStart.before(start) && dbEnd.before(start)) || (dbStart.after(end) && dbEnd.after(end))) {
                    
                        
                } else {
                    System.out.println("checking member clash for mtnID "+ conMtns.getInt(1));
                    isClash = memClash(conMtns.getInt(1));
                }
                }
                
 
            }
            
             if (!isClash) {
                  addToDatabase();
                }
            
            
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(CrtFrmController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
   }
   
   private Boolean memClash(int mtnID){
       
       Boolean isClash = false;
       
       try {
            
            String mQuery = "SELECT * FROM users u JOIN user_meeting um "
                    + "ON u.UserID = um.UserID JOIN meetings m ON m.MeetingID "
                    + "= um.MeetingID WHERE m.MeetingID =" + mtnID;
            ResultSet dbMembers = main.DataConn.returnQuery(mQuery);
            
            dbMembers.beforeFirst();
            while (dbMembers.next()) {
                
                String dbName = dbMembers.getString(2);
                System.out.println(dbName);
                
                for (int i = 0; i< members.length;i++) {
                    
                    if (dbName.equalsIgnoreCase(members[i]) && !dbName.equalsIgnoreCase(creator)) {
                        System.out.println("member clash");
                        isClash = true;
                        
                        mtnError.setText(mtnError.getText() + dbName +
                                " has another meeting during that time \n"); 
                    }
                }
                
            }
            
                
        } catch (SQLException ex) {
            Logger.getLogger(CrtFrmController.class.getName()).log(Level.SEVERE, null, ex);
   }
         return isClash;  
   }
   
      private List<String> allUsers(){
       List<String> list = new ArrayList<>(); 
       
       String query = "SELECT `UserName` FROM `users`";
       ResultSet users = main.DataConn.returnQuery(query);
    
       
        try {
               users.first();
            while (users.next()) {
                System.out.println(users.getString(1));
                list.add(users.getString(1));
            }} catch (SQLException ex) {
            Logger.getLogger(CrtFrmController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return list;
   }
   
    @FXML
   private void list(ActionEvent event){
        view();
    }

   private void view() {
        try {
            Stage stage;
            Parent root;
            
            stage = (Stage) mtnTitle.getScene().getWindow();
            
            root = FXMLLoader.load(view.viewFrmCntrlr.class.getResource("viewFrm.fxml"));
                        stage = (Stage) mtnTitle.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
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
        loadDetails();
                TextFields.bindAutoCompletion(
           enterMembers,
           allUsers());
    
    } 
}