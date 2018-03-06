package eu.europeana.metis.core.workflow.plugins;

import com.fasterxml.jackson.annotation.JsonFormat;
import eu.europeana.metis.CommonStringValues;
import java.util.Date;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2017-05-29
 */
public class OaipmhHarvestPluginMetadata extends AbstractMetisPluginMetadata {
  private static final PluginType PLUGIN_TYPE = PluginType.OAIPMH_HARVEST;
  private String url;
  private String metadataFormat;
  private String setSpec;
  @JsonFormat(pattern = CommonStringValues.DATE_FORMAT)
  private Date fromDate;
  @JsonFormat(pattern = CommonStringValues.DATE_FORMAT)
  private Date untilDate;

  public OaipmhHarvestPluginMetadata() {
    //Required for json serialization
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMetadataFormat() {
    return metadataFormat;
  }

  public void setMetadataFormat(String metadataFormat) {
    this.metadataFormat = metadataFormat;
  }

  public String getSetSpec() {
    return setSpec;
  }

  public void setSetSpec(String setSpec) {
    this.setSpec = setSpec;
  }

  public Date getFromDate() {
    return fromDate == null?null:new Date(fromDate.getTime());
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate == null?null:new Date(fromDate.getTime());
  }

  public Date getUntilDate() {
    return untilDate == null?null:new Date(untilDate.getTime());
  }

  public void setUntilDate(Date untilDate) {
    this.untilDate = untilDate == null?null:new Date(untilDate.getTime());
  }

  @Override
  public PluginType getPluginType() {
    return PLUGIN_TYPE;
  }

}
