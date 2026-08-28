package SoftwareII.Model;

/**User class that builds User object
 */
public class User {
    private int userID;
    private String userName;
    private String userPassword;


    /**Builds User constructor
     * @param userID - User ID
     * @param userName - User Name
     * @param userPassword - User Password
     */
    public User(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**Gets User ID
     * @return - Returns User ID
     */
    public int getUserID() {
        return userID;
    }

    /**Sets User ID
     * @param userID - User ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**Gets username
     * @return - Returns username
     */
    public String getUserName() {
        return userName;
    }

    /**Sets username
     * @param userName - Username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**Gets password
     * @return - Returns password
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**Sets override for displaying User information
     * @return - Returns User display string
     */
    @Override
    public String toString(){
        return("#" + userID + " " + userName);

    }
}
