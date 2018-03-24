/*
    Author: Jared Dalessandro
    Date: 3/23/18
    Purpose: Keeps track of income
*/

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App extends Application {


    @Override
    public void start(Stage primaryStage) {

        final String FILENAME = "deposits.txt";
        final File file = new File(FILENAME);

        // GRID & SCENE & STAGE
        BorderPane mainPane = new BorderPane();
        HBox topPane = new HBox();
        GridPane gridpane = new GridPane();

        // Config for the textfields' container
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setPadding(new Insets(10,10,15,10));
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setStyle("-fx-background-color: LIGHTSTEELBLUE");


        TextField memoField = new TextField();
        TextField dateField = new TextField();
        TextField grossField = new TextField();
        TextField netField = new TextField();

        // Textfields would get out of control without this
        memoField.setMaxWidth(140);
        dateField.setMaxWidth(60);
        grossField.setMaxWidth(50);
        netField.setMaxWidth(50);

        // Dont want to checkkey for the string textfields
        grossField.setOnKeyTyped(new CheckKey(grossField));
        netField.setOnKeyTyped(new CheckKey(netField));

        // Add Labels for the Textfields and then add TextFields to grid
        gridpane.add(new Label("Memo:"), 0,0);
        gridpane.add(new Label("Date:"),2,0);
        gridpane.add(new Label("Gross:"), 4,0);
        gridpane.add(new Label("Net:"),6,0);
        gridpane.add(memoField,1,0);
        gridpane.add(dateField,3,0);
        gridpane.add(grossField,5,0);
        gridpane.add(netField,7,0);

        // TABLEVIEW Config
        TableView<Deposit> table = new TableView<>();
        TableColumn<Deposit, SimpleStringProperty> memoCol = new TableColumn<>("Memo");
        TableColumn<Deposit, SimpleStringProperty> dateCol = new TableColumn<>("Date");
        TableColumn<Deposit, SimpleDoubleProperty> grossCol = new TableColumn<>("Gross Income");
        TableColumn<Deposit, SimpleDoubleProperty> netCol = new TableColumn<>("Net Income");
        table.setPrefSize(650,360);


        // Set TableView Column widths
        memoCol.setMinWidth(250);
        dateCol.setMinWidth(100);
        grossCol.setMinWidth(150);
        netCol.setMinWidth(150);

        // TABLEVIEW Factories
        memoCol.setCellValueFactory(new PropertyValueFactory<>("memo"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        grossCol.setCellValueFactory(new PropertyValueFactory<>("grossIncome"));
        netCol.setCellValueFactory(new PropertyValueFactory<>("netIncome"));

        // Button declarations
        Button addBtn = new Button("Add");
        addBtn.setStyle("-fx-background-color: LIGHTSLATEGREY");
        addBtn.setTextFill(Color.WHITE);
        addBtn.setPrefWidth(80);
        gridpane.add(addBtn,8,0);



        // Place panes in correct orientation
        table.getColumns().addAll(memoCol,dateCol,grossCol,netCol);
        topPane.getChildren().add(table);
        mainPane.setTop(topPane);
        mainPane.setBottom(gridpane);

        // GET DATA
        getDeposits(table, file);


        ////////////// SCENE, ICON, STAGE //////////////////////////
        Scene scene = new Scene(mainPane, 650, 400);
        primaryStage.setTitle("Gold Ledger");
        Image icon = new Image("file:money.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


        ///////////////////// BUTTON HANDLERS /////////////////////////////

        // Writes text field values to deposits.txt and then refreshes data
        // If a field is missing doesn't write data.
        addBtn.setOnAction((ActionEvent e) -> {

            try{
                //This way it's in append mode, so that we don't delete previous data
                PrintWriter writer = new PrintWriter(new FileWriter(file, true));

                // Create alert if missing data
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Missing Data");
                alert.setContentText("Make sure all fields are entered.");

                // Checks to make sure fields are filled. Otherwise sends a warning.
                if (memoField.getLength() == 0) {
                    alert.show();
                    writer.close();
                }
                if (dateField.getLength() == 0) {

                    alert.show();
                    writer.close();
                }
                if (grossField.getLength() == 0 ){
                    alert.show();
                    writer.close();
                }
                if (netField.getLength() == 0) {
                    alert.show();
                    writer.close();
                }

                // Don't need incomes to be in double for writing
                String memo = memoField.getText();
                String date = dateField.getText();
                String grossIncome = grossField.getText();
                String netIncome = netField.getText();

                // Writes to the file
                writer.println();
                writer.println(memo);
                writer.println(date);
                writer.println(grossIncome);
                writer.print(netIncome); //This way we write an extra blank line at the end.
                writer.close();

                //Clear fields once data is sent.
                memoField.clear();
                dateField.clear();
                grossField.clear();
                netField.clear();

            } catch(IOException s){
                System.out.println(s);
            }


            // Refresh the table after writing
            getDeposits(table, file);

        });

    }

    // Reads deposits from deposits.txt
    private void getDeposits(TableView table, File file){

        ArrayList<Deposit> depositArrayList = new ArrayList<>();

        try {

            Scanner sc = new Scanner(file);

            //Clears the arrayList before each reading
            depositArrayList.clear();

            while(sc.hasNextLine()) {

                Deposit depositObj = new Deposit();
                depositObj.setMemo(sc.nextLine());
                depositObj.setDate(sc.nextLine());
                depositObj.setGrossIncome(Double.parseDouble(sc.nextLine()));
                depositObj.setNetIncome(Double.parseDouble(sc.nextLine()));
                depositArrayList.add(depositObj);

            }
        }
        catch(Exception e){
            System.out.println("ERROR: Couldn't find file.");
        }

        // TableView's require an observableList, so we must convert.
        ObservableList<Deposit> olDepositList = FXCollections.observableArrayList(depositArrayList);
        table.setItems(olDepositList);
    }

    //////////////// LIGHT THE CANDLE ///////////
    public static void main(String[] args){
        launch(args);
    }

}

/*
Checks for non-numeric keys
Uses the textfield as a parameter takes in constructor
checks if the text includes any non-numerics using regex
Syntax: tfOtherIncome.setOnKeyTyped(new CheckKey(tfOtherIncome))
*/
class CheckKey implements EventHandler<KeyEvent> {

    private TextField tf;

    public CheckKey(TextField tf){
        this.tf = tf;
    }

    public void handle(KeyEvent e) {

        //Doesn't throw error color for input: 0-9 or "," or "."
        if(!(tf.getText().matches("[0-9,.]+"))) {
            //tf.setBackground();
            tf.setStyle("-fx-background-color: firebrick");
        }
        else {
            tf.setStyle("-fx-background-color: white");
        }
    }
}
