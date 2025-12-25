package library;

import java.time.LocalDate;

public class Loan {

    private int id;
    private Member member;
    private Book book;
    private LocalDate issueDate; // مطابق لـ LoanDate في الداتابيز
    private LocalDate dueDate;   // مطابق لـ DueDate
    private LocalDate returnDate; // مطابق لـ ActualReturnDate في صورتك

    public Loan(int id, Member member, Book book,
                LocalDate issueDate, LocalDate dueDate) {
        this.id = id;
        this.member = member;
        this.book = book;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    // Constructor إضافي لو محتاج تعرض البيانات كاملة من الداتابيز (بما فيها تاريخ المرتجع)
    public Loan(int id, Member member, Book book,
                LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {
        this(id, member, book, issueDate, dueDate);
        this.returnDate = returnDate;
    }

    public int getId() { return id; }
    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // منطق ذكي: لو فيه تاريخ رجوع، يبقى الكتاب رجع خلاص
    public boolean isReturned() {
        return returnDate != null;
    }

    // منطق حساب التأخير
    public boolean isOverdue() {
        if (returnDate == null) {
            return LocalDate.now().isAfter(dueDate);
        } else {
            return returnDate.isAfter(dueDate);
        }
    }
}