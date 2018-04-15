package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IRequestOperationCallback;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class AdditionalRequestHeadersInterceptorR4Test {

	private static final String ERR403 = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"processing\",\"diagnostics\":\"Access denied by default policy (no applicable rules)\"}]}";
	private static CloseableHttpClient ourClient;
	private static String ourConditionalCreateId;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static boolean ourHitMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AdditionalRequestHeadersInterceptorR4Test.class);
	private static int ourPort;
	private static List<Resource> ourReturn;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		for (IServerInterceptor next : new ArrayList<>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
		ourReturn = null;
		ourHitMethod = false;
		ourConditionalCreateId = "1123";
	}

	private Resource createCarePlan(Integer theId, String theSubjectId) {
		CarePlan retVal = new CarePlan();
		if (theId != null) {
			retVal.setId(new IdType("CarePlan", (long) theId));
		}
		retVal.setSubject(new Reference("Patient/" + theSubjectId));
		return retVal;
	}

	private HttpEntity createFhirResourceEntity(IBaseResource theResource) {
		String out = ourCtx.newJsonParser().encodeResourceToString(theResource);
		return new StringEntity(out, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8"));
	}

	private Resource createObservation(Integer theId, String theSubjectId) {
		Observation retVal = new Observation();
		if (theId != null) {
			retVal.setId(new IdType("Observation", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new Reference(theSubjectId));
		return retVal;
	}

	private Resource createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdType("Patient", (long) theId));
		}
		retVal.addName().setFamily("FAM");
		return retVal;
	}

	private Resource createPatient(Integer theId, int theVersion) {
		Resource retVal = createPatient(theId);
		retVal.setId(retVal.getIdElement().withVersion(Integer.toString(theVersion)));
		return retVal;
	}

	private String extractResponseAndClose(HttpResponse status) throws IOException {
		if (status.getEntity() == null) {
			return null;
		}
		String responseContent;
		responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		return responseContent;
	}

	@Test
	public void testAllowAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allowAll("Default Rule")
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Rule 1"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithAnyType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().denyAll().build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().allow("Rule 1").read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().denyAll()
						.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testBatchWhenOnlyTransactionAllowed() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Collections.singletonList((Resource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testBatchWhenTransactionReadDenied() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(2));

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Collections.singletonList((Resource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testBatchWhenTransactionWrongBundleType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.COLLECTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(1));

		HttpPost httpPost;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList((Resource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
	}

	@Test
	public void testDeleteByCompartment() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").delete().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testDenyAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("Default Rule")
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithAnyType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll().build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll()
						.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testHistoryWithReadAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().withAnyId()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2, 1));

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testMetadataAllow() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").metadata()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testMetadataDeny() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").metadata()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testOperationAnyName() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().withAnyName().onServer().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeAllowed() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class)
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeWithInvalidReturnValue() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// With a return value we don't allow
		ourReturn = Collections.singletonList(createPatient(222));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		// With a return value we do
		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeWithReturnValue() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class)
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
	}

	@Test
	public void testOperationInstanceLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onInstance(new IdType("http://example.com/Patient/1/_history/2")).andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationInstanceLevelAnyInstance() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
						.allow("RULE 1").operation().named("opName").onAnyInstance().andThen()
						.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Another Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationNotAllowedWithWritePermissiom() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").write().allResources().withAnyId().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// System
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationServerLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onServer().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Patient.class).andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWildcard() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onAnyType().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Another type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByAnyId() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentRight() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadPageRight() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
						.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1"))
						.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String respString;
		Bundle respBundle;

		ourReturn = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			ourReturn.add(createPatient(1));
		}

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_count=5&_format=json");
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(10, respBundle.getTotal());
		assertEquals("Patient/1", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page

		ourHitMethod = false;
		httpGet = new HttpGet(respBundle.getLink("next").getUrl());
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(10, respBundle.getTotal());
		assertEquals("Patient/1", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNull(respBundle.getLink("next"));

	}

	@Test
	public void testReadPageWrong() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
						.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1"))
						.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String respString;
		Bundle respBundle;

		ourReturn = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ourReturn.add(createPatient(1));
		}
		for (int i = 0; i < 5; i++) {
			ourReturn.add(createPatient(2));
		}

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_count=5&_format=json");
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(10, respBundle.getTotal());
		assertEquals("Patient/1", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page

		ourHitMethod = false;
		httpGet = new HttpGet(respBundle.getLink("next").getUrl());
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrong() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createCarePlan(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testTransactionWriteGood() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.PUT);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Collections.singletonList((Resource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testWriteByCompartmentCreate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 1b").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1123")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader("If-None-Exist", "Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/1")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentCreateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").createConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));

		ourHitMethod = false;
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalWithoutDirectMatch() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));

		ourHitMethod = false;
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDoesntAllowDelete() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		String response;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
		httpPost.setEntity(createFhirResourceEntity(createPatient(2)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/1");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(99)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToInvalid() throws Exception {
		ourConditionalCreateId = "1123";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValidAllTypes() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().allResources()
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertTrue(ourHitMethod);

	}

	@Test
	public void testInvalidInstanceIds() throws Exception {
		try {
			new RuleBuilder().allow("Rule 1").write().instance((String) null);
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("Observation/");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType());
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType(""));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType("Observation", (String) null));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
	}

	@Test
	public void testWritePatchByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().instance("Patient/900").andThen()
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		String input = "[ { \"op\": \"replace\", \"path\": \"/gender\", \"value\": \"male\" }  ]";

		ourHitMethod = false;
		httpPost = new HttpPatch("http://localhost:" + ourPort + "/Patient/900");
		httpPost.setEntity(new StringEntity(input, ContentType.parse("application/json-patch+json")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPatch("http://localhost:" + ourPort + "/Patient/999");
		httpPost.setEntity(new StringEntity(input, ContentType.parse("application/json-patch+json")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testWriteByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().instance("Observation/900").andThen()
					.allow("Rule 1").write().instance("901").andThen()
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/900");
		httpPost.setEntity(createFhirResourceEntity(createObservation(900, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/901");
		httpPost.setEntity(createFhirResourceEntity(createObservation(901, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/900")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().instance("Observation/900").andThen()
					.allow("Rule 1").read().instance("901").andThen()
					.build();
			}
		});

		HttpResponse status;
		String response;
		HttpGet httpGet;

		ourReturn = Collections.singletonList(createObservation(900, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/900");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(901));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/901");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patProvider = new DummyPatientResourceProvider();
		DummyObservationResourceProvider obsProv = new DummyObservationResourceProvider();
		DummyEncounterResourceProvider encProv = new DummyEncounterResourceProvider();
		DummyCarePlanResourceProvider cpProv = new DummyCarePlanResourceProvider();
		PlainProvider plainProvider = new PlainProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patProvider, obsProv, encProv, cpProv);
		ourServlet.setPlainProviders(plainProvider);
		ourServlet.setPagingProvider(new FifoMemoryPagingProvider(100));
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyCarePlanResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CarePlan.class;
		}

		@Read(version = true)
		public CarePlan read(@IdParam IdType theId) {
			ourHitMethod = true;
			return (CarePlan) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search() {
			ourHitMethod = true;
			return ourReturn;
		}
	}

	public static class DummyEncounterResourceProvider implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			ourHitMethod = true;
			Bundle retVal = new Bundle();
			for (Resource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Encounter.class;
		}
	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourHitMethod = true;
			theResource.setId("Observation/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(@IdParam IdType theId) {
			ourHitMethod = true;
			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Read(version = true)
		public Observation read(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Observation) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search() {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Observation", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(actual);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(theId.withVersion("2"));
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Patient", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.CREATE);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.CREATE);
			}

			ourHitMethod = true;
			theResource.setId("Patient/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(IRequestOperationCallback theRequestOperationCallback, @IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			for (IBaseResource next : ourReturn) {
				theRequestOperationCallback.resourceDeleted(next);
			}
			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			ourHitMethod = true;
			Bundle retVal = new Bundle();
			for (Resource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@History()
		public List<Resource> history() {
			ourHitMethod = true;
			return (ourReturn);
		}

		@History()
		public List<Resource> history(@IdParam IdType theId) {
			ourHitMethod = true;
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Patient) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search() {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Patient", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(actual);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(theId.withVersion("2"));
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

		@Patch()
		public MethodOutcome patch(@IdParam IdType theId, @ResourceParam String theResource, PatchTypeEnum thePatchType) {
			ourHitMethod = true;

			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding,
				@Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
				@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

	}

	public static class PlainProvider {

		@History()
		public List<Resource> history() {
			ourHitMethod = true;
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Transaction()
		public Bundle search(@TransactionParam Bundle theInput) {
			ourHitMethod = true;
			return (Bundle) ourReturn.get(0);
		}

	}

}
