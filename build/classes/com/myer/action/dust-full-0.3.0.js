
//
// Dust - Asynchronous Templating v0.3.0
// http://akdubya.github.com/dustjs
//
// Copyright (c) 2010, Aleksander Williams
// Released under the MIT License.
//

var dust={};
(function(o){function z(e,k,l){this.stack=e;this.global=k;this.blocks=l}function H(e,k,l,x){this.tail=k;this.isObject=!o.isArray(e)&&e&&typeof e==="object";this.head=e;this.index=l;this.of=x}function p(e){this.head=new B(this);this.callback=e;this.out=""}function J(){this.head=new B(this)}function B(e,k,l){this.root=e;this.next=k;this.data="";this.flushable=false;this.taps=l}function r(e,k){this.head=e;this.tail=k}o.cache={};o.register=function(e,k){if(e)o.cache[e]=k};o.render=function(e,k,l){l=(new p(l)).head;
o.load(e,l,z.wrap(k)).end()};o.stream=function(e,k){var l=new J;o.nextTick(function(){o.load(e,l.head,z.wrap(k)).end()});return l};o.renderSource=function(e,k,l){return o.compileFn(e)(k,l)};o.compileFn=function(e,k){var l=o.loadSource(o.compile(e,k));return function(x,C){var E=C?new p(C):new J;o.nextTick(function(){l(E.head,z.wrap(x)).end()});return E}};o.load=function(e,k,l){var x=o.cache[e];if(x)return x(k,l);else{if(o.onLoad)return k.map(function(C){o.onLoad(e,function(E,M){if(E)return C.setError(E);
o.cache[e]||o.loadSource(o.compile(M,e));o.cache[e](C,l).end()})});return k.setError(Error("Template Not Found: "+e))}};o.loadSource=function(e){return eval(e)};o.isArray=Array.isArray?Array.isArray:function(e){return Object.prototype.toString.call(e)=="[object Array]"};o.nextTick=function(e){setTimeout(e,0)};o.isEmpty=function(e){if(o.isArray(e)&&!e.length)return true;if(e===0)return false;return!e};o.filter=function(e,k,l){if(l)for(var x=0,C=l.length;x<C;x++){var E=l[x];if(E==="s")k=null;else e=
o.filters[E](e)}if(k)e=o.filters[k](e);return e};o.filters={h:function(e){return o.escapeHtml(e)},j:function(e){return o.escapeJs(e)},u:encodeURI,uc:encodeURIComponent};o.makeBase=function(e){return new z(new H,e)};z.wrap=function(e){if(e instanceof z)return e;return new z(new H(e))};z.prototype.get=function(e){for(var k=this.stack,l;k;){if(k.isObject){l=k.head[e];if(l!==undefined)return l}k=k.tail}return this.global?this.global[e]:undefined};z.prototype.getPath=function(e,k){var l=this.stack,x=k.length;
if(e&&x===0)return l.head;if(l.isObject){l=l.head;for(var C=0;l&&C<x;){l=l[k[C]];C++}return l}};z.prototype.push=function(e,k,l){return new z(new H(e,this.stack,k,l),this.global,this.blocks)};z.prototype.rebase=function(e){return new z(new H(e),this.global,this.blocks)};z.prototype.current=function(){return this.stack.head};z.prototype.getBlock=function(e){var k=this.blocks;if(k)for(var l=k.length,x;l--;)if(x=k[l][e])return x};z.prototype.shiftBlocks=function(e){var k=this.blocks;if(e){newBlocks=
k?k.concat([e]):[e];return new z(this.stack,this.global,newBlocks)}return this};p.prototype.flush=function(){for(var e=this.head;e;){if(e.flushable)this.out+=e.data;else{if(e.error){this.callback(e.error);this.flush=function(){}}return}this.head=e=e.next}this.callback(null,this.out)};J.prototype.flush=function(){for(var e=this.head;e;){if(e.flushable)this.emit("data",e.data);else{if(e.error){this.emit("error",e.error);this.flush=function(){}}return}this.head=e=e.next}this.emit("end")};J.prototype.emit=
function(e,k){var l=this.events;l&&l[e]&&l[e](k)};J.prototype.on=function(e,k){if(!this.events)this.events={};this.events[e]=k;return this};B.prototype.write=function(e){var k=this.taps;if(k)e=k.go(e);this.data+=e;return this};B.prototype.end=function(e){e&&this.write(e);this.flushable=true;this.root.flush();return this};B.prototype.map=function(e){var k=new B(this.root,this.next,this.taps),l=new B(this.root,k,this.taps);this.next=l;this.flushable=true;e(l);return k};B.prototype.tap=function(e){var k=
this.taps;this.taps=k?k.push(e):new r(e);return this};B.prototype.untap=function(){this.taps=this.taps.tail;return this};B.prototype.render=function(e,k){return e(this,k)};B.prototype.reference=function(e,k,l,x){if(typeof e==="function"){e=e(this,k,null,{auto:l,filters:x});if(e instanceof B)return e}return o.isEmpty(e)?this:this.write(o.filter(e,l,x))};B.prototype.section=function(e,k,l,x){if(typeof e==="function"){e=e(this,k,l,x);if(e instanceof B)return e}var C=l.block;l=l["else"];if(x)k=k.push(x);
if(o.isArray(e)){if(C){x=e.length;l=this;for(var E=0;E<x;E++)l=C(l,k.push(e[E],E,x));return l}}else if(e===true){if(C)return C(this,k)}else if(e||e===0){if(C)return C(this,k.push(e))}else if(l)return l(this,k);return this};B.prototype.exists=function(e,k,l){var x=l.block;l=l["else"];if(o.isEmpty(e)){if(l)return l(this,k)}else if(x)return x(this,k);return this};B.prototype.notexists=function(e,k,l){var x=l.block;l=l["else"];if(o.isEmpty(e)){if(x)return x(this,k)}else if(l)return l(this,k);return this};
B.prototype.block=function(e,k,l){l=l.block;if(e)l=e;if(l)return l(this,k);return this};B.prototype.partial=function(e,k){if(typeof e==="function")return this.capture(e,k,function(l,x){o.load(l,x,k).end()});return o.load(e,this,k)};B.prototype.helper=function(e,k,l,x){return o.helpers[e](this,k,l,x)};B.prototype.capture=function(e,k,l){return this.map(function(x){var C=new p(function(E,M){E?x.setError(E):l(M,x)});e(C.head,k).end()})};B.prototype.setError=function(e){this.error=e;this.root.flush();
return this};o.helpers={sep:function(e,k,l){if(k.stack.index===k.stack.of-1)return e;return l.block(e,k)},idx:function(e,k,l){return l.block(e,k.push(k.stack.index))}};r.prototype.push=function(e){return new r(e,this)};r.prototype.go=function(e){for(var k=this;k;){e=k.head(e);k=k.tail}return e};var K=RegExp(/[&<>\"]/),q=/&/g,j=/</g,w=/>/g,t=/\"/g;o.escapeHtml=function(e){if(typeof e==="string"){if(!K.test(e))return e;return e.replace(q,"&amp;").replace(j,"&lt;").replace(w,"&gt;").replace(t,"&quot;")}return e};
var y=/\\/g,A=/\r/g,F=/\u2028/g,L=/\u2029/g,N=/\n/g,V=/\f/g,I=/'/g,Q=/"/g,T=/\t/g;o.escapeJs=function(e){if(typeof e==="string")return e.replace(y,"\\\\").replace(Q,'\\"').replace(I,"\\'").replace(A,"\\r").replace(F,"\\u2028").replace(L,"\\u2029").replace(N,"\\n").replace(V,"\\f").replace(T,"\\t");return e}})(dust);if(typeof exports!=="undefined"){typeof process!=="undefined"&&require("./server")(dust);module.exports=dust}
(function(o){function z(q,j){for(var w=[j[0]],t=1,y=j.length;t<y;t++){var A=o.filterNode(q,j[t]);A&&w.push(A)}return w}function H(q,j){return j}function p(){}function J(q,j){for(var w="",t=1,y=j.length;t<y;t++)w+=o.compileNode(q,j[t]);return w}function B(q,j,w){return"."+w+"("+o.compileNode(q,j[1])+","+o.compileNode(q,j[2])+","+o.compileNode(q,j[4])+","+o.compileNode(q,j[3])+")"}o.compile=function(q,j){var w,t=o.parse(q);w=o.filterNode({},t);t={name:j,bodies:[],blocks:{},index:0,auto:"h"};w="(function(){dust.register("+
(j?'"'+j+'"':"null")+","+o.compileNode(t,w)+");";var y;var A=[],F=t.blocks;for(y in F)A.push(y+":"+F[y]);if(A.length){t.blocks="ctx=ctx.shiftBlocks(blocks);";y="var blocks={"+A.join(",")+"};"}else y=t.blocks="";y=w+y;w=[];A=t.bodies;t=t.blocks;F=0;for(var L=A.length;F<L;F++)w[F]="function body_"+F+"(chk,ctx){"+t+"return chk"+A[F]+";}";t=w.join("");return y+t+"return body_0;})();"};o.filterNode=function(q,j){return o.optimizers[j[0]](q,j)};o.optimizers={body:function(q,j){for(var w=[j[0]],t,y=1,A=
j.length;y<A;y++){var F=o.filterNode(q,j[y]);if(F)if(F[0]==="buffer")if(t)t[1]+=F[1];else{t=F;w.push(F)}else{t=null;w.push(F)}}return w},buffer:H,special:function(q,j){return["buffer",r[j[1]]]},format:p,reference:z,"#":z,"?":z,"^":z,"<":z,"+":z,"@":z,"%":z,partial:z,context:z,params:z,bodies:z,param:z,filters:H,key:H,path:H,literal:H,comment:p};o.pragmas={esc:function(q,j,w){var t=q.auto;j||(j="h");q.auto=j==="s"?"":j;j=J(q,w.block);q.auto=t;return j}};var r={s:" ",n:"\n",r:"\r",lb:"{",rb:"}"};o.compileNode=
function(q,j){return o.nodes[j[0]](q,j)};o.nodes={body:function(q,j){var w=q.index++,t="body_"+w;q.bodies[w]=J(q,j);return t},buffer:function(q,j){return".write("+K(j[1])+")"},format:function(q,j){return".write("+K(j[1]+j[2])+")"},reference:function(q,j){return".reference("+o.compileNode(q,j[1])+",ctx,"+o.compileNode(q,j[2])+")"},"#":function(q,j){return B(q,j,"section")},"?":function(q,j){return B(q,j,"exists")},"^":function(q,j){return B(q,j,"notexists")},"<":function(q,j){for(var w=j[4],t=1,y=
w.length;t<y;t++){var A=w[t];if(A[1][1]==="block"){q.blocks[j[1].text]=o.compileNode(q,A[2]);break}}return""},"+":function(q,j){return".block(ctx.getBlock("+K(j[1].text)+"),"+o.compileNode(q,j[2])+","+o.compileNode(q,j[4])+","+o.compileNode(q,j[3])+")"},"@":function(q,j){return".helper("+K(j[1].text)+","+o.compileNode(q,j[2])+","+o.compileNode(q,j[4])+","+o.compileNode(q,j[3])+")"},"%":function(q,j){var w=j[1][1];if(!o.pragmas[w])return"";for(var t=j[4],y={},A=1,F=t.length;A<F;A++){var L=t[A];y[L[1][1]]=
L[2]}t=j[3];L={};A=1;for(F=t.length;A<F;A++){var N=t[A];L[N[1][1]]=N[2][1]}return o.pragmas[w](q,j[2][1]?j[2][1].text:null,y,L)},partial:function(q,j){return".partial("+o.compileNode(q,j[1])+","+o.compileNode(q,j[2])+")"},context:function(q,j){if(j[1])return"ctx.rebase("+o.compileNode(q,j[1])+")";return"ctx"},params:function(q,j){for(var w=[],t=1,y=j.length;t<y;t++)w.push(o.compileNode(q,j[t]));if(w.length)return"{"+w.join(",")+"}";return"null"},bodies:function(q,j){for(var w=[],t=1,y=j.length;t<
y;t++)w.push(o.compileNode(q,j[t]));return"{"+w.join(",")+"}"},param:function(q,j){return o.compileNode(q,j[1])+":"+o.compileNode(q,j[2])},filters:function(q,j){for(var w=[],t=1,y=j.length;t<y;t++)w.push('"'+j[t]+'"');return'"'+q.auto+'"'+(w.length?",["+w.join(",")+"]":"")},key:function(q,j){return'ctx.get("'+j[1]+'")'},path:function(q,j){for(var w=j[1],t=j[2],y=[],A=0,F=t.length;A<F;A++)y.push('"'+t[A]+'"');return"ctx.getPath("+w+",["+y.join(",")+"])"},literal:function(q,j){return K(j[1])}};var K=
typeof JSON==="undefined"?function(q){return'"'+o.escapeJs(q)+'"'}:JSON.stringify})(typeof exports!=="undefined"?exports:dust);
(function(o){var z=function(){var H={parse:function(p){function J(n){var b=n.charCodeAt(0);if(b<255){n="x";var c=2}else{n="u";c=4}n="\\"+n;var d=b.toString(16).toUpperCase();b=d;c=c-d.length;for(d=0;d<c;d++)b="0"+b;return n+b}function B(n){return'"'+n.replace(/\\/g,"\\\\").replace(/"/g,'\\"').replace(/\r/g,"\\r").replace(/\n/g,"\\n").replace(/[\x80-\uFFFF]/g,J)+'"'}function r(n){if(!(a<R)){if(a>R){R=a;W=[]}W.push(n)}}function K(){var n="body@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=[];for(var c=
q();c!==null;){b.push(c);c=q()}b=b!==null?["body"].concat(b):null;v[n]={nextPos:a,result:b};return b}function q(){var n="part@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=l();if(b!==null)b=b;else{b=j();if(b!==null)b=b;else{b="partial@"+a;var c=v[b];if(c){a=c.nextPos;b=c.result}else{c=h;h=false;var d=a,g=C();if(g!==null){if(p.substr(a,1)===">"){var f=">";a+=1}else{f=null;h&&r('">"')}if(f!==null){var i=I();i=i!==null?["literal",i]:null;if(i!==null)i=i;else{i=Q();i=i!==null?i:null}if(i!==null){var m=
y();if(m!==null){if(p.substr(a,1)==="/"){var s="/";a+=1}else{s=null;h&&r('"/"')}if(s!==null){var u=E();if(u!==null)g=[g,f,i,m,s,u];else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}d=g!==null?["partial",g[2],g[3]]:null;(h=c)&&d===null&&r("partial");v[b]={nextPos:a,result:d};b=d}if(b!==null)b=b;else{b=L();if(b!==null)b=b;else{b=F();if(b!==null)b=b;else{b="buffer@"+a;if(c=v[b]){a=c.nextPos;b=c.result}else{c=h;h=false;d=a;g=M();if(g!==null){f=[];for(i=
U();i!==null;){f.push(i);i=U()}if(f!==null)g=[g,f];else{g=null;a=d}}else{g=null;a=d}d=g!==null?["format",g[0],g[1].join("")]:null;if(d!==null)d=d;else{i=g=a;f=h;h=false;m=x();h=f;if(m===null)f="";else{f=null;a=i}if(f!==null){m=a;i=h;h=false;s=M();h=i;if(s===null)i="";else{i=null;a=m}if(i!==null){m=a;s=h;h=false;u=l();h=s;if(u===null)s="";else{s=null;a=m}if(s!==null){if(p.length>a){m=p.charAt(a);a++}else{m=null;h&&r("any character")}if(m!==null)f=[f,i,s,m];else{f=null;a=g}}else{f=null;a=g}}else{f=
null;a=g}}else{f=null;a=g}g=f!==null?f[3]:null;if(g!==null)for(d=[];g!==null;){d.push(g);i=g=a;f=h;h=false;m=x();h=f;if(m===null)f="";else{f=null;a=i}if(f!==null){m=a;i=h;h=false;s=M();h=i;if(s===null)i="";else{i=null;a=m}if(i!==null){m=a;s=h;h=false;u=l();h=s;if(u===null)s="";else{s=null;a=m}if(s!==null){if(p.length>a){m=p.charAt(a);a++}else{m=null;h&&r("any character")}if(m!==null)f=[f,i,s,m];else{f=null;a=g}}else{f=null;a=g}}else{f=null;a=g}}else{f=null;a=g}g=f!==null?f[3]:null}else d=null;d=d!==
null?["buffer",d.join("")]:null;d=d!==null?d:null}(h=c)&&d===null&&r("buffer");v[b]={nextPos:a,result:d};b=d}b=b!==null?b:null}}}}}v[n]={nextPos:a,result:b};return b}function j(){var n="section@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a,d=w();if(d!==null){var g=E();if(g!==null){var f=K();if(f!==null){var i=A();if(i!==null){var m=t();if(m!==null){var s=d[1].text===m.text?"":null;if(s!==null)d=[d,g,f,i,m,s];else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}}else{d=
null;a=c}}else{d=null;a=c}c=d!==null?function(u,D,O){O.push(["param",["literal","block"],D]);u.push(O);return u}(d[0],d[2],d[3],d[4]):null;if(c!==null)c=c;else{c=a;d=w();if(d!==null){if(p.substr(a,1)==="/"){g="/";a+=1}else{g=null;h&&r('"/"')}if(g!==null){f=E();if(f!==null)d=[d,g,f];else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?function(u){u.push(["bodies"]);return u}(d[0]):null;c=c!==null?c:null}(h=b)&&c===null&&r("section");v[n]={nextPos:a,result:c};return c}function w(){var n="sec_tag_start@"+
a,b=v[n];if(b){a=b.nextPos;return b.result}b=a;var c=C();if(c!==null){if(p.substr(a).match(/^[#?^<+@%]/)!==null){var d=p.charAt(a);a++}else{d=null;h&&r("[#?^<+@%]")}if(d!==null){var g=N();if(g!==null){var f=y();if(f!==null){var i;i="params@"+a;var m=v[i];if(m){a=m.nextPos;i=m.result}else{m=h;h=false;var s=[],u=a,D=U();if(D!==null){var O=I();if(O!==null){if(p.substr(a,1)==="="){var P="=";a+=1}else{P=null;h&&r('"="')}if(P!==null){var G=N();if(G!==null)G=G;else{G=Q();G=G!==null?G:null}if(G!==null)D=
[D,O,P,G];else{D=null;a=u}}else{D=null;a=u}}else{D=null;a=u}}else{D=null;a=u}for(u=D!==null?["param",["literal",D[1]],D[3]]:null;u!==null;){s.push(u);u=a;D=U();if(D!==null){O=I();if(O!==null){if(p.substr(a,1)==="="){P="=";a+=1}else{P=null;h&&r('"="')}if(P!==null){G=N();if(G!==null)G=G;else{G=Q();G=G!==null?G:null}if(G!==null)D=[D,O,P,G];else{D=null;a=u}}else{D=null;a=u}}else{D=null;a=u}}else{D=null;a=u}u=D!==null?["param",["literal",D[1]],D[3]]:null}s=s!==null?["params"].concat(s):null;(h=m)&&s===
null&&r("params");v[i]={nextPos:a,result:s};i=s}if(i!==null)c=[c,d,g,f,i];else{c=null;a=b}}else{c=null;a=b}}else{c=null;a=b}}else{c=null;a=b}}else{c=null;a=b}b=c!==null?[c[1],c[2],c[3],c[4]]:null;v[n]={nextPos:a,result:b};return b}function t(){var n="end_tag@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a,d=C();if(d!==null){if(p.substr(a,1)==="/"){var g="/";a+=1}else{g=null;h&&r('"/"')}if(g!==null){var f=N();if(f!==null){var i=E();if(i!==null)d=[d,g,f,i];else{d=null;a=c}}else{d=null;
a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?d[2]:null;(h=b)&&c===null&&r("end tag");v[n]={nextPos:a,result:c};return c}function y(){var n="context@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=a;if(p.substr(a,1)===":"){var c=":";a+=1}else{c=null;h&&r('":"')}if(c!==null){var d=N();if(d!==null)c=[c,d];else{c=null;a=b}}else{c=null;a=b}b=c!==null?c[1]:null;b=b!==null?b:"";b=b!==null?b?["context",b]:["context"]:null;v[n]={nextPos:a,result:b};return b}function A(){var n="bodies@"+a,b=v[n];if(b){a=
b.nextPos;return b.result}b=h;h=false;var c=[],d=a,g=C();if(g!==null){if(p.substr(a,1)===":"){var f=":";a+=1}else{f=null;h&&r('":"')}if(f!==null){var i=I();if(i!==null){var m=E();if(m!==null){var s=K();if(s!==null)g=[g,f,i,m,s];else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}for(d=g!==null?["param",["literal",g[2]],g[4]]:null;d!==null;){c.push(d);d=a;g=C();if(g!==null){if(p.substr(a,1)===":"){f=":";a+=1}else{f=null;h&&r('":"')}if(f!==null){i=I();if(i!==null){m=
E();if(m!==null){s=K();if(s!==null)g=[g,f,i,m,s];else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}}else{g=null;a=d}d=g!==null?["param",["literal",g[2]],g[4]]:null}c=c!==null?["bodies"].concat(c):null;(h=b)&&c===null&&r("bodies");v[n]={nextPos:a,result:c};return c}function F(){var n="reference@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a,d=C();if(d!==null){var g=N();if(g!==null){var f;f="filters@"+a;var i=v[f];if(i){a=i.nextPos;f=i.result}else{i=h;h=false;var m=
[],s=a;if(p.substr(a,1)==="|"){var u="|";a+=1}else{u=null;h&&r('"|"')}if(u!==null){var D=I();if(D!==null)u=[u,D];else{u=null;a=s}}else{u=null;a=s}for(s=u!==null?u[1]:null;s!==null;){m.push(s);s=a;if(p.substr(a,1)==="|"){u="|";a+=1}else{u=null;h&&r('"|"')}if(u!==null){D=I();if(D!==null)u=[u,D];else{u=null;a=s}}else{u=null;a=s}s=u!==null?u[1]:null}m=m!==null?["filters"].concat(m):null;(h=i)&&m===null&&r("filters");v[f]={nextPos:a,result:m};f=m}if(f!==null){i=E();if(i!==null)d=[d,g,f,i];else{d=null;
a=c}}else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?["reference",d[1],d[2]]:null;(h=b)&&c===null&&r("reference");v[n]={nextPos:a,result:c};return c}function L(){var n="special@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a,d=C();if(d!==null){if(p.substr(a,1)==="~"){var g="~";a+=1}else{g=null;h&&r('"~"')}if(g!==null){var f=I();if(f!==null){var i=E();if(i!==null)d=[d,g,f,i];else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?["special",d[2]]:
null;(h=b)&&c===null&&r("special");v[n]={nextPos:a,result:c};return c}function N(){var n="identifier@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=V();c=c!==null?X(["path"].concat(c),n):null;if(c!==null)c=c;else{c=I();c=c!==null?X(["key",c],n):null;c=c!==null?c:null}(h=b)&&c===null&&r("identifier");v[n]={nextPos:a,result:c};return c}function V(){var n="path@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a,d=I();d=d!==null?d:"";if(d!==null){var g=a;if(p.substr(a,1)===
"."){var f=".";a+=1}else{f=null;h&&r('"."')}if(f!==null){var i=I();if(i!==null)f=[f,i];else{f=null;a=g}}else{f=null;a=g}g=f!==null?f[1]:null;if(g!==null)for(var m=[];g!==null;){m.push(g);g=a;if(p.substr(a,1)==="."){f=".";a+=1}else{f=null;h&&r('"."')}if(f!==null){i=I();if(i!==null)f=[f,i];else{f=null;a=g}}else{f=null;a=g}g=f!==null?f[1]:null}else m=null;if(m!==null)d=[d,m];else{d=null;a=c}}else{d=null;a=c}c=d!==null?function(s,u){if(s){u.unshift(s);return[false,u]}return[true,u]}(d[0],d[1]):null;if(c!==
null)c=c;else{if(p.substr(a,1)==="."){c=".";a+=1}else{c=null;h&&r('"."')}c=c!==null?[true,[]]:null;c=c!==null?c:null}(h=b)&&c===null&&r("path");v[n]={nextPos:a,result:c};return c}function I(){var n="key@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a;if(p.substr(a).match(/^[a-zA-Z_$]/)!==null){var d=p.charAt(a);a++}else{d=null;h&&r("[a-zA-Z_$]")}if(d!==null){var g=[];if(p.substr(a).match(/^[0-9a-zA-Z_$]/)!==null){var f=p.charAt(a);a++}else{f=null;h&&r("[0-9a-zA-Z_$]")}for(;f!==null;){g.push(f);
if(p.substr(a).match(/^[0-9a-zA-Z_$]/)!==null){f=p.charAt(a);a++}else{f=null;h&&r("[0-9a-zA-Z_$]")}}if(g!==null)d=[d,g];else{d=null;a=c}}else{d=null;a=c}c=d!==null?d[0]+d[1].join(""):null;(h=b)&&c===null&&r("key");v[n]={nextPos:a,result:c};return c}function Q(){var n="inline@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a;if(p.substr(a,1)==='"'){var d='"';a+=1}else{d=null;h&&r('"\\""')}if(d!==null){if(p.substr(a,1)==='"'){var g='"';a+=1}else{g=null;h&&r('"\\""')}if(g!==null)d=[d,
g];else{d=null;a=c}}else{d=null;a=c}c=d!==null?["literal",""]:null;if(c!==null)c=c;else{c=a;if(p.substr(a,1)==='"'){d='"';a+=1}else{d=null;h&&r('"\\""')}if(d!==null){g=e();if(g!==null){if(p.substr(a,1)==='"'){var f='"';a+=1}else{f=null;h&&r('"\\""')}if(f!==null)d=[d,g,f];else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?["literal",d[1]]:null;if(c!==null)c=c;else{c=a;if(p.substr(a,1)==='"'){d='"';a+=1}else{d=null;h&&r('"\\""')}if(d!==null){f=T();if(f!==null)for(g=[];f!==null;){g.push(f);
f=T()}else g=null;if(g!==null){if(p.substr(a,1)==='"'){f='"';a+=1}else{f=null;h&&r('"\\""')}if(f!==null)d=[d,g,f];else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?["body"].concat(d[1]):null;c=c!==null?c:null}}(h=b)&&c===null&&r("inline");v[n]={nextPos:a,result:c};return c}function T(){var n="inline_part@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=L();if(b!==null)b=b;else{b=F();if(b!==null)b=b;else{b=e();b=b!==null?["buffer",b]:null;b=b!==null?b:null}}v[n]={nextPos:a,result:b};return b}
function e(){var n="literal@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a,d=a,g=h;h=false;var f=x();h=g;if(f===null)g="";else{g=null;a=d}if(g!==null){f=a;d=h;h=false;var i=M();h=d;if(i===null)d="";else{d=null;a=f}if(d!==null){f=k();if(f!==null)f=f;else{if(p.substr(a).match(/^[^"]/)!==null){f=p.charAt(a);a++}else{f=null;h&&r('[^"]')}f=f!==null?f:null}if(f!==null)g=[g,d,f];else{g=null;a=c}}else{g=null;a=c}}else{g=null;a=c}c=g!==null?g[2]:null;if(c!==null)for(var m=[];c!==null;){m.push(c);
d=c=a;g=h;h=false;f=x();h=g;if(f===null)g="";else{g=null;a=d}if(g!==null){f=a;d=h;h=false;i=M();h=d;if(i===null)d="";else{d=null;a=f}if(d!==null){f=k();if(f!==null)f=f;else{if(p.substr(a).match(/^[^"]/)!==null){f=p.charAt(a);a++}else{f=null;h&&r('[^"]')}f=f!==null?f:null}if(f!==null)g=[g,d,f];else{g=null;a=c}}else{g=null;a=c}}else{g=null;a=c}c=g!==null?g[2]:null}else m=null;m=m!==null?m.join(""):null;(h=b)&&m===null&&r("literal");v[n]={nextPos:a,result:m};return m}function k(){var n="esc@"+a,b=v[n];
if(b){a=b.nextPos;return b.result}if(p.substr(a,2)==='\\"'){b='\\"';a+=2}else{b=null;h&&r('"\\\\\\""')}b=b!==null?'"':null;v[n]={nextPos:a,result:b};return b}function l(){var n="comment@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=h;h=false;var c=a;if(p.substr(a,2)==="{!"){var d="{!";a+=2}else{d=null;h&&r('"{!"')}if(d!==null){var g=[],f=a,i=a,m=h;h=false;if(p.substr(a,2)==="!}"){var s="!}";a+=2}else{s=null;h&&r('"!}"')}h=m;if(s===null)m="";else{m=null;a=i}if(m!==null){if(p.length>a){i=p.charAt(a);
a++}else{i=null;h&&r("any character")}if(i!==null)i=[m,i];else{i=null;a=f}}else{i=null;a=f}for(f=i!==null?i[1]:null;f!==null;){g.push(f);i=f=a;m=h;h=false;if(p.substr(a,2)==="!}"){s="!}";a+=2}else{s=null;h&&r('"!}"')}h=m;if(s===null)m="";else{m=null;a=i}if(m!==null){if(p.length>a){i=p.charAt(a);a++}else{i=null;h&&r("any character")}if(i!==null)i=[m,i];else{i=null;a=f}}else{i=null;a=f}f=i!==null?i[1]:null}if(g!==null){if(p.substr(a,2)==="!}"){f="!}";a+=2}else{f=null;h&&r('"!}"')}if(f!==null)d=[d,g,
f];else{d=null;a=c}}else{d=null;a=c}}else{d=null;a=c}c=d!==null?["comment",d[1].join("")]:null;(h=b)&&c===null&&r("comment");v[n]={nextPos:a,result:c};return c}function x(){var n="tag@"+a,b=v[n];if(b){a=b.nextPos;return b.result}b=a;var c=C();if(c!==null){if(p.substr(a).match(/^[#?^><+%:@\/~%]/)!==null){var d=p.charAt(a);a++}else{d=null;h&&r("[#?^><+%:@\\/~%]")}if(d!==null){var g=a,f=a,i=h;h=false;var m=E();h=i;if(m===null)i="";else{i=null;a=f}if(i!==null){f=a;m=h;h=false;var s=M();h=m;if(s===null)m=
"";else{m=null;a=f}if(m!==null){if(p.length>a){f=p.charAt(a);a++}else{f=null;h&&r("any character")}if(f!==null)i=[i,m,f];else{i=null;a=g}}else{i=null;a=g}}else{i=null;a=g}if(i!==null)for(var u=[];i!==null;){u.push(i);f=g=a;i=h;h=false;m=E();h=i;if(m===null)i="";else{i=null;a=f}if(i!==null){f=a;m=h;h=false;s=M();h=m;if(s===null)m="";else{m=null;a=f}if(m!==null){if(p.length>a){f=p.charAt(a);a++}else{f=null;h&&r("any character")}if(f!==null)i=[i,m,f];else{i=null;a=g}}else{i=null;a=g}}else{i=null;a=g}}else u=
null;if(u!==null){g=E();if(g!==null)c=[c,d,u,g];else{c=null;a=b}}else{c=null;a=b}}else{c=null;a=b}}else{c=null;a=b}if(c!==null)b=c;else{b=F();b=b!==null?b:null}v[n]={nextPos:a,result:b};return b}function C(){var n="ld@"+a,b=v[n];if(b){a=b.nextPos;return b.result}if(p.substr(a,1)==="{"){b="{";a+=1}else{b=null;h&&r('"{"')}v[n]={nextPos:a,result:b};return b}function E(){var n="rd@"+a,b=v[n];if(b){a=b.nextPos;return b.result}if(p.substr(a,1)==="}"){b="}";a+=1}else{b=null;h&&r('"}"')}v[n]={nextPos:a,result:b};
return b}function M(){var n="eol@"+a,b=v[n];if(b){a=b.nextPos;return b.result}if(p.substr(a,1)==="\n"){b="\n";a+=1}else{b=null;h&&r('"\\n"')}if(b!==null)b=b;else{if(p.substr(a,2)==="\r\n"){b="\r\n";a+=2}else{b=null;h&&r('"\\r\\n"')}if(b!==null)b=b;else{if(p.substr(a,1)==="\r"){b="\r";a+=1}else{b=null;h&&r('"\\r"')}if(b!==null)b=b;else{if(p.substr(a,1)==="\u2028"){b="\u2028";a+=1}else{b=null;h&&r('"\\u2028"')}if(b!==null)b=b;else{if(p.substr(a,1)==="\u2029"){b="\u2029";a+=1}else{b=null;h&&r('"\\u2029"')}b=
b!==null?b:null}}}}v[n]={nextPos:a,result:b};return b}function U(){var n="ws@"+a,b=v[n];if(b){a=b.nextPos;return b.result}if(p.substr(a).match(/^[\t\u000b\u000c \xA0\uFEFF]/)!==null){b=p.charAt(a);a++}else{b=null;h&&r("[\t\u000b\u000c \\xA0\\uFEFF]")}v[n]={nextPos:a,result:b};return b}function Y(){var n=function(c){c.sort();for(var d=null,g=[],f=0;f<c.length;f++)if(c[f]!==d){g.push(c[f]);d=c[f]}switch(g.length){case 0:return"end of input";case 1:return g[0];default:return g.slice(0,g.length-1).join(", ")+
" or "+g[g.length-1]}}(W),b=Math.max(a,R);b=b<p.length?B(p.charAt(b)):"end of input";return"Expected "+n+" but "+b+" found."}function Z(){for(var n=1,b=1,c=false,d=0;d<R;d++){var g=p.charAt(d);if(g==="\n"){c||n++;b=1;c=false}else if(g==="\r"|g==="\u2028"||g==="\u2029"){n++;b=1;c=true}else{b++;c=false}}return{line:n,column:b}}function X(n,b){n.text=p.substring(b.split("@")[1],a);return n}var a=0,h=true,R=0,W=[],v={},S=K();if(S===null||a!==p.length){S=Z();throw new SyntaxError(Y(),S.line,S.column);
}return S},toSource:function(){return this._source}};H.SyntaxError=function(p,J,B){this.name="SyntaxError";this.message=p;this.line=J;this.column=B};H.SyntaxError.prototype=Error.prototype;return H}();o.parse=z.parse})(typeof exports!=="undefined"?exports:dust);