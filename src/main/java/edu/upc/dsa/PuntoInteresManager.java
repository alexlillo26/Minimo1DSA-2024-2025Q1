package edu.upc.dsa;

import edu.upc.dsa.exceptions.TrackNotFoundException;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.User;
import java.util.Comparator;
import edu.upc.dsa.models.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;

public interface PuntoInteresManager {
    User addUser(String id, String nombre, String apellidos, String email, java.time.LocalDate nacimiento);
    List<User> getUsersSortedByName();
    User getUserById(String id);
    void addPointOfInterest(int x, int y, edu.upc.dsa.models.PuntoInteres.ElementType type);
    void registerUserAtPointOfInterest(String userId, int x, int y);
    List<edu.upc.dsa.models.PuntoInteres> getPointsOfInterestForUser(String userId);
    List<User> getUsersAtPointOfInterest(int x, int y);
    List<edu.upc.dsa.models.PuntoInteres> getPointsOfInterestByType(edu.upc.dsa.models.PuntoInteres.ElementType type);
    PuntoInteres getPointOfInterestByCoordinates(int x, int y);
}


