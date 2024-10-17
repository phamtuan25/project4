package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;

import java.time.LocalDateTime;


public class ProvisionMapper {

        public static ProvisionResponse convertToResponse(Provision provision) {
            ProvisionResponse provisionResponse = new ProvisionResponse();
            provisionResponse.setProvisionId(provision.getProvisionId());
            provisionResponse.setProvisionName(provision.getProvisionName());
            provisionResponse.setDescription(provision.getDescription());
            provisionResponse.setPrice(provision.getPrice());
            provisionResponse.setCreatedAt(provision.getCreatedAt());
            provisionResponse.setUpdatedAt(provision.getUpdatedAt());
            return provisionResponse;
        }

        public static Provision convertFromRequest(ProvisionRequest provisionRequest) {
            Provision provision = new Provision();
            provision.setProvisionName(provisionRequest.getProvisionName());
            provision.setDescription(provisionRequest.getDescription());
            provision.setPrice(provisionRequest.getPrice());
            return provision;
        }

}
