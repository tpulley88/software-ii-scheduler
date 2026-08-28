package SoftwareII.Model;

import java.util.Objects;

/**Abstract appointment object that provides super for Appointment class
 */
public abstract class AbstractAppointment {

    //Declare variables
    private String apptTitle;
    private String apptDesc;
    private String apptLoc;
    private String apptType;
    private int custID;
    private int userID;
    private int contactID;

    /**Defines abstract appointment constructor
     * @param apptTitle - Appointment title
     * @param apptDesc - Appointment description
     * @param apptLoc - Appointment location
     * @param apptType - Appointment type
     * @param custID - Customer ID number
     * @param userID - User ID number
     * @param contactID - Contact ID number
     */
    public AbstractAppointment(String apptTitle, String apptDesc, String apptLoc, String apptType, int custID, int userID, int contactID) {
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLoc = apptLoc;
        this.apptType = apptType;
        this.custID = custID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**Gets appointment title
     * @return - Returns appointment title
     */
    public String getApptTitle() {
        return apptTitle;
    }

    /**Sets appointment title
     * @param apptTitle - Appointment title
     */
    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    /**Gets appointment description
     * @return - Returns appointment description
     */
    public String getApptDesc() {
        return apptDesc;
    }

    /**Gets appointment location
     * @return - Returns appointment location
     */
    public String getApptLoc() {
        return apptLoc;
    }

    /**Gets appointment type
     * @return - Returns appointment type
     */
    public String getApptType() {
        return apptType;
    }

    /**Gets customer ID
     * @return - Returns customer ID
     */
    public int getCustID() {
        return custID;
    }

    /**Sets customer ID
     * @param custID - Customer ID
     */
    public void setCustID(int custID) {
        this.custID = custID;
    }

    /**Gets user ID
     * @return - Returns user ID
     */
    public int getUserID() {
        return userID;
    }

    /**Sets user ID
     * @param userID - User ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**Gets contact ID
     * @return - Returns contact ID
     */
    public int getContactID() {
        return contactID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAppointment that = (AbstractAppointment) o;
        return custID == that.custID && userID == that.userID && contactID == that.contactID && apptTitle.equals(that.apptTitle) && apptDesc.equals(that.apptDesc) && apptLoc.equals(that.apptLoc) && apptType.equals(that.apptType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apptTitle, apptDesc, apptLoc, apptType, custID, userID, contactID);
    }
}

