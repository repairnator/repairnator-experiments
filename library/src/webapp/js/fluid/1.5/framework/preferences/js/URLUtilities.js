var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.registerNamespace("fluid.url"),fluid.url.generateDepth=function(depth){return fluid.generate(depth,"../").join("")},fluid.url.parsePathInfo=function(pathInfo){var togo={},segs=pathInfo.split("/");if(segs.length>0){var top=segs.length-1,dotpos=segs[top].indexOf(".");-1!==dotpos&&(togo.extension=segs[top].substring(dotpos+1),segs[top]=segs[top].substring(0,dotpos))}return togo.pathInfo=segs,togo},fluid.url.parsePathInfoTrim=function(pathInfo){var togo=fluid.url.parsePathInfo(pathInfo);return""===togo.pathInfo[togo.pathInfo.length-1]&&togo.pathInfo.length--,togo},fluid.url.collapseSegs=function(segs,from,to){var togo="";void 0===from&&(from=0),void 0===to&&(to=segs.length);for(var i=from;to-1>i;++i)togo+=segs[i]+"/";return to>from&&(togo+=segs[to-1]),togo},fluid.url.makeRelPath=function(parsed,index){var togo=fluid.kettle.collapseSegs(parsed.pathInfo,index);return parsed.extension&&(togo+="."+parsed.extension),togo},fluid.url.cononocolosePath=function(pathInfo){for(var consume=0,i=0;i<pathInfo.length;++i)".."===pathInfo[i]?++consume:0!==consume&&(pathInfo.splice(i-2*consume,2*consume),i-=2*consume,consume=0);return pathInfo},fluid.url.parseUri=function(str){for(var o=fluid.url.parseUri.options,m=o.parser[o.strictMode?"strict":"loose"].exec(str),uri={},i=14;i--;)uri[o.key[i]]=m[i]||"";return uri[o.q.name]={},uri[o.key[12]].replace(o.q.parser,function($0,$1,$2){$1&&(uri[o.q.name][$1]=$2)}),uri},fluid.url.parseUri.options={strictMode:!0,key:["source","protocol","authority","userInfo","user","password","host","port","relative","path","directory","file","query","anchor"],q:{name:"queryKey",parser:/(?:^|&)([^&=]*)=?([^&]*)/g},parser:{strict:/^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/,loose:/^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/}},fluid.url.parseSegs=function(url){var parsed=fluid.url.parseUri(url),parsedSegs=fluid.url.parsePathInfoTrim(parsed.directory);return parsedSegs.pathInfo},fluid.url.isAbsoluteUrl=function(url){var parseRel=fluid.url.parseUri(url);return parseRel.host||parseRel.protocol||"/"===parseRel.directory.charAt(0)},fluid.url.computeRelativePrefix=function(outerLocation,iframeLocation,relPath){if(fluid.url.isAbsoluteUrl(relPath))return relPath;var relSegs=fluid.url.parsePathInfo(relPath).pathInfo,parsedOuter=fluid.url.parseSegs(outerLocation),parsedRel=parsedOuter.concat(relSegs);fluid.url.cononocolosePath(parsedRel);for(var parsedInner=fluid.url.parseSegs(iframeLocation),seg=0;seg<parsedRel.length&&parsedRel[seg]===parsedInner[seg];++seg);var excess=parsedInner.length-seg,back=fluid.url.generateDepth(excess),front=fluid.url.collapseSegs(parsedRel,seg);return back+front}}(jQuery,fluid_1_5);