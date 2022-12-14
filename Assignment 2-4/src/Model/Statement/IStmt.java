package Model.Statement;

import Model.Exceptions.MyExceptions;
import Model.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws MyExceptions;
    IStmt deepCopy();

}
