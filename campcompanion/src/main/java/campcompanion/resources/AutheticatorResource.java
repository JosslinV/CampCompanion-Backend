package campcompanion.resources;

import java.sql.Timestamp;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import campcompanion.authentication.token.TokenIssuer;
import campcompanion.exception.AuthenticationException;
import campcompanion.helper.UserHelper;
import campcompanion.hibernate.HibernateUtils;
import campcompanion.model.Credential;
import campcompanion.model.User;

@Path("authentication")
public class AutheticatorResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(String credentialStr) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			Credential credential = objectMapper.readValue(credentialStr, Credential.class);

			User userAuthenticated = authenticate(credential);

			String token = TokenIssuer.generateToken();

			// Update the user token on base
			userAuthenticated.setToken(token);
			updateUser(userAuthenticated);

			// Return the token on the response
			return Response.ok(token).build();

		} catch (AuthenticationException e) {
			System.err.println(e);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			System.err.println(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	private User authenticate(Credential credential) throws AuthenticationException {
		User user = UserHelper.getUserByUsername(credential.getUsername());

		// If it is found
		if (user != null) {
			// If Password is correct
			if (user.getPassword().equals(credential.getPassword())) {
				System.out.println("User " + user.getUsername() + " authenticated.");
				return user;
			} else {
				throw new AuthenticationException("Password is incorrect");
			}
			// User doesn't exist
		} else {
			throw new AuthenticationException("User doesn't exist");
		}
	}

	private void updateUser(User user) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		tx = session.beginTransaction();
		session.update(user);
		tx.commit();
		
		session.close();
	}
}
