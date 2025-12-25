package library;

public class Category {
    private int id;
    private String name;
    private String description;

    // Constructor بـ 3 خانات
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // 🟢 Constructor بـ 2 خانة عشان يحل إيرور الـ BookDAO
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.description = "";
    }

    @Override
    public String toString() { return name; }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}