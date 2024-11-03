package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProvisionService {
    public Page<ProvisionResponse> getAllProvision(Pageable pageable, String keyword);
    public ProvisionResponse getDetailProvision(Long serviceId);
    public Provision createProvision(ProvisionRequest provisionRequest);
    public Provision updateProvision(Long provisionId, ProvisionRequest provisionRequest);
    public void deleteProvision(Long provisionId);
}
