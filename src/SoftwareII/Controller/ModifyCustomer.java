package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
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

/** Modify Customer is a form that allows user to modify an existing customer in the SQL database. Controller has functionality
 * that populates existing information to all fields in Modify Customer. ID field is not editable. There is an appointment box on form
 * that shows customer's current appointments. The manage appointments button on form will redirect user to Appointments Dashboard.
 * On clicking save, fields will be validated and customer will be modified in SQL database. Successful message will be displayed on
 * Customer Dashboard.
 * On clicking cancel, values in fields will be discarded and customer will not be modified.
 */
public class ModifyCustomer {

    //Set stage and scene
    Parent scene;
    Stage stage;

    //Declare variables
    public ComboBox<String> custCountryCB;
    public ComboBox<String> custDivCB;
    public TextField custName;
    public TextField custAddress;
    public TextField custZip;
    public TextField custPhone;
    public TextField custID;
    public ListView<Appointment> custApptBox;

    public Button manageButton;
    public Button saveButton;
    public Button cancelButton;

    private static String username;

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

    /**Initializes Modify Customer form. Populates country combo box.
      * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {
        //Set values for country combo box
        custCountryCB.setPromptText(rBundle.getString("countryPrompt"));
        custCountryCB.setItems(CustomersDAO.getCountryNames());
    }

    /**Method that sets username from Login page
     * @param user - passed from Login to Scheduling Dashboard to Customer Dashboard to Modify Customer
     */
    public static void setUsername(String user) {
        username = user;
    }

    /**Method that allows username to be declared with fields
     * @return - returns username
     */
    public static String getUsername() {
        return username;
    }

    /**Loads and populates fields for customer
     * @param custToUpdate - selected customer
     * @throws SQLException - Exception
     */
    public void setSelectedCustomer(Customer custToUpdate) throws SQLException {
        custID.setText(String.valueOf(custToUpdate.getCustID()));
        custName.setText(custToUpdate.getCustName());
        custAddress.setText(custToUpdate.getCustAddress());
        custCountryCB.setValue(custToUpdate.getCustCountry());
        custDivCB.setValue(custToUpdate.getCustState());
        custZip.setText(custToUpdate.getCustPostalCode());
        custPhone.setText(custToUpdate.getCustPhone());


        custApptBox.setItems(AppointmentsDAO.getCustAppt(custToUpdate.getCustID()));

        Object selectedCountry = custCountryCB.getValue();

        ObservableList<String> divisionNames = CustomersDAO.getDivisionNames(selectedCountry);

        custDivCB.setItems(divisionNames);
    }

    /**Takes selected country and populates division combobox
     * @param actionEvent - on click of country combo box
     * @throws SQLException - Exception
     */
    public void onClickCountryCB(ActionEvent actionEvent) throws SQLException {

        Object selectedCountry = custCountryCB.getSelectionModel().getSelectedItem();

        if (selectedCountry != null) {
            ObservableList<String> divisionNames = CustomersDAO.getDivisionNames(selectedCountry);

            custDivCB.setItems(divisionNames);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("nocountry"));
            alert.setContentText(rBundle.getString("selectCountry"));
            alert.showAndWait();

            custDivCB.hide();
            custCountryCB.requestFocus();
            custCountryCB.show();
        }
    }

    /**Populates division combo box based on selected country
     * @param event - on loading of division
     * @throws SQLException - Exception
     */
    public void showingDivCB(Event event) throws SQLException {
        Object selectedCountry = custCountryCB.getValue();

        ObservableList<String> divisionNames = CustomersDAO.getDivisionNames(selectedCountry);

        custDivCB.setItems(divisionNames);
    }

    /**Alerts if division menu requested but no country selected
     * @param event - alert event
     */
    public void shownDivCB(Event event) {

        String countrySelection = custCountryCB.getSelectionModel().getSelectedItem();

        if (countrySelection == null) {
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

            //Send username to CustomerDashboard Controller
            AppointmentsDashboard.setUsername(user);
            AppointmentsDashboard.setUserZone(ZoneId.systemDefault());

            stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
        }
    }

    /**Retrieves selected state and country from appropriate combo boxes. Ensures fields have valid values.
     * Saves modified customer to SQL database. Displays successful message on Customer Dashboard.
     * @param actionEvent - on click of save button.
     */
    public void onClickSave(ActionEvent actionEvent) {

        try {

            Object selectedState = custDivCB.getSelectionModel().getSelectedItem();
            Object selectedCountry = custCountryCB.getSelectionModel().getSelectedItem();

            if (selectedState != null && selectedCountry != null) {

                int cusID = Integer.parseInt(custID.getText());
                String cusName = custName.getText();
                String cusAddress = custAddress.getText();
                String cusPostalCode = custZip.getText();
                String cusPhone = custPhone.getText();
                Timestamp lastUpdate = (Timestamp.valueOf(ZoneChange.getDesiredDateTime(LocalDateTime.now(), ZoneId.systemDefault(), ZoneId.of("UTC"))));
                String updateBy = username;

                //Get divID for Customer object
                String cusState = selectedState.toString();
                int cusDivID = CustomersDAO.getDivisionID(cusState);

                if (!cusName.isEmpty() && !cusAddress.isEmpty() && !cusPostalCode.isEmpty() && !cusPhone.isEmpty()) {

                    //Creates customer object
                    Customer updatedCustomer = new Customer(cusID, cusName, cusAddress, cusPostalCode, cusPhone, cusDivID, lastUpdate, updateBy);

                    //Adds new customer object to SQL
                    CustomersDAO.modifyCustomer(updatedCustomer);

                    String custAddedMessage = updatedCustomer.getCustName() + (rBundle.getString("succMod"));

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
            e.printStackTrace();

            //Empty field Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("validValues"));
            alert.showAndWait();
        }
    }

    /** Cancels modification of customer and returns user to Customer Dashboard without changing customer in SQL database.
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
