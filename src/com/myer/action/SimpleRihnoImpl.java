package com.myer.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.Scriptable;



/**
 * Demonstrates how to use Rhino to execute a simple Hello, Rhino JavaScript
 * @author shaines
 */
public class SimpleRihnoImpl
{
    public String executeHelloRhino()
    {
        // Create and enter a Context. A Context stores information about the execution environment of a script.
        Context cx = Context.enter();

        try
        {
            // Initialize the standard objects (Object, Function, etc.). This must be done before scripts can be
            // executed. The null parameter tells initStandardObjects 
            // to create and return a scope object that we use
            // in later calls.
//        	Writer writer = response.getWriter();;
            Scriptable scope = cx.initStandardObjects();

//             final ScriptEngine engine;
//             this.engine = factory.getEngineByName("JavaScript");
//             
//             this.engine.put("Java6RhinoRunner", this);
//             this.engine.eval("function load(filename) { Java6RhinoRunner.load(filename); }");
             
            
            File scriptFile = new File("C:/Sankar/Workspace1/TestWebProject/src/com/myer/action/dust-loader.js");
            if (!scriptFile.exists()) {
                System.err.println("Script file '%s' does not exist.");
//                System.exit(1);
            }
            // Read in the JavaScript file.
            byte[] buffer = new byte[512];
            StringBuilder sb = new StringBuilder();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(scriptFile), 10512);
            int bytesRead = bis.read(buffer);
            while (bytesRead != -1) {
                sb.append(new String(buffer));
                bytesRead = bis.read(buffer);
            }
            String renderScript = sb.toString();
            
            System.out.println( "script: " + renderScript.trim() );
            // Build the script
//            String renderScript =
//                ("{   dust.render( name,  JSON.parse(json) , " +
//                		"function( err, data) { if(err) { writer.write(err);} else { writer.write( data );}  } );   }");
             renderScript = "var s = 'Hello, Rhino'; s;";
            String json ="<response>Success</response>";
//            String name = "TestScript";
//            String script = "";
//            scope.put("writer", scope, writer);
            scope.put("json", scope, json);
//            scope.put("name", scope, name);
            // Execute the script
            
            Object obj = cx.evaluateString( scope, renderScript.trim(), "TestScript", 1, null );
            System.out.println( "Object: " + obj );

            // Cast the result to a string
            return ( String )obj;

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            // Exit the Context. This removes the association between the Context and the current thread and is an
            // essential cleanup action. There should be a call to exit for every call to enter.
            Context.exit();
        }
        return null;
    }
}