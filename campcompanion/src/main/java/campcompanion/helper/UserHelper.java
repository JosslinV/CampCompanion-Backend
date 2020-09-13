package campcompanion.helper;

import org.hibernate.Session;
import org.hibernate.Transaction;

import campcompanion.hibernate.HibernateUtils;
import campcompanion.model.User;

public class UserHelper {
	
	public static User getUserByUsername(String username) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		// Look for user by his username
		tx = session.beginTransaction();
		User user = (User) session.createQuery("FROM User WHERE username=:username")
				.setParameter("username", username)
				.uniqueResult();
		tx.commit();
		
		session.close();
		
		return user;
	}
	
	public static User getUserByToken(String token) {
		Session session = HibernateUtils.getSessionFactory().openSession();
		Transaction tx = null;

		// Look for user by his username
		tx = session.beginTransaction();
		User user = (User) session.createQuery("FROM User WHERE token=:token")
				.setParameter("token", token)
				.uniqueResult();
		tx.commit();
		
		session.close();
		
		return user;
	}


}
