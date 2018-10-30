/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author SEGBEFIA_c
 */

import java.util.Arrays;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class test2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        
        String[][] staffArray = {{"Monday ", "Tuesday", "Wenesday", "Thursday"},
                                 {"1", "2", "3", "4"},
                                 {"5", "6", "7","8"}};
        
        
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        
        TableView<String[]> table = new TableView<>();
        
        for (int i = 0; i < staffArray[0].length; i++) {
            
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            
            tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
                
            });
            
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
            
        }
        
        table.setItems(data);
        root.getChildren().add(table);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
        
    }
    
    	public static void main(String[] args) {
		launch(args);
	}
}
