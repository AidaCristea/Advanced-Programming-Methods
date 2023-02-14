package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.PrgState;
import Model.Type.IType;

public interface IStmt {
    PrgState execute(PrgState state) throws MyExceptions;
    IStmt deepCopy();
    MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions;

}
