package com.myer.action;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;




 
public class DustEngine
{
  public static final String MODULE             = DustEngine.class.getName();
  private static Scriptable  globalScope;
 
  public DustEngine(InputStream dustStream)
  {
    try
    {
      Reader dustReader = new InputStreamReader(dustStream, "UTF-8");
      Context dustEngineContext = Context.enter();
      dustEngineContext.setOptimizationLevel(9);
      try
      {
        globalScope = dustEngineContext.initStandardObjects();
    	Map model = new HashMap ();
    	model.put("firstname", "jeff");
    	model.put("lastname", "jeff");
    	model.put("balance", "10");
//		globalScope.put("basePath", globalScope, basePath);
		globalScope.put("modelMap", globalScope, model);
	
        dustEngineContext.evaluateReader(globalScope,
                                         dustReader,
                                         "dust",
                                         0,
                                         null);
      }
      finally
      {
        Context.exit();
      }
    }
    catch (IOException ex)
    {
      throw new RuntimeException(" ERROR : Unable to load dust engine resource: ", ex);
    }
  }
  
  public static String compileTemplate(String name, String rawSource)
  {
    Context dustContext = Context.enter();
    try
    {
      Scriptable compileScope = dustContext.newObject(globalScope);
      compileScope.setParentScope(globalScope);
      compileScope.put("rawSource", compileScope, rawSource);
      compileScope.put("name", compileScope, name);
 
      try
      {
        return (String) dustContext.evaluateString(compileScope,
                                                           "(dust.compile(rawSource, name))",
                                                           "JDustCompiler",
                                                           0,
                                                           null);
      }
      catch (JavaScriptException e)
      {
        // Fail hard on any compile time error for dust templates
        throw new RuntimeException(e);
      }
    }
    finally
    {
      Context.exit();
    }
  }
 
 
  public static void loadTemplate(String name, String rawSource)
  {
 
    Context dustContext = Context.enter();
    try
    {
      Scriptable compileScope = dustContext.newObject(globalScope); 
      compileScope.setParentScope(globalScope);
      compileScope.put("rawSource", compileScope, rawSource);
      compileScope.put("name", compileScope, name);
 
      try
      {
        dustContext.evaluateString(compileScope,
                                                           "(dust.loadSource(dust.compile(rawSource, name)))",
                                                           "JDustCompiler",
                                                           0,
                                                           null);
      }
      catch (JavaScriptException e)
      {
        // Fail hard on any compile time error for dust templates
        throw new RuntimeException(e);
      }
    }
    finally
    {
      Context.exit();
    }
  }
  public static void render2(String name, String json, Writer writer)
  {
    Context dustContext = Context.enter();
 
    Scriptable renderScope = dustContext.newObject(globalScope);
    renderScope.setParentScope(globalScope);
 
    String renderScript =
        ("{   dust.stream( name,  {});   }");
 
    
//    String renderScript =
//        ("{   dust.stream( name,  {} , function( err, data) { if(err) { writer.write(err);} else { writer.write( data );}  } );   }");
// 
    try
    {
      renderScope.put("writer", renderScope, writer);
//      renderScope.put("json", renderScope, json);
      renderScope.put("name", renderScope, name);
 
    String s = ((NativeObject)  dustContext.evaluateString(renderScope,
                                       renderScript,
                                       "JDustCompiler",
                                       0,
                                       null)).toString();
 System.out.println("output" + s);
    }
    catch (JavaScriptException e)
    {
      // Fail hard on any render time error for dust templates
      throw new RuntimeException(e);
    }
    finally
    {
      Context.exit();
    }
  }
  public static String render(String name, String json, Writer writer, String s)
  {
    Context dustContext = Context.enter();
    Context dustEngineContext = Context.enter();
    dustEngineContext.setOptimizationLevel(9);
//    globalScope = dustEngineContext.initStandardObjects();
    Scriptable renderScope = dustEngineContext.newObject(globalScope);
    renderScope.setParentScope(globalScope);
//      Context dustEngineContext = Context.enter();
//      dustEngineContext.setOptimizationLevel(9);
//      Scriptable compileScope = dustEngineContext.newObject(globalScope);
//      compileScope.setParentScope(globalScope);
//  	JSONObject obj = new JSONObject();
//	try {
////		obj.put("name", "mkyong.com");
////		obj.put("age", new Integer(100));
////		json = obj.toString();
//	} catch (JSONException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}

	
//      compileScope.put("name", compileScope, name);
//      compileScope.put("writer", compileScope, writer);
//      compileScope.put("json", compileScope, json);
//    String renderScript =
//        "(   dust.render( name,  JSON.parse(json) , function( err, data) { if(err) { writer.write(err);} else { writer.write( data );}  } )   )";
//        
    try
    {  renderScope.delete("name");
    renderScope.put("name", renderScope, name);
      renderScope.put("writer", renderScope, writer);
      renderScope.put("json", renderScope, json);
//     String renderScript = "var s = 'Hello, Rhino'; s;";
//     renderScript = "( dust.render( name, {} , function( err, data) { if(err) {  writer.write(err);} else {  writer.println(data) ;}  } ))";     
    	String output ="";
    	name ="intro";
    	
    	
    	output = (String)  dustEngineContext.evaluateString(renderScope,
                                       "( dust.render( name, {} , function( err, data) { if(err) {  writer.write(err);} else {  writer.println(data) ;}  } ))",
                                       "JDustCompiler",
                                       0,
                                       null).getClass().getName();
//    	output = (String)  dustEngineContext.evaluateString(renderScope,
//    			renderScript,
//                "JDustCompiler",
//                0,
//                null);
    	
    	
    
 return output;
    }
    catch (JavaScriptException e)
    {
      // Fail hard on any render time error for dust templates
      throw new RuntimeException(e);
    } 
    finally
    {
      Context.exit();
    }
  }
 
  public static void testLoad(){
			  String script = "function abc(x,y) {return x+y;}"
			      + "function def(u,v) {return u-v;}";
			Context context = Context.enter();
			try {
			  ScriptableObject scope = context.initStandardObjects();
			  context.evaluateString(scope, script, "script", 1, null);
			  Function fct = (Function)scope.get("abc", scope);
			  Object result = fct.call(context, scope, scope, new Object[] {new Integer("2"), new Integer("3")});
			  System.out.println(Context.jsToJava(result, int.class));
			} finally {
			  Context.exit();
			}
  }
}