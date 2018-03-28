/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.apimanagement.implementation;

import retrofit2.Retrofit;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.management.apimanagement.ErrorResponseException;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.rest.Validator;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in QuotaByPeriodKeys.
 */
public class QuotaByPeriodKeysInner {
    /** The Retrofit service to perform REST calls. */
    private QuotaByPeriodKeysService service;
    /** The service client containing this operation class. */
    private ApiManagementClientImpl client;

    /**
     * Initializes an instance of QuotaByPeriodKeysInner.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public QuotaByPeriodKeysInner(Retrofit retrofit, ApiManagementClientImpl client) {
        this.service = retrofit.create(QuotaByPeriodKeysService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for QuotaByPeriodKeys to be
     * used by Retrofit to perform actually REST calls.
     */
    interface QuotaByPeriodKeysService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.apimanagement.QuotaByPeriodKeys get" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.ApiManagement/service/{serviceName}/quotas/{quotaCounterKey}/periods/{quotaPeriodKey}")
        Observable<Response<ResponseBody>> get(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("serviceName") String serviceName, @Path("quotaCounterKey") String quotaCounterKey, @Path("quotaPeriodKey") String quotaPeriodKey, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.apimanagement.QuotaByPeriodKeys update" })
        @PATCH("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.ApiManagement/service/{serviceName}/quotas/{quotaCounterKey}/periods/{quotaPeriodKey}")
        Observable<Response<ResponseBody>> update(@Path("resourceGroupName") String resourceGroupName, @Path("serviceName") String serviceName, @Path("quotaCounterKey") String quotaCounterKey, @Path("quotaPeriodKey") String quotaPeriodKey, @Path("subscriptionId") String subscriptionId, @Body QuotaCounterValueContractPropertiesInner parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Gets the value of the quota counter associated with the counter-key in the policy for the specific period in service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws ErrorResponseException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the QuotaCounterContractInner object if successful.
     */
    public QuotaCounterContractInner get(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey) {
        return getWithServiceResponseAsync(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey).toBlocking().single().body();
    }

    /**
     * Gets the value of the quota counter associated with the counter-key in the policy for the specific period in service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<QuotaCounterContractInner> getAsync(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey, final ServiceCallback<QuotaCounterContractInner> serviceCallback) {
        return ServiceFuture.fromResponse(getWithServiceResponseAsync(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey), serviceCallback);
    }

    /**
     * Gets the value of the quota counter associated with the counter-key in the policy for the specific period in service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QuotaCounterContractInner object
     */
    public Observable<QuotaCounterContractInner> getAsync(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey) {
        return getWithServiceResponseAsync(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey).map(new Func1<ServiceResponse<QuotaCounterContractInner>, QuotaCounterContractInner>() {
            @Override
            public QuotaCounterContractInner call(ServiceResponse<QuotaCounterContractInner> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets the value of the quota counter associated with the counter-key in the policy for the specific period in service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the QuotaCounterContractInner object
     */
    public Observable<ServiceResponse<QuotaCounterContractInner>> getWithServiceResponseAsync(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serviceName == null) {
            throw new IllegalArgumentException("Parameter serviceName is required and cannot be null.");
        }
        if (quotaCounterKey == null) {
            throw new IllegalArgumentException("Parameter quotaCounterKey is required and cannot be null.");
        }
        if (quotaPeriodKey == null) {
            throw new IllegalArgumentException("Parameter quotaPeriodKey is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.get(this.client.subscriptionId(), resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<QuotaCounterContractInner>>>() {
                @Override
                public Observable<ServiceResponse<QuotaCounterContractInner>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<QuotaCounterContractInner> clientResponse = getDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<QuotaCounterContractInner> getDelegate(Response<ResponseBody> response) throws ErrorResponseException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<QuotaCounterContractInner, ErrorResponseException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<QuotaCounterContractInner>() { }.getType())
                .registerError(ErrorResponseException.class)
                .build(response);
    }

    /**
     * Updates an existing quota counter value in the specified service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @param parameters The value of the Quota counter to be applied on the specified period.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws ErrorResponseException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void update(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey, QuotaCounterValueContractPropertiesInner parameters) {
        updateWithServiceResponseAsync(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey, parameters).toBlocking().single().body();
    }

    /**
     * Updates an existing quota counter value in the specified service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @param parameters The value of the Quota counter to be applied on the specified period.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> updateAsync(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey, QuotaCounterValueContractPropertiesInner parameters, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey, parameters), serviceCallback);
    }

    /**
     * Updates an existing quota counter value in the specified service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @param parameters The value of the Quota counter to be applied on the specified period.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> updateAsync(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey, QuotaCounterValueContractPropertiesInner parameters) {
        return updateWithServiceResponseAsync(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey, parameters).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates an existing quota counter value in the specified service instance.
     *
     * @param resourceGroupName The name of the resource group.
     * @param serviceName The name of the API Management service.
     * @param quotaCounterKey Quota counter key identifier.This is the result of expression defined in counter-key attribute of the quota-by-key policy.For Example, if you specify counter-key="boo" in the policy, then it’s accessible by "boo" counter key. But if it’s defined as counter-key="@("b"+"a")" then it will be accessible by "ba" key
     * @param quotaPeriodKey Quota period key identifier.
     * @param parameters The value of the Quota counter to be applied on the specified period.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> updateWithServiceResponseAsync(String resourceGroupName, String serviceName, String quotaCounterKey, String quotaPeriodKey, QuotaCounterValueContractPropertiesInner parameters) {
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (serviceName == null) {
            throw new IllegalArgumentException("Parameter serviceName is required and cannot be null.");
        }
        if (quotaCounterKey == null) {
            throw new IllegalArgumentException("Parameter quotaCounterKey is required and cannot be null.");
        }
        if (quotaPeriodKey == null) {
            throw new IllegalArgumentException("Parameter quotaPeriodKey is required and cannot be null.");
        }
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter parameters is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        return service.update(resourceGroupName, serviceName, quotaCounterKey, quotaPeriodKey, this.client.subscriptionId(), parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = updateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Void> updateDelegate(Response<ResponseBody> response) throws ErrorResponseException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Void, ErrorResponseException>newInstance(this.client.serializerAdapter())
                .register(204, new TypeToken<Void>() { }.getType())
                .registerError(ErrorResponseException.class)
                .build(response);
    }

}
