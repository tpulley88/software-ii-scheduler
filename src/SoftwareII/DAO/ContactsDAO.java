package SoftwareII.DAO;

import SoftwareII.Model.Appointment;
import SoftwareII.Model.Contact;
import SoftwareII.Utility.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**ContactsDAO retrieves and manipulates contact information from SQL database.
 */
public class ContactsDAO {


    /**Creates a list of all customers from SQL database
     * @return - returns list of customers
     * @throws SQLException - Exception
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {

        //Constructs empty contacts list
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM contacts";

        JDBC.setPreparedStatement(sqlStatement, JDBC.getConnection());

        PreparedStatement contListStatement = JDBC.getPreparedStatement();

        ResultSet rs = contListStatement.executeQuery();

        if (rs != null) {
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                Contact addContact= new Contact(contactID, contactName, contactEmail);

                contactList.add(addContact);
            }
        }
        return contactList;
    }

    /**Returns Contact object when provided contact ID
     * @param contID - contact ID
     * @return - returns customer
     * @throws SQLException - Exception
     */
    public static Contact uploadContact (int contID) throws SQLException {

        Contact findCont = null;

        ObservableList<Contact> allCont = getAllContacts();

        for (Contact cont : allCont) {
            if (cont.getContactID() == contID) {
                findCont = cont;
            }
        }
        return findCont;
    }

    public static ObservableList<Appointment> getApptList(Contact contact) throws SQLException {

        ObservableList<Appointment> contactAppt = FXCollections.observableArrayList();

        ObservableList<Appointment> getAllAppt = AppointmentsDAO.getAllApptsDTString();

        int contID = contact.getContactID();

        for (Appointment appt : getAllAppt) {
            if (appt.getContactID() == contID) {
                contactAppt.add(appt);
            }
        }

        return contactAppt;
    }
}
