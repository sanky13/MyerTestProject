(function() {
load(basePath + "/dust-full-0.3.0.js");
  dust.register("demo", body_0);

  function body_0(chk, ctx) {
    return chk.write("Hello ").reference(ctx._get(false, ["name"]), ctx, "h").write("! You have ").reference(ctx._get(false, ["count"]), ctx, "h").write(" new messages.");
  }
  return body_0;
})();