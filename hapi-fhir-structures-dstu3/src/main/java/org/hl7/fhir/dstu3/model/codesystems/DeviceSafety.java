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

// Generated on Thu, Feb 9, 2017 08:03-0500 for FHIR v1.9.0


import org.hl7.fhir.exceptions.FHIRException;

public enum DeviceSafety {

        /**
         * Indicates that the device or packaging contains natural rubber that contacts humans
         */
        CONTAINSLATEX, 
        /**
         * Indicates that natural rubber latex was not used as materials in the manufacture of the medical product and container and the device labeling contains this information.
         */
        LATEXFREE, 
        /**
         * Indicates that whether the device or packaging contains natural rubber that contacts humans is not indicated on the label Not all medical products that are NOT made with natural rubber latex will be marked
         */
        LATEXUNKNOWN, 
        /**
         * The device, when used in the MRI environment, has been demonstrated to present no additional risk to the patient or other individual, but may affect the quality of the diagnostic information. The MRI conditions in which the device was tested should be specified in conjunction with the term MR safe since a device which is safe under one set of conditions may not be found to be so under more extreme MRI conditions.
         */
        MRSAFE, 
        /**
         * An item that is known to pose hazards in all MRI environments. MR Unsafe items include magnetic items such as a pair of ferromagnetic scissors.
         */
        MRUNSAFE, 
        /**
         * An item that has been demonstrated to pose no known hazards in a specified MRI environment with specified conditions of use. Field conditions that define the MRI environment include, for instance, static magnetic field or specific absorption rate (SAR).
         */
        MRCONDITIONAL, 
        /**
         * Labeling does not contain MRI Safety information
         */
        MRUNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceSafety fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contains-latex".equals(codeString))
          return CONTAINSLATEX;
        if ("latex-free".equals(codeString))
          return LATEXFREE;
        if ("latex-unknown".equals(codeString))
          return LATEXUNKNOWN;
        if ("mr-safe".equals(codeString))
          return MRSAFE;
        if ("mr-unsafe".equals(codeString))
          return MRUNSAFE;
        if ("mr-conditional".equals(codeString))
          return MRCONDITIONAL;
        if ("mr-unknown".equals(codeString))
          return MRUNKNOWN;
        throw new FHIRException("Unknown DeviceSafety code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONTAINSLATEX: return "contains-latex";
            case LATEXFREE: return "latex-free";
            case LATEXUNKNOWN: return "latex-unknown";
            case MRSAFE: return "mr-safe";
            case MRUNSAFE: return "mr-unsafe";
            case MRCONDITIONAL: return "mr-conditional";
            case MRUNKNOWN: return "mr-unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/device-safety";
        }
        public String getDefinition() {
          switch (this) {
            case CONTAINSLATEX: return "Indicates that the device or packaging contains natural rubber that contacts humans";
            case LATEXFREE: return "Indicates that natural rubber latex was not used as materials in the manufacture of the medical product and container and the device labeling contains this information.";
            case LATEXUNKNOWN: return "Indicates that whether the device or packaging contains natural rubber that contacts humans is not indicated on the label Not all medical products that are NOT made with natural rubber latex will be marked";
            case MRSAFE: return "The device, when used in the MRI environment, has been demonstrated to present no additional risk to the patient or other individual, but may affect the quality of the diagnostic information. The MRI conditions in which the device was tested should be specified in conjunction with the term MR safe since a device which is safe under one set of conditions may not be found to be so under more extreme MRI conditions.";
            case MRUNSAFE: return "An item that is known to pose hazards in all MRI environments. MR Unsafe items include magnetic items such as a pair of ferromagnetic scissors.";
            case MRCONDITIONAL: return "An item that has been demonstrated to pose no known hazards in a specified MRI environment with specified conditions of use. Field conditions that define the MRI environment include, for instance, static magnetic field or specific absorption rate (SAR).";
            case MRUNKNOWN: return "Labeling does not contain MRI Safety information";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTAINSLATEX: return "Contains Latex";
            case LATEXFREE: return "Latex Free";
            case LATEXUNKNOWN: return "Latex Unknown";
            case MRSAFE: return "MR Safe";
            case MRUNSAFE: return "MR Unsafe";
            case MRCONDITIONAL: return "MR Conditional";
            case MRUNKNOWN: return "MR Unknown";
            default: return "?";
          }
    }


}

