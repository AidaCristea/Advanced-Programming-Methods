package Model.ADT;

import Model.Exceptions.MyExceptions;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws MyExceptions;
    void push(T v);
    boolean isEmpty();

    T peek();

    List<T> getReversed();
}
