package Repository;

import Model.Exceptions.MyExceptions;
import Model.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    //PrgState getCrtPrg();
    void addPrg(PrgState prog);
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> newProgStates);

    //void logPrgStateExec() throws MyExceptions, IOException;
    void logPrgStateExec(PrgState programState) throws MyExceptions, IOException;
    void emptyLogFile() throws IOException;

}
