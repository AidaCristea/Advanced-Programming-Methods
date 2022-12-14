package Model.ADT;

import Model.Exceptions.MyExceptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<Key,Val> implements MyIDictionary<Key,Val>{
     HashMap<Key, Val> dictionary;

     public MyDictionary()
     {
         this.dictionary = new HashMap<>();
     }

    @Override
    public void put(Key k, Val v) {
        dictionary.put(k,v);
    }

    @Override
    public void remove(Key k) throws MyExceptions{
        if(!isDefined(k))
            throw new MyExceptions(k + " is not deifned");
        this.dictionary.remove(k);
    }

    @Override
    public Collection<Val> values()
    {
        return this.dictionary.values();
    }


    @Override
    public Val lookup(Key k) throws MyExceptions
    {
        if(!isDefined(k))
            throw new MyExceptions(k +" is not defined");
        return dictionary.get(k);
    }

    @Override
    public boolean isDefined(Key id) {
        return this.dictionary.containsKey(id);
    }

    @Override
    public void update(Key id, Val val) throws MyExceptions {
         if(!isDefined(id))
             throw new MyExceptions(id + " is not defined");
        dictionary.put(id, val);
    }

    public String toString()
    {
        return this.dictionary.toString();
    }

    @Override
    public Map<Key,Val> getContent()
    {
        return dictionary;
    }

}
