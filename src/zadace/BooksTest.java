package src.zadace;


import java.util.*;


public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде
class Book {


    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class TitleComparator implements Comparator<Book> {

    @Override
    public int compare(Book o1, Book o2) {
        int result = o1.getTitle().compareTo(o2.getTitle());
        if (result != 0) {
            return result;
        }
        return Float.compare(o1.getPrice(), o2.getPrice());
    }
}

class PriceComparator implements Comparator<Book> {

    @Override
    public int compare(Book o1, Book o2) {
        int result = Float.compare(o1.getPrice(), o2.getPrice());
        if (result != 0) {
            return result;
        }
        return o1.getTitle().compareTo(o2.getTitle());
    }
}

class BookCollection {

    private static TreeSet<Book> books;

    public BookCollection() {
        books = new TreeSet<>(new PriceComparator());
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void printByCategory(String category) {
        Set<Book> booksByTitle = new TreeSet<>(new TitleComparator());
        booksByTitle.addAll(books);
        for (Book book : booksByTitle) {
            if (book.getCategory().equals(category)) {
                System.out.println(book);
            }
        }
    }


    public List<Book> getCheapestN(int n) {
        List<Book> firstNBooks = new ArrayList<>(n);
        int i = 0;
        for (Book book : books) {
            if (i < n) {
                firstNBooks.add(book);
            }
            i++;
        }
        return firstNBooks;
    }


}