/* (c) 2014 - 2016 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.emf.ecore.EObject;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.PublishedInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.ows.Request;
import org.geoserver.ows.WorkspaceQualifyingCallback;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.wfs.request.DescribeFeatureTypeRequest;
import org.geoserver.wfs.request.GetCapabilitiesRequest;
import org.geoserver.wfs.request.GetFeatureRequest;
import org.geoserver.wfs.request.Insert;
import org.geoserver.wfs.request.Lock;
import org.geoserver.wfs.request.LockFeatureRequest;
import org.geoserver.wfs.request.Query;
import org.geoserver.wfs.request.Replace;
import org.geoserver.wfs.request.TransactionElement;
import org.geoserver.wfs.request.TransactionRequest;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;

public class WFSWorkspaceQualifier extends WorkspaceQualifyingCallback {

    public WFSWorkspaceQualifier(Catalog catalog) {
        super(catalog);
    }

    @Override
    protected void qualifyRequest(
            WorkspaceInfo workspace, PublishedInfo layer, Service service, Request request) {
        if (request.getContext() != null) {
            // if a qualifying workspace exist, try to qualify the request typename
            // parameter, if present
            if (workspace != null && request.getKvp().containsKey("TYPENAME")) {
                Iterable typeNames = (Iterable) request.getKvp().get("TYPENAME");
                NamespaceInfo ns = catalog.getNamespaceByPrefix(workspace.getName());
                if (ns != null) {
                    List<QName> qualifiedNames = new ArrayList<QName>();
                    for (Object name : typeNames) {
                        if (name != null && name instanceof QName) {
                            QName typeName = (QName) name;
                            // no namespace specified, we can qualify
                            if (typeName.getNamespaceURI() == null
                                    || typeName.getNamespaceURI().equals("")) {
                                typeName = new QName(ns.getURI(), typeName.getLocalPart());
                            } else if (typeName.getNamespaceURI()
                                            .equals(catalog.getDefaultNamespace().getURI())
                                    || !typeName.getNamespaceURI().equals(ns.getURI())) {
                                // more complex case, if we have the default
                                // namespace, we have to check if it's been
                                // specified on the request, or assigned by parser
                                typeName = checkOriginallyUnqualified(request, ns, typeName);
                            }
                            qualifiedNames.add(typeName);
                        }
                    }
                    request.getKvp().put("TYPENAME", qualifiedNames);
                }
            }
        }
    }

    /**
     * Checks if the typeName default namespace is present in the original request, or it has been
     * overridden by parser. If it's been overridden we can qualify with the given namespace.
     *
     * @param request
     * @param ns
     * @param typeName
     */
    private QName checkOriginallyUnqualified(Request request, NamespaceInfo ns, QName typeName) {
        Map<String, String[]> originalParams = request.getHttpRequest().getParameterMap();
        for (String paramName : originalParams.keySet()) {
            if (paramName.equalsIgnoreCase("TYPENAME")) {
                for (String originalTypeName : originalParams.get(paramName)) {
                    if (originalTypeName.equals(typeName.getLocalPart())) {
                        // the original typeName was not
                        // qualified, we can qualify it
                        typeName = new QName(ns.getURI(), typeName.getLocalPart());
                    }
                }
            }
        }
        return typeName;
    }

    @Override
    protected void qualifyRequest(
            WorkspaceInfo workspace, PublishedInfo layer, Operation operation, Request request) {
        NamespaceInfo ns = catalog.getNamespaceByPrefix(workspace.getName());

        GetCapabilitiesRequest caps =
                GetCapabilitiesRequest.adapt(
                        OwsUtils.parameter(operation.getParameters(), EObject.class));
        if (caps != null) {
            caps.setNamespace(workspace.getName());
            return;
        }

        DescribeFeatureTypeRequest dft =
                DescribeFeatureTypeRequest.adapt(
                        OwsUtils.parameter(operation.getParameters(), EObject.class));
        if (dft != null) {
            qualifyTypeNames(dft.getTypeNames(), workspace, ns);
            return;
        }

        GetFeatureRequest gf =
                GetFeatureRequest.adapt(
                        OwsUtils.parameter(operation.getParameters(), EObject.class));
        if (gf != null) {
            for (Query q : gf.getQueries()) {
                // in case of stored query usage the typenames might be null
                if (q.getTypeNames() != null) {
                    qualifyTypeNames(q.getTypeNames(), workspace, ns);
                }
            }
            return;
        }

        LockFeatureRequest lf =
                LockFeatureRequest.adapt(
                        OwsUtils.parameter(operation.getParameters(), EObject.class));
        if (lf != null) {
            for (Lock lock : lf.getLocks()) {
                lock.setTypeName(qualifyTypeName(lock.getTypeName(), workspace, ns));
            }
            return;
        }

        TransactionRequest t =
                TransactionRequest.adapt(
                        OwsUtils.parameter(operation.getParameters(), EObject.class));
        if (t != null) {
            for (TransactionElement el : t.getElements()) {
                if (el instanceof Insert) {
                    Insert in = (Insert) el;
                    // in the insert case the objects are gt feature types which are not mutable
                    // so we just check them and throw an exception if a name does not match
                    List features = in.getFeatures();
                    ensureFeatureNamespaceUriMatches(features, ns, t);
                } else if (el instanceof Replace) {
                    Replace rep = (Replace) el;
                    // in the replace case the objects are gt feature types which are not mutable
                    // so we just check them and throw an exception if a name does not match
                    List features = rep.getFeatures();
                    ensureFeatureNamespaceUriMatches(features, ns, t);
                } else {
                    el.setTypeName(qualifyTypeName(el.getTypeName(), workspace, ns));
                }
            }
        }
    }

    /**
     * Iterates the given features and ensures their namespaceURI matches the given namespace
     *
     * @param features
     * @param ns
     * @param t
     */
    private void ensureFeatureNamespaceUriMatches(
            List features, NamespaceInfo ns, TransactionRequest t) {
        for (Iterator j = features.iterator(); j.hasNext(); ) {
            Object next = j.next();
            if (next instanceof Feature) {
                Feature f = (Feature) next;
                Name n = f.getType().getName();
                if (n.getNamespaceURI() != null && !ns.getURI().equals(n.getNamespaceURI())) {
                    throw new WFSException(t, "No such feature type " + n);
                }
            }
        }
    }

    void qualifyTypeNames(List names, WorkspaceInfo ws, NamespaceInfo ns) {
        if (names != null) {
            for (int i = 0; i < names.size(); i++) {
                QName name = (QName) names.get(i);
                names.set(i, qualifyTypeName(name, ws, ns));
            }
        }
    }

    QName qualifyTypeName(QName name, WorkspaceInfo ws, NamespaceInfo ns) {
        if (name != null) {
            return new QName(ns.getURI(), name.getLocalPart(), ws.getName());
        }
        return null;
    }
}
