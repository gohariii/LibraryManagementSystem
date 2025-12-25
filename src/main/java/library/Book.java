package library;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private Category category;
    private int publishedYear;
    private int totalCopies;
    private int availableCopies;
    private int reservedCount;

    public Book(int id, String title, String author, String isbn, Category category, 
                int publishedYear, int totalCopies, int availableCopies, int reservedCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.publishedYear = publishedYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.reservedCount = reservedCount;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public Category getCategory() { return category; }
    public int getPublishedYear() { return publishedYear; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public int getReservedCount() { return reservedCount; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
}