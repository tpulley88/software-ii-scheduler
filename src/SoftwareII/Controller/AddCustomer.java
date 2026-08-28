package SoftwareII.Controller;

import SoftwareII.DAO.CustomersDAO;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Customer;
import SoftwareII.Utility.ZoneChange;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/** Add Customer is the form that allows user to add a new customer to the SQL database. Controller has functionality
 * that includes an auto-generated numeric ID and the ability to provide input for name, address, zip/postal code, and phone number text fields.
 * User also has ability to choose country via combo box, which then populates the state/province combo box.
 * There is an appointment box on form that is disabled due to customer being new and not having any current appointments; however, the manage
 * appointments button on form will redirect user to Appointments Dashboard.
 * On clicking save, fields will be validated and customer will be added to SQL database. Successful message will be displayed on
 * Customer Dashboard.
 * On clicking cancel, values in fields will be discarded and customer will not be added to SQL database.
 */
public class AddCustomer {

    //Set stage and scene
    Parent scene;
    Stage stage;

    //Declare variables
    public TextField custID;
    public TextField custName;
    public TextField custAddress;
    public TextField custZip;
    public TextField custPhone;
    public ComboBox<String> custCountryCB;
    public ComboBox<String> custDivCB;
    public Button manageButton;
    public Button saveButton;
    public Button cancelButton;
    public ListView<Appointment> custApptBox;
    private static String username;

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

    /**Initializes Add Customer controller, sets country combo box values
     * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {

        //Set values for country combo box
        custCountryCB.setPromptText(rBundle.getString("countryPrompt"));
        custCountryCB.setItems(CustomersDAO.getCountryNames());
    }

    /**Method that allows username to be declared with fields
     * @return - returns username
     */
    public static String getUsername() {
        return username;
    }

    /**Method that sets username from Login page
     * @param user - passed from Login to Scheduling Dashboard to Customer Dashboard to Add Customer
     */
    public static void setUsername(String user) {
        username = user;
    }

    /**Takes selected country and populates division combobox
     * @param actionEvent - on click of country combo box
     * @throws SQLException - Exception
     */
    public void onClickCountryCB(ActionEvent actionEvent) throws SQLException {

        Object selectedCountry = custCountryCB.getSelectionModel().getSelectedItem();

        ObservableList<String> divisionNames = CustomersDAO.getDivisionNames(selectedCountry);

        custDivCB.setItems(divisionNames);
    }

    /**Alerts if division menu requested but no country selected
     * @param event - alert event
     */
    public void shownDivCB(Event event) {

        int countrySelection = custCountryCB.getSelectionModel().getSelectedIndex();

        if (countrySelection < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("nocountry"));
            alert.setContentText(rBundle.getString("selectCountry"));
            alert.showAndWait();

            custDivCB.hide();
            custCountryCB.requestFocus();
            custCountryCB.show();
        }
    }

    /** Allows user to go directly to Appointments Dashboard from Add Customer
     * @param actionEvent - on click of manage appointments button
     * @throws IOException - Exception
     */
    public void onClickManage(ActionEvent actionEvent) throws IOException {
        //Get username
        String user = getUsername();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("goApptDash"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            //Close CustomerDashboard
            stage = (Stage) ((Button) actionEvent.getSource()).getParent().getScene().getWindow();
            stage.getOwner().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/SoftwareII/View/AppointmentsDashboard.fxml"));
            loader.setResources(rBundle);
            scene = loader.load();

            //Send username to AppointmentDashboard Controller
            AppointmentsDashboard.setUsername(user);
            AppointmentsDashboard.setUserZone(ZoneId.systemDefault());

            stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
        }
    }

    /**Retrieves selected state and country from appropriate combo boxes. Ensures fields have valid values.
     * Saves new customer to SQL database. Displays successful message on Customer Dashboard.
     * @param actionEvent - on click of save button.
     */
    public void onClickSave(ActionEvent actionEvent) {

        try {

            Object selectedState = custDivCB.getSelectionModel().getSelectedItem();
            Object selectedCountry = custCountryCB.getSelectionModel().getSelectedItem();

            if (selectedState != null && selectedCountry != null) {

                String cusName = custName.getText();
                String cusAddress = custAddress.getText();
                String cusPostalCode = custZip.getText();
                String cusPhone = custPhone.getText();
                LocalDateTime createDate = ZoneChange.getDesiredDateTime(LocalDateTime.now(), ZoneId.systemDefault(), ZoneId.of("UTC"));
                String createBy = username;
                Timestamp lastUpdate = (Timestamp.valueOf(createDate));
                String updateBy = username;

                //Get divID for Customer object
                String cusState = selectedState.toString();
                int cusDivID = CustomersDAO.getDivisionID(cusState);

                if (!cusName.isEmpty() && !cusAddress.isEmpty() && !cusPostalCode.isEmpty() && !cusPhone.isEmpty()) {

                    //Creates customer object
                    Customer newCustomer = new Customer(cusName, cusAddress, cusPostalCode, cusPhone, createDate, createBy, lastUpdate, updateBy, cusDivID);

                    //Adds new customer object to SQL
                    CustomersDAO.addCustomer(newCustomer);

                    //Constructs customer added message
                    String custAddedMessage = newCustomer.getCustName() + (rBundle.getString("succAdd"));

                    //Send user message to CustomerDashboard Controller
                    CustomerDashboard.setUserMessage(custAddedMessage);

                    //Return to Customer Dashboard
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    stage.close();

                } else {
                    //Empty field Alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rBundle.getString("errorDetected"));
                    alert.setContentText(rBundle.getString("validValues"));
                    alert.showAndWait();
                }
            }
            else {
                //Empty field Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("validValues"));
                alert.showAndWait();
            }
        } catch (Exception e) {
            //Empty field Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("validValues"));
            alert.showAndWait();
        }
    }

    /** Cancels addition of new customer and returns user to Customer Dashboard without adding customer to SQL database.
     * @param actionEvent - on click of cancel button
     */
    public void onClickCancel(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("returnCustomer"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) (((Button) actionEvent.getSource()).getScene().getWindow());
            stage.close();
        }

    }
}


