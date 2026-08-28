package SoftwareII.Interface;

import javafx.event.ActionEvent;
import java.io.IOException;

/**Creates interface, allows lambda expression that allows user to load new stage, scene from an action event.
 */
public interface NewLoaderStageScene {

    /**Provides user with new controller from parameters provided
     * @param rName - Resource location
     * @param rString - Resource title
     * @param aV - Action event
     * @throws IOException - Exception
     */
    void load(String rName, String rString, ActionEvent aV) throws IOException;

}
