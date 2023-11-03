package com.emos.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.emos.backend.Model.Jobs;
import org.springframework.stereotype.Repository;

public interface JobsRepository extends JpaRepository <Jobs, Long> {

}
