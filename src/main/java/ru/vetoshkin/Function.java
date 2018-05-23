package ru.vetoshkin;

public interface Function<T> {

    public int getArgumentCount();

    public T method(T[] args);

}
