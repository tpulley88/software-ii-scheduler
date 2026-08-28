package SoftwareII.Model;

/**Country class that builds Country object
 */
public class Country {

    //Declare variables
    private int countryID;
    private String countryName;

    /**Builds Country constructor
     * @param countryID - Country ID
     * @param countryName - Country Name
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**Gets Country ID
     * @return - Returns Country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**Gets Country Name
     * @return - Returns Country Name
     */
    public String getCountryName() {
        return countryName;
    }

    /**Sets override for displaying Country information
     * @return - Returns Country display string
     */
    @Override
    public String toString(){
        return("#" + countryID + " " + getCountryName());
    }
}
