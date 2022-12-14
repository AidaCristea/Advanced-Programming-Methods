package Controller;

import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.ValueExp;
import Model.Expression.VarExp;
import Model.PrgState;
import Model.Statement.*;
import Model.Type.IntType;
import Model.Value.IValue;
import Model.Value.IntValue;
import Model.Value.RefValue;
import Repository.IRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {
    IRepository repo;
    boolean displayFlag=false; //if displayFlag is true, then display the program state after each execution step

    public void setDisplayFlag(boolean val)
    {
        this.displayFlag = val;
    }
    public Controller(IRepository r)
    {
        this.repo=r;
    }

    public List<Integer> getAddrFromHeap(Collection<IValue> heapValues)
    {
        return heapValues.stream()
                .filter(v-> v instanceof  RefValue)
                .map(v->{RefValue v1= (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }


    public  Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, IValue> heap)
    {
        return heap.entrySet().stream()
                .filter(e->(symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues)
    {
        return symTableValues.stream()
                .filter(v->v instanceof RefValue)
                .map(v->{RefValue v1=(RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }



    public PrgState oneStepExecution(PrgState state) throws MyExceptions
    {
        MyIStack<IStmt> stk=state.getStk();
        if(stk.isEmpty()) throw new MyExceptions("Prgstate stack is empty");

        IStmt crtStmt = stk.pop();
        state.setStack(stk);
        return crtStmt.execute(state);
    }

    //complete execution of a program
    public void allStep() throws MyExceptions, IOException
    {
        PrgState prg = repo.getCrtPrg();
        this.repo.logPrgStateExec();
        display();
        while (!prg.getStk().isEmpty())
        {
            oneStepExecution(prg);
            this.repo.logPrgStateExec();
            /*
            Map<Integer, IValue> newHeap = safeGarbageCollector(getAddrFromSymTable(prg.getSymTable().values()),
                    getAddrFromHeap(prg.getHeap().getContent().values()),
                    prg.getHeap().getContent());*/

            prg.getHeap().setContent((HashMap<Integer,IValue>)safeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().getContent().values()),
                    getAddrFromHeap(prg.getHeap().getContent().values()),
                    prg.getHeap().getContent()));

            //prg.getHeap().setContent((HashMap<Integer,IValue>)newHeap);
            repo.logPrgStateExec();
            display();
        }
    }
    private void display()
    {
        if(displayFlag)
        {
            System.out.println(this.repo.getCrtPrg().toString());
        }
    }

}
