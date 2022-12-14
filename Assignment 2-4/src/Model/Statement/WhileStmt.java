package Model.Statement;

import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.BoolType;
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
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(expression.deepCopy(), statement.deepCopy());
    }

    public String toString()
    {
        return "While("+expression.toString() + "){"+statement.toString() + "}";
    }
}
