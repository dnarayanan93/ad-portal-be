package com.adas.app.leap.controller;
import java.io.Serializable;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adas.app.leap.common.CommonFunc;
import com.adas.app.leap.common.ResponseDataGeneric;
import com.adas.app.leap.service.LoginPageService;

import com.adas.app.leap.system.HybernetUtil;
import com.google.gson.Gson;

@RestController
@RequestMapping(path = "/login")
public class LoginPageController {
	@Autowired
	ResponseDataGeneric responseDataGeneric;

	
	@Autowired
	private LoginPageService loginPageService;
		
	@Autowired
	private CommonFunc utils;
	
	private SessionFactory factory = HybernetUtil.GetHyberNetSessionFactory();
	
	Logger logger = LoggerFactory.getLogger(LoginPageController.class);


	
	@RequestMapping(value = "/pageDetails", method = RequestMethod.GET)
	public ResponseDataGeneric<JSONObject> getLoginPageDetails(HttpServletRequest request)
			throws SQLException, ClassNotFoundException {
		
		JSONObject return_obj = new JSONObject();
		ResponseDataGeneric<JSONObject> result = new ResponseDataGeneric<JSONObject>();
		Gson gson = new Gson();

		
		String userId = utils.getUserFromToken(request);
		
		try {
			return_obj.put("login_page_details",loginPageService.getLoginDetails());
		
		
			logger.info("Full object :" +return_obj );
		}catch(Exception e) {
			
			logger.info("Error in Returning getGroupName"+e.getMessage());
		}
		

		
		result.SetSuccess("Success", return_obj);

		return result;
	}
	

}
