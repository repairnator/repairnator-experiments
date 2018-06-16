package org.hisp.dhis.dxf2.metadata.objectbundle.hooks;

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

import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.dxf2.metadata.objectbundle.ObjectBundle;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.predictor.Predictor;

/**
 * @author Ken Haase
 */
public class PredictorObjectBundleHook
    extends AbstractObjectBundleHook
{
    @Override
    public void preCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !Predictor.class.isInstance( object ) )
        {
            return;
        }

        Predictor predictor = (Predictor) object;
        Expression skipTest = predictor.getSampleSkipTest();

        preheatService.connectReferences( predictor.getGenerator(), bundle.getPreheat(), bundle.getPreheatIdentifier() );

        if ( skipTest != null )
        {
            preheatService.connectReferences( skipTest, bundle.getPreheat(), bundle.getPreheatIdentifier() );
        }

        sessionFactory.getCurrentSession().save( predictor.getGenerator() );

        if ( skipTest != null )
        {
            sessionFactory.getCurrentSession().save( skipTest );
        }
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !Predictor.class.isInstance( object ) )
        {
            return;
        }

        Predictor predictor = (Predictor) object;
        Expression skipTest = predictor.getSampleSkipTest();

        preheatService.connectReferences( predictor.getGenerator(), bundle.getPreheat(),
            bundle.getPreheatIdentifier() );

        if ( skipTest != null )
        {
            preheatService.connectReferences( skipTest, bundle.getPreheat(), bundle.getPreheatIdentifier() );
        }

        sessionFactory.getCurrentSession().save( predictor.getGenerator() );

        if ( skipTest != null )
        {
            sessionFactory.getCurrentSession().save( skipTest );
        }
    }
}
