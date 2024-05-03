package com.adas.app.leap.system;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HybernetUtil {
	private static SessionFactory factory; 
	private static SessionFactory sql_factory; 
	 
	  
	@SuppressWarnings("unused")
 
	
	private static void HyberNetSessionFactory()         
	{
		try
		{
		    Configuration cfg = new Configuration().configure("/hibernate_pg_sql.cfg.xml");
			
	        factory = cfg.buildSessionFactory();


	    } 
		catch (Throwable ex) 
		{
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	
	public static SessionFactory GetHyberNetSessionFactory()
	{
		if(factory==null || factory.isClosed())
		{
			HyberNetSessionFactory();
			System.out.println("Creating New sql Factory");
			
		}
		else
		{
			System.out.println("sql Factory found");
		}
		return factory;
	}
	//mysql
	private static void HyberNetMysqlSessionFactory()         
	{
		try
		{
		    Configuration cfg = new Configuration().configure("/hibernate_mysql.cfg.xml");
			
			sql_factory = cfg.buildSessionFactory();


	    }  
		catch (Throwable ex) 
		{
			System.err.println("Initial SessionFactory creation failed sql_factory." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	
	public static SessionFactory GetHyberNetMysqlSessionFactoryOLd()
	{
		if(sql_factory==null || sql_factory.isClosed())
		{
			HyberNetMysqlSessionFactory();
			System.out.println("Creating New MYSQLsql Factory");
			
		}
		else
		{
			System.out.println("sql Factory found");
		}
		return sql_factory;
	}
	
	
	 
	
}
