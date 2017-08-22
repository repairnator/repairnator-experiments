/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.es;

import java.util.Arrays;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;

public class DefaultIndexSettings {

  /** Maximum length of ngrams. */
  public static final int MAXIMUM_NGRAM_LENGTH = 15;

  /** Pattern, that splits the user search input **/
  public static final String SEARCH_TERM_TOKENIZER_PATTERN = "[\\s]+";

  public static final String ANALYSIS = "index.analysis";
  public static final String DELIMITER = ".";

  public static final String TOKENIZER = "tokenizer";
  public static final String FILTER = "filter";
  public static final String ANALYZER = "analyzer";
  public static final String SEARCH_ANALYZER = "search_analyzer";

  public static final String TYPE = "type";
  public static final String INDEX = "index";
  public static final String ANALYZED = "analyzed";
  public static final String STRING = "string";
  public static final String STANDARD = "standard";
  public static final String PATTERN = "pattern";
  public static final String CUSTOM = "custom";
  public static final String KEYWORD = "keyword";
  public static final String CLASSIC = "classic";
  public static final String TRUNCATE = "truncate";

  public static final String SUB_FIELD_DELIMITER = ".";

  public static final String TRIM = "trim";
  public static final String LOWERCASE = "lowercase";
  public static final String WHITESPACE = "whitespace";
  public static final String STOP = "stop";
  public static final String ASCIIFOLDING = "asciifolding";
  public static final String PORTER_STEM = "porter_stem";
  public static final String MIN_GRAM = "min_gram";
  public static final String MAX_GRAM = "max_gram";
  public static final String LENGTH = "length";

  private DefaultIndexSettings() {
    // only static stuff
  }

  static Settings.Builder defaults() {
    Settings.Builder builder = Settings.builder()
      .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, 1)
      .put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 0)
      .put("index.refresh_interval", "30s")
      .put("index.mapper.dynamic", false);

    Arrays.stream(DefaultIndexSettingsElement.values())
      .map(DefaultIndexSettingsElement::settings)
      .forEach(builder::put);

    return builder;
  }
}
