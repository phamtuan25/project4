package com.example.projectbackend.repository;

import com.example.projectbackend.entity.RelProvisionBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RelProvisionBookingDetailRepository extends JpaRepository<RelProvisionBookingDetail, Long>, JpaSpecificationExecutor<RelProvisionBookingDetail> {
}
