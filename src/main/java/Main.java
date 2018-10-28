import java.util.Random;

public class Main {
    private static int[] weights = new int[15];
    // Порог функции активации
    //http://programador.ru/resize-image/
    //http://qaru.site/questions/52162/how-can-i-resize-an-image-using-java
    private static int bias = 7;
    private static Random random = new Random();

    public static void main(String[] args) {
        String num0 = "111101101101111";
        String num1 = "001001001001001";
        String num2 = "111001111100111";
        String num3 = "111001111001111";
        String num4 = "101101111001001";
        String num5 = "111100111001111";
        String num6 = "111100111101111";
        String num7 = "111001001001001";
        String num8 = "111101111101111";
        String num9 = "111101111001111";

        String[] nums = {num0, num1, num2, num3, num4, num5, num6, num7, num8, num9};

        String num51 = "111100111000111";
        String num52 = "111100010001111";
        String num53 = "111100011001111";
        String num54 = "110100111001111";
        String num55 = "110100111001011";
        String num56 = "111100101001111";

        for (int i = 0; i < 10000; i++) {
            int option = random.nextInt(9);
            if (option != 5) {
                if (proceed(nums[option])) decrease(nums[option]);
            } else {
                if (!proceed(num5)) increase(num5);
            }
        }

        System.out.println("0 это 5? " + proceed(num0));
        System.out.println("1 это 5? " + proceed(num1));
        System.out.println("2 это 5? " + proceed(num2));
        System.out.println("3 это 5? " + proceed(num3));
        System.out.println("4 это 5? " + proceed(num4));
        System.out.println("6 это 5? " + proceed(num6));
        System.out.println("7 это 5? " + proceed(num7));
        System.out.println("8 это 5? " + proceed(num8));
        System.out.println("9 это 5? " + proceed(num9));

        System.out.println("Узнал 5? " + proceed(num5));
        System.out.println("Узнал 5 - 1? " + proceed(num51));
        System.out.println("Узнал 5 - 2? " + proceed(num52));
        System.out.println("Узнал 5 - 3? " + proceed(num53));
        System.out.println("Узнал 5 - 4? " + proceed(num54));
        System.out.println("Узнал 5 - 5? " + proceed(num55));
        System.out.println("Узнал 5 - 6? " + proceed(num56));
    }

    private static boolean proceed(String number) {
        int net = 0;
        for (int i = 0; i < 15; i++) {
            net += new Integer(number.charAt(i)) * weights[i];
        }
        return net >= bias;
    }

    private static void decrease(String number) {
        for (int i = 0; i < 15; i++) {
            if (number.charAt(i) == '1') {
                weights[i]--;
            }
        }
    }

    private static void increase(String number) {
        for (int i = 0; i < 15; i++) {
            if (number.charAt(i) == '1') {
                weights[i]++;
            }
        }
    }
}
