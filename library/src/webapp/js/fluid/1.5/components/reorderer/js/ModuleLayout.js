var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.registerNamespace("fluid.moduleLayout"),fluid.moduleLayout.findColumnAndItemIndices=function(item,layout){return fluid.find(layout.columns,function(column,colIndex){var index=$.inArray(item,column.elements);return-1===index?void 0:{columnIndex:colIndex,itemIndex:index}},{columnIndex:-1,itemIndex:-1})},fluid.moduleLayout.findColIndex=function(item,layout){return fluid.find(layout.columns,function(column,colIndex){return item===column.container?colIndex:void 0},-1)},fluid.moduleLayout.updateLayout=function(item,target,position,layout){item=fluid.unwrap(item),target=fluid.unwrap(target);var itemIndices=fluid.moduleLayout.findColumnAndItemIndices(item,layout);layout.columns[itemIndices.columnIndex].elements.splice(itemIndices.itemIndex,1);var targetCol;if(position===fluid.position.INSIDE)targetCol=layout.columns[fluid.moduleLayout.findColIndex(target,layout)].elements,targetCol.splice(targetCol.length,0,item);else{var relativeItemIndices=fluid.moduleLayout.findColumnAndItemIndices(target,layout);targetCol=layout.columns[relativeItemIndices.columnIndex].elements,position=fluid.dom.normalisePosition(position,itemIndices.columnIndex===relativeItemIndices.columnIndex,relativeItemIndices.itemIndex,itemIndices.itemIndex);var relative=position===fluid.position.BEFORE?0:1;targetCol.splice(relativeItemIndices.itemIndex+relative,0,item)}},fluid.moduleLayout.layoutFromFlat=function(container,columns,portlets){var layout={};return layout.container=container,layout.columns=fluid.transform(columns,function(column){return{container:column,elements:fluid.makeArray(portlets.filter(function(){return fluid.dom.isContainer(column,this)}))}}),layout},fluid.moduleLayout.layoutFromIds=function(idLayout){return{container:fluid.byId(idLayout.id),columns:fluid.transform(idLayout.columns,function(column){return{container:fluid.byId(column.id),elements:fluid.transform(column.children,fluid.byId)}})}},fluid.moduleLayout.layoutToIds=function(idLayout){return{id:fluid.getId(idLayout.container),columns:fluid.transform(idLayout.columns,function(column){return{id:fluid.getId(column.container),children:fluid.transform(column.elements,fluid.getId)}})}},fluid.moduleLayout.defaultOnShowKeyboardDropWarning=function(item,dropWarning){if(dropWarning){var offset=$(item).offset();dropWarning=$(dropWarning),dropWarning.css("position","absolute"),dropWarning.css("top",offset.top),dropWarning.css("left",offset.left)}},fluid.defaults("fluid.moduleLayoutHandler",{gradeNames:["fluid.layoutHandler","autoInit"],orientation:fluid.orientation.VERTICAL,containerRole:fluid.reorderer.roles.REGIONS,selectablesTabindex:-1,sentinelize:!0,events:{onMove:"{reorderer}.events.onMove",onRefresh:"{reorderer}.events.onRefresh",onShowKeyboardDropWarning:"{reorderer}.events.onShowKeyboardDropWarning"},listeners:{"onShowKeyboardDropWarning.setPosition":"fluid.moduleLayout.defaultOnShowKeyboardDropWarning",onRefresh:{priority:"first",listener:"{that}.computeLayout"},onMove:{priority:"last",listener:"fluid.moduleLayout.onMoveListener",args:["{arguments}.0","{arguments}.1","{that}.layout"]}},members:{layout:{expander:{func:"{that}.computeLayout"}}},invokers:{computeLayout:{funcName:"fluid.moduleLayout.computeLayout",args:["{that}","{reorderer}.options.selectors.modules","{that}.dom"],dynamic:!0},computeModules:{funcName:"fluid.moduleLayout.computeModules",args:["{that}.layout","{that}.isLocked","{arguments}.0"],dynamic:!0},makeComputeModules:{funcName:"fluid.moduleLayout.makeComputeModules",args:["{that}","{arguments}.0"]},isLocked:{funcName:"fluid.moduleLayout.isLocked",args:["{arguments}.0","{reorderer}.options.selectors.lockedModules","{that}.reordererDom"]}},selectors:{modules:"{reorderer}.options.selectors.modules",columns:"{reorderer}.options.selectors.columns"},distributeOptions:{target:"{reorderer}.options",record:{selectors:{movables:{expander:{func:"{that}.makeComputeModules",args:[!1]}},dropTargets:{expander:{func:"{that}.makeComputeModules",args:[!1]}},selectables:{expander:{func:"{that}.makeComputeModules",args:[!0]}}}}}}),fluid.moduleLayout.computeLayout=function(that,modulesSelector,dom){var togo;if(modulesSelector&&(togo=fluid.moduleLayout.layoutFromFlat(that.container,dom.locate("columns"),dom.locate("modules"))),!togo){var idLayout=fluid.get(that.options,"moduleLayout.layout");togo=fluid.moduleLayout.layoutFromIds(idLayout)}return that.layout=togo,togo},fluid.moduleLayout.computeModules=function(layout,isLocked,all){var modules=fluid.accumulate(layout.columns,function(column,list){return list.concat(column.elements)},[]);return all||fluid.remove_if(modules,isLocked),modules},fluid.moduleLayout.makeComputeModules=function(that,all){return function(){return that.computeModules(all)}},fluid.moduleLayout.isLocked=function(item,lockedModulesSelector,dom){var lockedModules=lockedModulesSelector?dom.fastLocate("lockedModules"):[];return-1!==$.inArray(item,lockedModules)},fluid.moduleLayout.onMoveListener=function(item,requestedPosition,layout){fluid.moduleLayout.updateLayout(item,requestedPosition.element,requestedPosition.position,layout)},fluid.moduleLayoutHandler.finalInit=function(that){var options=that.options;that.getRelativePosition=fluid.reorderer.relativeInfoGetter(options.orientation,fluid.reorderer.WRAP_LOCKED_STRATEGY,fluid.reorderer.GEOMETRIC_STRATEGY,that.dropManager,options.disableWrap),that.getGeometricInfo=function(){var extents=[],togo={extents:extents,sentinelize:options.sentinelize};togo.elementMapper=function(element){return that.isLocked(element)?"locked":null},togo.elementIndexer=function(element){var indices=fluid.moduleLayout.findColumnAndItemIndices(element,that.layout);return{index:indices.itemIndex,length:that.layout.columns[indices.columnIndex].elements.length,moduleIndex:indices.columnIndex,moduleLength:that.layout.columns.length}};for(var col=0;col<that.layout.columns.length;col++){var column=that.layout.columns[col],thisEls={orientation:options.orientation,elements:fluid.makeArray(column.elements),parentElement:column.container};extents.push(thisEls)}return togo},that.getModel=function(){return fluid.moduleLayout.layoutToIds(that.layout)}}}(jQuery,fluid_1_5);