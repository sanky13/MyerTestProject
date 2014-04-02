package com.myer.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.MarkUp;
import net.sourceforge.wurfl.core.WURFLHolder;
import net.sourceforge.wurfl.core.WURFLManager;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.myer.demo.CreateJson;
import com.myer.demo.HelloWorldForm;

public class TestActionClass extends Action {
	private ClassLoader classLoader;
	 private static DustEngine2 engineworking;
	   private static final long serialVersionUID = 10L;
	   
	    private static final String XHTML_ADV = "xhtmladv.jsp";
	    private static final String XHTML_SIMPLE = "xhtmlmp.jsp";
	    private static final String CHTML = "chtml.jsp";
	    private static final String WML = "wml.jsp";
//		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			processRequest(request, response);
//		}
//		protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//			WURFLHolder wurfl = (WURFLHolder) getServletContext().getAttribute(WURFLHolder.class.getName());
//			
//			WURFLManager manager = wurfl.getWURFLManager();
//
//			Device device = manager.getDeviceForRequest(request);
//			
//			log.debug("Device: " + device.getId());
//			log.debug("Capability: " + device.getCapability("preferred_markup"));
//
//		}

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	 HttpSession session = request.getSession();
//	WURFLEngine engine = (WURFLEngine)session.getServletContext().getAttribute(WURFLEngine.class.getName());      
	
	WURFLHolder wurfl = (WURFLHolder) session.getServletContext().getAttribute(WURFLHolder.class.getName()); 
	WURFLManager manager = wurfl.getWURFLManager();
	//
				Device device = manager.getDeviceForRequest(request);
//    Device device = engine.getDeviceForRequest(request);
				
   System.out.println("device" + device.getId());
    System.out.println("Capability: " + device.getCapability("preferred_markup"));
    
    MarkUp markUp = device.getMarkUp();
    
    String jspView = null; 

    if (MarkUp.XHTML_ADVANCED.equals(markUp)) {
        jspView = XHTML_ADV;
    } else if (MarkUp.XHTML_SIMPLE.equals(markUp)) {
        jspView = XHTML_SIMPLE;
    } else if (MarkUp.CHTML.equals(markUp)) {
        jspView = CHTML;
    } else if (MarkUp.WML.equals(markUp)) {
        jspView = WML;
    }


    System.out.println("View: " + jspView);
    
	HelloWorldForm hwForm = (HelloWorldForm) form;
	
	 List emploeelists = new ArrayList();
	 emploeelists.add("list1");
	 emploeelists.add("list2");
	 emploeelists.add("list3");
	 emploeelists.add("list4");
	 emploeelists.add("list5");
	
		classLoader = getClass().getClassLoader();
//		URL dust = options.getDust();
//		classLoader = Thread.currentThread().getContextClassLoader();

		URL envs = classLoader.getResource("C:/Sankar/Workspace1/TestWebProject/src/com/myer/action/env.js");
		URL engines = classLoader.getResource("/engine.js");
	hwForm.setLists(emploeelists);
	hwForm.setMessage("Demo for Struts to Dust integration");
	PrintWriter outres = response.getWriter();
	String sb ="<response>Success</response>";
	response.setContentType("text/html");
	response.setContentLength(sb.length()); 
	
	// Using Rhino//
	SimpleRihnoImpl sr = new SimpleRihnoImpl();
	sb = sr.executeHelloRhino();
	

	// USing Dust

	PrintWriter writer = response.getWriter(); 
//	 converts a map to json using jackson json library
	//DustEngine.render("dust", "Json", writer);

//    String renderScript = renderjs("C:/Sankar/Workspace1/TestWebProject/src/com/myer/action/template.js");
    
    File scriptFile = new File("C:/Sankar/Workspace1/TestWebProject/src/com/myer/action/dust-full-2.0.0.min.js");
    DustEngine DSE = new DustEngine( new FileInputStream(scriptFile));
//    scriptFile = new File("C:/Sankar/Workspace1/TestWebProject/src/com/myer/action/json.js");
//    DSE = new DustEngine( new FileInputStream(scriptFile));
//    System.out.println( "script: " + renderScript.trim() );
//    String ss= DustEngine.compileTemplate("template", renderScript.trim());
//    System.out.println("check " + ss.toString());
	Map model = new HashMap ();
	model.put("firstname", "jeff");
	model.put("lastname", "jeff");
	model.put("balance", "10");
//	String s1= DustEngine.compileTemplate("intro", "Hello {name}!");
	String s1= DustEngine.compileTemplate("intro", "Hello World");
	System.out.println("check " + s1.toString());
	
//	DustEngine.loadTemplate("intro", s1);
//	DustEngine.loadTemplate("dust", renderScript.trim());
//	DustEngine.render2("intro", model.toString(), out); 
//	System.out.println("checking " + s.toString());
	
	DustEngine.testLoad();
	Map m = new HashMap ();
//	 ...
//	ScriptEngineManager manager3 = new ScriptEngineManager();
//	ScriptEngine engine3 = manager3.getEngineByName("js");
//	CompiledScript script = (CompiledScript) m.get("fib");
//	if(script == null) {
//		Compilable compilingEngine = (Compilable)engine;
//		script = compilingEngine.compile(
//				"fib(num);" +
//				"function fib(n) {" +
//				"  if(n <= 1) return n; " +
//				"  return fib(n-1) + fib(n-2); " +
//				"};"		
//		);
//		m.put("fib", script);
//	}
//		Bindings bindings = engine3.createBindings();
//		bindings.put("num", "20");
//		Object result = script.eval(bindings);
//		System.out.println(result);
		DustOptions options = new DustOptions();
		engineworking = new DustEngine2(options);
		engineworking.load("Hello {name}! You have {count} new messages.", "test");
		  
        CreateJson createJson = new CreateJson();
        String jsonString = createJson.create();
        System.out.println("jsonString :" + jsonString);
        
        
		String x = engineworking.render("test", jsonString);
//		outres.print("<response>Success</response>"); 
		
	    String contentype = "text";
	    if("google_bot".equalsIgnoreCase(device.getId())) {
	    	contentype ="text/html";
	    	x= jsonString;
	    } 
		response.setContentType(contentype);   
	    
		PrintWriter out;
		 out = response.getWriter();
		try {
		        out = response.getWriter();
		     out.println(x.toString()); 
		     out.close();
		     out.flush();
		} catch (IOException e) {
		     
	}
		return mapping.findForward("success");
//
}

public String renderjs(String path) throws IOException {
//	String stringJs = null;
    File scriptFile = new File(path);
    if (!scriptFile.exists()) {
        System.err.println("Script file '%s' does not exist.");
//        System.exit(1);
    }
    // Read in the JavaScript file.
    byte[] buffer = new byte[512];
    StringBuilder sb1 = new StringBuilder();
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(scriptFile), 10512);
    int bytesRead = bis.read(buffer);
    while (bytesRead != -1) {
        sb1.append(new String(buffer));
        bytesRead = bis.read(buffer);
    }
	return sb1.toString();
}
}