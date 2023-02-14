package Model.Expression;

import Model.ADT.MyDictionary;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Type.IType;
import Model.Value.IValue;
//import com.sun.jdi.Value;

public class VarExp implements IExp{
    String id;

    public VarExp(String i)
    {
        this.id=i;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        return tbl.lookup(id);
    }

    @Override
    public IExp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        return typeEnv.lookup(id);
    }

    public String toString()
    {
        return id;
    }
}
