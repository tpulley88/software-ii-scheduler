package SoftwareII.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**User can choose to Manage Customers, Manage Appointments, return to Login, or exit application.
 */
public class SchedulingDashboard {

    //Set stage and scene
    Stage stage;
    Parent scene;

    //Declare variables
    public Button appointmentButton;
    public Button returnLogin;
    public Button mainExit;
    public Button customerButton;

    private static String username;
    private static ZoneId userZone;

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());


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

    /**Method that gets user zone from Login page
     * @param user - user
     */
    public static void setUserZone(ZoneId user) {
        userZone = user;
    }

    /**Allows user to access Customer Dashboard.
      * @param actionEvent - on click Manage Customers
     * @throws IOException - Exception
     */
    public void onCustDashClick(ActionEvent actionEvent) throws IOException {

        //Get username
        String user = getUsername();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SoftwareII/View/CustomerDashboard.fxml"));
        loader.setResources(rBundle);
        scene = loader.load();

        //Send username to CustomerDashboard Controller
        CustomerDashboard.setUsername(user);

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**Allows user to access Appointment Dashboard.
     * @param actionEvent - on click Manage Appointments
     * @throws IOException - Exception
     */
    public void onApptDashClick(ActionEvent actionEvent) throws IOException {

        //Get username
        String user = getUsername();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SoftwareII/View/AppointmentsDashboard.fxml"));
        loader.setResources(rBundle);
        scene = loader.load();

        //Send username to CustomerDashboard Controller
        AppointmentsDashboard.setUsername(user);
        AppointmentsDashboard.setUserZone(userZone);



        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    /**Allows user to return to Login.
     * @param actionEvent - on click Login
     * @throws IOException - Exception
     */
    public void onClickReturnLogin(ActionEvent actionEvent) throws IOException {

        stage = (Stage) (((Button) actionEvent.getSource()).getScene().getWindow());
        scene = FXMLLoader.load(getClass().getResource("/SoftwareII/View/Login.fxml"), rBundle);
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();

    }

    /**Allows user to exit application.
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
