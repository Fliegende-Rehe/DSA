package BinaryTree;// ru.abdullin@innopolis.university
// Ruslan Abdullin

import java.util.*;

public class FraudDetection {
    // O(n*d*log(d))
    public static void main(String[] args) {
        final int DAY = 86400000; // millisecond in day

        BTIO.Input data = BTIO.Read();
        List<Transaction> transactions = data.transactions;
        int D = data.trailing;

        CQueue q = CQueue.toCQueue(transactions, D);
        Queue<Transaction> queue = q.queue;

        int alerts = 0;
        double total = 0;

        for (int i = q.head; i < transactions.size(); i++) {
            if (transactions.get(i).date.equals(new Date(q.next))) {
                total += transactions.get(i).amount;
                if (total >= Median(new ArrayList<>(queue))) alerts += 1;
                if (i == transactions.size() - 1 || transactions.get(i).date.equals(transactions.get(i + 1).date))
                    continue;
                queue.poll();
                queue.add(new Transaction(transactions.get(i).date.getTime(), total));
                q.next += DAY;
                total = 0;
            } else {
                long notStonksDays = (transactions.get(i).date.getTime() - q.next) / DAY;
                for (int j = 0; j < Math.min(notStonksDays, D); j++, q.next += DAY) {
                    queue.poll();
                    queue.add(new Transaction(q.next, 0));
                }
                if (notStonksDays >= D) q.next = transactions.get(i).date.getTime();
                i -= 1;
            }
        }

        BTIO.Write(alerts);
    }

    public static double Median(List<Transaction> transactions) {
        List<Transaction> sortByAmount = Sort.Merge(transactions);
        if (transactions.size() % 2 == 0)
            return Objects.requireNonNull(sortByAmount).get(sortByAmount.size() / 2).amount +
                    sortByAmount.get(sortByAmount.size() / 2 - 1).amount;
        return Objects.requireNonNull(sortByAmount).get(sortByAmount.size() / 2).amount * 2;
    }
}

class CQueue {
    public final Queue<Transaction> queue;
    public final int head;
    public long next;

    public CQueue(Queue<Transaction> queue, long next, int head) {
        this.queue = queue;
        this.next = next;
        this.head = head;
    }

    public static CQueue toCQueue(List<Transaction> transactions, int D) {
        final int DAY = 86400000; // millisecond in day
        List<Transaction> arr = new ArrayList<>();
        arr.add(transactions.get(0));
        long next = arr.get(0).date.getTime() + DAY;
        int head = 1;

        for (int i = 1; i < transactions.size() && transactions.get(i).date.getTime() <= (long) (D - 1) * DAY + transactions.get(0).date.getTime(); i++, head++) {
            Transaction cur = transactions.get(i);
            for (; !arr.get(arr.size() - 1).date.equals(cur.date); next += DAY) arr.add(new Transaction(next, 0));
            if (arr.get(arr.size() - 1).date.equals(cur.date))
                arr.set(arr.size() - 1, new Transaction(arr.get(arr.size() - 1).date.getTime(), arr.get(arr.size() - 1).amount + cur.amount));
            else arr.add(transactions.get(i));
        }

        Queue<Transaction> queue = new LinkedList<>(arr);
        for (int i = 0; i < D - arr.size(); i++, next += DAY) queue.add(new Transaction(next, 0));
        return new CQueue(queue, next, head);
    }
}

class Transaction {
    Date date;
    double amount;

    Transaction(String tr) {
        this.date = new Date(Integer.parseInt(tr.split(" ")[0].split("-")[0]) - 1900,
                Integer.parseInt(tr.split(" ")[0].split("-")[1]) - 1,
                Integer.parseInt(tr.split(" ")[0].split("-")[2]));
        this.amount = Double.parseDouble(tr.split(" ")[1].replace("$", ""));
    }

    Transaction(long ms, double amountOfMoney) {
        this.date = new Date(ms);
        this.amount = amountOfMoney;
    }
}

class IO {
    public static class Input {
        public final int trailing;
        public final List<Transaction> transactions;

        public Input(List<Transaction> h, int t) {
            this.transactions = h;
            this.trailing = t;
        }
    }

    public static Input Read() {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(), D = sc.nextInt();
        sc.nextLine();
        List<Transaction> transactions = new ArrayList<>(N);
        for (int i = 0; i < N; i++) transactions.add(new Transaction(sc.nextLine()));
        sc.close();
        return new Input(Sort.Count(transactions), D);
    }

    public static void Write(int alerts) {
        System.out.println(alerts);
    }
}

class Sort {
    // O(n*log(n))
    public static List<Transaction> Merge(List<Transaction> transactions) {
        if (transactions.size() < 2) return null;
        int middle = transactions.size() / 2;
        List<Transaction> l = Arrays.asList(new Transaction[middle]),
                r = Arrays.asList(new Transaction[transactions.size() - middle]);
        for (int i = 0; i < middle; i++) l.set(i, transactions.get(i));
        for (int i = middle; i < transactions.size(); i++) r.set(i - middle, transactions.get(i));
        Merge(l);
        Merge(r);
        int i = 0, j = 0, k = 0;
        while (i < l.size() && j < r.size()) {
            if (l.get(i).amount <= r.get(j).amount) transactions.set(k++, l.get(i++));
            else transactions.set(k++, r.get(j++));
        }
        while (i < l.size()) transactions.set(k++, l.get(i++));
        while (j < r.size()) transactions.set(k++, r.get(j++));
        return transactions;
    }

    // O(n)
    public static List<Transaction> Count(List<Transaction> transactions) {
        int DAY = 86400000, // millisecond in day
                maxDate = Integer.MIN_VALUE,
                minDate = Integer.MAX_VALUE;
        for (Transaction transaction1 : transactions) {
            maxDate = Math.max(maxDate, (int) (transaction1.date.getTime() / DAY));
            minDate = Math.min(minDate, (int) (transaction1.date.getTime() / DAY));
        }
        List<Integer> count = Arrays.asList(new Integer[maxDate - minDate + 1]);
        for (int i = 0; i < maxDate - minDate + 1; i++) count.set(i, 0);
        for (Transaction transaction : transactions) {
            count.set((int) (transaction.date.getTime() / DAY) - minDate,
                    count.get((int) (transaction.date.getTime() / DAY) - minDate) + 1);
        }
        List<Integer> indexes = Arrays.asList(new Integer[maxDate - minDate + 1]);
        indexes.set(0, count.get(0));
        for (int i = 1; i < maxDate - minDate + 1; i++) indexes.set(i, indexes.get(i - 1) + count.get(i));
        List<Transaction> toRet = Arrays.asList(new Transaction[transactions.size()]);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            toRet.set(indexes.get((int) (transactions.get(i).date.getTime() / DAY) - minDate) - 1,
                    new Transaction(transactions.get(i).date.getTime(), transactions.get(i).amount));
            indexes.set((int) (transactions.get(i).date.getTime() / DAY) - minDate,
                    indexes.get((int) (transactions.get(i).date.getTime() / DAY) - minDate) - 1);
        }
        return toRet;
    }
}
