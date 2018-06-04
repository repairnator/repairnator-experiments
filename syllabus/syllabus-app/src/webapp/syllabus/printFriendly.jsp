<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

	<sakai:view_container title="#{SyllabusTool.siteTitle}">
	<sakai:view_content>


<%-- gsilver: global things about syllabus tool:
1 ) what happens to empty lists - still generate a table?
2 ) Ids generated by jsf start with _  not optimal keeps us from validating fully.
 --%>
	<h:form>
		 	<div class="noprint">
				<a id="printIcon" href="" onClick="javascript:window.print();">
					<h:outputText value="#{msgs.send_to_printer}" />
				</a>
				<h:outputText value=" " /><h:outputText value="|" /><h:outputText value=" " />
				<a value="" href="" onClick="window.close();" >
					<h:outputText value="#{msgs.close_window}" />
				</a>
			</div>
			
			<syllabus:syllabus_if test="#{SyllabusTool.syllabusItem.redirectURL}">
				<h:dataTable value="#{SyllabusTool.entries}" var="eachEntry" rendered="#{! SyllabusTool.syllabusItem.redirectURL}" style="margin-top:1em;clear:both;" summary="#{msgs.mainCaption}" >
					<h:column>
							<f:verbatim><h4 class="textPanelHeader"></f:verbatim>		  
								<h:outputText rendered="#{eachEntry.status == eachEntry.draftStatus}" value="#{msgs.mainDraft} - "/> 
								<h:outputText value="#{eachEntry.entry.title}" />
								<f:subview id="date" rendered="#{eachEntry.entry.startDate != null || eachEntry.entry.endDate != null}">
									<f:verbatim><br/><span style="font-weight: normal"></f:verbatim>
										<h:outputText value="("/>
										<h:outputText value="#{eachEntry.entry.startDate}">
											<f:convertDateTime type="date" pattern="EEE MMM dd, yyyy hh:mm a"/>
										</h:outputText>
										<h:outputText value=" - " rendered="#{eachEntry.entry.startDate != null && eachEntry.entry.endDate != null}"/>
										<h:outputText value="#{eachEntry.entry.endDate}" rendered="#{!eachEntry.startAndEndDatesSameDay}">
								  			<f:convertDateTime type="date" pattern="EEE MMM dd, yyyy hh:mm a"/>
										</h:outputText>
										<h:outputText value="#{eachEntry.entry.endDate}" rendered="#{eachEntry.startAndEndDatesSameDay}">
								  			<f:convertDateTime type="date" pattern="hh:mm a"/>
										</h:outputText>
										<h:outputText value=")"/>
									<f:verbatim></span></f:verbatim>
								</f:subview>
							<f:verbatim></h4></f:verbatim>
							<f:verbatim><div class="textPanel"></f:verbatim>
							<syllabus:syllabus_htmlShowArea value="#{eachEntry.entry.asset}" />
							<f:verbatim></div></f:verbatim>
							<h:dataTable value="#{eachEntry.attachmentList}" var="eachAttach" styleClass="indnt1">
							  <h:column>
									<f:facet name="header">
										<h:outputText value="" />
									</f:facet>
									<sakai:contentTypeMap fileType="#{eachAttach.type}" mapType="image" var="imagePath" pathPrefix="/library/image/"/>									
									<h:graphicImage id="exampleFileIcon" value="#{imagePath}" />
									<h:outputLink value="#{eachAttach.url}" target="_blank" title="#{msgs.openLinkNewWindow}">
										<h:outputText value=" "/><h:outputText value="#{eachAttach.name}"/>
									</h:outputLink>
								</h:column>
							</h:dataTable>
					</h:column>
				</h:dataTable>
				<h:outputText value="#{msgs.syllabus_noEntry}" styleClass="instruction" rendered="#{SyllabusTool.displayNoEntryMsg}"/>
			</syllabus:syllabus_if>				
			<syllabus:syllabus_ifnot test="#{SyllabusTool.syllabusItem.redirectURL}">
				<h:outputText escape="false" value="#{msgs.redirect_explanation} " />
				<h:outputLink target="_blank" rel="noopener" title="#{msgs.openLinkNewWindow}" value="#{SyllabusTool.syllabusItem.redirectURL}">
					<h:outputText escape="false" value="#{SyllabusTool.syllabusItem.redirectURL}" />
				</h:outputLink>
			</syllabus:syllabus_ifnot>
		</h:form>
	</sakai:view_content>
	</sakai:view_container>
</f:view>
