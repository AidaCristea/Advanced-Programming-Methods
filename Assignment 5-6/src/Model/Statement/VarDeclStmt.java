package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.Exceptions.MyExceptions;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IValue;


public class VarDeclStmt implements IStmt{
    String name;
    IType type;

    //toString?

    public VarDeclStmt(String name, IType type)
    {
        this.name=name;
        this.type=type;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIDictionary<String, IValue> table = state.getSymTable();
        if(table.isDefined(name))
        {
            throw new MyExceptions("Variable " + name + " already declared in symtable!");
        }

        table.put(name, type.deafultValue());
        state.setSymTbl(table);
        return null;

    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(name, type);
    }


    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        typeEnv.put(name, type);
        return typeEnv;
    }

    public String toString()
    {
        return type.toString() + " " + name;
    }
}
