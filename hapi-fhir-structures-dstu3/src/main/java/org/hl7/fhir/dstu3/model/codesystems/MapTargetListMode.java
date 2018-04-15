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

public enum MapTargetListMode {

        /**
         * when the target list is being assembled, the items for this rule go first. If more that one rule defines a first item (for a given instance of mapping) then this is an error
         */
        FIRST, 
        /**
         * the target instance is shared with the target instances generated by another rule (up to the first common n items, then create new ones)
         */
        SHARE, 
        /**
         * when the target list is being assembled, the items for this rule go last. If more that one rule defines a last item (for a given instance of mapping) then this is an error
         */
        LAST, 
        /**
         * re-use the first item in the list, and keep adding content to it
         */
        COLLATE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MapTargetListMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("first".equals(codeString))
          return FIRST;
        if ("share".equals(codeString))
          return SHARE;
        if ("last".equals(codeString))
          return LAST;
        if ("collate".equals(codeString))
          return COLLATE;
        throw new FHIRException("Unknown MapTargetListMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FIRST: return "first";
            case SHARE: return "share";
            case LAST: return "last";
            case COLLATE: return "collate";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/map-target-list-mode";
        }
        public String getDefinition() {
          switch (this) {
            case FIRST: return "when the target list is being assembled, the items for this rule go first. If more that one rule defines a first item (for a given instance of mapping) then this is an error";
            case SHARE: return "the target instance is shared with the target instances generated by another rule (up to the first common n items, then create new ones)";
            case LAST: return "when the target list is being assembled, the items for this rule go last. If more that one rule defines a last item (for a given instance of mapping) then this is an error";
            case COLLATE: return "re-use the first item in the list, and keep adding content to it";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FIRST: return "First";
            case SHARE: return "Share";
            case LAST: return "Last";
            case COLLATE: return "Collate";
            default: return "?";
          }
    }


}

