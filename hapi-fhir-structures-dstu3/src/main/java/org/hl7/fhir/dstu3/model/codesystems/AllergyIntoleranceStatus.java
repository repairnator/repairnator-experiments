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

// Generated on Sat, Nov 5, 2016 17:49-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum AllergyIntoleranceStatus {

        /**
         * An active record of a risk of a reaction to the identified substance.
         */
        ACTIVE, 
        /**
         * A high level of certainty about the propensity for a reaction to the identified substance, which may include clinical evidence by testing or rechallenge.
         */
        ACTIVECONFIRMED, 
        /**
         * An inactivated record of a risk of a reaction to the identified substance
         */
        INACTIVE, 
        /**
         * A reaction to the identified substance has been clinically reassessed by testing or re-exposure and considered to be resolved
         */
        RESOLVED, 
        /**
         * A propensity for a reaction to the identified substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.
         */
        REFUTED, 
        /**
         * The statement was entered in error and is not valid.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AllergyIntoleranceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("active-confirmed".equals(codeString))
          return ACTIVECONFIRMED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown AllergyIntoleranceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case ACTIVECONFIRMED: return "active-confirmed";
            case INACTIVE: return "inactive";
            case RESOLVED: return "resolved";
            case REFUTED: return "refuted";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/allergy-intolerance-status";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "An active record of a risk of a reaction to the identified substance.";
            case ACTIVECONFIRMED: return "A high level of certainty about the propensity for a reaction to the identified substance, which may include clinical evidence by testing or rechallenge.";
            case INACTIVE: return "An inactivated record of a risk of a reaction to the identified substance";
            case RESOLVED: return "A reaction to the identified substance has been clinically reassessed by testing or re-exposure and considered to be resolved";
            case REFUTED: return "A propensity for a reaction to the identified substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.";
            case ENTEREDINERROR: return "The statement was entered in error and is not valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case ACTIVECONFIRMED: return "Active Confirmed";
            case INACTIVE: return "Inactive";
            case RESOLVED: return "Resolved";
            case REFUTED: return "Refuted";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
    }


}

