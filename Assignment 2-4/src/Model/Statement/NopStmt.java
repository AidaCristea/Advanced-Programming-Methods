package Model.Statement;

import Model.Exceptions.MyExceptions;
import Model.PrgState;

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
}
