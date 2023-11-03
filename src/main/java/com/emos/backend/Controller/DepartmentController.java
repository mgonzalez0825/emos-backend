
package com.emos.backend.Controller;

import com.emos.backend.Exception.ResourceNotFoundException;
import com.emos.backend.Model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.emos.backend.Repository.DepartmentRepository;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class DepartmentController {


    @Autowired
    private DepartmentRepository departmentRepository;


    // getAllDepts API

    @GetMapping("/departments")
    public ArrayList<Department> getDepartments(){
        return (ArrayList<Department>) departmentRepository.findAll();
    }

    //create department API

    @PostMapping("/departments")
    public Department createDepartment(@RequestBody Department department){
        return  departmentRepository.save(department);
    }

    // get departmentById API

    @GetMapping("/departments/{deptNo}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long deptNo){

        Department department = departmentRepository.findById(deptNo)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + deptNo + " does not exists"));
        return ResponseEntity.ok(department);
    }
    //
//    //update department  API
//
    @PutMapping("/departments/{deptNo}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long deptNo,@RequestBody Department departmentAttributes){

        Department department = departmentRepository.findById(deptNo)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id: " + deptNo + " does not exists"));

        department.setDeptName(departmentAttributes.getDeptName());
        department.setParentDept(departmentAttributes.getParentDept());
        department.setLocation(departmentAttributes.getLocation());
        department.setManagerNo(departmentAttributes.getManagerNo());

        Department updatedDepartment =  departmentRepository.save(department);
        return ResponseEntity.ok(updatedDepartment);
    }
    //
    //delete department API
    @DeleteMapping("/departments/{deptNo}")
    public ResponseEntity<Map<String,Boolean>> deleteDepartmentById(@PathVariable Long deptNo){

        Department department = departmentRepository.findById(deptNo)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + deptNo + " does not exists"));


        departmentRepository.delete(department);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
