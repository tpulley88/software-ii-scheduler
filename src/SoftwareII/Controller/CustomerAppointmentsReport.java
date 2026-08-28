package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.Month;

/**User can choose a month and type of appointment to view the number of appointments currently associated with that data.
 * User can clear the information and/or return to Appointment Dashboard.
 */
public class CustomerAppointmentsReport {

    //Set stage
    Stage stage;

    //Declare variables
    public ComboBox<Month> monthCB;
    public ComboBox<String> typeCB;
    public Label result;
    public Button clearSel;
    public Button apptDash;

    //Sets empty list
    ObservableList<Month> monthsList = FXCollections.observableArrayList();

    /**Initializes report. Populates month and type combo boxes
     * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {

        int valueCycleMonth = 1;

        while (valueCycleMonth <= Month.DECEMBER.getValue()) {
            monthsList.add(Month.of(valueCycleMonth));
            valueCycleMonth++;
        }

        monthCB.setItems(monthsList);
        typeCB.setItems(AppointmentsDAO.getApptType());
    }

    /**When month is selected, result will show the number of appointments in selected month.
     * @param actionEvent - on click of month
     * @throws SQLException - Exception
     */
    public void onClickMonth(ActionEvent actionEvent) throws SQLException {

        if (typeCB.getValue() != null) {

            ObservableList<Appointment> allAppt = AppointmentsDAO.getAllAppointments();

            int apptCount = 0;
            Month selectedMonth = monthCB.getValue();
            String selectedType = typeCB.getValue();

            for (Appointment appt : allAppt) {
                if (selectedMonth == appt.getApptStartDate().getMonth()) {
                    if (selectedType.equals(appt.getApptType())) {
                        ++apptCount;
                    }
                }
            }
            result.setText(String.valueOf(apptCount));
        }
    }

    /**When type is selected, result will show the number of appointments associated with that type
     * @param actionEvent - on click of type
     * @throws SQLException - Exception
     */
    public void onClickType(ActionEvent actionEvent) throws SQLException {

        if (typeCB.getValue() != null) {

            ObservableList<Appointment> allAppt = AppointmentsDAO.getAllAppointments();

            int apptCount = 0;
            Month selectedMonth = monthCB.getValue();
            String selectedType = typeCB.getValue();

            for (Appointment appt : allAppt) {

                if (selectedMonth == appt.getApptStartDate().getMonth()) {
                    if (selectedType.equals(appt.getApptType())) {
                        ++apptCount;
                    }
                }
            }
            result.setText(String.valueOf(apptCount));
        }
    }

    /**Clears month and type combo boxes
     * @param actionEvent - on click clear
     */
    public void onClickClear(ActionEvent actionEvent) {
        monthCB.setValue(null);
        typeCB.setValue(null);
    }

    /**Returns user to Appointment Dashboard
     * @param actionEvent - on click Appointment Dashboard
     */
    public void onClickApptDash(ActionEvent actionEvent) {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
