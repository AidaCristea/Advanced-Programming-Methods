package Model.ADT;

import Model.Exceptions.MyExceptions;

import java.util.List;

public interface MyIList<T>{
    void add(T e);
    T remove() throws MyExceptions;

    boolean isEmpty();
    List<T> getList();
}
