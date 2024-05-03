package com.adas.app.leap.service;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

import com.adas.app.leap.common.CommonFunc;
import com.adas.app.leap.system.HybernetUtil;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.json.simple.*;


@Repository
public class LoginPageService {
	@Autowired
	public CommonFunc utils;

	Logger logger = LoggerFactory.getLogger(LoginPageService.class);

	public JSONObject getLoginDetails() {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();
		
		JSONObject return_obj = new org.json.simple.JSONObject();

		
		try {
			String sql = "select * from lp.mst_co_setup";
			
			logger.info("getLoginDetails SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {

					return_obj.put("co_name", row.get("co_name").toString());
					return_obj.put("bg_image_path", row.get("co_bg_image_path").toString());
					return_obj.put("logo_path", row.get("co_logo_path").toString());
				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting SAP Group Name:"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}
		
		System.gc();
		Runtime.getRuntime().gc();
		
		return return_obj;

	}

	
	public JSONArray getChannelBI(String groupName) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();

		
		try {
			String sql = "select distinct bi_channel from app_central_v2.sap_customer_master where upper(group_name) in ("+groupName.toUpperCase()+") and bi_channel is not null";
			
			logger.info("getChannelBI SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
										

					jsonArray.add(row.get("bi_channel").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting BI Channel:"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}
		
		System.gc();
		Runtime.getRuntime().gc();		
		return jsonArray;

	}

	public JSONArray getSubChannelBI(String channel,String groupName) {
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "select distinct coalesce(bi_sub_channel,'NR') bi_sub_channel from app_central_v2.sap_customer_master "+
							"where bi_channel in ("+channel+") and group_name in ("+groupName+")";
			
			logger.info("getSubChannelBI SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					

					jsonArray.add(row.get("bi_sub_channel").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting BI Sub Channel:"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}

		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	

	public JSONArray getDistType(String subChannel,String channel,String groupName) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "select distinct coalesce(dist_type,'NR') dist_type from app_central_v2.sap_customer_master where "+
						 "coalesce(bi_sub_channel,'NR') in ("+subChannel+")";
			
			logger.info("getDistType SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					JSONObject return_obj = new org.json.simple.JSONObject();

					return_obj.put("dist_type", row.get("dist_type").toString());
					

					jsonArray.add(return_obj);

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Distributor Type"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}

		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	

	public JSONArray getZone(String groupName,String channel,String subChannel,String distType) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "SELECT distinct upper(bi_zone) bi_zone FROM app_central_v2.sap_customer_master where bi_zone is not null and card_type='C'";
			
			logger.info("getZone SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					JSONObject return_obj = new org.json.simple.JSONObject();

					return_obj.put("zone", row.get("bi_zone").toString());
					

					jsonArray.add(return_obj);

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Distributor Type"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}

		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	

	public JSONArray getState(String zone,String groupName,String channel,String subChannel,String distType) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "SELECT distinct bi_state state FROM app_central_v2.sap_customer_master where bi_state is not null and upper(bi_zone) in ("+zone.toUpperCase()+")";
			
			logger.info("getState SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					JSONObject return_obj = new org.json.simple.JSONObject();

					
					

					jsonArray.add(row.get("state").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Region"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}

		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	
	
	public JSONArray getRegion(String state,String zone,String groupName,String channel,String subChannel,String distType) {
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();		
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "SELECT distinct bi_region region FROM app_central_v2.sap_customer_master where bi_region is not null and upper(bi_state) in ("+state+")";
			
			logger.info("getRegion SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					JSONObject return_obj = new org.json.simple.JSONObject();

				
					

					jsonArray.add(row.get("region").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Region"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}

		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	
	
		
	public JSONArray getCity(String zone,String state,String region,String groupName,String channel,String subChannel,String distType) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();

		Session session = factory.openSession();		
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "SELECT distinct bi_city city FROM app_central_v2.sap_customer_master where upper(bi_zone) in ("+zone.toUpperCase()+") and " +
							" upper(bi_state) in ("+state.toUpperCase()+") and upper(bi_region) in ("+region.toUpperCase()+")";
			
			logger.info("getCity SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					

					
					

					jsonArray.add(row.get("city").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting city"+e.getMessage());
		}finally {
			session.close();
			factory.close();

		}

		System.gc();
		Runtime.getRuntime().gc();

		
		return jsonArray;

	}	
	
	
	public JSONArray getSelectedDBs(String channel,String subChannel,String distType,String zone,String state,String region,String city,
			int channelCount,int subChannelCount,int distTypeCount,int zoneCount,int stateCount,int regionCount,int cityCount,int groupNameCount,
			String groupName
			) {
		logger.info("inside");
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
		
		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		String strChannel = channel;
		String strSubChannel = subChannel;
		String strDistType = distType;
		
		String strZone = zone;
		String strState = state;
		String strRegion = region;
		String strCity = city;
		String strGroupName = groupName;
		
		String channelSQL = "like ('%')";;
		String subChannelSQL = "like ('%')";;
		String distTypeSQL = "like ('%')";;
		
		String zoneSQL = "like ('%')";
		String stateSQL = "like ('%')";
		String regionSQL = "like ('%')";
		String citySQL = "like ('%')";
		
		String groupNameSQL = "like ('%')";
		
		if(groupNameCount>0) {
			groupNameSQL = "in ("+strGroupName+")";
		}
		
		if(channelCount>0) {
			channelSQL = "in ("+strChannel+")";
		}

		if(subChannelCount>0) {
			subChannelSQL = "in ("+strSubChannel+")";
		}
		
		if(distTypeCount>0) {
			distTypeSQL = "in ("+strDistType+")";
		}
		
		if(zoneCount>0) {
			zoneSQL = "in ("+strZone+")";
		}
		
		if(stateCount>0) {
			stateSQL = "in ("+strState+")";
		}
		
		if(regionCount>0) {
			regionSQL = "in ("+strRegion+")";
		}
		
		if(cityCount>0) {
			citySQL = "in ('NR',"+strCity+")";
		}
		
		try {
			String sql = "select customer_code,customer_name,coalesce(dist_type,'NR') dist_type from app_central_v2.sap_customer_master where coalesce(bi_zone,'NR') "+zoneSQL+ " and " +
						  " coalesce(bi_state,'NR') "+stateSQL+" and coalesce(bi_region,'NR') "+regionSQL+" and coalesce(bi_city,'NR') "+citySQL+ " and " +
						  " coalesce(bi_channel,'NR') "+channelSQL+" and coalesce(bi_sub_channel,'NR') "+subChannelSQL+" and coalesce(dist_type,'NR') "+distTypeSQL+ " and coalesce(group_name,'NR') "+groupNameSQL;
			
			logger.info("getSelectedDBs SQL...." + sql);
		
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					JSONObject return_obj = new org.json.simple.JSONObject();

					return_obj.put("customer_code", row.get("customer_code").toString());
					return_obj.put("customer_name", row.get("customer_name").toString());
					return_obj.put("dist_type", row.get("dist_type").toString());
					
					
					
					

					jsonArray.add(return_obj);

				}

			}
			
		}catch(Exception e) {

			session.close();
			factory.close();
			logger.info("Error in getting DB details:"+e.getMessage());

			
		}finally {
			session.close();
			factory.close();
		}
		
		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	
	
	//Product Hierarchy from here
	
	public JSONArray getPackType(String itemGroupName) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
		
		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "select distinct customer_type  from app_central_v2.sap_item_master "+
						 "where customer_type is not null and upper(item_group_name) in ("+itemGroupName+") ";
			
			logger.info("getPackType SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					

					jsonArray.add(row.get("customer_type").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Pack Type"+e.getMessage());
		}finally {
			session.close();
			factory.close();
			
		}
		
		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	
	
	public JSONArray getItemGroupName() {
			
			SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
			
			Session session = factory.openSession();
			
			JSONArray jsonArray = new org.json.simple.JSONArray();
			
			try {
				String sql = "select distinct item_group_name from app_central_v2.sap_item_master where brand is not null order by item_group_name";
				
				logger.info("getItemGroupName SQL...." + sql);
				
				SQLQuery q = session.createSQLQuery(sql);
				List<Map<String, Object>> query_data_obj = null;
				if (q.list().size() > 0) {
					q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
					query_data_obj = q.list();
					for (Map<String, Object> row : query_data_obj) {
						
	
						jsonArray.add(row.get("item_group_name").toString());
	
					}
	
				}
				
			}catch(Exception e) {
				session.close();
				factory.close();
				logger.info("Error in getting Item Group Name:"+e.getMessage());
			}finally {
				session.close();
				factory.close();
				
			}
			
			System.gc();
			Runtime.getRuntime().gc();
			
			return jsonArray;
	
		}	
		
	
	public JSONArray getBrand(String itemGroupName) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
		
		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "select distinct brand from app_central_v2.sap_item_master "+
						 "where brand is not null and upper(item_group_name) in ("+itemGroupName+") "+
						 "order by brand";
			
			logger.info("getBrand SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					

					jsonArray.add(row.get("brand").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Brand Type"+e.getMessage());
		}finally {
			session.close();
			factory.close();
			
		}
		
		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}	
	
	
	@SuppressWarnings("unchecked")
	public JSONArray getProductFamily(String packType,String itemGroupName,String brand) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
		
		Session session = factory.openSession();
		
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		logger.info(brand);
		
		try {
			String sql = "select distinct product_family from app_central_v2.sap_item_master sim " +
						 "where product_family  is not null and "+
						 "customer_type in ("+packType+") and upper(item_group_name) in ("+itemGroupName+") and "+
						 "upper(brand) in ("+brand.toUpperCase().replace("'CHEF'", "'CHEF''")+") "+
						 "order by product_family";
			
			logger.info("getProductFamily SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					
					

					jsonArray.add(row.get("product_family").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Pack Type"+e.getMessage());
		}finally {
			session.close();
			factory.close();
			
		}
		
		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}
	
	
	@SuppressWarnings("unchecked")
	public JSONArray getProductLine(String packType,String brand, String productFamily,String itemGroupName) {
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
		
		Session session = factory.openSession();
		
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		try {
			String sql = "select distinct product_line from app_central_v2.sap_item_master "+
						 "where upper(customer_type) in ("+packType.toUpperCase()+") and "+
						 "upper(brand) in ("+brand.toUpperCase().replace("'CHEF'", "'CHEF''")+") and "+
						 "upper(product_family) in ("+productFamily.toUpperCase()+") and "+
						 "upper(item_group_name) in ("+itemGroupName.toUpperCase()+") and product_line is not null "+
						 "order by product_line";
			
			logger.info("getProductLine SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			
			List<Map<String, Object>> query_data_obj = null;
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					
					

					jsonArray.add(row.get("product_line").toString());

				}

			}
			
		}catch(Exception e) {
			session.close();
			factory.close();
			logger.info("Error in getting Product Line"+e.getMessage());
		}finally {
			session.close();
			factory.close();
			
		}
		
		System.gc();
		Runtime.getRuntime().gc();
		
		return jsonArray;

	}

	@SuppressWarnings("unchecked")
	public JSONArray getSelectedSKU(String pack_type,String brand,String product_line,String itemGroupName)
		{
		
		SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
		
		Session session = factory.openSession();
		
		JSONArray jsonArray = new org.json.simple.JSONArray();
		
		logger.info(brand);
		
		try {
			String sql = "select parent_code,parent_code_description,item_code,item_name,brand,product_line,customer_type  from app_central_v2.sap_item_master "+
						 "where upper(brand) in ("+brand.toUpperCase().replace("'CHEF'", "'CHEF''")+") and "+
						 "upper(customer_type) in ("+pack_type.toUpperCase()+") and "+
						 "upper(product_line) in ("+product_line.toUpperCase()+") and "+
						 "upper(item_group_name ) in ("+itemGroupName.toUpperCase()+") "+
						 " and parent_code!=''";
			
			logger.info("getSelectedSKUs SQL...." + sql);
			
			SQLQuery q = session.createSQLQuery(sql);
			List<Map<String, Object>> query_data_obj = null;
			
			if (q.list().size() > 0) {
				q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				query_data_obj = q.list();
				for (Map<String, Object> row : query_data_obj) {
					JSONObject return_obj = new org.json.simple.JSONObject();

					return_obj.put("parent_code", row.get("parent_code").toString());
					return_obj.put("parent_code_desc", row.get("parent_code_description").toString());
					return_obj.put("item_code", row.get("item_code").toString());
					return_obj.put("item_desc", row.get("item_name").toString());
					return_obj.put("brand", row.get("brand").toString());
					return_obj.put("product_line", row.get("product_line").toString());
					return_obj.put("pack_type", row.get("customer_type").toString());
					
					

					jsonArray.add(return_obj);

				}

			}
			
		}catch(Exception e) {

			session.close();
			factory.close();
			logger.info("Error in getting DB details:"+e.getMessage());

			
		}finally {
			session.close();
			factory.close();
		}
		
		return jsonArray;

	}	
	
	
	public List<Map<String, Object>> saveTemplate(String created_uid,String template_short_desc,String template_detail_desc,
			String period_from,String period_to,String ckey,String data,String itemData,String selectedProps,int defFlag)
	{
	
	SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
	
	Session session = factory.openSession();
	
	Transaction tx = session.beginTransaction();
	
	List<Map<String, Object>> query_data_obj = null;
	
	
	try {
		
		tx.begin();
		
		String sql = "select * from app_central_v2.proc_insert_new_template('"+created_uid+"','"+template_short_desc+"',"+
					"'"+template_detail_desc+"','"+period_from+"','"+period_to+"','"+ckey+"','"+data+"','"+itemData+"','"+selectedProps+"','"+defFlag+"');";
		
		logger.info("Save Customer SQL...." + sql);
		
		SQLQuery q = session.createSQLQuery(sql);
		String errorCode = "1";

		q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		query_data_obj = q.list();

		for (Map<String, Object> row : query_data_obj) {
			errorCode = row.get("error_code").toString();
		}

		if (errorCode.equals("0")) {
			tx.commit();
		} else {
			tx.rollback();
		}

		logger.info("Completed saving:");
		
	}catch(Exception e) {

		session.close();
		factory.close();
		logger.info("Error in Saving Customer details:"+e.getMessage());

		
	}finally {
		session.close();
		factory.close();
	}
	System.gc();
	Runtime.getRuntime().gc();
	
	return query_data_obj;

}	
	
	public List<Map<String, Object>> validateTemplate(String period_from,String period_to,String ckey,String data,String itemData)
	{
	
	SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
	
	Session session = factory.openSession();
	
	Transaction tx = session.beginTransaction();
	
	List<Map<String, Object>> query_data_obj = null;
	
	
	try {
		
		tx.begin();
		
		String sql = "select * from app_central_v2.proc_insert_bpc_validate('"+period_from+"','"+period_to+"','"+ckey+"','"+data+"','"+itemData+"');";
		
		logger.info("Validate New Catalog submission SQL...." + sql);
		
		SQLQuery q = session.createSQLQuery(sql);
		String errorCode = "1";

		q.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		query_data_obj = q.list();

		for (Map<String, Object> row : query_data_obj) {
			errorCode = row.get("error_code").toString();
		}

		if (errorCode.equals("0") || errorCode.equals("2")) {
			tx.commit();
		} else {
			tx.rollback();
		}

		
		
	}catch(Exception e) {

		session.close();
		factory.close();
		logger.info("Error in Validating Catalog:"+e.getMessage());

		
	}finally {
		session.close();
		factory.close();
	}
	System.gc();
	Runtime.getRuntime().gc();
	
	return query_data_obj;

}	
	
}
