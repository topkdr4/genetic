package ru.vetoshkin;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        World<Double> world = new World<>(100, Double.class);

        world.setIterations(340);
        world.setCreator(random::nextDouble);

        // Исследуемая функция
        world.setFunction(new Function<Double>() {
            @Override
            public int getArgumentCount() {
                return 2;
            }

            @Override
            public Double method(Double[] args) {
                return Math.sqrt(1 - Math.pow(args[1], 2) - Math.pow(args[0], 2));
            }
        });

        // Функция мутирования
        world.setMutation(source -> {
            for (int i = 0; i < source.length; i++) {
                if (random.nextInt(0, 4) == 3) {
                    source[i] = Double.longBitsToDouble(source[i].longValue() << random.nextInt(0, 64));
                }
            }
        });

        // Функция выборки
        world.setSelection((o1, o2) -> {
            int result = Double.compare(o1, o2);
            if (result == 0)
                return 0;

            return result == -1 ? 1 : -1;
        });

        Double[] result = world.start();
        System.out.println("Результат алгортима:");
        System.out.println(Arrays.toString(result));
    }

}
