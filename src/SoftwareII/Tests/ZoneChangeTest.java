package SoftwareII.Tests;

import SoftwareII.Utility.ZoneChange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

class ZoneChangeTest {

    //Tests whether time changes from EST to UTC
    @Test
    void checkESTtoUTC() {
        LocalDateTime currentESTtime = LocalDateTime.now(ZoneId.of("America/New_York"));
        String currentUTCtime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES).toString();
        String ESTtoUTCConvertedTime = ZoneChange.getDesiredDateTime(currentESTtime, ZoneId.of("America/New_York"), ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES).toString();

        Assertions.assertEquals(currentUTCtime, ESTtoUTCConvertedTime);
    }

    //Tests whether system time can be used to change to UTC
    @Test
    void checkSystemtoUTC() {
        LocalDateTime currentSystemTime = LocalDateTime.now();
        String currentUTCtime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES).toString();
        String ESTtoUTCConvertedTime = ZoneChange.getDesiredDateTime(currentSystemTime, ZoneId.systemDefault(), ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES).toString();

        Assertions.assertEquals(currentUTCtime, ESTtoUTCConvertedTime);
    }

    //Tests whether UTC time can be used to change to UTC
    @Test
    void checkUTCtoUTC() {
        LocalDateTime currentUTCTime = LocalDateTime.now(ZoneId.of("UTC"));
        String currentUTCtime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES).toString();
        String ESTtoUTCConvertedTime = ZoneChange.getDesiredDateTime(currentUTCTime, ZoneId.of("UTC"), ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES).toString();

        Assertions.assertEquals(currentUTCtime, ESTtoUTCConvertedTime);
    }

    //Tests whether time changes from UTC to EST
    @Test
    void checkUTCtoEST() {
        LocalDateTime currentUTCtime = LocalDateTime.now(ZoneId.of("UTC"));
        String currentESTtime = LocalDateTime.now(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.MINUTES).toString();
        String ESTtoUTCConvertedTime = ZoneChange.getDesiredDateTime(currentUTCtime, ZoneId.of("UTC"), ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.MINUTES).toString();

        Assertions.assertEquals(currentESTtime, ESTtoUTCConvertedTime);
    }

}