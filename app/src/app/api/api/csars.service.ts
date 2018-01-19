/**
 * TOSCAna
 * To be Done!
 *
 * OpenAPI spec version: 1.0.0-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs/Observable';

import { CsarResources } from '../model/csarResources';
import { CsarResponse } from '../model/csarResponse';
import { CsarUploadErrorResponse } from '../model/csarUploadErrorResponse';
import { RestErrorResponse } from '../model/restErrorResponse';
import { TransformationResources } from '../model/transformationResources';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class CsarsService {

    protected basePath = 'https://stupro-toscana.de';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (let consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * Deletes a Existing CSAR
     * Deletes the Resulting CSAR and its transformations (if none of them is running). If a transformation is running (in the state TRANSFORMING) the csar cannot be deleted!
     * @param name The unique identifier for the CSAR
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public deleteCsarUsingDELETE(name: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public deleteCsarUsingDELETE(name: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public deleteCsarUsingDELETE(name: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public deleteCsarUsingDELETE(name: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (name === null || name === undefined) {
            throw new Error('Required parameter name was null or undefined when calling deleteCsarUsingDELETE.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/hal+json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];

        return this.httpClient.delete<any>(`${this.basePath}/api/csars/${encodeURIComponent(String(name))}/delete`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Returns details for a specific name (identifier)
     * Returns the element with the given name, Object contents are equal to a regular /csars request (if you just look at the desired entry)
     * @param name The unique identifier for the CSAR
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getCSARInfoUsingGET(name: string, observe?: 'body', reportProgress?: boolean): Observable<CsarResponse>;
    public getCSARInfoUsingGET(name: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<CsarResponse>>;
    public getCSARInfoUsingGET(name: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<CsarResponse>>;
    public getCSARInfoUsingGET(name: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (name === null || name === undefined) {
            throw new Error('Required parameter name was null or undefined when calling getCSARInfoUsingGET.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/hal+json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];

        return this.httpClient.get<CsarResponse>(`${this.basePath}/api/csars/${encodeURIComponent(String(name))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * List all transformations of a CSAR
     * Returns a HAL-Resources list containing all Transformations for a specific CSAR
     * @param csarId The unique identifier for the CSAR
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getCSARTransformationsUsingGET(csarId: string, observe?: 'body', reportProgress?: boolean): Observable<TransformationResources>;
    public getCSARTransformationsUsingGET(csarId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<TransformationResources>>;
    public getCSARTransformationsUsingGET(csarId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<TransformationResources>>;
    public getCSARTransformationsUsingGET(csarId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (csarId === null || csarId === undefined) {
            throw new Error('Required parameter csarId was null or undefined when calling getCSARTransformationsUsingGET.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/hal+json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];

        return this.httpClient.get<TransformationResources>(`${this.basePath}/api/csars/${encodeURIComponent(String(csarId))}/transformations`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * List all CSARs stored on the server
     * Returns a Hypermedia Resource containing all CSARs that have been uploaded to the server and did not get removed
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public listCSARsUsingGET(observe?: 'body', reportProgress?: boolean): Observable<CsarResources>;
    public listCSARsUsingGET(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<CsarResources>>;
    public listCSARsUsingGET(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<CsarResources>>;
    public listCSARsUsingGET(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/hal+json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];

        return this.httpClient.get<CsarResources>(`${this.basePath}/api/csars`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Creates a new CSAR
     * This operation creates a new CSAR if the name (identifier) is not used already. The uploaded file has to be a valid CSAR archive. Once the file was uploaded the server will synchronously (the client has to wait for the response) unzip the archive and parse it. Upload gets performed using Multipart Form upload.
     * @param name The unique identifier for the CSAR
     * @param file The CSAR Archive (Compressed as ZIP)
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public uploadCSARUsingPOST(name: string, file: Blob, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public uploadCSARUsingPOST(name: string, file: Blob, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public uploadCSARUsingPOST(name: string, file: Blob, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public uploadCSARUsingPOST(name: string, file: Blob, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (name === null || name === undefined) {
            throw new Error('Required parameter name was null or undefined when calling uploadCSARUsingPOST.');
        }
        if (file === null || file === undefined) {
            throw new Error('Required parameter file was null or undefined when calling uploadCSARUsingPOST.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/hal+json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'multipart/form-data'
        ];

        const canConsumeForm = this.canConsumeForm(consumes);

        let formParams: { append(param: string, value: any): void; };
        let useForm = false;
        let convertFormParamsToString = false;
        // use FormData to transmit files using content-type "multipart/form-data"
        // see https://stackoverflow.com/questions/4007969/application-x-www-form-urlencoded-or-multipart-form-data
        useForm = canConsumeForm;
        if (useForm) {
            formParams = new FormData();
        } else {
            formParams = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        }

        if (file !== undefined) {
            formParams = formParams.append('file', <any>file) || formParams;
        }

        return this.httpClient.post<any>(`${this.basePath}/api/csars/${encodeURIComponent(String(name))}`,
            convertFormParamsToString ? formParams.toString() : formParams,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Creates a new CSAR
     * This operation creates a new CSAR if the name (identifier) is not used already. The uploaded file has to be a valid CSAR archive. Once the file was uploaded the server will synchronously (the client has to wait for the response) unzip the archive and parse it. Upload gets performed using Multipart Form upload.
     * @param name The unique identifier for the CSAR
     * @param file The CSAR Archive (Compressed as ZIP)
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public uploadCSARUsingPUT(name: string, file: Blob, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public uploadCSARUsingPUT(name: string, file: Blob, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public uploadCSARUsingPUT(name: string, file: Blob, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public uploadCSARUsingPUT(name: string, file: Blob, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (name === null || name === undefined) {
            throw new Error('Required parameter name was null or undefined when calling uploadCSARUsingPUT.');
        }
        if (file === null || file === undefined) {
            throw new Error('Required parameter file was null or undefined when calling uploadCSARUsingPUT.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/hal+json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'multipart/form-data'
        ];

        const canConsumeForm = this.canConsumeForm(consumes);

        let formParams: { append(param: string, value: any): void; };
        let useForm = false;
        let convertFormParamsToString = false;
        // use FormData to transmit files using content-type "multipart/form-data"
        // see https://stackoverflow.com/questions/4007969/application-x-www-form-urlencoded-or-multipart-form-data
        useForm = canConsumeForm;
        if (useForm) {
            formParams = new FormData();
        } else {
            formParams = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        }

        if (file !== undefined) {
            formParams = formParams.append('file', <any>file) || formParams;
        }

        return this.httpClient.put<any>(`${this.basePath}/api/csars/${encodeURIComponent(String(name))}`,
            convertFormParamsToString ? formParams.toString() : formParams,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
