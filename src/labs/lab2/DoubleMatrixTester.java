package src.labs.lab2;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

class InsufficientElementsException extends Exception {
    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException() {
        super("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException() {
        super("Invalid column number");
    }
}

class DoubleMatrix {
    //    private double a[];
    private final int m;
    private final int n;

    private final double[][] matrix;

    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
//        this.a = a;
        this.m = m;
        this.n = n;
        if (a.length < m * n) {
            throw new InsufficientElementsException();
        }
        matrix = new double[m][n];
        int counter = a.length - m * n;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = a[counter++];
            }
        }
    }

    public String getDimensions() {
        return "[" + m + " x " + n + "]";
    }

    public int rows() {
        return m;
    }

    public int columns() {
        return n;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if (row < 1 || row > m) {
            throw new InvalidRowNumberException();
        }
        return Arrays.stream(matrix[row - 1])
                .max()
                .orElseThrow();
    }

    public double maxElementAtColumn(int col) throws InvalidColumnNumberException {
        if (col < 1 || col > n) {
            throw new InvalidColumnNumberException();
        }
        return Arrays.stream(matrix)
                .map(row -> row[col - 1])
                .max((o1, o2) -> Double.compare(o1, o2)).orElseThrow();
    }

    public double sum() {
        return Arrays.stream(matrix)
                .map(arr -> Arrays.stream(arr).sum())
                .reduce(0.0, (left, right) -> left + right);
    }

    public double[] toSortedArray() {
        return Arrays.stream(matrix)
                .flatMapToDouble(line -> Arrays.stream(line))
                .boxed()
                .sorted((a, b) -> Double.compare(b, a))
                .mapToDouble(it -> it)
                .toArray();
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                sb.append(df.format(matrix[i][j])).append("\t");
            }
            sb.append(df.format(matrix[i][n - 1]));
            if (i != m - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleMatrix)) return false;
        return Arrays.deepEquals(matrix, ((DoubleMatrix) o).matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m, n);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws IOException, InsufficientElementsException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));

        String[] parts = bf.readLine().split("\\s+");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        String line;
        double[] array = new double[row * col];
        int i = 0;
        while ((line = bf.readLine()) != null) {
            parts = line.split("\\s+");
            for (String part:parts ) {
                array[i++] = Double.parseDouble(part);
            }
        }
        return new DoubleMatrix(array, row, col);

    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
