package src.kol_2.zadnji_20;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    String title;
    int[] ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public double averageRating() {
        return Arrays.stream(ratings).average().orElse(0);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, averageRating(), ratings.length);
    }

    public double ratingCoefficient(int maximumRatingCount) {
        return (averageRating() * ratings.length) / maximumRatingCount;
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        Movie movie = new Movie(title, ratings);
        movies.add(movie);
    }

    public List<Movie> top10ByAvgRating() {
        Comparator<Movie> comparator =
                      Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle);

        return movies.stream().sorted(comparator).limit(10).collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int maximumRatingCount = findMaximumRatingCount();
        Comparator<Movie> comparator = Comparator.comparing((Movie it) -> it.ratingCoefficient(maximumRatingCount),
                      Comparator.reverseOrder()).thenComparing(Movie::getTitle);
        return movies.stream().sorted(comparator)
                      .limit(10)
                      .collect(Collectors.toList());
    }

    private int findMaximumRatingCount() {
        int i = movies.get(0).ratings.length;

        for (Movie m : movies) {
            if (i > m.ratings.length) {
                i = m.ratings.length;
            }
        }
        return i;
    }
}

// 24
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
