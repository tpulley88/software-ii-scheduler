package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.DAO.CountriesDAO;
import SoftwareII.DAO.CustomersDAO;
import SoftwareII.DAO.First_Level_DivisionsDAO;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Country;
import SoftwareII.Model.Customer;
import SoftwareII.Model.First_Level_Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.sql.SQLException;

/**User can choose to view Appointments by Country report. User can choose a country to view a list of the current
 * appointments associated with that country. User can clear the list and/or return to Appointment Dashboard.
 */
public class AppointmentByCustomerCountryReport {

    //Sets stage
    Stage stage;

    //Declare variables
    public ComboBox<Country> countryCB;
    public ListView<Appointment> countryList;
    public Button clearButton;
    public Button returnButton;

    /**Initializes report. Loads country combo box.
     * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {
        countryCB.setItems(CountriesDAO.getAllCountries());
    }

    /**Takes selected country and finds appointments with divisions of selected country. Loads selected appointments into list.
     * @param actionEvent - on click of country
     * @throws SQLException - Exception
     */
    public void onClickCountryCB(ActionEvent actionEvent) throws SQLException {

        if (countryCB.getValue() != null) {
            ObservableList<First_Level_Divisions> getAllDiv = First_Level_DivisionsDAO.getAllDivisions();
            ObservableList<Customer> getAllCust = CustomersDAO.getAllCustomers();
            ObservableList<Appointment> getAllAppointments = AppointmentsDAO.getAllAppts();
            ObservableList<Appointment> countryApptList = FXCollections.observableArrayList();

            Country selectedCountry = countryCB.getValue();
            int countryID = selectedCountry.getCountryID();

            for (First_Level_Divisions div : getAllDiv) {
                if (div.getCountryID() == countryID) {
                    int divID = div.getDivisionID();
                    for (Customer cust : getAllCust) {
                        if (divID == cust.getDivID()) {
                            int custID = cust.getCustID();
                            for (Appointment appt : getAllAppointments) {
                                if (custID == appt.getCustID()) {
                                    countryApptList.add(appt);
                                }
                            }
                        }
                    }
                }
            }
            countryList.setItems(countryApptList);
        }
    }

    /** Clears selections from combo boxes
     * @param actionEvent - on click of clear button
     */
    public void onClickClear(ActionEvent actionEvent) {
        countryList.getItems().clear();
        countryCB.setItems(null);
    }

    /** Returns user to Appointment Dashboard.
     * @param actionEvent - on click of return button
     */
    public void onClickReturn(ActionEvent actionEvent) {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
