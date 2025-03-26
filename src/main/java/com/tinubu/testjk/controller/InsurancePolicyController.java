package com.tinubu.testjk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tinubu.testjk.entity.InsurancePolicy;
import com.tinubu.testjk.service.InsurancePolicyService;

@RestController
public class InsurancePolicyController {

    @Autowired
    InsurancePolicyService policyService;

    @GetMapping("/api/v1/policies")
    public List<InsurancePolicy> getPolicies() {
        return policyService.findAll();
    }

    @PostMapping("/api/v1/policy")
    public ResponseEntity<InsurancePolicy> createPolicy(@RequestBody InsurancePolicy policy) {
        InsurancePolicy createdPolicy = policyService.createPolicy(policy);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPolicy);
    }

    @GetMapping("/api/v1/policy/{id}")
    public InsurancePolicy getPolicy(@PathVariable Integer id) {
        return policyService.findPolicy(id);
    }

    @PutMapping("/api/v1/policy/{id}")
    public InsurancePolicy updatePolicy(@PathVariable Integer id, @RequestBody InsurancePolicy policy) {
        return policyService.updatePolicy(id, policy);
    }
}
