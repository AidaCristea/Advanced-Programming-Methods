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
    private boolean first;

    public Repository(PrgState programstate, String logFilePath) throws IOException
    {
        this.logFilePath = logFilePath;
        this.programStates = new ArrayList<>();
        this.currentIndex=0;
        this.addPrg(programstate);
        this.emptyLogFile();
        this.first=true;

    }

    public int getCurrentIndex()
    {
        return currentIndex;
    }

    public void setCurrentIndex(int cP)
    {
        this.currentIndex=cP;
    }

    /*
    @Override
    public PrgState getCrtPrg()
    {
        return this.programStates.get(this.currentIndex);
    }*/

    @Override
    public void addPrg(PrgState prog) {
        this.programStates.add(prog);

    }

    @Override
    public List<PrgState> getPrgList() {
        return this.programStates;
    }

    @Override
    public void setPrgList(List<PrgState> newProgStates) {
        this.programStates=newProgStates;
    }

    @Override
    public void logPrgStateExec(PrgState programState) throws MyExceptions, IOException {

        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        logFile.println(programState.toString());
        logFile.close();



    }
    /*
    @Override
    public void logPrgStateExec() throws MyExceptions, IOException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        logFile.println(this.programStates.get(0).toString());
        logFile.close();
    }*/

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
