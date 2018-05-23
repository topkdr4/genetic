package ru.vetoshkin;

/**
 * Ветошкин А.В.
 * РИС-16-бзу
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Обертка на пулом где живут особи
 */
public class World<T> {
    private final List<T[]> pool = new ArrayList<>();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final int poolSize;
    private final Class<T> clazz;
    private int iterations;
    private Function<T> function;
    private Creator<T> creator;
    private Mutation<T[]> mutation;
    private Selection<T> selection;


    public World(int poolSize, Class<T> clazz) {
        this.poolSize = poolSize;
        this.clazz = clazz;
    }


    /**
     * Инициализация
     */
    private void init() {
        for (int i = 0; i < poolSize; i++) {
            pool.add(generate());
        }
    }


    /**
     * Заполнить массив случайными элементами
     */
    private void fillRandom(T[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = creator.create();
        }
    }


    @SuppressWarnings("unchecked")
    private T[] generate() {
        int arguments = function.getArgumentCount();

        T[] data = (T[]) Array.newInstance(clazz, arguments);
        fillRandom(data);

        return data;
    }


    public void setFunction(Function<T> function) {
        this.function = function;
    }


    public void setMutation(Mutation<T[]> mutation) {
        this.mutation = mutation;
    }


    public void setSelection(Selection<T> selection) {
        this.selection = selection;
    }


    public void setIterations(int iter) {
        this.iterations = iter;
    }


    public void setCreator(Creator<T> creator) {
        this.creator = creator;
    }


    /**
     * Запуск генетического алгоритма
     */
    public T[] start() {
        init();
        for (int i = 0; i < iterations; i++) {
            /**
             * 1. Скрещивание и/или мутация
             * 2. Селекция
             * 3. Формирование нового поколения
             * */
            for (T[] ts : pool) {
                mutation.mutation(ts);
            }

            // Селекция
            pool.sort((o1, o2) -> {
                T result1 = function.method(o1);
                T result2 = function.method(o2);
                return selection.compare(result1, result2);
            });

            int position = poolSize / 2;
            for (int j = position; j < poolSize; j++) {
                pool.set(j, generate());
            }
        }

        return pool.get(0);
    }

}
