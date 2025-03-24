package com.tinubu.testjk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.javafaker.Faker;
import com.tinubu.testjk.entity.InsurancePolicy;
import com.tinubu.testjk.enums.PolicyStatus;
import com.tinubu.testjk.repository.InsurancePolicyRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class InsurancePolicyRepositoryTest {

    @Autowired
    InsurancePolicyRepository repository;
    private InsurancePolicy policy;
    private Faker faker;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        // Initialisation d'une insurance policy pour les tests
        policy = new InsurancePolicy();
        policy.setName(faker.company().name());
        policy.setStatus(PolicyStatus.ACTIVE);
        LocalDate now = LocalDate.now();
        policy.setCoverageStartDate(now.minusWeeks(faker.number().randomDigit()));
        policy.setCoverageEndDate(now.plusWeeks(faker.number().randomDigit()));
    }

    @Test
    public void testCreateInsurancePolicy() {
        // Save the policy
        InsurancePolicy savedPolicy = repository.save(policy);
        System.out.println("savedPolicy=" + savedPolicy);

        // Check data
        assertNotNull(savedPolicy.getId(), "Policy ID should not be null after save");
        assertNotNull(savedPolicy.getCreationDate(), "Creation date should not be null after save");
        assertNull(savedPolicy.getLastUpdateDate(), "Last Update date should be null after save");
        assertEquals(policy.getName(), savedPolicy.getName(), "Policy name should match");
    }

    @Test
    public void testFindInsurancePolicyById() {
        // Save the policy
        InsurancePolicy savedPolicy = repository.save(policy);
        // Search for it
        InsurancePolicy foundPolicy = repository.findById(savedPolicy.getId()).orElse(null);
        
        // Check data
        assertNotNull(foundPolicy, "Policy should be found by ID");
        assertEquals(policy.getId(), foundPolicy.getId(), "Policy IDs should match");
    }

    @Test
    public void testUpdateInsurancePolicy() {
        // Save the policy
        InsurancePolicy savedPolicy = repository.saveAndFlush(policy);
        // Clear the hibernate cache and detach all entities so that the next findById returns another object
        entityManager.clear();
        System.out.println("savedPolicy=" + savedPolicy);

        // Search for the policy
        InsurancePolicy foundPolicy = repository.findById(savedPolicy.getId()).orElse(null);
        System.out.println("foundPolicy=" + foundPolicy);

        // Modify all policy properties
        foundPolicy.setName(faker.funnyName().name());
        LocalDate now = LocalDate.now();
        foundPolicy.setCoverageStartDate(now.minusMonths(faker.number().randomDigit() * 2));
        foundPolicy.setCoverageEndDate(now.plusMonths(faker.number().randomDigit() * 3));
        foundPolicy.setStatus(PolicyStatus.INACTIVE);
        // Save and flush to force lastUpdateDate
        InsurancePolicy updatedPolicy = repository.saveAndFlush(foundPolicy);
        System.out.println("updatedPolicy=" + updatedPolicy);

        assertEquals(savedPolicy.getId(), updatedPolicy.getId(), "Policy IDs should match");
        assertNotEquals(savedPolicy.getName(), updatedPolicy.getName(), "Policy names should be different");
        assertNotEquals(savedPolicy.getCoverageStartDate(), updatedPolicy.getCoverageStartDate(),
                "Policy coverage start date should be different");
        assertNotEquals(savedPolicy.getCoverageEndDate(), updatedPolicy.getCoverageEndDate(),
                "Policy coverage end date should be different");
        assertNotEquals(savedPolicy.getStatus(), updatedPolicy.getStatus(), "Policy status should be different");
        assertNotEquals(savedPolicy.getLastUpdateDate(), updatedPolicy.getLastUpdateDate(),
                "Policy last update date should be different");
        assertEquals(savedPolicy.getCreationDate().withNano(0), updatedPolicy.getCreationDate().withNano(0),
                "Policy creation date should match");
    }

    @Test
    public void testDeleteInsurancePolicy() {
        // Save the policy
        InsurancePolicy savedPolicy = repository.save(policy);
        assertNotNull(savedPolicy.getId(), "Policy ID should not be null after save");

        // Retrieve the policy
        InsurancePolicy foundPolicy = repository.findById(savedPolicy.getId()).orElse(null);
        assertNotNull(foundPolicy, "policy not found");

        // Delete the policy
        repository.delete(savedPolicy);

        // Check that the policy was indeed deleted
        InsurancePolicy deletedPolicy = repository.findById(savedPolicy.getId()).orElse(null);
        assertNull(deletedPolicy, "Policy should be null after delete");
    }
}
