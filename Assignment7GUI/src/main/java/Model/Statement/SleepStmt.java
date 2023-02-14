package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.PrgState;
import Model.Type.IType;

public class SleepStmt implements IStmt{
    private final int value;

    public SleepStmt(int v)
    {
        this.value=v;
    }
    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        if(value!=0)
        {
            MyIStack<IStmt> exeStk = state.getStk();
            exeStk.push(new SleepStmt(value-1));
            state.setStack(exeStk);
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new SleepStmt(value);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        return typeEnv;
    }

    public String toString()
    {
        return "sleep("+ value + ")";
    }
}
