<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<html>
<head>
  <script src="js/dust-full-1.2.0.js"></script>
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

	
<script>
		
(function($){
	
  var data = {
    "name": "SOME NAME",
    "favorite_food": 1,
    "food_options": [
	    
      {id:1, value: "Ice Cream"},
      {id:2, value: "Pizza"},
      {id:3, value: "Fish"}
     ]
  };
  
	// Set up and compile the Dust.js template
	var foodTpl = dust.compile($("#food-tpl").html(),"foodTpl");
	dust.loadSource(foodTpl);
	
  
  /*
  // you could render the template like this
  
  dust.render("foodTpl", data, function(err, res){
    $("#main").append(res);
  });
  
  */
  
  // Or, if you use backbone, you could create a template function
  // to pass into a Backbone.View (as the 'template' param
  var template = function(foodData) {
		var result;
		dust.render("foodTpl", foodData, function(err, res) {
			result = res;
		});
		return result;
	};
  
  // render the template
  $("#main").append(template(data));
	
})(jQuery);
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Demo</title>
</head>
<body>
<bean:write name="helloWorldForm" property="message"/>
<bean:write name="helloWorldForm" property="lists"/>

<c:out value="${message}">  
</c:out>  
 
<div id="main"></div>
</body>
<script type="text/x-template" id="food-tpl">
	<h1>{name|j}</h1>
	<b>{favorite_food}</b>
  <select>
    {#food_options}
      <option value="{id}">{id} - {value}</option>
    {/food_options}
  </select>
</script>


<script>
		
(function($){
	
  var data = {
    "name": "SOME NAME",
    "favorite_food": 1,
    "food_options": [
	    
      {id:1, value: "Ice Cream"},
      {id:2, value: "Pizza"},
      {id:3, value: "Fish"}
     ]
  };
  
	// Set up and compile the Dust.js template
	var foodTpl = dust.compile($("#food-tpl").html(),"foodTpl");
	dust.loadSource(foodTpl);
	
  
  /*
  // you could render the template like this
  
  dust.render("foodTpl", data, function(err, res){
    $("#main").append(res);
  });
  
  */
  
  // Or, if you use backbone, you could create a template function
  // to pass into a Backbone.View (as the 'template' param
  var template = function(foodData) {
		var result;
		dust.render("foodTpl", foodData, function(err, res) {
			result = res;
		});
		return result;
	};
  
  // render the template
  $("#main").append(template(data));
	
})(jQuery);
	
</script>
</html>