package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3MapRelationship {

        /**
         * The first concept is at a more abstract level than the second concept.  For example, Hepatitis is broader than Hepatitis A, and endocrine disease is broader than Diabetes Mellitus.  Broader than is the opposite of the narrower than relationship.
         */
        BT, 
        /**
         * The two concepts have identical meaning.
         */
        E, 
        /**
         * The first concept is at a more detailed level than the second concept.  For example, Pennicillin G is narrower than Pennicillin, and vellus hair is narrower than hair.  Narrower than is the opposite of broader than.
         */
        NT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3MapRelationship fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BT".equals(codeString))
          return BT;
        if ("E".equals(codeString))
          return E;
        if ("NT".equals(codeString))
          return NT;
        throw new Exception("Unknown V3MapRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BT: return "BT";
            case E: return "E";
            case NT: return "NT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/MapRelationship";
        }
        public String getDefinition() {
          switch (this) {
            case BT: return "The first concept is at a more abstract level than the second concept.  For example, Hepatitis is broader than Hepatitis A, and endocrine disease is broader than Diabetes Mellitus.  Broader than is the opposite of the narrower than relationship.";
            case E: return "The two concepts have identical meaning.";
            case NT: return "The first concept is at a more detailed level than the second concept.  For example, Pennicillin G is narrower than Pennicillin, and vellus hair is narrower than hair.  Narrower than is the opposite of broader than.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BT: return "Broader Than";
            case E: return "Exact";
            case NT: return "Narrower Than";
            default: return "?";
          }
    }


}

