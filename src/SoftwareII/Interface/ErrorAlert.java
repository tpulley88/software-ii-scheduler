package SoftwareII.Interface;

import javafx.scene.control.Alert;


/**Creates interface, allows lambda expression that alerts user to an error with a custom error message.
 */
public interface ErrorAlert {

    /**Provides user an error alert with parameters allowing user to set title and content of error message.
     * @param title - Error message title
     * @param contentText - Error message content
     * @return - returns Alert
     */
    Alert errorAlert(String title, String contentText);
}
