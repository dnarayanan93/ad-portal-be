package com.adas.app.leap.common;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.adas.app.leap.config.JwtTokenUtil;
import com.adas.app.leap.system.HybernetUtil;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class CommonFunc {

	@Autowired
	private ResponseDataGeneric result;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${telegram.url-string}")
	private String tel_url;

	@Value("${telegram.token}")
	private String tel_token;

	@Value("${smtp-config.mail-from}")
	private String mail_from;
	
	@Value("${smtp-config.mail-username}")
	private String mail_username;
	
	@Value("${smtp-config.mail-password}")
	private String mail_password;


	
	private SessionFactory factory;
	
	Logger logger = LoggerFactory.getLogger(CommonFunc.class);

	public String getTimeStamp() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		return sdf.format(timestamp).toString();

	}

	public String getUserDetails(String uid) {
		int currentLine = 0;
		Gson gson = new Gson();
		org.json.simple.JSONObject return_obj = new JSONObject();
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();

		try {

			String sql = "select emp_code,crypt_pwd bpass from lp.employee_master where emp_code='"+uid+"'";

			SQLQuery q = session.createSQLQuery(sql);



			logger.info("getUserDetails-Sql :" + sql);
			List<Map<String, Object>> query_data_obj = null;

			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					return_obj.put("uid", row.get("emp_code").toString());
					return_obj.put("pwd", row.get("bpass").toString());

				}

			}

		} catch (Exception e) {
			if(session.isOpen()) {
				session.close();
			}
			logger.info("Error in geting user details :" + e.getMessage());
		}finally {
			if(session.isOpen()) {
				session.close();
			}
		}


		result.SetSuccess("Success", return_obj);
		return gson.toJson(result);

	}
	
	public String getUserRoleID(String uid,String appCode) {
		
		
		Gson gson = new Gson();
		org.json.simple.JSONObject return_obj = new JSONObject();
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();
		
		String userRoleCode=null;
		
		try {
			
			String sql="select uid,role_code  from app_central_v2.user_role_master where uid='"+uid+"' and app_code='"+appCode+"'";
			
			System.out.println("role :"+sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			
			List<Map<String, Object>> query_data_obj = null;
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				
				for (Map<String, Object> row : query_data_obj) {

					userRoleCode = row.get("role_code").toString();					

				}

			}
			

			
		}catch(Exception e){
			System.out.println("Error in geting user details"+e.getMessage());
		}finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		

		return userRoleCode;
		
	}

	public String getUserRoleDesc(String uid,String appCode) {
		
		String userRoleDesc=null;
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();
		
		try {
			
			String sql="select uid,role_desc  from app_central_v2.user_role_master where uid='"+uid+"' and app_code='"+appCode+"'";
			
			System.out.println("role :"+sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			
			List<Map<String, Object>> query_data_obj = null;
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					userRoleDesc = row.get("role_desc").toString();
				

				}

			}
			
		}catch(Exception e){
			System.out.println("Error in geting user details"+e.getMessage());
		}finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		

		return userRoleDesc;
		
	}
	
	public String getMailID(String searchKey,String searchKeyType) {
		
		String userMailID=null;
		
		String sqlCondition = null;
		
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();
		
		if(searchKeyType.equals("role_code")) {
			sqlCondition ="rm.role_code='"+searchKey+"'";
		}else {
			sqlCondition = "um.uid='"+searchKey+"'";
		}
		
		try {
			
			String sql="select um.mail_id "+ 
						"from app_central_v2.app_user_master um "+
						"left join app_central_v2.user_role_master rm on um.uid=rm.uid where "+sqlCondition;
			
			System.out.println("getMailID :"+sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					userMailID = row.get("mail_id").toString();
				}

			}

			
		}catch(Exception e){
			System.out.println("Error in geting user mail id"+e.getMessage());
		}finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		

		return userMailID;
		
	}
	
	public String getMailIdByDocNo(int docNo) {
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();
		
		String userMailID=null;
		try {
			
			String sql="select um.mail_id from app_central_v2.item_master im " + 
						"left join app_central_v2.app_user_master um on im.created_by=um.uid "+
						"where im.internal_code="+docNo+"";
			
			System.out.println("getMailIdByDocNo :"+sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			
			List<Map<String, Object>> query_data_obj = null;
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					userMailID = row.get("mail_id").toString();
				

				}

			}
			

			
		}catch(Exception e){
			System.out.println("Error in geting user mail id"+e.getMessage());
		}finally {
			if(session.isOpen()) {
				session.close();
			}
		}
		

		return userMailID;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDetailsByDocNo(int docNo) {
		
		List<Map<String, Object>> retRows = null;
		
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();
		
		try {
			
			String sql="select * from app_central_v2.item_master im where internal_code="+docNo+"";
			
			System.out.println("getMailIdByDocNo :"+sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			
			
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
					retRows = q.list();

			}
			
		}catch(Exception e){
			System.out.println("Error in geting user mail id"+e.getMessage());
		}
		
		return retRows;
		
	}
	

	
	
	
	public String getUserFromToken(HttpServletRequest request) {

		
		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the
		// Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				System.out.println("username   " + username);
			} catch (IllegalArgumentException e) {
				System.out.println(e);
			} catch (ExpiredJwtException e) {
				System.out.println(e);
			}
		} else {
			logger.info("JWT Token does not begin with Bearer String");
		}

		return username;

	}

	public void sendMail(String sub, String body, String to, String[] cc) throws UnsupportedEncodingException {

		// Sender's email ID needs to be mentioned
		String from = mail_from;
		String username = mail_username;
		String password = mail_password;

		String from_name = "Partner Portal";

		logger.info("sendMail---->>from:"+from);
		logger.info("sendMail---->>username:"+username);
		logger.info("sendMail---->>password:"+password);
		// Assuming you are sending email through relay.jangosmtp.net

		String host = "czismtp.logix.in";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from, from_name));

			// Enter TO Recepient here
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Enter CC Recepient here

			
			if(cc!=null) {
				int ccLen = cc.length;
				String ccName = null;
				
				
				for (int i = 0; i < ccLen; i++) {
					if (!cc[i].equals("na")) {
						message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
					}
				}
			}
			message.setSubject(sub);

			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setContent(body, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);

			message.setContent(multipart);

			Transport.send(message);
			logger.info("Mail sent to ->>>> "+to);
			logger.info("message body----------"+body);

			// System.exit(0);
		} catch (MessagingException e) {
			
			logger.info("Error sending mail sent to ->>>> "+e.getMessage());
			
		}

	}

public static void sendMailToArray(String sub, String body, String[] to) throws UnsupportedEncodingException {
        
        
        // Sender's email ID needs to be mentioned
        String from = "it_reports@weikfield.com";//change accordingly
        final String username = "it_reports@weikfield.com";//change accordingly
        final String password = "Blue*@23";//change accordingly
        
        String from_name="DT Web Admin";

        // Assuming you are sending email through relay.jangosmtp.net
        
        String host = "czismtp.logix.in";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        javax.mail.Session session = javax.mail.Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, from_name));

            
            //Enter CC Recepient here
            int toLen=to.length;
            String toName=null;
            
            for(int i=0;i< toLen ; i++) {
            	if(!to[i].equals("na")) {
            		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
            	}	            	
            }



            message.setSubject(sub);

            BodyPart messageBodyPart1 = new MimeBodyPart();  
            messageBodyPart1.setContent(body, "text/html");  
            

            
            Multipart multipart = new MimeMultipart();  
            multipart.addBodyPart(messageBodyPart1);  
            
            message.setContent(multipart);

            Transport.send(message);

            //System.exit(0);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

	
	
	
	public String getUserFromToken1(HttpServletRequest request) {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;

		String jwtToken = null;

		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the
		// Token

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

			jwtToken = requestTokenHeader.substring(7);

			try {

				username = jwtTokenUtil.getUsernameFromToken(jwtToken);

			} catch (IllegalArgumentException e) {

				logger.info("Unable to get JWT Token");

			} catch (ExpiredJwtException e) {

				logger.info("JWT Token has expired");

			}

		} else {

			logger.info("JWT Token does not begin with Bearer String");

		}

		return username;
	}

	public void excecuteUpdateQuery(Session session, String qry) {
		Transaction tx = null;
		String query = "";
		try {
			tx = session.beginTransaction();
			query = " " + qry + " ";
			logger.info("ExcecuteUpdateQry........" + query);
			Query q = session.createSQLQuery(query);
			q.executeUpdate();
			tx.commit();

		} catch (HibernateException e) {
			Throwable cause = e.getCause();
			logger.info("Error while updating ...ExcecuteUpdateQry ....." + query);
			e.printStackTrace();
			tx.rollback();
			if (cause instanceof SQLException) {

			}
		}

	}

	public String getUserType(String uid) {
		int currentLine = 0;
		Gson gson = new Gson();
		org.json.simple.JSONObject return_obj = new JSONObject();
		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();

		try {

			String sql = "select user_type from lp.employee_master  where emp_code ='" + uid + "'";

			SQLQuery q = session.createSQLQuery(sql);

			currentLine = new Throwable().getStackTrace()[0].getLineNumber();

			logger.info("getUserDetails-Sql(CommonFunc.java:---" + currentLine);
			
			List<Map<String, Object>> query_data_obj = null;

			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					return_obj.put("usertype", row.get("user_type").toString());


				}

			}

		} catch (Exception e) {
		//	session.close();
			logger.info("Error in geting user type" + e.getMessage());
		}finally {
			session.close();
		}

	
		result.SetSuccess("Success", return_obj);
		return gson.toJson(result);

	}

	public String getUserTypeString(String uid) {

		factory = HybernetUtil.GetHyberNetSessionFactory();
		Session session = factory.openSession();
		String userType=null;

		try {
			
			String sql = "select user_type from lp.employee_master  where emp_code ='" + uid + "'";

			SQLQuery q = session.createSQLQuery(sql);


			
			List<Map<String, Object>> query_data_obj = null;

			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					userType = row.get("user_type").toString();

				}

			}

		} catch (Exception e) {
			session.close();
			logger.info("Error in geting user type" + e.getMessage());
		}finally {
			session.close();
		}


		
		return userType;

	}

	public String ExcecuteUpdateQryWithMsg(Session session, String qry) {
		Transaction tx = null;
		String query = "";
		String msg = "";
		
		try {
			tx = session.beginTransaction();
			query = " " + qry + " ";
			
			logger.info("ExcecuteUpdateQry........" + query);
			
			Query q = session.createSQLQuery(query);
			q.executeUpdate();
			tx.commit();

		} catch (HibernateException ex) {
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) {
				msg = cause.getMessage();
				logger.info("sqlErrorMsg..." + msg);

				ex.printStackTrace();

			}
			tx.rollback();
		}
		return msg;

	}

	public void ExcecuteUpdateQry(Session session, String qry) {
		Transaction tx = null;
		String query = "";
		try {
			tx = session.beginTransaction();
			query = " " + qry + " ";
			System.out.println("ExcecuteUpdateQry........" + query);
			Query q = session.createSQLQuery(query);
			q.executeUpdate();
			tx.commit();

		} catch (HibernateException e) {
			Throwable cause = e.getCause();
			System.out.println("Error while updating ...ExcecuteUpdateQry ....." + query);
			e.printStackTrace();
			tx.rollback();
			if (cause instanceof SQLException) {

			}
		}

	}

	public void sendMailforgetPassword(String passwordreal, String userId, String email)
			throws UnsupportedEncodingException {
		
		
		
		String from = mail_from;// change accordingly
		final String username = mail_username;// change accordingly
		final String password = mail_password;// change accordingly

		String from_name = "Partner Portal Admin";

		// Assuming you are sending email through relay.jangosmtp.net

		String host = "czismtp.logix.in";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from, from_name));
			
			message.setSubject("Forgot Password Retrieval ");

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

			String body = "Dear Sir,<br><br>" +
						  "It seems you have requested for the password retrieval from Weikfield Online Partner Portal. "+
						  "Please make a note of the same & <b>DO NOT SHARE WITH ANYONE.</b><br><br>"+
						  "YOUR PASSWORD:"+passwordreal+ "<br><br>"+
						  "In case you have not requested for password retrieval, highlight the same to Weikfield Partner Portal Support Team by sending mail to "+
						  "support.partner@weikfield.com<br><br>"+
						  "Best Regards <br>"+
						  "Partner Portal Admin";
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setContent(body, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);

			message.setContent(multipart);

			Transport.send(message);


		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
	
	

	public void sendTelegramMessage(String chat_id,String textMessage) {
		
		String callURL = String.format(tel_url, tel_token, chat_id, textMessage);
		
		try {
            URL url = new URL(callURL);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	public String logMemoryBytes() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        
        String bytes = "Memory(Bytes): Used=" + (totalMemory - freeMemory) + " Total=" + totalMemory + " Free=" + freeMemory;
        				//" Free(%)="+ (freeMemory / totalMemory ) *100 +"%";

        return bytes;
    }

	public String logMemoryGB() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1073741824;
        long freeMemory = runtime.freeMemory() / 1073741824;
        
        String gb = "Memory(Gb): Used=" + (totalMemory - freeMemory) + " Total=" + totalMemory + " Free=" + freeMemory;
        			

        return gb;
    }
	
	public String convertArrayToString(String[] arrayStrings) {
		String output = "";

		for (int i = 0; i < arrayStrings.length; i++)
		{
			arrayStrings[i] = "'" + arrayStrings[i] + "'";
		    output += arrayStrings[i] + ", ";
		}
		
		return output;
	}

}
