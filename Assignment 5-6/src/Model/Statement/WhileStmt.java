package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;

public class WhileStmt implements IStmt{
    private final IExp expression;
    private final IStmt statement;

    public WhileStmt(IExp exp, IStmt stmt)
    {
        this.expression=exp;
        this.statement=stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        IValue value=expression.eval(state.getSymTable(), state.getHeap());
        MyIStack<IStmt> stack=state.getStk();
        if(!value.getType().equal(new BoolType()))
            throw new MyExceptions(value.toString() + " is not of BoolType");
        BoolValue boolVal=(BoolValue) value;
        if(boolVal.getVal())
        {
            stack.push(this);
            stack.push(statement);
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType typeExp = expression.typecheck(typeEnv);
        if(typeExp.equal(new BoolType()))
        {
            statement.typecheck(typeEnv.clone());
            return typeEnv;
        }
        else throw new MyExceptions("The condition of WHILE does not have the type Bool");
    }

    public String toString()
    {
        return "While("+expression.toString() + "){"+statement.toString() + "}";
    }
}
