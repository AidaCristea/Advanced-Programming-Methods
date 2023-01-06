package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.PrgState;
import Model.Type.IType;

public class NopStmt implements IStmt{

    public String toString()
    {
        return "NopStatement";
    }

    @Override
    public PrgState execute(PrgState state){
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        return typeEnv;
    }
}
