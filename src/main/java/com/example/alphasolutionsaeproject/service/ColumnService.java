package com.example.alphasolutionsaeproject.service;
import com.example.alphasolutionsaeproject.model.Column;
import com.example.alphasolutionsaeproject.repository.ColumnRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service  // Gør denne klasse til en Spring Service, som kan injiceres i andre komponenter.
public class ColumnService {

    private final ColumnRepository columnRepository;  // Definerer et repository, der håndterer databaseinteraktioner for kolonner.

    // Konstruktør til at initialisere ColumnRepository
    public ColumnService(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;  // Injicerer ColumnRepository afhængighed ved konstruktion.
    }

    // Henter alle kolonner fra databasen.
    public List<Column> getAllColumns() {
        return columnRepository.findAll();  // Kalder findAll() på ColumnRepository for at hente alle kolonner.
    }

    // Henter en enkelt kolonne baseret på dens ID.
    public Column getColumnById(int id) {
        Column column = columnRepository.findById(id);  // Kalder findById() på ColumnRepository, som returnerer en Optional.
        return column;
    }

    // Tilføjer en ny kolonne til databasen.
    public void addColumn(Column column) {
        columnRepository.save(column);  // Kalder save() på ColumnRepository for at gemme den nye kolonne.
    }

    // Opdaterer en eksisterende kolonne i databasen.
    public void updateColumn(Column column) {
        columnRepository.save(column);  // Kalder save() på ColumnRepository for at opdatere kolonnen (save() bruges også til opdatering).
    }

    // Sletter en kolonne baseret på dens ID.
    public void deleteColumn(int id) {
        columnRepository.deleteById(id);  // Kalder deleteById() på ColumnRepository for at slette kolonnen med det specifikke ID.
    }
}
