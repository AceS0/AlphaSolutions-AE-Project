package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Column;
import com.example.alphasolutionsaeproject.service.ColumnService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("projects/id/subprojects/subid/columns")
public class ColumnController {

    private final ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    // 1. Vis alle columns
    @GetMapping
    public String listColumns(Model model) {
        List<Column> columns = columnService.getAllColumns();
        model.addAttribute("columns", columns);
        return "columns"; // Thymeleaf side: columns.html
    }

    // 2. Vis form for at tilføje column
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("column", new Column());
        return "addColumn"; // Thymeleaf side: addColumn.html
    }

    // 3. Gem ny column
    @PostMapping("/add")
    public String addColumn(@ModelAttribute Column column) {
        columnService.addColumn(column);
        return "redirect:/columns";
    }

    // 4. Vis form for at redigere en column
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Column column = columnService.getColumnById(id);
        model.addAttribute("column", column);
        return "editColumn"; // Thymeleaf side: editColumn.html
    }

    // 5. Gem ændringer på eksisterende column
    @PostMapping("/edit/{id}")
    public String editColumn(@PathVariable int id, @ModelAttribute Column column) {
        column.setId(id);
        columnService.updateColumn(column);
        return "redirect:/columns";
    }

    // 6. Slet column
    @GetMapping("/delete/{id}")
    public String deleteColumn(@PathVariable int id) {
        columnService.deleteColumn(id);
        return "redirect:/columns";
    }
}
