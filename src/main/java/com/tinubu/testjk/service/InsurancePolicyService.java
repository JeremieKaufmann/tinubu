package com.tinubu.testjk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinubu.testjk.entity.InsurancePolicy;
import com.tinubu.testjk.repository.InsurancePolicyRepository;

@Service
public class InsurancePolicyService {

    @Autowired
    InsurancePolicyRepository repository;

    public List<InsurancePolicy> findAll() {
        return repository.findAll();
    }

    public InsurancePolicy createPolicy(InsurancePolicy policy) {
        return repository.save(policy);
    }

    public InsurancePolicy findPolicy(Integer id) {
        // Try to find the existing ID,
        // and throw an Exception if the policy doesn't exist
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    public InsurancePolicy updatePolicy(Integer id, InsurancePolicy updatedPolicy) {
        InsurancePolicy foundPolicy = findPolicy(id);
        foundPolicy.setCoverageStartDate(updatedPolicy.getCoverageStartDate());
        foundPolicy.setCoverageEndDate(updatedPolicy.getCoverageEndDate());
        foundPolicy.setStatus(updatedPolicy.getStatus());
        foundPolicy.setName(updatedPolicy.getName());

        return repository.save(foundPolicy);
    }

}
