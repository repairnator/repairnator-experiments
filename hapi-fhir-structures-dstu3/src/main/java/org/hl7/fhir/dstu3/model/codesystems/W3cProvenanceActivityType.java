package org.hl7.fhir.dstu3.model.codesystems;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum W3cProvenanceActivityType {

        /**
         * The completion of production of a new entity by an activity. This entity did not exist before generation and becomes available for usage after this generation. Given that a generation is the completion of production of an entity, it is instantaneous.
         */
        GENERATION, 
        /**
         * the beginning of utilizing an entity by an activity. Before usage, the activity had not begun to utilize this entity and could not have been affected by the entity.  (Note: This definition is formulated for a given usage; it is permitted for an activity to have used a same entity multiple times.) Given that a usage is the beginning of utilizing an entity, it is instantaneous.
         */
        USAGE, 
        /**
         * The exchange of some unspecified entity by two activities, one activity using some entity generated by the other. A communication implies that activity a2 is dependent on another activity, a1, by way of some unspecified entity that is generated by a1 and used by a2.
         */
        COMMUNICATION, 
        /**
         * When an activity is deemed to have been started by an entity, known as trigger. The activity did not exist before its start. Any usage, generation, or invalidation involving an activity follows the activity's start. A start may refer to a trigger entity that set off the activity, or to an activity, known as starter, that generated the trigger. Given that a start is when an activity is deemed to have started, it is instantaneous.
         */
        START, 
        /**
         * When an activity is deemed to have been ended by an entity, known as trigger. The activity no longer exists after its end. Any usage, generation, or invalidation involving an activity precedes the activity's end. An end may refer to a trigger entity that terminated the activity, or to an activity, known as ender that generated the trigger.
         */
        END, 
        /**
         * The start of the destruction, cessation, or expiry of an existing entity by an activity. The entity is no longer available for use (or further invalidation) after invalidation. Any generation or usage of an entity precedes its invalidation. Given that an invalidation is the start of destruction, cessation, or expiry, it is instantaneous.
         */
        INVALIDATION, 
        /**
         * A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a pre-existing entity. For an entity to be transformed from, created from, or resulting from an update to another, there must be some underpinning activity or activities performing the necessary action(s) resulting in such a derivation. A derivation can be described at various levels of precision. In its simplest form, derivation relates two entities. Optionally, attributes can be added to represent further information about the derivation. If the derivation is the result of a single known activity, then this activity can also be optionally expressed. To provide a completely accurate description of the derivation, the generation and usage of the generated and used entities, respectively, can be provided, so as to make the derivation path, through usage, activity, and generation, explicit. Optional information such as activity, generation, and usage can be linked to derivations to aid analysis of provenance and to facilitate provenance-based reproducibility.
         */
        DERIVATION, 
        /**
         * A derivation for which the resulting entity is a revised version of some original. The implication here is that the resulting entity contains substantial content from the original. A revision relation is a kind of derivation relation from a revised entity to a preceding entity.
         */
        REVISION, 
        /**
         * The repeat of (some or all of) an entity, such as text or image, by someone who may or may not be its original author. A quotation relation is a kind of derivation relation, for which an entity was derived from a preceding entity by copying, or 'quoting', some or all of it.
         */
        QUOTATION, 
        /**
         * Refers to something produced by some agent with direct experience and knowledge about the topic, at the time of the topic's study, without benefit from hindsight. Because of the directness of primary sources, they 'speak for themselves' in ways that cannot be captured through the filter of secondary sources. As such, it is important for secondary sources to reference those primary sources from which they were derived, so that their reliability can be investigated. It is also important to note that a given entity might be a primary source for one entity but not another. It is the reason why Primary Source is defined as a relation as opposed to a subtype of Entity.
         */
        PRIMARYSOURCE, 
        /**
         * Ascribing of an entity (object/document) to an agent.
         */
        ATTRIBUTION, 
        /**
         *  An aggregating activity that results in composition of an entity, which provides a structure to some constituents that must themselves be entities. These constituents are said to be member of the collections.
         */
        COLLECTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static W3cProvenanceActivityType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Generation".equals(codeString))
          return GENERATION;
        if ("Usage".equals(codeString))
          return USAGE;
        if ("Communication".equals(codeString))
          return COMMUNICATION;
        if ("Start".equals(codeString))
          return START;
        if ("End".equals(codeString))
          return END;
        if ("Invalidation".equals(codeString))
          return INVALIDATION;
        if ("Derivation".equals(codeString))
          return DERIVATION;
        if ("Revision".equals(codeString))
          return REVISION;
        if ("Quotation".equals(codeString))
          return QUOTATION;
        if ("Primary-Source".equals(codeString))
          return PRIMARYSOURCE;
        if ("Attribution".equals(codeString))
          return ATTRIBUTION;
        if ("Collection".equals(codeString))
          return COLLECTION;
        throw new FHIRException("Unknown W3cProvenanceActivityType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GENERATION: return "Generation";
            case USAGE: return "Usage";
            case COMMUNICATION: return "Communication";
            case START: return "Start";
            case END: return "End";
            case INVALIDATION: return "Invalidation";
            case DERIVATION: return "Derivation";
            case REVISION: return "Revision";
            case QUOTATION: return "Quotation";
            case PRIMARYSOURCE: return "Primary-Source";
            case ATTRIBUTION: return "Attribution";
            case COLLECTION: return "Collection";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/w3c-provenance-activity-type";
        }
        public String getDefinition() {
          switch (this) {
            case GENERATION: return "The completion of production of a new entity by an activity. This entity did not exist before generation and becomes available for usage after this generation. Given that a generation is the completion of production of an entity, it is instantaneous.";
            case USAGE: return "the beginning of utilizing an entity by an activity. Before usage, the activity had not begun to utilize this entity and could not have been affected by the entity.  (Note: This definition is formulated for a given usage; it is permitted for an activity to have used a same entity multiple times.) Given that a usage is the beginning of utilizing an entity, it is instantaneous.";
            case COMMUNICATION: return "The exchange of some unspecified entity by two activities, one activity using some entity generated by the other. A communication implies that activity a2 is dependent on another activity, a1, by way of some unspecified entity that is generated by a1 and used by a2.";
            case START: return "When an activity is deemed to have been started by an entity, known as trigger. The activity did not exist before its start. Any usage, generation, or invalidation involving an activity follows the activity's start. A start may refer to a trigger entity that set off the activity, or to an activity, known as starter, that generated the trigger. Given that a start is when an activity is deemed to have started, it is instantaneous.";
            case END: return "When an activity is deemed to have been ended by an entity, known as trigger. The activity no longer exists after its end. Any usage, generation, or invalidation involving an activity precedes the activity's end. An end may refer to a trigger entity that terminated the activity, or to an activity, known as ender that generated the trigger.";
            case INVALIDATION: return "The start of the destruction, cessation, or expiry of an existing entity by an activity. The entity is no longer available for use (or further invalidation) after invalidation. Any generation or usage of an entity precedes its invalidation. Given that an invalidation is the start of destruction, cessation, or expiry, it is instantaneous.";
            case DERIVATION: return "A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a pre-existing entity. For an entity to be transformed from, created from, or resulting from an update to another, there must be some underpinning activity or activities performing the necessary action(s) resulting in such a derivation. A derivation can be described at various levels of precision. In its simplest form, derivation relates two entities. Optionally, attributes can be added to represent further information about the derivation. If the derivation is the result of a single known activity, then this activity can also be optionally expressed. To provide a completely accurate description of the derivation, the generation and usage of the generated and used entities, respectively, can be provided, so as to make the derivation path, through usage, activity, and generation, explicit. Optional information such as activity, generation, and usage can be linked to derivations to aid analysis of provenance and to facilitate provenance-based reproducibility.";
            case REVISION: return "A derivation for which the resulting entity is a revised version of some original. The implication here is that the resulting entity contains substantial content from the original. A revision relation is a kind of derivation relation from a revised entity to a preceding entity.";
            case QUOTATION: return "The repeat of (some or all of) an entity, such as text or image, by someone who may or may not be its original author. A quotation relation is a kind of derivation relation, for which an entity was derived from a preceding entity by copying, or 'quoting', some or all of it.";
            case PRIMARYSOURCE: return "Refers to something produced by some agent with direct experience and knowledge about the topic, at the time of the topic's study, without benefit from hindsight. Because of the directness of primary sources, they 'speak for themselves' in ways that cannot be captured through the filter of secondary sources. As such, it is important for secondary sources to reference those primary sources from which they were derived, so that their reliability can be investigated. It is also important to note that a given entity might be a primary source for one entity but not another. It is the reason why Primary Source is defined as a relation as opposed to a subtype of Entity.";
            case ATTRIBUTION: return "Ascribing of an entity (object/document) to an agent.";
            case COLLECTION: return " An aggregating activity that results in composition of an entity, which provides a structure to some constituents that must themselves be entities. These constituents are said to be member of the collections.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GENERATION: return "wasGeneratedBy";
            case USAGE: return "used";
            case COMMUNICATION: return "wasInformedBy";
            case START: return "wasStartedBy";
            case END: return "wasEndedBy";
            case INVALIDATION: return "wasInvalidatedBy";
            case DERIVATION: return "wasDerivedFrom";
            case REVISION: return "wasRevisionOf";
            case QUOTATION: return "wasQuotedFrom";
            case PRIMARYSOURCE: return "wasPrimarySourceOf";
            case ATTRIBUTION: return "wasAttributedTo";
            case COLLECTION: return "isCollectionOf";
            default: return "?";
          }
    }


}

