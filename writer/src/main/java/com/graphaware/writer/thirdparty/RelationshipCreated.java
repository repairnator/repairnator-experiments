/*
 * Copyright (c) 2013-2017 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.writer.thirdparty;

import com.graphaware.common.representation.DetachedNode;
import com.graphaware.common.representation.DetachedRelationship;
import com.graphaware.common.representation.GraphDetachedRelationship;
import org.neo4j.graphdb.Relationship;

/**
 * {@link WriteOperation} representing a {@link Relationship} being created.
 */
public class RelationshipCreated<ID> extends CreateOrDelete<ID, DetachedRelationship<ID, ? extends DetachedNode<ID>>, Relationship> {

    /**
     * Create the operation.
     *
     * @param createdRelationship representation of the created relationship. Must not be <code>null</code>.
     */
    public RelationshipCreated(DetachedRelationship<ID, ? extends DetachedNode<ID>> createdRelationship) {
        super(createdRelationship);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationType getType() {
        return OperationType.RELATIONSHIP_CREATED;
    }
}
