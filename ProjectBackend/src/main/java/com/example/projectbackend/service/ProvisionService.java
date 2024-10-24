package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProvisionService {
    public List<ProvisionResponse> getAllProvision();
    public ProvisionResponse getDetailProvision(Long serviceId);
    public Provision createProvision(ProvisionRequest provisionRequest);
    public Provision updateProvision(Long provisionId, Provision provision);
    public void deleteProvision(Long provisionId);
}
