/*
 * Copyright 2007 Yusuke Yamamoto
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

package twitter4j.examples.savedsearches;

import twitter4j.SavedSearch;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.List;

/**
 * List saved searches.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetSavedSearches {
    /**
     * Usage: java twitter4j.examples.savedsearches.GetSavedSearches
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            List<SavedSearch> savedSearches = twitter.getSavedSearches();
            for (SavedSearch savedSearch : savedSearches) {
                System.out.println("[name:" + savedSearch.getName() + " query:" + savedSearch.getQuery() + " id:"
                        + savedSearch.getId() + "]");
            }
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to retrieve saved searches: " + te.getMessage());
            System.exit(-1);
        }
    }
}
