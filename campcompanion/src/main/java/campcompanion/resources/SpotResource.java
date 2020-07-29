package campcompanion.resources;

import java.util.ArrayList;
import java.util.Iterator;
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
import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import campcompanion.hibernate.HibernateUtils;
import campcompanion.model.Spot;

@Path("spot")
public class SpotResource {

	/********************************
	 * 
	 * ALL C R U D OPERATIONS
	 * 
	 ********************************/

	// C stands for C R E A T E !
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String spot) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Spot spotAdd = objectMapper.readValue(spot, Spot.class);
			session.save(spotAdd);
			tx.commit();

			return Response.status(200).entity(spot).build();
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
			List spots = session.createQuery("FROM Spot").list();
			tx.commit();

			String arrayToJson = objectMapper.writeValueAsString(spots);
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
			Spot spot = (Spot) session.get(Spot.class, Integer.parseInt(id));
			tx.commit();

			if (spot != null) {
				String objectToJson = objectMapper.writeValueAsString(spot);
				return Response.status(200).entity(objectToJson).build();
			} else {
				return Response.status(500).entity("Spot with id " + id + " doesn't exist.").build();
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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response update(@PathParam("id") String id, String userInfo) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			Spot spot = (Spot) session.get(Spot.class, Integer.parseInt(id));
			if (spot != null) {
				Spot spotMod = objectMapper.readValue(userInfo, Spot.class);

				spot.setName(spotMod.getName());
				spot.setLatitude(spotMod.getLatitude());
				spot.setLongitude(spotMod.getLongitude());
				spot.setAccessibilityNote(spotMod.getAccessibilityNote());
				spot.setUtilitiesNote(spotMod.getUtilitiesNote());
				spot.setLocationNote(spotMod.getLocationNote());
				spot.setPrivacyNote(spotMod.getPrivacyNote());

				session.update(spot);
				tx.commit();

				String objectToJson = objectMapper.writeValueAsString(spot);
				return Response.status(200).entity(objectToJson).build();
			} else {
				return Response.status(500).entity("Spot with id " + id + " doesn't exist.").build();
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
			Spot spot = (Spot) session.get(Spot.class, Integer.parseInt(id));
			session.delete(spot);
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
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response query(@Context UriInfo query) {
    	ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;
		
		//TODO: Refactor cette salet�e...
		String latitude = query.getQueryParameters().getFirst("latitude");
		String longitude = query.getQueryParameters().getFirst("longitude");
		String accessibility_str = query.getQueryParameters().getFirst("accessibility");
		String location_str = query.getQueryParameters().getFirst("location");
		String utilities_str = query.getQueryParameters().getFirst("utilities");
		String privacy_str = query.getQueryParameters().getFirst("privacy");
		
		int accessibility = (accessibility_str == null) ? 0 : Integer.parseInt(accessibility_str);
		int location = (location_str == null) ? 0 : Integer.parseInt(location_str);
		int utilities = (utilities_str == null) ? 0 : Integer.parseInt(utilities_str);
		int privacy = (privacy_str == null) ? 0 : Integer.parseInt(privacy_str);
		
		List<String> requestOptions = new ArrayList<>();
		if(accessibility != 0) {
			requestOptions.add("accessibility_note=" + accessibility);
		}
		if(location != 0) {
			requestOptions.add("location_note=" + location);
		}
		if(utilities != 0) {
			requestOptions.add("utilities_note=" + utilities);
		}
		if(privacy != 0) {
			requestOptions.add("privacy_note=" + privacy);
		}
		
		StringBuilder where = new StringBuilder();
		if(requestOptions.size() > 0) {
			where.append("WHERE ");
			Iterator<String> optionItr = requestOptions.iterator();
			while(optionItr.hasNext()) {
				String option = optionItr.next();
				where.append(option);
				if(optionItr.hasNext()) {
					where.append(" AND ");
				}
			}
		}
		
		
		String select = "SELECT *, haversine(:userLat,:userLong,Spot.latitude,Spot.longitude) AS distance ";
		String from = "FROM Spot ";
		String order = "ORDER BY distance ASC";

		StringBuilder hql = new StringBuilder(select).append(from).append(where.toString()).append(order);
		
		
		try {
			tx = session.beginTransaction();
			
			@SuppressWarnings("rawtypes")
			List spots = session.createNativeQuery(hql.toString(),Spot.class)
					.setParameter("userLat",Float.parseFloat(latitude))
					.setParameter("userLong",Float.parseFloat(longitude))
					.list();
			
			tx.commit();
			
			String arrayToJson = objectMapper.writeValueAsString(spots);
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
