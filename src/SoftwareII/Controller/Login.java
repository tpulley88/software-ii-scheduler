package SoftwareII.Controller;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.DAO.UsersDAO;
import SoftwareII.Model.User;
import SoftwareII.Utility.ZoneChange;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**Login form allows username to provide a username/password combination that is checked against the SQL database. If it matches,
 * application allows user to access application.
 */
public class Login {

    //Set stage
    Stage stage;

    //Declare variables
    public Button cancelButton;
    public Label getUserLocation;
    public Button loginButton;
    public TextField userNameField;
    public PasswordField passwordField;

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

    //Declares user's Zone ID
    ZoneId userZone = ZoneId.systemDefault();

    /**Initializes Login page and sets label to user's zone ID
     */
    public void initialize() {
        getUserLocation.setText(userZone.getId());
    }

    /**Allows user to attempt to Login. If username/password combination matches SQL database, user is allowed access to application.
     * All login attempts are recorded to login_activity.txt.
     * @param actionEvent - on click login
     */
    public void loginClick(ActionEvent actionEvent) {

        try {

            //Construct list of users
            ObservableList<User> allUsers = UsersDAO.getAllUsers();

            //Get user input for username/password
            String userNameAttempt = userNameField.getText();
            String passwordAttempt = passwordField.getText();

            //Initializes Boolean for correct username/password combination
            boolean resultsFound = false;

            for (User user : allUsers) {
                //If match found, load dashboard
                if ((user.getUserName().matches(userNameAttempt)) && (user.getUserPassword().matches(passwordAttempt))) {

                    //Set Boolean to true if results found
                    resultsFound = true;

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/SoftwareII/View/SchedulingDashboard.fxml"));
                    loader.setResources(rBundle);
                    loader.load();

                    //Send username to Scheduling Dashboard Controller
                    SchedulingDashboard.setUsername(userNameAttempt);
                    SchedulingDashboard.setUserZone(userZone);

                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Parent scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.centerOnScreen();
                    stage.show();

                    //Checks for upcoming appointments
                    AppointmentsDAO.checkUpcomingAppt(LocalDateTime.now());

                    //Method that passes username, UTC date/time, UTC timestamp, and result of login attempt to login_activity.txt
                    String fileName = "login_activity.txt", toWrite;

                    LocalDateTime getUTCDT = ZoneChange.getDesiredDateTime(LocalDateTime.now(), userZone, ZoneId.of("UTC"));

                    FileWriter file = new FileWriter(fileName, true);

                    PrintWriter printLogin = new PrintWriter(file);

                    toWrite = "Attempted username: " + userNameAttempt + " | UTC DateTime: " + getUTCDT + " | UTC Timestamp: " + Timestamp.valueOf(getUTCDT)
                            + " |  SUCCESSFUL LOGIN";

                    printLogin.println(toWrite);

                    printLogin.close();
                }

            }
            //If no username/password match found and Boolean is false, display error alert
            if (!resultsFound) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rBundle.getString("UnabletoLogin"));
                alert.setContentText(rBundle.getString("UPNotFound"));
                alert.show();

                //Method that passes username, UTC date/time, UTC timestamp, and result of login attempt to login_activity.txt
                String fileName = "login_activity.txt", toWrite;

                LocalDateTime getUTCDT = ZoneChange.getDesiredDateTime(LocalDateTime.now(), userZone, ZoneId.of("UTC"));

                FileWriter file = new FileWriter(fileName, true);

                PrintWriter printLogin = new PrintWriter(file);

                toWrite = "Attempted username: " + userNameAttempt + " | UTC DateTime: " + getUTCDT + " | UTC Timestamp: " + Timestamp.valueOf(getUTCDT)
                        + " |  FAILED LOGIN";

                printLogin.println(toWrite);

                printLogin.close();
            }

        } catch (Exception ignored) {
        }
    }

    /**If user cancels Login, application will close.
     * @param actionEvent - on click cancel
     */
    public void cancelClick(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rBundle.getString("exitprompt"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
