import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        BlockingQueue<String> strings1 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> strings2 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> strings3 = new ArrayBlockingQueue<>(100);
        int textsQuantity = 10000;
        int threadsQuantity = 3;
        AtomicInteger maxCountA = new AtomicInteger(0);
        AtomicInteger maxCountB = new AtomicInteger(0);
        AtomicInteger maxCountC = new AtomicInteger(0);

        new Thread (() -> {
            int i = 0;
            while (i < textsQuantity/threadsQuantity) {
                try {
                    strings1.put(generateText("abc", 100000));
                    strings2.put(generateText("abc", 100000));
                    strings3.put(generateText("abc", 100000));
                    i++;

                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        Thread threadA = new Thread (() -> {
            for (int i = 0; i < textsQuantity/threadsQuantity; i++) {
                try {
                    String textA = strings1.take();
                    int countA = countChar(textA, 'a');
                    if (maxCountA.get() < countA) {
                        maxCountA.set(countA);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadA.start();

        Thread threadB = new Thread (() -> {
            for (int i = 0; i < textsQuantity/threadsQuantity; i++) {
                try {
                    String textB = strings2.take();
                    int countB = countChar(textB, 'b');
                    if (maxCountB.get() < countB) {
                        maxCountB.set(countB);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadB.start();

        Thread threadC = new Thread (() -> {
            for (int i = 0; i < textsQuantity/threadsQuantity; i++) {
                try {
                    String textC = strings3.take();
                    int countC = countChar(textC, 'c');
                    if (maxCountC.get() < countC) {
                        maxCountC.set(countC);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadC.start();

        try {
            threadA.join();
            System.out.println("максимальное количество букв 'a' = " + maxCountA.get());
            threadB.join();
            System.out.println("максимальное количество букв 'b' = " + maxCountB.get());
            threadC.join();
            System.out.println("максимальное количество букв 'c' = " + maxCountC.get());
        } catch (InterruptedException e) {
            return;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Integer countChar (String text, char symbol) {
        char[] mas = text.toCharArray();
        int count = 0;
        for (int i = 0; i < mas.length; i++) {
            if (mas[i] == symbol) {
                count++;
            }
        }
        return count;
    }
}
