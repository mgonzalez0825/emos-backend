package com.emos.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emos.backend.Model.Scheduler;




@Repository
public interface SchedulerRepository extends JpaRepository <Scheduler, Long> {

}
