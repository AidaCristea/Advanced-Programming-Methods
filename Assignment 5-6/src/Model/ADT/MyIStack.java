package Model.ADT;

import Model.Exceptions.MyExceptions;

public interface MyIStack<T> {
    T pop() throws MyExceptions;
    void push(T v);
    boolean isEmpty();

    T peek();
}
