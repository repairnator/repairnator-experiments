/**
 * Copyright (C) 2006-2017 INRIA and contributors
 * Spoon - http://spoon.gforge.inria.fr/
 *
 * This software is governed by the CeCILL-C License under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-C license as
 * circulated by CEA, CNRS and INRIA at http://www.cecill.info.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the CeCILL-C License for more details.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 */
package spoon.test.metamodel;

/**
 * Represents type of Spoon model class
 */
public enum MMTypeKind {
	/**
	 * Kind of type which represents leaf of Spoon model.
	 * Examples: CtClass, CtField, CtThrow
	 */
	LEAF,
	/**
	 * Kind of type which represents some abstract concept of Spoon model
	 * Examples: CtExecutable, CtReference, CtBodyHolder, ...
	 */
	ABSTRACT;
}
