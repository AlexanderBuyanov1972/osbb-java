package com.example.osbb.dao.owner;

import com.example.osbb.entity.owner.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PhotoDAO extends JpaRepository<Photo, Long> {
    boolean existsById(long id);
}
