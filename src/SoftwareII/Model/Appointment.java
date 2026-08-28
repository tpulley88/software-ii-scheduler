package SoftwareII.Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**Appointment class that builds Appointment object from Abstract Appointment
 */
public class Appointment extends AbstractAppointment {

    //Declare variables
    private int apptID;
    private LocalDateTime apptStartDT;
    private LocalDateTime apptEndDT;
    private String apptStartDTS;
    private String apptEndDTS;
    private LocalDate apptStartDate;
    private LocalTime apptStartTime;
    private Timestamp apptStart;
    private LocalDate apptEndDate;
    private LocalTime apptEndTime;
    private Timestamp apptEnd;
    private LocalDateTime apptCreatedDate;
    private String apptCreateBy;
    private Timestamp apptLastUpdate;
    private String apptLastUpdateBy;

    /**
     * Appointment constructor to add new Appointment object to AppointmentsDAO.getAllAppointments() method list
     *
     * @param apptID        - Appointment ID
     * @param apptTitle     - Appointment Title
     * @param apptDesc      - Appointment Description
     * @param apptLoc       - Appointment Location
     * @param apptType      - Appointment Type
     * @param apptStartDate - Appointment Start Date
     * @param apptStartTime - Appointment Start Time
     * @param apptEndDate   - Appointment End Date
     * @param apptEndTime   - Appontment End Time
     * @param custID        - Customer ID
     * @param userID        - User ID
     * @param contactID     - Contact ID
     */
    public Appointment(int apptID, String apptTitle, String apptDesc, String apptLoc, String apptType, LocalDate apptStartDate, LocalTime apptStartTime, LocalDate apptEndDate, LocalTime apptEndTime, int custID, int userID, int contactID) {
        super(apptTitle, apptDesc, apptLoc, apptType, custID, userID, contactID);

        this.apptID = apptID;
        this.apptStartDate = apptStartDate;
        this.apptStartTime = apptStartTime;
        this.apptEndDate = apptEndDate;
        this.apptEndTime = apptEndTime;
    }

    /**
     * Appointment constructor to add new Appointment object to AppointmentsDAO.getAllAppts() method list
     *
     * @param apptID      - Appointment ID
     * @param apptTitle   - Appointment Title
     * @param apptDesc    - Appointment Description
     * @param apptLoc     - Appointment Location
     * @param apptType    - Appointment Type
     * @param apptStartDT - Appointment Start Date/Time
     * @param apptEndDT   - Appointment End Date/Time
     * @param custID      - Customer ID
     * @param userID      - User ID
     * @param contactID   - Contact ID
     */
    public Appointment(int apptID, String apptTitle, String apptDesc, String apptLoc, String apptType, LocalDateTime apptStartDT, LocalDateTime apptEndDT, int custID, int userID, int contactID) {
        super(apptTitle, apptDesc, apptLoc, apptType, custID, userID, contactID);

        this.apptID = apptID;
        this.apptStartDT = apptStartDT;
        this.apptEndDT = apptEndDT;
    }

    /**
     * Appointment constructor to add new Appointment object to AppointmentsDAO.getAllApptsDTString() method list
     *
     * @param apptID       - Appointment ID
     * @param apptTitle    - Appointment Title
     * @param apptDesc     - Appointment Description
     * @param apptLoc      - Appointment Location
     * @param apptType     - Appointment Type
     * @param apptStartDTS - Appointment Start Date/Time String (for display)
     * @param apptEndDTS   - Appointment End Date/Time String (for display)
     * @param custID       - Customer ID
     * @param userID       - User ID
     * @param contactID    - Contact ID
     */
    public Appointment(int apptID, String apptTitle, String apptDesc, String apptLoc, String apptType, String apptStartDTS, String apptEndDTS, int custID, int userID, int contactID) {
        super(apptTitle, apptDesc, apptLoc, apptType, custID, userID, contactID);

        this.apptID = apptID;
        this.apptStartDTS = apptStartDTS;
        this.apptEndDTS = apptEndDTS;
    }

    /**
     * Appointment constructor to add new appointment to SQL database
     *
     * @param apptTitle        - Appointment Title
     * @param apptDesc         - Appointment Description
     * @param apptLoc          - Appointment Location
     * @param apptType         - Appointment Type
     * @param apptStart        - Appointment State Date/Time
     * @param apptEnd          - Appointment End Date/Time
     * @param apptCreatedDate  - Appointment Created Date/Time
     * @param apptCreateBy     - Appointment Created By
     * @param apptLastUpdate   - Appointment Last Updated Date/Time
     * @param apptLastUpdateBy - Appointment Last Updated By
     * @param custID           - Customer ID
     * @param userID           - User ID
     * @param contactID        - Contact ID
     */
    public Appointment(String apptTitle, String apptDesc, String apptLoc, String apptType, Timestamp apptStart, Timestamp apptEnd, LocalDateTime apptCreatedDate, String apptCreateBy, Timestamp apptLastUpdate, String apptLastUpdateBy, int custID, int userID, int contactID) {
        super(apptTitle, apptDesc, apptLoc, apptType, custID, userID, contactID);

        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCreatedDate = apptCreatedDate;
        this.apptCreateBy = apptCreateBy;
        this.apptLastUpdate = apptLastUpdate;
        this.apptLastUpdateBy = apptLastUpdateBy;
    }

    /**
     * Appointment constructor for modifying appointment in SQL database
     *
     * @param apptID           - Appointment ID
     * @param apptTitle        - Appointment Title
     * @param apptDesc         - Appointment Description
     * @param apptLoc          - Appointment Location
     * @param apptType         - Appointment Type
     * @param apptStart        - Appointment Start Date/Time
     * @param apptEnd          - Appointment End Date/Time
     * @param apptLastUpdate   - Appointment Last Updated Date/Time
     * @param apptLastUpdateBy - Appointment Last Updated By
     * @param custID           - Customer ID
     * @param userID           - User ID
     * @param contactID        - Contact ID
     */
    public Appointment(int apptID, String apptTitle, String apptDesc, String apptLoc, String apptType, Timestamp apptStart, Timestamp apptEnd, Timestamp apptLastUpdate, String apptLastUpdateBy, int custID, int userID, int contactID) {
        super(apptTitle, apptDesc, apptLoc, apptType, custID, userID, contactID);

        this.apptID = apptID;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptLastUpdate = apptLastUpdate;
        this.apptLastUpdateBy = apptLastUpdateBy;
    }

    /**
     * Get appointment ID
     *
     * @return - Returns appointment ID
     */
    public int getApptID() {
        return apptID;
    }

    /**
     * Sets appointment ID
     *
     * @param apptID - Appointment ID
     */
    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    /**
     * Gets appointment start date/time
     *
     * @return - Returns appointment state date/time
     */
    public LocalDateTime getApptStartDT() {
        return apptStartDT;
    }

    /**
     * Gets appointment end date/time
     *
     * @return - Returns appointment end/date time
     */
    public LocalDateTime getApptEndDT() {
        return apptEndDT;
    }

    /**
     * Gets appointment created date/time
     *
     * @return - Returns appointment created date/time
     */
    public LocalDateTime getApptCreateDate() {
        return apptCreatedDate;
    }

    /**
     * Gets appointment created by
     *
     * @return - Returns user
     */
    public String getApptCreateBy() {
        return apptCreateBy;
    }

    /**
     * Gets appointment last updated Timestamp
     *
     * @return - Returns last update Timestamp
     */
    public Timestamp getApptLastUpdate() {
        return apptLastUpdate;
    }

    /**
     * Gets appointment last updated by
     *
     * @return - Returns user who updated appointment
     */
    public String getApptLastUpdateBy() {
        return apptLastUpdateBy;
    }

    /**
     * Gets appointment start date
     *
     * @return - Returns appointment start date
     */
    public LocalDate getApptStartDate() {
        return apptStartDate;
    }

    /**
     * Gets appointment start time
     *
     * @return - Returns appointment start time
     */
    public LocalTime getApptStartTime() {
        return apptStartTime;
    }

    /**
     * Gets appointment end date
     *
     * @return - Returns appointment end date
     */
    public LocalDate getApptEndDate() {
        return apptEndDate;
    }

    /**
     * Gets appointment end time
     *
     * @return - Returns appointment end time
     */
    public LocalTime getApptEndTime() {
        return apptEndTime;
    }

    /**
     * Gets appointment start Timestamp
     *
     * @return - Returns appointment start Timestamp
     */
    public Timestamp getApptStart() {
        return apptStart;
    }

    /**
     * Gets appointment end Timestamp
     *
     * @return - Returns appointment end Timestamp
     */
    public Timestamp getApptEnd() {
        return apptEnd;
    }

    /**
     * Gets appointment start date/time as a String
     *
     * @return - Returns String of appointment start date/time
     */
    public String getApptStartDTS() {
        return apptStartDTS;
    }

    /**
     * Gets appointment end date/time as a String
     *
     * @return - Returns String of appointment end date/time
     */
    public String getApptEndDTS() {
        return apptEndDTS;
    }

    /**
     * Sets override for displaying Appointment object information
     *
     * @return - Returns Appointment display string
     */
    @Override
    public String toString() {
        return ("#" + apptID + " " + getApptTitle());
    }

}


