/*
 * Copyright (c) 2010-2016. Axon Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.common.transaction;

/**
 * Interface of an object that represents a started transaction that can be committed or rolled back.
 *
 * @author Rene de Waele
 */
public interface Transaction {

    /**
     * Commit this transaction.
     */
    void commit();

    /**
     * Roll back this transaction.
     */
    void rollback();

}
