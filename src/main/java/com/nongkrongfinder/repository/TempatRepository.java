package com.nongkrongfinder.repository;

import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TempatRepository extends JpaRepository<Tempat, Long>, JpaSpecificationExecutor<Tempat> {
    List<Tempat> findByUser(User user);
}
