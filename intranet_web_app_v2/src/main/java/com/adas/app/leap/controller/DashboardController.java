package com.adas.app.leap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.adas.app.leap.common.CommonFunc;
import com.adas.app.leap.common.ResponseDataGeneric;

import com.adas.app.leap.service.DashboardService;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {
	@Autowired
	ResponseDataGeneric responseDataGeneric;


	@Autowired
	private CommonFunc utils;
	
	@Autowired
	DashboardService dasboardService;
	
	static Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getDashboardDetails", method = RequestMethod.POST)
	public String getdashBoardDetails(HttpServletRequest request, @RequestBody JSONObject jsonRequest)
			throws ClassNotFoundException {
		
		JSONObject return_obj = new JSONObject();
		ResponseDataGeneric result = new ResponseDataGeneric();
		
		Gson gson = new Gson();
		
		String usertype = jsonRequest.get("usertype").toString();
		
		
		String userId = utils.getUserFromToken(request);
		
		
		return_obj.put("profile_details",dasboardService.getProfileDetails(userId));
		return_obj.put("menu_details",dasboardService.getMenuDetails(usertype));
		
		logger.info("Full object :" +return_obj );
		
		result.SetSuccess("Success", return_obj);
		return gson.toJson(result);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getUserType")
	public @ResponseBody String getusertypeData(HttpServletRequest request){
		
		JSONObject return_obj = new JSONObject();
		
		ResponseDataGeneric result = new ResponseDataGeneric();

		Gson gson = new Gson();

		String userId = utils.getUserFromToken(request);

		return_obj.put("userType",utils.getUserTypeString(userId));
		
		result.SetSuccess("Success", return_obj);
		return gson.toJson(result);
	}
}
