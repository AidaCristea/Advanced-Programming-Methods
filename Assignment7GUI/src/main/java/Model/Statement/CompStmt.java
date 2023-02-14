package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.IType;

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
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(), snd.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        MyIDictionary<String, IType> typeEnv1 = first.typecheck(typeEnv);
        MyIDictionary<String, IType> typeEnv2 = snd.typecheck(typeEnv1);
        return typeEnv2;
        // or return snd.typecheck(first.typecheck(typeEnv));
    }

}
