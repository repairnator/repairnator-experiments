var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.registerNamespace("fluid.inlineEdit"),fluid.inlineEdit.sendKey=function(control,event,virtualCode,charCode){var kE=document.createEvent("KeyEvents");kE.initKeyEvent(event,1,1,null,0,0,0,0,virtualCode,charCode),control.dispatchEvent(kE)},fluid.setCaretToEnd=function(control,value){var pos=value?value.length:0;try{if(control.focus(),control.setSelectionRange)control.setSelectionRange(pos,pos),$.browser.mozilla&&pos>0&&(fluid.inlineEdit.sendKey(control,"keypress",92,92),fluid.inlineEdit.sendKey(control,"keydown",8,0),fluid.inlineEdit.sendKey(control,"keypress",8,0));else if(control.createTextRange){var range=control.createTextRange();range.move("character",pos),range.select()}}catch(e){}},fluid.inlineEdit.switchToViewMode=function(that){that.editContainer.hide(),that.displayModeRenderer.show()},fluid.inlineEdit.cancel=function(that){that.isEditing()&&(setTimeout(function(){that.editView.value(that.model.value)},1),fluid.inlineEdit.switchToViewMode(that),that.events.afterFinishEdit.fire(that.model.value,that.model.value,that.editField[0],that.viewEl[0]))},fluid.inlineEdit.finish=function(that){var newValue=that.editView.value(),oldValue=that.model.value,viewNode=that.viewEl[0],editNode=that.editField[0],ret=that.events.onFinishEdit.fire(newValue,oldValue,editNode,viewNode);ret!==!1&&(that.updateModelValue(newValue),that.events.afterFinishEdit.fire(newValue,oldValue,editNode,viewNode),fluid.inlineEdit.switchToViewMode(that))},fluid.inlineEdit.bindEditFinish=function(that){function keyCode(evt){return evt.keyCode?evt.keyCode:evt.which?evt.which:0}void 0===that.options.submitOnEnter&&(that.options.submitOnEnter="textarea"!==fluid.unwrap(that.editField).nodeName.toLowerCase());var button=that.textEditButton||$(),escHandler=function(evt){var code=keyCode(evt);return code===$.ui.keyCode.ESCAPE?(button.focus(),fluid.inlineEdit.cancel(that),!1):void 0},finishHandler=function(evt){var code=keyCode(evt);return code!==$.ui.keyCode.ENTER?(button.blur(),!0):(fluid.inlineEdit.finish(that),button.focus(),!1)};that.options.submitOnEnter&&that.editContainer.keypress(finishHandler),that.editContainer.keydown(escHandler)},fluid.inlineEdit.bindBlurHandler=function(that){if(that.options.blurHandlerBinder)that.options.blurHandlerBinder(that);else{var blurHandler=function(){return that.isEditing()&&fluid.inlineEdit.finish(that),!1};that.editField.blur(blurHandler)}},fluid.inlineEdit.initializeEditView=function(that,initial){that.editInitialized||(fluid.inlineEdit.renderEditContainer(that,!that.options.lazyEditView||!initial),that.options.lazyEditView&&initial||(that.events.onCreateEditView.fire(),that.textEditButton&&fluid.inlineEdit.bindEditFinish(that),fluid.inlineEdit.bindBlurHandler(that),that.editView.refreshView(that),that.editInitialized=!0))},fluid.inlineEdit.edit=function(that){fluid.inlineEdit.initializeEditView(that,!1);var viewEl=that.viewEl,displayText=that.displayView.value();that.updateModelValue(""===that.model.value?"":displayText),that.options.applyEditPadding&&that.editField.width(Math.max(viewEl.width()+that.options.paddings.edit,that.options.paddings.minimumEdit)),that.displayModeRenderer.hide(),that.editContainer.show(),setTimeout(function(){fluid.setCaretToEnd(that.editField[0],that.editView.value()),that.options.selectOnEdit&&that.editField[0].select()},0),that.events.afterBeginEdit.fire()},fluid.inlineEdit.clearEmptyViewStyles=function(textEl,styles,originalViewPadding){textEl.removeClass(styles.defaultViewStyle),textEl.css("padding-right",originalViewPadding),textEl.removeClass(styles.emptyDefaultViewText)},fluid.inlineEdit.showDefaultViewText=function(that){that.displayView.value(that.options.strings.defaultViewText),that.viewEl.css("padding-right",that.existingPadding),that.viewEl.addClass(that.options.styles.defaultViewStyle)},fluid.inlineEdit.showNothing=function(that){that.displayView.value(""),$.browser.msie&&"inline"===that.viewEl.css("display")&&that.viewEl.css("display","inline-block")},fluid.inlineEdit.showEditedText=function(that){that.displayView.value(that.model.value),fluid.inlineEdit.clearEmptyViewStyles(that.viewEl,that.options.styles,that.existingPadding)},fluid.inlineEdit.refreshView=function(that,source){that.displayView.refreshView(that,source),that.editView&&that.editView.refreshView(that,source)},fluid.inlineEdit.updateModelValue=function(that,newValue,source){var comparator=that.options.modelComparator,unchanged=comparator?comparator(that.model.value,newValue):that.model.value===newValue;if(!unchanged){var oldModel=$.extend(!0,{},that.model);that.model.value=newValue,that.events.modelChanged.fire(that.model,oldModel,source),that.refreshView(source)}},fluid.inlineEdit.editHandler=function(that){var prevent=that.events.onBeginEdit.fire();return prevent===!1?!1:(fluid.inlineEdit.edit(that),!0)},fluid.inlineEdit.initTooltips=function(that){var tooltipOptions={content:that.options.tooltipText,position:{my:"left top",at:"left bottom",offset:"0 5"},target:"*",delay:that.options.tooltipDelay,styles:{tooltip:that.options.styles.tooltip}};fluid.tooltip(that.viewEl,tooltipOptions),that.textEditButton&&fluid.tooltip(that.textEditButton,tooltipOptions)},fluid.inlineEdit.calculateInitialPadding=function(viewEl){var padding=viewEl.css("padding-right");return padding?parseFloat(padding):0},fluid.inlineEdit.setupEditField=function(editStyle,editField,editFieldMarkup){var eField=$(editField);return eField=eField.length?eField:$(editFieldMarkup),eField.addClass(editStyle),eField},fluid.inlineEdit.setupEditContainer=function(displayContainer,editField,editContainer,editContainerMarkup){var eContainer=$(editContainer);return eContainer=eContainer.length?eContainer:$(editContainerMarkup),displayContainer.after(eContainer),eContainer.append(editField),eContainer},fluid.inlineEdit.defaultEditModeRenderer=function(that){var editField=fluid.inlineEdit.setupEditField(that.options.styles.edit,that.editField,that.options.markup.editField),editContainer=fluid.inlineEdit.setupEditContainer(that.displayModeRenderer,editField,that.editContainer,that.options.markup.editContainer),editModeInstruction=fluid.inlineEdit.setupEditModeInstruction(that.options.styles.editModeInstruction,that.options.strings.editModeInstruction,that.options.markup.editModeInstruction),id=fluid.allocateSimpleId(editModeInstruction);return editField.attr("aria-describedby",id),fluid.inlineEdit.positionEditModeInstruction(editModeInstruction,editContainer,editField),{container:editContainer,field:editField}},fluid.inlineEdit.renderEditContainer=function(that,lazyEditView){if(that.editContainer=that.locate("editContainer"),that.editField=that.locate("edit"),1!==that.editContainer.length&&that.editContainer.length>1&&fluid.fail("InlineEdit did not find a unique container for selector "+that.options.selectors.editContainer+": "+fluid.dumpEl(that.editContainer)),lazyEditView){var editElms=that.options.editModeRenderer(that);editElms&&(that.editContainer=editElms.container,that.editField=editElms.field)}},fluid.inlineEdit.setupEditModeInstruction=function(editModeInstructionStyle,editModeInstructionText,editModeInstructionMarkup){var editModeInstruction=$(editModeInstructionMarkup);return editModeInstruction.addClass(editModeInstructionStyle),editModeInstruction.text(editModeInstructionText),editModeInstruction},fluid.inlineEdit.positionEditModeInstruction=function(editModeInstruction,editContainer,editField){editContainer.append(editModeInstruction),editField.focus(function(){editModeInstruction.show();var editFieldPosition=editField.offset();editModeInstruction.css({left:editFieldPosition.left}),editModeInstruction.css({top:editFieldPosition.top+editField.height()+5})})},fluid.inlineEdit.setupDisplayModeContainer=function(styles,displayModeWrapper){var displayModeContainer=$(displayModeWrapper);return displayModeContainer=displayModeContainer.length?displayModeContainer:$("<span></span>"),displayModeContainer.addClass(styles.displayView),displayModeContainer},fluid.inlineEdit.setupDisplayText=function(viewEl,textStyle){return viewEl.attr("tabindex","-1"),viewEl.addClass(textStyle),viewEl},fluid.inlineEdit.setupTextEditButton=function(that,model){var opts=that.options,textEditButton=that.locate("textEditButton");if(0===textEditButton.length){var markup=$(that.options.markup.textEditButton);markup.addClass(opts.styles.textEditButton),markup.text(opts.tooltipText),fluid.inlineEdit.updateTextEditButton(markup,model.value||opts.strings.defaultViewText,opts.strings.textEditButton),that.events.modelChanged.addListener(function(){fluid.inlineEdit.updateTextEditButton(markup,model.value||opts.strings.defaultViewText,opts.strings.textEditButton)}),that.locate("text").after(markup),textEditButton=that.locate("textEditButton")}return textEditButton},fluid.inlineEdit.updateTextEditButton=function(textEditButton,value,stringTemplate){var buttonText=fluid.stringTemplate(stringTemplate,{text:value});textEditButton.text(buttonText)},fluid.inlineEdit.bindHoverHandlers=function(displayModeRenderer,invitationStyle){var over=function(){displayModeRenderer.addClass(invitationStyle)},out=function(){displayModeRenderer.removeClass(invitationStyle)};displayModeRenderer.hover(over,out)},fluid.inlineEdit.bindHighlightHandler=function(element,displayModeRenderer,styles,strings,model){element=$(element);var makeFocusSwitcher=function(focusOn){return function(){displayModeRenderer.toggleClass(styles.focus,focusOn),displayModeRenderer.toggleClass(styles.invitation,focusOn),model&&model.value||displayModeRenderer.prevObject.text(focusOn?strings.defaultFocussedViewText:strings.defaultViewText)}};element.focus(makeFocusSwitcher(!0)),element.blur(makeFocusSwitcher(!1))},fluid.inlineEdit.bindMouseHandlers=function(element,edit){element=$(element);var triggerGuard=fluid.inlineEdit.makeEditTriggerGuard(element,edit);element.click(function(e){return triggerGuard(e),!1})},fluid.inlineEdit.bindKeyboardHandlers=function(element,edit){element=$(element),element.attr("role","button");var guard=fluid.inlineEdit.makeEditTriggerGuard(element,edit);fluid.activatable(element,function(event){return guard(event)})},fluid.inlineEdit.makeEditTriggerGuard=function(jElement,edit){var element=fluid.unwrap(jElement);return function(event){var outer=fluid.findAncestor(event.target,function(elem){return/input|select|textarea|button|a/i.test(elem.nodeName)||elem===element?!0:void 0});return outer===element?(edit(),!1):void 0}},fluid.inlineEdit.bindEventHandlers=function(that,edit,displayModeContainer){var styles=that.options.styles;fluid.inlineEdit.bindHoverHandlers(displayModeContainer,styles.invitation),fluid.inlineEdit.bindMouseHandlers(that.viewEl,edit),fluid.inlineEdit.bindMouseHandlers(that.textEditButton,edit),fluid.inlineEdit.bindKeyboardHandlers(that.textEditButton,edit),fluid.inlineEdit.bindHighlightHandler(that.viewEl,displayModeContainer,that.options.styles,that.options.strings,that.model),fluid.inlineEdit.bindHighlightHandler(that.textEditButton,displayModeContainer,that.options.styles,that.options.strings,that.model)},fluid.inlineEdit.defaultDisplayModeRenderer=function(that,edit,model){var styles=that.options.styles,displayModeWrapper=fluid.inlineEdit.setupDisplayModeContainer(styles),displayModeContainer=that.viewEl.wrap(displayModeWrapper).parent();return that.textEditButton=fluid.inlineEdit.setupTextEditButton(that,model),displayModeContainer.append(that.textEditButton),fluid.inlineEdit.bindEventHandlers(that,edit,displayModeContainer),displayModeContainer},fluid.inlineEdit.getNodeName=function(element){return fluid.unwrap(element).nodeName.toLowerCase()},fluid.defaults("fluid.inlineEdit.standardAccessor",{gradeNames:["fluid.viewComponent","autoInit"],members:{nodeName:{expander:{funcName:"fluid.inlineEdit.getNodeName",args:"{that}.container"}}},invokers:{value:{funcName:"fluid.inlineEdit.standardAccessor.value",args:["{that}.nodeName","{that}.container","{arguments}.0"]}}}),fluid.inlineEdit.standardAccessor.value=function(nodeName,element,newValue){return fluid["input"===nodeName||"textarea"===nodeName?"value":"text"]($(element),newValue)},fluid.defaults("fluid.inlineEdit.standardDisplayView",{gradeNames:["fluid.viewComponent","autoInit"],invokers:{refreshView:{funcName:"fluid.inlineEdit.standardDisplayView.refreshView",args:["{fluid.inlineEdit}","{that}.container","{arguments}.0"]}}}),fluid.inlineEdit.standardDisplayView.refreshView=function(componentThat){componentThat.model.value?fluid.inlineEdit.showEditedText(componentThat):componentThat.options.strings.defaultViewText?fluid.inlineEdit.showDefaultViewText(componentThat):fluid.inlineEdit.showNothing(componentThat),0===$.trim(componentThat.viewEl.text()).length&&(componentThat.viewEl.addClass(componentThat.options.styles.emptyDefaultViewText),componentThat.existingPadding<componentThat.options.paddings.minimumView&&componentThat.viewEl.css("padding-right",componentThat.options.paddings.minimumView))},fluid.defaults("fluid.inlineEdit.standardEditView",{gradeNames:["fluid.viewComponent","autoInit"],invokers:{refreshView:{funcName:"fluid.inlineEdit.standardEditView.refreshView",args:["{fluid.inlineEdit}","{that}.container","{arguments}.0"]}}}),fluid.inlineEdit.standardEditView.refreshView=function(componentThat,editField,source){(!source||editField&&-1===editField.index(source))&&componentThat.editView.value(componentThat.model.value)},fluid.inlineEdit.setup=function(that){that.editContainer&&that.editContainer.hide(),that.tooltipEnabled()&&fluid.inlineEdit.initTooltips(that)},fluid.inlineEdit.setIsEditing=function(that,state){that.isEditingState=state},fluid.inlineEdit.tooltipEnabled=function(useTooltip){return useTooltip&&$.fn.tooltip},fluid.inlineEdit.processUndoDecorator=function(that){if(that.options.componentDecorators){var decorators=fluid.makeArray(that.options.componentDecorators),decorator=decorators[0];"string"==typeof decorator&&(decorator={type:decorator}),"fluid.undoDecorator"===decorator.type&&(fluid.set(that.options,["components","undo"],{type:"fluid.undo",options:decorator.options}),that.decorators=[fluid.initDependent(that,"undo")])}},fluid.defaults("fluid.inlineEdit",{gradeNames:["fluid.viewComponent","fluid.undoable","autoInit"],mergePolicy:{"strings.defaultViewText":"defaultViewText"},members:{isEditingState:!1,viewEl:{expander:{funcName:"fluid.inlineEdit.setupDisplayText",args:["{that}.dom.text","{that}.options.styles.text"]}},existingPadding:{expander:{funcName:"fluid.inlineEdit.calculateInitialPadding",args:"{that}.viewEl"}},displayModeRenderer:{expander:{func:"{that}.options.displayModeRenderer",args:["{that}","{that}.edit","{that}.model"]}}},invokers:{edit:{funcName:"fluid.inlineEdit.editHandler",args:"{that}"},isEditing:{funcName:"fluid.identity",args:"{that}.isEditingState",dynamic:!0},finish:{funcName:"fluid.inlineEdit.finish",args:"{that}"},cancel:{funcName:"fluid.inlineEdit.cancel",args:"{that}"},tooltipEnabled:{funcName:"fluid.inlineEdit.tooltipEnabled",args:"{that}.options.useTooltip"},refreshView:{funcName:"fluid.inlineEdit.refreshView",args:["{that}","{arguments}.0"]},updateModelValue:{funcName:"fluid.inlineEdit.updateModelValue",args:["{that}","{arguments}.0","{arguments}.1"]},updateModel:{funcName:"fluid.inlineEdit.updateModelValue",args:["{that}","{arguments}.0.value","{arguments}.1"]}},components:{displayView:{type:"{that}.options.displayView.type",container:"{that}.viewEl",options:{gradeNames:"{fluid.inlineEdit}.options.displayAccessor.type"}},editView:{type:"{that}.options.editView.type",createOnEvent:"onCreateEditView",container:"{that}.editField",options:{gradeNames:"{fluid.inlineEdit}.options.editAccessor.type"}}},model:{value:{expander:{func:"{that}.displayView.value"}}},selectors:{text:".flc-inlineEdit-text",editContainer:".flc-inlineEdit-editContainer",edit:".flc-inlineEdit-edit",textEditButton:".flc-inlineEdit-textEditButton"},styles:{text:"fl-inlineEdit-text",edit:"fl-inlineEdit-edit",invitation:"fl-inlineEdit-invitation",defaultViewStyle:"fl-inlineEdit-emptyText-invitation",emptyDefaultViewText:"fl-inlineEdit-emptyDefaultViewText",focus:"fl-inlineEdit-focus",tooltip:"fl-inlineEdit-tooltip",editModeInstruction:"fl-inlineEdit-editModeInstruction",displayView:"fl-inlineEdit-simple-editableText fl-inlineEdit-textContainer",textEditButton:"fl-offScreen-hidden"},events:{modelChanged:null,onBeginEdit:"preventable",afterBeginEdit:null,onFinishEdit:"preventable",afterFinishEdit:null,afterInitEdit:null,onCreateEditView:null},listeners:{onCreate:[{func:"{that}.refreshView"},{funcName:"fluid.inlineEdit.initializeEditView",args:["{that}",!0]},{funcName:"fluid.inlineEdit.setup",args:"{that}"},{funcName:"fluid.inlineEdit.processUndoDecorator",args:"{that}"}],onBeginEdit:{funcName:"fluid.inlineEdit.setIsEditing",args:["{that}",!0]},afterFinishEdit:{funcName:"fluid.inlineEdit.setIsEditing",args:["{that}",!1]}},strings:{textEditButton:"Edit text %text",editModeInstruction:"Escape to cancel, Enter or Tab when finished",defaultViewText:"Click here to edit",defaultFocussedViewText:"Click here or press enter to edit"},markup:{editField:"<input type='text' class='flc-inlineEdit-edit'/>",editContainer:"<span></span>",editModeInstruction:"<p></p>",textEditButton:"<a href='#_' class='flc-inlineEdit-textEditButton'></a>"},paddings:{edit:10,minimumEdit:80,minimumView:60},applyEditPadding:!0,blurHandlerBinder:null,submitOnEnter:void 0,modelComparator:null,displayAccessor:{type:"fluid.inlineEdit.standardAccessor"},displayView:{type:"fluid.inlineEdit.standardDisplayView"},editAccessor:{type:"fluid.inlineEdit.standardAccessor"},editView:{type:"fluid.inlineEdit.standardEditView"},displayModeRenderer:fluid.inlineEdit.defaultDisplayModeRenderer,editModeRenderer:fluid.inlineEdit.defaultEditModeRenderer,lazyEditView:!1,useTooltip:!0,tooltipText:"Select or press Enter to edit",tooltipDelay:1e3,selectOnEdit:!1}),fluid.setupInlineEdits=function(that,editables){return fluid.transform(editables,function(editable,i){var componentDef={type:"fluid.inlineEdit",container:editable},name="inlineEdit-"+i;return fluid.set(that.options,["components",name],componentDef),fluid.initDependent(that,name)})},fluid.defaults("fluid.inlineEdits",{gradeNames:["fluid.viewComponent","autoInit"],distributeOptions:{source:"{that}.options",exclusions:["members.inlineEdits","selectors.editables"],removeSource:!0,target:"{that > fluid.inlineEdit}.options"},members:{inlineEdits:{expander:{funcName:"fluid.setupInlineEdits",args:["{that}","{that}.dom.editables"]}}},returnedPath:"inlineEdits",selectors:{editables:".flc-inlineEditable"}})}(jQuery,fluid_1_5);