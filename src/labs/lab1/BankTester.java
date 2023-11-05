package src.labs.lab1;

import java.util.*;
import java.util.stream.Collectors;

class Account {
    private String name;
    private Long id;
    private String balance;


    public Account(String name, String balance) {
        this.id = new Random().nextLong();
        this.name = name;
        this.balance = balance;
    }

    private double parseStringToDouble(String str) {
        return Double.parseDouble(str.substring(0, str.length() - 1));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        return Objects.equals(getName(), ((Account) o).getName()) && Objects.equals(getId(), ((Account) o).getId()) && Objects.equals(getBalance(), ((Account) o).getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, balance);
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nBalance: " + balance + "\n";
    }
}

abstract class Transaction {
    private final long fromId;
    private final long toId;
    private final String amount;
    private final String description;

    public Transaction(long fromId, long toId, String amount, String description) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.description = description;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    double parseStringToDouble(String str) {
        return Double.parseDouble(str.substring(0, str.length() - 1));
    }

    public abstract double getProvision();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        return fromId == ((Transaction) o).fromId && toId == ((Transaction) o).toId && Objects.equals(amount, ((Transaction) o).amount) && Objects.equals(description, ((Transaction) o).description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, amount, description);
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private final String flatAmount;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, amount, "FlatAmount");
        this.flatAmount = flatProvision;
    }

    @Override
    public double getProvision() {
        return parseStringToDouble(flatAmount);
    }

    public String getFlatAmount() {
        return flatAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatAmountProvisionTransaction)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(flatAmount, ((FlatAmountProvisionTransaction) o).flatAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatAmount);
    }
}


class FlatPercentProvisionTransaction extends Transaction {

    private final int percent;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDollar) {
        super(fromId, toId, amount, "FlatPercent");
        this.percent = centsPerDollar;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    public double getProvision() {
        return ((int) parseStringToDouble(super.getAmount()) * percent) / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatPercentProvisionTransaction)) return false;
        if (!super.equals(o)) return false;
        return percent == ((FlatPercentProvisionTransaction) o).percent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), percent);
    }
}

class Bank {
    private String name;
    private Account[] accounts;
    private double totalTransfers;
    private double totalProvision;


    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = accounts;
    }


    public double getTotalTransfers() {
        return totalTransfers;
    }

    public void setTotalTransfers(double totalTransfers) {
        this.totalTransfers = totalTransfers;
    }

    public double getTotalProvision() {
        return totalProvision;
    }

    public void setTotalProvision(double totalProvision) {
        this.totalProvision = totalProvision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    double parseStringToDouble(String str) {
        return Double.parseDouble(str.substring(0, str.length() - 1));
    }

    String parseDoubleToString(double val) {
        return String.format(Locale.ENGLISH, "%.2f$", val);
    }

    public boolean makeTransaction(Transaction t) {
        Account sender = findUserById(t.getFromId());
        Account receiver = findUserById(t.getToId());


        if (sender == null || receiver == null) {
            return false;
        }


        double senderBalance = parseStringToDouble(sender.getBalance());
        double receiverBalance = parseStringToDouble(receiver.getBalance());
        double transactionAmount = parseStringToDouble(t.getAmount()) + t.getProvision();

        boolean userFromHasBalance = senderBalance >= transactionAmount;

        if (!userFromHasBalance) {
            return false;
        }

        senderBalance -= transactionAmount;
        receiverBalance += transactionAmount - t.getProvision();
        if (sender.equals(receiver)) {
            receiverBalance -= transactionAmount;
        }

        setTotalTransfers(getTotalTransfers() + transactionAmount - t.getProvision());
        setTotalProvision(getTotalProvision() + t.getProvision());

        sender.setBalance(parseDoubleToString(senderBalance));
        receiver.setBalance(parseDoubleToString(receiverBalance));

        return true;
    }

    private Account findUserById(long userId) {
        for (Account account : accounts) {
            if (account.getId().equals(userId)) {
                return account;
            }
        }
        return null;
    }

    public String totalTransfers() {
        return parseDoubleToString(totalTransfers);
    }

    public String totalProvision() {
        return parseDoubleToString(totalProvision);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;
        return Double.compare(((Bank) o).getTotalTransfers(), getTotalTransfers()) == 0 && Double.compare(((Bank) o).getTotalProvision(), getTotalProvision()) == 0 && Objects.equals(getName(), ((Bank) o).getName()) && Arrays.equals(getAccounts(), ((Bank) o).getAccounts());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getTotalTransfers(), getTotalProvision());
        result = 31 * result + Arrays.hashCode(getAccounts());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n\n", name));

        for (Account acc : accounts) {
            sb.append(acc);
        }
        return sb.toString();
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
