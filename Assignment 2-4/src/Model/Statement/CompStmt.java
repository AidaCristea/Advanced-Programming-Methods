package Model.Statement;

import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;

public class CompStmt implements IStmt{
    IStmt first;
    IStmt snd;

    public CompStmt(IStmt f, IStmt s)
    {
        this.first=f;
        this.snd=s;
    }


    public String toString()
    {
        return "("+first.toString() +"|" + snd.toString()+")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIStack<IStmt> stk = state.getStk();
        stk.push(snd);
        stk.push(first);
        state.setStack(stk);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(), snd.deepCopy());
    }

}
