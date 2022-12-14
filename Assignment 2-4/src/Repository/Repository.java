package Repository;

import Model.ADT.MyIList;
import Model.Exceptions.MyExceptions;
import Model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    List<PrgState> programStates;
    int currentIndex;
    private final String logFilePath;

    public Repository(PrgState programstate, String logFilePath)
    {
        this.logFilePath = logFilePath;
        this.programStates = new ArrayList<>();
        this.currentIndex=0;
        this.addPrg(programstate);

    }

    public int getCurrentIndex()
    {
        return currentIndex;
    }

    public void setCurrentIndex(int cP)
    {
        this.currentIndex=cP;
    }
    @Override
    public PrgState getCrtPrg()
    {
        return this.programStates.get(this.currentIndex);
    }

    @Override
    public void addPrg(PrgState prog) {
        this.programStates.add(prog);

    }

    @Override
    public List<PrgState> getProgramList() {
        return this.programStates;
    }

    @Override
    public void logPrgStateExec() throws MyExceptions, IOException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        logFile.println(this.programStates.get(0).toString());
        logFile.close();
    }

    @Override
    public void emptyLogFile() throws IOException {
        PrintWriter logFile;
        logFile=new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, false)));
        logFile.close();
    }

    public void setProgramStates(List<PrgState> programStates)
    {
        this.programStates=programStates;
    }




}
