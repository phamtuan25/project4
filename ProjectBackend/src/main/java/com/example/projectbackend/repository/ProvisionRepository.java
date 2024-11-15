package com.example.projectbackend.repository;

import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long>, JpaSpecificationExecutor<Provision> {
    List<Provision> findAllByProvisionIdIn(List<Long> provisionIds);
}




