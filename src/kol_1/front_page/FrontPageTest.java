package src.kol_1.front_page;

import java.util.*;
import java.util.stream.Collectors;

class Category {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

abstract class NewsItem {
    private String title;
    private Date datePublished;
    private Category category;

    public NewsItem(String title, Date datePublished, Category category) {
        this.title = title;
        this.datePublished = datePublished;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public Category getCategory() {
        return category;
    }

    abstract String getTeaser();

    public int when() {
        Date now = new Date();
        long ms = now.getTime() - datePublished.getTime();
        return (int) (ms / 1000 / 60);

    }
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String title, Date datePublished, Category category, String text) {
        super(title, datePublished, category);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    String getTeaser() {
        String text = getText();
        if (getText().length() > 80) {
            text = getText().substring(0, 80);
        }
        return String.format("%s\n%d\n%s\n", getTitle(), when(), text);
    }

}

class MediaNewsItem extends NewsItem {
    private String url;
    private int numViews;

    public MediaNewsItem(String title, Date datePublished, Category category, String url, int numViews) {
        super(title, datePublished, category);
        this.url = url;
        this.numViews = numViews;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumViews() {
        return numViews;
    }

    public void setNumViews(int numViews) {
        this.numViews = numViews;
    }

    @Override
    String getTeaser() {
        return String.format("%s\n%d\n%s\n%d\n", getTitle(), when(), getUrl(), getNumViews());
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String cat) {
        super(String.format("Category %s was not found", cat));
    }
}

class FrontPage {
    private List<NewsItem> news;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        news = new ArrayList<>();
        this.categories = categories;
    }

    public List<NewsItem> getNews() {
        return news;
    }

    public void setNews(List<NewsItem> news) {
        this.news = news;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    void addNewsItem(NewsItem newsItem) {
        news.add(newsItem);
    }

    List<NewsItem> listByCategory(Category category) {
        return news.stream().filter(it -> it.getCategory().equals(category)).collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Category cat = Arrays.stream(categories)
                          .filter(it -> it.getName().equals(category))
                          .findFirst().orElseThrow(() -> new CategoryNotFoundException(category));
        List<NewsItem> newsItems =
                          news.stream()
                                            .filter(it -> it.getCategory().equals(cat))
                                            .collect(Collectors.toList());
        return newsItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        news.forEach(it -> sb.append(it.getTeaser()));

        return sb.toString();
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Vasiot kod ovde