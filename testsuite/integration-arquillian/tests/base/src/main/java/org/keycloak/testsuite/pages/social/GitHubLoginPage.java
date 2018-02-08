/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.testsuite.pages.social;

import org.keycloak.testsuite.util.UIUtils;
import org.keycloak.testsuite.util.URLUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Vaclav Muzikar <vmuzikar@redhat.com>
 */
public class GitHubLoginPage extends AbstractSocialLoginPage {
    @FindBy(id = "login_field")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "commit")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement logoutButton;

    @Override
    public void login(String user, String password) {
        usernameInput.clear();
        usernameInput.sendKeys(user);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    @Override
    public void logout() {
        log.info("performing logout from GitHub");
        URLUtils.navigateToUri("https://github.com/logout", true);
        UIUtils.clickLink(logoutButton);
    }
}
