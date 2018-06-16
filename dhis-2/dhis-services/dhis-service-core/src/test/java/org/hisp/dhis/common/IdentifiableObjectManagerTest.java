package org.hisp.dhis.common;

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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.hibernate.SessionFactory;
import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.hibernate.exception.CreateAccessDeniedException;
import org.hisp.dhis.hibernate.exception.DeleteAccessDeniedException;
import org.hisp.dhis.hibernate.exception.UpdateAccessDeniedException;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.security.acl.AccessStringHelper;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserGroup;
import org.hisp.dhis.user.UserGroupAccess;
import org.hisp.dhis.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class IdentifiableObjectManagerTest
    extends DhisSpringTest
{
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;

    @Autowired
    private UserService _userService;

    @Override
    protected void setUpTest() throws Exception
    {
        this.userService = _userService;
    }

    @Test
    public void testGetObjectWithIdScheme()
    {
        DataElement dataElementA = createDataElement( 'A' );

        dataElementService.addDataElement( dataElementA );

        assertEquals( dataElementA, identifiableObjectManager.get( DataDimensionItem.DATA_DIMENSION_CLASSES, IdScheme.CODE, dataElementA.getCode() ) );
        assertEquals( dataElementA, identifiableObjectManager.get( DataDimensionItem.DATA_DIMENSION_CLASSES, IdScheme.UID, dataElementA.getUid() ) );
    }

    @Test
    public void testGetObject()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );

        dataElementService.addDataElement( dataElementA );
        int dataElementIdA = dataElementA.getId();
        dataElementService.addDataElement( dataElementB );
        int dataElementIdB = dataElementB.getId();

        DataElementGroup dataElementGroupA = createDataElementGroup( 'A' );
        DataElementGroup dataElementGroupB = createDataElementGroup( 'B' );

        dataElementService.addDataElementGroup( dataElementGroupA );
        int dataElementGroupIdA = dataElementGroupA.getId();
        dataElementService.addDataElementGroup( dataElementGroupB );
        int dataElementGroupIdB = dataElementGroupB.getId();

        assertEquals( dataElementA, identifiableObjectManager.getObject( dataElementIdA, DataElement.class.getSimpleName() ) );
        assertEquals( dataElementB, identifiableObjectManager.getObject( dataElementIdB, DataElement.class.getSimpleName() ) );

        assertEquals( dataElementGroupA, identifiableObjectManager.getObject( dataElementGroupIdA, DataElementGroup.class.getSimpleName() ) );
        assertEquals( dataElementGroupB, identifiableObjectManager.getObject( dataElementGroupIdB, DataElementGroup.class.getSimpleName() ) );
    }

    @Test
    public void testGetWithClasses()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );

        Set<Class<? extends IdentifiableObject>> classes = ImmutableSet.<Class<? extends IdentifiableObject>>builder().
            add( Indicator.class ).add( DataElement.class ).add( DataElementOperand.class ).build();

        assertEquals( dataElementA, identifiableObjectManager.get( classes, dataElementA.getUid() ) );
        assertEquals( dataElementB, identifiableObjectManager.get( classes, dataElementB.getUid() ) );
    }

    @Test
    public void publicAccessSetIfNoUser()
    {
        DataElement dataElement = createDataElement( 'A' );
        identifiableObjectManager.save( dataElement );

        assertNotNull( dataElement.getPublicAccess() );
        assertFalse( AccessStringHelper.canRead( dataElement.getPublicAccess() ) );
        assertFalse( AccessStringHelper.canWrite( dataElement.getPublicAccess() ) );
    }

    @Test
    public void getCount()
    {
        identifiableObjectManager.save( createDataElement( 'A' ) );
        identifiableObjectManager.save( createDataElement( 'B' ) );
        identifiableObjectManager.save( createDataElement( 'C' ) );
        identifiableObjectManager.save( createDataElement( 'D' ) );

        assertEquals( 4, identifiableObjectManager.getCount( DataElement.class ) );
    }

    @Test
    public void getEqualToName()
    {
        DataElement dataElement = createDataElement( 'A' );
        identifiableObjectManager.save( dataElement );

        assertNotNull( identifiableObjectManager.getByName( DataElement.class, "DataElementA" ) );
        assertNull( identifiableObjectManager.getByName( DataElement.class, "DataElementB" ) );
        assertEquals( dataElement, identifiableObjectManager.getByName( DataElement.class, "DataElementA" ) );
    }

    @Test
    public void getAllOrderedName()
    {
        identifiableObjectManager.save( createDataElement( 'D' ) );
        identifiableObjectManager.save( createDataElement( 'B' ) );
        identifiableObjectManager.save( createDataElement( 'C' ) );
        identifiableObjectManager.save( createDataElement( 'A' ) );

        List<DataElement> dataElements = new ArrayList<>( identifiableObjectManager.getAllSorted( DataElement.class ) );

        assertEquals( 4, dataElements.size() );
        assertEquals( "DataElementA", dataElements.get( 0 ).getName() );
        assertEquals( "DataElementB", dataElements.get( 1 ).getName() );
        assertEquals( "DataElementC", dataElements.get( 2 ).getName() );
        assertEquals( "DataElementD", dataElements.get( 3 ).getName() );
    }

    @Test
    public void userIsCurrentIfNoUserSet()
    {
        User user = createUserAndInjectSecurityContext( true );

        DataElement dataElement = createDataElement( 'A' );
        identifiableObjectManager.save( dataElement );

        assertNotNull( dataElement.getUser() );
        assertEquals( user, dataElement.getUser() );
    }

    @Test
    public void userCanCreatePublic()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PUBLIC_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        identifiableObjectManager.save( dataElement );

        assertNotNull( dataElement.getPublicAccess() );
        assertTrue( AccessStringHelper.canRead( dataElement.getPublicAccess() ) );
        assertTrue( AccessStringHelper.canWrite( dataElement.getPublicAccess() ) );
    }

    @Test
    public void userCanCreatePrivate()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PRIVATE_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        identifiableObjectManager.save( dataElement );

        assertNotNull( dataElement.getPublicAccess() );
        assertFalse( AccessStringHelper.canRead( dataElement.getPublicAccess() ) );
        assertFalse( AccessStringHelper.canWrite( dataElement.getPublicAccess() ) );
    }

    @Test( expected = CreateAccessDeniedException.class )
    public void userDeniedCreateObject()
    {
        createUserAndInjectSecurityContext( false );
        identifiableObjectManager.save( createDataElement( 'A' ) );
    }

    @Test
    public void publicUserModifiedPublicAccessDEFAULT()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PUBLIC_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        dataElement.setPublicAccess( AccessStringHelper.DEFAULT );

        identifiableObjectManager.save( dataElement, false );

        assertFalse( AccessStringHelper.canRead( dataElement.getPublicAccess() ) );
        assertFalse( AccessStringHelper.canWrite( dataElement.getPublicAccess() ) );
    }

    @Test
    public void publicUserModifiedPublicAccessRW()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PUBLIC_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        dataElement.setPublicAccess( AccessStringHelper.READ_WRITE );

        identifiableObjectManager.save( dataElement, false );
    }

    @Test( expected = CreateAccessDeniedException.class )
    public void privateUserModifiedPublicAccessRW()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PRIVATE_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        dataElement.setPublicAccess( AccessStringHelper.READ_WRITE );

        identifiableObjectManager.save( dataElement, false );
    }

    @Test
    public void privateUserModifiedPublicAccessDEFAULT()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PRIVATE_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        dataElement.setPublicAccess( AccessStringHelper.DEFAULT );

        identifiableObjectManager.save( dataElement, false );
    }

    @Test( expected = UpdateAccessDeniedException.class )
    public void updateForPrivateUserDeniedAfterChangePublicAccessRW()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PRIVATE_ADD" );

        DataElement dataElement = createDataElement( 'A' );
        dataElement.setPublicAccess( AccessStringHelper.DEFAULT );

        identifiableObjectManager.save( dataElement, false );

        dataElement.setPublicAccess( AccessStringHelper.READ_WRITE );

        identifiableObjectManager.update( dataElement );
    }

    @Test( expected = CreateAccessDeniedException.class )
    public void userDeniedForPublicAdd()
    {
        createUserAndInjectSecurityContext( false );

        DataElement dataElement = createDataElement( 'A' );
        dataElement.setPublicAccess( AccessStringHelper.READ_WRITE );

        identifiableObjectManager.save( dataElement, false );
    }

    @Test( expected = DeleteAccessDeniedException.class )
    public void userDeniedDeleteObject()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD" );

        User user = createUser( 'B' );
        identifiableObjectManager.save( user );

        DataElement dataElement = createDataElement( 'A' );
        identifiableObjectManager.save( dataElement );

        dataElement.setUser( user );
        dataElement.setPublicAccess( AccessStringHelper.DEFAULT );
        sessionFactory.getCurrentSession().update( dataElement );

        identifiableObjectManager.delete( dataElement );
    }

    @Test
    public void objectsWithNoUser()
    {
        identifiableObjectManager.save( createDataElement( 'A' ) );
        identifiableObjectManager.save( createDataElement( 'B' ) );
        identifiableObjectManager.save( createDataElement( 'C' ) );
        identifiableObjectManager.save( createDataElement( 'D' ) );

        assertEquals( 4, identifiableObjectManager.getCount( DataElement.class ) );
        assertEquals( 4, identifiableObjectManager.getAll( DataElement.class ).size() );
    }

    @Test
    public void readPrivateObjects()
    {
        createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD" );

        User user = createUser( 'B' );
        identifiableObjectManager.save( user );

        identifiableObjectManager.save( createDataElement( 'A' ) );
        identifiableObjectManager.save( createDataElement( 'B' ) );
        identifiableObjectManager.save( createDataElement( 'C' ) );
        identifiableObjectManager.save( createDataElement( 'D' ) );

        assertEquals( 4, identifiableObjectManager.getAll( DataElement.class ).size() );

        List<DataElement> dataElements = new ArrayList<>( identifiableObjectManager.getAll( DataElement.class ) );

        for ( DataElement dataElement : dataElements )
        {
            dataElement.setUser( user );
            dataElement.setPublicAccess( AccessStringHelper.DEFAULT );

            sessionFactory.getCurrentSession().update( dataElement );
        }

        assertEquals( 0, identifiableObjectManager.getCount( DataElement.class ) );
        assertEquals( 0, identifiableObjectManager.getAll( DataElement.class ).size() );
    }

    @Test
    public void readUserGroupSharedObjects()
    {
        User loginUser = createUserAndInjectSecurityContext( false, "F_DATAELEMENT_PUBLIC_ADD", "F_USER_ADD", "F_USERGROUP_PUBLIC_ADD" );

        User user = createUser( 'B' );
        identifiableObjectManager.save( user );

        UserGroup userGroup = createUserGroup( 'A', Sets.newHashSet( loginUser ) );
        identifiableObjectManager.save( userGroup );

        identifiableObjectManager.save( createDataElement( 'A' ) );
        identifiableObjectManager.save( createDataElement( 'B' ) );
        identifiableObjectManager.save( createDataElement( 'C' ) );
        identifiableObjectManager.save( createDataElement( 'D' ) );

        assertEquals( 4, identifiableObjectManager.getCount( DataElement.class ) );
        assertEquals( 4, identifiableObjectManager.getAll( DataElement.class ).size() );

        List<DataElement> dataElements = new ArrayList<>( identifiableObjectManager.getAll( DataElement.class ) );

        for ( DataElement dataElement : dataElements )
        {
            dataElement.setUser( user );
            dataElement.setPublicAccess( AccessStringHelper.newInstance().build() );

            UserGroupAccess userGroupAccess = new UserGroupAccess();
            userGroupAccess.setAccess( AccessStringHelper.READ );
            userGroupAccess.setUserGroup( userGroup );

            sessionFactory.getCurrentSession().save( userGroupAccess );

            dataElement.getUserGroupAccesses().add( userGroupAccess );
            sessionFactory.getCurrentSession().update( dataElement );
        }

        assertEquals( 4, identifiableObjectManager.getCount( DataElement.class ) );
        assertEquals( 4, identifiableObjectManager.getAll( DataElement.class ).size() );
    }

    @Test
    public void getByUidTest()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        identifiableObjectManager.save( dataElementA );
        identifiableObjectManager.save( dataElementB );
        identifiableObjectManager.save( dataElementC );
        identifiableObjectManager.save( dataElementD );

        List<DataElement> ab = identifiableObjectManager.getByUid( DataElement.class, Arrays.asList( dataElementA.getUid(), dataElementB.getUid() ) );
        List<DataElement> cd = identifiableObjectManager.getByUid( DataElement.class, Arrays.asList( dataElementC.getUid(), dataElementD.getUid() ) );

        assertTrue( ab.contains( dataElementA ) );
        assertTrue( ab.contains( dataElementB ) );
        assertFalse( ab.contains( dataElementC ) );
        assertFalse( ab.contains( dataElementD ) );

        assertFalse( cd.contains( dataElementA ) );
        assertFalse( cd.contains( dataElementB ) );
        assertTrue( cd.contains( dataElementC ) );
        assertTrue( cd.contains( dataElementD ) );
    }

    @Test
    public void getByUidOrderedTest()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        identifiableObjectManager.save( dataElementA );
        identifiableObjectManager.save( dataElementB );
        identifiableObjectManager.save( dataElementC );
        identifiableObjectManager.save( dataElementD );

        List<String> uids = Arrays.asList( dataElementA.getUid(), dataElementC.getUid(), dataElementB.getUid(), dataElementD.getUid() );

        List<DataElement> expected = new ArrayList<>( Arrays.asList( dataElementA, dataElementC, dataElementB, dataElementD ) );

        List<DataElement> actual = new ArrayList<>( identifiableObjectManager.getByUidOrdered( DataElement.class, uids ) );

        assertEquals( expected, actual );
    }

    @Test
    public void getByCodeTest()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        dataElementA.setCode( "DE_A" );
        dataElementB.setCode( "DE_B" );
        dataElementC.setCode( "DE_C" );
        dataElementD.setCode( "DE_D" );

        identifiableObjectManager.save( dataElementA );
        identifiableObjectManager.save( dataElementB );
        identifiableObjectManager.save( dataElementC );
        identifiableObjectManager.save( dataElementD );

        List<DataElement> ab = identifiableObjectManager.getByCode( DataElement.class, Arrays.asList( dataElementA.getCode(), dataElementB.getCode() ) );
        List<DataElement> cd = identifiableObjectManager.getByCode( DataElement.class, Arrays.asList( dataElementC.getCode(), dataElementD.getCode() ) );

        assertTrue( ab.contains( dataElementA ) );
        assertTrue( ab.contains( dataElementB ) );
        assertFalse( ab.contains( dataElementC ) );
        assertFalse( ab.contains( dataElementD ) );

        assertFalse( cd.contains( dataElementA ) );
        assertFalse( cd.contains( dataElementB ) );
        assertTrue( cd.contains( dataElementC ) );
        assertTrue( cd.contains( dataElementD ) );
    }

    @Test
    public void testGetObjects()
    {
        OrganisationUnit unit1 = createOrganisationUnit( 'A' );
        OrganisationUnit unit2 = createOrganisationUnit( 'B' );
        OrganisationUnit unit3 = createOrganisationUnit( 'C' );

        identifiableObjectManager.save( unit1 );
        identifiableObjectManager.save( unit2 );
        identifiableObjectManager.save( unit3 );

        Set<String> codes = Sets.newHashSet( unit2.getCode(), unit3.getCode() );

        List<OrganisationUnit> units = identifiableObjectManager.getObjects( OrganisationUnit.class, IdentifiableProperty.CODE, codes );

        assertEquals( 2, units.size() );
        assertTrue( units.contains( unit2 ) );
        assertTrue( units.contains( unit3 ) );
    }

    @Test
    public void testGetIdMapIdScheme()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );

        dataElementService.addDataElement( dataElementA );
        dataElementService.addDataElement( dataElementB );

        Map<String, DataElement> map = identifiableObjectManager.getIdMap( DataElement.class, IdScheme.CODE );

        assertEquals( dataElementA, map.get( "DataElementCodeA" ) );
        assertEquals( dataElementB, map.get( "DataElementCodeB" ) );
        assertNull( map.get( "DataElementCodeX" ) );
    }
}
