/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.aem.ui.menu;

/**
 * This enum represents options available in context menu and toolbar.
 */
public enum MenuOption {

  EDIT("Edit"),
  TARGET("Target"),
  ANNOTATE("Annotate"),
  CUT("Cut"),
  COPY("Copy"),
  PASTE("Paste"),
  DELETE("Delete"),
  NEW("New..."),
  MENU("Menu"),
  MANAGE_STYLES("Manage styles"),
  MANAGE_SELECTION_STYLES("Manage selection styles"),
  MANAGE_HYPERLINK_STYLES("Manage hyperlink styles");

  private final String label;

  MenuOption(String label) {
    this.label = label;
  }

  /**
   * @return Label of the menu option.
   */
  public String getLabel() {
    return label;
  }
}
