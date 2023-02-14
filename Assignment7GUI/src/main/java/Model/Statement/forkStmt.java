package Model.Statement;

import Model.ADT.*;
import Model.Exceptions.MyExceptions;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IValue;

import java.io.BufferedReader;
import java.util.Map;

public class forkStmt implements  IStmt{
    private final IStmt statement;

    public forkStmt(IStmt stmt)
    {
        this.statement=stmt;
    }
    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIStack<IStmt> newStack = new MyStack<>();
        newStack.push(statement);

        //MyIDictionary<String, IValue> symTbl=state.getSymTable();
        //MyIDictionary<String, IValue> cloneSymTbl = null;

        /*
        try
        {
            cloneSymTbl= (MyIDictionary<String, IValue>) symTbl.clone();
        }catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }*/
        //cloneSymTbl = symTbl.clone();
        //cloneSymTbl=symTbl.copy();
        //MyIDictionary<String, IValue> cc=cloneSymTbl;


        MyIList<IValue> out = state.getOut();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        MyIHeap heap = state.getHeap();



        MyIDictionary<String, IValue> newSymTbl = new MyDictionary<>();
        for (Map.Entry<String, IValue> entry: state.getSymTable().getContent().entrySet()) {
            newSymTbl.put(entry.getKey(), entry.getValue().deepCopy());
        }
        //return new PrgState(newStack, cloneSymTbl, out, fileTable, heap);
        return new PrgState(newStack, newSymTbl, out, fileTable, heap);

    }

    @Override
    public IStmt deepCopy() {
        return new forkStmt(statement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        statement.typecheck(typeEnv.clone());
        return typeEnv;
    }

    public String toString()
    {
        return "Fork("+ statement.toString() + ")";
    }
}
