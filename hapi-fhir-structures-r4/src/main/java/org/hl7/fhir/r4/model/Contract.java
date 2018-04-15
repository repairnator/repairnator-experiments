package org.hl7.fhir.r4.model;

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

// Generated on Thu, Mar 1, 2018 20:26+1100 for FHIR v3.2.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
 */
@ResourceDef(name="Contract", profile="http://hl7.org/fhir/Profile/Contract")
public class Contract extends DomainResource {

    public enum ContractStatus {
        /**
         * Contract is augmented with additional information to correct errors in a predecessor or to updated values in a predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: revised; replaced.
         */
        AMENDED, 
        /**
         * Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.
         */
        APPENDED, 
        /**
         * Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; aborted.
         */
        CANCELLED, 
        /**
         * Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7.Comparable FHIR and v.3 status codes: on hold; pended; suspended.
         */
        DISPUTED, 
        /**
         * Contract was created in error. No Precedence Order.  Status may be applied to a Contract with any status.
         */
        ENTEREDINERROR, 
        /**
         * Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant signs the insurers application, which references the policy. Usage: Optional first step of contract execution activity.  May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable FHIR and v.3 status codes: draft; preliminary; planned; intended; active.
         */
        EXECUTABLE, 
        /**
         * Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state for normal completion of contracting activity.  Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; completed.
         */
        EXECUTED, 
        /**
         * Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised contract provisions. I.e., where the party which has not signed proposes changes to the terms.  E .g., a life insurer declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.
         */
        NEGOTIABLE, 
        /**
         * Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template','form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.
         */
        OFFERED, 
        /**
         * Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health insurance policy; consent directive policy.  Usage: Required initial contract activity, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: proposed; intended.
         */
        POLICY, 
        /**
         *  Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. Precedence Order = 5. Comparable FHIR and v.3 status codes:  stopped; cancelled.
         */
        REJECTED, 
        /**
         * Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: superseded.
         */
        RENEWED, 
        /**
         * A Contract that is rescinded.  May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 status codes: nullified.
         */
        REVOKED, 
        /**
         * Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or where the policy is substantially different from and did not accompany the application/form so that the applicant could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.
         */
        RESOLVED, 
        /**
         * Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.
         */
        TERMINATED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ContractStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("appended".equals(codeString))
          return APPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("disputed".equals(codeString))
          return DISPUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("executable".equals(codeString))
          return EXECUTABLE;
        if ("executed".equals(codeString))
          return EXECUTED;
        if ("negotiable".equals(codeString))
          return NEGOTIABLE;
        if ("offered".equals(codeString))
          return OFFERED;
        if ("policy".equals(codeString))
          return POLICY;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("renewed".equals(codeString))
          return RENEWED;
        if ("revoked".equals(codeString))
          return REVOKED;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if ("terminated".equals(codeString))
          return TERMINATED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ContractStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AMENDED: return "amended";
            case APPENDED: return "appended";
            case CANCELLED: return "cancelled";
            case DISPUTED: return "disputed";
            case ENTEREDINERROR: return "entered-in-error";
            case EXECUTABLE: return "executable";
            case EXECUTED: return "executed";
            case NEGOTIABLE: return "negotiable";
            case OFFERED: return "offered";
            case POLICY: return "policy";
            case REJECTED: return "rejected";
            case RENEWED: return "renewed";
            case REVOKED: return "revoked";
            case RESOLVED: return "resolved";
            case TERMINATED: return "terminated";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AMENDED: return "http://hl7.org/fhir/contract-status";
            case APPENDED: return "http://hl7.org/fhir/contract-status";
            case CANCELLED: return "http://hl7.org/fhir/contract-status";
            case DISPUTED: return "http://hl7.org/fhir/contract-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/contract-status";
            case EXECUTABLE: return "http://hl7.org/fhir/contract-status";
            case EXECUTED: return "http://hl7.org/fhir/contract-status";
            case NEGOTIABLE: return "http://hl7.org/fhir/contract-status";
            case OFFERED: return "http://hl7.org/fhir/contract-status";
            case POLICY: return "http://hl7.org/fhir/contract-status";
            case REJECTED: return "http://hl7.org/fhir/contract-status";
            case RENEWED: return "http://hl7.org/fhir/contract-status";
            case REVOKED: return "http://hl7.org/fhir/contract-status";
            case RESOLVED: return "http://hl7.org/fhir/contract-status";
            case TERMINATED: return "http://hl7.org/fhir/contract-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AMENDED: return "Contract is augmented with additional information to correct errors in a predecessor or to updated values in a predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: revised; replaced.";
            case APPENDED: return "Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.";
            case CANCELLED: return "Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; aborted.";
            case DISPUTED: return "Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7.Comparable FHIR and v.3 status codes: on hold; pended; suspended.";
            case ENTEREDINERROR: return "Contract was created in error. No Precedence Order.  Status may be applied to a Contract with any status.";
            case EXECUTABLE: return "Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant signs the insurers application, which references the policy. Usage: Optional first step of contract execution activity.  May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable FHIR and v.3 status codes: draft; preliminary; planned; intended; active.";
            case EXECUTED: return "Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state for normal completion of contracting activity.  Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; completed.";
            case NEGOTIABLE: return "Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised contract provisions. I.e., where the party which has not signed proposes changes to the terms.  E .g., a life insurer declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.";
            case OFFERED: return "Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template','form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.";
            case POLICY: return "Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health insurance policy; consent directive policy.  Usage: Required initial contract activity, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: proposed; intended.";
            case REJECTED: return " Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. Precedence Order = 5. Comparable FHIR and v.3 status codes:  stopped; cancelled.";
            case RENEWED: return "Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: superseded.";
            case REVOKED: return "A Contract that is rescinded.  May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 status codes: nullified.";
            case RESOLVED: return "Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or where the policy is substantially different from and did not accompany the application/form so that the applicant could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.";
            case TERMINATED: return "Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AMENDED: return "Amended";
            case APPENDED: return "Appended";
            case CANCELLED: return "Cancelled";
            case DISPUTED: return "Disputed";
            case ENTEREDINERROR: return "Entered in Error";
            case EXECUTABLE: return "Executable";
            case EXECUTED: return "Executed";
            case NEGOTIABLE: return "Negotiable";
            case OFFERED: return "Offered";
            case POLICY: return "Policy";
            case REJECTED: return "Rejected";
            case RENEWED: return "Renewed";
            case REVOKED: return "Revoked";
            case RESOLVED: return "Resolved";
            case TERMINATED: return "Terminated";
            default: return "?";
          }
        }
    }

  public static class ContractStatusEnumFactory implements EnumFactory<ContractStatus> {
    public ContractStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("amended".equals(codeString))
          return ContractStatus.AMENDED;
        if ("appended".equals(codeString))
          return ContractStatus.APPENDED;
        if ("cancelled".equals(codeString))
          return ContractStatus.CANCELLED;
        if ("disputed".equals(codeString))
          return ContractStatus.DISPUTED;
        if ("entered-in-error".equals(codeString))
          return ContractStatus.ENTEREDINERROR;
        if ("executable".equals(codeString))
          return ContractStatus.EXECUTABLE;
        if ("executed".equals(codeString))
          return ContractStatus.EXECUTED;
        if ("negotiable".equals(codeString))
          return ContractStatus.NEGOTIABLE;
        if ("offered".equals(codeString))
          return ContractStatus.OFFERED;
        if ("policy".equals(codeString))
          return ContractStatus.POLICY;
        if ("rejected".equals(codeString))
          return ContractStatus.REJECTED;
        if ("renewed".equals(codeString))
          return ContractStatus.RENEWED;
        if ("revoked".equals(codeString))
          return ContractStatus.REVOKED;
        if ("resolved".equals(codeString))
          return ContractStatus.RESOLVED;
        if ("terminated".equals(codeString))
          return ContractStatus.TERMINATED;
        throw new IllegalArgumentException("Unknown ContractStatus code '"+codeString+"'");
        }
        public Enumeration<ContractStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ContractStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("amended".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.AMENDED);
        if ("appended".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.APPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.CANCELLED);
        if ("disputed".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.DISPUTED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.ENTEREDINERROR);
        if ("executable".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.EXECUTABLE);
        if ("executed".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.EXECUTED);
        if ("negotiable".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.NEGOTIABLE);
        if ("offered".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.OFFERED);
        if ("policy".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.POLICY);
        if ("rejected".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.REJECTED);
        if ("renewed".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.RENEWED);
        if ("revoked".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.REVOKED);
        if ("resolved".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.RESOLVED);
        if ("terminated".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.TERMINATED);
        throw new FHIRException("Unknown ContractStatus code '"+codeString+"'");
        }
    public String toCode(ContractStatus code) {
      if (code == ContractStatus.AMENDED)
        return "amended";
      if (code == ContractStatus.APPENDED)
        return "appended";
      if (code == ContractStatus.CANCELLED)
        return "cancelled";
      if (code == ContractStatus.DISPUTED)
        return "disputed";
      if (code == ContractStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ContractStatus.EXECUTABLE)
        return "executable";
      if (code == ContractStatus.EXECUTED)
        return "executed";
      if (code == ContractStatus.NEGOTIABLE)
        return "negotiable";
      if (code == ContractStatus.OFFERED)
        return "offered";
      if (code == ContractStatus.POLICY)
        return "policy";
      if (code == ContractStatus.REJECTED)
        return "rejected";
      if (code == ContractStatus.RENEWED)
        return "renewed";
      if (code == ContractStatus.REVOKED)
        return "revoked";
      if (code == ContractStatus.RESOLVED)
        return "resolved";
      if (code == ContractStatus.TERMINATED)
        return "terminated";
      return "?";
      }
    public String toSystem(ContractStatus code) {
      return code.getSystem();
      }
    }

    public enum ContractDataMeaning {
        /**
         * The consent applies directly to the instance of the resource
         */
        INSTANCE, 
        /**
         * The consent applies directly to the instance of the resource and instances it refers to
         */
        RELATED, 
        /**
         * The consent applies directly to the instance of the resource and instances that refer to it
         */
        DEPENDENTS, 
        /**
         * The consent applies to instances of resources that are authored by
         */
        AUTHOREDBY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ContractDataMeaning fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return INSTANCE;
        if ("related".equals(codeString))
          return RELATED;
        if ("dependents".equals(codeString))
          return DEPENDENTS;
        if ("authoredby".equals(codeString))
          return AUTHOREDBY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ContractDataMeaning code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "instance";
            case RELATED: return "related";
            case DEPENDENTS: return "dependents";
            case AUTHOREDBY: return "authoredby";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INSTANCE: return "http://hl7.org/fhir/contract-data-meaning";
            case RELATED: return "http://hl7.org/fhir/contract-data-meaning";
            case DEPENDENTS: return "http://hl7.org/fhir/contract-data-meaning";
            case AUTHOREDBY: return "http://hl7.org/fhir/contract-data-meaning";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "The consent applies directly to the instance of the resource";
            case RELATED: return "The consent applies directly to the instance of the resource and instances it refers to";
            case DEPENDENTS: return "The consent applies directly to the instance of the resource and instances that refer to it";
            case AUTHOREDBY: return "The consent applies to instances of resources that are authored by";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "Instance";
            case RELATED: return "Related";
            case DEPENDENTS: return "Dependents";
            case AUTHOREDBY: return "AuthoredBy";
            default: return "?";
          }
        }
    }

  public static class ContractDataMeaningEnumFactory implements EnumFactory<ContractDataMeaning> {
    public ContractDataMeaning fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return ContractDataMeaning.INSTANCE;
        if ("related".equals(codeString))
          return ContractDataMeaning.RELATED;
        if ("dependents".equals(codeString))
          return ContractDataMeaning.DEPENDENTS;
        if ("authoredby".equals(codeString))
          return ContractDataMeaning.AUTHOREDBY;
        throw new IllegalArgumentException("Unknown ContractDataMeaning code '"+codeString+"'");
        }
        public Enumeration<ContractDataMeaning> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ContractDataMeaning>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("instance".equals(codeString))
          return new Enumeration<ContractDataMeaning>(this, ContractDataMeaning.INSTANCE);
        if ("related".equals(codeString))
          return new Enumeration<ContractDataMeaning>(this, ContractDataMeaning.RELATED);
        if ("dependents".equals(codeString))
          return new Enumeration<ContractDataMeaning>(this, ContractDataMeaning.DEPENDENTS);
        if ("authoredby".equals(codeString))
          return new Enumeration<ContractDataMeaning>(this, ContractDataMeaning.AUTHOREDBY);
        throw new FHIRException("Unknown ContractDataMeaning code '"+codeString+"'");
        }
    public String toCode(ContractDataMeaning code) {
      if (code == ContractDataMeaning.INSTANCE)
        return "instance";
      if (code == ContractDataMeaning.RELATED)
        return "related";
      if (code == ContractDataMeaning.DEPENDENTS)
        return "dependents";
      if (code == ContractDataMeaning.AUTHOREDBY)
        return "authoredby";
      return "?";
      }
    public String toSystem(ContractDataMeaning code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class TermComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique identifier for this particular Contract Provision.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Number", formalDefinition="Unique identifier for this particular Contract Provision." )
        protected Identifier identifier;

        /**
         * When this Contract Provision was issued.
         */
        @Child(name = "issued", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Issue Date Time", formalDefinition="When this Contract Provision was issued." )
        protected DateTimeType issued;

        /**
         * Relevant time or time-period when this Contract Provision is applicable.
         */
        @Child(name = "applies", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Effective Time", formalDefinition="Relevant time or time-period when this Contract Provision is applicable." )
        protected Period applies;

        /**
         * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Type or Form", formalDefinition="Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-term-type")
        protected CodeableConcept type;

        /**
         * Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.
         */
        @Child(name = "subType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Type specific classification", formalDefinition="Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-term-subtype")
        protected CodeableConcept subType;

        /**
         * The matter of concern in the context of this provision of the agrement.
         */
        @Child(name = "offer", type = {}, order=6, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Context of the Contract term", formalDefinition="The matter of concern in the context of this provision of the agrement." )
        protected ContractOfferComponent offer;

        /**
         * Contract Term Asset List.
         */
        @Child(name = "asset", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Asset List", formalDefinition="Contract Term Asset List." )
        protected List<ContractAssetComponent> asset;

        /**
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.
         */
        @Child(name = "agent", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Entity being ascribed responsibility", formalDefinition="An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place." )
        protected List<AgentComponent> agent;

        /**
         * Action stipulated by this Contract Provision.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Activity", formalDefinition="Action stipulated by this Contract Provision." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-action")
        protected List<CodeableConcept> action;

        /**
         * Reason or purpose for the action stipulated by this Contract Provision.
         */
        @Child(name = "actionReason", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Purpose for the Contract Term Action", formalDefinition="Reason or purpose for the action stipulated by this Contract Provision." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-PurposeOfUse")
        protected List<CodeableConcept> actionReason;

        /**
         * Nested group of Contract Provisions.
         */
        @Child(name = "group", type = {TermComponent.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Contract Term Group", formalDefinition="Nested group of Contract Provisions." )
        protected List<TermComponent> group;

        private static final long serialVersionUID = 1344727862L;

    /**
     * Constructor
     */
      public TermComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TermComponent(ContractOfferComponent offer) {
        super();
        this.offer = offer;
      }

        /**
         * @return {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public TermComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #issued} (When this Contract Provision was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public DateTimeType getIssuedElement() { 
          if (this.issued == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.issued");
            else if (Configuration.doAutoCreate())
              this.issued = new DateTimeType(); // bb
          return this.issued;
        }

        public boolean hasIssuedElement() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        public boolean hasIssued() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        /**
         * @param value {@link #issued} (When this Contract Provision was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public TermComponent setIssuedElement(DateTimeType value) { 
          this.issued = value;
          return this;
        }

        /**
         * @return When this Contract Provision was issued.
         */
        public Date getIssued() { 
          return this.issued == null ? null : this.issued.getValue();
        }

        /**
         * @param value When this Contract Provision was issued.
         */
        public TermComponent setIssued(Date value) { 
          if (value == null)
            this.issued = null;
          else {
            if (this.issued == null)
              this.issued = new DateTimeType();
            this.issued.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #applies} (Relevant time or time-period when this Contract Provision is applicable.)
         */
        public Period getApplies() { 
          if (this.applies == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.applies");
            else if (Configuration.doAutoCreate())
              this.applies = new Period(); // cc
          return this.applies;
        }

        public boolean hasApplies() { 
          return this.applies != null && !this.applies.isEmpty();
        }

        /**
         * @param value {@link #applies} (Relevant time or time-period when this Contract Provision is applicable.)
         */
        public TermComponent setApplies(Period value) { 
          this.applies = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public TermComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subType} (Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.)
         */
        public CodeableConcept getSubType() { 
          if (this.subType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.subType");
            else if (Configuration.doAutoCreate())
              this.subType = new CodeableConcept(); // cc
          return this.subType;
        }

        public boolean hasSubType() { 
          return this.subType != null && !this.subType.isEmpty();
        }

        /**
         * @param value {@link #subType} (Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.)
         */
        public TermComponent setSubType(CodeableConcept value) { 
          this.subType = value;
          return this;
        }

        /**
         * @return {@link #offer} (The matter of concern in the context of this provision of the agrement.)
         */
        public ContractOfferComponent getOffer() { 
          if (this.offer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.offer");
            else if (Configuration.doAutoCreate())
              this.offer = new ContractOfferComponent(); // cc
          return this.offer;
        }

        public boolean hasOffer() { 
          return this.offer != null && !this.offer.isEmpty();
        }

        /**
         * @param value {@link #offer} (The matter of concern in the context of this provision of the agrement.)
         */
        public TermComponent setOffer(ContractOfferComponent value) { 
          this.offer = value;
          return this;
        }

        /**
         * @return {@link #asset} (Contract Term Asset List.)
         */
        public List<ContractAssetComponent> getAsset() { 
          if (this.asset == null)
            this.asset = new ArrayList<ContractAssetComponent>();
          return this.asset;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setAsset(List<ContractAssetComponent> theAsset) { 
          this.asset = theAsset;
          return this;
        }

        public boolean hasAsset() { 
          if (this.asset == null)
            return false;
          for (ContractAssetComponent item : this.asset)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ContractAssetComponent addAsset() { //3
          ContractAssetComponent t = new ContractAssetComponent();
          if (this.asset == null)
            this.asset = new ArrayList<ContractAssetComponent>();
          this.asset.add(t);
          return t;
        }

        public TermComponent addAsset(ContractAssetComponent t) { //3
          if (t == null)
            return this;
          if (this.asset == null)
            this.asset = new ArrayList<ContractAssetComponent>();
          this.asset.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #asset}, creating it if it does not already exist
         */
        public ContractAssetComponent getAssetFirstRep() { 
          if (getAsset().isEmpty()) {
            addAsset();
          }
          return getAsset().get(0);
        }

        /**
         * @return {@link #agent} (An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.)
         */
        public List<AgentComponent> getAgent() { 
          if (this.agent == null)
            this.agent = new ArrayList<AgentComponent>();
          return this.agent;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setAgent(List<AgentComponent> theAgent) { 
          this.agent = theAgent;
          return this;
        }

        public boolean hasAgent() { 
          if (this.agent == null)
            return false;
          for (AgentComponent item : this.agent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AgentComponent addAgent() { //3
          AgentComponent t = new AgentComponent();
          if (this.agent == null)
            this.agent = new ArrayList<AgentComponent>();
          this.agent.add(t);
          return t;
        }

        public TermComponent addAgent(AgentComponent t) { //3
          if (t == null)
            return this;
          if (this.agent == null)
            this.agent = new ArrayList<AgentComponent>();
          this.agent.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #agent}, creating it if it does not already exist
         */
        public AgentComponent getAgentFirstRep() { 
          if (getAgent().isEmpty()) {
            addAgent();
          }
          return getAgent().get(0);
        }

        /**
         * @return {@link #action} (Action stipulated by this Contract Provision.)
         */
        public List<CodeableConcept> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setAction(List<CodeableConcept> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (CodeableConcept item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAction() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return t;
        }

        public TermComponent addAction(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public CodeableConcept getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        /**
         * @return {@link #actionReason} (Reason or purpose for the action stipulated by this Contract Provision.)
         */
        public List<CodeableConcept> getActionReason() { 
          if (this.actionReason == null)
            this.actionReason = new ArrayList<CodeableConcept>();
          return this.actionReason;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setActionReason(List<CodeableConcept> theActionReason) { 
          this.actionReason = theActionReason;
          return this;
        }

        public boolean hasActionReason() { 
          if (this.actionReason == null)
            return false;
          for (CodeableConcept item : this.actionReason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addActionReason() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.actionReason == null)
            this.actionReason = new ArrayList<CodeableConcept>();
          this.actionReason.add(t);
          return t;
        }

        public TermComponent addActionReason(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.actionReason == null)
            this.actionReason = new ArrayList<CodeableConcept>();
          this.actionReason.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #actionReason}, creating it if it does not already exist
         */
        public CodeableConcept getActionReasonFirstRep() { 
          if (getActionReason().isEmpty()) {
            addActionReason();
          }
          return getActionReason().get(0);
        }

        /**
         * @return {@link #group} (Nested group of Contract Provisions.)
         */
        public List<TermComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          return this.group;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setGroup(List<TermComponent> theGroup) { 
          this.group = theGroup;
          return this;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (TermComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TermComponent addGroup() { //3
          TermComponent t = new TermComponent();
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          this.group.add(t);
          return t;
        }

        public TermComponent addGroup(TermComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          this.group.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #group}, creating it if it does not already exist
         */
        public TermComponent getGroupFirstRep() { 
          if (getGroup().isEmpty()) {
            addGroup();
          }
          return getGroup().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, 1, identifier));
          children.add(new Property("issued", "dateTime", "When this Contract Provision was issued.", 0, 1, issued));
          children.add(new Property("applies", "Period", "Relevant time or time-period when this Contract Provision is applicable.", 0, 1, applies));
          children.add(new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, 1, type));
          children.add(new Property("subType", "CodeableConcept", "Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.", 0, 1, subType));
          children.add(new Property("offer", "", "The matter of concern in the context of this provision of the agrement.", 0, 1, offer));
          children.add(new Property("asset", "", "Contract Term Asset List.", 0, java.lang.Integer.MAX_VALUE, asset));
          children.add(new Property("agent", "", "An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, agent));
          children.add(new Property("action", "CodeableConcept", "Action stipulated by this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, action));
          children.add(new Property("actionReason", "CodeableConcept", "Reason or purpose for the action stipulated by this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, actionReason));
          children.add(new Property("group", "@Contract.term", "Nested group of Contract Provisions.", 0, java.lang.Integer.MAX_VALUE, group));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, 1, identifier);
          case -1179159893: /*issued*/  return new Property("issued", "dateTime", "When this Contract Provision was issued.", 0, 1, issued);
          case -793235316: /*applies*/  return new Property("applies", "Period", "Relevant time or time-period when this Contract Provision is applicable.", 0, 1, applies);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, 1, type);
          case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.", 0, 1, subType);
          case 105650780: /*offer*/  return new Property("offer", "", "The matter of concern in the context of this provision of the agrement.", 0, 1, offer);
          case 93121264: /*asset*/  return new Property("asset", "", "Contract Term Asset List.", 0, java.lang.Integer.MAX_VALUE, asset);
          case 92750597: /*agent*/  return new Property("agent", "", "An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, agent);
          case -1422950858: /*action*/  return new Property("action", "CodeableConcept", "Action stipulated by this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, action);
          case 1465121818: /*actionReason*/  return new Property("actionReason", "CodeableConcept", "Reason or purpose for the action stipulated by this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, actionReason);
          case 98629247: /*group*/  return new Property("group", "@Contract.term", "Nested group of Contract Provisions.", 0, java.lang.Integer.MAX_VALUE, group);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // DateTimeType
        case -793235316: /*applies*/ return this.applies == null ? new Base[0] : new Base[] {this.applies}; // Period
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : new Base[] {this.subType}; // CodeableConcept
        case 105650780: /*offer*/ return this.offer == null ? new Base[0] : new Base[] {this.offer}; // ContractOfferComponent
        case 93121264: /*asset*/ return this.asset == null ? new Base[0] : this.asset.toArray(new Base[this.asset.size()]); // ContractAssetComponent
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // AgentComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // CodeableConcept
        case 1465121818: /*actionReason*/ return this.actionReason == null ? new Base[0] : this.actionReason.toArray(new Base[this.actionReason.size()]); // CodeableConcept
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // TermComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -1179159893: // issued
          this.issued = castToDateTime(value); // DateTimeType
          return value;
        case -793235316: // applies
          this.applies = castToPeriod(value); // Period
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.subType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 105650780: // offer
          this.offer = (ContractOfferComponent) value; // ContractOfferComponent
          return value;
        case 93121264: // asset
          this.getAsset().add((ContractAssetComponent) value); // ContractAssetComponent
          return value;
        case 92750597: // agent
          this.getAgent().add((AgentComponent) value); // AgentComponent
          return value;
        case -1422950858: // action
          this.getAction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1465121818: // actionReason
          this.getActionReason().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 98629247: // group
          this.getGroup().add((TermComponent) value); // TermComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("issued")) {
          this.issued = castToDateTime(value); // DateTimeType
        } else if (name.equals("applies")) {
          this.applies = castToPeriod(value); // Period
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.subType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("offer")) {
          this.offer = (ContractOfferComponent) value; // ContractOfferComponent
        } else if (name.equals("asset")) {
          this.getAsset().add((ContractAssetComponent) value);
        } else if (name.equals("agent")) {
          this.getAgent().add((AgentComponent) value);
        } else if (name.equals("action")) {
          this.getAction().add(castToCodeableConcept(value));
        } else if (name.equals("actionReason")) {
          this.getActionReason().add(castToCodeableConcept(value));
        } else if (name.equals("group")) {
          this.getGroup().add((TermComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -1179159893:  return getIssuedElement();
        case -793235316:  return getApplies(); 
        case 3575610:  return getType(); 
        case -1868521062:  return getSubType(); 
        case 105650780:  return getOffer(); 
        case 93121264:  return addAsset(); 
        case 92750597:  return addAgent(); 
        case -1422950858:  return addAction(); 
        case 1465121818:  return addActionReason(); 
        case 98629247:  return addGroup(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1179159893: /*issued*/ return new String[] {"dateTime"};
        case -793235316: /*applies*/ return new String[] {"Period"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case 105650780: /*offer*/ return new String[] {};
        case 93121264: /*asset*/ return new String[] {};
        case 92750597: /*agent*/ return new String[] {};
        case -1422950858: /*action*/ return new String[] {"CodeableConcept"};
        case 1465121818: /*actionReason*/ return new String[] {"CodeableConcept"};
        case 98629247: /*group*/ return new String[] {"@Contract.term"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("offer")) {
          this.offer = new ContractOfferComponent();
          return this.offer;
        }
        else if (name.equals("asset")) {
          return addAsset();
        }
        else if (name.equals("agent")) {
          return addAgent();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("actionReason")) {
          return addActionReason();
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else
          return super.addChild(name);
      }

      public TermComponent copy() {
        TermComponent dst = new TermComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        dst.offer = offer == null ? null : offer.copy();
        if (asset != null) {
          dst.asset = new ArrayList<ContractAssetComponent>();
          for (ContractAssetComponent i : asset)
            dst.asset.add(i.copy());
        };
        if (agent != null) {
          dst.agent = new ArrayList<AgentComponent>();
          for (AgentComponent i : agent)
            dst.agent.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (actionReason != null) {
          dst.actionReason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : actionReason)
            dst.actionReason.add(i.copy());
        };
        if (group != null) {
          dst.group = new ArrayList<TermComponent>();
          for (TermComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof TermComponent))
          return false;
        TermComponent o = (TermComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true)
           && compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true) && compareDeep(offer, o.offer, true)
           && compareDeep(asset, o.asset, true) && compareDeep(agent, o.agent, true) && compareDeep(action, o.action, true)
           && compareDeep(actionReason, o.actionReason, true) && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof TermComponent))
          return false;
        TermComponent o = (TermComponent) other_;
        return compareValues(issued, o.issued, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, issued, applies
          , type, subType, offer, asset, agent, action, actionReason, group);
      }

  public String fhirType() {
    return "Contract.term";

  }

  }

    @Block()
    public static class ContractOfferComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).
         */
        @Child(name = "topic", type = {Reference.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Negotiable offer asset", formalDefinition="The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30)." )
        protected Reference topic;

        /**
         * The actual object that is the target of the reference (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        protected Resource topicTarget;

        /**
         * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Offer Type or Form", formalDefinition="Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-term-type")
        protected CodeableConcept type;

        /**
         * The type of decision made by a grantor with respect to an offer made by a grantee.
         */
        @Child(name = "decision", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Decision by Grantor", formalDefinition="The type of decision made by a grantor with respect to an offer made by a grantee." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActConsentDirective")
        protected CodeableConcept decision;

        /**
         * Human readable form of this Contract Offer.
         */
        @Child(name = "text", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human readable offer text", formalDefinition="Human readable form of this Contract Offer." )
        protected StringType text;

        /**
         * The id of the clause or question text of the offer in the referenced questionnaire/response.
         */
        @Child(name = "linkId", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to text", formalDefinition="The id of the clause or question text of the offer in the referenced questionnaire/response." )
        protected StringType linkId;

        private static final long serialVersionUID = 1062084590L;

    /**
     * Constructor
     */
      public ContractOfferComponent() {
        super();
      }

        /**
         * @return {@link #topic} (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public Reference getTopic() { 
          if (this.topic == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.topic");
            else if (Configuration.doAutoCreate())
              this.topic = new Reference(); // cc
          return this.topic;
        }

        public boolean hasTopic() { 
          return this.topic != null && !this.topic.isEmpty();
        }

        /**
         * @param value {@link #topic} (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public ContractOfferComponent setTopic(Reference value) { 
          this.topic = value;
          return this;
        }

        /**
         * @return {@link #topic} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public Resource getTopicTarget() { 
          return this.topicTarget;
        }

        /**
         * @param value {@link #topic} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public ContractOfferComponent setTopicTarget(Resource value) { 
          this.topicTarget = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public ContractOfferComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #decision} (The type of decision made by a grantor with respect to an offer made by a grantee.)
         */
        public CodeableConcept getDecision() { 
          if (this.decision == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.decision");
            else if (Configuration.doAutoCreate())
              this.decision = new CodeableConcept(); // cc
          return this.decision;
        }

        public boolean hasDecision() { 
          return this.decision != null && !this.decision.isEmpty();
        }

        /**
         * @param value {@link #decision} (The type of decision made by a grantor with respect to an offer made by a grantee.)
         */
        public ContractOfferComponent setDecision(CodeableConcept value) { 
          this.decision = value;
          return this;
        }

        /**
         * @return {@link #text} (Human readable form of this Contract Offer.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Human readable form of this Contract Offer.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ContractOfferComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Human readable form of this Contract Offer.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Human readable form of this Contract Offer.
         */
        public ContractOfferComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #linkId} (The id of the clause or question text of the offer in the referenced questionnaire/response.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public StringType getLinkIdElement() { 
          if (this.linkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.linkId");
            else if (Configuration.doAutoCreate())
              this.linkId = new StringType(); // bb
          return this.linkId;
        }

        public boolean hasLinkIdElement() { 
          return this.linkId != null && !this.linkId.isEmpty();
        }

        public boolean hasLinkId() { 
          return this.linkId != null && !this.linkId.isEmpty();
        }

        /**
         * @param value {@link #linkId} (The id of the clause or question text of the offer in the referenced questionnaire/response.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public ContractOfferComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return The id of the clause or question text of the offer in the referenced questionnaire/response.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value The id of the clause or question text of the offer in the referenced questionnaire/response.
         */
        public ContractOfferComponent setLinkId(String value) { 
          if (Utilities.noString(value))
            this.linkId = null;
          else {
            if (this.linkId == null)
              this.linkId = new StringType();
            this.linkId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("topic", "Reference(Any)", "The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).", 0, 1, topic));
          children.add(new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, 1, type));
          children.add(new Property("decision", "CodeableConcept", "The type of decision made by a grantor with respect to an offer made by a grantee.", 0, 1, decision));
          children.add(new Property("text", "string", "Human readable form of this Contract Offer.", 0, 1, text));
          children.add(new Property("linkId", "string", "The id of the clause or question text of the offer in the referenced questionnaire/response.", 0, 1, linkId));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 110546223: /*topic*/  return new Property("topic", "Reference(Any)", "The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).", 0, 1, topic);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, 1, type);
          case 565719004: /*decision*/  return new Property("decision", "CodeableConcept", "The type of decision made by a grantor with respect to an offer made by a grantee.", 0, 1, decision);
          case 3556653: /*text*/  return new Property("text", "string", "Human readable form of this Contract Offer.", 0, 1, text);
          case -1102667083: /*linkId*/  return new Property("linkId", "string", "The id of the clause or question text of the offer in the referenced questionnaire/response.", 0, 1, linkId);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : new Base[] {this.topic}; // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 565719004: /*decision*/ return this.decision == null ? new Base[0] : new Base[] {this.decision}; // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : new Base[] {this.linkId}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 110546223: // topic
          this.topic = castToReference(value); // Reference
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 565719004: // decision
          this.decision = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1102667083: // linkId
          this.linkId = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("topic")) {
          this.topic = castToReference(value); // Reference
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("decision")) {
          this.decision = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("linkId")) {
          this.linkId = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110546223:  return getTopic(); 
        case 3575610:  return getType(); 
        case 565719004:  return getDecision(); 
        case 3556653:  return getTextElement();
        case -1102667083:  return getLinkIdElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110546223: /*topic*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 565719004: /*decision*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1102667083: /*linkId*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("topic")) {
          this.topic = new Reference();
          return this.topic;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("decision")) {
          this.decision = new CodeableConcept();
          return this.decision;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
        }
        else if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.linkId");
        }
        else
          return super.addChild(name);
      }

      public ContractOfferComponent copy() {
        ContractOfferComponent dst = new ContractOfferComponent();
        copyValues(dst);
        dst.topic = topic == null ? null : topic.copy();
        dst.type = type == null ? null : type.copy();
        dst.decision = decision == null ? null : decision.copy();
        dst.text = text == null ? null : text.copy();
        dst.linkId = linkId == null ? null : linkId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ContractOfferComponent))
          return false;
        ContractOfferComponent o = (ContractOfferComponent) other_;
        return compareDeep(topic, o.topic, true) && compareDeep(type, o.type, true) && compareDeep(decision, o.decision, true)
           && compareDeep(text, o.text, true) && compareDeep(linkId, o.linkId, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ContractOfferComponent))
          return false;
        ContractOfferComponent o = (ContractOfferComponent) other_;
        return compareValues(text, o.text, true) && compareValues(linkId, o.linkId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(topic, type, decision, text
          , linkId);
      }

  public String fhirType() {
    return "Contract.term.offer";

  }

  }

    @Block()
    public static class ContractAssetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Resource Type, Profile, or CDA etc.
         */
        @Child(name = "class", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource Type, Profile, or CDA etc", formalDefinition="Resource Type, Profile, or CDA etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-class")
        protected Coding class_;

        /**
         * Code in the content.
         */
        @Child(name = "code", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code in the content", formalDefinition="Code in the content." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-code")
        protected Coding code;

        /**
         * Time period of the asset.
         */
        @Child(name = "period", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time period of the asset", formalDefinition="Time period of the asset." )
        protected Period period;

        /**
         * Time period of the data for the asset.
         */
        @Child(name = "dataPeriod", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time period of the data for the asset", formalDefinition="Time period of the data for the asset." )
        protected Period dataPeriod;

        /**
         * Data defined by this Asset.
         */
        @Child(name = "data", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Data defined by this Asset", formalDefinition="Data defined by this Asset." )
        protected List<AssetDataComponent> data;

        /**
         * Contract Valued Item List.
         */
        @Child(name = "valuedItem", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item List", formalDefinition="Contract Valued Item List." )
        protected List<ValuedItemComponent> valuedItem;

        /**
         * A set of security labels that define which terms are controlled by this condition.
         */
        @Child(name = "securityLabel", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Security Labels that define affected terms", formalDefinition="A set of security labels that define which terms are controlled by this condition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/security-labels")
        protected List<Coding> securityLabel;

        private static final long serialVersionUID = 1335706802L;

    /**
     * Constructor
     */
      public ContractAssetComponent() {
        super();
      }

        /**
         * @return {@link #class_} (Resource Type, Profile, or CDA etc.)
         */
        public Coding getClass_() { 
          if (this.class_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.class_");
            else if (Configuration.doAutoCreate())
              this.class_ = new Coding(); // cc
          return this.class_;
        }

        public boolean hasClass_() { 
          return this.class_ != null && !this.class_.isEmpty();
        }

        /**
         * @param value {@link #class_} (Resource Type, Profile, or CDA etc.)
         */
        public ContractAssetComponent setClass_(Coding value) { 
          this.class_ = value;
          return this;
        }

        /**
         * @return {@link #code} (Code in the content.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code in the content.)
         */
        public ContractAssetComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #period} (Time period of the asset.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Time period of the asset.)
         */
        public ContractAssetComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #dataPeriod} (Time period of the data for the asset.)
         */
        public Period getDataPeriod() { 
          if (this.dataPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.dataPeriod");
            else if (Configuration.doAutoCreate())
              this.dataPeriod = new Period(); // cc
          return this.dataPeriod;
        }

        public boolean hasDataPeriod() { 
          return this.dataPeriod != null && !this.dataPeriod.isEmpty();
        }

        /**
         * @param value {@link #dataPeriod} (Time period of the data for the asset.)
         */
        public ContractAssetComponent setDataPeriod(Period value) { 
          this.dataPeriod = value;
          return this;
        }

        /**
         * @return {@link #data} (Data defined by this Asset.)
         */
        public List<AssetDataComponent> getData() { 
          if (this.data == null)
            this.data = new ArrayList<AssetDataComponent>();
          return this.data;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setData(List<AssetDataComponent> theData) { 
          this.data = theData;
          return this;
        }

        public boolean hasData() { 
          if (this.data == null)
            return false;
          for (AssetDataComponent item : this.data)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AssetDataComponent addData() { //3
          AssetDataComponent t = new AssetDataComponent();
          if (this.data == null)
            this.data = new ArrayList<AssetDataComponent>();
          this.data.add(t);
          return t;
        }

        public ContractAssetComponent addData(AssetDataComponent t) { //3
          if (t == null)
            return this;
          if (this.data == null)
            this.data = new ArrayList<AssetDataComponent>();
          this.data.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #data}, creating it if it does not already exist
         */
        public AssetDataComponent getDataFirstRep() { 
          if (getData().isEmpty()) {
            addData();
          }
          return getData().get(0);
        }

        /**
         * @return {@link #valuedItem} (Contract Valued Item List.)
         */
        public List<ValuedItemComponent> getValuedItem() { 
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<ValuedItemComponent>();
          return this.valuedItem;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setValuedItem(List<ValuedItemComponent> theValuedItem) { 
          this.valuedItem = theValuedItem;
          return this;
        }

        public boolean hasValuedItem() { 
          if (this.valuedItem == null)
            return false;
          for (ValuedItemComponent item : this.valuedItem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ValuedItemComponent addValuedItem() { //3
          ValuedItemComponent t = new ValuedItemComponent();
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<ValuedItemComponent>();
          this.valuedItem.add(t);
          return t;
        }

        public ContractAssetComponent addValuedItem(ValuedItemComponent t) { //3
          if (t == null)
            return this;
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<ValuedItemComponent>();
          this.valuedItem.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #valuedItem}, creating it if it does not already exist
         */
        public ValuedItemComponent getValuedItemFirstRep() { 
          if (getValuedItem().isEmpty()) {
            addValuedItem();
          }
          return getValuedItem().get(0);
        }

        /**
         * @return {@link #securityLabel} (A set of security labels that define which terms are controlled by this condition.)
         */
        public List<Coding> getSecurityLabel() { 
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          return this.securityLabel;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setSecurityLabel(List<Coding> theSecurityLabel) { 
          this.securityLabel = theSecurityLabel;
          return this;
        }

        public boolean hasSecurityLabel() { 
          if (this.securityLabel == null)
            return false;
          for (Coding item : this.securityLabel)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addSecurityLabel() { //3
          Coding t = new Coding();
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return t;
        }

        public ContractAssetComponent addSecurityLabel(Coding t) { //3
          if (t == null)
            return this;
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #securityLabel}, creating it if it does not already exist
         */
        public Coding getSecurityLabelFirstRep() { 
          if (getSecurityLabel().isEmpty()) {
            addSecurityLabel();
          }
          return getSecurityLabel().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("class", "Coding", "Resource Type, Profile, or CDA etc.", 0, 1, class_));
          children.add(new Property("code", "Coding", "Code in the content.", 0, 1, code));
          children.add(new Property("period", "Period", "Time period of the asset.", 0, 1, period));
          children.add(new Property("dataPeriod", "Period", "Time period of the data for the asset.", 0, 1, dataPeriod));
          children.add(new Property("data", "", "Data defined by this Asset.", 0, java.lang.Integer.MAX_VALUE, data));
          children.add(new Property("valuedItem", "", "Contract Valued Item List.", 0, java.lang.Integer.MAX_VALUE, valuedItem));
          children.add(new Property("securityLabel", "Coding", "A set of security labels that define which terms are controlled by this condition.", 0, java.lang.Integer.MAX_VALUE, securityLabel));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 94742904: /*class*/  return new Property("class", "Coding", "Resource Type, Profile, or CDA etc.", 0, 1, class_);
          case 3059181: /*code*/  return new Property("code", "Coding", "Code in the content.", 0, 1, code);
          case -991726143: /*period*/  return new Property("period", "Period", "Time period of the asset.", 0, 1, period);
          case 1177250315: /*dataPeriod*/  return new Property("dataPeriod", "Period", "Time period of the data for the asset.", 0, 1, dataPeriod);
          case 3076010: /*data*/  return new Property("data", "", "Data defined by this Asset.", 0, java.lang.Integer.MAX_VALUE, data);
          case 2046675654: /*valuedItem*/  return new Property("valuedItem", "", "Contract Valued Item List.", 0, java.lang.Integer.MAX_VALUE, valuedItem);
          case -722296940: /*securityLabel*/  return new Property("securityLabel", "Coding", "A set of security labels that define which terms are controlled by this condition.", 0, java.lang.Integer.MAX_VALUE, securityLabel);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // Coding
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Coding
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1177250315: /*dataPeriod*/ return this.dataPeriod == null ? new Base[0] : new Base[] {this.dataPeriod}; // Period
        case 3076010: /*data*/ return this.data == null ? new Base[0] : this.data.toArray(new Base[this.data.size()]); // AssetDataComponent
        case 2046675654: /*valuedItem*/ return this.valuedItem == null ? new Base[0] : this.valuedItem.toArray(new Base[this.valuedItem.size()]); // ValuedItemComponent
        case -722296940: /*securityLabel*/ return this.securityLabel == null ? new Base[0] : this.securityLabel.toArray(new Base[this.securityLabel.size()]); // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 94742904: // class
          this.class_ = castToCoding(value); // Coding
          return value;
        case 3059181: // code
          this.code = castToCoding(value); // Coding
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 1177250315: // dataPeriod
          this.dataPeriod = castToPeriod(value); // Period
          return value;
        case 3076010: // data
          this.getData().add((AssetDataComponent) value); // AssetDataComponent
          return value;
        case 2046675654: // valuedItem
          this.getValuedItem().add((ValuedItemComponent) value); // ValuedItemComponent
          return value;
        case -722296940: // securityLabel
          this.getSecurityLabel().add(castToCoding(value)); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("class")) {
          this.class_ = castToCoding(value); // Coding
        } else if (name.equals("code")) {
          this.code = castToCoding(value); // Coding
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("dataPeriod")) {
          this.dataPeriod = castToPeriod(value); // Period
        } else if (name.equals("data")) {
          this.getData().add((AssetDataComponent) value);
        } else if (name.equals("valuedItem")) {
          this.getValuedItem().add((ValuedItemComponent) value);
        } else if (name.equals("securityLabel")) {
          this.getSecurityLabel().add(castToCoding(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742904:  return getClass_(); 
        case 3059181:  return getCode(); 
        case -991726143:  return getPeriod(); 
        case 1177250315:  return getDataPeriod(); 
        case 3076010:  return addData(); 
        case 2046675654:  return addValuedItem(); 
        case -722296940:  return addSecurityLabel(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742904: /*class*/ return new String[] {"Coding"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 1177250315: /*dataPeriod*/ return new String[] {"Period"};
        case 3076010: /*data*/ return new String[] {};
        case 2046675654: /*valuedItem*/ return new String[] {};
        case -722296940: /*securityLabel*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("class")) {
          this.class_ = new Coding();
          return this.class_;
        }
        else if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("dataPeriod")) {
          this.dataPeriod = new Period();
          return this.dataPeriod;
        }
        else if (name.equals("data")) {
          return addData();
        }
        else if (name.equals("valuedItem")) {
          return addValuedItem();
        }
        else if (name.equals("securityLabel")) {
          return addSecurityLabel();
        }
        else
          return super.addChild(name);
      }

      public ContractAssetComponent copy() {
        ContractAssetComponent dst = new ContractAssetComponent();
        copyValues(dst);
        dst.class_ = class_ == null ? null : class_.copy();
        dst.code = code == null ? null : code.copy();
        dst.period = period == null ? null : period.copy();
        dst.dataPeriod = dataPeriod == null ? null : dataPeriod.copy();
        if (data != null) {
          dst.data = new ArrayList<AssetDataComponent>();
          for (AssetDataComponent i : data)
            dst.data.add(i.copy());
        };
        if (valuedItem != null) {
          dst.valuedItem = new ArrayList<ValuedItemComponent>();
          for (ValuedItemComponent i : valuedItem)
            dst.valuedItem.add(i.copy());
        };
        if (securityLabel != null) {
          dst.securityLabel = new ArrayList<Coding>();
          for (Coding i : securityLabel)
            dst.securityLabel.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ContractAssetComponent))
          return false;
        ContractAssetComponent o = (ContractAssetComponent) other_;
        return compareDeep(class_, o.class_, true) && compareDeep(code, o.code, true) && compareDeep(period, o.period, true)
           && compareDeep(dataPeriod, o.dataPeriod, true) && compareDeep(data, o.data, true) && compareDeep(valuedItem, o.valuedItem, true)
           && compareDeep(securityLabel, o.securityLabel, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ContractAssetComponent))
          return false;
        ContractAssetComponent o = (ContractAssetComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(class_, code, period, dataPeriod
          , data, valuedItem, securityLabel);
      }

  public String fhirType() {
    return "Contract.term.asset";

  }

  }

    @Block()
    public static class AssetDataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * instance | related | dependents | authoredby.
         */
        @Child(name = "meaning", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="instance | related | dependents | authoredby", formalDefinition="instance | related | dependents | authoredby." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-data-meaning")
        protected Enumeration<ContractDataMeaning> meaning;

        /**
         * The actual data reference.
         */
        @Child(name = "reference", type = {Reference.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The actual data reference", formalDefinition="The actual data reference." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (The actual data reference.)
         */
        protected Resource referenceTarget;

        private static final long serialVersionUID = 2123707153L;

    /**
     * Constructor
     */
      public AssetDataComponent() {
        super();
      }

        /**
         * @return {@link #meaning} (instance | related | dependents | authoredby.). This is the underlying object with id, value and extensions. The accessor "getMeaning" gives direct access to the value
         */
        public Enumeration<ContractDataMeaning> getMeaningElement() { 
          if (this.meaning == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AssetDataComponent.meaning");
            else if (Configuration.doAutoCreate())
              this.meaning = new Enumeration<ContractDataMeaning>(new ContractDataMeaningEnumFactory()); // bb
          return this.meaning;
        }

        public boolean hasMeaningElement() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        public boolean hasMeaning() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        /**
         * @param value {@link #meaning} (instance | related | dependents | authoredby.). This is the underlying object with id, value and extensions. The accessor "getMeaning" gives direct access to the value
         */
        public AssetDataComponent setMeaningElement(Enumeration<ContractDataMeaning> value) { 
          this.meaning = value;
          return this;
        }

        /**
         * @return instance | related | dependents | authoredby.
         */
        public ContractDataMeaning getMeaning() { 
          return this.meaning == null ? null : this.meaning.getValue();
        }

        /**
         * @param value instance | related | dependents | authoredby.
         */
        public AssetDataComponent setMeaning(ContractDataMeaning value) { 
          if (value == null)
            this.meaning = null;
          else {
            if (this.meaning == null)
              this.meaning = new Enumeration<ContractDataMeaning>(new ContractDataMeaningEnumFactory());
            this.meaning.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #reference} (The actual data reference.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AssetDataComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (The actual data reference.)
         */
        public AssetDataComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The actual data reference.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The actual data reference.)
         */
        public AssetDataComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("meaning", "code", "instance | related | dependents | authoredby.", 0, 1, meaning));
          children.add(new Property("reference", "Reference(Any)", "The actual data reference.", 0, 1, reference));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 938160637: /*meaning*/  return new Property("meaning", "code", "instance | related | dependents | authoredby.", 0, 1, meaning);
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "The actual data reference.", 0, 1, reference);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 938160637: /*meaning*/ return this.meaning == null ? new Base[0] : new Base[] {this.meaning}; // Enumeration<ContractDataMeaning>
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 938160637: // meaning
          value = new ContractDataMeaningEnumFactory().fromType(castToCode(value));
          this.meaning = (Enumeration) value; // Enumeration<ContractDataMeaning>
          return value;
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("meaning")) {
          value = new ContractDataMeaningEnumFactory().fromType(castToCode(value));
          this.meaning = (Enumeration) value; // Enumeration<ContractDataMeaning>
        } else if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938160637:  return getMeaningElement();
        case -925155509:  return getReference(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938160637: /*meaning*/ return new String[] {"code"};
        case -925155509: /*reference*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("meaning")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.meaning");
        }
        else if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public AssetDataComponent copy() {
        AssetDataComponent dst = new AssetDataComponent();
        copyValues(dst);
        dst.meaning = meaning == null ? null : meaning.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AssetDataComponent))
          return false;
        AssetDataComponent o = (AssetDataComponent) other_;
        return compareDeep(meaning, o.meaning, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AssetDataComponent))
          return false;
        AssetDataComponent o = (AssetDataComponent) other_;
        return compareValues(meaning, o.meaning, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(meaning, reference);
      }

  public String fhirType() {
    return "Contract.term.asset.data";

  }

  }

    @Block()
    public static class ValuedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific type of Contract Valued Item that may be priced.
         */
        @Child(name = "entity", type = {CodeableConcept.class, Reference.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Type", formalDefinition="Specific type of Contract Valued Item that may be priced." )
        protected Type entity;

        /**
         * Identifies a Contract Valued Item instance.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Number", formalDefinition="Identifies a Contract Valued Item instance." )
        protected Identifier identifier;

        /**
         * Indicates the time during which this Contract ValuedItem information is effective.
         */
        @Child(name = "effectiveTime", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Effective Tiem", formalDefinition="Indicates the time during which this Contract ValuedItem information is effective." )
        protected DateTimeType effectiveTime;

        /**
         * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of Contract Valued Items", formalDefinition="Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances." )
        protected SimpleQuantity quantity;

        /**
         * A Contract Valued Item unit valuation measure.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item fee, charge, or cost", formalDefinition="A Contract Valued Item unit valuation measure." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Price Scaling Factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Difficulty Scaling Factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total Contract Valued Item Value", formalDefinition="Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        private static final long serialVersionUID = 1782449516L;

    /**
     * Constructor
     */
      public ValuedItemComponent() {
        super();
      }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public Type getEntity() { 
          return this.entity;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public CodeableConcept getEntityCodeableConcept() throws FHIRException { 
          if (!(this.entity instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (CodeableConcept) this.entity;
        }

        public boolean hasEntityCodeableConcept() { 
          return this.entity instanceof CodeableConcept;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public Reference getEntityReference() throws FHIRException { 
          if (!(this.entity instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (Reference) this.entity;
        }

        public boolean hasEntityReference() { 
          return this.entity instanceof Reference;
        }

        public boolean hasEntity() { 
          return this.entity != null && !this.entity.isEmpty();
        }

        /**
         * @param value {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public ValuedItemComponent setEntity(Type value) { 
          this.entity = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Identifies a Contract Valued Item instance.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifies a Contract Valued Item instance.)
         */
        public ValuedItemComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #effectiveTime} (Indicates the time during which this Contract ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public DateTimeType getEffectiveTimeElement() { 
          if (this.effectiveTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.effectiveTime");
            else if (Configuration.doAutoCreate())
              this.effectiveTime = new DateTimeType(); // bb
          return this.effectiveTime;
        }

        public boolean hasEffectiveTimeElement() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        public boolean hasEffectiveTime() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        /**
         * @param value {@link #effectiveTime} (Indicates the time during which this Contract ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public ValuedItemComponent setEffectiveTimeElement(DateTimeType value) { 
          this.effectiveTime = value;
          return this;
        }

        /**
         * @return Indicates the time during which this Contract ValuedItem information is effective.
         */
        public Date getEffectiveTime() { 
          return this.effectiveTime == null ? null : this.effectiveTime.getValue();
        }

        /**
         * @param value Indicates the time during which this Contract ValuedItem information is effective.
         */
        public ValuedItemComponent setEffectiveTime(Date value) { 
          if (value == null)
            this.effectiveTime = null;
          else {
            if (this.effectiveTime == null)
              this.effectiveTime = new DateTimeType();
            this.effectiveTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #quantity} (Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.)
         */
        public ValuedItemComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (A Contract Valued Item unit valuation measure.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (A Contract Valued Item unit valuation measure.)
         */
        public ValuedItemComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public ValuedItemComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public ValuedItemComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public ValuedItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity));
          children.add(new Property("identifier", "Identifier", "Identifies a Contract Valued Item instance.", 0, 1, identifier));
          children.add(new Property("effectiveTime", "dateTime", "Indicates the time during which this Contract ValuedItem information is effective.", 0, 1, effectiveTime));
          children.add(new Property("quantity", "SimpleQuantity", "Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "A Contract Valued Item unit valuation measure.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.", 0, 1, points));
          children.add(new Property("net", "Money", "Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, 1, net));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -740568643: /*entity[x]*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case -1298275357: /*entity*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case 924197182: /*entityCodeableConcept*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case -356635992: /*entityReference*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifies a Contract Valued Item instance.", 0, 1, identifier);
          case -929905388: /*effectiveTime*/  return new Property("effectiveTime", "dateTime", "Indicates the time during which this Contract ValuedItem information is effective.", 0, 1, effectiveTime);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "A Contract Valued Item unit valuation measure.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case -982754077: /*points*/  return new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.", 0, 1, points);
          case 108957: /*net*/  return new Property("net", "Money", "Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, 1, net);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1298275357: /*entity*/ return this.entity == null ? new Base[0] : new Base[] {this.entity}; // Type
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -929905388: /*effectiveTime*/ return this.effectiveTime == null ? new Base[0] : new Base[] {this.effectiveTime}; // DateTimeType
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case -982754077: /*points*/ return this.points == null ? new Base[0] : new Base[] {this.points}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1298275357: // entity
          this.entity = castToType(value); // Type
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -929905388: // effectiveTime
          this.effectiveTime = castToDateTime(value); // DateTimeType
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case -982754077: // points
          this.points = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("entity[x]")) {
          this.entity = castToType(value); // Type
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("effectiveTime")) {
          this.effectiveTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("points")) {
          this.points = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -740568643:  return getEntity(); 
        case -1298275357:  return getEntity(); 
        case -1618432855:  return getIdentifier(); 
        case -929905388:  return getEffectiveTimeElement();
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case -982754077:  return getPointsElement();
        case 108957:  return getNet(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1298275357: /*entity*/ return new String[] {"CodeableConcept", "Reference"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -929905388: /*effectiveTime*/ return new String[] {"dateTime"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case -982754077: /*points*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("entityCodeableConcept")) {
          this.entity = new CodeableConcept();
          return this.entity;
        }
        else if (name.equals("entityReference")) {
          this.entity = new Reference();
          return this.entity;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("effectiveTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.effectiveTime");
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else
          return super.addChild(name);
      }

      public ValuedItemComponent copy() {
        ValuedItemComponent dst = new ValuedItemComponent();
        copyValues(dst);
        dst.entity = entity == null ? null : entity.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.effectiveTime = effectiveTime == null ? null : effectiveTime.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ValuedItemComponent))
          return false;
        ValuedItemComponent o = (ValuedItemComponent) other_;
        return compareDeep(entity, o.entity, true) && compareDeep(identifier, o.identifier, true) && compareDeep(effectiveTime, o.effectiveTime, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ValuedItemComponent))
          return false;
        ValuedItemComponent o = (ValuedItemComponent) other_;
        return compareValues(effectiveTime, o.effectiveTime, true) && compareValues(factor, o.factor, true)
           && compareValues(points, o.points, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(entity, identifier, effectiveTime
          , quantity, unitPrice, factor, points, net);
      }

  public String fhirType() {
    return "Contract.term.asset.valuedItem";

  }

  }

    @Block()
    public static class AgentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Who or what parties are assigned roles in this Contract.
         */
        @Child(name = "actor", type = {Contract.class, Device.class, Group.class, Location.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, Substance.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Agent Type", formalDefinition="Who or what parties are assigned roles in this Contract." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (Who or what parties are assigned roles in this Contract.)
         */
        protected Resource actorTarget;

        /**
         * Role type of agent assigned roles in this Contract.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Role type of the agent", formalDefinition="Role type of agent assigned roles in this Contract." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-actorrole")
        protected List<CodeableConcept> role;

        private static final long serialVersionUID = -454551165L;

    /**
     * Constructor
     */
      public AgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AgentComponent(Reference actor) {
        super();
        this.actor = actor;
      }

        /**
         * @return {@link #actor} (Who or what parties are assigned roles in this Contract.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AgentComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (Who or what parties are assigned roles in this Contract.)
         */
        public AgentComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what parties are assigned roles in this Contract.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what parties are assigned roles in this Contract.)
         */
        public AgentComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Role type of agent assigned roles in this Contract.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AgentComponent setRole(List<CodeableConcept> theRole) { 
          this.role = theRole;
          return this;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

        public AgentComponent addRole(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #role}, creating it if it does not already exist
         */
        public CodeableConcept getRoleFirstRep() { 
          if (getRole().isEmpty()) {
            addRole();
          }
          return getRole().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("actor", "Reference(Contract|Device|Group|Location|Organization|Patient|Practitioner|RelatedPerson|Substance)", "Who or what parties are assigned roles in this Contract.", 0, 1, actor));
          children.add(new Property("role", "CodeableConcept", "Role type of agent assigned roles in this Contract.", 0, java.lang.Integer.MAX_VALUE, role));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 92645877: /*actor*/  return new Property("actor", "Reference(Contract|Device|Group|Location|Organization|Patient|Practitioner|RelatedPerson|Substance)", "Who or what parties are assigned roles in this Contract.", 0, 1, actor);
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "Role type of agent assigned roles in this Contract.", 0, java.lang.Integer.MAX_VALUE, role);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : this.role.toArray(new Base[this.role.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          return value;
        case 3506294: // role
          this.getRole().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actor")) {
          this.actor = castToReference(value); // Reference
        } else if (name.equals("role")) {
          this.getRole().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877:  return getActor(); 
        case 3506294:  return addRole(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return new String[] {"Reference"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else
          return super.addChild(name);
      }

      public AgentComponent copy() {
        AgentComponent dst = new AgentComponent();
        copyValues(dst);
        dst.actor = actor == null ? null : actor.copy();
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AgentComponent))
          return false;
        AgentComponent o = (AgentComponent) other_;
        return compareDeep(actor, o.actor, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AgentComponent))
          return false;
        AgentComponent o = (AgentComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actor, role);
      }

  public String fhirType() {
    return "Contract.term.agent";

  }

  }

    @Block()
    public static class SignatoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role of this Contract signer, e.g. notary, grantee.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Signatory Role", formalDefinition="Role of this Contract signer, e.g. notary, grantee." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-signer-type")
        protected Coding type;

        /**
         * Party which is a signator to this Contract.
         */
        @Child(name = "party", type = {Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Signatory Party", formalDefinition="Party which is a signator to this Contract." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Party which is a signator to this Contract.)
         */
        protected Resource partyTarget;

        /**
         * Legally binding Contract DSIG signature contents in Base64.
         */
        @Child(name = "signature", type = {Signature.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Documentation Signature", formalDefinition="Legally binding Contract DSIG signature contents in Base64." )
        protected List<Signature> signature;

        private static final long serialVersionUID = 1948139228L;

    /**
     * Constructor
     */
      public SignatoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SignatoryComponent(Coding type, Reference party) {
        super();
        this.type = type;
        this.party = party;
      }

        /**
         * @return {@link #type} (Role of this Contract signer, e.g. notary, grantee.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Role of this Contract signer, e.g. notary, grantee.)
         */
        public SignatoryComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #party} (Party which is a signator to this Contract.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Party which is a signator to this Contract.)
         */
        public SignatoryComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Party which is a signator to this Contract.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Party which is a signator to this Contract.)
         */
        public SignatoryComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        /**
         * @return {@link #signature} (Legally binding Contract DSIG signature contents in Base64.)
         */
        public List<Signature> getSignature() { 
          if (this.signature == null)
            this.signature = new ArrayList<Signature>();
          return this.signature;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SignatoryComponent setSignature(List<Signature> theSignature) { 
          this.signature = theSignature;
          return this;
        }

        public boolean hasSignature() { 
          if (this.signature == null)
            return false;
          for (Signature item : this.signature)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Signature addSignature() { //3
          Signature t = new Signature();
          if (this.signature == null)
            this.signature = new ArrayList<Signature>();
          this.signature.add(t);
          return t;
        }

        public SignatoryComponent addSignature(Signature t) { //3
          if (t == null)
            return this;
          if (this.signature == null)
            this.signature = new ArrayList<Signature>();
          this.signature.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #signature}, creating it if it does not already exist
         */
        public Signature getSignatureFirstRep() { 
          if (getSignature().isEmpty()) {
            addSignature();
          }
          return getSignature().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "Coding", "Role of this Contract signer, e.g. notary, grantee.", 0, 1, type));
          children.add(new Property("party", "Reference(Organization|Patient|Practitioner|RelatedPerson)", "Party which is a signator to this Contract.", 0, 1, party));
          children.add(new Property("signature", "Signature", "Legally binding Contract DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, signature));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "Coding", "Role of this Contract signer, e.g. notary, grantee.", 0, 1, type);
          case 106437350: /*party*/  return new Property("party", "Reference(Organization|Patient|Practitioner|RelatedPerson)", "Party which is a signator to this Contract.", 0, 1, party);
          case 1073584312: /*signature*/  return new Property("signature", "Signature", "Legally binding Contract DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, signature);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 106437350: /*party*/ return this.party == null ? new Base[0] : new Base[] {this.party}; // Reference
        case 1073584312: /*signature*/ return this.signature == null ? new Base[0] : this.signature.toArray(new Base[this.signature.size()]); // Signature
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          return value;
        case 106437350: // party
          this.party = castToReference(value); // Reference
          return value;
        case 1073584312: // signature
          this.getSignature().add(castToSignature(value)); // Signature
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCoding(value); // Coding
        } else if (name.equals("party")) {
          this.party = castToReference(value); // Reference
        } else if (name.equals("signature")) {
          this.getSignature().add(castToSignature(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 106437350:  return getParty(); 
        case 1073584312:  return addSignature(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"Coding"};
        case 106437350: /*party*/ return new String[] {"Reference"};
        case 1073584312: /*signature*/ return new String[] {"Signature"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("party")) {
          this.party = new Reference();
          return this.party;
        }
        else if (name.equals("signature")) {
          return addSignature();
        }
        else
          return super.addChild(name);
      }

      public SignatoryComponent copy() {
        SignatoryComponent dst = new SignatoryComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.party = party == null ? null : party.copy();
        if (signature != null) {
          dst.signature = new ArrayList<Signature>();
          for (Signature i : signature)
            dst.signature.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SignatoryComponent))
          return false;
        SignatoryComponent o = (SignatoryComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(party, o.party, true) && compareDeep(signature, o.signature, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SignatoryComponent))
          return false;
        SignatoryComponent o = (SignatoryComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, party, signature);
      }

  public String fhirType() {
    return "Contract.signer";

  }

  }

    @Block()
    public static class FriendlyLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.
         */
        @Child(name = "content", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Easily comprehended representation of this Contract", formalDefinition="Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public FriendlyLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public FriendlyLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public FriendlyLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 264548711: /*content[x]*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          case 951530617: /*content*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          case -702028164: /*contentAttachment*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          case 1193747154: /*contentReference*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]")) {
          this.content = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); 
        case 951530617:  return getContent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public FriendlyLanguageComponent copy() {
        FriendlyLanguageComponent dst = new FriendlyLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other_;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Contract.friendly";

  }

  }

    @Block()
    public static class LegalLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Contract legal text in human renderable form.
         */
        @Child(name = "content", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Legal Text", formalDefinition="Contract legal text in human renderable form." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public LegalLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LegalLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Contract legal text in human renderable form.)
         */
        public LegalLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 264548711: /*content[x]*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          case 951530617: /*content*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          case -702028164: /*contentAttachment*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          case 1193747154: /*contentReference*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]")) {
          this.content = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); 
        case 951530617:  return getContent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public LegalLanguageComponent copy() {
        LegalLanguageComponent dst = new LegalLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other_;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Contract.legal";

  }

  }

    @Block()
    public static class ComputableLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).
         */
        @Child(name = "content", type = {Attachment.class, DocumentReference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Computable Contract Rules", formalDefinition="Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal)." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public ComputableLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ComputableLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public ComputableLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 264548711: /*content[x]*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          case 951530617: /*content*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          case -702028164: /*contentAttachment*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          case 1193747154: /*contentReference*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]")) {
          this.content = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); 
        case 951530617:  return getContent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public ComputableLanguageComponent copy() {
        ComputableLanguageComponent dst = new ComputableLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ComputableLanguageComponent))
          return false;
        ComputableLanguageComponent o = (ComputableLanguageComponent) other_;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ComputableLanguageComponent))
          return false;
        ComputableLanguageComponent o = (ComputableLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Contract.rule";

  }

  }

    /**
     * Unique identifier for this Contract or a derivative that references a Source Contract.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contract number", formalDefinition="Unique identifier for this Contract or a derivative that references a Source Contract." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="amended | appended | cancelled | disputed | entered-in-error | executable | executed | negotiable | offered | policy | rejected | renewed | revoked | resolved | terminated", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-status")
    protected Enumeration<ContractStatus> status;

    /**
     * The minimal content derived from the basal information source at a specific stage in its lifecycle.
     */
    @Child(name = "contentDerivative", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Content derived from the basal information", formalDefinition="The minimal content derived from the basal information source at a specific stage in its lifecycle." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-content-derivative")
    protected CodeableConcept contentDerivative;

    /**
     * When this  Contract was issued.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this Contract was issued", formalDefinition="When this  Contract was issued." )
    protected DateTimeType issued;

    /**
     * Relevant time or time-period when this Contract is applicable.
     */
    @Child(name = "applies", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Effective time", formalDefinition="Relevant time or time-period when this Contract is applicable." )
    protected Period applies;

    /**
     * The target entity impacted by or of interest to parties to the agreement.
     */
    @Child(name = "subject", type = {Reference.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contract Target Entity", formalDefinition="The target entity impacted by or of interest to parties to the agreement." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (The target entity impacted by or of interest to parties to the agreement.)
     */
    protected List<Resource> subjectTarget;


    /**
     * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.
     */
    @Child(name = "authority", type = {Organization.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Authority under which this Contract has standing", formalDefinition="A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies." )
    protected List<Reference> authority;
    /**
     * The actual objects that are the target of the reference (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    protected List<Organization> authorityTarget;


    /**
     * Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.
     */
    @Child(name = "domain", type = {Location.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A sphere of control governed by an authoritative jurisdiction, organization, or person", formalDefinition="Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources." )
    protected List<Reference> domain;
    /**
     * The actual objects that are the target of the reference (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    protected List<Location> domainTarget;


    /**
     * Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type or form", formalDefinition="Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-type")
    protected CodeableConcept type;

    /**
     * More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Subtype within the context of type", formalDefinition="More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-subtype")
    protected List<CodeableConcept> subType;

    /**
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     */
    @Child(name = "term", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Term List", formalDefinition="One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups." )
    protected List<TermComponent> term;

    /**
     * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.
     */
    @Child(name = "signer", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Signatory", formalDefinition="Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness." )
    protected List<SignatoryComponent> signer;

    /**
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     */
    @Child(name = "friendly", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Friendly Language", formalDefinition="The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement." )
    protected List<FriendlyLanguageComponent> friendly;

    /**
     * List of Legal expressions or representations of this Contract.
     */
    @Child(name = "legal", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Legal Language", formalDefinition="List of Legal expressions or representations of this Contract." )
    protected List<LegalLanguageComponent> legal;

    /**
     * List of Computable Policy Rule Language Representations of this Contract.
     */
    @Child(name = "rule", type = {}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Computable Contract Language", formalDefinition="List of Computable Policy Rule Language Representations of this Contract." )
    protected ComputableLanguageComponent rule;

    /**
     * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
     */
    @Child(name = "legallyBinding", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class, Contract.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Binding Contract", formalDefinition="Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract." )
    protected Type legallyBinding;

    private static final long serialVersionUID = 1347256849L;

  /**
   * Constructor
   */
    public Contract() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique identifier for this Contract or a derivative that references a Source Contract.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public Contract addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ContractStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ContractStatus>(new ContractStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Contract setStatusElement(Enumeration<ContractStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ContractStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public Contract setStatus(ContractStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ContractStatus>(new ContractStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contentDerivative} (The minimal content derived from the basal information source at a specific stage in its lifecycle.)
     */
    public CodeableConcept getContentDerivative() { 
      if (this.contentDerivative == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.contentDerivative");
        else if (Configuration.doAutoCreate())
          this.contentDerivative = new CodeableConcept(); // cc
      return this.contentDerivative;
    }

    public boolean hasContentDerivative() { 
      return this.contentDerivative != null && !this.contentDerivative.isEmpty();
    }

    /**
     * @param value {@link #contentDerivative} (The minimal content derived from the basal information source at a specific stage in its lifecycle.)
     */
    public Contract setContentDerivative(CodeableConcept value) { 
      this.contentDerivative = value;
      return this;
    }

    /**
     * @return {@link #issued} (When this  Contract was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (When this  Contract was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Contract setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return When this  Contract was issued.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When this  Contract was issued.
     */
    public Contract setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #applies} (Relevant time or time-period when this Contract is applicable.)
     */
    public Period getApplies() { 
      if (this.applies == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.applies");
        else if (Configuration.doAutoCreate())
          this.applies = new Period(); // cc
      return this.applies;
    }

    public boolean hasApplies() { 
      return this.applies != null && !this.applies.isEmpty();
    }

    /**
     * @param value {@link #applies} (Relevant time or time-period when this Contract is applicable.)
     */
    public Contract setApplies(Period value) { 
      this.applies = value;
      return this;
    }

    /**
     * @return {@link #subject} (The target entity impacted by or of interest to parties to the agreement.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSubject(List<Reference> theSubject) { 
      this.subject = theSubject;
      return this;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    public Contract addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
     */
    public Reference getSubjectFirstRep() { 
      if (getSubject().isEmpty()) {
        addSubject();
      }
      return getSubject().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #authority} (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    public List<Reference> getAuthority() { 
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      return this.authority;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setAuthority(List<Reference> theAuthority) { 
      this.authority = theAuthority;
      return this;
    }

    public boolean hasAuthority() { 
      if (this.authority == null)
        return false;
      for (Reference item : this.authority)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAuthority() { //3
      Reference t = new Reference();
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return t;
    }

    public Contract addAuthority(Reference t) { //3
      if (t == null)
        return this;
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #authority}, creating it if it does not already exist
     */
    public Reference getAuthorityFirstRep() { 
      if (getAuthority().isEmpty()) {
        addAuthority();
      }
      return getAuthority().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getAuthorityTarget() { 
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      return this.authorityTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addAuthorityTarget() { 
      Organization r = new Organization();
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      this.authorityTarget.add(r);
      return r;
    }

    /**
     * @return {@link #domain} (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    public List<Reference> getDomain() { 
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      return this.domain;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setDomain(List<Reference> theDomain) { 
      this.domain = theDomain;
      return this;
    }

    public boolean hasDomain() { 
      if (this.domain == null)
        return false;
      for (Reference item : this.domain)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDomain() { //3
      Reference t = new Reference();
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return t;
    }

    public Contract addDomain(Reference t) { //3
      if (t == null)
        return this;
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #domain}, creating it if it does not already exist
     */
    public Reference getDomainFirstRep() { 
      if (getDomain().isEmpty()) {
        addDomain();
      }
      return getDomain().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getDomainTarget() { 
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      return this.domainTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addDomainTarget() { 
      Location r = new Location();
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      this.domainTarget.add(r);
      return r;
    }

    /**
     * @return {@link #type} (Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.)
     */
    public Contract setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.)
     */
    public List<CodeableConcept> getSubType() { 
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      return this.subType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSubType(List<CodeableConcept> theSubType) { 
      this.subType = theSubType;
      return this;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (CodeableConcept item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSubType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return t;
    }

    public Contract addSubType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subType}, creating it if it does not already exist
     */
    public CodeableConcept getSubTypeFirstRep() { 
      if (getSubType().isEmpty()) {
        addSubType();
      }
      return getSubType().get(0);
    }

    /**
     * @return {@link #term} (One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.)
     */
    public List<TermComponent> getTerm() { 
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      return this.term;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setTerm(List<TermComponent> theTerm) { 
      this.term = theTerm;
      return this;
    }

    public boolean hasTerm() { 
      if (this.term == null)
        return false;
      for (TermComponent item : this.term)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TermComponent addTerm() { //3
      TermComponent t = new TermComponent();
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      this.term.add(t);
      return t;
    }

    public Contract addTerm(TermComponent t) { //3
      if (t == null)
        return this;
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      this.term.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #term}, creating it if it does not already exist
     */
    public TermComponent getTermFirstRep() { 
      if (getTerm().isEmpty()) {
        addTerm();
      }
      return getTerm().get(0);
    }

    /**
     * @return {@link #signer} (Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.)
     */
    public List<SignatoryComponent> getSigner() { 
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      return this.signer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSigner(List<SignatoryComponent> theSigner) { 
      this.signer = theSigner;
      return this;
    }

    public boolean hasSigner() { 
      if (this.signer == null)
        return false;
      for (SignatoryComponent item : this.signer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SignatoryComponent addSigner() { //3
      SignatoryComponent t = new SignatoryComponent();
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      this.signer.add(t);
      return t;
    }

    public Contract addSigner(SignatoryComponent t) { //3
      if (t == null)
        return this;
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      this.signer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #signer}, creating it if it does not already exist
     */
    public SignatoryComponent getSignerFirstRep() { 
      if (getSigner().isEmpty()) {
        addSigner();
      }
      return getSigner().get(0);
    }

    /**
     * @return {@link #friendly} (The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.)
     */
    public List<FriendlyLanguageComponent> getFriendly() { 
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      return this.friendly;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setFriendly(List<FriendlyLanguageComponent> theFriendly) { 
      this.friendly = theFriendly;
      return this;
    }

    public boolean hasFriendly() { 
      if (this.friendly == null)
        return false;
      for (FriendlyLanguageComponent item : this.friendly)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public FriendlyLanguageComponent addFriendly() { //3
      FriendlyLanguageComponent t = new FriendlyLanguageComponent();
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return t;
    }

    public Contract addFriendly(FriendlyLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #friendly}, creating it if it does not already exist
     */
    public FriendlyLanguageComponent getFriendlyFirstRep() { 
      if (getFriendly().isEmpty()) {
        addFriendly();
      }
      return getFriendly().get(0);
    }

    /**
     * @return {@link #legal} (List of Legal expressions or representations of this Contract.)
     */
    public List<LegalLanguageComponent> getLegal() { 
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      return this.legal;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setLegal(List<LegalLanguageComponent> theLegal) { 
      this.legal = theLegal;
      return this;
    }

    public boolean hasLegal() { 
      if (this.legal == null)
        return false;
      for (LegalLanguageComponent item : this.legal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public LegalLanguageComponent addLegal() { //3
      LegalLanguageComponent t = new LegalLanguageComponent();
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return t;
    }

    public Contract addLegal(LegalLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #legal}, creating it if it does not already exist
     */
    public LegalLanguageComponent getLegalFirstRep() { 
      if (getLegal().isEmpty()) {
        addLegal();
      }
      return getLegal().get(0);
    }

    /**
     * @return {@link #rule} (List of Computable Policy Rule Language Representations of this Contract.)
     */
    public ComputableLanguageComponent getRule() { 
      if (this.rule == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.rule");
        else if (Configuration.doAutoCreate())
          this.rule = new ComputableLanguageComponent(); // cc
      return this.rule;
    }

    public boolean hasRule() { 
      return this.rule != null && !this.rule.isEmpty();
    }

    /**
     * @param value {@link #rule} (List of Computable Policy Rule Language Representations of this Contract.)
     */
    public Contract setRule(ComputableLanguageComponent value) { 
      this.rule = value;
      return this;
    }

    /**
     * @return {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Type getLegallyBinding() { 
      return this.legallyBinding;
    }

    /**
     * @return {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Attachment getLegallyBindingAttachment() throws FHIRException { 
      if (!(this.legallyBinding instanceof Attachment))
        throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.legallyBinding.getClass().getName()+" was encountered");
      return (Attachment) this.legallyBinding;
    }

    public boolean hasLegallyBindingAttachment() { 
      return this.legallyBinding instanceof Attachment;
    }

    /**
     * @return {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Reference getLegallyBindingReference() throws FHIRException { 
      if (!(this.legallyBinding instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.legallyBinding.getClass().getName()+" was encountered");
      return (Reference) this.legallyBinding;
    }

    public boolean hasLegallyBindingReference() { 
      return this.legallyBinding instanceof Reference;
    }

    public boolean hasLegallyBinding() { 
      return this.legallyBinding != null && !this.legallyBinding.isEmpty();
    }

    /**
     * @param value {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Contract setLegallyBinding(Type value) { 
      this.legallyBinding = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique identifier for this Contract or a derivative that references a Source Contract.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("contentDerivative", "CodeableConcept", "The minimal content derived from the basal information source at a specific stage in its lifecycle.", 0, 1, contentDerivative));
        children.add(new Property("issued", "dateTime", "When this  Contract was issued.", 0, 1, issued));
        children.add(new Property("applies", "Period", "Relevant time or time-period when this Contract is applicable.", 0, 1, applies));
        children.add(new Property("subject", "Reference(Any)", "The target entity impacted by or of interest to parties to the agreement.", 0, java.lang.Integer.MAX_VALUE, subject));
        children.add(new Property("authority", "Reference(Organization)", "A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.", 0, java.lang.Integer.MAX_VALUE, authority));
        children.add(new Property("domain", "Reference(Location)", "Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.", 0, java.lang.Integer.MAX_VALUE, domain));
        children.add(new Property("type", "CodeableConcept", "Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.", 0, 1, type));
        children.add(new Property("subType", "CodeableConcept", "More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.", 0, java.lang.Integer.MAX_VALUE, subType));
        children.add(new Property("term", "", "One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.", 0, java.lang.Integer.MAX_VALUE, term));
        children.add(new Property("signer", "", "Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.", 0, java.lang.Integer.MAX_VALUE, signer));
        children.add(new Property("friendly", "", "The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.", 0, java.lang.Integer.MAX_VALUE, friendly));
        children.add(new Property("legal", "", "List of Legal expressions or representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, legal));
        children.add(new Property("rule", "", "List of Computable Policy Rule Language Representations of this Contract.", 0, 1, rule));
        children.add(new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this Contract or a derivative that references a Source Contract.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case -92412192: /*contentDerivative*/  return new Property("contentDerivative", "CodeableConcept", "The minimal content derived from the basal information source at a specific stage in its lifecycle.", 0, 1, contentDerivative);
        case -1179159893: /*issued*/  return new Property("issued", "dateTime", "When this  Contract was issued.", 0, 1, issued);
        case -793235316: /*applies*/  return new Property("applies", "Period", "Relevant time or time-period when this Contract is applicable.", 0, 1, applies);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Any)", "The target entity impacted by or of interest to parties to the agreement.", 0, java.lang.Integer.MAX_VALUE, subject);
        case 1475610435: /*authority*/  return new Property("authority", "Reference(Organization)", "A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.", 0, java.lang.Integer.MAX_VALUE, authority);
        case -1326197564: /*domain*/  return new Property("domain", "Reference(Location)", "Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.", 0, java.lang.Integer.MAX_VALUE, domain);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.", 0, 1, type);
        case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.", 0, java.lang.Integer.MAX_VALUE, subType);
        case 3556460: /*term*/  return new Property("term", "", "One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.", 0, java.lang.Integer.MAX_VALUE, term);
        case -902467798: /*signer*/  return new Property("signer", "", "Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.", 0, java.lang.Integer.MAX_VALUE, signer);
        case -1423054677: /*friendly*/  return new Property("friendly", "", "The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.", 0, java.lang.Integer.MAX_VALUE, friendly);
        case 102851257: /*legal*/  return new Property("legal", "", "List of Legal expressions or representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, legal);
        case 3512060: /*rule*/  return new Property("rule", "", "List of Computable Policy Rule Language Representations of this Contract.", 0, 1, rule);
        case -772497791: /*legallyBinding[x]*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        case -126751329: /*legallyBinding*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        case 344057890: /*legallyBindingAttachment*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        case -296528788: /*legallyBindingReference*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ContractStatus>
        case -92412192: /*contentDerivative*/ return this.contentDerivative == null ? new Base[0] : new Base[] {this.contentDerivative}; // CodeableConcept
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // DateTimeType
        case -793235316: /*applies*/ return this.applies == null ? new Base[0] : new Base[] {this.applies}; // Period
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : this.authority.toArray(new Base[this.authority.size()]); // Reference
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : this.domain.toArray(new Base[this.domain.size()]); // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : this.subType.toArray(new Base[this.subType.size()]); // CodeableConcept
        case 3556460: /*term*/ return this.term == null ? new Base[0] : this.term.toArray(new Base[this.term.size()]); // TermComponent
        case -902467798: /*signer*/ return this.signer == null ? new Base[0] : this.signer.toArray(new Base[this.signer.size()]); // SignatoryComponent
        case -1423054677: /*friendly*/ return this.friendly == null ? new Base[0] : this.friendly.toArray(new Base[this.friendly.size()]); // FriendlyLanguageComponent
        case 102851257: /*legal*/ return this.legal == null ? new Base[0] : this.legal.toArray(new Base[this.legal.size()]); // LegalLanguageComponent
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : new Base[] {this.rule}; // ComputableLanguageComponent
        case -126751329: /*legallyBinding*/ return this.legallyBinding == null ? new Base[0] : new Base[] {this.legallyBinding}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new ContractStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ContractStatus>
          return value;
        case -92412192: // contentDerivative
          this.contentDerivative = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1179159893: // issued
          this.issued = castToDateTime(value); // DateTimeType
          return value;
        case -793235316: // applies
          this.applies = castToPeriod(value); // Period
          return value;
        case -1867885268: // subject
          this.getSubject().add(castToReference(value)); // Reference
          return value;
        case 1475610435: // authority
          this.getAuthority().add(castToReference(value)); // Reference
          return value;
        case -1326197564: // domain
          this.getDomain().add(castToReference(value)); // Reference
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.getSubType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3556460: // term
          this.getTerm().add((TermComponent) value); // TermComponent
          return value;
        case -902467798: // signer
          this.getSigner().add((SignatoryComponent) value); // SignatoryComponent
          return value;
        case -1423054677: // friendly
          this.getFriendly().add((FriendlyLanguageComponent) value); // FriendlyLanguageComponent
          return value;
        case 102851257: // legal
          this.getLegal().add((LegalLanguageComponent) value); // LegalLanguageComponent
          return value;
        case 3512060: // rule
          this.rule = (ComputableLanguageComponent) value; // ComputableLanguageComponent
          return value;
        case -126751329: // legallyBinding
          this.legallyBinding = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ContractStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ContractStatus>
        } else if (name.equals("contentDerivative")) {
          this.contentDerivative = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("issued")) {
          this.issued = castToDateTime(value); // DateTimeType
        } else if (name.equals("applies")) {
          this.applies = castToPeriod(value); // Period
        } else if (name.equals("subject")) {
          this.getSubject().add(castToReference(value));
        } else if (name.equals("authority")) {
          this.getAuthority().add(castToReference(value));
        } else if (name.equals("domain")) {
          this.getDomain().add(castToReference(value));
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.getSubType().add(castToCodeableConcept(value));
        } else if (name.equals("term")) {
          this.getTerm().add((TermComponent) value);
        } else if (name.equals("signer")) {
          this.getSigner().add((SignatoryComponent) value);
        } else if (name.equals("friendly")) {
          this.getFriendly().add((FriendlyLanguageComponent) value);
        } else if (name.equals("legal")) {
          this.getLegal().add((LegalLanguageComponent) value);
        } else if (name.equals("rule")) {
          this.rule = (ComputableLanguageComponent) value; // ComputableLanguageComponent
        } else if (name.equals("legallyBinding[x]")) {
          this.legallyBinding = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -92412192:  return getContentDerivative(); 
        case -1179159893:  return getIssuedElement();
        case -793235316:  return getApplies(); 
        case -1867885268:  return addSubject(); 
        case 1475610435:  return addAuthority(); 
        case -1326197564:  return addDomain(); 
        case 3575610:  return getType(); 
        case -1868521062:  return addSubType(); 
        case 3556460:  return addTerm(); 
        case -902467798:  return addSigner(); 
        case -1423054677:  return addFriendly(); 
        case 102851257:  return addLegal(); 
        case 3512060:  return getRule(); 
        case -772497791:  return getLegallyBinding(); 
        case -126751329:  return getLegallyBinding(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -92412192: /*contentDerivative*/ return new String[] {"CodeableConcept"};
        case -1179159893: /*issued*/ return new String[] {"dateTime"};
        case -793235316: /*applies*/ return new String[] {"Period"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1475610435: /*authority*/ return new String[] {"Reference"};
        case -1326197564: /*domain*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case 3556460: /*term*/ return new String[] {};
        case -902467798: /*signer*/ return new String[] {};
        case -1423054677: /*friendly*/ return new String[] {};
        case 102851257: /*legal*/ return new String[] {};
        case 3512060: /*rule*/ return new String[] {};
        case -126751329: /*legallyBinding*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.status");
        }
        else if (name.equals("contentDerivative")) {
          this.contentDerivative = new CodeableConcept();
          return this.contentDerivative;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("authority")) {
          return addAuthority();
        }
        else if (name.equals("domain")) {
          return addDomain();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          return addSubType();
        }
        else if (name.equals("term")) {
          return addTerm();
        }
        else if (name.equals("signer")) {
          return addSigner();
        }
        else if (name.equals("friendly")) {
          return addFriendly();
        }
        else if (name.equals("legal")) {
          return addLegal();
        }
        else if (name.equals("rule")) {
          this.rule = new ComputableLanguageComponent();
          return this.rule;
        }
        else if (name.equals("legallyBindingAttachment")) {
          this.legallyBinding = new Attachment();
          return this.legallyBinding;
        }
        else if (name.equals("legallyBindingReference")) {
          this.legallyBinding = new Reference();
          return this.legallyBinding;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Contract";

  }

      public Contract copy() {
        Contract dst = new Contract();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.contentDerivative = contentDerivative == null ? null : contentDerivative.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        if (authority != null) {
          dst.authority = new ArrayList<Reference>();
          for (Reference i : authority)
            dst.authority.add(i.copy());
        };
        if (domain != null) {
          dst.domain = new ArrayList<Reference>();
          for (Reference i : domain)
            dst.domain.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (subType != null) {
          dst.subType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subType)
            dst.subType.add(i.copy());
        };
        if (term != null) {
          dst.term = new ArrayList<TermComponent>();
          for (TermComponent i : term)
            dst.term.add(i.copy());
        };
        if (signer != null) {
          dst.signer = new ArrayList<SignatoryComponent>();
          for (SignatoryComponent i : signer)
            dst.signer.add(i.copy());
        };
        if (friendly != null) {
          dst.friendly = new ArrayList<FriendlyLanguageComponent>();
          for (FriendlyLanguageComponent i : friendly)
            dst.friendly.add(i.copy());
        };
        if (legal != null) {
          dst.legal = new ArrayList<LegalLanguageComponent>();
          for (LegalLanguageComponent i : legal)
            dst.legal.add(i.copy());
        };
        dst.rule = rule == null ? null : rule.copy();
        dst.legallyBinding = legallyBinding == null ? null : legallyBinding.copy();
        return dst;
      }

      protected Contract typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Contract))
          return false;
        Contract o = (Contract) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(contentDerivative, o.contentDerivative, true)
           && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true) && compareDeep(subject, o.subject, true)
           && compareDeep(authority, o.authority, true) && compareDeep(domain, o.domain, true) && compareDeep(type, o.type, true)
           && compareDeep(subType, o.subType, true) && compareDeep(term, o.term, true) && compareDeep(signer, o.signer, true)
           && compareDeep(friendly, o.friendly, true) && compareDeep(legal, o.legal, true) && compareDeep(rule, o.rule, true)
           && compareDeep(legallyBinding, o.legallyBinding, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Contract))
          return false;
        Contract o = (Contract) other_;
        return compareValues(status, o.status, true) && compareValues(issued, o.issued, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, contentDerivative
          , issued, applies, subject, authority, domain, type, subType, term, signer
          , friendly, legal, rule, legallyBinding);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Contract;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Contract.identifier", description="The identity of the contract", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of the subject of the contract (if a patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Contract.subject", description="The identity of the subject of the contract (if a patient)", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of the subject of the contract (if a patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Contract:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identity of the subject of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Contract.subject", description="The identity of the subject of the contract", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identity of the subject of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Contract:subject").toLocked();

 /**
   * Search parameter: <b>authority</b>
   * <p>
   * Description: <b>The authority of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.authority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authority", path="Contract.authority", description="The authority of the contract", type="reference", target={Organization.class } )
  public static final String SP_AUTHORITY = "authority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authority</b>
   * <p>
   * Description: <b>The authority of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.authority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHORITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHORITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:authority</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHORITY = new ca.uhn.fhir.model.api.Include("Contract:authority").toLocked();

 /**
   * Search parameter: <b>domain</b>
   * <p>
   * Description: <b>The domain of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.domain</b><br>
   * </p>
   */
  @SearchParamDefinition(name="domain", path="Contract.domain", description="The domain of the contract", type="reference", target={Location.class } )
  public static final String SP_DOMAIN = "domain";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>domain</b>
   * <p>
   * Description: <b>The domain of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.domain</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DOMAIN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DOMAIN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:domain</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DOMAIN = new ca.uhn.fhir.model.api.Include("Contract:domain").toLocked();

 /**
   * Search parameter: <b>issued</b>
   * <p>
   * Description: <b>The date/time the contract was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Contract.issued</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issued", path="Contract.issued", description="The date/time the contract was issued", type="date" )
  public static final String SP_ISSUED = "issued";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issued</b>
   * <p>
   * Description: <b>The date/time the contract was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Contract.issued</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ISSUED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ISSUED);

 /**
   * Search parameter: <b>signer</b>
   * <p>
   * Description: <b>Contract Signatory Party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.signer.party</b><br>
   * </p>
   */
  @SearchParamDefinition(name="signer", path="Contract.signer.party", description="Contract Signatory Party", type="reference", target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_SIGNER = "signer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>signer</b>
   * <p>
   * Description: <b>Contract Signatory Party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.signer.party</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SIGNER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SIGNER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:signer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SIGNER = new ca.uhn.fhir.model.api.Include("Contract:signer").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Contract.status", description="The status of the contract", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

