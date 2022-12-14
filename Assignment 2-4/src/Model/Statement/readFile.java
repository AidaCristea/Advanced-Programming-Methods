package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.IntValue;
import Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class readFile implements IStmt{
    private final IExp exp;
    private final String varName;

    public readFile(IExp e, String s)
    {
        this.exp=e;
        this.varName=s;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if(symTable.isDefined(varName))
        {
            IValue value = symTable.lookup(varName);
            if(value.getType().equal(new IntType()))
            {
                value = exp.eval(symTable, state.getHeap());
                if(value.getType().equal(new StringType()))
                {
                    StringValue castValue = (StringValue) value;
                    if(fileTable.isDefined(castValue.getValue()))
                    {
                        BufferedReader br = fileTable.lookup(castValue.getValue());
                        try
                        {
                            String line =br.readLine();
                            if(line==null)
                                line="0";
                            symTable.put(varName, new IntValue(Integer.parseInt(line)));
                        } catch (IOException e)
                        {
                            throw new MyExceptions("Could not read from file " + castValue);
                        }
                    }
                    else
                    {
                        throw new MyExceptions("The file table does not contain " + castValue);
                    }
                }
                else
                {
                    throw new MyExceptions(value + " does not evaluate to StringType");
                }
            }
            else
            {
                throw new MyExceptions(value + " is not of type IntType");
            }
        }
        else
        {
            throw new MyExceptions(varName + " is not present in the symTable");
        }
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new readFile(exp.deepCopy(), varName);
    }

    public String toString()
    {
        return "ReadFile(" + exp.toString() + "," + varName + ")";
    }

}
