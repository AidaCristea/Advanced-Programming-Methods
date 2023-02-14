package Model.ADT;

import Model.Exceptions.MyExceptions;
import Model.Value.IValue;

import java.util.HashMap;
import java.util.Set;

public interface MyIHeap {
    int getFreeValue();
    HashMap<Integer, IValue> getContent();
    void setContent(HashMap<Integer, IValue> newMap);
    int add(IValue value);
    void update(Integer position, IValue value) throws MyExceptions;
    IValue get(Integer posiiton) throws MyExceptions;
    boolean containsKey(Integer position);
    void remove(Integer key) throws MyExceptions;
    Set<Integer> keySet();
}
