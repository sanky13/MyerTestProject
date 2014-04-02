package com.myer.action;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import com.myer.demo.CreateJson;

public class DustEngine2 {

	private final Log logger = LogFactory.getLog(getClass());

	private Scriptable scope;
	private ClassLoader classLoader;
	private Function compileString;
    private Function compileFile;
    private Function loadString;
    private Function renderString;
	public DustEngine2() {
		this(new DustOptions());
	}

	public DustEngine2(DustOptions options) {
		try {
			logger.debug("Initializing dust Engine.");
			classLoader = getClass().getClassLoader();
			URL dust = options.getDust();
			classLoader = Thread.currentThread().getContextClassLoader();

			URL env = classLoader.getResource("env.js");
			URL engine = classLoader.getResource("engine.js");
			URL json = classLoader.getResource("jsonparse.js");
			Context cx = Context.enter();
			logger.debug("Using implementation version: " + cx.getImplementationVersion());
			cx.setOptimizationLevel(9);
			Global global = new Global();
			global.init(cx);
			scope = cx.initStandardObjects(global);
			cx.evaluateReader(scope, new InputStreamReader(env.openConnection().getInputStream()), env.getFile(), 1, null);
			cx.evaluateString(scope, "dustenv.charset = '" + options.getCharset() + "';", "charset", 1, null);
			cx.evaluateReader(scope, new InputStreamReader(dust.openConnection().getInputStream()), dust.getFile(), 1, null);
			cx.evaluateReader(scope, new InputStreamReader(json.openConnection().getInputStream()), json.getFile(), 1, null);
			cx.evaluateReader(scope, new InputStreamReader(engine.openConnection().getInputStream()), engine.getFile(), 1, null);

			compileString = (Function) scope.get("compileString", scope);
            compileFile = (Function) scope.get("compileFile", scope); 
            loadString = (Function) scope.get("loadString", scope); 
            renderString = (Function) scope.get("renderString", scope); 
			Context.exit();
		} catch (Exception e) {
			logger.error("Dust engine initialization failed.", e);
		}
	}

    public String compile(String source, String templateName) {
        long time = System.currentTimeMillis();
        String result = call(compileString, new Object[] {source, templateName});
        logger.debug("The compilation of '" + source + "' took " + (System.currentTimeMillis () - time) + " ms.");
        return result;
    }
	
	public String compile(File input) {
        long time = System.currentTimeMillis();
        logger.debug("Compiling File: " + "file:" + input.getAbsolutePath());
        String templateName = input.getName().split("\\.")[0];
        String result = call(compileFile, new Object[]{input, templateName});
        logger.debug("The compilation of '" + input + "' took " + (System.currentTimeMillis () - time) + " ms.");
        return result;
	}
	
	public void load(String source, String templateName) {
        long time = System.currentTimeMillis();
       callvoid(loadString, new Object[] {source, templateName});
        logger.debug("The compilation of '" + source + "' took " + (System.currentTimeMillis () - time) + " ms.");
       
	}
	
	public String render(String templateName, String jsonString) {
        long time = System.currentTimeMillis();
        PrintWriter pw = new PrintWriter(System.out, true);

       String x = call(renderString, new Object[] { templateName, pw, jsonString});
       System.out.println("output :" + x);
        logger.debug("The compilation of '" +  "' took " + (System.currentTimeMillis () - time) + " ms.");
        return x;
	}
	
	
	public void compile(File input, File output) {
		try {
			String content = compile(input);
			if (!output.exists()) {
				output.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private synchronized String call(Function fn, Object[] args) {
		return (String) Context.call(null, fn, scope, scope, args);
	}
	
	private synchronized void callvoid(Function fn, Object[] args) {
	Context.call(null, fn, scope, scope, args);
	}
	
	
	public static void main(String[] args) throws URISyntaxException {
		Options cmdOptions = new Options();
		cmdOptions.addOption(DustOptions.CHARSET_OPTION, true, "Input file charset encoding. Defaults to UTF-8.");
		cmdOptions.addOption(DustOptions.DUST_OPTION, true, "Path to a custom dust.js for Rhino version.");
		try {
			CommandLineParser cmdParser = new GnuParser();
			CommandLine cmdLine = cmdParser.parse(cmdOptions, args);
			DustOptions options = new DustOptions();
			if (cmdLine.hasOption(DustOptions.CHARSET_OPTION)) {
				options.setCharset(cmdLine.getOptionValue(DustOptions.CHARSET_OPTION));
			}
			if (cmdLine.hasOption(DustOptions.DUST_OPTION)) {
				options.setDust(new File(cmdLine.getOptionValue(DustOptions.DUST_OPTION)).toURI().toURL());
			}
			options.setDust(new File(cmdLine.getOptionValue(DustOptions.DUST_OPTION)).toURI().toURL());
			DustEngine2 engine = new DustEngine2(options);
			String[] files = cmdLine.getArgs();
			files = new String[] {"template.js"};
            String src = null;
            if (files == null  || files.length == 0) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                StringWriter sw = new StringWriter();
                char[] buffer = new char[1024];
                int n = 0;
                while (-1 != (n = in.read(buffer))) {
                    sw.write(buffer, 0, n);
                }
                src = sw.toString();
            }

			if (src != null  && !src.isEmpty()) {
				System.out.println(engine.compile(src, "test"));
                return;
			}

			if (files.length == 1) {
				System.out.println(engine.compile(new File(files[0])));
                return;
			}

			if (files.length == 2) {
				engine.compile(new File(files[0]), new File(files[1]));
                return;
			}
			
		} catch (IOException ioe) {
			System.err.println("Error opening input file.");
		} catch (ParseException pe) {
			System.err.println("Error parsing arguments.");
		}


		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar dust-engine.jar input [output] [options]", cmdOptions);
		System.exit(1);
	}

}