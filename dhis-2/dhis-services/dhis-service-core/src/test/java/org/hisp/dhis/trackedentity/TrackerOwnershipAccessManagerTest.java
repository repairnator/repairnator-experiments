package org.hisp.dhis.trackedentity;

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

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 * @author Ameen Mohamed <ameen@dhis2.org>
 */
public class TrackerOwnershipAccessManagerTest extends DhisSpringTest
{
    @Autowired
    private TrackerOwnershipAccessManager trackerOwnershipAccessManager;

    @Autowired
    private UserService _userService;

    @Autowired
    private TrackedEntityInstanceService entityInstanceService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private ProgramService programService;

    private TrackedEntityInstance entityInstanceA1;

    private TrackedEntityInstance entityInstanceB1;

    private OrganisationUnit organisationUnitA;

    private OrganisationUnit organisationUnitB;

    private Program programA;

    private User userA;

    private User userB;

    @Override
    protected void setUpTest()
        throws Exception
    {
        userService = _userService;
        organisationUnitA = createOrganisationUnit( 'A' );
        organisationUnitService.addOrganisationUnit( organisationUnitA );

        organisationUnitB = createOrganisationUnit( 'B' );
        organisationUnitService.addOrganisationUnit( organisationUnitB );

        entityInstanceA1 = createTrackedEntityInstance( 'A', organisationUnitA );
        entityInstanceB1 = createTrackedEntityInstance( 'B', organisationUnitB );
        entityInstanceService.addTrackedEntityInstance( entityInstanceA1 );
        entityInstanceService.addTrackedEntityInstance( entityInstanceB1 );

        programA = createProgram( 'A' );
        programService.addProgram( programA );

        userA = createUser( "userA" );
        userB = createUser( "userB" );
        userA.addOrganisationUnit( organisationUnitA );
        userB.addOrganisationUnit( organisationUnitB );
        userService.addUser( userA );
        userService.addUser( userB );
    }

    @Test
    public void testAssignOwnership()
    {
        assertTrue(trackerOwnershipAccessManager.isOwner( userA, entityInstanceA1, programA ));
        assertFalse(trackerOwnershipAccessManager.isOwner( userB, entityInstanceA1, programA ));
        assertTrue(trackerOwnershipAccessManager.isOwner( userB, entityInstanceB1, programA ));
        trackerOwnershipAccessManager.assignOwnership( entityInstanceA1, programA, organisationUnitB, false, true );
        assertFalse(trackerOwnershipAccessManager.isOwner( userA, entityInstanceA1, programA ));
        assertFalse(trackerOwnershipAccessManager.hasTemporaryAccess( entityInstanceA1, programA,userA ));
        assertTrue(trackerOwnershipAccessManager.hasAccess( userB,entityInstanceA1, programA));
        assertFalse(trackerOwnershipAccessManager.hasTemporaryAccess( entityInstanceA1, programA,userB ));
    }
    
    @Test
    public void testGrantTemporaryOwnershipWithAudit()
    {
        assertTrue(trackerOwnershipAccessManager.hasAccess( userA, entityInstanceA1, programA ));
        assertFalse(trackerOwnershipAccessManager.hasAccess( userB, entityInstanceA1, programA ));
        trackerOwnershipAccessManager.grantTemporaryOwnership( entityInstanceA1, programA, userB, "testing reason" );
        assertTrue(trackerOwnershipAccessManager.isOwner( userA, entityInstanceA1, programA ));
        assertTrue(trackerOwnershipAccessManager.hasTemporaryAccess( entityInstanceA1, programA,userB ));
        assertTrue(trackerOwnershipAccessManager.hasAccess( userA,entityInstanceA1, programA));
        assertTrue(trackerOwnershipAccessManager.hasAccess( userB, entityInstanceA1, programA ));
    }

    @Test
    public void testTransferOwnership()
    {
        trackerOwnershipAccessManager.assignOwnership( entityInstanceA1, programA, organisationUnitA, false, true );
        assertTrue(trackerOwnershipAccessManager.isOwner( userA, entityInstanceA1, programA ));
        assertFalse(trackerOwnershipAccessManager.isOwner( userB,entityInstanceA1, programA ));
        
        trackerOwnershipAccessManager.transferOwnership( entityInstanceA1, programA, organisationUnitB, false, true );
        assertFalse(trackerOwnershipAccessManager.isOwner( userA, entityInstanceA1, programA ));
        assertTrue(trackerOwnershipAccessManager.isOwner( userB,entityInstanceA1, programA ));
        
    }
}
