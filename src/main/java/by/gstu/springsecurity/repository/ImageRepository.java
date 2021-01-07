package by.gstu.springsecurity.repository;

import by.gstu.springsecurity.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
