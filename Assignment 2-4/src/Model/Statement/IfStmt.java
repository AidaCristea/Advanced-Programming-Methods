package Model.Statement;

import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Value.BoolValue;
import Model.Value.IValue;

public class IfStmt implements IStmt{
    IExp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(IExp e, IStmt t, IStmt el)
    {
        exp=e;
        thenS=t;
        elseS = el;
    }


    public String toString()
    {
        return "(IF("+exp.toString()+") THEN(" + thenS.toString()+")ELSE("+elseS.toString()+"))";

    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        IValue result = this.exp.eval(state.getSymTable(), state.getHeap());
        if(result instanceof BoolValue boolRes)
        {
            IStmt statement;
            if(boolRes.getVal())
                statement = thenS;
            else statement=elseS;
            MyIStack<IStmt> stack = state.getStk();
            stack.push(statement);
            state.setStack(stack);
            return state;
        }
        else throw new MyExceptions("Please provide a boolean expression in an if statement");

    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }
}
