package com.asistlab.imagemaker.repository;

import com.asistlab.imagemaker.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
