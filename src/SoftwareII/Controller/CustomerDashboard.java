package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.DAO.CustomersDAO;
import SoftwareII.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**Customer Dashboard enables user to view current customer list. User can choose to add, modify, or delete a customer.
 * Once user is done with Customer Dashboard, user can choose to return to Scheduling Dashboard, go to Appointments Dashboard, or exit the application.
 */
public class CustomerDashboard {

    //Set stage and scene
    Stage stage;
    Parent scene;

    //Declare variables
    public TableView<Customer> custTable;
    public TableColumn colCustIDNum;
    public TableColumn colCustName;
    public TableColumn colCustAdd;
    public TableColumn colCustState;
    public TableColumn colCustZip;
    public TableColumn colCustCountry;
    public TableColumn colCustPhone;
    public Label userMessLabel;
    public Button custModButton;
    public Button custDeleteButton;
    public Button custAddButton;
    public Button returnMainButton;
    public Button manageApptButton;
    public Button exitButton;
    private static String username;
    public static String userMessage;

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

    //Sets empty list
    ObservableList<Customer> custList = FXCollections.observableArrayList();

    /**Initializes Customer Dashboard. Populates table with list of customers.
     * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {

        //Loads customer table with customers from database
        custList = CustomersDAO.getAllCustomers();

        custTable.setItems(custList);

        colCustIDNum.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colCustAdd.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        colCustState.setCellValueFactory(new PropertyValueFactory<>("custState"));
        colCustZip.setCellValueFactory(new PropertyValueFactory<>("custPostalCode"));
        colCustCountry.setCellValueFactory(new PropertyValueFactory<>("custCountry"));
        colCustPhone.setCellValueFactory(new PropertyValueFactory<>("custPhone"));

        //Selects customer from customer table
        custTable.getSelectionModel().select(0);

        //Displays user message
        userMessLabel.setText(getUserMessage());
    }

    /**Method that allows username to be declared with fields
     * @return - returns username
     */
    public static String getUsername() {
        return username;
    }

    /**Method that sets username from Login page
     * @param user - passed from Login to Scheduling Dashboard to Customer Dashboard
     */
    public static void setUsername(String user) {
        username = user;
    }

    /** Retrieves user message and displays on Appointment Dashboard
     * @return - returns user message
     */
    public static String getUserMessage() {
        return userMessage;
    }

    /** Sets user message to display on Appointment Dashboard
     * @param custMessage - user message
     */
    public static void setUserMessage(String custMessage) { userMessage = custMessage;
    }

    /**Opens Modify Customer form and passes selected customer and username to form. Sets user message on completion.
     * @param actionEvent - on click Modify
     * @throws IOException - Exception
     * @throws SQLException - Exception
     */
    public void onClickModify(ActionEvent actionEvent) throws IOException, SQLException {

        Customer getCust = custTable.getSelectionModel().getSelectedItem();

        if (getCust != null) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/SoftwareII/View/ModifyCustomer.fxml"));
            loader.setResources(rBundle);
            scene = loader.load();

            //Send username and selected customer to Modify Customer Controller
            ModifyCustomer ModCustController = loader.getController();
            ModCustController.setSelectedCustomer(getCust);
            ModifyCustomer.setUsername(username);


            stage = new Stage();
            stage.setTitle(rBundle.getString("modifyCust"));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();

            //Calls refresh method to update customer table
            custTable.getItems().clear();
            custList = CustomersDAO.getAllCustomers();
            custTable.setItems(custList);
            custTable.refresh();

            //Updates user message
            userMessLabel.setText(getUserMessage());
        }
        else {
            //Select customer Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("selectCustomer"));
            alert.showAndWait();
        }
    }

    /**Checks to see if customer has associated appointments. If customer does have associated appointments, those appointments are deleted.
     * Following this, the selected customer is deleted from the SQL database. Shows user message if successful.
     * @param actionEvent - on click Delete
     * @throws SQLException - Exception
     */
    public void onClickDelete(ActionEvent actionEvent) throws SQLException {

        //Selects current customer selection
        Customer currentCustomer = custTable.getSelectionModel().getSelectedItem();

        if (currentCustomer != null) {

            Boolean custHasAppointment = AppointmentsDAO.custAppointment(currentCustomer.getCustID());

            if (!custHasAppointment) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("deletePrompt") + " " + currentCustomer.getCustName());
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //Deletes customer
                    CustomersDAO.deleteCustomer(currentCustomer);

                    //Calls refresh method to update customer table
                    custTable.getItems().clear();
                    custList = CustomersDAO.getAllCustomers();
                    custTable.setItems(custList);
                    custTable.refresh();

                    //Updates user message
                    userMessLabel.setText(currentCustomer.getCustName() + (rBundle.getString("succDelete")));

                }
            } else {
                //Customer has appointments Alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("deletePrompt") + " " + currentCustomer.getCustName() + "? " + rBundle.getString("deleteApptToo"));
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //Deletes customer
                    AppointmentsDAO.deleteCustAppt(currentCustomer.getCustID());
                    CustomersDAO.deleteCustomer(currentCustomer);

                    //Calls refresh method to update customer table
                    custTable.getItems().clear();
                    custList = CustomersDAO.getAllCustomers();
                    custTable.setItems(custList);
                    custTable.refresh();

                    //Updates user message
                    userMessLabel.setText(currentCustomer.getCustName() + (rBundle.getString("succDelete")));
                }
            }
        }
        else {
            //Select customer Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("selectCustomer"));
            alert.showAndWait();
        }


    }

    /**Opens Add Customer form and passes username to form. Sets user message on completion.
     * @param actionEvent - on click Add
     * @throws IOException - Exception
     * @throws SQLException - Exception
     */
    public void onClickAdd(ActionEvent actionEvent) throws IOException, SQLException {

        //Send username to CustomerDashboard Controller
        AddCustomer.setUsername(username);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SoftwareII/View/AddCustomer.fxml"));
        loader.setResources(rBundle);
        scene = loader.load();

        stage = new Stage();
        stage.setTitle(rBundle.getString("addCust"));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
        stage.showAndWait();

        //Calls refresh method to update customer table
        custTable.getItems().clear();
        custList = CustomersDAO.getAllCustomers();
        custTable.setItems(custList);
        custTable.refresh();

        //Updates user message
        userMessLabel.setText(getUserMessage());

    }

    /**Returns user to Scheduling Dashboard
     * @param actionEvent - on click return
     * @throws IOException - Exception
     */
    public void onClickMain(ActionEvent actionEvent) throws IOException {

        custList.clear();

        stage = (Stage) (((Button) actionEvent.getSource()).getScene().getWindow());
        scene = FXMLLoader.load(getClass().getResource("/SoftwareII/View/SchedulingDashboard.fxml"), rBundle);
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**Opens and passes username to Appointments Dashboard
     * @param actionEvent - on click Appointment
     * @throws IOException - Exception
     */
    public void onClickManage(ActionEvent actionEvent) throws IOException {

        //Get username
        String user = getUsername();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SoftwareII/View/AppointmentsDashboard.fxml"));
        loader.setResources(rBundle);
        scene = loader.load();

        //Send username to CustomerDashboard Controller
        AppointmentsDashboard.setUsername(user);


        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**Allows user to exit application
     * @param actionEvent - on click Exit
     */
    public void onClickExit(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("exitprompt"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
