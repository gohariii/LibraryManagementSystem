package library;

import dao.AdminDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    private int nextUserId = 1;

    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Admin> admins = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    // ====== Books ======
    public void addBook(Book book) {
        books.add(book);
    }
    
    public int getNextUserId() {
        return nextUserId++;
    }

    public void removeBook(int bookId) {
        books.removeIf(b -> b.getId() == bookId);
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())
                    || b.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    // ====== Members & Admins ======
    public void registerMember(Member member) {
        members.add(member);
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    // ====== Loans ======
    public Loan createLoan(Member member, Book book) {
        int loanId = loans.size() + 1;
        
        // التعديل هنا: استخدمنا getAvailableCopies بدل isAvailable
        if (book.getAvailableCopies() <= 0) {
            System.out.println("Book is not available!");
            return null;
        }

        // ملاحظة: لو ميثود decreaseAvailableCopies مش موجودة، 
        // بنعدلها من الـ Setter في كلاس Book
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        
        Loan loan = new Loan(loanId, member, book,
                LocalDate.now(),
                LocalDate.now().plusDays(14));
        loans.add(loan);
        return loan;
    }

    public void returnBook(Loan loan) {
        if (loan != null && !loan.isReturned()) {
            loan.setReturnDate(LocalDate.now());
            // التعديل هنا: زيادة النسخ المتاحة عند الإرجاع
            loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        }
    }

    // ====== Reservations ======
    public Reservation createReservation(Member member, Book book) {
        int reservationId = reservations.size() + 1;
        Reservation r = new Reservation(reservationId, member, book, LocalDate.now());
        reservations.add(r);
        return r;
    }

    // ====== Login ======
    public User login(String username, String password) { 
        Admin admin = AdminDAO.loginAdmin(username, password);
        if (admin != null) {
            return admin;
        }
        return null;
    }
}