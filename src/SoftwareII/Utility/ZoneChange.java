package SoftwareII.Utility;

import java.time.*;

/**Method that allows for conversion between time zones
 */
public class ZoneChange {

    /** Takes time from one zone and converts to another time zone
     * @param dateTime - Date/Time to convert
     * @param fromZone - Change from this zone
     * @param zoneDesired - Change to this zone
     * @return - Return converted Date/Time
     */
    public static LocalDateTime getDesiredDateTime(LocalDateTime dateTime, ZoneId fromZone, ZoneId zoneDesired) {

        ZonedDateTime convTimeZDT = ZonedDateTime.of(dateTime, fromZone);
        Instant convToInstant = convTimeZDT.toInstant();
        ZonedDateTime getNewDT = convToInstant.atZone(zoneDesired);

        return getNewDT.toLocalDateTime();

    }
}
