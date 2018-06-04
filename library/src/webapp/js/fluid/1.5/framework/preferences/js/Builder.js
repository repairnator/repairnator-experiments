var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.registerNamespace("fluid.prefs"),fluid.defaults("fluid.prefs.builder",{gradeNames:["fluid.eventedComponent","fluid.prefs.auxBuilder","autoInit"],mergePolicy:{auxSchema:"expandedAuxSchema"},assembledPrefsEditorGrade:{expander:{func:"fluid.prefs.builder.generateGrade",args:["prefsEditor","{that}.options.auxSchema.namespace",{gradeNames:["fluid.viewComponent","autoInit","fluid.prefs.assembler.prefsEd"],componentGrades:"{that}.options.constructedGrades"}]}},assembledUIEGrade:{expander:{func:"fluid.prefs.builder.generateGrade",args:["uie","{that}.options.auxSchema.namespace",{gradeNames:["fluid.viewComponent","autoInit","fluid.prefs.assembler.uie"],componentGrades:"{that}.options.constructedGrades"}]}},constructedGrades:{expander:{func:"fluid.prefs.builder.constructGrades",args:["{that}.options.auxSchema",["enactors","messages","panels","rootModel","templateLoader","messageLoader","templatePrefix","messagePrefix"]]}},mappedDefaults:"{primaryBuilder}.options.schema.properties",components:{primaryBuilder:{type:"fluid.prefs.primaryBuilder",options:{typeFilter:{expander:{func:"fluid.prefs.builder.parseAuxSchema",args:"{builder}.options.auxiliarySchema"}}}}},distributeOptions:[{source:"{that}.options.primarySchema",removeSource:!0,target:"{that > primaryBuilder}.options.primarySchema"}]}),fluid.defaults("fluid.prefs.assembler.uie",{gradeNames:["autoInit","fluid.viewComponent"],components:{store:{type:"fluid.littleComponent",options:{gradeNames:["{that}.options.storeType"],storeType:"fluid.globalSettingsStore"}},enhancer:{type:"fluid.littleComponent",options:{gradeNames:"{that}.options.enhancerType",enhancerType:"fluid.pageEnhancer",components:{uiEnhancer:{options:{gradeNames:["{fluid.prefs.assembler.uie}.options.componentGrades.enactors"]}}}}}},distributeOptions:[{source:"{that}.options.enhancer",removeSource:!0,target:"{that uiEnhancer}.options"},{source:"{that}.options.store",removeSource:!0,target:"{that settingsStore}.options"},{source:"{that}.options.storeType",removeSource:!0,target:"{that > store}.options.storeType"},{source:"{that}.options.enhancerType",removeSource:!0,target:"{that > enhancer}.options.enhancerType"}]}),fluid.defaults("fluid.prefs.assembler.prefsEd",{gradeNames:["autoInit","fluid.viewComponent","fluid.prefs.assembler.uie"],components:{prefsEditorLoader:{type:"fluid.viewComponent",container:"{fluid.prefs.assembler.prefsEd}.container",priority:"last",options:{gradeNames:["{fluid.prefs.assembler.prefsEd}.options.componentGrades.templatePrefix","{fluid.prefs.assembler.prefsEd}.options.componentGrades.messagePrefix","{fluid.prefs.assembler.prefsEd}.options.componentGrades.messages","{that}.options.prefsEditorType"],prefsEditorType:"fluid.prefs.separatedPanel",templateLoader:{gradeNames:["{fluid.prefs.assembler.prefsEd}.options.componentGrades.templateLoader"]},messageLoader:{gradeNames:["{fluid.prefs.assembler.prefsEd}.options.componentGrades.messageLoader"]},prefsEditor:{gradeNames:["{fluid.prefs.assembler.prefsEd}.options.componentGrades.panels","{fluid.prefs.assembler.prefsEd}.options.componentGrades.rootModel","fluid.prefs.uiEnhancerRelay"]},events:{onReady:"{fluid.prefs.assembler.prefsEd}.events.onPrefsEditorReady"}}}},events:{onPrefsEditorReady:null,onReady:{events:{onPrefsEditorReady:"onPrefsEditorReady",onCreate:"onCreate"},args:["{that}"]}},distributeOptions:[{source:"{that}.options.prefsEditorType",removeSource:!0,target:"{that > prefsEditorLoader}.options.prefsEditorType"},{source:"{that}.options.prefsEditor",removeSource:!0,target:"{that prefsEditor}.options"},{source:"{that}.options.templatePrefix",removeSource:!0,target:"{that prefsEditorLoader}.options.templatePrefix"},{source:"{that}.options.messagePrefix",removeSource:!0,target:"{that prefsEditorLoader}.options.messagePrefix"}]}),fluid.prefs.builder.generateGrade=function(name,namespace,options){var gradeNameTemplate="%namespace.%name",gradeName=fluid.stringTemplate(gradeNameTemplate,{name:name,namespace:namespace});return fluid.defaults(gradeName,options),gradeName},fluid.prefs.builder.constructGrades=function(auxSchema,gradeCategories){var constructedGrades={};return fluid.each(gradeCategories,function(category){var gradeOpts=auxSchema[category];fluid.get(gradeOpts,"gradeNames")&&(constructedGrades[category]=fluid.prefs.builder.generateGrade(category,auxSchema.namespace,gradeOpts))}),constructedGrades},fluid.prefs.builder.parseAuxSchema=function(auxSchema){var auxTypes=[];return fluid.each(auxSchema,function(field){var type=field.type;type&&auxTypes.push(type)}),auxTypes},fluid.prefs.create=function(container,options){options=options||{};var builder=fluid.prefs.builder(options.build);return fluid.invokeGlobalFunction(builder.options.assembledPrefsEditorGrade,[container,options.prefsEditor])}}(jQuery,fluid_1_5);