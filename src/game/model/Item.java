package game.model;

// Класс описывает любой предмет в игре
public class Item {
    private final String name;
    private final String description;

    public Item(String name, String description) {
        if (name == null || description == null) {
            throw new IllegalArgumentException("Name and description cannot be null");
        }
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}