/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.powerbi.implementation;

import retrofit2.Retrofit;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.management.powerbi.ErrorException;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in Workspaces.
 */
public class WorkspacesInner {
    /** The Retrofit service to perform REST calls. */
    private WorkspacesService service;
    /** The service client containing this operation class. */
    private PowerBIEmbeddedManagementClientImpl client;

    /**
     * Initializes an instance of WorkspacesInner.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public WorkspacesInner(Retrofit retrofit, PowerBIEmbeddedManagementClientImpl client) {
        this.service = retrofit.create(WorkspacesService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for Workspaces to be
     * used by Retrofit to perform actually REST calls.
     */
    interface WorkspacesService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.powerbi.Workspaces list" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.PowerBI/workspaceCollections/{workspaceCollectionName}/workspaces")
        Observable<Response<ResponseBody>> list(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("workspaceCollectionName") String workspaceCollectionName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Retrieves all existing Power BI workspaces in the specified workspace collection.
     *
     * @param resourceGroupName Azure resource group
     * @param workspaceCollectionName Power BI Embedded Workspace Collection name
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws ErrorException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the List&lt;WorkspaceInner&gt; object if successful.
     */
    public List<WorkspaceInner> list(String resourceGroupName, String workspaceCollectionName) {
        return listWithServiceResponseAsync(resourceGroupName, workspaceCollectionName).toBlocking().single().body();
    }

    /**
     * Retrieves all existing Power BI workspaces in the specified workspace collection.
     *
     * @param resourceGroupName Azure resource group
     * @param workspaceCollectionName Power BI Embedded Workspace Collection name
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<WorkspaceInner>> listAsync(String resourceGroupName, String workspaceCollectionName, final ServiceCallback<List<WorkspaceInner>> serviceCallback) {
        return ServiceFuture.fromResponse(listWithServiceResponseAsync(resourceGroupName, workspaceCollectionName), serviceCallback);
    }

    /**
     * Retrieves all existing Power BI workspaces in the specified workspace collection.
     *
     * @param resourceGroupName Azure resource group
     * @param workspaceCollectionName Power BI Embedded Workspace Collection name
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the List&lt;WorkspaceInner&gt; object
     */
    public Observable<List<WorkspaceInner>> listAsync(String resourceGroupName, String workspaceCollectionName) {
        return listWithServiceResponseAsync(resourceGroupName, workspaceCollectionName).map(new Func1<ServiceResponse<List<WorkspaceInner>>, List<WorkspaceInner>>() {
            @Override
            public List<WorkspaceInner> call(ServiceResponse<List<WorkspaceInner>> response) {
                return response.body();
            }
        });
    }

    /**
     * Retrieves all existing Power BI workspaces in the specified workspace collection.
     *
     * @param resourceGroupName Azure resource group
     * @param workspaceCollectionName Power BI Embedded Workspace Collection name
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the List&lt;WorkspaceInner&gt; object
     */
    public Observable<ServiceResponse<List<WorkspaceInner>>> listWithServiceResponseAsync(String resourceGroupName, String workspaceCollectionName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (workspaceCollectionName == null) {
            throw new IllegalArgumentException("Parameter workspaceCollectionName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.list(this.client.subscriptionId(), resourceGroupName, workspaceCollectionName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<List<WorkspaceInner>>>>() {
                @Override
                public Observable<ServiceResponse<List<WorkspaceInner>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<WorkspaceInner>> result = listDelegate(response);
                        ServiceResponse<List<WorkspaceInner>> clientResponse = new ServiceResponse<List<WorkspaceInner>>(result.body().items(), result.response());
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<WorkspaceInner>> listDelegate(Response<ResponseBody> response) throws ErrorException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<WorkspaceInner>, ErrorException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<WorkspaceInner>>() { }.getType())
                .registerError(ErrorException.class)
                .build(response);
    }

}
