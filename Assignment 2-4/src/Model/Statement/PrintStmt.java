package Model.Statement;

import Model.ADT.MyIList;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Value.IValue;

public class PrintStmt implements IStmt{
    IExp exp;
    public PrintStmt(IExp e)
    {
        this.exp=e;
    }

    public String toString()
    {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIList<IValue> out = state.getOut();
        out.add(exp.eval(state.getSymTable(), state.getHeap()));
        state.setOut(out);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }
}
