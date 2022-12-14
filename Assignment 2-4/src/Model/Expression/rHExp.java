package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Value.IValue;
import Model.Value.RefValue;

public class rHExp implements IExp{
    private final IExp expression;

    public rHExp(IExp exp)
    {
        this.expression=exp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        IValue value = expression.eval(tbl, heap);
        if (!(value instanceof RefValue))
            throw new MyExceptions(value.toString() + " not of RefType");
        RefValue refVal=(RefValue) value;
        return heap.get(refVal.getAddress());
    }

    @Override
    public IExp deepCopy() {
        return new rHExp(expression.deepCopy());
    }

    public String toString()
    {
        return "ReadHeap("+ expression.toString() + ")";
    }

}
