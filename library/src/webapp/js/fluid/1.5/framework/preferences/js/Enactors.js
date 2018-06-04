var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.defaults("fluid.prefs.enactor",{gradeNames:["fluid.modelComponent","fluid.eventedComponent","fluid.prefs.modelRelay","autoInit"]}),fluid.defaults("fluid.prefs.uiEnhancerConnections",{gradeNames:["fluid.eventedComponent","autoInit"],mergePolicy:{sourceApplier:"nomerge"},sourceApplier:"{uiEnhancer}.applier"}),fluid.defaults("fluid.prefs.enactor.styleElements",{gradeNames:["fluid.prefs.enactor","autoInit"],cssClass:null,invokers:{applyStyle:{funcName:"fluid.prefs.enactor.styleElements.applyStyle",args:["{arguments}.0","{arguments}.1"]},resetStyle:{funcName:"fluid.prefs.enactor.styleElements.resetStyle",args:["{arguments}.0","{arguments}.1"]},handleStyle:{funcName:"fluid.prefs.enactor.styleElements.handleStyle",args:["{arguments}.0",{expander:{func:"{that}.getElements"}},"{that}"],dynamic:!0},getElements:"fluid.prefs.enactor.getElements"},listeners:{onCreate:{listener:"{that}.handleStyle",args:["{that}.model.value"]}}}),fluid.prefs.enactor.styleElements.applyStyle=function(elements,cssClass){elements.addClass(cssClass)},fluid.prefs.enactor.styleElements.resetStyle=function(elements,cssClass){$(elements,"."+cssClass).andSelf().removeClass(cssClass)},fluid.prefs.enactor.styleElements.handleStyle=function(value,elements,that){value?that.applyStyle(elements,that.options.cssClass):that.resetStyle(elements,that.options.cssClass)},fluid.prefs.enactor.styleElements.finalInit=function(that){that.applier.modelChanged.addListener("value",function(newModel){that.handleStyle(newModel.value)})},fluid.defaults("fluid.prefs.enactor.classSwapper",{gradeNames:["fluid.viewComponent","fluid.prefs.enactor","autoInit"],classes:{},invokers:{clearClasses:{funcName:"fluid.prefs.enactor.classSwapper.clearClasses",args:["{that}.container","{that}.classStr"]},swap:{funcName:"fluid.prefs.enactor.classSwapper.swap",args:["{arguments}.0","{that}"]}},listeners:{onCreate:{listener:"{that}.swap",args:["{that}.model.value"]}},members:{classStr:{expander:{func:"fluid.prefs.enactor.classSwapper.joinClassStr",args:"{that}.options.classes"}}}}),fluid.prefs.enactor.classSwapper.clearClasses=function(container,classStr){container.removeClass(classStr)},fluid.prefs.enactor.classSwapper.swap=function(value,that){that.clearClasses(),that.container.addClass(that.options.classes[value])},fluid.prefs.enactor.classSwapper.joinClassStr=function(classes){var classStr="";return fluid.each(classes,function(oneClassName){oneClassName&&(classStr+=classStr?" "+oneClassName:oneClassName)}),classStr},fluid.prefs.enactor.classSwapper.finalInit=function(that){that.applier.modelChanged.addListener("value",function(newModel){that.swap(newModel.value)})},fluid.defaults("fluid.prefs.enactor.emphasizeLinks",{gradeNames:["fluid.viewComponent","fluid.prefs.enactor.styleElements","autoInit"],preferenceMap:{"fluid.prefs.emphasizeLinks":{"model.value":"default"}},cssClass:null,invokers:{getElements:{funcName:"fluid.prefs.enactor.emphasizeLinks.getLinks",args:"{that}.container"}}}),fluid.prefs.enactor.emphasizeLinks.getLinks=function(container){return $("a",container)},fluid.defaults("fluid.prefs.enactor.inputsLarger",{gradeNames:["fluid.viewComponent","fluid.prefs.enactor.styleElements","autoInit"],preferenceMap:{"fluid.prefs.inputsLarger":{"model.value":"default"}},cssClass:null,invokers:{getElements:{funcName:"fluid.prefs.enactor.inputsLarger.getInputs",args:"{that}.container"}}}),fluid.prefs.enactor.inputsLarger.getInputs=function(container){return $("input, button",container)},fluid.defaults("fluid.prefs.enactor.textFont",{gradeNames:["fluid.prefs.enactor.classSwapper","autoInit"],preferenceMap:{"fluid.prefs.textFont":{"model.value":"default"}}}),fluid.defaults("fluid.prefs.enactor.contrast",{gradeNames:["fluid.prefs.enactor.classSwapper","autoInit"],preferenceMap:{"fluid.prefs.contrast":{"model.value":"default"}}}),fluid.prefs.enactor.getTextSizeInPx=function(container,fontSizeMap){var fontSize=container.css("font-size");return fontSizeMap[fontSize]&&(fontSize=fontSizeMap[fontSize]),parseFloat(fontSize)},fluid.defaults("fluid.prefs.enactor.textSize",{gradeNames:["fluid.viewComponent","fluid.prefs.enactor","autoInit"],preferenceMap:{"fluid.prefs.textSize":{"model.value":"default"}},members:{root:{expander:{"this":"{that}.container",method:"closest",args:["html"]}}},fontSizeMap:{},invokers:{set:{funcName:"fluid.prefs.enactor.textSize.set",args:["{arguments}.0","{that}"]},getTextSizeInPx:{funcName:"fluid.prefs.enactor.getTextSizeInPx",args:["{that}.container","{that}.options.fontSizeMap"]}},listeners:{onCreate:{listener:"{that}.set",args:"{that}.model.value"}}}),fluid.prefs.enactor.textSize.set=function(times,that){if(times=times||1,that.initialSize||(that.initialSize=that.getTextSizeInPx()),that.initialSize){var targetSize=times*that.initialSize;that.root.css("font-size",targetSize+"px")}},fluid.prefs.enactor.textSize.finalInit=function(that){that.applier.modelChanged.addListener("value",function(newModel){that.set(newModel.value)})},fluid.defaults("fluid.prefs.enactor.lineSpace",{gradeNames:["fluid.viewComponent","fluid.prefs.enactor","autoInit"],preferenceMap:{"fluid.prefs.lineSpace":{"model.value":"default"}},fontSizeMap:{},invokers:{set:{funcName:"fluid.prefs.enactor.lineSpace.set",args:["{arguments}.0","{that}"]},getTextSizeInPx:{funcName:"fluid.prefs.enactor.getTextSizeInPx",args:["{that}.container","{that}.options.fontSizeMap"]},getLineHeight:{funcName:"fluid.prefs.enactor.lineSpace.getLineHeight",args:"{that}.container"},getLineHeightMultiplier:{funcName:"fluid.prefs.enactor.lineSpace.getLineHeightMultiplier",args:[{expander:{func:"{that}.getLineHeight"}},{expander:{func:"{that}.getTextSizeInPx"}}],dynamic:!0}},listeners:{onCreate:{listener:"{that}.set",args:"{that}.model.value"}}}),fluid.prefs.enactor.lineSpace.getLineHeight=function(container){return container.css("line-height")},fluid.prefs.enactor.lineSpace.getLineHeightMultiplier=function(lineHeight,fontSize){return lineHeight?"normal"===lineHeight?1.2:lineHeight.match(/[0-9]$/)?lineHeight:Math.round(parseFloat(lineHeight)/fontSize*100)/100:0},fluid.prefs.enactor.lineSpace.set=function(times,that){if(that.initialSize||(that.initialSize=that.getLineHeightMultiplier()),that.initialSize){var targetLineSpace=times*that.initialSize;that.container.css("line-height",targetLineSpace)}},fluid.prefs.enactor.lineSpace.finalInit=function(that){that.applier.modelChanged.addListener("value",function(newModel){that.set(newModel.value)})},fluid.defaults("fluid.prefs.enactor.tableOfContents",{gradeNames:["fluid.viewComponent","fluid.prefs.enactor","autoInit"],preferenceMap:{"fluid.prefs.tableOfContents":{"model.value":"default"}},tocTemplate:null,components:{tableOfContents:{type:"fluid.tableOfContents",container:"{fluid.prefs.enactor.tableOfContents}.container",createOnEvent:"onCreateTOCReady",options:{components:{levels:{type:"fluid.tableOfContents.levels",options:{resources:{template:{forceCache:!0,url:"{fluid.prefs.enactor.tableOfContents}.options.tocTemplate"}}}}},listeners:{afterRender:"{fluid.prefs.enactor.tableOfContents}.events.afterTocRender"}}}},invokers:{applyToc:{funcName:"fluid.prefs.enactor.tableOfContents.applyToc",args:["{arguments}.0","{that}"]}},events:{onCreateTOCReady:null,afterTocRender:null,onLateRefreshRelay:null},listeners:{onCreate:{listener:"{that}.applyToc",args:"{that}.model.value"}}}),fluid.prefs.enactor.tableOfContents.applyToc=function(value,that){var async=!1;value?that.tableOfContents?that.tableOfContents.show():(that.events.onCreateTOCReady.fire(),async=!0):that.tableOfContents&&that.tableOfContents.hide(),async||that.events.onLateRefreshRelay.fire(that)},fluid.prefs.enactor.tableOfContents.finalInit=function(that){that.applier.modelChanged.addListener("value",function(newModel){that.applyToc(newModel.value)})},fluid.defaults("fluid.prefs.tocWithEmphasizeLinks",{gradeNames:["fluid.eventedComponent","autoInit"],listeners:{afterTocRender:{listener:"{uiEnhancer}.emphasizeLinks.handleStyle",args:"{uiEnhancer}.model.links"},onLateRefreshRelay:{listener:"{uiEnhancer}.emphasizeLinks.handleStyle",args:"{uiEnhancer}.model.links"}}}),fluid.defaults("fluid.prefs.tocWithInputsLarger",{gradeNames:["fluid.eventedComponent","autoInit"],listeners:{afterTocRender:{listener:"{uiEnhancer}.inputsLarger.handleStyle",args:"{uiEnhancer}.model.inputsLarger"},onLateRefreshRelay:{listener:"{uiEnhancer}.inputsLarger.handleStyle",args:"{uiEnhancer}.model.inputsLarger"}}}),fluid.demands("fluid.prefs.enactor.tableOfContents","fluid.prefs.enactor.emphasizeLinks",{options:{gradeNames:"fluid.prefs.tocWithEmphasizeLinks"}}),fluid.demands("fluid.prefs.enactor.tableOfContents","fluid.prefs.enactor.inputsLarger",{options:{gradeNames:"fluid.prefs.tocWithInputsLarger"}})}(jQuery,fluid_1_5);