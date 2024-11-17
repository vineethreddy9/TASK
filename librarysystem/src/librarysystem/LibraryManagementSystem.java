package librarysystem;

import java.util.*;
class Book {
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int quantity;

    public Book(String title, String author, String isbn, String genre, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.quantity = quantity;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getGenre() { return genre; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void updateInfo(String title, String author, int quantity) {
        if (title != null) this.title = title;
        if (author != null) this.author = author;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Genre: " + genre + ", Quantity: " + quantity;
    }
}

class Borrower {
    private String name;
    private String contact;
    private String membershipId;

    public Borrower(String name, String contact, String membershipId) {
        this.name = name;
        this.contact = contact;
        this.membershipId = membershipId;
    }

    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getMembershipId() { return membershipId; }

    public void updateInfo(String name, String contact) {
        if (name != null) this.name = name;
        if (contact != null) this.contact = contact;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Contact: " + contact + ", Membership ID: " + membershipId;
    }
}

class Library {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Borrower> borrowers = new HashMap<>();
    private Map<String, Map<String, String>> borrowedBooks = new HashMap<>(); // membershipId -> (isbn, dueDate)

    public void addBook(String title, String author, String isbn, String genre, int quantity) {
        if (books.containsKey(isbn)) {
            books.get(isbn).setQuantity(books.get(isbn).getQuantity() + quantity);
            System.out.println("Book quantity updated.");
        } else {
            books.put(isbn, new Book(title, author, isbn, genre, quantity));
            System.out.println("Book added successfully.");
        }
    }

    public void updateBook(String isbn, String title, String author, int quantity) {
        if (books.containsKey(isbn)) {
            books.get(isbn).updateInfo(title, author, quantity);
            System.out.println("Book updated successfully.");
        } else {
            System.out.println("Book with ISBN " + isbn + " not found.");
        }
    }

    public void removeBook(String isbn) {
        if (books.remove(isbn) != null) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book with ISBN " + isbn + " not found.");
        }
    }

    public void addBorrower(String name, String contact, String membershipId) {
        if (borrowers.containsKey(membershipId)) {
            System.out.println("Borrower already exists.");
        } else {
            borrowers.put(membershipId, new Borrower(name, contact, membershipId));
            System.out.println("Borrower added successfully.");
        }
    }

    public void updateBorrower(String membershipId, String name, String contact) {
        if (borrowers.containsKey(membershipId)) {
            borrowers.get(membershipId).updateInfo(name, contact);
            System.out.println("Borrower updated successfully.");
        } else {
            System.out.println("Borrower with Membership ID " + membershipId + " not found.");
        }
    }

    public void removeBorrower(String membershipId) {
        if (borrowers.remove(membershipId) != null) {
            System.out.println("Borrower removed successfully.");
        } else {
            System.out.println("Borrower with Membership ID " + membershipId + " not found.");
        }
    }

    public void borrowBook(String membershipId, String isbn, int days) {
        if (!borrowers.containsKey(membershipId)) {
            System.out.println("Borrower not found.");
            return;
        }
        if (!books.containsKey(isbn)) {
            System.out.println("Book not found.");
            return;
        }
        Book book = books.get(isbn);
        if (book.getQuantity() <= 0) {
            System.out.println("Book is currently unavailable.");
            return;
        }
        book.setQuantity(book.getQuantity() - 1);
        String dueDate = java.time.LocalDate.now().plusDays(days).toString();
        borrowedBooks.put(membershipId, Map.of(isbn, dueDate));
        System.out.println("Book borrowed successfully. Due date: " + dueDate);
    }

    public void returnBook(String membershipId) {
        if (borrowedBooks.containsKey(membershipId)) {
            Map<String, String> borrowDetails = borrowedBooks.get(membershipId);
            String isbn = borrowDetails.keySet().iterator().next();
            if (books.containsKey(isbn)) {
                books.get(isbn).setQuantity(books.get(isbn).getQuantity() + 1);
            }
            borrowedBooks.remove(membershipId);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("No borrowed books found for Membership ID " + membershipId + ".");
        }
    }

    public void searchBooks(String query, String searchBy) {
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            switch (searchBy.toLowerCase()) {
                case "title":
                    if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                    break;
                case "author":
                    if (book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                    break;
                case "genre":
                    if (book.getGenre().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                    break;
                default:
                    System.out.println("Invalid search criteria.");
                    return;
            }
        }

        if (results.isEmpty()) {
            System.out.println("No books found matching the query.");
        } else {
            System.out.println("Search Results:");
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. Add Borrower");
            System.out.println("5. Update Borrower");
            System.out.println("6. Remove Borrower");
            System.out.println("7. Borrow Book");
            System.out.println("8. Return Book");
            System.out.println("9. Search Books");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    library.addBook(title, author, isbn, genre, quantity);
                }
                case 2 -> {
                    System.out.print("Enter ISBN to update: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter new title (or press Enter to skip): ");
                    String title = scanner.nextLine();
                    System.out.print("Enter new author (or press Enter to skip): ");
                    String author = scanner.nextLine();
                    System.out.print("Enter new quantity: ");
                    int quantity = scanner.nextInt();
                    library.updateBook(isbn, title.isEmpty() ? null : title, author.isEmpty() ? null : author, quantity);
                }
                case 3 -> {
                    System.out.print("Enter ISBN to remove: ");
                    String isbn = scanner.nextLine();
                    library.removeBook(isbn);
                }
                case 4 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter contact: ");
                    String contact = scanner.nextLine();
                    System.out.print("Enter membership ID: ");
                    String membershipId = scanner.nextLine();
                    library.addBorrower(name, contact, membershipId);
                }
                case 5 -> {
                    System.out.print("Enter membership ID to update: ");
                    String membershipId = scanner.nextLine();
                    System.out.print("Enter new name (or press Enter to skip): ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new contact (or press Enter to skip): ");
                    String contact = scanner.nextLine();
                    library.updateBorrower(membershipId, name.isEmpty() ? null : name, contact.isEmpty() ? null : contact);
                }
                case 6 -> {
                    System.out.print("Enter membership ID to remove: ");
                    String membershipId = scanner.nextLine();
                    library.removeBorrower(membershipId);
                }
                case 7 -> {
                    System.out.print("Enter membership ID: ");
                    String membershipId = scanner.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter number of days to borrow: ");
                    int days = scanner.nextInt();
                    library.borrowBook(membershipId, isbn, days);
                }
                case 8 -> {
                    System.out.print("Enter membership ID: ");
                    String membershipId = scanner.nextLine();
                    library.returnBook(membershipId);
                }
                case 9 -> {
                    System.out.println("Search by: 1. Title  2. Author  3. Genre");
                    System.out.print("Enter your choice: ");
                    int searchChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    String searchBy = switch (searchChoice) {
                        case 1 -> "title";
                        case 2 -> "author";
                        case 3 -> "genre";
                        default -> {
                            System.out.println("Invalid choice.");
                            yield null;
                        }
                    };
                    if (searchBy != null) {
                        System.out.print("Enter search query: ");
                        String query = scanner.nextLine();
                        library.searchBooks(query, searchBy);
                    }
                }
                case 10 -> {
                    running = false;
                    System.out.println("Exiting Library Management System. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}