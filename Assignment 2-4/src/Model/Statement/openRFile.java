package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class openRFile implements IStmt{
    private final IExp exp;

    public openRFile(IExp e)
    {
        this.exp=e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        IValue value = exp.eval(state.getSymTable(), state.getHeap());
        if(value.getType().equal(new StringType()))
        {
            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if(!fileTable.isDefined(fileName.getValue()))
            {
                BufferedReader br;
                try
                {
                    br = new BufferedReader(new FileReader(fileName.getValue()));
                } catch (FileNotFoundException e)
                {
                    throw new MyExceptions("Could not be opened " + fileName.getValue());
                }
                fileTable.put(fileName.getValue(), br);
                state.setFileTable(fileTable);
            }
            else
            {
                throw new MyExceptions(fileName.getValue() + " is already opened");
            }
        }
        else
        {
            throw new MyExceptions(exp.toString() + " does not evaluate to StringType");
        }

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new openRFile(exp.deepCopy());
    }

    public String toString()
    {
        return "OpenRFile " + exp.toString();
    }
}
