package library;

import java.time.LocalDate;

// حجز كتاب عندما لا يكون متوفر
public class Reservation {

    private int id;
    private Member member;
    private Book book;
    private LocalDate reservationDate;
    private String status; // "PENDING" أو "COMPLETED" أو "CANCELLED"

    public Reservation(int id, Member member, Book book, LocalDate reservationDate) {
        this.id = id;
        this.member = member;
        this.book = book;
        this.reservationDate = reservationDate;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void completeReservation() {
        this.status = "COMPLETED";
    }

    public void cancelReservation() {
        this.status = "CANCELLED";
    }
}
