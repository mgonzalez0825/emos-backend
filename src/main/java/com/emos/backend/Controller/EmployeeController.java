package com.emos.backend.Controller;
import java.util.*;

import com.emos.backend.Exception.ResourceNotFoundException;
import com.emos.backend.Model.Department;
import com.emos.backend.Model.Employee;
import com.emos.backend.Model.EmployeeDetails;
import com.emos.backend.Repository.DepartmentRepository;
import com.emos.backend.Repository.EmployeeRepository;
import com.emos.backend.Repository.EmployeeDetailsRepository;
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
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    // getAllEmployees rest

    @GetMapping("/employees")
    public ArrayList<Employee> getAllEmployees(){
        return (ArrayList<Employee>) employeeRepository.findAll();
    }

    //create employee API


    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeRequest){
        Department department = departmentRepository.findById(employeeRequest.getDepartment().getDeptNo())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeRequest.getDepartment().getDeptNo()));


        Employee employee = new Employee();
        employee.setDepartment(department); // Set the department on the employee object
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setEmailId(employeeRequest.getEmailId());
        employee.setPassword(employeeRequest.getPassword());

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setSsn(employeeRequest.getEmployeeDetails().getSsn());
        employeeDetails.setEmployeeType(employeeRequest.getEmployeeDetails().getEmployeeType());
        employeeDetails.setPhoneNumber(employeeRequest.getEmployeeDetails().getPhoneNumber());
        employeeDetails.setSupervisor(employeeRequest.getEmployeeDetails().isSupervisor());
        employeeDetails.setSalary(employeeRequest.getEmployeeDetails().getSalary());
        employeeDetails.setPosition(employeeRequest.getEmployeeDetails().getPosition());
        employeeDetails.setPayRate(employeeRequest.getEmployeeDetails().getPayRate());
        employeeDetails.setHireDate(employeeRequest.getEmployeeDetails().getHireDate());
        employeeDetails.setEmployee(employee);

        employee.setEmployeeDetails(employeeDetails);
        Employee createdEmployee = employeeRepository.save(employee);

        department.getEmployees().add(createdEmployee);
        departmentRepository.save(department);

        return ResponseEntity.ok(createdEmployee);
    }






    //get employeeById API

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id ){



        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + id + " does not exists"));
                return ResponseEntity.ok(employee);
    }




    //update employee
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeRequest) {

    Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        Department department = departmentRepository.findById(employeeRequest.getDepartment().getDeptNo())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeRequest.getDepartment().getDeptNo()));

        employee.setDepartment(department);
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setEmailId(employeeRequest.getEmailId());
        employee.setPassword(employeeRequest.getPassword());

        EmployeeDetails existingDetails = employeeRequest.getEmployeeDetails();
        existingDetails.setSsn(employeeRequest.getEmployeeDetails().getSsn());
        existingDetails.setPhoneNumber(employeeRequest.getEmployeeDetails().getPhoneNumber());
        existingDetails.setPosition(employeeRequest.getEmployeeDetails().getPosition());
        existingDetails.setSalary(employeeRequest.getEmployeeDetails().getSalary());
        existingDetails.setPayRate(employeeRequest.getEmployeeDetails().getPayRate());
        existingDetails.setSupervisor(employeeRequest.getEmployeeDetails().isSupervisor());
        existingDetails.setEmployeeType(employeeRequest.getEmployeeDetails().getEmployeeType());
        existingDetails.setHireDate(employeeRequest.getEmployeeDetails().getHireDate());
        existingDetails.setEmployee(employee);



        Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }


    //edit employee details
    @PutMapping("/employees/add-details/{id}")
    public ResponseEntity<Employee> updateEmployeeDetails(@PathVariable Long id, @RequestBody EmployeeDetails employeeDetailsRequest) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        EmployeeDetails existingDetails = employee.getEmployeeDetails();
        existingDetails.setSsn(employeeDetailsRequest.getSsn());
        existingDetails.setPhoneNumber(employeeDetailsRequest.getPhoneNumber());
        existingDetails.setPosition(employeeDetailsRequest.getPosition());
        existingDetails.setSalary(employeeDetailsRequest.getSalary());
        existingDetails.setPayRate(employeeDetailsRequest.getPayRate());
        existingDetails.setSupervisor(employeeDetailsRequest.isSupervisor());
        existingDetails.setEmployeeType(employeeDetailsRequest.getEmployeeType());
        existingDetails.setHireDate(employeeDetailsRequest.getHireDate());

        employee.setEmployeeDetails(existingDetails);
        Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    //delete employee API
    @DeleteMapping("/employees/{id}")
    public ResponseEntity< Map<String,Boolean>> deleteEmployeeById(@PathVariable Long id){

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + id + " does not exists"));

        employeeRepository.delete(employee);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
