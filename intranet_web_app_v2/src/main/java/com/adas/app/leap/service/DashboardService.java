package com.adas.app.leap.service;


import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adas.app.leap.common.CommonFunc;
//import com.adas.app.leap.controller.GetDashBoardController;
import com.adas.app.leap.system.HybernetUtil;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import org.json.simple.*;

@Repository
public class DashboardService {

	@Autowired
	public CommonFunc utils;
	
	private  SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

	Logger logger = LoggerFactory.getLogger(DashboardService.class);
	
	public JSONObject getProfileDetails(String userId) {
		

		Session session = factory.openSession();

		// ArrayList<ProfileDetail> profile = new ArrayList<ProfileDetail>();
		JSONObject return_obj = new org.json.simple.JSONObject();


		try {

			String profileSql = "select emp_code,emp_name,coalesce(email_id,'na') email_id,"+
								"coalesce(phone_no,'0') phone_no,coalesce(address,'na') address,user_type "+
								"from lp.employee_master  where emp_code ='" + userId + "'";
			
			logger.info("ProfileDetails...." + profileSql);

			SQLQuery q = session.createSQLQuery(profileSql);
			
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					

					return_obj.put("user_id", row.get("emp_code").toString());
					return_obj.put("user_name", row.get("emp_name").toString());
					return_obj.put("email_id", row.get("email_id").toString());
					return_obj.put("user_phone", row.get("phone_no").toString());
					return_obj.put("address", row.get("address").toString());
					return_obj.put("user_type", row.get("user_type").toString());

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch(OutOfMemoryError e) {
			logger.info("Caught Out of Memory Issue:"+e.getMessage());
			System.gc();
		}finally {
			session.close();
			factory.close();
		}

		System.gc();
		Runtime.getRuntime().gc();
		
		
		return return_obj;

	}
	
	public JSONArray getMenuDetails(String usertype) {

		Session session = factory.openSession();
		// ArrayList<MenuDetail> menus = new ArrayList<MenuDetail>();
		
		JSONArray arrMenuLevel1 = new JSONArray();
		
		
		int menuMaxLevel=0;
		String menuSql1 = "";
		String menuSql2 = "";
		String menuSql3 = "";
		
		JSONArray arrMenuLevel2 = new JSONArray();
		JSONArray arrMenuLevel3 = new JSONArray();
		
		
		try {

			menuMaxLevel=getMaxMenuLevel(session,usertype);
			
			for (int i=0;i<=menuMaxLevel;i++) {
				menuSql1="select * from lp.menu_details where user_type='"+usertype+"' and menu_level='"+i+"'";
				
				logger.info("menuDetailsLevel1...." + menuSql1);
				
				SQLQuery q = session.createSQLQuery(menuSql1);
				List<Map<String, Object>> query_data_obj = null;
				
				if (q.list().size() > 0) {
				
					q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
					query_data_obj = q.list();
					
					for (Map<String, Object> row : query_data_obj) {
						JSONObject return_obj = new JSONObject();

						return_obj.put("menu_display_name", row.get("menu_display_name").toString());
						return_obj.put("menu_href", row.get("menu_href").toString());
						return_obj.put("menu_icon", row.get("menu_icon").toString());
						return_obj.put("menu_index", row.get("menu_index").toString());
						return_obj.put("menu_desc", row.get("menu_desc").toString());
						return_obj.put("id", row.get("menu_code").toString());
						return_obj.put("sub_menu", createMenuLevel2(session,usertype,Integer.parseInt(row.get("menu_int_code").toString())));
						
						

						arrMenuLevel1.add(return_obj);

					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			factory.close();
		}
		System.gc();
		Runtime.getRuntime().gc();


		return arrMenuLevel1;

	}
	
	public JSONArray createMenuLevel2(Session session,String userType,int menuIntCode) {
		
		String menuSql2 = "";
		
		JSONArray arrMenuLevel2 = new JSONArray();
		
		try {

				menuSql2="select * from lp.menu_details where user_type='"+userType+"' and ref_menu_int_code='"+menuIntCode+"'";
				
				logger.info("menuDetailsLevel2...." + menuSql2);
				
				SQLQuery q = session.createSQLQuery(menuSql2);
				List<Map<String, Object>> query_data_obj = null;
				
				if (q.list().size() > 0) {
				
					q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
					query_data_obj = q.list();
					
					for (Map<String, Object> row : query_data_obj) {
						JSONObject return_obj = new JSONObject();

						return_obj.put("menu_display_name", row.get("menu_display_name").toString());
						return_obj.put("menu_href", row.get("menu_href").toString());
						return_obj.put("menu_icon", row.get("menu_icon").toString());
						return_obj.put("menu_index", row.get("menu_index").toString());
						return_obj.put("menu_desc", row.get("menu_desc").toString());
						return_obj.put("id", row.get("menu_code").toString());

						arrMenuLevel2.add(return_obj);

					}
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		return arrMenuLevel2;
	}

	public int getMaxMenuLevel(Session session,String usertype) {
		
		int maxLevel = 0;
		try {
			
			String sql = "select max(menu_level) menu_max_lvl from lp.menu_details where user_type='"+usertype+"'";

			
			logger.info("getMaxMenuLevel...." + sql);

			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
		
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				
				for (Map<String, Object> row : query_data_obj) {
					
					maxLevel = Integer.parseInt(row.get("menu_display_name").toString());

				}

			}
			
			
			
		}catch(Exception e) {
			logger.info("Error in fetching max level:"+e.getMessage());
		}
		
		return maxLevel;
	}

}
