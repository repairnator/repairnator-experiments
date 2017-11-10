/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.jackrabbit.oak.jcr.query;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

import org.apache.jackrabbit.core.query.AbstractQueryTest;

/**
 * Tests the fulltext index.
 */
public class QueryFulltextTest extends AbstractQueryTest {

    public void testScore() throws Exception {
        Session session = superuser;
        QueryManager qm = session.getWorkspace().getQueryManager();
        Node n1 = testRootNode.addNode("node1");
        n1.setProperty("text", "hello hello hello");
        Node n2 = testRootNode.addNode("node2");
        n2.setProperty("text", "hello");
        session.save();

        String xpath = "/jcr:root//*[jcr:contains(@text, 'hello')] order by jcr:score()";
        Query q = qm.createQuery(xpath, "xpath");
        String result = getResult(q.execute(), "jcr:score");
        // expect two numbers (any value)
        result = result.replaceAll("[0-9\\.]+", "n");
        assertEquals("n, n", result);
    }

    public void testScoreWithOr() throws Exception {
        Session session = superuser;
        QueryManager qm = session.getWorkspace().getQueryManager();
        Node n1 = testRootNode.addNode("node1");
        n1.setProperty("text", "hello");
        n1.setProperty("id", "1");
        session.save();

        String xpath = "/jcr:root//*[jcr:contains(@text, 'hello') or @id = '1']";
        Query q = qm.createQuery(xpath, "xpath");
        String result = getResult(q.execute(), "jcr:path");
        assertEquals("/testroot/node1", result);
    }

    public void testFulltext() throws Exception {
        Session session = superuser;
        QueryManager qm = session.getWorkspace().getQueryManager();
        Node n1 = testRootNode.addNode("node1");
        n1.setProperty("text", "hello");
        Node n2 = testRootNode.addNode("node2");
        n2.setProperty("text", "hallo");
        Node n3 = testRootNode.addNode("node3");
        n3.setProperty("text", "hello hallo");
        session.save();

        // lowercase "or" mean search for the term "or"
        String sql2 = "select [jcr:path] as [path] from [nt:base] " +
                "where contains([text], 'hello or hallo') order by [jcr:path]";
        Query q = qm.createQuery(sql2, Query.JCR_SQL2);
        assertEquals("", getResult(q.execute(), "path"));

    }

    public void testSurrogateFulltext() throws Exception {
        String surrogateString = "壱\n" +
                "複数の文字\n" +
                "カナポ\n" +
                "ﾊﾝｶｸﾎﾟ\n" +
                "表十ソ\n" +
                "\uD842\uDF9F\n" +
                "Mixあポﾋﾟ表\uD842\uDF9F" +
                "";
        String [] searchStrs = new String[]{"\uD842\uDF9F", "Mix"};
        Session session = superuser;
        QueryManager qm = session.getWorkspace().getQueryManager();
        Node n1 = testRootNode.addNode("node");
        n1.setProperty("text", surrogateString);
        session.save();

        for (String searchTerm : searchStrs) {
            String sql2 = "select [jcr:path] as [path] from [nt:base] " +
                    "where contains([text], '" + searchTerm + "') order by [jcr:path]";
            Query q = qm.createQuery(sql2, Query.JCR_SQL2);
            log.println("Testing" + searchTerm);
            assertEquals("Lookup failed for " + searchTerm, "/testroot/node", getResult(q.execute(), "path"));
        }
    }

    public void testFulltextRelativeProperty() throws Exception {
        Session session = superuser;
        QueryManager qm = session.getWorkspace().getQueryManager();
        Node n1 = testRootNode.addNode("node1");
        n1.setProperty("text", "hello");
        Node n2 = testRootNode.addNode("node2");
        n2.setProperty("text", "hallo");
        Node n3 = testRootNode.addNode("node3");
        n3.setProperty("text", "hello hallo");
        session.save();

        Query q;

        String sql2 = "select [jcr:path] as [path] from [nt:base] " + 
                "where ISCHILDNODE([/testroot])" + 
                " AND CONTAINS(text, 'hallo')";

        q = qm.createQuery(sql2, Query.JCR_SQL2);
        assertEquals("/testroot/node2, /testroot/node3", getResult(q.execute(), "path"));

        sql2 = "select [jcr:path] as [path] from [nt:base] "
                + "where contains([node1/text], 'hello') order by [jcr:path]";
        q = qm.createQuery(sql2, Query.JCR_SQL2);
        assertEquals("/testroot", getResult(q.execute(), "path"));

        sql2 = "select [jcr:path] as [path] from [nt:base] "
                + "where contains([node2/text], 'hello OR hallo') order by [jcr:path]";
        q = qm.createQuery(sql2, Query.JCR_SQL2);
        assertEquals("/testroot", getResult(q.execute(), "path"));

        // TODO OAK-890
        // sql2 = "select [jcr:path] as [path] from [nt:base] "
        // + "where contains([node1/text], 'hello') "
        // + "and contains([node2/text], 'hallo') "
        // + "order by [jcr:path]";
        // q = qm.createQuery(sql2, Query.JCR_SQL2);
        // assertEquals("/testroot", getResult(q.execute(), "path"));
    }

    static String getResult(QueryResult result, String propertyName) throws RepositoryException {
        StringBuilder buff = new StringBuilder();
        RowIterator it = result.getRows();
        while (it.hasNext()) {
            if (buff.length() > 0) {
                buff.append(", ");
            }
            buff.append(it.nextRow().getValue(propertyName).getString());
        }
        return buff.toString();
    }
    
}
