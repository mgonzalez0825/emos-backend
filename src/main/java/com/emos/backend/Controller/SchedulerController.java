
package com.emos.backend.Controller;

import com.emos.backend.Model.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.emos.backend.Repository.SchedulerRepository;
import com.emos.backend.Repository.JobsRepository;
import com.emos.backend.Exception.ResourceNotFoundException;
import com.emos.backend.Model.Employee;
import com.emos.backend.Repository.EmployeeRepository;
import com.emos.backend.Model.Jobs;







import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class SchedulerController {

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobsRepository jobsRepository;

    @GetMapping("/scheduler")
    public ArrayList<Scheduler> getEvents(){
        return (ArrayList<Scheduler>) schedulerRepository.findAll();
    }

    @PostMapping("/scheduler")
    public ResponseEntity<Scheduler> createEvent(@RequestBody Scheduler eventRequest){

        Jobs jobs = jobsRepository.findById(eventRequest.getJobs().getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + eventRequest.getJobs().getJobId()));

        Employee employee = employeeRepository.findById(eventRequest.getEmployee().getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + eventRequest.getEmployee().getEmpId()));


        Scheduler event = new Scheduler();
        event.setJobs(jobs);
        event.setEmployee(employee);
        event.setEventJob(eventRequest.getEventJob());
        event.setEventEmployee(eventRequest.getEventEmployee());
        event.setEventStartDate(eventRequest.getEventStartDate());
        event.setEventEndDate(eventRequest.getEventEndDate());

        Scheduler createdEvent = schedulerRepository.save(event);


//        jobs.getEvents().add(createdEvent);
        jobsRepository.save(jobs);

        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping("/scheduler/{id}")
    public ResponseEntity<Scheduler> getEventById(@PathVariable Long id){

        Scheduler scheduler = schedulerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id: " + id + " does not exist"));
        return ResponseEntity.ok(scheduler);
    }

    @PutMapping("/scheduler/{id}")
    public ResponseEntity<Scheduler> updateEvent(@PathVariable Long id,@RequestBody Scheduler eventRequest){

        Scheduler scheduler = schedulerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id: " + id + " does not exist."));

//        Jobs jobs = jobsRepository.findById(eventRequest.getJobs().getJobId())
//        		.orElseThrow(() -> new ResourceNotFoundException("Job with id: " + eventRequest.getJobs().getJobId() + " does not exist."));
//
//        scheduler.setJobs(jobs);
        scheduler.setEventJob(eventRequest.getEventJob());
        scheduler.setEventEmployee(eventRequest.getEventEmployee());
        scheduler.setEventStartDate(eventRequest.getEventStartDate());
        scheduler.setEventEndDate(eventRequest.getEventEndDate());

        Scheduler updatedEvent =  schedulerRepository.save(scheduler);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/scheduler/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteEventById(@PathVariable Long id){

        Scheduler event = schedulerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The event with id: " + id + " does not exist."));


        schedulerRepository.delete(event);

        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
