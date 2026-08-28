package SoftwareII.Model;

/**Contact class that builds Contact object
 */
public class Contact {

    //Declare variables
    private int contactID;
    private String contactName;
    private String contactEmail;

    /**Builds Contact constructor
     * @param contactID - Contact ID
     * @param contactName - Contact Name
     * @param contactEmail - Contact Email
     */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**Gets Contact ID
     * @return - Returns Contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**Sets override for displaying Contact information
     * @return - Returns Contact display string
     */
    @Override
    public String toString(){
        return("#" + contactID + " " + contactName);

    }
}
