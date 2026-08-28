package SoftwareII.Controller;

import SoftwareII.DAO.ContactsDAO;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Contact;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

/**User can choose a contact by ID/name to view a list detailing the current appointments associated with that contact.
 * User can then return to Appointment Dashboard.
 */
public class ContactScheduleReport {

    //Set stage
    Stage stage;

    //Declare variables
    public ComboBox<Contact> contactCB;
    public TableColumn apptID;
    public TableColumn apptTitle;
    public TableColumn apptType;
    public TableColumn apptDesc;
    public TableColumn startDT;
    public TableColumn endDT;
    public TableColumn custID;
    public TableView<Appointment> contactTV;

    /**Initializes report. Loads contact combo box.
     * @throws SQLException - Exception
     */
    //Loads and populates combo box
    public void initialize() throws SQLException {

        //Set values for combo boxes
        contactCB.setItems(ContactsDAO.getAllContacts());

        apptID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        apptType.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        apptDesc.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        startDT.setCellValueFactory(new PropertyValueFactory<>("apptStartDTS"));
        endDT.setCellValueFactory(new PropertyValueFactory<>("apptEndDTS"));
        custID.setCellValueFactory(new PropertyValueFactory<>("custID"));

    }

    /**Populates table from chosen contact
     * @param actionEvent - on click Contact
     * @throws SQLException - Exception
     */
    public void onClickContactCB(ActionEvent actionEvent) throws SQLException {

        Contact contact = contactCB.getValue();

        if (contact != null) {

            contactTV.setItems(ContactsDAO.getApptList(contact));
        }
    }

    /**Allows user to return to Appointment Dashboard
     * @param actionEvent - on click Return
     */
    public void onClickApptDash(ActionEvent actionEvent) {

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
