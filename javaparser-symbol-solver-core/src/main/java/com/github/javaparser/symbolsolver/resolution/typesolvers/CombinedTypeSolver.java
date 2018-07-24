/*
 * Copyright 2016 Federico Tomassetti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for type solvers. All solving is done by the contained type solvers.
 * This helps you when an API asks for a single type solver, but you need several.
 *
 * @author Federico Tomassetti
 */
public class CombinedTypeSolver implements TypeSolver {

    private TypeSolver parent;
    private List<TypeSolver> elements = new ArrayList<>();

    public CombinedTypeSolver(TypeSolver... elements) {
        for (TypeSolver el : elements) {
            add(el);
        }
    }

    @Override
    public TypeSolver getParent() {
        return parent;
    }

    @Override
    public void setParent(TypeSolver parent) {
        this.parent = parent;
    }

    public void add(TypeSolver typeSolver) {
        this.elements.add(typeSolver);
        typeSolver.setParent(this);
    }

    @Override
    public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
        for (TypeSolver ts : elements) {
            SymbolReference<ResolvedReferenceTypeDeclaration> res = ts.tryToSolveType(name);
            if (res.isSolved()) {
                return res;
            }
        }
        return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
    }

    @Override
    public ResolvedReferenceTypeDeclaration solveType(String name) throws UnsolvedSymbolException {
        SymbolReference<ResolvedReferenceTypeDeclaration> res = tryToSolveType(name);
        if (res.isSolved()) {
            return res.getCorrespondingDeclaration();
        } else {
            throw new UnsolvedSymbolException(name);
        }
    }
}
