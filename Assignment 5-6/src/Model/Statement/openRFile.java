package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.IType;
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

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new openRFile(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        if(exp.typecheck(typeEnv).equal(new StringType()))
            return typeEnv;
        else throw new MyExceptions("OpenReadFile requires a string expression");
    }

    public String toString()
    {
        return "OpenRFile " + exp.toString();
    }
}
