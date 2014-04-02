package com.myer.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class HelloWorldForm extends ActionForm {
	private static final long serialVersionUID = -473562596852452021L;

	private String message;
	private List employeeList;
	private List lists = new ArrayList();
	private Map  maps = new HashMap ();
	public String getMessage() {

	return message;

	}

	public void setMessage(String message) {

	this.message = message;

	}

	public List getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List employeeList) {
		this.employeeList = employeeList;
	}

	public List getLists() {
		return lists;
	}

	public void setLists(List lists) {
		this.lists = lists;
	}

	public Map getMaps() {
		return maps;
	}

	public void setMaps(Map maps) {
		this.maps = maps;
	}
}
