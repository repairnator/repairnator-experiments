/*******************************************************************************
 *
 *    Copyright (C) 2015-2018 Jan Kristof Nidzwetzki
 *  
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License. 
 *    
 *******************************************************************************/
package com.github.jnidzwetzki.bitfinex.v2.commands;

import java.util.Collection;

import org.json.JSONObject;

import com.github.jnidzwetzki.bitfinex.v2.BitfinexApiBroker;
import com.github.jnidzwetzki.bitfinex.v2.BitfinexConnectionFeature;

public class SetConnectionFeaturesCommand extends AbstractAPICommand {

	/**
	 * The active features
	 */
	private Collection<BitfinexConnectionFeature> features;

	public SetConnectionFeaturesCommand(final Collection<BitfinexConnectionFeature> features) {
		this.features = features;
	}
	
	@Override
	public String getCommand(final BitfinexApiBroker bitfinexApiBroker) {
		
		// XOR all features
		int featureFlags = 0;
		for(final BitfinexConnectionFeature feature : features) {
			featureFlags = featureFlags ^ feature.getFeatureFlag();
		}
		
		final JSONObject subscribeJson = new JSONObject();
		subscribeJson.put("event", "conf");
		subscribeJson.put("flags", featureFlags);
		return subscribeJson.toString();
	}

}
