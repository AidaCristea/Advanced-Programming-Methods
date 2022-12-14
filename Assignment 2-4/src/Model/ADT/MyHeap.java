package Model.ADT;

import Model.Exceptions.MyExceptions;
import Model.Value.IValue;

import java.util.HashMap;
import java.util.Set;

public class MyHeap implements MyIHeap{
    HashMap<Integer, IValue> heap;
    Integer freeLocationValue;

    public int newVal()
    {
        //find the next free location for value
        freeLocationValue +=1;
        while (freeLocationValue==0 || heap.containsKey(freeLocationValue))
            freeLocationValue+=1;
        return freeLocationValue;
    }

    public MyHeap()
    {
        this.heap=new HashMap<>();
        freeLocationValue=1;
    }

    @Override
    public int getFreeValue() {
        return freeLocationValue;
    }

    @Override
    public HashMap<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(HashMap<Integer, IValue> newMap) {
        this.heap=newMap;

    }

    @Override
    public int add(IValue value) {
        heap.put(freeLocationValue, value);
        Integer toReturn = freeLocationValue;
        freeLocationValue=newVal();
        return toReturn;
    }

    @Override
    public void update(Integer position, IValue value) throws MyExceptions {
        if (!heap.containsKey(position))
            throw new MyExceptions(position.toString() + " is not present in the heap");
        heap.put(position, value);
    }

    @Override
    public IValue get(Integer position) throws MyExceptions {
        if (!heap.containsKey(position))
            throw new MyExceptions(position.toString() + " is not present in the heap");
        return heap.get(position);
    }

    @Override
    public boolean containsKey(Integer position) {
        return this.heap.containsKey(position);
    }

    @Override
    public void remove(Integer key) throws MyExceptions {
        if (!containsKey(key))
            throw new MyExceptions( key.toString() + " is not defined");
        freeLocationValue=key;
        this.heap.remove(key);

    }

    @Override
    public Set<Integer> keySet() {
        return heap.keySet();
    }

    public String toString()
    {
        return this.heap.toString();
    }
}
