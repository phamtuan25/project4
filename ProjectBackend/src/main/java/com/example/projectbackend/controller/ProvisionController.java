package com.example.projectbackend.controller;


import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.service.ProvisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/provisions")
public class ProvisionController {

    private final ProvisionService provisionService;
    @GetMapping
    public List<ProvisionResponse> getAllProvision() {
        return provisionService.getAllProvision();
    }

    @GetMapping("/{provisionId}")
    public ProvisionResponse getDetailServices(@PathVariable Long provisionId) {
        return provisionService.getDetailProvision(provisionId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public Provision createProvision(@RequestBody ProvisionRequest provisionRequest) {
        return provisionService.createProvision(provisionRequest);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{provisionId}")
    public Provision updateProvision(@PathVariable Long provisionId, @RequestBody  Provision provision) {
        return provisionService.updateProvision(provisionId, provision);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{provisionId}")
    public void deleteProvision(@PathVariable Long provisionId) {
        provisionService.deleteProvision(provisionId);
    }
}

