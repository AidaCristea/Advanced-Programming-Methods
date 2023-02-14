package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.ValueExp;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IntValue;

public class WaitStmt implements IStmt{
    private final int val;

    public WaitStmt(int val)
    {
        this.val=val;
    }


    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        if(val!=0)
        {
            MyIStack<IStmt> stk=state.getStk();
            stk.push(new CompStmt(new PrintStmt(new ValueExp(new IntValue(val))),
                    new WaitStmt(val-1)));
            state.setStack(stk);
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WaitStmt(val);
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        return typeEnv;
    }

    public String toString()
    {
        return "wait("+ val+")";
    }
}
