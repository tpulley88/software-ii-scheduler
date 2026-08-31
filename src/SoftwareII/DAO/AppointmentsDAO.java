package SoftwareII.DAO;

import SoftwareII.Model.*;
import SoftwareII.Utility.JDBC;
import SoftwareII.Utility.ZoneChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**AppointmentsDAO retrieves and manipulates appointment information from SQL database.
 */
public class AppointmentsDAO {


    /**Creates a list of all appointments from SQL database
     * @return - returns list of all appointments with separate start date, start time, end date, and end time.
     * @throws SQLException - Exception
     */
    public static ObservableList<Appointment> getAllAppointments () throws SQLException {

        ObservableList<Appointment> appointmentsObservableList = FXCollections.observableArrayList();

        //Prepares SQL statement
        String sqlStatement = "SELECT * FROM appointments";

        JDBC.setPreparedStatement(sqlStatement, JDBC.getConnection());

        PreparedStatement apptListStatement = JDBC.getPreparedStatement();

        //Executes SQL statement and returns its results
        ResultSet rs = apptListStatement.executeQuery();

        //Parses data from SQL results
        if (rs != null) {
            while (rs.next()) {
                int apptID = rs.getInt("Appointment_ID");
                String apptTitle = rs.getString("Title");
                String apptDesc = rs.getString("Description");
                String apptLoc = rs.getString("Location");
                String apptType = rs.getString("Type");

                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));

                Timestamp apptStart = rs.getTimestamp(("Start"), cal);
                Timestamp apptEnd = rs.getTimestamp(("End"), cal);
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");


                LocalDate apptStartDate = apptStart.toLocalDateTime().toLocalDate();
                LocalTime apptStartTime = apptStart.toLocalDateTime().toLocalTime();
                LocalDate apptEndDate = apptEnd.toLocalDateTime().toLocalDate();
                LocalTime apptEndTime = apptEnd.toLocalDateTime().toLocalTime();

                //Creates new appointment object to add to appointment list
                Appointment addAppt = new Appointment(apptID, apptTitle, apptDesc, apptLoc, apptType, apptStartDate, apptStartTime, apptEndDate, apptEndTime, custID, userID, contactID);

                //Adds new appointment object to appointment list
                appointmentsObservableList.add(addAppt);
            }
        }
        //returns list of all appointments
        return appointmentsObservableList;
    }

    /**Creates list of all appointments with LocalDateTime
     * @return - returns list of all appointments with Start Date/Time and End Date/Time.
     * @throws SQLException - Exception
     */
    public static ObservableList<Appointment> getAllAppts () throws SQLException {

        ObservableList<Appointment> getAll = FXCollections.observableArrayList();

        ObservableList<Appointment> getAllApptList = getAllAppointments();

        for (Appointment appt : getAllApptList) {

            int apptID = appt.getApptID();
            String apptTitle = appt.getApptTitle();
            String apptDesc = appt.getApptDesc();
            String apptLoc = appt.getApptLoc();
            String apptType = appt.getApptType();
            LocalDateTime apptStart = LocalDateTime.of(appt.getApptStartDate(), appt.getApptStartTime());
            LocalDateTime apptEnd = LocalDateTime.of(appt.getApptEndDate(), appt.getApptEndTime());
            int custID = appt.getCustID();
            int userID = appt.getUserID();
            int contactID = appt.getContactID();

            //Creates new appointment object to add to appointment list
            Appointment addAppt = new Appointment(apptID, apptTitle, apptDesc, apptLoc, apptType, apptStart, apptEnd, custID, userID, contactID);

            //Adds new appointment object to appointment list
            getAll.add(addAppt);
        }
        //returns list of all appointments
        return getAll;
    }

    /**Creates list of all appointments for Appointment Tableviews
     * @return - returns list of all appointments with Start Date/Time and End Date/Time formatted for easy vieweing.
     * @throws SQLException - Exception
     */
    public static ObservableList<Appointment> getAllApptsDTString () throws SQLException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        ObservableList<Appointment> getAll = FXCollections.observableArrayList();

        ObservableList<Appointment> getAllApptList = getAllAppointments();

        for (Appointment appt : getAllApptList) {

            int apptID = appt.getApptID();
            String apptTitle = appt.getApptTitle();
            String apptDesc = appt.getApptDesc();
            String apptLoc = appt.getApptLoc();
            String apptType = appt.getApptType();
            String apptStart = dtf.format(LocalDateTime.of(appt.getApptStartDate(), appt.getApptStartTime()));
            String apptEnd = dtf.format(LocalDateTime.of(appt.getApptEndDate(), appt.getApptEndTime()));
            int custID = appt.getCustID();
            int userID = appt.getUserID();
            int contactID = appt.getContactID();

            //Creates new appointment object to add to appointment list
            Appointment addAppt = new Appointment(apptID, apptTitle, apptDesc, apptLoc, apptType, apptStart, apptEnd, custID, userID, contactID);

            //Adds new appointment object to appointment list
            getAll.add(addAppt);
        }
        //returns list of all appointments
        return getAll;
    }

    /**Adds new appointment to SQL database.
     * @param newAppt - object passed to SQL database
     * @throws SQLException - Exception
     * @return
     */
    public static boolean addAppointment(Appointment newAppt) throws SQLException {

        try {
            //Set statement
            String insertStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            JDBC.setPreparedStatement(insertStatement, JDBC.getConnection());

            PreparedStatement statement = JDBC.getPreparedStatement();

            //Import appointment data
            String title = newAppt.getApptTitle();
            String description = newAppt.getApptDesc();
            String location = newAppt.getApptLoc();
            String type = newAppt.getApptType();
            Timestamp start = newAppt.getApptStart();
            Timestamp end = newAppt.getApptEnd();
            LocalDateTime createDate = newAppt.getApptCreateDate();
            String createBy = newAppt.getApptCreateBy();
            Timestamp lastUpdate = newAppt.getApptLastUpdate();
            String updateBy = newAppt.getApptLastUpdateBy();
            int custID = newAppt.getCustID();
            int userID = newAppt.getUserID();
            int contactID = newAppt.getContactID();

            //Mapping of value keys
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setString(5, start.toString());
            statement.setString(6, end.toString());
            statement.setString(7, createDate.toString());
            statement.setString(8, createBy);
            statement.setString(9, lastUpdate.toString());
            statement.setString(10, updateBy);
            statement.setInt(11, custID);
            statement.setInt(12, userID);
            statement.setInt(13, contactID);

            //Execute statement
            statement.execute();

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**Deletes appointment from SQL database
     * @param currentAppointment - object passed to SQL database
     * @throws SQLException - Exception
     */
    public static void deleteAppointment(Appointment currentAppointment) throws SQLException {

        //Set statement
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";

        JDBC.setPreparedStatement(deleteStatement, JDBC.getConnection());

        PreparedStatement statement = JDBC.getPreparedStatement();

        //Import customer data
        int appID = currentAppointment.getApptID();

        //Mapping of value keys
        statement.setInt(1,appID);

        //Execute statement
        statement.execute();
    }

    /**Modifies appointment in SQL database.
     * @param modAppt - object passed to SQL database
     * @throws SQLException - Exception
     */
    public static boolean modifyAppointment(Appointment modAppt) throws SQLException {

        //Set statement
        String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        JDBC.setPreparedStatement(updateStatement, JDBC.getConnection());

        PreparedStatement statement = JDBC.getPreparedStatement();

        //Import customer data
        String title = modAppt.getApptTitle();
        String description = modAppt.getApptDesc();
        String location = modAppt.getApptLoc();
        String type = modAppt.getApptType();
        Timestamp start = modAppt.getApptStart();
        Timestamp end = modAppt.getApptEnd();
        Timestamp lastUpdate = modAppt.getApptLastUpdate();
        String updateBy = modAppt.getApptLastUpdateBy();
        int custID = modAppt.getCustID();
        int userID = modAppt.getUserID();
        int contactID = modAppt.getContactID();
        int apptID = modAppt.getApptID();

        //Mapping of value keys
        statement.setString(1, title);
        statement.setString(2, description);
        statement.setString(3, location);
        statement.setString(4, type);
        statement.setString(5, start.toString());
        statement.setString(6, end.toString());
        statement.setString(7, lastUpdate.toString());
        statement.setString(8, updateBy);
        statement.setInt(9, custID);
        statement.setInt(10, userID);
        statement.setInt(11, contactID);
        statement.setInt(12, apptID);


        //Execute statement
        statement.execute();

        return true;
    }

    /**Checks to see if customer has associated appointments
     * @param custID - searched for in appointments list
     * @return - returns true if customer has appointments, false if customer has no associated appointments
     * @throws SQLException - Exception
     */
    public static Boolean custAppointment(int custID) throws SQLException {

        ObservableList<Appointment> aptList = AppointmentsDAO.getAllAppointments();

        boolean positiveAppt = false;

        for (Appointment apt : aptList) {
            if (apt.getCustID() == custID) {
                positiveAppt = true;
            }
        }
        return positiveAppt;
    }

    /** Deletes appointments associated with customer by customer ID number
     * @param custID - customer ID number to search for
     * @throws SQLException - Exception
     */
    public static void deleteCustAppt(int custID) throws SQLException {

        ObservableList<Appointment> aptList = AppointmentsDAO.getAllAppointments();

        for (Appointment apt : aptList) {
            if (apt.getCustID() == custID) {
                deleteAppointment(apt);
            }
        }
    }

    /**Filters list of appointments by appointments in current month
     * @return - list of current month's appointments
     * @throws SQLException - Exception
     */
    public static ObservableList getMonthAppointment() throws SQLException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        ObservableList<Appointment> apptMonthlyList = FXCollections.observableArrayList();

        ObservableList<Appointment> allAppt = getAllApptsDTString();

        LocalDate todayDate = ZoneChange.getDesiredDateTime(LocalDateTime.now(), ZoneId.of("UTC"), ZoneId.systemDefault()).toLocalDate();


        for (Appointment appt : allAppt) {

            LocalDateTime parseDT = LocalDateTime.parse(appt.getApptStartDTS(), dtf);
            LocalDate parseDate = parseDT.toLocalDate();

            if (parseDate.getMonth() == todayDate.getMonth()) {
                apptMonthlyList.add(appt);
            }
        }
        return apptMonthlyList;
    }

    /**Filters list of appointments by appointments in current week
     * @return - list of current week's appointments
     * @throws SQLException - Exception
     */
    public static ObservableList<Appointment> getWeekAppointment() throws SQLException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        ObservableList<Appointment> apptWeeklyList = FXCollections.observableArrayList();

        ObservableList<Appointment> allAppt = AppointmentsDAO.getAllApptsDTString();

        DayOfWeek todayDay = ZoneChange.getDesiredDateTime(LocalDateTime.now(), ZoneId.of("UTC"), ZoneId.systemDefault()).toLocalDate().getDayOfWeek();
        LocalDate todayDate = ZoneChange.getDesiredDateTime(LocalDateTime.now(), ZoneId.of("UTC"), ZoneId.systemDefault()).toLocalDate();

        for (Appointment appt : allAppt) {

            LocalDate beginWeek;
            LocalDate endWeek;

            LocalDateTime parseDT = LocalDateTime.parse(appt.getApptStartDTS(), dtf);
            LocalDate parseDate = parseDT.toLocalDate();

            if (todayDay == DayOfWeek.SUNDAY) {
                beginWeek = todayDate.minusDays(1);
                endWeek = todayDate.plusDays(7);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            } else if (todayDay == DayOfWeek.MONDAY) {
                beginWeek = todayDate.minusDays(2);
                endWeek = todayDate.plusDays(6);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            } else if (todayDay == DayOfWeek.TUESDAY) {
                beginWeek = todayDate.minusDays(3);
                endWeek = todayDate.plusDays(5);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            } else if (todayDay == DayOfWeek.WEDNESDAY) {
                beginWeek = todayDate.minusDays(4);
                endWeek = todayDate.plusDays(4);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            } else if (todayDay == DayOfWeek.THURSDAY) {
                beginWeek = todayDate.minusDays(5);
                endWeek = todayDate.plusDays(3);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            } else if (todayDay == DayOfWeek.FRIDAY) {
                beginWeek = todayDate.minusDays(6);
                endWeek = todayDate.plusDays(2);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            } else if (todayDay == DayOfWeek.SATURDAY) {
                beginWeek = todayDate.minusDays(7);
                endWeek = todayDate.plusDays(1);

                if (parseDate.isAfter(beginWeek) && parseDate.isBefore(endWeek)) {
                    apptWeeklyList.add(appt);
                }
            }
        }
        return apptWeeklyList;
    }

    /**Filters list of appointments by appointments in current day
     * @return - list of current day's appointments
     * @throws SQLException - Exception
     */
    public static ObservableList<Appointment> getDaysAppointment() throws SQLException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        ObservableList<Appointment> apptDayList = FXCollections.observableArrayList();

        ObservableList<Appointment> allAppt = getAllApptsDTString();

        LocalDate todayDate = ZoneChange.getDesiredDateTime(LocalDateTime.now(), ZoneId.of("UTC"), ZoneId.systemDefault()).toLocalDate();

        for (Appointment appt : allAppt) {

            LocalDateTime parseDT = LocalDateTime.parse(appt.getApptStartDTS(), dtf);
            LocalDate parseDate = parseDT.toLocalDate();

             if (parseDate.isEqual(todayDate)) {
                apptDayList.add(appt);
            }
        }
        return apptDayList;
    }

    /**Get list of appointments associated with customer ID
     * @param custID - customer ID to search for
     * @return - returns list of appointments associated with customer
     * @throws SQLException - Exception
     */
    public static ObservableList<Appointment> getCustAppt(int custID) throws SQLException {

        ObservableList<Appointment> custApptList = FXCollections.observableArrayList();

        ObservableList<Appointment> allAppt = getAllAppointments();

        for (Appointment appt : allAppt) {
            if (appt.getCustID() == custID) {
                custApptList.add(appt);
            }
        }
        return custApptList;
    }

    /**Checks Add Appointment proposed appointment time against current appointments in SQL database, ensuring that there are no appointment
     * times that overlap.
     * @param localStartDT - local start date
     * @param localEndDT - local end date
     * @param custID - customer ID
     * @return - returns true if appointment times don't overlap and false if appointment times do overlap
     * @throws SQLException - Exception
     */
    public static Boolean checkProposedDateTimes(LocalDateTime localStartDT, LocalDateTime localEndDT, int custID) throws SQLException {

        ObservableList<Appointment> allAppt = getAllAppts();

        for (Appointment appt : allAppt) {

            LocalDateTime apptSDT = appt.getApptStartDT();
            LocalDateTime apptEDT = appt.getApptEndDT();


            if (appt.getCustID() == custID) {
                if (((localStartDT.isAfter(apptSDT)) || localStartDT.isEqual(apptSDT)) && localStartDT.isBefore(apptEDT)){
                    return false;
                }
                if ((localEndDT.isAfter(apptSDT)) && (localEndDT.isBefore(apptEDT) || localEndDT.isEqual(apptEDT))){
                    return false;
                }
                if (((localStartDT.isBefore(apptSDT)) || localStartDT.isEqual(apptSDT)) && (localEndDT.isAfter(apptEDT) || localEndDT.isEqual(apptEDT))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**Checks Modified appointment time against current appointments in SQL database, ensuring that there are no appointment
     * times that overlap. Excludes current appointment by utilizing appointment ID.
     * @param localStartDT - local start date
     * @param localEndDT - local end date
     * @param custID - customer ID
     * @param apptID  - appointment ID
     * @return - returns true if appointment times don't overlap and false if appointment times do overlap
     * @throws SQLException - Exception
     */
    public static Boolean checkModifiedDateTimes(LocalDateTime localStartDT, LocalDateTime localEndDT, int custID, int apptID) throws SQLException {

        ObservableList<Appointment> allAppt = getAllAppts();

        for (Appointment appt : allAppt) {

            LocalDateTime apptSDT = appt.getApptStartDT();
            LocalDateTime apptEDT = appt.getApptEndDT();


            if (appt.getCustID() == custID) {
                if (appt.getApptID() != apptID) {
                    if (((localStartDT.isAfter(apptSDT)) || localStartDT.isEqual(apptSDT)) && localStartDT.isBefore(apptEDT)) {
                        return false;
                    }
                    if ((localEndDT.isAfter(apptSDT)) && (localEndDT.isBefore(apptEDT) || localEndDT.isEqual(apptEDT))) {
                        return false;
                    }
                    if (((localStartDT.isBefore(apptSDT)) || localStartDT.isEqual(apptSDT)) && (localEndDT.isAfter(apptEDT) || localEndDT.isEqual(apptEDT))) {
                        return false;
                    }
                }
            }
        } return true;
    }

    public static Boolean checkDateTime(LocalDateTime localStartDT, LocalDateTime localEndDT) throws SQLException {

        return !localStartDT.isBefore(LocalDateTime.now()) && (!localEndDT.isBefore(localStartDT));
    }


    /**Returns list of appointment types
     * @return - list of appointment types
     * @throws SQLException - Exception
     */
    public static ObservableList<String> getApptType() throws SQLException {

        ObservableList<Appointment> allAppt = getAllAppointments();

        ObservableList<String> typeList = FXCollections.observableArrayList();

        for (Appointment appt : allAppt) {

            typeList.add(appt.getApptType());
        }
        return typeList;
    }

    /**Checks to see if there is an upcoming appointment within 15 minutes
     * @param now - given time
     * @throws SQLException - Exception
     */
    public static void checkUpcomingAppt(LocalDateTime now) throws SQLException {

        ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

        ObservableList<Appointment> allAppt = getAllAppts();

        boolean upcomingAppt = false;

        for (Appointment appt : allAppt) {
            String custName = null;
            Appointment upcoming = null;

            if (appt.getApptStartDT().isAfter(LocalDateTime.now()) && appt.getApptStartDT().isBefore(LocalDateTime.now().plusMinutes(15))) {
                upcomingAppt = true;
                custName = CustomersDAO.uploadCust(appt.getCustID()).getCustName();
                upcoming = appt;
            }

            if (upcomingAppt) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(rBundle.getString("upcomingAppt"));
                alert.setContentText((rBundle.getString("upcomingApptText")) + " " + custName + "\n" +
                        (rBundle.getString("apptID")) + ": " + upcoming.getApptID() + "\n" + (rBundle.getString("startDate")) + ": " +
                        upcoming.getApptStartDT().toLocalDate() + "\n" + (rBundle.getString("startTime")) + ": " + upcoming.getApptStartDT().toLocalTime());
                alert.showAndWait();
            }
        }
        if (!upcomingAppt){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rBundle.getString("upcomingAppt"));
            alert.setContentText(rBundle.getString("noAppt"));
            alert.showAndWait();
        }
    }
}
