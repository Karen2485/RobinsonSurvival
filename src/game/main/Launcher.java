package game.main;

import game.intro.IntroVideoPlayer;
import game.ui.MainMenu;
import game.ui.fx.MainUI;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("🎮 Starting Robinson Survival...");

        // Проверяем аргументы командной строки
        if (args.length > 0) {
            switch (args[0]) {
                case "--skip-intro":
                    System.out.println("🏃 Skipping intro, starting GUI...");
                    Application.launch(MainUI.class);
                    break;
                case "--console":
                    System.out.println("🖥️ Starting console version...");
                    MainMenu consoleMenu = new MainMenu();
                    consoleMenu.show();
                    break;
                case "--console-skip":
                    System.out.println("🖥️ Skipping intro, starting console...");
                    MainMenu consoleMenu2 = new MainMenu();
                    consoleMenu2.show();
                    break;
                default:
                    System.out.println("❓ Unknown argument: " + args[0]);
                    System.out.println("Available options: --skip-intro, --console, --console-skip");
                    startWithIntro();
            }
        } else {
            // Запускаем с видео заставкой (по умолчанию GUI)
            startWithIntro();
        }
    }

    private static void startWithIntro() {
        System.out.println("🎬 Starting with intro video → GUI...");
        Application.launch(IntroVideoPlayer.class);
    }
}