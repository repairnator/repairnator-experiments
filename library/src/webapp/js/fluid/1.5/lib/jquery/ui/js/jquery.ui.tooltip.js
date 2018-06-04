!function($){function addDescribedBy(elem,id){var describedby=(elem.attr("aria-describedby")||"").split(/\s+/);describedby.push(id),elem.data("ui-tooltip-id",id).attr("aria-describedby",$.trim(describedby.join(" ")))}function removeDescribedBy(elem){var id=elem.data("ui-tooltip-id"),describedby=(elem.attr("aria-describedby")||"").split(/\s+/),index=$.inArray(id,describedby);-1!==index&&describedby.splice(index,1),elem.removeData("ui-tooltip-id"),describedby=$.trim(describedby.join(" ")),describedby?elem.attr("aria-describedby",describedby):elem.removeAttr("aria-describedby")}var increments=0;$.widget("ui.tooltip",{version:"1.10.4",options:{content:function(){var title=$(this).attr("title")||"";return $("<a>").text(title).html()},hide:!0,items:"[title]:not([disabled])",position:{my:"left top+15",at:"left bottom",collision:"flipfit flip"},show:!0,tooltipClass:null,track:!1,close:null,open:null},_create:function(){this._on({mouseover:"open",focusin:"open"}),this.tooltips={},this.parents={},this.options.disabled&&this._disable()},_setOption:function(key,value){var that=this;return"disabled"===key?(this[value?"_disable":"_enable"](),void(this.options[key]=value)):(this._super(key,value),void("content"===key&&$.each(this.tooltips,function(id,element){that._updateContent(element)})))},_disable:function(){var that=this;$.each(this.tooltips,function(id,element){var event=$.Event("blur");event.target=event.currentTarget=element[0],that.close(event,!0)}),this.element.find(this.options.items).addBack().each(function(){var element=$(this);element.is("[title]")&&element.data("ui-tooltip-title",element.attr("title")).attr("title","")})},_enable:function(){this.element.find(this.options.items).addBack().each(function(){var element=$(this);element.data("ui-tooltip-title")&&element.attr("title",element.data("ui-tooltip-title"))})},open:function(event){var that=this,target=$(event?event.target:this.element).closest(this.options.items);target.length&&!target.data("ui-tooltip-id")&&(target.attr("title")&&target.data("ui-tooltip-title",target.attr("title")),target.data("ui-tooltip-open",!0),event&&"mouseover"===event.type&&target.parents().each(function(){var blurEvent,parent=$(this);parent.data("ui-tooltip-open")&&(blurEvent=$.Event("blur"),blurEvent.target=blurEvent.currentTarget=this,that.close(blurEvent,!0)),parent.attr("title")&&(parent.uniqueId(),that.parents[this.id]={element:this,title:parent.attr("title")},parent.attr("title",""))}),this._updateContent(target,event))},_updateContent:function(target,event){var content,contentOption=this.options.content,that=this,eventType=event?event.type:null;return"string"==typeof contentOption?this._open(event,target,contentOption):(content=contentOption.call(target[0],function(response){target.data("ui-tooltip-open")&&that._delay(function(){event&&(event.type=eventType),this._open(event,target,response)})}),void(content&&this._open(event,target,content)))},_open:function(event,target,content){function position(event){positionOption.of=event,tooltip.is(":hidden")||tooltip.position(positionOption)}var tooltip,events,delayedShow,positionOption=$.extend({},this.options.position);if(content){if(tooltip=this._find(target),tooltip.length)return void tooltip.find(".ui-tooltip-content").html(content);target.is("[title]")&&(event&&"mouseover"===event.type?target.attr("title",""):target.removeAttr("title")),tooltip=this._tooltip(target),addDescribedBy(target,tooltip.attr("id")),tooltip.find(".ui-tooltip-content").html(content),this.options.track&&event&&/^mouse/.test(event.type)?(this._on(this.document,{mousemove:position}),position(event)):tooltip.position($.extend({of:target},this.options.position)),tooltip.hide(),this._show(tooltip,this.options.show),this.options.show&&this.options.show.delay&&(delayedShow=this.delayedShow=setInterval(function(){tooltip.is(":visible")&&(position(positionOption.of),clearInterval(delayedShow))},$.fx.interval)),this._trigger("open",event,{tooltip:tooltip}),events={keyup:function(event){if(event.keyCode===$.ui.keyCode.ESCAPE){var fakeEvent=$.Event(event);fakeEvent.currentTarget=target[0],this.close(fakeEvent,!0)}},remove:function(){this._removeTooltip(tooltip)}},event&&"mouseover"!==event.type||(events.mouseleave="close"),event&&"focusin"!==event.type||(events.focusout="close"),this._on(!0,target,events)}},close:function(event){var that=this,target=$(event?event.currentTarget:this.element),tooltip=this._find(target);this.closing||(clearInterval(this.delayedShow),target.data("ui-tooltip-title")&&target.attr("title",target.data("ui-tooltip-title")),removeDescribedBy(target),tooltip.stop(!0),this._hide(tooltip,this.options.hide,function(){that._removeTooltip($(this))}),target.removeData("ui-tooltip-open"),this._off(target,"mouseleave focusout keyup"),target[0]!==this.element[0]&&this._off(target,"remove"),this._off(this.document,"mousemove"),event&&"mouseleave"===event.type&&$.each(this.parents,function(id,parent){$(parent.element).attr("title",parent.title),delete that.parents[id]}),this.closing=!0,this._trigger("close",event,{tooltip:tooltip}),this.closing=!1)},_tooltip:function(element){var id="ui-tooltip-"+increments++,tooltip=$("<div>").attr({id:id,role:"tooltip"}).addClass("ui-tooltip ui-widget ui-corner-all ui-widget-content "+(this.options.tooltipClass||""));return $("<div>").addClass("ui-tooltip-content").appendTo(tooltip),tooltip.appendTo(this.document[0].body),this.tooltips[id]=element,tooltip},_find:function(target){var id=target.data("ui-tooltip-id");return id?$("#"+id):$()},_removeTooltip:function(tooltip){tooltip.remove(),delete this.tooltips[tooltip.attr("id")]},_destroy:function(){var that=this;$.each(this.tooltips,function(id,element){var event=$.Event("blur");event.target=event.currentTarget=element[0],that.close(event,!0),$("#"+id).remove(),element.data("ui-tooltip-title")&&(element.attr("title",element.data("ui-tooltip-title")),element.removeData("ui-tooltip-title"))})}})}(jQuery);