package com.example.projectbackend.repository;

import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long>, JpaSpecificationExecutor<Provision> {

}




