package campcompanion.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import campcompanion.authentication.Role;
import campcompanion.authentication.Secured;
import campcompanion.hibernate.HibernateUtils;
import campcompanion.model.User;

@Path("user")
@Secured({Role.USER})
public class UserResource {
	
	@Context
	SecurityContext securityContext;
	
	/********************************
	 * 
	 * 	ALL C R U D OPERATIONS
	 * 
	 ********************************/
	
	// C stands for C R E A T E !
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String user) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			User userAdd = objectMapper.readValue(user , User.class);
			userAdd.setRole(Role.USER);
			session.save(userAdd);
			tx.commit();

			return Response.status(200).entity(user).build();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return Response.serverError().build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			session.close();
		}
	}
	
	// R stands for R E A D !
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response read() {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			@SuppressWarnings("rawtypes")
			List users = session.createQuery("FROM User").list();
			tx.commit();
			
			String arrayToJson = objectMapper.writeValueAsString(users);
			return Response.status(200).entity(arrayToJson).build();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return Response.serverError().build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			session.close();
		}
	}
	
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readOne(@PathParam("id") String id) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			User user = (User) session.get(User.class, Integer.parseInt(id));
			tx.commit();
			if(user != null ) {
				String objectToJson = objectMapper.writeValueAsString(user);
				return Response.status(200).entity(objectToJson).build();
			} else {
				return Response.status(500).entity("User with id " + id + " doesn't exist.").build();
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return Response.serverError().build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			session.close();
		}
    }
	
	// U stands for U P D A T E !
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response update(@PathParam("id") String id, String userInfo) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			User user = (User) session.get(User.class, Integer.parseInt(id));
			if(user != null) {
				User userMod = objectMapper.readValue(userInfo , User.class);
				
				user.setUsername(userMod.getUsername());
				user.setPassword(userMod.getPassword());
				user.setToken(userMod.getToken());
				
				session.update(user);
				tx.commit();
				
				String objectToJson = objectMapper.writeValueAsString(user);
				return Response.status(200).entity(objectToJson).build();
			} else {
				return Response.status(500).entity("User with id " + id + " doesn't exist.").build();
			}

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return Response.serverError().build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			session.close();
		}
    }
	
	// D stands for D E L E T E !
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			User user = (User) session.get(User.class, Integer.parseInt(id));
			session.delete(user);
			tx.commit();
			
			return Response.ok().entity(id).build();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			session.close();
		}
    }

}
