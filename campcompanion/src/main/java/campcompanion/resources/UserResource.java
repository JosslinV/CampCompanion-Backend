package campcompanion.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import campcompanion.hibernate.HibernateUtils;
import campcompanion.model.User;

@Path("user")
public class UserResource {
	
	/*
	 * 
	 * 	ALL C R U D OPERATIONS
	 * 
	 */
	
	// C stands for C R E A T E !
	
	// R stands for R E A D !
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			List users = session.createQuery("FROM User").list();
			tx.commit();
			
			return Response.status(200).entity("[]").build();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return Response.serverError().build();
		} finally {
			session.close();
		}
	}
	
	// U stands for U P D A T E !
	
	// D stands for D E L E T E !
	

}
