import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static int STRINGS = 10_000;
    public static BlockingQueue<String> array1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> array2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> array3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        Thread fillArrays = new Thread(() -> {
            for (int i = 0; i < STRINGS; i++) {
                try {
                    String text = generateText("abc", 100_000);
                    array1.put(text);
                    array2.put(text);
                    array3.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        fillArrays.start();

        Thread findA = new Thread(() -> {
            int maxA = 0;
            String strA = null;
            for (int i = 0; i < STRINGS; i++) {
                try {
                    String text = array1.take();
                    int counter = countChar(text, 'a');
                    if(counter > maxA) {
                        maxA = counter;
                        strA = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(strA.substring(0, 100) + " -> количество а: " + maxA);
        });
        findA.start();

        Thread findB = new Thread(() -> {
            int maxB = 0;
            String strB = null;
            for (int i = 0; i < STRINGS; i++) {
                try {
                    String text = array2.take();
                    int counter = countChar(text, 'b');
                    if(counter > maxB) {
                        maxB = counter;
                        strB = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(strB.substring(0, 100) + " -> количество b: " + maxB);
        });
        findB.start();

        Thread findC = new Thread(() -> {
            int maxC = 0;
            String strC = null;
            for (int i = 0; i < STRINGS; i++) {
                try {
                    String text = array3.take();
                    int counter = countChar(text, 'c');
                    if(counter > maxC) {
                        maxC = counter;
                        strC = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(strC.substring(0, 100) + " -> количество c: " + maxC);
        });
        findC.start();

        try {
            fillArrays.join();
            findA.join();
            findB.join();
            findC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public static int countChar(String text, char letter) {
        int counter = 0;
        for (int j = 0; j < text.length(); j++) {
            if(text.charAt(j) == letter) {
                ++counter;
            }
        }
        return counter;
    }
}
