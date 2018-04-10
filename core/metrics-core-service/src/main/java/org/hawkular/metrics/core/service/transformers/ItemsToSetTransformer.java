/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.core.service.transformers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

/**
 * RxJava Composer, transforms emitted items to a Set of items
 *
 * @author Michael Burman
 */
public class ItemsToSetTransformer<T> implements Observable.Transformer<T, Set<T>> {

    @Override
    public Observable<Set<T>> call(Observable<T> metricIndexObservable) {
        return metricIndexObservable
                .toList()
                .switchIfEmpty(Observable.from(new HashSet<>()))
                .map((Func1<List<T>, HashSet<T>>) HashSet<T>::new);
    }
}
