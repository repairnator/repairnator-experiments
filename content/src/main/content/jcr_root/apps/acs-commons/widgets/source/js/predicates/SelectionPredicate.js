/*
 * #%L
 * ACS AEM Commons Package
 * %%
 * Copyright (C) 2013 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
/*global CQ: false, ACS: false */

/**
 * Search predicate form widget which uses a selection to select a property
 * value.
 */
ACS.CQ.wcm.SelectionPredicate = CQ.Ext.extend(CQ.form.CompositeField, {

    /**
     * @cfg {String} propertyName Name of the property.
     */
    propertyName : null,

    /**
     * @cfg {String} predicateName Name of the predicate. Defaults to
     *      'property'.
     */
    predicateName : null,

    /**
     * @cfg {String} options
     */
    options : null,

    constructor : function(config) {
        config = config || {};
        var defaults = {
            "border" : false,
            "predicateName" : "property"
        };
        config = CQ.Util.applyDefaults(config, defaults);
        ACS.CQ.wcm.SelectionPredicate.superclass.constructor.call(this, config);
    },

    initComponent : function() {
        ACS.CQ.wcm.SelectionPredicate.superclass.initComponent.call(this);

        var id = CQ.wcm.PredicateBase.createId(this.predicateName);

        this.add(new CQ.Ext.form.Hidden({
            "name" : id,
            "value" : this.propertyName
        }));

        this.add(new CQ.form.Selection({
            "type" : "select",
            "name" : id + ".value",
            "options" : this.options,
            "triggerClass" : "",
            "hideLabel" : true,
            "anchor" : "100%"
        }));
    }

});
CQ.Ext.reg("selectionpredicate", ACS.CQ.wcm.SelectionPredicate);
