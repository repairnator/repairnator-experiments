var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";function lastDotIndex(path){return path.lastIndexOf(".")}function sourceWrapModelChanged(modelChanged,threadLocal){return function(changeRequest){var sources=threadLocal().sources,args=arguments,source=changeRequest.source||"";fluid.tryCatch(function(){void 0===sources[source]&&(sources[source]=0),++sources[source],modelChanged.apply(null,args)},null,function(){--sources[source]})}}fluid.model.makeEnvironmentStrategy=function(environment){return function(root,segment,index){return 0===index&&environment[segment]?environment[segment]:void 0}},fluid.model.defaultCreatorStrategy=function(root,segment){return void 0===root[segment]?(root[segment]={},root[segment]):void 0},fluid.model.defaultFetchStrategy=function(root,segment){return root[segment]},fluid.model.funcResolverStrategy=function(root,segment){return root.resolvePathSegment?root.resolvePathSegment(segment):void 0},fluid.model.traverseWithStrategy=function(root,segs,initPos,config,uncess){for(var strategies=config.strategies,limit=segs.length-uncess,i=initPos;limit>i;++i){if(!root)return root;for(var accepted,j=0;j<strategies.length&&(accepted=strategies[j](root,segs[i],i+1,segs),void 0===accepted);++j);accepted===fluid.NO_VALUE&&(accepted=void 0),root=accepted}return root},fluid.model.getValueAndSegments=function(root,EL,config,initSegs){return fluid.model.accessWithStrategy(root,EL,fluid.NO_VALUE,config,initSegs,!0)},fluid.model.makeTrundler=function(config){return function(valueSeg,EL){return fluid.model.getValueAndSegments(valueSeg.root,EL,config,valueSeg.segs)}},fluid.model.getWithStrategy=function(root,EL,config,initSegs){return fluid.model.accessWithStrategy(root,EL,fluid.NO_VALUE,config,initSegs)},fluid.model.setWithStrategy=function(root,EL,newValue,config,initSegs){fluid.model.accessWithStrategy(root,EL,newValue,config,initSegs)},fluid.model.accessWithStrategy=function(root,EL,newValue,config,initSegs,returnSegs){if(fluid.isPrimitive(EL)||fluid.isArrayable(EL))return fluid.model.accessImpl(root,EL,newValue,config,initSegs,returnSegs,fluid.model.traverseWithStrategy);var key=EL.type||"default",resolver=config.resolvers[key];resolver||fluid.fail("Unable to find resolver of type "+key);var trundler=fluid.model.makeTrundler(config),valueSeg={root:root,segs:initSegs};return valueSeg=resolver(valueSeg,EL,trundler),EL.path&&valueSeg&&(valueSeg=trundler(valueSeg,EL.path)),returnSegs?valueSeg:valueSeg?valueSeg.root:void 0},fluid.registerNamespace("fluid.pathUtil");var getPathSegmentImpl=function(accept,path,i){var segment=null;accept&&(segment="");for(var escaped=!1,limit=path.length;limit>i;++i){var c=path.charAt(i);if(escaped)escaped=!1,null!==segment&&(segment+=c);else{if("."===c)break;"\\"===c?escaped=!0:null!==segment&&(segment+=c)}}return null!==segment&&(accept[0]=segment),i},globalAccept=[];fluid.pathUtil.parseEL=function(path){for(var togo=[],index=0,limit=path.length;limit>index;){var firstdot=getPathSegmentImpl(globalAccept,path,index);togo.push(globalAccept[0]),index=firstdot+1}return togo},fluid.pathUtil.composeSegment=function(prefix,toappend){toappend=toappend.toString();for(var i=0;i<toappend.length;++i){var c=toappend.charAt(i);("."===c||"\\"===c||"}"===c)&&(prefix+="\\"),prefix+=c}return prefix},fluid.pathUtil.escapeSegment=function(segment){return fluid.pathUtil.composeSegment("",segment)},fluid.pathUtil.composePath=function(prefix,suffix){return 0!==prefix.length&&(prefix+="."),fluid.pathUtil.composeSegment(prefix,suffix)},fluid.pathUtil.composeSegments=function(){for(var path="",i=0;i<arguments.length;++i)path=fluid.pathUtil.composePath(path,arguments[i]);return path},fluid.model.unescapedParser={parse:fluid.model.parseEL,compose:fluid.model.composeSegments},fluid.model.defaultGetConfig={parser:fluid.model.unescapedParser,strategies:[fluid.model.funcResolverStrategy,fluid.model.defaultFetchStrategy]},fluid.model.defaultSetConfig={parser:fluid.model.unescapedParser,strategies:[fluid.model.funcResolverStrategy,fluid.model.defaultFetchStrategy,fluid.model.defaultCreatorStrategy]},fluid.model.escapedParser={parse:fluid.pathUtil.parseEL,compose:fluid.pathUtil.composeSegments},fluid.model.escapedGetConfig={parser:fluid.model.escapedParser,strategies:[fluid.model.defaultFetchStrategy]},fluid.model.escapedSetConfig={parser:fluid.model.escapedParser,strategies:[fluid.model.defaultFetchStrategy,fluid.model.defaultCreatorStrategy]},fluid.initSimpleModel=function(that,optionsModel){return that.model=optionsModel||{},that.model},fluid.initRelayModel=function(that,modelRelayModel){return modelRelayModel},fluid.isModelComplete=function(that){return that.model!==fluid.inEvaluationMarker},fluid.enlistModelComponent=function(that){var instantiator=fluid.getInstantiator(that),enlist=instantiator.modelTransactions.init[that.id];return enlist||(enlist={that:that,complete:fluid.isModelComplete(that)},instantiator.modelTransactions.init[that.id]=enlist),enlist},fluid.clearLinkCounts=function(transRec,relaysAlso){fluid.each(transRec,function(value,key){"number"==typeof value?transRec[key]=0:relaysAlso&&value.options&&"number"==typeof value.options.relayCount&&(value.options.relayCount=0)})},fluid.sortCompleteLast=function(reca,recb){return(reca.completeOnInit?1:0)-(recb.completeOnInit?1:0)},fluid.operateInitialTransaction=function(instantiator,mrec){var transac,transId=fluid.allocateGuid(),transRec=fluid.getModelTransactionRec(instantiator,transId),transacs=fluid.transform(mrec,function(recel){return transac=recel.that.applier.initiate(transId),transRec[recel.that.applier.applierId]={transaction:transac},transac}),recs=fluid.values(mrec).sort(fluid.sortCompleteLast);fluid.each(recs,function(recel){var that=recel.that,transac=transacs[that.id];recel.completeOnInit?fluid.initModelEvent(that,transac,that.applier.changeListeners.listeners):fluid.each(recel.initModels,function(initModel){transac.fireChangeRequest({type:"ADD",segs:[],value:initModel}),fluid.clearLinkCounts(transRec,!0)});var shadow=fluid.shadowForComponent(that);shadow.modelComplete=!0}),transac.commit()},fluid.deenlistModelComponent=function(that){var instantiator=fluid.getInstantiator(that),mrec=instantiator.modelTransactions.init;that.model=void 0,mrec[that.id].complete=!0;var incomplete=fluid.find_if(mrec,function(recel){return recel.complete!==!0});incomplete||(fluid.operateInitialTransaction(instantiator,mrec),instantiator.modelTransactions.init={})},fluid.transformToAdapter=function(transform,targetPath){var basedTransform={};return basedTransform[targetPath]=transform,function(trans,newValue){fluid.model.transformWithRules(newValue,basedTransform,{finalApplier:trans})}},fluid.parseModelReference=function(that,ref){var parsed=fluid.parseContextReference(ref);return parsed.segs=that.applier.parseEL(parsed.path),parsed},fluid.parseValidModelReference=function(that,name,ref){var parsed,target,reject=function(message){fluid.fail("Error in "+name+": "+ref+message)};return"{"===ref.charAt(0)?(parsed=fluid.parseModelReference(that,ref),"model"!==parsed.segs[0]?reject(' must be a reference into a component model beginning with "model"'):(parsed.modelSegs=parsed.segs.slice(1),delete parsed.path),target=fluid.resolveContext(parsed.context,that),target||reject(" must be a reference to an existing component")):(target=that,parsed={path:ref,modelSegs:that.applier.parseEL(ref)}),target.applier||fluid.getForComponent(target,["applier"]),target.applier||reject(" must be a reference to a component with a ChangeApplier (descended from fluid.modelComponent)"),parsed.that=target,parsed.applier=target.applier,parsed.path||(parsed.path=target.applier.composeSegments.apply(null,parsed.modelSegs)),parsed},fluid.getModelTransactionRec=function(instantiator,transId){transId||fluid.fail("Cannot get transaction record without transaction id");var transRec=instantiator.modelTransactions[transId];return transRec||instantiator.free||(transRec=instantiator.modelTransactions[transId]={},transRec.externalChanges={}),transRec},fluid.recordChangeListener=function(component,applier,sourceListener){var shadow=fluid.shadowForComponent(component);fluid.recordListener(applier.modelChanged,sourceListener,shadow)},fluid.registerDirectChangeRelay=function(target,targetSegs,source,sourceSegs,linkId,transducer,options){var instantiator=fluid.getInstantiator(target),targetApplier=options.targetApplier||target.applier,sourceApplier=options.sourceApplier||source.applier,applierId=targetApplier.applierId;targetSegs=fluid.makeArray(targetSegs),sourceSegs=sourceSegs?fluid.makeArray(sourceSegs):sourceSegs;var sourceListener=function(newValue,oldValue,path,changeRequest,trans,applier){var transId=trans.id,transRec=fluid.getModelTransactionRec(instantiator,transId);applier&&trans&&!transRec[applier.applierId]&&(transRec[applier.applierId]={transaction:trans});var existing=transRec[applierId];transRec[linkId]=transRec[linkId]||0;var relay=!0;if(relay){if(++transRec[linkId],!existing){var newTrans=targetApplier.initiate(transId,!0);existing=transRec[applierId]={transaction:newTrans,options:options}}transducer&&!options.targetApplier?transducer(existing.transaction,options.sourceApplier?void 0:newValue,sourceSegs,targetSegs):void 0!==newValue&&existing.transaction.fireChangeRequest({type:"ADD",segs:targetSegs,value:newValue})}};sourceSegs&&sourceApplier.modelChanged.addListener({isRelay:!0,segs:sourceSegs,transactional:options.transactional},sourceListener),source&&(fluid.recordChangeListener(source,sourceApplier,sourceListener),target!==source&&fluid.recordChangeListener(target,sourceApplier,sourceListener))},fluid.connectModelRelay=function(source,sourceSegs,target,targetSegs,options){function enlistComponent(component){var enlist=fluid.enlistModelComponent(component);if(enlist.complete){var shadow=fluid.shadowForComponent(component);shadow.modelComplete&&(enlist.completeOnInit=!0)}}var linkId=fluid.allocateGuid();enlistComponent(target),enlistComponent(source),options.update?options.targetApplier?fluid.registerDirectChangeRelay(source,sourceSegs,target,targetSegs,linkId,null,{targetApplier:options.targetApplier,relayCount:options.relayCount,update:options.update}):fluid.registerDirectChangeRelay(target,targetSegs,source,[],linkId+"-transform",options.forwardAdapter,{transactional:!0,sourceApplier:options.forwardApplier}):(fluid.registerDirectChangeRelay(target,targetSegs,source,sourceSegs,linkId,options.forwardAdapter,{}),sourceSegs&&fluid.registerDirectChangeRelay(source,sourceSegs,target,targetSegs,linkId,options.backwardAdapter,{}))},fluid.model.guardedAdapter=function(componentThat,cond,func,args){var isInit=componentThat.modelRelay===fluid.inEvaluationMarker,condValue=cond[isInit?"init":"live"];condValue&&func.apply(null,args)},fluid.makeTransformPackage=function(componentThat,transform,sourcePath,targetPath,forwardCond,backwardCond){var that={forwardHolder:{model:transform},backwardHolder:{model:null}};that.generateAdapters=function(trans){that.forwardAdapterImpl=fluid.transformToAdapter(trans?trans.newHolder.model:that.forwardHolder.model,targetPath),null!==sourcePath&&(that.backwardHolder.model=fluid.model.transform.invertConfiguration(transform),that.backwardAdapterImpl=fluid.transformToAdapter(that.backwardHolder.model,sourcePath))},that.forwardAdapter=function(transaction,newValue){void 0===newValue&&that.generateAdapters(),fluid.model.guardedAdapter(componentThat,forwardCond,that.forwardAdapterImpl,arguments)},that.runTransform=function(trans){trans.commit(),trans.reset()},that.forwardApplier=fluid.makeNewChangeApplier(that.forwardHolder),that.forwardApplier.isRelayApplier=!0,that.invalidator=fluid.makeEventFirer(null,null,"Invalidator for model relay with applier "+that.forwardApplier.applierId),null!==sourcePath&&(that.backwardApplier=fluid.makeNewChangeApplier(that.backwardHolder),that.backwardAdapter=function(){fluid.model.guardedAdapter(componentThat,backwardCond,that.backwardAdapterImpl,arguments)}),that.update=that.invalidator.fire;var implicitOptions={relayCount:0,targetApplier:that.forwardApplier,update:that.update,refCount:0};return that.forwardHolder.model=fluid.parseImplicitRelay(componentThat,transform,[],implicitOptions),that.refCount=implicitOptions.refCount,that.generateAdapters(),that.invalidator.addListener(that.generateAdapters),that.invalidator.addListener(that.runTransform),that},fluid.singleTransformToFull=function(singleTransform){var withPath=$.extend(!0,{valuePath:""},singleTransform);return{"":{transform:withPath}}},fluid.model.relayConditions={initOnly:{init:!0,live:!1},liveOnly:{init:!1,live:!0},never:{init:!1,live:!1},always:{init:!0,live:!0}},fluid.model.parseRelayCondition=function(condition){return fluid.model.relayConditions[condition||"always"]},fluid.parseModelRelay=function(that,mrrec){var parsedSource=mrrec.source?fluid.parseValidModelReference(that,'modelRelay record member "source"',mrrec.source):{path:null,modelSegs:null},parsedTarget=fluid.parseValidModelReference(that,'modelRelay record member "target"',mrrec.target),transform=mrrec.singleTransform?fluid.singleTransformToFull(mrrec.singleTransform):mrrec.transform;transform||fluid.fail('Cannot parse modelRelay record without element "singleTransform" or "transform":',mrrec);var forwardCond=fluid.model.parseRelayCondition(mrrec.forward),backwardCond=fluid.model.parseRelayCondition(mrrec.backward),transformPackage=fluid.makeTransformPackage(that,transform,parsedSource.path,parsedTarget.path,forwardCond,backwardCond);0===transformPackage.refCount?fluid.connectModelRelay(parsedSource.that||that,parsedSource.modelSegs,parsedTarget.that,parsedTarget.modelSegs,{forwardAdapter:transformPackage.forwardAdapter,backwardAdapter:transformPackage.backwardAdapter}):fluid.connectModelRelay(parsedSource.that||that,parsedSource.modelSegs,parsedTarget.that,parsedTarget.modelSegs,transformPackage)},fluid.parseImplicitRelay=function(that,modelRec,segs,options){var value;if("string"==typeof modelRec&&"{"===modelRec.charAt(0)){var parsed=fluid.parseModelReference(that,modelRec),target=fluid.resolveContext(parsed.context,that);if("model"===parsed.segs[0]){var modelSegs=parsed.segs.slice(1);++options.refCount,fluid.connectModelRelay(that,segs,target,modelSegs,options)}else value=fluid.getForComponent(target,parsed.segs)}else fluid.isPrimitive(modelRec)||!fluid.isPlainObject(modelRec)?value=modelRec:modelRec.expander&&fluid.isPlainObject(modelRec.expander)?value=fluid.expandOptions(modelRec,that):(value=fluid.freshContainer(modelRec),fluid.each(modelRec,function(innerValue,key){segs.push(key);var innerTrans=fluid.parseImplicitRelay(that,innerValue,segs,options);void 0!==innerTrans&&(value[key]=innerTrans),segs.pop()}));return value},fluid.model.notifyExternal=function(transRec){var allChanges=transRec?fluid.values(transRec.externalChanges):[];allChanges.sort(fluid.priorityComparator);for(var i=0;i<allChanges.length;++i){var change=allChanges[i];change.listener.apply(null,change.args)}fluid.clearLinkCounts(transRec,!0)},fluid.model.commitRelays=function(instantiator,transactionId){var transRec=instantiator.modelTransactions[transactionId];fluid.each(transRec,function(transEl){transEl.transaction&&(transEl.transaction.commit("relay"),transEl.transaction.reset())})},fluid.model.updateRelays=function(instantiator,transactionId){var transRec=instantiator.modelTransactions[transactionId],updates=0;return fluid.each(transRec,function(transEl){transEl.options&&transEl.transaction&&transEl.transaction.changeRecord.changes>0&&transEl.options.relayCount<2&&transEl.options.update&&(transEl.options.relayCount++,fluid.clearLinkCounts(transRec),transEl.options.update(transEl.transaction,transRec),++updates)}),updates},fluid.establishModelRelay=function(that,optionsModel,optionsML,optionsMR,applier){function updateRelays(transaction){for(;fluid.model.updateRelays(instantiator,transaction.id)>0;);}function commitRelays(transaction,applier,code){"relay"!==code&&fluid.model.commitRelays(instantiator,transaction.id)}function concludeTransaction(transaction,applier,code){"relay"!==code&&(fluid.model.notifyExternal(instantiator.modelTransactions[transaction.id]),delete instantiator.modelTransactions[transaction.id])}fluid.mergeModelListeners(that,optionsML);var enlist=fluid.enlistModelComponent(that);fluid.each(optionsMR,function(mrrec){fluid.parseModelRelay(that,mrrec)});var initModels=fluid.transform(optionsModel,function(modelRec){return fluid.parseImplicitRelay(that,modelRec,[],{refCount:0})});enlist.initModels=initModels;var instantiator=fluid.getInstantiator(that);return applier.preCommit.addListener(updateRelays),applier.preCommit.addListener(commitRelays),applier.postCommit.addListener(concludeTransaction),fluid.deenlistModelComponent(that),applier.holder.model},fluid.defaults("fluid.commonModelComponent",{gradeNames:["fluid.littleComponent","autoInit"],mergePolicy:{modelListeners:fluid.makeMergeListenersPolicy(fluid.arrayConcatPolicy)}}),fluid.defaults("fluid.modelComponent",{gradeNames:["fluid.commonModelComponent","autoInit"],members:{model:"@expand:fluid.initSimpleModel({that}, {that}.options.model)",applier:"@expand:fluid.makeChangeApplier({that}.model, {that}.options.changeApplierOptions)",modelListeners:"@expand:fluid.mergeModelListeners({that}, {that}.options.modelListeners)"},mergePolicy:{model:"preserve"}}),fluid.defaults("fluid.modelRelayComponent",{gradeNames:["fluid.commonModelComponent","fluid.eventedComponent","autoInit"],changeApplierOptions:{relayStyle:!0,cullUnchanged:!0},members:{model:"@expand:fluid.initRelayModel({that}, {that}.modelRelay)",applier:"@expand:fluid.makeNewChangeApplier({that}, {that}.options.changeApplierOptions)",modelRelay:"@expand:fluid.establishModelRelay({that}, {that}.options.model, {that}.options.modelListeners, {that}.options.modelRelay, {that}.applier)"},mergePolicy:{model:{noexpand:!0,func:fluid.arrayConcatPolicy},modelRelay:{noexpand:!0,func:fluid.arrayConcatPolicy}}}),fluid.defaults("fluid.standardComponent",{gradeNames:["fluid.modelComponent","fluid.eventedComponent","autoInit"]}),fluid.defaults("fluid.standardRelayComponent",{gradeNames:["fluid.modelRelayComponent","autoInit"]}),fluid.modelChangedToChange=function(isNewApplier,args){var newModel=args[0],oldModel=args[1],path=args[3];return isNewApplier?{value:args[0],oldValue:args[1],path:args[2]}:{value:fluid.get(newModel,path),oldValue:fluid.get(oldModel,path),path:path}},fluid.resolveModelListener=function(that,record,isNewApplier){var togo=function(){var change=fluid.modelChangedToChange(isNewApplier,arguments),args=[change],localRecord={change:change,arguments:args};record.args&&(args=fluid.expandOptions(record.args,that,{},localRecord)),fluid.event.invokeListener(record.listener,fluid.makeArray(args))};return fluid.event.impersonateListener(record.listener,togo),togo},fluid.mergeModelListeners=function(that,listeners){var listenerCount=0;fluid.each(listeners,function(value,path){"string"==typeof value&&(value={funcName:value});var records=fluid.event.resolveListenerRecord(value,that,"modelListeners",null,!1),parsed=fluid.parseValidModelReference(that,"modelListeners entry",path),isNewApplier=parsed.applier.preCommit;fluid.each(records.records,function(record){function initModelEvent(){if(isNewApplier&&fluid.isModelComplete(parsed.that)){var trans=parsed.applier.initiate();fluid.initModelEvent(that,trans,[spec]),trans.commit()}}var func=fluid.resolveModelListener(that,record,isNewApplier),spec={listener:func,listenerIndex:listenerCount,segs:parsed.modelSegs,path:parsed.path,priority:fluid.event.mapPriority(record.priority,listenerCount),transactional:!0};++listenerCount,fluid.addSourceGuardedListener(parsed.applier,spec,record.guardSource,func,"modelChanged",record.namespace,record.softNamespace),fluid.recordChangeListener(that,parsed.applier,func),that===parsed.that||fluid.isModelComplete(that)||that.events.onCreate.addListener(initModelEvent)})})},fluid.addSourceGuardedListener=function(applier,path,source,func,eventName,namespace,softNamespace){eventName=eventName||"modelChanged";var wrapped=function(newValue,oldValue,path,changes){return applier.hasChangeSource(source,changes)?void 0:func.apply(null,arguments)};fluid.event.impersonateListener(func,wrapped),applier[eventName].addListener(path,wrapped,namespace,softNamespace)},fluid.fireSourcedChange=function(applier,path,value,source){applier.fireChangeRequest({path:path,value:value,source:source})},fluid.requestChanges=function(applier,changes){for(var i=0;i<changes.length;++i)applier.fireChangeRequest(changes[i])},fluid.bindRequestChange=function(that){that.requestChange=that.change=function(path,value,type){var changeRequest={path:path,value:value,type:type};that.fireChangeRequest(changeRequest)}},fluid.identifyChangeListener=function(listener){return fluid.event.identifyListener(listener)||listener},fluid.typeCode=function(totest){return fluid.isPrimitive(totest)||!fluid.isPlainObject(totest)?"primitive":fluid.isArrayable(totest)?"array":"object"},fluid.model.isChangedPath=function(changeMap,segs){for(var i=0;i<=segs.length;++i){if("string"==typeof changeMap)return!0;i<segs.length&&changeMap&&(changeMap=changeMap[segs[i]])}return!1},fluid.model.setChangedPath=function(options,segs,value){var notePath=function(record){segs.unshift(record),fluid.model.setSimple(options,segs,value),segs.shift()};fluid.model.isChangedPath(options.changeMap,segs)||(++options.changes,notePath("changeMap")),fluid.model.isChangedPath(options.deltaMap,segs)||(++options.deltas,notePath("deltaMap"))},fluid.model.fetchChangeChildren=function(target,i,segs,source,options){fluid.each(source,function(value,key){segs[i]=key,fluid.model.applyChangeStrategy(target,key,i,segs,value,options),segs.length=i})},fluid.model.isSameValue=function(a,b){if("number"!=typeof a||"number"!=typeof b)return a===b;if(a===b)return!0;var relError=Math.abs((a-b)/b);return 1e-12>relError},fluid.model.applyChangeStrategy=function(target,name,i,segs,source,options){var targetSlot=target[name],sourceCode=fluid.typeCode(source),targetCode=fluid.typeCode(targetSlot),changedValue=fluid.NO_VALUE;"primitive"===sourceCode?fluid.model.isSameValue(targetSlot,source)||(changedValue=source):(targetCode!==sourceCode||"array"===sourceCode&&source.length!==targetSlot.length)&&(changedValue=fluid.freshContainer(source)),changedValue!==fluid.NO_VALUE&&(target[name]=changedValue,options.changeMap&&fluid.model.setChangedPath(options,segs,"ADD")),"primitive"!==sourceCode&&fluid.model.fetchChangeChildren(target[name],i+1,segs,source,options)},fluid.model.stepTargetAccess=function(target,type,segs,startpos,endpos,options){for(var i=startpos;endpos>i;++i){var oldTrunk=target[segs[i]];target=fluid.model.traverseWithStrategy(target,segs,i,options["ADD"===type?"resolverSetConfig":"resolverGetConfig"],segs.length-i-1),oldTrunk!==target&&options.changeMap&&fluid.model.setChangedPath(options,segs.slice(0,i+1),"ADD")}return{root:target,last:segs[endpos]}},fluid.model.defaultAccessorConfig=function(options){return options=options||{},options.resolverSetConfig=options.resolverSetConfig||fluid.model.defaultSetConfig,options.resolverGetConfig=options.resolverGetConfig||fluid.model.defaultGetConfig,options},fluid.model.applyHolderChangeRequest=function(holder,request,options){options=fluid.model.defaultAccessorConfig(options),options.deltaMap=options.changeMap?{}:null,options.deltas=0;var pen,length=request.segs.length,atRoot=0===length;if(atRoot?pen={root:holder,last:"model"}:(holder.model||(holder.model={},fluid.model.setChangedPath(options,[],"ADD")),pen=fluid.model.stepTargetAccess(holder.model,request.type,request.segs,0,length-1,options)),"ADD"===request.type){var value=request.value,segs=fluid.makeArray(request.segs);fluid.model.applyChangeStrategy(pen.root,pen.last,length-1,segs,value,options,atRoot)}else"DELETE"===request.type?pen.root&&void 0!==pen.root[pen.last]&&(delete pen.root[pen.last],options.changeMap&&fluid.model.setChangedPath(options,request.segs,"DELETE")):fluid.fail("Unrecognised change type of "+request.type);return options.deltas?options.deltaMap:null},fluid.matchChanges=function(changeMap,specSegs,newHolder){for(var root=newHolder.model,map=changeMap,outSegs=["model"],wildcard=!1,togo=[],i=0;i<specSegs.length;++i){var seg=specSegs[i];"*"===seg?i===specSegs.length-1?wildcard=!0:fluid.fail("Wildcard specification in modelChanged listener is only supported for the final path segment: "+specSegs.join(".")):(outSegs.push(seg),map=fluid.isPrimitive(map)?map:map[seg],root=root?root[seg]:void 0)}return map&&(wildcard?fluid.each(root,function(value,key){togo.push(outSegs.concat(key))}):togo.push(outSegs)),togo},fluid.storeExternalChange=function(transRec,applier,invalidPath,spec,args){var pathString=applier.composeSegments.apply(null,invalidPath),keySegs=[applier.applierId,fluid.event.identifyListener(spec.listener),spec.listenerIndex,pathString],keyString=keySegs.join("|");transRec.externalChanges[keyString]={listener:spec.listener,priority:spec.priority,args:args}},fluid.notifyModelChanges=function(listeners,changeMap,newHolder,oldHolder,changeRequest,transaction,applier,that){for(var instantiator=fluid.getInstantiator(that),transRec=transaction&&fluid.getModelTransactionRec(instantiator,transaction.id),i=0;i<listeners.length;++i)for(var spec=listeners[i],invalidPaths=fluid.matchChanges(changeMap,spec.segs,newHolder),j=0;j<invalidPaths.length;++j){var invalidPath=invalidPaths[j];spec.listener=fluid.event.resolveListener(spec.listener);var args=[fluid.model.getSimple(newHolder,invalidPath),fluid.model.getSimple(oldHolder,invalidPath),invalidPath.slice(1),changeRequest,transaction,applier];transRec&&!spec.isRelay&&spec.transactional?fluid.storeExternalChange(transRec,applier,invalidPath,spec,args):spec.listener.apply(null,args)}},fluid.bindELMethods=function(applier){applier.parseEL=function(EL){return fluid.model.pathToSegments(EL,applier.options.resolverSetConfig)},applier.composeSegments=function(){return applier.options.resolverSetConfig.parser.compose.apply(null,arguments)}},fluid.initModelEvent=function(that,trans,listeners){fluid.notifyModelChanges(listeners,"ADD",trans.oldHolder,fluid.emptyHolder,null,trans,that)},fluid.emptyHolder={model:void 0},fluid.makeNewChangeApplier=function(holder,options){function preFireChangeRequest(changeRequest){changeRequest.type||(changeRequest.type="ADD"),changeRequest.segs=changeRequest.segs||that.parseEL(changeRequest.path)}options=fluid.model.defaultAccessorConfig(options);var applierId=fluid.allocateGuid(),that={applierId:applierId,holder:holder,changeListeners:{listeners:[],transListeners:[]},options:options,modelChanged:{},preCommit:fluid.makeEventFirer(null,null,"preCommit event for ChangeApplier "+applierId),postCommit:fluid.makeEventFirer(null,null,"postCommit event for ChangeApplier "+applierId)};return that.modelChanged.addListener=function(spec,listener,namespace,softNamespace){spec="string"==typeof spec?{path:spec}:fluid.copy(spec),spec.id=fluid.event.identifyListener(listener),spec.namespace=namespace,spec.softNamespace=softNamespace,"string"==typeof listener&&(listener={globalName:listener}),spec.listener=listener;var transactional=spec.transactional;spec.segs=spec.segs||that.parseEL(spec.path);var collection=transactional?"transListeners":"listeners";that.changeListeners[collection].push(spec)},that.modelChanged.removeListener=function(listener){var id=fluid.event.identifyListener(listener),namespace="string"==typeof listener?listener:null,removePred=function(record){return record.id===id||record.namespace===namespace};fluid.remove_if(that.changeListeners.listeners,removePred),fluid.remove_if(that.changeListeners.transListeners,removePred)},that.modelChanged.isRelayEvent=!0,that.fireChangeRequest=function(changeRequest){var ation=that.initiate();ation.fireChangeRequest(changeRequest),ation.commit()},that.initiate=function(transactionId,defeatPost){var trans={instanceId:fluid.allocateGuid(),id:transactionId||fluid.allocateGuid(),changeRecord:{resolverSetConfig:options.resolverSetConfig,resolverGetConfig:options.resolverGetConfig},reset:function(){trans.oldHolder=holder,trans.newHolder={model:fluid.copy(holder.model)},trans.changeRecord.changes=0,trans.changeRecord.changeMap={}},commit:function(code){if(that.preCommit.fire(trans,that,code),trans.changeRecord.changes>0){var oldHolder={model:holder.model};holder.model=trans.newHolder.model,fluid.notifyModelChanges(that.changeListeners.transListeners,trans.changeRecord.changeMap,holder,oldHolder,null,trans,that,holder)}defeatPost||that.postCommit.fire(trans,that,code)},fireChangeRequest:function(changeRequest){preFireChangeRequest(changeRequest),changeRequest.transactionId=trans.id;var deltaMap=fluid.model.applyHolderChangeRequest(trans.newHolder,changeRequest,trans.changeRecord);fluid.notifyModelChanges(that.changeListeners.listeners,deltaMap,trans.newHolder,holder,changeRequest,trans,that,holder)}};return trans.reset(),fluid.bindRequestChange(trans),trans},that.hasChangeSource=function(source,changes){return changes?changes[source]:!1},fluid.bindRequestChange(that),fluid.bindELMethods(that),that},fluid.pathUtil.getPathSegment=function(path,i){return getPathSegmentImpl(globalAccept,path,i),globalAccept[0]},fluid.pathUtil.getHeadPath=function(path){return fluid.pathUtil.getPathSegment(path,0)},fluid.pathUtil.getFromHeadPath=function(path){var firstdot=getPathSegmentImpl(null,path,0);return firstdot===path.length?"":path.substring(firstdot+1)},fluid.pathUtil.getToTailPath=function(path){var lastdot=lastDotIndex(path);return-1===lastdot?"":path.substring(0,lastdot)},fluid.pathUtil.getTailPath=function(path){var lastdot=lastDotIndex(path);return fluid.pathUtil.getPathSegment(path,lastdot+1)},fluid.pathUtil.matchSegments=function(toMatch,segs,start,end){if(end-start!==toMatch.length)return!1;for(var i=start;end>i;++i)if(segs[i]!==toMatch[i-start])return!1;return!0},fluid.pathUtil.getExcessPath=function(base,longer){var index=longer.indexOf(base);return 0!==index&&fluid.fail("Path "+base+" is not a prefix of path "+longer),base.length===longer.length?"":("."!==longer[base.length]&&fluid.fail("Path "+base+" is not properly nested in path "+longer),longer.substring(base.length+1))},fluid.pathUtil.matchPath=function(spec,path,exact){for(var togo=[];;){if(""===path^""===spec&&exact)return null;if(!spec||!path)break;var spechead=fluid.pathUtil.getHeadPath(spec),pathhead=fluid.pathUtil.getHeadPath(path);if("*"!==spechead&&spechead!==pathhead)return null;togo.push(pathhead),spec=fluid.pathUtil.getFromHeadPath(spec),path=fluid.pathUtil.getFromHeadPath(path)}return togo},fluid.model.isNullChange=function(model,request,resolverGetConfig){if("ADD"===request.type&&!request.forceChange){var existing=fluid.get(model,request.segs,resolverGetConfig);if(existing===request.value)return!0}},fluid.model.applyChangeRequest=function(model,request,resolverSetConfig){var pen=fluid.model.accessWithStrategy(model,request.path,fluid.VALUE,resolverSetConfig||fluid.model.defaultSetConfig,null,!0),last=pen.segs[pen.segs.length-1];"ADD"===request.type||"MERGE"===request.type?0===pen.segs.length||"MERGE"===request.type&&pen.root[last]?("ADD"===request.type&&fluid.clear(pen.root),$.extend(!0,0===pen.segs.length?pen.root:pen.root[last],request.value)):pen.root[last]=request.value:"DELETE"===request.type&&(0===pen.segs.length?fluid.clear(pen.root):delete pen.root[last])},fluid.makeChangeApplier=function(model,options){return fluid.makeHolderChangeApplier({model:model},options)},fluid.makeHolderChangeApplier=function(holder,options){function makeGuardWrapper(cullUnchanged){if(!cullUnchanged)return null;var togo=function(guard){return function(model,changeRequest,internalApplier){var oldRet=guard(model,changeRequest,internalApplier);return oldRet===!1?!1:fluid.model.isNullChange(model,changeRequest)?(togo.culled=!0,!1):void 0}};return togo}function wrapListener(listener,spec){var pathSpec=spec,transactional=!1,priority=Number.MAX_VALUE;"string"==typeof spec&&(spec={
path:spec}),pathSpec=spec.path,transactional=spec.transactional,void 0!==spec.priority&&(priority=spec.priority),"!"===pathSpec.charAt(0)&&(transactional=!0,pathSpec=pathSpec.substring(1));var wrapped=function(changePath,fireSpec,accum){var guid=fluid.event.identifyListener(listener),exist=fireSpec.guids[guid];if(exist&&accum)accum&&(exist.accumulate||(exist.accumulate=[]),exist.accumulate.push(accum));else{var match=fluid.pathUtil.matchPath(pathSpec,changePath);if(null!==match){var record={match:match,pathSpec:pathSpec,listener:listener,priority:priority,transactional:transactional};accum&&(record.accumulate=[accum]),fireSpec.guids[guid]=record;var collection=transactional?"transListeners":"listeners";fireSpec[collection].push(record),fireSpec.all.push(record)}}};return fluid.event.impersonateListener(listener,wrapped),wrapped}function fireFromSpec(name,fireSpec,args,category,wrapper){return baseEvents[name].fireToListeners(fireSpec[category],args,wrapper)}function fireComparator(recA,recB){return recA.priority-recB.priority}function prepareFireEvent(name,changePath,fireSpec,accum){baseEvents[name].fire(changePath,fireSpec,accum),fireSpec.all.sort(fireComparator),fireSpec.listeners.sort(fireComparator),fireSpec.transListeners.sort(fireComparator)}function makeFireSpec(){return{guids:{},all:[],listeners:[],transListeners:[]}}function getFireSpec(name,changePath){var fireSpec=makeFireSpec();return prepareFireEvent(name,changePath,fireSpec),fireSpec}function fireEvent(name,changePath,args,wrapper){var fireSpec=getFireSpec(name,changePath);return fireFromSpec(name,fireSpec,args,"all",wrapper)}function adaptListener(that,name){that[name]={addListener:function(spec,listener,namespace,softNamespace){baseEvents[name].addListener(wrapListener(listener,spec),namespace,null,null,softNamespace)},removeListener:function(listener){baseEvents[name].removeListener(listener)}}}function preFireChangeRequest(changeRequest){changeRequest.type||(changeRequest.type="ADD"),changeRequest.segs=that.parseEL(changeRequest.path)}function fireAgglomerated(eventName,formName,changes,args,accpos,matchpos){for(var fireSpec=makeFireSpec(),i=0;i<changes.length;++i)prepareFireEvent(eventName,changes[i].path,fireSpec,changes[i]);for(var j=0;j<fireSpec[formName].length;++j){var spec=fireSpec[formName][j];void 0!==accpos&&(args[accpos]=spec.accumulate),void 0!==matchpos&&(args[matchpos]=spec.match);var ret=spec.listener.apply(null,args);if(ret===!1)return!1}}options=fluid.model.defaultAccessorConfig(options);var baseEvents={guards:fluid.event.getEventFirer(!1,!0,"guard event"),postGuards:fluid.event.getEventFirer(!1,!0,"postGuard event"),modelChanged:fluid.event.getEventFirer(!1,!1,"modelChanged event")},threadLocal=fluid.threadLocal(function(){return{sources:{}}}),that={applierId:fluid.allocateGuid(),holder:holder,options:options};adaptListener(that,"guards"),adaptListener(that,"postGuards"),adaptListener(that,"modelChanged");var bareApplier={fireChangeRequest:function(changeRequest){that.fireChangeRequest(changeRequest,!0)}};return fluid.bindRequestChange(bareApplier),that.fireChangeRequest=function(changeRequest){preFireChangeRequest(changeRequest);var ation=that.initiate();ation.fireChangeRequest(changeRequest),ation.commit()},that.fireChangeRequest=sourceWrapModelChanged(that.fireChangeRequest,threadLocal),fluid.bindRequestChange(that),fluid.bindELMethods(that),that.initiate=function(newModel){var cancelled=!1,changes=[];options.thin?newModel=holder.model:(newModel=newModel||{},fluid.model.copyModel(newModel,holder.model));var ation={commit:function(){var oldModel;if(cancelled)return!1;var ret=fireAgglomerated("postGuards","transListeners",changes,[newModel,null,ation],1);return ret===!1||cancelled?!1:(options.thin?oldModel=holder.model:(oldModel={},fluid.model.copyModel(oldModel,holder.model),fluid.clear(holder.model),fluid.model.copyModel(holder.model,newModel)),void fireAgglomerated("modelChanged","all",changes,[holder.model,oldModel,null,null],2,3))},fireChangeRequest:function(changeRequest){if(preFireChangeRequest(changeRequest),!options.cullUnchanged||!fluid.model.isNullChange(holder.model,changeRequest,options.resolverGetConfig)){var wrapper=makeGuardWrapper(options.cullUnchanged),prevent=fireEvent("guards",changeRequest.path,[newModel,changeRequest,ation],wrapper);prevent!==!1||wrapper&&wrapper.culled||(cancelled=!0),cancelled||wrapper&&wrapper.culled||(fluid.model.applyChangeRequest(newModel,changeRequest,options.resolverSetConfig),changes.push(changeRequest))}}};return ation.fireChangeRequest=sourceWrapModelChanged(ation.fireChangeRequest,threadLocal),fluid.bindRequestChange(ation),ation},that.hasChangeSource=function(source){return threadLocal().sources[source]>0},that},fluid.makeSuperApplier=function(){var subAppliers=[],that={};return that.addSubApplier=function(path,subApplier){subAppliers.push({path:path,subApplier:subApplier})},that.fireChangeRequest=function(request){for(var i=0;i<subAppliers.length;++i){var path=subAppliers[i].path;if(0===request.path.indexOf(path)){var subpath=request.path.substring(path.length+1),subRequest=fluid.copy(request);subRequest.path=subpath,subAppliers[i].subApplier.fireChangeRequest(subRequest)}}},fluid.bindRequestChange(that),that},fluid.attachModel=function(baseModel,path,model){for(var segs=fluid.model.parseEL(path),i=0;i<segs.length-1;++i){var seg=segs[i],subModel=baseModel[seg];subModel||(baseModel[seg]=subModel={}),baseModel=subModel}baseModel[segs[segs.length-1]]=model},fluid.assembleModel=function(modelSpec){var model={},superApplier=fluid.makeSuperApplier(),togo={model:model,applier:superApplier};for(var path in modelSpec){var rec=modelSpec[path];fluid.attachModel(model,path,rec.model),rec.applier&&superApplier.addSubApplier(path,rec.applier)}return togo}}(jQuery,fluid_1_5);