/*
 *
 * MariaDB Client for Java
 *
 * Copyright (c) 2012-2014 Monty Program Ab.
 * Copyright (c) 2015-2017 MariaDB Ab.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to Monty Program Ab info@montyprogram.com.
 *
 * This particular MariaDB Client for Java file is work
 * derived from a Drizzle-JDBC. Drizzle-JDBC file which is covered by subject to
 * the following copyright and notice provisions:
 *
 * Copyright (c) 2009-2011, Marcus Eriksson
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of the driver nor the names of its contributors may not be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS  AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */

package org.mariadb.jdbc;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class GeneratedTest extends BaseTest {

    /**
     * Tables initialisation.
     *
     * @throws SQLException exception
     */
    @BeforeClass()
    public static void initClass() throws SQLException {
        createTable("genkeys", "priKey INT NOT NULL AUTO_INCREMENT, dataField VARCHAR(64), PRIMARY KEY (priKey)");
    }

    /*
     Test with different APIs that generated keys work. Also test that any name in generatedKeys.getXXX(String name)
     can be passed and is equivalent to generatedKeys.getXXX(1). This might not be 100% compliant, but is a simple
     and effective solution for MySQL that does not does not support more than a single autogenerated value.
    */
    @Test
    public void generatedKeys() throws Exception {
        Statement st = sharedConnection.createStatement();
        st.executeUpdate("insert into genkeys(dataField) values('a')", Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();
        assertTrue(rs.next());
        assertEquals(rs.getInt(1), 1);
        assertEquals(rs.getInt("priKey"), 1);
        assertEquals(rs.getInt("foo"), 1);
        int[] indexes = {1, 2, 3};
        st.executeUpdate("insert into genkeys(dataField) values('b')", indexes);
        rs = st.getGeneratedKeys();
        assertTrue(rs.next());
        assertEquals(rs.getInt(1), 2);
        try {
            assertEquals(rs.getInt(2), 2);
            fail("should never get here");
        } catch (SQLException e) {
            // eat
        }

        String[] columnNames = {"priKey", "Alice", "Bob"};
        st.executeUpdate("insert into genkeys(dataField) values('c')", columnNames);
        rs = st.getGeneratedKeys();
        assertTrue(rs.next());
        for (int i = 0; i < 3; i++) {
            assertEquals(rs.getInt(columnNames[i]), 3);
        }
    }

}
