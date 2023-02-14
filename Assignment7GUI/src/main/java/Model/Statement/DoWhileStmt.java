package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;

public class DoWhileStmt implements IStmt{
    private final IStmt statement;
    private final IExp expression;

    public DoWhileStmt(IExp expression, IStmt statement) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        IStmt converted = new CompStmt( statement, new WhileStmt(expression, statement));
        state.getStk().push(converted);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new DoWhileStmt(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType texp = expression.typecheck(typeEnv);
        if(texp.equal(new BoolType()))
        {
            statement.typecheck(typeEnv.clone());
            return typeEnv;

        }
        else throw new MyExceptions("Condition in the do while statement must be bool");
    }

    public String toString()
    {
        return "do {" + statement + "} while (" + expression + ")";
    }
}
