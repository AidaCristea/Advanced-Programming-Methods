package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.Expression.NotExp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;

public class RepeatUntilStmt implements IStmt{
    private final IStmt stmt;
    private final IExp exp;

    public RepeatUntilStmt(IStmt s, IExp e)
    {
        this.stmt=s;
        this.exp=e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIStack<IStmt> stk = state.getStk();
        IStmt converted = new CompStmt(stmt, new WhileStmt(new NotExp(exp), stmt));
        stk.push(converted);
        state.setStack(stk);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new RepeatUntilStmt(stmt.deepCopy(), exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType type = exp.typecheck(typeEnv);
        if(type.equal(new BoolType()))
        {
            stmt.typecheck(typeEnv.clone());
            return typeEnv;
        }
        else throw new MyExceptions("Expression in the repeat statement must be of Bool type");
    }

    public String toString()
    {
        return "repeat("+stmt+") until ("+exp+")";
    }
}
