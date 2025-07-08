package game.main;

import javafx.application.Application;

/**
 * Launcher — точка входа в игру.
 * Запускает Main.java (где интро → главное меню).
 */
public class Launcher {
    public static void main(String[] args) {
        System.out.println("🎮 Starting Robinson Survival...");

        if (args.length > 0 && args[0].equals("--skip-intro")) {
            System.out.println("🏃 Skipping intro, starting GUI...");
            Application.launch(game.ui.fx.MainUI.class); // ← MainUI должен быть Application
        } else {
            System.out.println("🎬 Starting with intro video → GUI...");
            Application.launch(game.main.Main.class); // ← это твой Main.java с интро
        }
    }
}