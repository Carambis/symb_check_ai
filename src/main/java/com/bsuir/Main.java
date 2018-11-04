package com.bsuir;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int COUNT_LEARM = 10;
    private static final int SIZE_IMAGE = 9900;
    private static int[] weights = new int[SIZE_IMAGE];
    private static int bias = 6;
    private static Random random = new Random();
    private static String path = "src\\main\\resources\\study_file\\";
    private static final String path_to_weights = "src\\main\\resources\\weights.txt";
    private static String path_test = "src\\main\\resources\\test_file\\";
    private static String png = ".png";
    private static int test_number = 2;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(path_to_weights))) {
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {
                weights[i] = Integer.parseInt(str);
                i++;
            }
            if (i != 9900) {
                learn();
            }
        } catch (FileNotFoundException e) {
            learn();
        }

        while (true) {
            try {
                test();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean proceed(int[] number) {
        int net = 0;
        for (int i = 0; i < number.length; i++) {
            net += number[i] * weights[i];
        }
        return net >= bias;
    }

    private static void decrease(int[] number) {
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                weights[i]--;
            }
        }
    }

    private static void increase(int[] number) {
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                weights[i]++;
            }
        }
    }

    private static int[] readImage(String fileName) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        int[] points = new int[image.getHeight() * image.getWidth()];
        int k = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int rgb = image.getRGB(j, i);
                Color color = new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                points[k] = color.equals(Color.BLACK) ? 1 : 0;
                k++;
            }
        }
        return points;
    }

    private static void learn() throws IOException {
        System.out.println("Start learning");
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < COUNT_LEARM; i++) {
            list.add(readImage(path + i + "0" + png));
            list.add(readImage(path + i + "1" + png));
            list.add(readImage(path + i + "2" + png));
        }

        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 30; j++) {
                int[] number = list.get(j);
                if (j / 3 != test_number) {
                    if (proceed(number)) {
                        decrease(number);
                    }
                } else {
                    if (!proceed(number)) {
                        increase(number);
                    }
                }
            }
        }

        FileWriter fileWriter = new FileWriter(new File(path_to_weights));
        for (int weight : weights) {
            fileWriter.append(String.valueOf(weight));
            fileWriter.append("\n");
        }
        fileWriter.flush();
        fileWriter.close();

        System.out.println("Finish");
    }

    private static void test() throws IOException {
        System.out.println("Input number");
        String string = scanner.next();

        switch (string) {
            case "2":
            case "5":
            case "21":
            case "90":
                System.out.println(string + " = " + proceed(readImage(path_test + string + png)));
                break;
            default:
                System.out.println("Incorrect number");
        }
    }
}
