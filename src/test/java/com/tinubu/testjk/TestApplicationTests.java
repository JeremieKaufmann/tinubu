package com.tinubu.testjk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.github.javafaker.Faker;
import com.tinubu.testjk.entity.InsurancePolicy;
import com.tinubu.testjk.enums.InsurancePolicyStatus;
import com.tinubu.testjk.repository.InsurancePolicyRepository;

// Start the server on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// load SQL fixtures before tests
@Sql(scripts = "/fixtures.sql") 
public class TestApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	InsurancePolicyRepository repository;
	private Faker faker;
	private InsurancePolicy policy;

	@BeforeEach
	public void setUp() {
		faker = new Faker();
		policy = new InsurancePolicy();
		policy.setName(faker.company().name());
		policy.setStatus(InsurancePolicyStatus.INACTIVE);
		policy.setCoverageStartDate(LocalDate.now().minusWeeks(faker.number().randomDigit()));
		policy.setCoverageEndDate(LocalDate.now().plusWeeks(faker.number().randomDigit()));
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testCreatePolicy_OK() {
		ResponseEntity<InsurancePolicy> response = restTemplate.postForEntity("/api/v1/policy", policy, InsurancePolicy.class);

		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);

		InsurancePolicy createdPolicy = response.getBody();
		assertNotNull(createdPolicy);
		assertEquals(createdPolicy.getName(), policy.getName());
		assertEquals(createdPolicy.getStatus(), policy.getStatus());
		assertNull(createdPolicy.getLastUpdateDate());
		assertNotNull(createdPolicy.getCreationDate());
	}

	@Test
	void testCreatePolicy_KO_missingName() {
		policy.setName(null);
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/policy", policy, String.class);

		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains("Name should not be blank"));
	}

	@Test
	void testCreatePolicy_KO_missingStatus() {
		policy.setStatus(null);
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/policy", policy, String.class);

		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains("Status should not be blank"));
	}

	
	@Test
	void testCreatePolicy_KO_EmptyPolicy() {
		policy = new InsurancePolicy();
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/policy", policy, String.class);

		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains("Name should not be blank"));
		assertTrue(response.getBody().contains("Coverage start date shoud not be null"));
		assertTrue(response.getBody().contains("Coverage end date shoud not be null"));
		assertTrue(response.getBody().contains("Status should not be blank"));
		System.out.println(response.getBody());
	}

	@Test
	void testCreatePolicy_KO_EndDateBeforeStartDate() {
		policy.setCoverageEndDate(policy.getCoverageStartDate().minusDays(1));
		ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/policy", policy, String.class);
		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertNotNull(response.getBody());
		assertTrue(response.getBody().contains("Coverage end date must be after or equal to coverage start date"));
	}

	@Test
	void testGetAllEntities() {
		ResponseEntity<List<InsurancePolicy>> response = restTemplate.exchange(
				"/api/v1/policies",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<InsurancePolicy>>() {
				});

		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.OK);

		List<InsurancePolicy> policies = response.getBody();
		assertNotNull(policies);
		assertEquals(5, policies.size());
	}

	@Test
	void testGetPolicy() {
		// Getting Policy #1 - Life Insurance that is ACTIVE
		InsurancePolicy foundPolicy = getInsurancePolicy("1");

		assertNotNull(foundPolicy);
		assertEquals("Life Insurance", foundPolicy.getName());
		assertEquals(InsurancePolicyStatus.ACTIVE, foundPolicy.getStatus());
		assertNull(foundPolicy.getLastUpdateDate());
		assertNotNull(foundPolicy.getCreationDate());
	}

	@Test
	void testUpdatePolicy() {
		// Getting Policy #3
		String id = "3";
		InsurancePolicy foundPolicy = getInsurancePolicy(id);

		assertEquals("Car Insurance", foundPolicy.getName());
		assertEquals(InsurancePolicyStatus.INACTIVE, foundPolicy.getStatus());

		// Now we update it
		foundPolicy.setStatus(InsurancePolicyStatus.ACTIVE);
		foundPolicy.setName("UPDATED");
		foundPolicy.setCoverageStartDate(LocalDate.now());
		foundPolicy.setCoverageEndDate(LocalDate.now().plusDays(faker.number().randomNumber()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		restTemplate.put("/api/v1/policy/{id}", foundPolicy, params);

		// We retrieve it again to check modifications
		InsurancePolicy updatedPolicy = getInsurancePolicy(id);
		assertNotNull(updatedPolicy.getLastUpdateDate());
		assertEquals("UPDATED", updatedPolicy.getName());
	}

	private InsurancePolicy getInsurancePolicy(String id) {
		ResponseEntity<InsurancePolicy> response = restTemplate.exchange(
				"/api/v1/policy/{id}",
				HttpMethod.GET,
				null,
				InsurancePolicy.class,
				id);

		assertNotNull(response);
		assertNotNull(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.OK);

		InsurancePolicy policy = response.getBody();
		assertNotNull(policy);
		return policy;
	}

}
