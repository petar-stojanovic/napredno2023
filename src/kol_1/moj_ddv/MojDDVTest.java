package src.kol_1.moj_ddv;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum Type {
    A, B, V
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

class Article {
    private int price;
    private Type type;

    public Article(int price, Type type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }
}

class Bill {
    private Long id;
    private List<Article> articles;

    static final double TAX_RETURN_PERCENTAGE = 0.15;

    public Bill(Long id) {
        this.id = id;
        articles = new ArrayList<>();
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public double sum() {
        return articles.stream().mapToDouble(it -> it.getPrice()).sum();
    }

    public double tax() {
        return articles.stream().mapToDouble(it -> {
            double price = it.getPrice();
            if (it.getType() == Type.A) {
                return price * 0.18 * TAX_RETURN_PERCENTAGE;
            } else if (it.getType() == Type.B) {
                return price * 0.05 * TAX_RETURN_PERCENTAGE;
            }
            return 0;
        }).sum();
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%10.5f", id, (int) sum(), tax());
    }

}

class BillGenerator {
    public static Bill createBill(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        Bill bill = new Bill(Long.parseLong(parts[0]));

        List<Article> articles = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            Article article = new Article(Integer.parseInt(parts[i]), Type.valueOf(parts[i + 1]));

            articles.add(article);
        }

        bill.setArticles(articles);
        if (bill.sum() > 30000) {
            throw new AmountNotAllowedException(String.format("Receipt with amount %d is not allowed to be scanned"
                          , (int) bill.sum()));
        }

        return bill;
    }


}

class MojDDV {

    List<Bill> bills;

    public MojDDV() {
        this.bills = new ArrayList<>();
    }

    void readRecords(InputStream inputStream) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        bills = bf.lines()
                      .map(line -> {
                          try {
                              return BillGenerator.createBill(line);
                          } catch (AmountNotAllowedException e) {
                              System.out.println(e.getMessage());
                              return null;
                          }
                      }).filter(Objects::nonNull)
                      .collect(Collectors.toList());
        bf.close();
    }

    void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        bills.forEach(pw::println);
        pw.flush();
    }

    void printStatistics(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        DoubleSummaryStatistics dss = bills.stream().mapToDouble(Bill::tax).summaryStatistics();

        pw.printf("min:\t%2.3f\n",dss.getMin());
        pw.printf("max:\t%2.3f\n",dss.getMax());
        pw.printf("sum:\t%2.3f\n",dss.getSum());
        pw.printf("count:\t%d\n",dss.getCount());
        pw.printf("avg:\t%2.3f\n",dss.getAverage());


        pw.flush();
    }

}

public class MojDDVTest {

    public static void main(String[] args) throws IOException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}