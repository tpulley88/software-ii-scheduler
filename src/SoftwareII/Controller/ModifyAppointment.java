package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.DAO.ContactsDAO;
import SoftwareII.DAO.CustomersDAO;
import SoftwareII.DAO.UsersDAO;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Contact;
import SoftwareII.Model.Customer;
import SoftwareII.Utility.ZoneChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/** Modify Appointment is a form that allows user to modify an existing appointment in the SQL database. Controller has functionality
 * that populates existing information to all fields in Modify Appointment. ID field is not editable. Appt start/end date/times must remain
 * within business hours and must not conflict with existing appointment times.
 * On clicking save, fields will be validated and appointment will be modified in SQL database. Successful message will be displayed on
 * Appointment Dashboard.
 * On clicking cancel, values in fields will be discarded and appointment will not be modified.
 */
public class ModifyAppointment {

    //Set stage
    Stage stage;

    //Declare variables
    public TextField apptID;
    public TextField apptTitle;
    public TextField description;
    public TextField apptLocation;
    public ComboBox<Contact> contactCB;
    public TextField apptType;
    public ComboBox<Customer> custIDCB;
    public DatePicker startDate;
    public ComboBox<LocalTime> startTime;
    public DatePicker endDate;
    public ComboBox<LocalTime> endTime;
    public Label usernameLabel;
    public Button saveButton;
    public Button cancelButton;

    private static String username = getUsername();
    public static String apptModifiedMessage;
    private static ZoneId userZone;
    private static final ZoneId estZone = ZoneId.of("America/New_York");
    private static final ZoneId utcZone = ZoneId.of("UTC");

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());


    /**Initializes Modify Appointment. Populates combo boxes.
     * @throws SQLException - Exception
     */
    public void initialize() throws SQLException {

        //Set values for combo boxes
        contactCB.setItems(ContactsDAO.getAllContacts());
        custIDCB.setItems(CustomersDAO.getAllCustomers());

        //Set username text
        usernameLabel.setText(username);
    }

    /**Method that sets username from Login page
     * @param user - passed from Login to Scheduling Dashboard to Appointment Dashboard to Modify Appointment
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

    /**Method to set user's zone
     * @param user - retrieves user's ZoneID
     */
    public static void setUserZone(ZoneId user) { userZone = user;
    }

    /**Loads and populates fields for appointment.
      * @param apptUpdate - appointment to be modified
     * @throws SQLException - Exception
     */
    public void setSelectedAppointment(Appointment apptUpdate) throws SQLException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        //Time Conversion
        LocalDateTime startDTS = LocalDateTime.parse((apptUpdate.getApptStartDTS()), dtf);
        LocalDate startD = startDTS.toLocalDate();
        LocalTime startT = startDTS.toLocalTime();
        LocalDateTime endDTS = LocalDateTime.parse((apptUpdate.getApptEndDTS()), dtf);
        LocalDate endD = endDTS.toLocalDate();
        LocalTime endT = endDTS.toLocalTime();

        //Populate text fields
        apptID.setText(String.valueOf(apptUpdate.getApptID()));
        apptTitle.setText(String.valueOf(apptUpdate.getApptTitle()));
        description.setText(apptUpdate.getApptDesc());
        apptLocation.setText(apptUpdate.getApptLoc());
        apptType.setText(apptUpdate.getApptType());
        startDate.setValue(startD);
        startTime.setValue(startT);
        endDate.setValue(endD);
        endTime.setValue(endT);

        //Populate combo boxes and userID
        custIDCB.setValue(CustomersDAO.uploadCust(apptUpdate.getCustID()));
        contactCB.setValue(ContactsDAO.uploadContact(apptUpdate.getContactID()));
    }


    /**Checks for logical validation of start date, making sure it's not in the past.
     * If selected start date is current day, ensures start time combo box displays only future times. End time is set to 15 minutes after
     * start time.
     * Sets end date automatically to chosen start date.
     * @param actionEvent - On click of start date
     */
    public void onClickStartDate(ActionEvent actionEvent) throws NullPointerException{

        try {
            LocalDate selectedStartDate = startDate.getValue();
            LocalDateTime easternDT = ZoneChange.getDesiredDateTime(LocalDateTime.now(), userZone, estZone);

            if (selectedStartDate.isBefore(LocalDate.now())) {
                //Invalid Date Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("laterDate"));
                alert.showAndWait();

                startDate.getEditor().clear();
            } else {
                endDate.setValue(selectedStartDate);
            }
            if (selectedStartDate.equals(easternDT.toLocalDate())) {

                //Establish EST/Local times
                LocalTime currentTime = LocalTime.now();

                LocalTime newStartTime = LocalTime.of(currentTime.getHour() + 1, 0);
                LocalTime newEndTime = LocalTime.of(newStartTime.getHour(), newStartTime.getMinute()+15);

                startTime.setValue(newStartTime);
                endTime.setValue(newEndTime);
            } else {

                LocalTime openingTimeEST = LocalTime.of(8, 0);
                LocalTime openingTimeLocal = ZoneChange.getDesiredDateTime((LocalDateTime.of(selectedStartDate, openingTimeEST)), estZone, userZone).toLocalTime();

                startTime.setValue(openingTimeLocal);
                endTime.setValue(openingTimeLocal.plusMinutes(15));

            }
        } catch (NullPointerException ignored) {}
    }

    /** Ensures start times are within business hours
     * Sets end time automatically to 15 minutes after start time.
     * @param actionEvent - On click of start time
     */
    public void onClickStartTime(ActionEvent actionEvent) {

        try {
            //Get current selections
            LocalDate selectedStartDate = startDate.getValue();
            LocalTime selectedStartTime = startTime.getSelectionModel().getSelectedItem();

            //Establish EST & Local business hours
            LocalTime openingTimeEST = LocalTime.of(8, 0);
            LocalDateTime openingTimeLocal = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, openingTimeEST), estZone, userZone);

            if (selectedStartTime != null) {

                if (selectedStartDate.getMonth().getValue() > 1) {

                    if (selectedStartTime.isBefore(openingTimeLocal.toLocalTime())) {
                        //Business hours alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(rBundle.getString("errorDetected"));
                        alert.setContentText(rBundle.getString("outsideBH"));
                        alert.showAndWait();

                        startTime.setValue(openingTimeLocal.toLocalTime());
                        endTime.setValue(openingTimeLocal.toLocalTime().plusMinutes(15));
                    }
                }else {
                    //Invalid Date Alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rBundle.getString("errorDetected"));
                    alert.setContentText(rBundle.getString("selectStartDate"));
                    alert.showAndWait();

                    startDate.show();
                }

                endTime.setValue(startTime.getValue().plusMinutes(15));

            }

        }catch (NullPointerException | IndexOutOfBoundsException ignored) { }
    }

    /**Loads 15-minute interval start times in user's time zone.
     * Ensures combo box only loads times within business hours.
     * Sets end time automatically to 15 minutes after start time.
     * @param event - On loading of combo box for start times
     */
    public void onShowLoadNewStartTimes(Event event) {

        try {
            LocalDate selectedStartDate = startDate.getValue();

            //Establish EST & Local business hours/date
            LocalTime openingTimeEST = LocalTime.of(8, 0);
            LocalTime openingTimeLocal = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, openingTimeEST), estZone, userZone).toLocalTime();
            LocalTime currentTime = LocalTime.now();
            LocalTime currentTimeEST = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, currentTime), estZone, userZone).toLocalTime();
            LocalTime closingTimeEST = LocalTime.of(22, 0);
            LocalTime closingTimeLocal = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, closingTimeEST), estZone, userZone).toLocalTime();

            LocalDate localDate = ZoneChange.getDesiredDateTime(LocalDateTime.of(LocalDate.now(), currentTimeEST), userZone, estZone).toLocalDate();

            if (selectedStartDate.equals(localDate)) {

                LocalTime newStartTime = LocalTime.of(LocalTime.now().getHour() + 1, 0);
                LocalTime newEndTime = LocalTime.of(newStartTime.getHour(), newStartTime.getMinute() + 15);

                startTime.setValue(newStartTime);
                endTime.setValue(newEndTime);

                ObservableList timesList = FXCollections.observableArrayList();

                while (newStartTime.isBefore(closingTimeLocal)) {
                    timesList.add(newStartTime);
                    newStartTime = newStartTime.plusMinutes(15);
                }
                startTime.setItems(timesList);

            } else {

                ObservableList timesList = FXCollections.observableArrayList();

                while (openingTimeLocal.isBefore(closingTimeLocal)) {
                    timesList.add(openingTimeLocal);
                    openingTimeLocal = openingTimeLocal.plusMinutes(15);
                }
                startTime.setItems(timesList);
            }
        } catch (NullPointerException e) {
            //Invalid Date Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("selectStartDate"));
            alert.showAndWait();

            startTime.hide();
            startDate.requestFocus();
            startDate.show();

        }
    }

    /**Checks for logical validation of end date, making sure it's not before start date.
     * If selected start date is current day, ensures start time combo box displays only future times
     * @param actionEvent - On click of end date
     */
    public void onClickEndDate(ActionEvent actionEvent) {

        try {
            LocalDate selectedEndDate = endDate.getValue();
            LocalDate selectedStartDate = startDate.getValue();
            LocalDate localDate = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, LocalTime.now()), userZone, estZone).toLocalDate();

            if (selectedEndDate.isBefore(localDate)) {
                //Invalid Date Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("laterDate"));
                alert.showAndWait();

                endDate.getEditor().clear();
            }

            if (selectedEndDate.isBefore(startDate.getValue())) {
                //Invalid Date Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("endAfterStart"));
                alert.showAndWait();

                endDate.getEditor().clear();
            }
        } catch (NullPointerException e) {
            //Invalid Date Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("selectStartDate"));
            alert.showAndWait();

            startDate.show();
        }
    }

    /** Ensures end times are after start times and within business hours.
     * @param actionEvent - On click of end time
     */
    public void onClickEndTime(ActionEvent actionEvent) {

        try {

            if (startTime.getValue() == null) {
                //Invalid Time Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("selectStartTime"));
                alert.showAndWait();
            }

            LocalTime selectedEndTime = endTime.getSelectionModel().getSelectedItem();
            LocalDate selectedStartDate = startDate.getValue();

            if (selectedStartDate != null) {
                //Establish EST & Local business hours
                LocalTime closingTimeEST = LocalTime.of(22, 0);
                LocalTime closingTimeLocal = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, closingTimeEST), estZone, userZone).toLocalTime();


                if (selectedEndTime.isAfter(closingTimeLocal)) {
                    //Business hours alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rBundle.getString("errorDetected"));
                    alert.setContentText(rBundle.getString("outsideBH"));
                    alert.showAndWait();

                    endTime.getItems().clear();
                }
            } else {
                //Invalid Date Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("selectStartDate"));
                alert.showAndWait();
            }

            if (selectedEndTime != null && selectedStartDate != null) {
                if ((endTime.getValue() == startTime.getValue()) && selectedStartDate.isEqual(endDate.getValue())) {
                    //Invalid Time Alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rBundle.getString("errorDetected"));
                    alert.setContentText(rBundle.getString("endTimeError"));
                    alert.showAndWait();

                    endTime.hide();
                    startTime.show();
                }
            }
        } catch (NullPointerException ignored) {        }
    }

    /** Loads end times in current time zone for user zone.
     * Ensures combo box only loads times within business hours.
     * @param event - On loading of combo box for end times
     */
    public void onShowLoadNewEndTime(Event event) {

        try {
            //Get current selections
            LocalDate selectedStartDate = startDate.getValue();
            LocalDate selectedEndDate = endDate.getValue();
            LocalTime selectedStartTime = startTime.getSelectionModel().getSelectedItem();

            if (selectedStartDate != null) {
                //Establish EST & Local business hours
                LocalTime openingTimeEST = LocalTime.of(8, 0);
                LocalTime openingTimeLocal = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, openingTimeEST), estZone, userZone).toLocalTime();
                LocalTime closingTimeEST = LocalTime.of(22, 0);
                LocalTime closingTimeLocal = ZoneChange.getDesiredDateTime(LocalDateTime.of(selectedStartDate, closingTimeEST), estZone, userZone).toLocalTime();

                ObservableList<LocalTime> timesList = FXCollections.observableArrayList();

                if (selectedEndDate.isEqual(selectedStartDate)) {
                    while (selectedStartTime.isBefore(closingTimeLocal) || selectedStartTime.equals(closingTimeLocal)) {

                        timesList.add(selectedStartTime);
                        selectedStartTime = selectedStartTime.plusMinutes(15);

                        endTime.setItems(timesList);

                    }
                } else {
                    while (openingTimeLocal.isBefore(closingTimeLocal) || openingTimeLocal.equals(closingTimeLocal)) {

                        timesList.add(openingTimeLocal);
                        openingTimeLocal = openingTimeLocal.plusMinutes(15);

                        endTime.setItems(timesList);

                    }
                }
            } else {
                //Invalid Date Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("selectStartDate"));
                alert.showAndWait();
            }
        } catch (IndexOutOfBoundsException ignore) {

        }
    }

    /** Retrieves selected customer and contact from appropriate combo boxes. Converts selected date/time from user's time zone
     * to UTC time zone. Ensures proposed appointment times do not overlap with appointment times already in SQL database.
     * Ensures fields have valid values. Saves modified appointment to SQL database. Displays successful message on Appointment Dashboard.
     * @param actionEvent - on click of save button
     */
    public void onClickSave(ActionEvent actionEvent) {

        try {
            //Get customer and contact from combo boxes
            Customer selectedCustomer = custIDCB.getSelectionModel().getSelectedItem();
            Contact selectedContact = contactCB.getSelectionModel().getSelectedItem();

            //Get selected start/end date/time from fields
            LocalDateTime localSelectedStartDateTime = LocalDateTime.of(startDate.getValue(), startTime.getSelectionModel().getSelectedItem());
            LocalDateTime localSelectedEndDateTime = LocalDateTime.of(endDate.getValue(), endTime.getSelectionModel().getSelectedItem());

            //Convert Date/Time to UTC
            LocalDateTime convertedSDT = ZoneChange.getDesiredDateTime(localSelectedStartDateTime, userZone, utcZone);
            LocalDateTime convertedEDT = ZoneChange.getDesiredDateTime(localSelectedEndDateTime, userZone, utcZone);

            //Get username
            int retrievedUserID = UsersDAO.getUserID(username);

            //Import other fields
            int appID = Integer.parseInt(apptID.getText());
            String appTitle = apptTitle.getText();
            String appDesc = description.getText();
            String appLoc = apptLocation.getText();
            String appType = apptType.getText();
            Timestamp appStart = Timestamp.valueOf(convertedSDT);
            Timestamp appEnd = Timestamp.valueOf(convertedEDT);
            Timestamp lastUpdate = new Timestamp(System.currentTimeMillis());
            String updateBy = username;
            int cusID = selectedCustomer.getCustID();
            int contID = selectedContact.getContactID();

            //Check to ensure proposed times do not overlap with existing appointments
            Boolean okToProceed = AppointmentsDAO.checkModifiedDateTimes(localSelectedStartDateTime, localSelectedEndDateTime, cusID, appID);
            Boolean checkDT = AppointmentsDAO.checkDateTime(localSelectedStartDateTime, localSelectedEndDateTime);
            boolean emptyFields = false;

            //Validate fields and add appointment
            if (appTitle.isEmpty() || appDesc.isEmpty() || appLoc.isEmpty() || appType.isEmpty() ||appStart.toString() == null ||appEnd.toString() == null ||
                    cusID == 0 ||contID == 0) {
                emptyFields = true;
            }
            if (emptyFields) {
                //Empty field Alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("errorDetected"));
                alert.setContentText(rBundle.getString("validValues"));
                alert.showAndWait();
            } else {
                if (checkDT) {
                    if (okToProceed) {
                        //Creates appointment object
                        Appointment modAppt = new Appointment(appID, appTitle, appDesc, appLoc, appType, appStart, appEnd, lastUpdate, updateBy, cusID, retrievedUserID, contID);

                        //Adds new appointment object to SQL
                        AppointmentsDAO.modifyAppointment(modAppt);

                        //Constructs appointment modified message
                        apptModifiedMessage = (rBundle.getString("apptID")) + ": " + modAppt.getApptID() + "\n" + (rBundle.getString("title")) + ": " +
                                modAppt.getApptTitle() + "\n" + (rBundle.getString("type")) + ": " + modAppt.getApptType() + "\n" + (rBundle.getString("succMod"));

                        //Send user message to AppointmentDashboard Controller
                        AppointmentsDashboard.setUserMessage(apptModifiedMessage);

                        //Return to Customer Dashboard
                        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        stage.close();
                    } else {
                        //Time conflict alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(rBundle.getString("errorDetected"));
                        alert.setContentText(rBundle.getString("timeConflict"));
                        alert.showAndWait();
                    }
                } else {
                    //Date/Time conflict alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(rBundle.getString("errorDetected"));
                    alert.setContentText(rBundle.getString("catchBoth"));
                    alert.showAndWait();

                }
            }
        } catch (DateTimeException | SQLException e) {
            //Empty field Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rBundle.getString("errorDetected"));
            alert.setContentText(rBundle.getString("validValues"));
            alert.showAndWait();
        }
    }

    /** Cancels addition of new appointment and returns user to Appointment Dashboard without adding appointment to SQL database.
     * @param actionEvent - on click of cancel button
     */
    public void onClickCancel(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("returnAppointment"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) (((Button) actionEvent.getSource()).getScene().getWindow());
            stage.close();
        }
    }
}
