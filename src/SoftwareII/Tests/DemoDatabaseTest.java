package SoftwareII.Tests;

import SoftwareII.DAO.AppointmentsDAO;
import SoftwareII.DAO.CustomersDAO;
import SoftwareII.DAO.UsersDAO;
import SoftwareII.Utility.JDBC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoDatabaseTest {
    @AfterEach
    void closeDatabase() {
        JDBC.closeConnection();
    }

    @Test
    void seededDemoDatabaseSupportsCoreQueries() throws Exception {
        assertTrue(JDBC.isDemoMode());
        JDBC.makeConnection();

        assertEquals(1, UsersDAO.getAllUsers().size());
        assertEquals("demo", UsersDAO.getAllUsers().get(0).getUserName());
        assertEquals(1, CustomersDAO.getAllCustomers().size());
        assertEquals(1, AppointmentsDAO.getAllAppointments().size());
    }
}
