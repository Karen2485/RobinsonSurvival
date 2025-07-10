package game.logic;

public class SurvivalStatus {

    private int daysSurvived;      // Сколько дней игрок уже выжил
    private int daysWithoutFood;   // Сколько дней подряд без еды

    public SurvivalStatus() {
        this.daysSurvived = 0;
        this.daysWithoutFood = 0;
    }

    public int getDaysSurvived() {
        return daysSurvived;
    }

    public void setDaysSurvived(int days) {
        this.daysSurvived = days;
    }

    public int getDaysWithoutFood() {
        return daysWithoutFood;
    }

    public void setDaysWithoutFood(int daysWithoutFood) {
        this.daysWithoutFood = daysWithoutFood;
    }

    /**
     * Вызывается в конце каждого игрового дня.
     * @param ateFood true, если игрок ел в этот день, иначе false.
     */
    public void passDay(boolean ateFood) {
        daysSurvived++;
        if (ateFood) {
            daysWithoutFood = 0;
        } else {
            daysWithoutFood++;
        }
    }

    /**
     * Проверка, умер ли персонаж от голода.
     * @return true, если дней без еды >= 3
     */
    public boolean isDeadFromStarvation() {
        return daysWithoutFood >= 3;
    }
}
