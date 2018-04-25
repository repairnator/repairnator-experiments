/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.sql.implementation;

import retrofit2.Retrofit;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.CloudException;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in ServerCommunicationLinks.
 */
public class ServerCommunicationLinksInner {
    /** The Retrofit service to perform REST calls. */
    private ServerCommunicationLinksService service;
    /** The service client containing this operation class. */
    private SqlManagementClientImpl client;

    /**
     * Initializes an instance of ServerCommunicationLinksInner.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public ServerCommunicationLinksInner(Retrofit retrofit, SqlManagementClientImpl client) {
        this.service = retrofit.create(ServerCommunicationLinksService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for ServerCommunicationLinks to be
     * used by Retrofit to perform actually REST calls.
     */
    interface ServerCommunicationLinksService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.sql.ServerCommunicationLinks delete" })
        @HTTP(path = "subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Sql/servers/{serverName}/communicationLinks/{communicationLinkName}", method = "DELETE", hasBody = true)
        Observable<Response<ResponseBody>> delete(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("serverName") String serverName, @Path("communicationLinkName") String communicationLinkName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.sql.ServerCommunicationLinks get" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Sql/servers/{serverName}/communicationLinks/{communicationLinkName}")
        Observable<Response<ResponseBody>> get(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("serverName") String serverName, @Path("communicationLinkName") String communicationLinkName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.sql.ServerCommunicationLinks createOrUpdate" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Sql/servers/{serverName}/communicationLinks/{communicationLinkName}")
        Observable<Response<ResponseBody>> createOrUpdate(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("serverName") String serverName, @Path("communicationLinkName") String communicationLinkName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Body ServerCommunicationLinkInner parameters, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.sql.ServerCommunicationLinks beginCreateOrUpdate" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Sql/servers/{serverName}/communicationLinks/{communicationLinkName}")
        Observable<Response<ResponseBody>> beginCreateOrUpdate(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("serverName") String serverName, @Path("communicationLinkName") String communicationLinkName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Body ServerCommunicationLinkInner parameters, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.sql.ServerCommunicationLinks listByServer" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Sql/servers/{serverName}/communicationLinks")
        Observable<Response<ResponseBody>> listByServer(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("serverName") String serverName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Deletes a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void delete(String resourceGroupName, String serverName, String communicationLinkName) {
        deleteWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName).toBlocking().single().body();
    }

    /**
     * Deletes a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> deleteAsync(String resourceGroupName, String serverName, String communicationLinkName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(deleteWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName), serviceCallback);
    }

    /**
     * Deletes a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> deleteAsync(String resourceGroupName, String serverName, String communicationLinkName) {
        return deleteWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Deletes a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String serverName, String communicationLinkName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serverName == null) {
            throw new IllegalArgumentException("Parameter serverName is required and cannot be null.");
        }
        if (communicationLinkName == null) {
            throw new IllegalArgumentException("Parameter communicationLinkName is required and cannot be null.");
        }
        final String apiVersion = "2014-04-01";
        return service.delete(this.client.subscriptionId(), resourceGroupName, serverName, communicationLinkName, apiVersion, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = deleteDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Void> deleteDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Void, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Returns a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the ServerCommunicationLinkInner object if successful.
     */
    public ServerCommunicationLinkInner get(String resourceGroupName, String serverName, String communicationLinkName) {
        return getWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName).toBlocking().single().body();
    }

    /**
     * Returns a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<ServerCommunicationLinkInner> getAsync(String resourceGroupName, String serverName, String communicationLinkName, final ServiceCallback<ServerCommunicationLinkInner> serviceCallback) {
        return ServiceFuture.fromResponse(getWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName), serviceCallback);
    }

    /**
     * Returns a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the ServerCommunicationLinkInner object
     */
    public Observable<ServerCommunicationLinkInner> getAsync(String resourceGroupName, String serverName, String communicationLinkName) {
        return getWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName).map(new Func1<ServiceResponse<ServerCommunicationLinkInner>, ServerCommunicationLinkInner>() {
            @Override
            public ServerCommunicationLinkInner call(ServiceResponse<ServerCommunicationLinkInner> response) {
                return response.body();
            }
        });
    }

    /**
     * Returns a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the ServerCommunicationLinkInner object
     */
    public Observable<ServiceResponse<ServerCommunicationLinkInner>> getWithServiceResponseAsync(String resourceGroupName, String serverName, String communicationLinkName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serverName == null) {
            throw new IllegalArgumentException("Parameter serverName is required and cannot be null.");
        }
        if (communicationLinkName == null) {
            throw new IllegalArgumentException("Parameter communicationLinkName is required and cannot be null.");
        }
        final String apiVersion = "2014-04-01";
        return service.get(this.client.subscriptionId(), resourceGroupName, serverName, communicationLinkName, apiVersion, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<ServerCommunicationLinkInner>>>() {
                @Override
                public Observable<ServiceResponse<ServerCommunicationLinkInner>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<ServerCommunicationLinkInner> clientResponse = getDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<ServerCommunicationLinkInner> getDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<ServerCommunicationLinkInner, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<ServerCommunicationLinkInner>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the ServerCommunicationLinkInner object if successful.
     */
    public ServerCommunicationLinkInner createOrUpdate(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer) {
        return createOrUpdateWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName, partnerServer).toBlocking().last().body();
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<ServerCommunicationLinkInner> createOrUpdateAsync(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer, final ServiceCallback<ServerCommunicationLinkInner> serviceCallback) {
        return ServiceFuture.fromResponse(createOrUpdateWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName, partnerServer), serviceCallback);
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<ServerCommunicationLinkInner> createOrUpdateAsync(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer) {
        return createOrUpdateWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName, partnerServer).map(new Func1<ServiceResponse<ServerCommunicationLinkInner>, ServerCommunicationLinkInner>() {
            @Override
            public ServerCommunicationLinkInner call(ServiceResponse<ServerCommunicationLinkInner> response) {
                return response.body();
            }
        });
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<ServiceResponse<ServerCommunicationLinkInner>> createOrUpdateWithServiceResponseAsync(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serverName == null) {
            throw new IllegalArgumentException("Parameter serverName is required and cannot be null.");
        }
        if (communicationLinkName == null) {
            throw new IllegalArgumentException("Parameter communicationLinkName is required and cannot be null.");
        }
        if (partnerServer == null) {
            throw new IllegalArgumentException("Parameter partnerServer is required and cannot be null.");
        }
        final String apiVersion = "2014-04-01";
        ServerCommunicationLinkInner parameters = new ServerCommunicationLinkInner();
        parameters.withPartnerServer(partnerServer);
        Observable<Response<ResponseBody>> observable = service.createOrUpdate(this.client.subscriptionId(), resourceGroupName, serverName, communicationLinkName, apiVersion, this.client.acceptLanguage(), parameters, this.client.userAgent());
        return client.getAzureClient().getPutOrPatchResultAsync(observable, new TypeToken<ServerCommunicationLinkInner>() { }.getType());
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the ServerCommunicationLinkInner object if successful.
     */
    public ServerCommunicationLinkInner beginCreateOrUpdate(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer) {
        return beginCreateOrUpdateWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName, partnerServer).toBlocking().single().body();
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<ServerCommunicationLinkInner> beginCreateOrUpdateAsync(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer, final ServiceCallback<ServerCommunicationLinkInner> serviceCallback) {
        return ServiceFuture.fromResponse(beginCreateOrUpdateWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName, partnerServer), serviceCallback);
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the ServerCommunicationLinkInner object
     */
    public Observable<ServerCommunicationLinkInner> beginCreateOrUpdateAsync(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer) {
        return beginCreateOrUpdateWithServiceResponseAsync(resourceGroupName, serverName, communicationLinkName, partnerServer).map(new Func1<ServiceResponse<ServerCommunicationLinkInner>, ServerCommunicationLinkInner>() {
            @Override
            public ServerCommunicationLinkInner call(ServiceResponse<ServerCommunicationLinkInner> response) {
                return response.body();
            }
        });
    }

    /**
     * Creates a server communication link.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param communicationLinkName The name of the server communication link.
     * @param partnerServer The name of the partner server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the ServerCommunicationLinkInner object
     */
    public Observable<ServiceResponse<ServerCommunicationLinkInner>> beginCreateOrUpdateWithServiceResponseAsync(String resourceGroupName, String serverName, String communicationLinkName, String partnerServer) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serverName == null) {
            throw new IllegalArgumentException("Parameter serverName is required and cannot be null.");
        }
        if (communicationLinkName == null) {
            throw new IllegalArgumentException("Parameter communicationLinkName is required and cannot be null.");
        }
        if (partnerServer == null) {
            throw new IllegalArgumentException("Parameter partnerServer is required and cannot be null.");
        }
        final String apiVersion = "2014-04-01";
        ServerCommunicationLinkInner parameters = new ServerCommunicationLinkInner();
        parameters.withPartnerServer(partnerServer);
        return service.beginCreateOrUpdate(this.client.subscriptionId(), resourceGroupName, serverName, communicationLinkName, apiVersion, this.client.acceptLanguage(), parameters, this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<ServerCommunicationLinkInner>>>() {
                @Override
                public Observable<ServiceResponse<ServerCommunicationLinkInner>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<ServerCommunicationLinkInner> clientResponse = beginCreateOrUpdateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<ServerCommunicationLinkInner> beginCreateOrUpdateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<ServerCommunicationLinkInner, CloudException>newInstance(this.client.serializerAdapter())
                .register(201, new TypeToken<ServerCommunicationLinkInner>() { }.getType())
                .register(202, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets a list of server communication links.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the List&lt;ServerCommunicationLinkInner&gt; object if successful.
     */
    public List<ServerCommunicationLinkInner> listByServer(String resourceGroupName, String serverName) {
        return listByServerWithServiceResponseAsync(resourceGroupName, serverName).toBlocking().single().body();
    }

    /**
     * Gets a list of server communication links.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<ServerCommunicationLinkInner>> listByServerAsync(String resourceGroupName, String serverName, final ServiceCallback<List<ServerCommunicationLinkInner>> serviceCallback) {
        return ServiceFuture.fromResponse(listByServerWithServiceResponseAsync(resourceGroupName, serverName), serviceCallback);
    }

    /**
     * Gets a list of server communication links.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the List&lt;ServerCommunicationLinkInner&gt; object
     */
    public Observable<List<ServerCommunicationLinkInner>> listByServerAsync(String resourceGroupName, String serverName) {
        return listByServerWithServiceResponseAsync(resourceGroupName, serverName).map(new Func1<ServiceResponse<List<ServerCommunicationLinkInner>>, List<ServerCommunicationLinkInner>>() {
            @Override
            public List<ServerCommunicationLinkInner> call(ServiceResponse<List<ServerCommunicationLinkInner>> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets a list of server communication links.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serverName The name of the server.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the List&lt;ServerCommunicationLinkInner&gt; object
     */
    public Observable<ServiceResponse<List<ServerCommunicationLinkInner>>> listByServerWithServiceResponseAsync(String resourceGroupName, String serverName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serverName == null) {
            throw new IllegalArgumentException("Parameter serverName is required and cannot be null.");
        }
        final String apiVersion = "2014-04-01";
        return service.listByServer(this.client.subscriptionId(), resourceGroupName, serverName, apiVersion, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<List<ServerCommunicationLinkInner>>>>() {
                @Override
                public Observable<ServiceResponse<List<ServerCommunicationLinkInner>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<ServerCommunicationLinkInner>> result = listByServerDelegate(response);
                        ServiceResponse<List<ServerCommunicationLinkInner>> clientResponse = new ServiceResponse<List<ServerCommunicationLinkInner>>(result.body().items(), result.response());
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<ServerCommunicationLinkInner>> listByServerDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<ServerCommunicationLinkInner>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<ServerCommunicationLinkInner>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}
