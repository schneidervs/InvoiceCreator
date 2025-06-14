package com.github.schneidervs.invoicecreator.repository;

import com.github.schneidervs.invoicecreator.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByName(String positionName);
}
