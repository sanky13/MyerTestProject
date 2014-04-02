var result;


	var entries = modelMap.entrySet().iterator(),
		model = {},
		entry;
 
	// Convert model from java Map to javascript Object
	while(entries.hasNext()) {
		entry = entries.next();
 
		model[entry.getKey()] = entry.getValue();
	}
 
	var compiled = dust.compile(template, "template");
 
	dust.loadSource(compiled);
	dust.render("template", model, function(err, out) {
		result = "Sankar";
	});
})();