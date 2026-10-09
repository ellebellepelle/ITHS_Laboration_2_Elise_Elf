package me.ellebelle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositoryTest {

    @Test
    void addShouldAddItemToRepository() {
        // Arrange - skapar ett tomt Repository<String>
        Repository<String> repository = new Repository<>();

        // Act - utför det som jag faktiskt vill testa (min add-metod)
        repository.add("Test");

        // Assert - kontrollera att resultatet blev det jag förväntade mig.
        // Jag förväntar mig 1, och jag vill jämföra det med hur många objekt Repository faktiskt innehåller.
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findAllShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Arrange
        Repository<String> repository = new Repository<>();

        // Act
        List<String> result = repository.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnAllItems() {
        // Arrange
        Repository<String> repository = new Repository<>();
        repository.add("Ett");
        repository.add("Två");
        repository.add("Tre");

        // Act
        List<String> result = repository.findAll();

        // Assert
        assertEquals(3, result.size());
        assertEquals("Ett", result.get(0));
        assertEquals("Två", result.get(1));
        assertEquals("Tre", result.get(2));
    }
}
