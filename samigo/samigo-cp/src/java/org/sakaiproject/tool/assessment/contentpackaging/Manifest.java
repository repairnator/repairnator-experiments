/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.tool.assessment.contentpackaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.slf4j.Slf4j;
import org.sakaiproject.tool.assessment.qti.asi.ASIBaseClass;
import org.sakaiproject.tool.assessment.qti.constants.QTIConstantStrings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>Copyright: Copyright (c) 2003-5</p>
 * <p>Organization: Sakai Project</p>
 * @author casong
 * @author Ed Smiley esmiley@stanford.edu
 * @version $Id$
 */
 @Slf4j
 public class Manifest extends ASIBaseClass
{
  private String basePath;
  private Map sections;
  private Map items;
	public static final String MANIFEST_TAG = "manifest";
	public static final String RESOURCE_PATH = "manifest/resources/resource";
	public static final String FILE_PATH = "manifest/resources/resource/file";
	
  /**
   * Creates a new Assessment object.
   */
  protected Manifest()
  {
    super();
    this.sections = new HashMap();
    this.items = new HashMap();
    this.basePath = QTIConstantStrings.QUESTESTINTEROP + "/" +
                    QTIConstantStrings.ASSESSMENT;
  }

  /**
   * Creates a new Assessment object.
   *
   * @param document the Document containing the assessment.
   */
  public Manifest(Document document)
  {
    super(document);
    this.sections = new HashMap();
    this.items = new HashMap();
    this.basePath = QTIConstantStrings.QUESTESTINTEROP + "/" +
                    QTIConstantStrings.ASSESSMENT;
  }

  /**
   * @param fieldlabel
   * @param setValue
   */
  public void setFieldentry(String prefixPath, String fieldlabel, String setValue)
  {
    //String xpath = "questestinterop/assessment/qtimetadata/qtimetadatafield/fieldlabel[text()='" + fieldlabel + "']/following-sibling::fieldentry";
	  StringBuilder xpath = new StringBuilder(prefixPath);
	  xpath.append("/fieldlabel[text()='");
	  xpath.append(fieldlabel);
	  xpath.append("']/following-sibling::fieldentry");

    super.setFieldentry(xpath.toString(), setValue);
  }
  /**
   * Add a section ref with section Id sectionId.
   *
   * @param sectionId
   */
  public void addSectionRef(String sectionId)
  {
    if(log.isDebugEnabled())
    {
      log.debug("addSection(String " + sectionId + ")");
    }

    try {
    	String xpath = basePath;
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = dbf.newDocumentBuilder();
    	Document document = db.newDocument();
    	Element element = document.createElement(QTIConstantStrings.SECTIONREF);
    	element.setAttribute(QTIConstantStrings.LINKREFID, sectionId);
    	this.addElement(xpath, element);
    } catch(ParserConfigurationException pce) {
    	log.error("Exception thrown from addSectionRef() : " + pce.getMessage());
    }
  }

  /**
   * Remove a section ref with section Id sectionId.
   *
   * @param sectionId
   */
  public void removeSectionRef(String sectionId)
  {
    if(log.isDebugEnabled())
    {
      log.debug("removeSectionRef(String " + sectionId + ")");
    }

    String xpath =
      basePath + "/" + QTIConstantStrings.SECTIONREF + "[@" +
      QTIConstantStrings.LINKREFID + "='" + sectionId + "']";
    this.removeElement(xpath);
  }

  /**
   * Remove all section refs.
   */
  public void removeSectionRefs()
  {
    log.debug("removeSectionRefs()");
    String xpath = basePath + "/" + QTIConstantStrings.SECTIONREF;
    this.removeElement(xpath);
  }

  /**
   * Get a collection of section refs.
   *
   * @return
   */
  public List getSectionRefs()
  {
    log.debug("getSectionRefs()");
    String xpath = basePath + "/" + QTIConstantStrings.SECTIONREF;

    return this.selectNodes(xpath);
  }

  /**
   * Get a collection of sections.
   * @return the sections
   */
  public Collection getSections()
  {
  	return this.sections.values();
  }

  /**
   * Get a collection of items.
   * @return the items
   */
  public Collection getItems()
  {
  	return this.items.values();
  }

  /**
   *
   *
   * @return
   */
  public List getSectionRefIds()
  {
    log.debug("getSectionRefIds()");
    List refs = this.getSectionRefs();
    List ids = new ArrayList();
    int size = refs.size();
    for(int i = 0; i < size; i++)
    {
      Element ref = (Element) refs.get(0);
      String idString = ref.getAttribute(QTIConstantStrings.LINKREFID);
      ids.add(idString);
    }

    return ids;
  }

  /**
   * Assessment title.
   * @return title
   */
  public String getTitle()
  {
    String title = "";
    String xpath = basePath;
    List list = this.selectNodes(xpath);
    if(list.size()>0)
    {
      Element element = (Element)list.get(0);
      title = element.getAttribute("title");
    }
    return title;
  }

  /**
   * Assessment id (ident attribute)
   * @return ident
   */
	public String getIdent()
	{
		String ident = "";
		String xpath = basePath;
		List list = this.selectNodes(xpath);
		if(list.size()>0)
		{
			Element element = (Element)list.get(0);
			ident = element.getAttribute("ident");
		}
		return ident;
	}


  /**
   * Assessment id (ident attribute)
   * @param ident the ident
   */
  public void setIdent(String ident)
  {
    String xpath = basePath;
    List list = this.selectNodes(xpath);
    if(list.size()>0)
    {
      Element element = (Element)list.get(0);
      element.setAttribute("ident", ident);
    }
  }

  /**
   * Assessment title.
   * @param title
   */
  public void setTitle(String title)
  {
    String xpath = basePath;
    List list = this.selectNodes(xpath);
    if(list.size()>0)
    {
      Element element = (Element)list.get(0);
      element.setAttribute("title", escapeXml(title));
    }
  }

  /**
   * Base XPath for the assessment.
   * @return
   */
  public String getBasePath()
  {
    return basePath;
  }

  /**
   * Base XPath for the assessment.
   * @param basePath
   */
  public void setBasePath(String basePath)
  {
    this.basePath = basePath;
  }
}


