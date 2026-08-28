package SoftwareII.Model;

/**First_Level_Division class that builds Division object
 */
public class First_Level_Divisions {

    //Declare variables
    private int divisionID;
    private String divisionName;
    private int countryID;

    /**Builds Division Constructur
     * @param divisionID - Division ID
     * @param divisionName - Division Name
     * @param countryID - Country ID
     */
    public First_Level_Divisions(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**Gets Division ID
     * @return - Returns Division ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**Gets Division Name
     * @return - Returns Division Name
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**Gets Country ID
     * @return - Returns Country ID
     */
    public int getCountryID() {
        return countryID;
    }
}
