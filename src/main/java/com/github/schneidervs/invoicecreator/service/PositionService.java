package com.github.schneidervs.invoicecreator.service;

import com.github.schneidervs.invoicecreator.model.Position;
import com.github.schneidervs.invoicecreator.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {
    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    public Optional<Position> findById(Long id) {
        return positionRepository.findById(id);
    }
}
