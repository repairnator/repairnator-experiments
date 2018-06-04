var fluid_1_4=fluid_1_4||{};var fluid=fluid||fluid_1_4;(function($,fluid){fluid.renderTimestamp=function(date){var zeropad=function(num,width){if(!width){width=2}var numstr=(num==undefined?"":num.toString());return"00000".substring(5-width+numstr.length)+numstr};return zeropad(date.getHours())+":"+zeropad(date.getMinutes())+":"+zeropad(date.getSeconds())+"."+zeropad(date.getMilliseconds(),3)};fluid.detectStackStyle=function(e){var style="other";var stackStyle={offset:0};if(e.arguments){style="chrome"}else{if(typeof window!=="undefined"&&window.opera&&e.stacktrace){style="opera10"}else{if(e.stack){style="firefox";stackStyle.offset=e.stack.indexOf("Trace exception")===-1?1:0}else{if(typeof window!=="undefined"&&window.opera&&!("stacktrace" in e)){style="opera"}}}}stackStyle.style=style;return stackStyle};fluid.obtainException=function(){try{throw new Error("Trace exception")}catch(e){return e}};var stackStyle=fluid.detectStackStyle(fluid.obtainException());fluid.registerNamespace("fluid.exceptionDecoders");fluid.decodeStack=function(){if(stackStyle.style!=="firefox"){return null}var e=fluid.obtainException();return fluid.exceptionDecoders[stackStyle.style](e)};fluid.exceptionDecoders.firefox=function(e){var lines=e.stack.replace(/(?:\n@:0)?\s+$/m,"").replace(/^\(/gm,"{anonymous}(").split("\n");return fluid.transform(lines,function(line){var atind=line.indexOf("@");return atind===-1?[line]:[line.substring(atind+1),line.substring(0,atind)]})};fluid.getCallerInfo=function(atDepth){atDepth=(atDepth||3)-stackStyle.offset;var stack=fluid.decodeStack();return stack?stack[atDepth][0]:null};function generate(c,count){var togo="";for(var i=0;i<count;++i){togo+=c}return togo}function printImpl(obj,small,options){var big=small+options.indentChars;if(obj===null){return"null"}else{if(fluid.isPrimitive(obj)){return JSON.stringify(obj)}else{var j=[];if(fluid.isArrayable(obj)){if(obj.length===0){return"[]"}for(var i=0;i<obj.length;++i){j[i]=printImpl(obj[i],big,options)}return"[\n"+big+j.join(",\n"+big)+"\n"+small+"]"}else{var i=0;fluid.each(obj,function(value,key){j[i++]=JSON.stringify(key)+": "+printImpl(value,big,options)});return"{\n"+big+j.join(",\n"+big)+"\n"+small+"}"}}}}fluid.prettyPrintJSON=function(obj,options){options=$.extend({indent:4},options);options.indentChars=generate(" ",options.indent);return printImpl(obj,"",options)};fluid.dumpEl=function(element){var togo;if(!element){return"null"}if(element.nodeType===3||element.nodeType===8){return"[data: "+element.data+"]"}if(element.nodeType===9){return"[document: location "+element.location+"]"}if(!element.nodeType&&fluid.isArrayable(element)){togo="[";for(var i=0;i<element.length;++i){togo+=fluid.dumpEl(element[i]);if(i<element.length-1){togo+=", "}}return togo+"]"}element=$(element);togo=element.get(0).tagName;if(element.id){togo+="#"+element.id}if(element.attr("class")){togo+="."+element.attr("class")}return togo}})(jQuery,fluid_1_4);