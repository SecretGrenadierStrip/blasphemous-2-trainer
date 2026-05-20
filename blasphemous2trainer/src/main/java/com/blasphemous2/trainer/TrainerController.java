package com.blasphemous2.trainer;

import java.util.Scanner;

/**
 * Console-based controller for the Blasphemous 2 Trainer.
 * Provides a simple menu interface for toggling cheats.
 */
public class TrainerController {

    private final GameStateReader stateReader;
    private final MemoryTrainer trainer;

    /**
     * Initializes the controller with a game state reader.
     * @param stateReader The GameStateReader instance.
     * @param trainer The MemoryTrainer instance.
     */
    public TrainerController(GameStateReader stateReader, MemoryTrainer trainer) {
        this.stateReader = stateReader;
        this.trainer = trainer;
    }

    /**
     * Starts the interactive console menu.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("=== Blasphemous 2 Trainer ===");
        System.out.println("Connected to PID: " + trainer.getPid());

        while (running) {
            System.out.println("\nOptions:");
            System.out.println("1. Show current stats");
            System.out.println("2. Fill health to max");
            System.out.println("3. Set fervour to 999");
            System.out.println("4. Add 5000 tears");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input.trim());
                switch (choice) {
                    case 1 -> displayStats();
                    case 2 -> stateReader.fillHealth();
                    case 3 -> stateReader.setFervour(999);
                    case 4 -> stateReader.addTears(5000);
                    case 5 -> running = false;
                    default -> System.out.println("Invalid choice. Enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Shutting down trainer...");
        trainer.close();
        scanner.close();
    }

    private void displayStats() {
        int hp = stateReader.getHealth();
        int maxHp = stateReader.getMaxHealth();
        int fervour = stateReader.getFervour();
        int tears = stateReader.getTears();
        System.out.println("Health: " + hp + "/" + maxHp);
        System.out.println("Fervour: " + fervour);
        System.out.println("Tears: " + tears);
    }

    /**
     * Main entry point for the trainer application.
     * @param args Command-line arguments: expects the game's PID as first argument.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java TrainerController <pid>");
            System.exit(1);
        }
        int pid;
        try {
            pid = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid PID: " + args[0]);
            System.exit(1);
            return;
        }
        MemoryTrainer trainer = new MemoryTrainer(pid);
        GameStateReader reader = new GameStateReader(trainer);
        TrainerController controller = new TrainerController(reader, trainer);
        controller.start();
    }
}
