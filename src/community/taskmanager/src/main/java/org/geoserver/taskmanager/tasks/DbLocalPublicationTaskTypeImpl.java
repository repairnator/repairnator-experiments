package org.geoserver.taskmanager.tasks;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.CatalogFactoryImpl;
import org.geoserver.taskmanager.external.DbSource;
import org.geoserver.taskmanager.external.DbTable;
import org.geoserver.taskmanager.external.ExtTypes;
import org.geoserver.taskmanager.schedule.ParameterInfo;
import org.geoserver.taskmanager.schedule.TaskContext;
import org.geoserver.taskmanager.schedule.TaskException;
import org.geoserver.taskmanager.schedule.TaskResult;
import org.geoserver.taskmanager.schedule.TaskType;
import org.geoserver.taskmanager.util.SqlUtil;
import org.geotools.feature.NameImpl;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.opengis.feature.type.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbLocalPublicationTaskTypeImpl implements TaskType {
    
    public static final String NAME = "LocalDbPublication";
    
    public static final String PARAM_LAYER = "layer";
    
    public static final String PARAM_DB_NAME = "database";

    public static final String PARAM_TABLE_NAME = "table-name";
    
    protected final Map<String, ParameterInfo> paramInfo = new LinkedHashMap<String, ParameterInfo>();

    @Autowired
    protected ExtTypes extTypes;
    
    @Autowired
    protected Catalog catalog;

    @Override
    public String getName() {
        return NAME;
    }

    @PostConstruct
    public void initParamInfo() {
        ParameterInfo dbInfo = new ParameterInfo(PARAM_DB_NAME, extTypes.dbName, true);
        paramInfo.put(PARAM_DB_NAME, dbInfo);
        paramInfo.put(PARAM_TABLE_NAME, new ParameterInfo(PARAM_TABLE_NAME, extTypes.tableName(), false)
                .dependsOn(dbInfo));
        paramInfo.put(PARAM_LAYER, new ParameterInfo(PARAM_LAYER, extTypes.layerName, true));
    }

    @Override
    public Map<String, ParameterInfo> getParameterInfo() {
        return paramInfo;
    }

    @Override
    public TaskResult run(TaskContext ctx) throws TaskException {
        CatalogFactory catalogFac = new CatalogFactoryImpl(catalog);
        
        final DbSource db = (DbSource) ctx.getParameterValues().get(PARAM_DB_NAME);
        final DbTable table = (DbTable) ctx.getParameterValues().get(PARAM_TABLE_NAME);
        final Name layerName = (Name) ctx.getParameterValues().get(PARAM_LAYER);
        
        final NamespaceInfo ns = catalog.getNamespaceByURI(layerName.getNamespaceURI());
        final WorkspaceInfo ws = catalog.getWorkspaceByName(ns.getName());
        
        final boolean createLayer = catalog.getLayerByName(layerName) == null;
        final boolean createStore;
        final boolean createResource;
        
        final LayerInfo layer;
        final DataStoreInfo store;
        final FeatureTypeInfo resource;
                
        if (createLayer) {
            String schema = SqlUtil.schema(table.getTableName());
            String dbName = schema == null ? db.getName() : (db.getName() + "_" + schema);
            final DataStoreInfo _store = catalog.getStoreByName(ws, dbName, DataStoreInfo.class);
            final FeatureTypeInfo _resource = catalog.getResourceByName(layerName, FeatureTypeInfo.class);
            createStore = _store == null;
            createResource = _resource == null;
            
            if (createStore) {
                store = catalogFac.createDataStore();
                store.setWorkspace(ws);
                store.setName(dbName);
                store.getConnectionParameters().put(JDBCDataStoreFactory.NAMESPACE.getName(), ns.getURI());
                store.getConnectionParameters().putAll(db.getParameters());   
                if (schema != null) {
                    store.getConnectionParameters().put(JDBCDataStoreFactory.SCHEMA.getName(), schema);
                }
                store.setEnabled(true);
                catalog.add(store);
            } else {
                store = _store;
            }
            
            CatalogBuilder builder = new CatalogBuilder(catalog);
            if (createResource) {
                builder.setStore(store);
                try {
                    resource = builder.buildFeatureType(new NameImpl(
                            SqlUtil.notQualified(table.getTableName())));
                } catch (Exception e) {
                    if (createStore) {
                        catalog.remove(store);
                    }
                    throw new TaskException(e);
                }
                resource.setName(layerName.getLocalPart());
                catalog.add(resource);
            } else {
                resource = _resource;
            }
            
            try {
                layer = builder.buildLayer(resource);
                layer.setAdvertised(false);
                catalog.add(layer);     
            } catch (IOException e) {
                if (createStore) {
                    catalog.remove(store);
                }
                if (createResource) {
                    catalog.remove(resource);
                }
                throw new TaskException(e);
            }
        } else {
            layer = null;
            resource = null;
            store = null;
            createStore = false;
            createResource = false;
        }
        
        return new TaskResult() {

            @Override
            public void commit() throws TaskException {
                if (createLayer) {
                    LayerInfo editLayer = catalog.getLayer(layer.getId());
                    editLayer.setAdvertised(true);
                    catalog.save(editLayer);
                }
            }

            @Override
            public void rollback() throws TaskException {
                if (createLayer) {
                    catalog.remove(layer);
                    if (createResource) {
                        catalog.remove(resource);
                    }
                    if (createStore) {
                        catalog.remove(store);
                    }
                }
            }
            
        };        
       
    }

    @Override
    public void cleanup(TaskContext ctx) throws TaskException {
        final DbSource db = (DbSource) ctx.getParameterValues().get(PARAM_DB_NAME);
        final Name layerName = (Name) ctx.getParameterValues().get(PARAM_LAYER);
        final String workspace = catalog.getNamespaceByURI(layerName.getNamespaceURI()).getPrefix();
        
        final LayerInfo layer = catalog.getLayerByName(layerName);               
        final DataStoreInfo store = catalog.getStoreByName(workspace, db.getName(), DataStoreInfo.class);
        final FeatureTypeInfo resource = catalog.getResourceByName(layerName, FeatureTypeInfo.class);
        
        catalog.remove(layer);
        catalog.remove(resource);
        catalog.remove(store);
    }

}
