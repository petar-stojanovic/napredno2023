package src.kol_1.moj_ddv_vtora;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum FiscalType {
    A, B, V
}

class Article {
    int price;
    FiscalType type;

    public Article(int price, FiscalType type) {
        this.price = price;
        this.type = type;
    }

    public double calculateTax() {
        if (type.equals(FiscalType.A)) {
            return price * 0.18 * 0.15;
        } else if (type.equals(FiscalType.B)) {
            return price * 0.05 * 0.15;
        } else {
            return 0;
        }
    }
}

class Fiscal {
    private Long id;
    private List<Article> articles;

    public Fiscal(Long id, List<Article> articles) {
        this.id = id;
        this.articles = articles;
    }

    //12334 1789 А 1238 B 1222 V 111 V
    public static Fiscal create(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        List<Article> articleList = new ArrayList<>();
        Long id = Long.parseLong(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            int price = Integer.parseInt(parts[i++]);
            FiscalType ft = FiscalType.valueOf(parts[i]);
            articleList.add(new Article(price, ft));
        }

        int totalPrice = (int) articleList.stream().mapToDouble(it -> it.price).sum();
        if (totalPrice > 30000) {
            throw new AmountNotAllowedException(totalPrice);
        }

        return new Fiscal(id, articleList);

    }

    public int calculateSum() {
        return (int) articles.stream().mapToDouble(it -> it.price).sum();
    }

    public double calculateTax() {
        return articles.stream().mapToDouble(it -> it.calculateTax()).sum();
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%10.5f", id, calculateSum(), calculateTax());
    }

}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

class MojDDV {

    private List<Fiscal> fiscals;

    public MojDDV() {
        this.fiscals = new ArrayList<>();
    }

    void readRecords(InputStream inputStream) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));

        List<Fiscal> fiscalList = bf.lines()
                          .map(it -> {
                              try {
                                  return Fiscal.create(it);
                              } catch (AmountNotAllowedException e) {
                                  System.out.println(e.getMessage());
                                  return null;
                              }
                          }).filter(Objects::nonNull)
                          .collect(Collectors.toList());
        fiscals.addAll(fiscalList);
        bf.close();
    }

    void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        fiscals.forEach(it -> pw.println(it));

        pw.flush();
    }

    public void printStatistics(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        DoubleSummaryStatistics dss = fiscals.stream().mapToDouble(it -> it.calculateTax()).summaryStatistics();

        pw.format("min:\t%2.3f\nmax:\t%2.3f\nsum:\t%2.3f\ncount:\t%d\navg:\t%2.3f",
                          dss.getMin(),
                          dss.getMax(),
                          dss.getSum(),
                          dss.getCount(),
                          dss.getAverage());

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