/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.text;

import static com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.text.ControlToolbar.TOOLBAR_ITEM_SELECTOR;

import com.cognifide.qa.bb.qualifier.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents text justification options.
 */
@PageObject
public class JustifyControls {

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='justify#justifyleft']")
  private WebElement justifyLeftBtn;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='justify#justifycenter']")
  private WebElement justifyCenterBtn;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='justify#justifyright']")
  private WebElement justifyRightBtn;

  public WebElement getJustifyLeftBtn() {
    return justifyLeftBtn;
  }

  public WebElement getJustifyCenterBtn() {
    return justifyCenterBtn;
  }

  public WebElement getJustifyRightBtn() {
    return justifyRightBtn;
  }

}
