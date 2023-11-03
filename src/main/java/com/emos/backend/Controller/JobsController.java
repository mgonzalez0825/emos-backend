package com.emos.backend.Controller;
import java.util.*;

import com.emos.backend.Exception.ResourceNotFoundException;
import com.emos.backend.Model.Department;
import com.emos.backend.Model.Employee;
import com.emos.backend.Model.EmployeeDetails;
import com.emos.backend.Repository.DepartmentRepository;
import com.emos.backend.Repository.EmployeeRepository;
import com.emos.backend.Repository.EmployeeDetailsRepository;
import com.emos.backend.Model.Jobs;
import com.emos.backend.Model.Department;


import com.emos.backend.Repository.JobsRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class JobsController {

    @Autowired
    private JobsRepository jobsRepository;


    @Autowired
    private DepartmentRepository departmentRepository;
    // getAllEmployees rest

    @GetMapping("/jobs")
    public ArrayList<Jobs> getAllJobs() {
        return (ArrayList<Jobs>) jobsRepository.findAll();
    }


    //create employee API
    @PostMapping("/jobs")
    public ResponseEntity<Jobs> createJob(@RequestBody Jobs jobRequest) {

        Department department = departmentRepository.findById(jobRequest.getDepartment().getDeptNo())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + jobRequest.getDepartment().getDeptNo()));


        Jobs job = new Jobs();
        job.setDepartment(department); // Set the department on the employee object
        job.setJobTitle(jobRequest.getJobTitle());
        job.setJobDescription(jobRequest.getJobDescription());
        job.setJobType(jobRequest.getJobType());
        job.setJobPayRange(jobRequest.getJobPayRange());


        Jobs createdJob = jobsRepository.save(job);

        department.getJobs().add(createdJob);
        departmentRepository.save(department);

        return ResponseEntity.ok(createdJob);
    }


    //get jobsById API

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Jobs> getJobById(@PathVariable Long id) {


        Jobs job = jobsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + id + " does not exists"));
        return ResponseEntity.ok(job);
    }


    // update job
    @PutMapping("/jobs/{id}")
    public ResponseEntity<Jobs> updateJob(@PathVariable Long id, @RequestBody Jobs jobRequest) {

        Jobs job = jobsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Department department = departmentRepository.findById(jobRequest.getDepartment().getDeptNo())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + jobRequest.getDepartment().getDeptNo()));

        job.setDepartment(department);
        job.setJobTitle(jobRequest.getJobTitle());
        job.setJobDescription(jobRequest.getJobDescription());
        job.setJobType(jobRequest.getJobType());
        job.setJobPayRange(jobRequest.getJobPayRange());


        Jobs updatedJob = jobsRepository.save(job);

        return ResponseEntity.ok(updatedJob);
    }


    //delete job API
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity< Map<String,Boolean>> deleteJobById(@PathVariable Long id){

        Jobs job = jobsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id: " + id + " does not exists"));

        jobsRepository.delete(job);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
