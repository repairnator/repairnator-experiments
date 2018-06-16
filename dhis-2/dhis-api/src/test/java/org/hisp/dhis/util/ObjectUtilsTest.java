package org.hisp.dhis.util;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.common.collect.Lists;
import org.hisp.dhis.common.DataDimensionType;
import org.hisp.dhis.category.Category;
import org.hisp.dhis.category.CategoryCombo;
import org.hisp.dhis.dataelement.DataElement;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Lars Helge Overland
 */
public class ObjectUtilsTest
{
    @Test
    public void testJoin()
    {
        DataElement deA = new DataElement( "DataElementA" );
        DataElement deB = new DataElement( "DataElementB" );
        DataElement deC = new DataElement( "DataElementC" );
        
        List<DataElement> elements = Lists.newArrayList( deA, deB, deC );
        
        String actual = ObjectUtils.join( elements, ", ", de -> de.getName() );
                
        assertEquals( "DataElementA, DataElementB, DataElementC", actual );
        assertEquals( null, ObjectUtils.join( null, ", ", null ) );
    }
    
    @Test
    public void testGetAll()
    {
        Category ctA = new Category( "CategoryA", DataDimensionType.DISAGGREGATION );
        Category ctB = new Category( "CategoryB", DataDimensionType.DISAGGREGATION );
        Category ctC = new Category( "CategoryC", DataDimensionType.DISAGGREGATION );
        Category ctD = new Category( "CategoryD", DataDimensionType.DISAGGREGATION );

        CategoryCombo ccA = new CategoryCombo( "CategoryComboA", DataDimensionType.DISAGGREGATION );
        CategoryCombo ccB = new CategoryCombo( "CategoryComboB", DataDimensionType.DISAGGREGATION );
        
        ccA.addCategory( ctA );
        ccA.addCategory( ctB );
        ccB.addCategory( ctC );
        ccB.addCategory( ctD );
        
        List<CategoryCombo> ccs = Lists.newArrayList( ccA, ccB );
        
        Set<Category> cts = ObjectUtils.getAll( ccs, cc -> cc.getCategories() );
        
        assertEquals( 4, cts.size() );
        assertTrue( cts.contains( ctA ) );
        assertTrue( cts.contains( ctB ) );
        assertTrue( cts.contains( ctC ) );
        assertTrue( cts.contains( ctD ) );        
    }
}
