package campcompanion.hibernate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import campcompanion.model.Log;
import campcompanion.model.Spot;
import campcompanion.model.User;

public class HibernateUtils {

	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		Properties hibernateProp = new Properties();
		
		try {
			InputStream input = new FileInputStream("src/main/resources/hibernate.properties");
			hibernateProp.load(input);
		} catch (IOException ex) {
			System.err.println("Are you sure the hibernate.properties file is present ?" + ex);
		}

		try {
			Configuration configuration = new Configuration();

			Properties settings = new Properties();

			settings.put(Environment.DRIVER, hibernateProp.getProperty("database_driver"));
			settings.put(Environment.URL, hibernateProp.getProperty("database_url"));
			settings.put(Environment.USER, hibernateProp.getProperty("user"));
			settings.put(Environment.PASS, hibernateProp.getProperty("password"));
			settings.put(Environment.DIALECT, hibernateProp.getProperty("dialect"));
			settings.put(Environment.SHOW_SQL, hibernateProp.getProperty("show_sql"));
			settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, hibernateProp.getProperty("session_context"));
			settings.put(Environment.HBM2DDL_AUTO, hibernateProp.getProperty("mode"));

			configuration.setProperties(settings);
			configuration.addAnnotatedClass(User.class);
			configuration.addAnnotatedClass(Spot.class);
			configuration.addAnnotatedClass(Log.class);

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();

			return configuration.buildSessionFactory(serviceRegistry);

		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
