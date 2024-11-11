package edu.upc.dsa;

import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;
import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

public class PuntoInteresImpl implements PuntoInteresManager {
    private static PuntoInteresImpl instance;
    private List<User> users;
    private List<PuntoInteres> puntosInteres;
    private Map<String, List<PuntoInteres>> userPointsOfInterest;
    final static Logger logger = Logger.getLogger(PuntoInteresImpl.class);

    private PuntoInteresImpl() {
        this.users = new LinkedList<>();
        this.puntosInteres = new LinkedList<>();
        this.userPointsOfInterest = new HashMap<>();
    }

    public static PuntoInteresImpl getInstance() {
        if (instance == null) {
            instance = new PuntoInteresImpl();
        }
        return instance;
    }

    @Override
    public User addUser(String id, String nombre, String apellidos, String email, LocalDate nacimiento) {
        logger.info("addUser called with parameters: id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email + ", nacimiento=" + nacimiento);

        User newUser = new User(id, nombre, apellidos, email, nacimiento);
        this.users.add(newUser);

        logger.info("addUser finished, new user added: " + newUser.getNombre());
        return newUser;
    }

    @Override
    public List<User> getUsersSortedByName() {
        logger.info("getUsersSortedByName called");

        List<User> sortedUsers = new ArrayList<>(this.users);
        sortedUsers.sort(Comparator.comparing(User::getApellidos).thenComparing(User::getNombre));

        logger.info("getUsersSortedByName finished, returning sorted list of users: " + sortedUsers);
        return sortedUsers;
    }

    @Override
    public User getUserById(String id) {
        logger.info("getUserById called with id: " + id);

        User user = this.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            logger.error("User with id " + id + " does not exist.");
        }

        logger.info("getUserById finished, returning user: " + user);
        return user;
    }

    @Override
    public void addPointOfInterest(int x, int y, PuntoInteres.ElementType type) {
        logger.info("addPointOfInterest called with coordinates: (" + x + ", " + y + ") and type: " + type);

        PuntoInteres punto = new PuntoInteres();
        punto.setX(x);
        punto.setY(y);
        punto.setType(type);

        this.puntosInteres.add(punto);

        logger.info("addPointOfInterest finished, new point of interest added: " + punto);
    }

    @Override
    public void registerUserAtPointOfInterest(String userId, int x, int y) {
        logger.info("registerUserAtPointOfInterest called with userId: " + userId + ", coordinates: (" + x + ", " + y + ")");

        // Check if the user exists
        User user = this.users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (user == null) {
            logger.error("User with id " + userId + " does not exist.");
            return;
        }

        // Check if the point of interest exists
        PuntoInteres punto = this.puntosInteres.stream()
                .filter(p -> p.getX() == x && p.getY() == y)
                .findFirst()
                .orElse(null);

        if (punto == null) {
            logger.error("Point of interest at coordinates (" + x + ", " + y + ") does not exist.");
            return;
        }

        // Register the user at the point of interest
        this.userPointsOfInterest.computeIfAbsent(userId, k -> new LinkedList<>()).add(punto);
        logger.info("User " + userId + " passed through point of interest at coordinates (" + x + ", " + y + ")");
    }

    @Override
    public List<PuntoInteres> getPointsOfInterestForUser(String userId) {
        logger.info("getPointsOfInterestForUser called with userId: " + userId);

        List<PuntoInteres> points = this.userPointsOfInterest.get(userId);
        if (points == null) {
            logger.error("No points of interest found for user with id " + userId);
            return Collections.emptyList();
        }

        logger.info("getPointsOfInterestForUser finished, returning points of interest for user: " + points);
        return points;
    }

    @Override
    public List<User> getUsersAtPointOfInterest(int x, int y) {
        logger.info("getUsersAtPointOfInterest called with coordinates: (" + x + ", " + y + ")");

        // Check if the point of interest exists
        PuntoInteres punto = this.puntosInteres.stream()
                .filter(p -> p.getX() == x && p.getY() == y)
                .findFirst()
                .orElse(null);

        if (punto == null) {
            logger.error("Point of interest at coordinates (" + x + ", " + y + ") does not exist.");
            return Collections.emptyList();
        }

        // Find users who have passed through the point of interest
        List<User> usersAtPoint = new LinkedList<>();
        for (Map.Entry<String, List<PuntoInteres>> entry : this.userPointsOfInterest.entrySet()) {
            if (entry.getValue().contains(punto)) {
                usersAtPoint.add(this.users.stream()
                        .filter(u -> u.getId().equals(entry.getKey()))
                        .findFirst()
                        .orElse(null));
            }
        }

        logger.info("getUsersAtPointOfInterest finished, returning list of users: " + usersAtPoint);
        return usersAtPoint;
    }

    @Override
    public List<PuntoInteres> getPointsOfInterestByType(PuntoInteres.ElementType type) {
        logger.info("getPointsOfInterestByType called with type: " + type);

        List<PuntoInteres> filteredPoints = this.puntosInteres.stream()
                .filter(p -> p.getType() == type)
                .collect(Collectors.toList());

        logger.info("getPointsOfInterestByType finished, returning list of points: " + filteredPoints);
        return filteredPoints;
    }

    @Override
    public PuntoInteres getPointOfInterestByCoordinates(int x, int y) {
        return this.puntosInteres.stream()
                .filter(p -> p.getX() == x && p.getY() == y)
                .findFirst()
                .orElse(null);
    }
}