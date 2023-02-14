package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;

public class NotExp implements IExp{
    private final IExp exp;

    public NotExp(IExp e)
    {
        this.exp=e;
    }
    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        BoolValue val = (BoolValue) exp.eval(tbl, heap);
        if(!val.getVal())
            return new BoolValue(true);
        else return new BoolValue(false);
    }

    @Override
    public IExp deepCopy() {
        return new NotExp(exp.deepCopy());
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        return exp.typecheck(typeEnv);
    }

    public String toString()
    {
        return "!(" + exp + ")";
    }
}
