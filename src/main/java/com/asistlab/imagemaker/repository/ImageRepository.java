package com.asistlab.imagemaker.repository;

import com.asistlab.imagemaker.model.Image;
import com.asistlab.imagemaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Set<Image> findAllByUser(User user);
    void deleteById(Long id);
}
