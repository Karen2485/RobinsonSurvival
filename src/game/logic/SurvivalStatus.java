package game.logic;

// Этот класс отвечает за отслеживание голода персонажа и проверку на смерть от голода
public class SurvivalStatus {
    private int daysWithoutFood;  // Сколько дней игрок прожил без еды

    public SurvivalStatus() {
        this.daysWithoutFood = 0;  // Изначально персонаж не голоден
    }

    /**
     * Вызывается в конце каждого дня.
     * @param foundFood - true, если игрок ел в этот день, false - если не ел
     */
    public void passDay(boolean foundFood) {
        if (foundFood) {
            daysWithoutFood = 0;  // Если еда найдена — сбрасываем счётчик
        } else {
            daysWithoutFood++;    // Если не было еды — увеличиваем счётчик голода
        }

        // Проверка: умер ли персонаж от голода
        if (daysWithoutFood >= 3) {
            System.out.println("Your character has died from starvation.");  // Сообщение о смерти
            System.exit(0);  // Завершение программы
        } else {
            System.out.println("Day passed. Days without food: " + daysWithoutFood);
        }
    }
}