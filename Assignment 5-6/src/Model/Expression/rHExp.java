package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Type.IType;
import Model.Type.RefType;
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

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType typ= expression.typecheck(typeEnv);
        if(typ instanceof RefType)
        {
            RefType reft = (RefType) typ;
            return reft.getInner();
        }
        else throw new MyExceptions("the rH argument is not a Ref Type");
    }

    public String toString()
    {
        return "ReadHeap("+ expression.toString() + ")";
    }

}
