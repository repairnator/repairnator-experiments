/*
 * MIT License
 *
 * Copyright 2018 Sabre GLBL Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sabre.oss.yare.engine.integration.fact;

import java.util.Collection;
import java.util.Objects;

public class OuterChainingFact {

    public final Collection<MidChainingFact> collection;
    public final MidChainingFact instance;
    public final Object objectField = null;

    public OuterChainingFact(MidChainingFact instance) {
        this(null, instance);
    }

    public OuterChainingFact(Collection<MidChainingFact> collection) {
        this(collection, null);
    }

    public OuterChainingFact(Collection<MidChainingFact> collection, MidChainingFact instance) {
        this.collection = collection;
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OuterChainingFact that = (OuterChainingFact) o;
        return Objects.equals(collection, that.collection) && Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, instance);
    }
}
