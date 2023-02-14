package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Expression.ArithExp;
import Model.Expression.IExp;
import Model.Type.IType;
import Model.Type.IntType;
import Model.Value.IValue;

public class MULExp implements IExp {
    private final IExp exp1;
    private final IExp exp2;

    public MULExp(IExp e1, IExp e2)
    {
        this.exp1=e1;
        this.exp2=e2;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        IExp converted = new ArithExp('-',
                new ArithExp('*', exp1, exp2),
                new ArithExp('+', exp1, exp2));
        return converted.eval(tbl, heap);
    }

    @Override
    public IExp deepCopy() {
        return new MULExp(exp1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType t1 = exp1.typecheck(typeEnv);
        IType t2 = exp2.typecheck(typeEnv);
        if(t1.equal(new IntType()) && t2.equal(new IntType()))
            return new IntType();
        else throw new MyExceptions("Expressions in the MUL should be int!");
    }

    public String toString()
    {
        return "MUL("+ exp1+ ","+ exp2+")";
    }
}
