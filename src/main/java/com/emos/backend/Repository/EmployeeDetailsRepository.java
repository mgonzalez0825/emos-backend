package com.emos.backend.Repository;


import com.emos.backend.Model.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {
    }

