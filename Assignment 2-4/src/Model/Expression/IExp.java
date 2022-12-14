package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Value.IValue;


public interface IExp {
    IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions;
    IExp deepCopy();
}
