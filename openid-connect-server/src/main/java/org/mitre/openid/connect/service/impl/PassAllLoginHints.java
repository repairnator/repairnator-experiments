/*******************************************************************************
 * Copyright 2017 The MITRE Corporation
 *   and the MIT Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.mitre.openid.connect.service.impl;

import org.mitre.openid.connect.service.LoginHintExtracter;

/**
 * Sends all login hints through to the login page regardless of setup.
 *
 * @author jricher
 *
 */
public class PassAllLoginHints implements LoginHintExtracter {

	/* (non-Javadoc)
	 * @see org.mitre.openid.connect.service.LoginHintTester#useHint(java.lang.String)
	 */
	@Override
	public String extractHint(String loginHint) {
		return loginHint;
	}

}
