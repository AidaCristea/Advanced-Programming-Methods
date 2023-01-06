package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.IType;
import Model.Type.RefType;
import Model.Value.IValue;
import Model.Value.RefValue;

public class NewStmt implements IStmt{
    private final String varName;
    private final IExp expression;

    public NewStmt(String varN, IExp exp)
    {
        this.varName=varN;
        this.expression=exp;
    }


    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIDictionary<String, IValue> symTable =state.getSymTable();
        MyIHeap heap=state.getHeap();
        if(!symTable.isDefined(varName))
            throw new MyExceptions(varName + " is not in symTable");
        IValue varValue=symTable.lookup(varName);
        if(!(varValue.getType() instanceof RefType))
            throw new MyExceptions(varName + " is not a RefType");
        IValue evaluated=expression.eval(symTable,heap);
        IType locationType = ((RefValue)varValue).getLocationType();
        if(!locationType.equal(evaluated.getType()))
            throw new MyExceptions(varName + " not of " + evaluated.getType().toString());
        int newPos=heap.add(evaluated);
        symTable.put(varName, new RefValue(newPos, locationType));
        state.setSymTbl(symTable);
        state.setHeap(heap);
        return null;

    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType typeVar = typeEnv.lookup(varName);
        IType typeExp = expression.typecheck(typeEnv);
        if(typeVar.equal(new RefType(typeExp)))
        {
            return typeEnv;
        }
        else throw new MyExceptions("New statement: right hand side and left hand side have different types");
    }

    public String toString()
    {
        return "New(" + varName + "," + expression.toString() + ")";
    }
}
