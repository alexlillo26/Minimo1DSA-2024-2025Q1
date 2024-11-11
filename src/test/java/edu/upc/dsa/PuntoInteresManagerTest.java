package edu.upc.dsa;

import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class PuntoInteresManagerTest {
    PuntoInteresManager tm;

    @Before
    public void setUp() {
        this.tm = PuntoInteresImpl.getInstance();
        this.tm.addUser("1", "John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1));
        this.tm.addUser("2", "Jane", "Smith", "jane.smith@example.com", LocalDate.of(1985, 5, 15));
        this.tm.addPointOfInterest(10, 20, PuntoInteres.ElementType.DOOR);
        this.tm.addPointOfInterest(15, 25, PuntoInteres.ElementType.COIN);
    }

    @After
    public void tearDown() {
        // Clear the singleton instance
        this.tm = null;
    }

    @Test
    public void addUserTest() {
        Assert.assertEquals(2, tm.getUsersSortedByName().size());

        this.tm.addUser("3", "Alice", "Johnson", "alice.johnson@example.com", LocalDate.of(1992, 3, 10));

        Assert.assertEquals(3, tm.getUsersSortedByName().size());
    }

    @Test
    public void getUsersSortedByNameTest() {
        List<User> users = tm.getUsersSortedByName();
        Assert.assertEquals("Doe", users.get(0).getApellidos());
        Assert.assertEquals("Smith", users.get(1).getApellidos());
    }

    @Test
    public void getUserByIdTest() {
        User user = tm.getUserById("1");
        Assert.assertNotNull(user);
        Assert.assertEquals("John", user.getNombre());

        user = tm.getUserById("3");
        Assert.assertNull(user);
    }

    @Test
    public void addPointOfInterestTest() {
        Assert.assertEquals(1, tm.getPointsOfInterestByType(PuntoInteres.ElementType.DOOR).size());
        tm.addPointOfInterest(30, 40, PuntoInteres.ElementType.WALL);
        Assert.assertEquals(1, tm.getPointsOfInterestByType(PuntoInteres.ElementType.WALL).size());
    }

    @Test
    public void registerUserAtPointOfInterestTest() {
        tm.registerUserAtPointOfInterest("1", 10, 20);
        List<PuntoInteres> points = tm.getPointsOfInterestForUser("1");
        Assert.assertEquals(1, points.size());
        Assert.assertEquals(10, points.get(0).getX());
        Assert.assertEquals(20, points.get(0).getY());
    }

    @Test
    public void getUsersAtPointOfInterestTest() {
        tm.registerUserAtPointOfInterest("1", 10, 20);
        List<User> users = tm.getUsersAtPointOfInterest(10, 20);
        Assert.assertEquals(1, users.size());
        Assert.assertEquals("John", users.get(0).getNombre());
    }

    @Test
    public void getPointsOfInterestByTypeTest() {
        List<PuntoInteres> points = tm.getPointsOfInterestByType(PuntoInteres.ElementType.COIN);
        Assert.assertEquals(1, points.size());
        Assert.assertEquals(15, points.get(0).getX());
        Assert.assertEquals(25, points.get(0).getY());
    }
}