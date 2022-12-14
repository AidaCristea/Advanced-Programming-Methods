package Model;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyIList;
import Model.ADT.MyIStack;
import Model.Statement.IStmt;
import Model.Value.IValue;
import Model.Value.IntValue;
import Model.Value.RefValue;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrgState {
    MyIStack<IStmt> exeStack;
    MyIDictionary<String, IValue> symTable;
    MyIList<IValue> out;

    MyIDictionary<String,BufferedReader> fileTable;

    MyIHeap heap;
    IStmt originalProgram; //optional



    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String,IValue> symtbl, MyIList<IValue> ot, MyIDictionary<String, BufferedReader> fileTable,MyIHeap heap, IStmt prg)
    {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.fileTable = fileTable;
        this.heap=heap;
        this.originalProgram = prg.deepCopy();//recreate the original prg
        stk.push(this.originalProgram);
    }

    public String toString()
    {
        return "Execution stack: \n" + exeStack.toString() + "\nSymbol table: \n" + symTable.toString() + "\nOutput list: \n" + out.toString() + "\nFile table: \n" + fileTable.toString() + "\nHeap: \n" + heap.toString() +"\n";
    }

    public MyIStack<IStmt> getStk()
    {
        return exeStack;
    }

    public MyIDictionary<String, IValue> getSymTable()
    {
        return symTable;
    }

    public MyIList<IValue> getOut()
    {
        return out;
    }

    public MyIDictionary<String, BufferedReader> getFileTable()
    {
        return this.fileTable;
    }

    public MyIHeap getHeap()
    {
        return this.heap;
    }
    public void setStack(MyIStack<IStmt> newStack)
    {
        this.exeStack = newStack;
    }

    public void setSymTbl(MyIDictionary<String, IValue> newSymTbl)
    {
        this.symTable= newSymTbl;
    }

    public void setOut(MyIList<IValue> newOut)
    {
        this.out=newOut;
    }

    public void setFileTable(MyIDictionary<String, BufferedReader> newFt)
    {
        this.fileTable=newFt;
    }

    public void setHeap(MyIHeap newHeap)
    {
        this.heap=newHeap;
    }



}
