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
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    IRepository repo;
    ExecutorService executor;
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


    public void conservativeGarbageCollector(List<PrgState> prgStates)
    {

        List<Integer> symTableAddresses = Objects.requireNonNull(prgStates.stream()
                .map(p -> getAddrFromSymTable(p.getSymTable().values())))
                .map(Collection::stream)
                .reduce(Stream::concat).orElse(null)
                .collect(Collectors.toList());

        prgStates.forEach(p->
        {
            p.getHeap().setContent((HashMap<Integer, IValue>) safeGarbageCollector(symTableAddresses, getAddrFromHeap(p.getHeap().getContent().values()), p.getHeap().getContent()));

        });
    }


    /*
    public PrgState oneStepExecution(PrgState state) throws MyExceptions
    {
        MyIStack<IStmt> stk=state.getStk();
        if(stk.isEmpty()) throw new MyExceptions("Prgstate stack is empty");

        IStmt crtStmt = stk.pop();
        state.setStack(stk);
        return crtStmt.execute(state);
    }*/

    void oneStepForAllPrg(List<PrgState> prgList) throws MyExceptions, IOException, InterruptedException {
        prgList.forEach(prg -> {
            try
            {
                repo.logPrgStateExec(prg);
                display(prg);
            }
            catch (IOException | MyExceptions e)
            {
                System.out.println(e.getMessage());
            }
        });

        //prepare the list of callables; makes threads
        List<Callable<PrgState>> callList =prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(p::oneStepExecution))
                .collect(Collectors.toList());

        //start the execution of the callables
        //it returns the list of new created PrgStates (namely threads)
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future ->
                {
                    try
                    {
                        try
                        {
                            return future.get();
                        } catch ( InterruptedException e)
                        {
                            e.printStackTrace();
                            return null;
                        }
                    }
                    catch (ExecutionException e)
                    {
                        System.out.println(e.getMessage());
                        return null;
                    }
                    //return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);

        //print the PrgState List into the log file
        prgList.forEach( p -> {
            try
            {
                repo.logPrgStateExec(p);
                display(p);
            } catch (IOException | MyExceptions e)
            {
                System.out.println(e.getMessage());
            }

        });

        //save the current programs in the repository
        repo.setPrgList(prgList);

    }

    //new version of the allStep()
    public void allStep()
    {
        executor = Executors.newFixedThreadPool(2);

        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while(prgList.size() > 0)
        {
            try{
                oneStepForAllPrg(prgList);
                conservativeGarbageCollector(prgList);
                prgList.forEach(prg -> {
                    try
                    {
                        repo.logPrgStateExec(prg);
                        display(prg);
                    }
                    catch (IOException | MyExceptions e)
                    {
                        System.out.println(e.getMessage());
                    }
                });

                prgList= removeCompletedPrg(repo.getPrgList());
            } catch (MyExceptions | IOException | InterruptedException e)
            {
                System.out.println(e.getMessage());
            }


            //remove the completed programs
            //prgList= removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();

        // HERE the repo still contains at least one Completed Prg
        // and its List<PrgState> is not empty.
        //prgList= removeCompletedPrg(repo.getPrgList());
        // update the repo state
        repo.setPrgList(prgList);
    }

    /*
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


            prg.getHeap().setContent((HashMap<Integer,IValue>)safeGarbageCollector(
                    getAddrFromSymTable(prg.getSymTable().getContent().values()),
                    getAddrFromHeap(prg.getHeap().getContent().values()),
                    prg.getHeap().getContent()));

            //prg.getHeap().setContent((HashMap<Integer,IValue>)newHeap);
            repo.logPrgStateExec();
            display();
        }

    }
    */
    /*
    private void display()
    {
        if(displayFlag)
        {
            System.out.println(this.repo.getCrtPrg().toString());
        }
    }*/

    private void display(PrgState prg)
    {
        if (displayFlag)
        {
            System.out.println(prg.toString());
        }
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList)
    {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }



}
