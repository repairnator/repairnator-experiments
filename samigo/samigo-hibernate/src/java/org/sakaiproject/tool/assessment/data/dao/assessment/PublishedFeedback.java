/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.tool.assessment.data.dao.assessment;

import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentBaseIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentFeedbackIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;

public class PublishedFeedback
    implements java.io.Serializable, AssessmentFeedbackIfc
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6725754767789047244L;
private Long id;
  private AssessmentIfc assessment;
  private Integer feedbackDelivery; // 0 = cannot edit, 1=immediate, 2=on specific date , 3= no feedback
  private Integer feedbackComponentOption; // total scores only, or select components 
  private Integer feedbackAuthoring; // 0 = cannot edit, 1=questionlevel, 2=sectionlevel, 3= both feedback
  private Integer editComponents; // 0 = cannot
  private Boolean showQuestionText;
  private Boolean showStudentResponse;
  private Boolean showCorrectResponse;
  private Boolean showStudentScore;
  private Boolean showStudentQuestionScore;
  private Boolean showQuestionLevelFeedback;
  private Boolean showSelectionLevelFeedback; // must be MC
  private Boolean showGraderComments;
  private Boolean showStatistics;
  private Long assessmentId;

  /**
   * Creates a new SubmissionModel object.
   */
  public PublishedFeedback()
  {
    setShowCorrectResponse(Boolean.FALSE);
    setShowGraderComments(Boolean.FALSE);
    setShowQuestionLevelFeedback(Boolean.FALSE);
    setShowQuestionText(Boolean.TRUE);
    setShowSelectionLevelFeedback(Boolean.FALSE);
    setShowStatistics(Boolean.FALSE);
    setShowStudentScore(Boolean.FALSE);
    setShowStudentQuestionScore(Boolean.FALSE);
    setFeedbackDelivery(AssessmentFeedbackIfc.NO_FEEDBACK);
    setFeedbackComponentOption(AssessmentFeedbackIfc.SELECT_COMPONENTS);
    setFeedbackAuthoring(AssessmentFeedbackIfc.QUESTIONLEVEL_FEEDBACK);
  }

  public PublishedFeedback(
      Long assessmentId,
      Integer feedbackDelivery, Integer feedbackComponentOption, Integer feedbackAuthoring, Integer editComponents, Boolean showQuestionText,
      Boolean showStudentResponse, Boolean showCorrectResponse,
      Boolean showStudentScore,  Boolean showStudentQuestionScore, 
      Boolean showQuestionLevelFeedback, Boolean showSelectionLevelFeedback,
      Boolean showGraderComments, Boolean showStatistics)
  {
    this.assessmentId = assessmentId;
    this.feedbackDelivery = feedbackDelivery;
    this.feedbackComponentOption = feedbackComponentOption;
    this.feedbackAuthoring = feedbackAuthoring;
    this.editComponents = editComponents;
    this.showQuestionText = showQuestionText;
    this.showStudentResponse = showStudentResponse;
    this.showCorrectResponse = showCorrectResponse;
    this.showStudentScore = showStudentScore;
    this.showStudentQuestionScore = showStudentQuestionScore;
    this.showQuestionLevelFeedback = showQuestionLevelFeedback;
    this.showSelectionLevelFeedback = showSelectionLevelFeedback; // must be MC
    this.showGraderComments = showGraderComments;
    this.showStatistics = showStatistics;
  }

  public PublishedFeedback(
      Integer feedbackDelivery,Integer feedbackComponentOption, Integer feedbackAuthoring, Integer editComponents, Boolean showQuestionText,
      Boolean showStudentResponse, Boolean showCorrectResponse,
      Boolean showStudentScore,  Boolean showStudentQuestionScore,
      Boolean showQuestionLevelFeedback, Boolean showSelectionLevelFeedback,
      Boolean showGraderComments, Boolean showStatistics)
  {
    this.feedbackDelivery = feedbackDelivery;
    this.feedbackComponentOption = feedbackComponentOption;
    this.feedbackAuthoring = feedbackAuthoring;
    this.editComponents = editComponents;
    this.showQuestionText = showQuestionText;
    this.showStudentResponse = showStudentResponse;
    this.showCorrectResponse = showCorrectResponse;
    this.showStudentScore = showStudentScore;
    this.showStudentQuestionScore = showStudentQuestionScore;
    this.showQuestionLevelFeedback = showQuestionLevelFeedback;
    this.showSelectionLevelFeedback = showSelectionLevelFeedback; // must be MC
    this.showGraderComments = showGraderComments;
    this.showStatistics = showStatistics;
  }

  public Object clone() throws CloneNotSupportedException{
    Object cloned = new PublishedFeedback(
        this.getFeedbackDelivery(), this.getFeedbackComponentOption(), this.getFeedbackAuthoring(), this.getEditComponents(),
        this.getShowQuestionText(),
        this.getShowStudentResponse(), this.getShowCorrectResponse(),
        this.getShowStudentScore(),   this.getShowStudentQuestionScore(), 
        this.getShowQuestionLevelFeedback(),
        this.getShowSelectionLevelFeedback(), this.getShowGraderComments(),
        this.getShowStatistics());
    return cloned;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public void setAssessment(AssessmentIfc assessment)
  {
    this.assessment = assessment;
  }

  public AssessmentIfc getAssessment()
  {
     return (AssessmentIfc)assessment;
  }

  public void setAssessmentBase(AssessmentBaseIfc assessment)
  {
    setAssessment((AssessmentIfc)assessment);
  }

  public AssessmentBaseIfc getAssessmentBase()
  {
    return getAssessment();
  }

  public Integer getFeedbackDelivery()
  {
    return feedbackDelivery;
  }

  public void setFeedbackDelivery(Integer feedbackDelivery)
  {
    this.feedbackDelivery = feedbackDelivery;
  }

  public Integer getFeedbackAuthoring()
  {
    return feedbackAuthoring;
  }

  public void setFeedbackAuthoring(Integer feedbackAuthoring)
  {
    this.feedbackAuthoring = feedbackAuthoring;
  }

  public Integer getEditComponents() {
    return editComponents;
  }

  public void setEditComponents(Integer editComponents) {
    this.editComponents = editComponents;
  }

  public Boolean getShowQuestionText()
  {
    return showQuestionText;
  }

  public void setShowQuestionText(Boolean showQuestionText)
  {
    this.showQuestionText = showQuestionText;
  }

  public Boolean getShowStudentResponse()
  {
    return showStudentResponse;
  }

  public void setShowStudentResponse(Boolean showStudentResponse)
  {
    this.showStudentResponse = showStudentResponse;
  }

  public Boolean getShowCorrectResponse()
  {
    return showCorrectResponse;
  }

  public void setShowCorrectResponse(Boolean showCorrectResponse)
  {
    this.showCorrectResponse = showCorrectResponse;
  }

  public Boolean getShowStudentScore()
  {
    return showStudentScore;
  }

  public void setShowStudentScore(Boolean showStudentScore)
  {
    this.showStudentScore = showStudentScore;
  }

   public Boolean getShowStudentQuestionScore()
  {
    return showStudentQuestionScore;
  }

  public void setShowStudentQuestionScore(Boolean showStudentQuestionScore)
  {
    this.showStudentQuestionScore = showStudentQuestionScore;
  }

  public Boolean getShowQuestionLevelFeedback()
  {
    return showQuestionLevelFeedback;
  }

  public void setShowQuestionLevelFeedback(Boolean showQuestionLevelFeedback)
  {
    this.showQuestionLevelFeedback = showQuestionLevelFeedback;
  }

  public Boolean getShowSelectionLevelFeedback()
  {
    return showSelectionLevelFeedback;
  }

  public void setShowSelectionLevelFeedback(Boolean showSelectionLevelFeedback)
  {
    this.showSelectionLevelFeedback = showSelectionLevelFeedback;
  }

  public Boolean getShowGraderComments()
  {
    return showGraderComments;
  }

  public void setShowGraderComments(Boolean showGraderComments)
  {
    this.showGraderComments = showGraderComments;
  }

  public Boolean getShowStatistics()
  {
    return showStatistics;
  }

  public void setShowStatistics(Boolean showStatistics)
  {
    this.showStatistics = showStatistics;
  }

  public Long getAssessmentId() {
    return this.assessmentId;
  }

  public void setAssessmentId(Long assessmentId) {
    this.assessmentId = assessmentId;
  }

public Integer getFeedbackComponentOption() {
	return feedbackComponentOption;
}

public void setFeedbackComponentOption(Integer feedbackComponentOption) {
	this.feedbackComponentOption = feedbackComponentOption;
	
}

}
