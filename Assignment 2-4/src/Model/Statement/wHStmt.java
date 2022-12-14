package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Value.IValue;
import Model.Value.RefValue;

public class wHStmt implements IStmt{
    private final String varName;
    private final IExp expression;

    public wHStmt(String vN, IExp exp)
    {
        this.varName=vN;
        this.expression=exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIDictionary<String, IValue> symTable=state.getSymTable();
        MyIHeap heap=state.getHeap();
        if(!symTable.isDefined(varName))
            throw new MyExceptions(varName +" is not present in the SymTable");
        IValue value=symTable.lookup(varName);
        if(!(value instanceof RefValue))
            throw new MyExceptions(value.toString() + " not od RefType");
        RefValue refVal=(RefValue) value;
        IValue evaluated = expression.eval(symTable, heap);
        if(!evaluated.getType().equal(refVal.getLocationType()))
            throw new MyExceptions(evaluated.toString() + " not of " + refVal.getLocationType().toString());
        heap.update(refVal.getAddress(), evaluated);
        state.setHeap(heap);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new wHStmt(varName, expression.deepCopy());
    }

    public String toString()
    {
        return "WriteHeap("+ varName +"," + expression.toString() +")";
    }
}
