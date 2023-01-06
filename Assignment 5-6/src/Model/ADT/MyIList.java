package Model.ADT;

import Model.Exceptions.MyExceptions;

public interface MyIList<T>{
    void add(T e);
    T remove() throws MyExceptions;

    boolean isEmpty();
}
