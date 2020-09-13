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
import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import campcompanion.authentication.Role;
import campcompanion.authentication.Secured;
import campcompanion.hibernate.HibernateUtils;
import campcompanion.model.Log;
import campcompanion.model.Spot;

@Path("log")
@Secured({Role.USER})
public class LogResource {
	
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
	public Response create(String log) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Log LogAdd = objectMapper.readValue(log , Log.class);
			session.save(LogAdd);
			tx.commit();

			return Response.status(200).entity(log).build();
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
			List logs = session.createQuery("FROM Log").list();
			tx.commit();
			
			String arrayToJson = objectMapper.writeValueAsString(logs);
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
			Log log = (Log) session.get(Log.class, Integer.parseInt(id));
			tx.commit();
			
			if(log != null ) {
				String objectToJson = objectMapper.writeValueAsString(log);
				return Response.status(200).entity(objectToJson).build();
			} else {
				return Response.status(500).entity("Log with id " + id + " doesn't exist.").build();
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
    public Response update(@PathParam("id") String id, String logInfo) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Log log = (Log) session.get(Log.class, Integer.parseInt(id));
			if(log != null) {
				Log logMod = objectMapper.readValue(logInfo , Log.class);
				
				log.setComment(logMod.getComment());
				log.setNote(logMod.getNote());
				
				session.update(log);
				tx.commit();
				
				String objectToJson = objectMapper.writeValueAsString(log);
				return Response.status(200).entity(objectToJson).build();
			} else {
				return Response.status(500).entity("Log with id " + id + " doesn't exist.").build();
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
			Log log = (Log) session.get(Log.class, Integer.parseInt(id));
			session.delete(log);
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
	
	/********************************
	 * 
	 * 	OTHER R E Q U E S T S
	 * 
	 ********************************/
    @GET
    @Path("list/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogsForSpot(@PathParam("id") String spotId) {
    	ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		
		// Check if spot exists
		tx = session.beginTransaction();
		Spot spot = (Spot) session.get(Spot.class, Integer.parseInt(spotId));
		tx.commit();

		if (spot == null) {
			return Response.status(500).entity("Spot with id " + spotId + " doesn't exist.").build();
		}
		
		//Gather all logs for spot
		
		try {
			tx = session.beginTransaction();
			
			String hql =  "SELECT * FROM Log WHERE spot_id = :spotId";

			@SuppressWarnings("rawtypes")
			List logs = session.createNativeQuery(hql, Log.class)
					.setParameter("spotId",Integer.parseInt(spotId))
					.list();
			
			tx.commit();
			
			String arrayToJson = objectMapper.writeValueAsString(logs);
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
}
