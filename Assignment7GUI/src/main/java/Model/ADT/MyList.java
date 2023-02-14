package Model.ADT;

import Model.Exceptions.MyExceptions;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    List<T> list;

    public MyList()
    {
        this.list=new ArrayList<>();
    }
    @Override
    public void add(T e) {
        this.list.add(e);
    }


    @Override
    public T remove() throws MyExceptions {
        //remove the element from the first position
        if(list.isEmpty())
            throw new MyExceptions("The list is empty");
        return this.list.remove(0);
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public List<T> getList() {
        synchronized (this)
        {
            return this.list;
        }
    }

    public String toString()
    {
        return this.list.toString();
    }

}
