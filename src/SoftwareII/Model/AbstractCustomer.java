package SoftwareII.Model;

/**Abstract customer object that provides super for Customer class
 */
public abstract class AbstractCustomer {

    //Declare variables
    private String custName;
    private String custAddress;
    private String custPostalCode;
    private String custPhone;

    /**Defines abstract customer constructor
     * @param custName - Customer name
     * @param custAddress - Customer address
     * @param custPostalCode - Customer postal code
     * @param custPhone - Customer phone number
     */
    public AbstractCustomer(String custName, String custAddress, String custPostalCode, String custPhone) {
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPostalCode = custPostalCode;
        this.custPhone = custPhone;
    }

    /**Gets customer name
     * @return - Returns customer name
     */
    public String getCustName() {
        return custName;
    }

    /**Gets customer address
     * @return - Returns customer address
     */
    public String getCustAddress() {
        return custAddress;
    }

    /**Gets customer zip/postal code
     * @return - Returns zip/postal code
     */
    public String getCustPostalCode() {
        return custPostalCode;
    }

    /**Gets customer phone number
     * @return - Returns customer phone number
     */
    public String getCustPhone() {
        return custPhone;
    }
}

