package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.DAO.ContactsDAO;
import SoftwareII.DAO.CustomersDAO;
import SoftwareII.Interface.ErrorAlert;
import SoftwareII.Interface.NewLoaderStageScene;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Contact;
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
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**Appointment Dashboard allows user to view current appointment list. User can choose to view all appointments, view appointments
 *  by current month, view appointments by current year, or view current day's appointments. User can choose to add, modify, or
 *  delete an appointment. User can also view reports (Number of Appointments by type/month, schedule of appointments by contact, or
 *  list of appointments by country). User can choose to return to Scheduling Dashboard, go to Customers Dashboard, or exit the application.
 */
public class AppointmentsDashboard {

    //Sets stage and scene
    Parent scene;
    Stage stage;

    //Declare variables
    public ToggleGroup apptRadioButtons;
    public RadioButton viewAllRadio;
    public RadioButton viewMonthRadio;
    public RadioButton viewWeekRadio;
    public RadioButton viewDayRadio;
    public TableColumn<Object, Object> IDNumberCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDTCol;
    public TableColumn endDTCol;
    public TableColumn custIDCol;
    public TableColumn userIDCol;
    public TableView<Appointment> apptTable = new TableView<>();
    public Label userMessLabel;
    public Button modifyAppt;
    public Button deleteAppt;
    public Button addAppt;
    public ComboBox<Customer> searchCustCB;
    public ComboBox<Contact> searchContactCB;

    private static String username;
    public static String userMessage;
    private static ZoneId userZone;

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

    //Declares an empty array list
    ObservableList<Appointment> apptList = FXCollections.observableArrayList();

    //Lambda expression for error message
    ErrorAlert showErrorMess = (str1, str2) -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(str1);
        alert.setContentText(str2);
        alert.showAndWait();
        return alert;
    };

    //Lambda expression for loading window
    NewLoaderStageScene loadNew = (resourceName, resourceString, actionEv) -> {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(resourceName));
        loader.setResources(rBundle);
        scene = loader.load();

        stage = new Stage();
        stage.setTitle(rBundle.getString(resourceString));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Button) actionEv.getSource()).getScene().getWindow());
        stage.showAndWait();
    };

    /**Initializes Appointment Dashboard and populates tableview with all appointments from SQL database.
     * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {

        //Selects View All radio button
        apptRadioButtons.selectToggle(viewAllRadio);

        //Loads customer table with customers from database
        apptList = AppointmentsDAO.getAllApptsDTString();

        //Populate Combo Boxes
        searchContactCB.setItems(ContactsDAO.getAllContacts());
        searchCustCB.setItems(CustomersDAO.getAllCustomers());

        IDNumberCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("apptLoc"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        startDTCol.setCellValueFactory(new PropertyValueFactory<>("apptStartDTS"));
        endDTCol.setCellValueFactory(new PropertyValueFactory<>("apptEndDTS"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //Selects appointment from appointment table
        apptTable.getSelectionModel().select(0);

        //Displays user message
        userMessLabel.setText(getUserMessage());

        //Sets Tableview items
        apptTable.setItems(apptList);
    }

    /**Method that allows username to be declared with fields
     * @return - returns username
     */
    public static String getUsername() {
        return username;
    }

    /**Method that sets username from Login page
     * @param user - passed from Login to Scheduling Dashboard to Appointment Dashboard
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

    /**Method to set user's zone
     * @param user - retrieves user's ZoneID
     */
    public static void setUserZone(ZoneId user) { userZone = user;
    }

    /**Sets table view to include all appointments from SQL database. Resets customer/contact combo boxes.
     * @param actionEvent - on click of View All toggle button
     * @throws SQLException - Exception
     */
    public void onClickViewAll(ActionEvent actionEvent) throws SQLException {
        apptList = AppointmentsDAO.getAllApptsDTString();
        apptTable.setItems(apptList);

        if (searchContactCB.getValue() != null) {
            searchContactCB.setItems(null);
            searchContactCB.setItems(ContactsDAO.getAllContacts());
        }

        if (searchCustCB.getValue() != null) {
            searchCustCB.setItems(null);
            searchCustCB.setItems(CustomersDAO.getAllCustomers());
        }
    }

    /**Sets table view to include current month's appointments from SQL database. Resets customer/contact combo boxes.
     * @param actionEvent - on click of View Month's toggle button
     * @throws SQLException - Exception
     */
    public void onClickViewMonth(ActionEvent actionEvent) throws SQLException {
        if (searchContactCB.getValue() != null) {
            searchContactCB.setItems(null);
            searchContactCB.setItems(ContactsDAO.getAllContacts());
        }

        if (searchCustCB.getValue() != null) {
            searchCustCB.setItems(null);
            searchCustCB.setItems(CustomersDAO.getAllCustomers());
        }

        apptList = AppointmentsDAO.getMonthAppointment();
        apptTable.setItems(apptList);
    }

    /**Sets table view to include current week's appointments from SQL database. Resets customer/contact combo boxes.
     * @param actionEvent - on click of View Week's toggle button
     * @throws SQLException - Exception
     */
    public void onClickViewWeek(ActionEvent actionEvent) throws SQLException {
        if (searchContactCB.getValue() != null) {
            searchContactCB.setItems(null);
            searchContactCB.setItems(ContactsDAO.getAllContacts());
        }

        if (searchCustCB.getValue() != null) {
            searchCustCB.setItems(null);
            searchCustCB.setItems(CustomersDAO.getAllCustomers());
        }

        apptList = AppointmentsDAO.getWeekAppointment();
        apptTable.setItems(apptList);
    }

    /**Sets table view to include current day's appointments from SQL database. Resets customer/contact combo boxes.
     * @param actionEvent - on click of View Day's toggle button
     * @throws SQLException - Exception
     */
    public void onClickViewDay(ActionEvent actionEvent) throws SQLException {
        if (searchContactCB.getValue() != null) {
            searchContactCB.setItems(null);
            searchContactCB.setItems(ContactsDAO.getAllContacts());
        }

        if (searchCustCB.getValue() != null) {
            searchCustCB.setItems(null);
            searchCustCB.setItems(CustomersDAO.getAllCustomers());
        }

        apptList = AppointmentsDAO.getDaysAppointment();
        apptTable.setItems(apptList);

    }

    /**Allows user to search for appointments by Customer name using Combox box. Resets Contact box.
     * @param actionEvent
     * @throws SQLException
     */
    public void onClickSearchCustCB(ActionEvent actionEvent) throws SQLException {
        Customer customer = searchCustCB.getValue();

        if (customer != null) {
            apptRadioButtons.selectToggle(viewAllRadio);

            apptTable.setItems(CustomersDAO.getApptList(customer));

            if (searchContactCB.getValue() != null) {
                searchContactCB.setItems(null);
                searchContactCB.setItems(ContactsDAO.getAllContacts());
            }
        }
    }

    /**Allows user to search for appointments by Customer name using Combox box. Resets Contact box.
     * @param actionEvent
     * @throws SQLException
     */
    public void onClickSearchContactCB(ActionEvent actionEvent) throws SQLException {
        Contact contact = searchContactCB.getValue();

        if (contact != null) {
            apptRadioButtons.selectToggle(viewAllRadio);

            apptTable.setItems(ContactsDAO.getApptList(contact));

            if (searchCustCB.getValue() != null) {
                searchCustCB.setItems(null);
                searchCustCB.setItems(CustomersDAO.getAllCustomers());
            }
        }
    }

    /**Allows user to reset combo boxes and display All Appointments
     * @param actionEvent
     * @throws SQLException
     */
    public void onClickClear(ActionEvent actionEvent) throws SQLException {
        viewAllRadio.fire();
        apptList = AppointmentsDAO.getAllApptsDTString();
        apptTable.setItems(apptList);

        if (searchContactCB.getValue() != null) {
            searchContactCB.setItems(null);
            searchContactCB.setItems(ContactsDAO.getAllContacts());
        }

        if (searchCustCB.getValue() != null) {
            searchCustCB.setItems(null);
            searchCustCB.setItems(CustomersDAO.getAllCustomers());
        }

    }

    /**Opens Modify Appointment form and passes selected appointment, username, and user zone to form. Sets user message on completion.
     *
     *<p><b>
     * *******************
     * LAMBDA EXPRESSION USED:
     *
     *     showErrorMess.errorAlert((rBundle.getString("errorDetected")), (rBundle.getString("selectAppointment")));
     *
     * ErrorAlert lambda expression is used in this method to alert user if a potential issue. This lambda
     * expression allows for the reduction of code from 5 lines to 1 simple line with only 2 input parameters. This makes code much
     * easier to produce and also reduces the length of code and ease of reading.
     * *******************
     *</b></p>
     *
     * @param actionEvent - on click Modify Appointment
     * @throws IOException - Exception
     * @throws SQLException - Exception
     */
    public void onClickModify(ActionEvent actionEvent) throws IOException, SQLException {

        try {
            Appointment getAppt = apptTable.getSelectionModel().getSelectedItem();

            if (getAppt != null) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/SoftwareII/View/ModifyAppointment.fxml"));
                loader.setResources(rBundle);

                //Send username and user zone to Modify Appointment
                ModifyAppointment.setUserZone(userZone);
                ModifyAppointment.setUsername(username);
                scene = loader.load();

                //Sends selected appointment to Modify Appointment
                ModifyAppointment ModifyApptController = loader.getController();
                ModifyApptController.setSelectedAppointment(getAppt);

                stage = new Stage();
                stage.setTitle(rBundle.getString("modifyAppt"));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
                stage.showAndWait();

                //Calls refresh to update appointment table
                apptTable.getItems().clear();
                apptList = AppointmentsDAO.getAllApptsDTString();
                apptTable.setItems(apptList);
                apptTable.refresh();
                apptRadioButtons.selectToggle(viewAllRadio);


                //Updates user message
                userMessLabel.setText(getUserMessage());

            } else {

                //Select customer Alert
                showErrorMess.errorAlert((rBundle.getString("errorDetected")), (rBundle.getString("selectAppointment")));
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    /**Deletes selected appointment from SQL database. Shows user message if successful.
     * @param actionEvent - on click Delete button
     * @throws SQLException - Exception
     */
    public void onClickDelete(ActionEvent actionEvent) throws SQLException {

        //Selects current appointment
        Appointment currentAppointment = apptTable.getSelectionModel().getSelectedItem();

        if (currentAppointment != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("deletePrompt") + " " + currentAppointment.getApptTitle());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                //Deletes customer
                AppointmentsDAO.deleteAppointment(currentAppointment);

                //Calls refresh method to update customer table
                apptTable.getItems().clear();
                apptList = AppointmentsDAO.getAllApptsDTString();
                apptTable.setItems(apptList);
                apptTable.refresh();
                apptRadioButtons.selectToggle(viewAllRadio);


                //Updates user message
                userMessLabel.setText((rBundle.getString("apptID")) + ": " + currentAppointment.getApptID() + "\n" + (rBundle.getString("title")) + ": " +
                        currentAppointment.getApptTitle() + "\n" + (rBundle.getString("type")) + ": "  + currentAppointment.getApptType() + "\n" + (rBundle.getString("succDelete")));

            }
        }
        else {
            //Select customer Alert
            showErrorMess.errorAlert((rBundle.getString("errorDetected")), (rBundle.getString("selectCustomer")));
        }

    }

    /**Opens Add Appointment form and passes username and user zone to form. Sets user message on completion.
     *
     *<p><b>
     * *******************
     * LAMBDA EXPRESSION USED:
     *
     *      loadNew.load("/SoftwareII/View/AddAppointment.fxml", "addAppt", actionEvent);
     *
     * NewLoaderStageScene lambda expression is used in this method to load a new stage and new scene. This lambda
     * expression allows for the reduction of code from 11 lines to 1 simple line with 3 input parameters. This makes code much
     * easier to produce and also reduces the length of code and ease of reading.
     * *******************
     *</b></p>
     *
     * @param actionEvent - on click Add Appointment
     * @throws IOException - Exception
     * @throws SQLException - Exception
     */
    public void onClickAdd(ActionEvent actionEvent) throws IOException, SQLException {

        //Send username and user zone to Add Appointment Controller
        AddAppointment.setUsername(username);
        AddAppointment.setUserZone(userZone);

        //Load Add Appointment form
        loadNew.load("/SoftwareII/View/AddAppointment.fxml", "addAppt", actionEvent);

        //Calls refresh method to update appointment table
        apptTable.getItems().clear();
        apptList = AppointmentsDAO.getAllApptsDTString();
        apptTable.setItems(apptList);
        apptTable.refresh();
        apptRadioButtons.selectToggle(viewAllRadio);

        //Updates user message
        userMessLabel.setText(getUserMessage());

    }

    /**Opens report of customer appointments by type/month
     * @param actionEvent - on click Number of Appointments
     * @throws IOException - Exception
     */
    public void onClickApptCust(ActionEvent actionEvent) throws IOException {

        loadNew.load("/SoftwareII/View/CustomerAppointmentsReport.fxml", "numApptCust", actionEvent);

    }

    /**Opens report of list of appointments by contact
     * @param actionEvent - on click Contact Schedule
     * @throws IOException - Exception
     */
    public void onClickContSchedule(ActionEvent actionEvent) throws IOException {

        loadNew.load("/SoftwareII/View/ContactScheduleReport.fxml", "contSchedule", actionEvent);

    }

    /**Opens report of list of appointments by country
     * @param actionEvent - on click Country Appointments
     * @throws IOException - Exception
     */
    public void onClickCountryAppt(ActionEvent actionEvent) throws IOException {

        loadNew.load("/SoftwareII/View/AppointmentByCustomerCountryReport.fxml", "countryAppt", actionEvent);
    }

    /**Returns user to Scheduling Dashboard
     * @param actionEvent - on click Return to Main
     * @throws IOException - Exception
     */
    public void onClickReturnMain(ActionEvent actionEvent) throws IOException {

        //Return to Scheduling Dashboard
        stage = (Stage) (((Button) actionEvent.getSource()).getScene().getWindow());
        scene = FXMLLoader.load(getClass().getResource("/SoftwareII/View/SchedulingDashboard.fxml"), rBundle);
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**Opens and passes username to Customer Dashboard.
     * @param actionEvent - on click Manage Customers
     * @throws IOException - Exception
     */
    public void onClickManageCust(ActionEvent actionEvent) throws IOException {

        //Get username
        String user = getUsername();

        //Send username to CustomerDashboard Controller
        CustomerDashboard.setUsername(user);

        //Load Customer Dashboard
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SoftwareII/View/CustomerDashboard.fxml"));
        loader.setResources(rBundle);
        scene = loader.load();

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
