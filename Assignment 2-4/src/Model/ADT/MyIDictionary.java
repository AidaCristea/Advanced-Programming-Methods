package Model.ADT;

import Model.Exceptions.MyExceptions;

import java.util.Collection;
import java.util.Map;

public interface MyIDictionary<Key, Val> {
    void put(Key k, Val v);
    void remove(Key k) throws MyExceptions;

    Val lookup(Key k) throws MyExceptions;

    boolean isDefined(Key id);

    void update(Key id, Val val) throws MyExceptions;

    Collection<Val> values();

    Map<Key,Val> getContent();
}
