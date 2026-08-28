package SoftwareII.Model;

import SoftwareII.DAO.CustomersDAO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**Customer class that builds Customer object from Abstract Customer
 */
public class Customer extends AbstractCustomer{

    //Declare variables
    private int custID;
    private int divID;
    private String custState;
    private String custCountry;
    private LocalDateTime createdDate;
    private String createdBy;
    private Timestamp updatedTime;
    private String updatedBy;

    /**Customer constructor to add new Customer object to CustomersDAO.getAllCustomers() method list
     * @param custID - Customer ID
     * @param custName - Customer Name
     * @param custAddress - Customer Address
     * @param custPostalCode - Customer Zip/Postal Code
     * @param custPhone - Customer Phone Number
     * @param divID - Division ID
     */
    public Customer(int custID, String custName, String custAddress, String custPostalCode, String custPhone, int divID) {
        super(custName, custAddress, custPostalCode, custPhone);
        this.custID = custID;
        this.divID = divID;

    }

    /**Customer constructor to add new Customer to SQL database
     * @param custName - Customer Name
     * @param custAddress - Customer Address
     * @param custPostalCode - Customer Zip/Postal Code
     * @param custPhone - Customer Phone Number
     * @param createdDate - Created Date/Time
     * @param createdBy - Created By
     * @param updatedTime - Last Updated Date/Time
     * @param updatedBy - Last Updated By
     * @param divID - Division ID
     */
    public Customer(String custName, String custAddress, String custPostalCode, String custPhone, LocalDateTime createdDate, String createdBy, Timestamp updatedTime, String updatedBy, int divID) {
        super(custName, custAddress, custPostalCode, custPhone);
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedTime = updatedTime;
        this.updatedBy = updatedBy;
        this.divID = divID;
    }

    /**Customer constructor for modifying customer in SQL database
     * @param custID - Customer ID
     * @param custName - Customer Name
     * @param custAddress - Customer Address
     * @param custPostalCode - Customer Zip/Postal Code
     * @param custPhone - Customer Phone Number
     * @param divID - Division ID
     * @param updatedTime - Last Updated Date/Time
     * @param updatedBy - Last Updated By
     */
    public Customer(int custID, String custName, String custAddress, String custPostalCode, String custPhone, int divID, Timestamp updatedTime, String updatedBy) {
        super(custName, custAddress, custPostalCode, custPhone);
        this.custID = custID;
        this.divID = divID;
        this.updatedTime = updatedTime;
        this.updatedBy = updatedBy;
    }

    /**Gets Customer ID
     * @return - Returns customer ID
     */
    public int getCustID() {
        return custID;
    }

    /**Sets Customer ID
     * @param custID - Customer ID
     */
    public void setCustID(int custID) {
        this.custID = custID;
    }

    /**Gets Division ID
     * @return - Returns Division ID
     */
    public int getDivID() {
        return divID;
    }

    /**Gets Customer State from CustomersDAO
     * @return - Returns customer state
     * @throws SQLException - Exception
     */
    public String getCustState() throws SQLException {
        custState = CustomersDAO.custStateFromDivID(divID);
        return custState;
    }

    /**Gets Customer Country from Customers DAO
     * @return - Returns customer country
     * @throws SQLException - Exception
     */
    public String getCustCountry() throws SQLException {
        custCountry = CustomersDAO.custCountryFromDivID(divID);
        return custCountry;
    }

    /**Gets Created Date/Time
     * @return - Returns created date/time
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**Gets user who created customer
     * @return - Returns user
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**Gets updated time
     * @return - Returns updated Timestamp
     */
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    /**Gets user who updated customer
     * @return - Returns user
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**Sets override for displaying Customer information
     * @return - Returns Customer display string
     */
    @Override
    public String toString(){
        return("#" + custID + " " + getCustName());

    }
}


