package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.Position;
import com.github.schneidervs.invoicecreator.service.PositionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/positions")
@PreAuthorize("hasRole('ADMIN')")

public class PositionController {
    private static final String REDIRECT_POSITIONS = "redirect:/positions";
    private static final String POSITION_ATTR = "position";

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public String managePositions(Model model) {
        model.addAttribute("positions", positionService.findAll());
        return "positions/positions";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute(POSITION_ATTR, new Position());
        return "positions/new-position";
    }

    @PostMapping("/save")
    public String createPosition(@ModelAttribute(POSITION_ATTR) Position position, Model model) {
        if (positionService.positionExists(position.getName())) {
            model.addAttribute("errorMessage", "Position with name '" + position.getName() + "' already exists.");
            model.addAttribute(POSITION_ATTR, position);
            return "positions/new-position";
        }
        positionService.create(position);
        return REDIRECT_POSITIONS;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Position position = positionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position Id:" + id));
        model.addAttribute(POSITION_ATTR, position);
        return "positions/edit-position";
    }

    @PostMapping("/update")
    public String updatePosition(
            @RequestParam(value = "id", required = false) Long id,
            @ModelAttribute(POSITION_ATTR) Position updatedPosition) {

        Position existingPosition = positionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position Id:" + id));

        existingPosition.setName(updatedPosition.getName());
        positionService.update(existingPosition);
        return REDIRECT_POSITIONS;
    }

    @PostMapping("/delete/{id}")
    public String deletePosition(@PathVariable Long id) {
        positionService.deleteById(id);
        return REDIRECT_POSITIONS;
    }
}
