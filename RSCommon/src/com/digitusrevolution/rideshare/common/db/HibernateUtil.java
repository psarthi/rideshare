package com.digitusrevolution.rideshare.common.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory;
	private static final Logger logger = LogManager.getLogger(HibernateUtil.class.getName());

	static {
        try {
        	/* Commenting it as we will pass the complete JDBC string itself
        	String dbName = System.getProperty("RDS_DB_NAME");
        String userName = System.getProperty("RDS_USERNAME");
        String password = System.getProperty("RDS_PASSWORD");
        String hostname = System.getProperty("RDS_HOSTNAME");
        String port = System.getProperty("RDS_PORT");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
        */        	
        	Configuration conf = new Configuration().configure("/resources/hibernate.cfg.xml");
        conf.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        conf.setProperty("hibernate.connection.url", System.getProperty("JDBC_CONNECTION_STRING"));
        	sessionFactory = conf.buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
  
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
