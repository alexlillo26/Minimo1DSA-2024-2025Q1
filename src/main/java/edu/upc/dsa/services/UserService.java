package edu.upc.dsa.services;

import edu.upc.dsa.PuntoInteresImpl;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.ElementType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Api(value = "/user", description = "Endpoint to User Service")
@Path("/user")
public class UserService {
    private PuntoInteresImpl tm = PuntoInteresImpl.getInstance();

    // 1. Añadir un usuario
    @POST
    @ApiOperation(value = "Add a new user", notes = "Adds a new user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User created successfully"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        try {
            tm.addUser(user.getId(), user.getNombre(), user.getApellidos(), user.getEmail(), user.getNacimiento());
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    // 2. Listar todos los usuarios ordenados por nombre alfabético
    @GET
    @ApiOperation(value = "Get all users ordered by name", notes = "Returns the list of all users ordered by last name and first name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/users/ordered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersOrderedByName() {
        List<User> users = this.tm.getUsersSortedByName();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.status(200).entity(entity).build();
    }

    // 3. Obtener un usuario por su id
    @GET
    @ApiOperation(value = "Get user by id", notes = "Returns the user information for the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class),
            @ApiResponse(code = 500, message = "User not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id) {
        User user = this.tm.getUserById(id);
        if (user == null) {
            return Response.status(500).build();
        }
        return Response.status(200).entity(user).build();
    }

    // 4. Añadir un punto de interés
    @POST
    @ApiOperation(value = "Add a new point of interest", notes = "Adds a new point of interest with the provided coordinates and type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Point of interest created successfully"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/addPointOfInterest")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPointOfInterest(PuntoInteres puntoInteres) {
        try {
            tm.addPointOfInterest(puntoInteres.getX(), puntoInteres.getY(), puntoInteres.getElementType());
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    // 5. Registrar un usuario en un punto de interés
    @POST
    @ApiOperation(value = "Register a user at a point of interest", notes = "Registers a user at the specified point of interest with the given coordinates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User registered at point of interest successfully"),
            @ApiResponse(code = 404, message = "User or point of interest not found"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/registerUserAtPointOfInterest")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUserAtPointOfInterest(@QueryParam("userId") String userId, @QueryParam("x") int x, @QueryParam("y") int y) {
        try {
            User user = tm.getUserById(userId);
            if (user == null) {
                return Response.status(404).entity("User not found").build();
            }

            PuntoInteres puntoInteres = tm.getPointOfInterestByCoordinates(x, y);
            if (puntoInteres == null) {
                return Response.status(404).entity("Point of interest not found").build();
            }

            tm.registerUserAtPointOfInterest(userId, x, y);
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    // 6. Consultar los puntos de interés por los que un usuario ha pasado
    @GET
    @ApiOperation(value = "Get points of interest where a user has walked by", notes = "Returns the list of points of interest a user has passed through, in the order they were registered")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/pointsOfInterestForUser/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPointsOfInterestForUser(@PathParam("userId") String userId) {
        try {
            List<PuntoInteres> points = tm.getPointsOfInterestForUser(userId);
            if (points == null || points.isEmpty()) {
                return Response.status(404).entity("No points of interest found for user").build();
            }
            GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(points) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    // 7. Listar los usuarios que han pasado por un punto de interés indentificado por sus coordenadas.
    @GET
    @ApiOperation(value = "Get users that have walked by a point of interest with its coordenates", notes = "Returns the list of users who have passed through a point of interest identified by its coordinates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 404, message = "Point of interest not found"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/usersAtPointOfInterest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersAtPointOfInterest(@QueryParam("x") int x, @QueryParam("y") int y) {
        try {
            List<User> users = tm.getUsersAtPointOfInterest(x, y);
            if (users == null || users.isEmpty()) {
                return Response.status(404).entity("No users found at the point of interest").build();
            }
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    // 8. Consultar los puntos de interés del mapa que sean de un tipo determinado
    @GET
    @ApiOperation(value = "Get points of interest of the map that are of a certain type", notes = "Returns the list of points of interest on the map that are of a specific type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/pointsOfInterestByType")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPointsOfInterestByType(@QueryParam("type") PuntoInteres.ElementType type) {
        try {
            List<PuntoInteres> points = tm.getPointsOfInterestByType(type);
            if (points == null || points.isEmpty()) {
                return Response.status(404).entity("No points of interest found for the specified type").build();
            }
            GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(points) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}