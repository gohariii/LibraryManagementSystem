package library;

// Member يرث من User
public class Member extends User {

    private String username; // ضفنا ده عشان موجود في جدولك
    private int maxBorrowedBooks; 

    // الكونستركتور المعدل عشان يطابق البيانات اللي بنجيبها من الـ DAO
    public Member(int id, String name, String username, String email) {
        // بنبعت لـ super البيانات الأساسية (id, name, email, password)
        // بنحط Password افتراضي "123" حالياً
        super(id, name, email, "123"); 
        this.username = username;
        this.maxBorrowedBooks = 5; // قيمة افتراضية من جدولك
    }

    // الـ Getter للـ Username عشان نحتاجه في الجدول
    public String getUsername() {
        return username;
    }

    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    @Override
    public void showMenu() {
        System.out.println("=== Member Menu ===");
        System.out.println("1- Search Book");
        System.out.println("2- Request Loan");
    }
}