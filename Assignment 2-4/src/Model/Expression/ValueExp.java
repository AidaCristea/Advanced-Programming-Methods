package Model.Expression;


import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Value.IValue;
import Model.Value.IntValue;


public class ValueExp implements IExp{
    IValue e;
    //toString?

    public ValueExp(IValue e)
    {
        this.e=e;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        return this.e;
    }

    @Override
    public IExp deepCopy() {
        return new ValueExp(this.e);
    }

    public String toString()
    {
        return this.e.toString();
    }
}
