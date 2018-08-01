/*
 * Copyright (c) 2012-2017 The ANTLR Project. All rights reserved.
 * Use of this file is governed by the BSD 3-clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.test.runtime.javascript.chrome;

import org.antlr.v4.test.runtime.javascript.browser.BaseBrowserTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseChromeTest extends BaseBrowserTest {
	@BeforeClass
	public static void initWebDriver() {
		driver = SharedWebDriver.init();
	}

	@AfterClass
	public static void closeWebDriver() {
		SharedWebDriver.close();
	}

}
