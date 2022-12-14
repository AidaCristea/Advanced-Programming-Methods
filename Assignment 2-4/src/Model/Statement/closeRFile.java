package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFile implements IStmt{
    private final IExp exp;

    public closeRFile(IExp e)
    {
        this.exp=e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        IValue value = exp.eval(state.getSymTable(), state.getHeap());
        if(!value.getType().equal(new StringType()))
        {
            throw new MyExceptions(exp + " does not evaluate to StringValue");
        }
        StringValue fileName = (StringValue) value;
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        if(!fileTable.isDefined(fileName.getValue()))
        {
            throw new MyExceptions(value + " is not present in the FileTable");
        }
        BufferedReader br = fileTable.lookup(fileName.getValue());
        try
        {
            br.close();
        } catch (IOException e)
        {
            throw new MyExceptions("Unexpected error in closing " + value);
        }
        fileTable.remove(fileName.getValue());
        state.setFileTable(fileTable);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new closeRFile(exp.deepCopy());
    }

    public String toString()
    {
        return "CloseReadFile( " + exp.toString() + " )";
    }
}
