package eu.europeana.enrichment.api.external;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Wrapper Entity for the output generated by Enrichment
 * 
 * @author Yorgos.Mamakis@ europeana.eu
 *
 */
@XmlRootElement
@JsonSerialize
public class EntityWrapper {

  @XmlElement
  private String className;
  @XmlElement
  private String originalField;
  @XmlElement
  private String contextualEntity;
  @XmlElement
  private String url;
  @XmlElement
  private String originalValue;

  public EntityWrapper() {
    // Required for XML binding.
  }

  public String getOriginalField() {
    return originalField;
  }

  public void setOriginalField(String originalField) {
    this.originalField = originalField;
  }

  public String getContextualEntity() {
    return contextualEntity;
  }

  public void setContextualEntity(String contextualEntity) {
    this.contextualEntity = contextualEntity;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getOriginalValue() {
    return originalValue;
  }

  public void setOriginalValue(String originalValue) {
    this.originalValue = originalValue;
  }
}
