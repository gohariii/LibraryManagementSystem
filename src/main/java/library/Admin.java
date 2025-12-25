package library;

// Admin يرث من User
public class Admin extends User {

    private String role; // مثال: "Admin"

    public Admin(int id, String name, String email, String password, String role) {
        super(id, name, email, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void showMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1- Add Book");
        System.out.println("2- Update Book");
        System.out.println("3- Delete Book");
        System.out.println("4- List Books");
    }

    // ميثود بسيطة placeholder (هتتطبق فعليًا في LibrarySystem)
    public void generateReport() {
        System.out.println("Generating simple report...");
    }
}

