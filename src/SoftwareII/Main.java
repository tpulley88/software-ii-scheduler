package SoftwareII;

import SoftwareII.Utility.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**Main class that launches application and establishes database connection
 */
public class Main extends Application {

    //Resource bundle location
    ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

    /**Override that provides starting stage and scene for application
     * @param primaryStage - Primary Stage
     * @throws Exception - Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/LogIn.fxml"), rBundle);
        primaryStage.setTitle(rBundle.getString("GCOTitle"));
        primaryStage.setScene(new Scene(root, 350, 350));
        primaryStage.show();
    }


    /**Launches application and establishes database connection
     * @param args - Arguments
     */
    public static void main(String[] args) {

        JDBC.makeConnection();

        launch(args);

        JDBC.closeConnection();
    }
}
