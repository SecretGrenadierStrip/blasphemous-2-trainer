package com.blasphemous2.trainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameStateReader.
 * These tests use a mock MemoryTrainer to avoid actual memory access.
 */
class GameStateReaderTest {

    private MemoryTrainer mockTrainer;
    private GameStateReader reader;

    @BeforeEach
    void setUp() {
        // Use a test double that simulates memory operations
        mockTrainer = new MemoryTrainer(12345) {
            private int health = 100;
            private int maxHealth = 150;
            private int fervour = 50;
            private int tears = 200;

            @Override
            public int readInt(long address) {
                if (address == 0x7FFE0000A4B0L) return health;
                if (address == 0x7FFE0000A4B4L) return fervour;
                if (address == 0x7FFE0000A4B8L) return tears;
                if (address == 0x7FFE0000A4C0L) return maxHealth;
                throw new IllegalArgumentException("Unknown address: " + address);
            }

            @Override
            public void writeInt(long address, int value) {
                if (address == 0x7FFE0000A4B0L) health = value;
                else if (address == 0x7FFE0000A4B4L) fervour = value;
                else if (address == 0x7FFE0000A4B8L) tears = value;
                else if (address == 0x7FFE0000A4C0L) maxHealth = value;
                else throw new IllegalArgumentException("Unknown address: " + address);
            }

            @Override
            public void close() { /* no-op */ }
        };
        reader = new GameStateReader(mockTrainer);
    }

    @Test
    void testGetHealth() {
        assertEquals(100, reader.getHealth());
    }

    @Test
    void testSetHealth() {
        reader.setHealth(75);
        assertEquals(75, reader.getHealth());
    }

    @Test
    void testGetFervour() {
        assertEquals(50, reader.getFervour());
    }

    @Test
    void testSetFervour() {
        reader.setFervour(999);
        assertEquals(999, reader.getFervour());
    }

    @Test
    void testGetTears() {
        assertEquals(200, reader.getTears());
    }

    @Test
    void testAddTears() {
        reader.addTears(500);
        assertEquals(700, reader.getTears());
    }

    @Test
    void testFillHealth() {
        reader.fillHealth();
        assertEquals(150, reader.getHealth());
    }

    @Test
    void testGetMaxHealth() {
        assertEquals(150, reader.getMaxHealth());
    }
}
