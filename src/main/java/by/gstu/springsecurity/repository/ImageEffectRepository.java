package by.gstu.springsecurity.repository;

import by.gstu.springsecurity.model.Effect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageEffectRepository extends JpaRepository<Effect, Long> {
}
