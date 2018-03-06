package eu.europeana.normalization.common.language.nal;

import eu.europeana.normalization.common.language.LanguagesVocabulary;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 * Data about a language in NAL. It is a subset of the data available in NAL, containing only the
 * data used for matching and normalizing.
 *
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 16/03/2016
 */
public class NalLanguage {

  private String nalId = null;
  private String iso6391 = null;
  private String iso6392b = null;
  private String iso6392t = null;
  private String iso6393 = null;
  private List<Label> originalNames = new ArrayList<>(3);
  private List<Label> alternativeNames = new ArrayList<>(3);
  private List<Label> labels = new ArrayList<>();

  /**
   * Creates a new instance of this class.
   */
  public NalLanguage(String nalId) {
    super();
    this.nalId = nalId;
  }

  public String getIso6391() {
    return iso6391;
  }

  public void setIso6391(String iso6391) {
    this.iso6391 = iso6391;
  }

  public String getIso6392b() {
    return iso6392b;
  }

  public void setIso6392b(String iso6392b) {
    this.iso6392b = iso6392b;
  }

  public String getIso6392t() {
    return iso6392t;
  }

  public void setIso6392t(String iso6392t) {
    this.iso6392t = iso6392t;
  }

  public String getIso6393() {
    return iso6393;
  }

  public void setIso6393(String iso6393) {
    this.iso6393 = iso6393;
  }

  public List<Label> getOriginalNames() {
    return originalNames;
  }

  public void setOriginalNames(List<Label> originalNames) {
    this.originalNames = originalNames;
  }

  public List<Label> getAlternativeNames() {
    return alternativeNames;
  }

  public void setAlternativeNames(List<Label> alternativeNames) {
    this.alternativeNames = alternativeNames;
  }

  public List<Label> getLabels() {
    return labels;
  }

  public void setLabels(List<Label> labels) {
    this.labels = labels;
  }

  /**
   * @return
   */
  public Set<String> getAllLabels() {
    HashSet<String> dedup = new HashSet<>(labels.size() + 8);
    for (Label l : originalNames) {
      dedup.add(l.getLabel());
    }
    for (Label l : alternativeNames) {
      dedup.add(l.getLabel());
    }
    for (Label l : labels) {
      dedup.add(l.getLabel());
    }
    return dedup;
  }

  /**
   * @return
   */
  public Set<String> getAllLabelsAndCodes() {
    HashSet<String> dedup = new HashSet<>(labels.size() + 8);
    if (!StringUtils.isEmpty(iso6391)) {
      dedup.add(iso6391);
    }
    if (!StringUtils.isEmpty(iso6392b)) {
      dedup.add(iso6392b);
    }
    if (!StringUtils.isEmpty(iso6392t)) {
      dedup.add(iso6392t);
    }
    if (!StringUtils.isEmpty(iso6393)) {
      dedup.add(iso6393);
    }
    for (Label l : originalNames) {
      dedup.add(l.getLabel());
    }
    for (Label l : alternativeNames) {
      dedup.add(l.getLabel());
    }
    for (Label l : labels) {
      dedup.add(l.getLabel());
    }
    return dedup;
  }

  @Override
  public String toString() {
    return "NalLanguage [iso6391=" + iso6391 + ", iso6392b=" + iso6392b + ", iso6392t=" +
        iso6392t + ", iso6393=" + iso6393 + ", originalNames=" + originalNames +
        ", alternativeNames=" + alternativeNames + ", labels=" + labels + "]";
  }

  public String getNormalizedLanguageId(LanguagesVocabulary target) {
    switch (target) {
      case ISO_639_1:
        return getIso6391();
      case ISO_639_2b:
        return getIso6392b();
      case ISO_639_2t:
        return getIso6392t();
      case ISO_639_3:
        return getIso6393();
      case LANGUAGES_NAL:
        return getIso6393();
      default:
        throw new RuntimeException("TODO");
    }
  }

  public String getPrefLabel(String langCode) {
    for (Label l : originalNames) {
      if (StringUtils.equals(langCode, l.getLanguage())) {
        return l.getLabel();
      }
    }
    for (Label l : alternativeNames) {
      if (StringUtils.equals(langCode, l.getLanguage())) {
        return l.getLabel();
      }
    }
    for (Label l : labels) {
      if (StringUtils.equals(langCode, l.getLanguage())) {
        return l.getLabel();
      }
    }
    return originalNames.get(0).getLabel();
  }

}
