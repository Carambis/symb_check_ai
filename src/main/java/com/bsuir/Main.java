package com.bsuir;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int COUNT_LEARN = 10;
    private static final int SIZE_IMAGE = 9900;
    private static final int NUMBER_SET = 3;
    private static int[] weights = new int[SIZE_IMAGE];
    private static final int BIAS = 5;
    private static final String PATH = "src\\main\\resources\\study_file\\";
    private static final String PATH_TO_WEIGHTS = "src\\main\\resources\\weights.txt";
    private static final String PATH_TEST = "src\\main\\resources\\test_file\\";
    private static final String PNG = ".PNG";
    private static final int TEST_NUMBER = 3;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_TO_WEIGHTS))) {
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {
                weights[i] = Integer.parseInt(str);
                i++;
            }
            if (i != SIZE_IMAGE) {
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
        return net >= BIAS;
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
        int height = image.getHeight();
        int width = image.getWidth();
        int[] points = new int[height * width];
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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
        for (int i = 0; i < COUNT_LEARN; i++) {
            for (int j = 0; j < NUMBER_SET; j++) {
                list.add(readImage(PATH + i + "" + j + PNG));
            }
        }

        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < list.size(); j++) {
                int[] number = list.get(j);
                if (j / NUMBER_SET != TEST_NUMBER) {
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

        FileWriter fileWriter = new FileWriter(new File(PATH_TO_WEIGHTS));
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
            case "32":
            case "90":
                System.out.println(string + " = " + proceed(readImage(PATH_TEST + string + PNG)));
                break;
            default:
                System.out.println("Incorrect number");
        }
    }
}
