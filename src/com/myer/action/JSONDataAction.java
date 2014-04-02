package com.myer.action;


	import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.myer.demo.HelloWorldForm;
	 

	 
	public class JSONDataAction extends Action {
	 
		private String string1 = "A";
		private String[] stringarray1 = {"A1","B1"};
		private int number1 = 123456789;
		private int[] numberarray1 = {1,2,3,4,5,6,7,8,9};
		private List lists = new ArrayList();
		private Map  maps = new HashMap ();
	 
		//no getter method, will not include in the JSON
		public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

			HelloWorldForm hwForm = (HelloWorldForm) form;

			hwForm.setMessage("Demo for Struts to Dust integration");

			return mapping.findForward("success");
		}
	 
		public JSONDataAction(){
			lists.add("list1");
			lists.add("list2");
			lists.add("list3");
			lists.add("list4");
			lists.add("list5");
	 
			maps.put("key1", "value1");
			maps.put("key2", "value2");
			maps.put("key3", "value3");
			maps.put("key4", "value4");
			maps.put("key5", "value5");
		}
	 
		public String execute() {
			System.out.println("sankar");
	               return "SUCCESS";
	        }
	 
		public String getString1() {
			return string1;
		}
	 
		public void setString1(String string1) {
			this.string1 = string1;
		}
	 
		public String[] getStringarray1() {
			return stringarray1;
		}
	 
		public void setStringarray1(String[] stringarray1) {
			this.stringarray1 = stringarray1;
		}
	 
		public int getNumber1() {
			return number1;
		}
	 
		public void setNumber1(int number1) {
			this.number1 = number1;
		}
	 
		public int[] getNumberarray1() {
			return numberarray1;
		}
	 
		public void setNumberarray1(int[] numberarray1) {
			this.numberarray1 = numberarray1;
		}
	 
		public List  getLists() {
			return lists;
		}
	 
		public void setLists(List  lists) {
			this.lists = lists;
		}
	 
		public Map  getMaps() {
			return maps;
		}
	 
		public void setMaps(Map maps) {
			this.maps = maps;
		}
}
