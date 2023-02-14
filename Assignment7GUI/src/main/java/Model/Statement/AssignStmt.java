package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IValue;

public class AssignStmt implements IStmt{
    final private String id;
    final private IExp exp;

    public AssignStmt(String id, IExp exp)
    {
        this.id=id;
        this.exp=exp;
    }

    public String toString()
    {
        return id+ "=" + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        //MyIStack<IStmt> stk = state.getStk();
        MyIDictionary<String, IValue> symTbl = state.getSymTable();

        if (symTbl.isDefined(id))
        {

            IValue val = exp.eval(symTbl, state.getHeap());
            IType typId =(symTbl.lookup(id)).getType();
            IType get = val.getType();
            if(get.equal(typId))
            {
                symTbl.update(id,val);
            }
            else
            {
                throw new MyExceptions("Declared type of variable " + id +" and type of the assigned expression do not match");

            }
        }
        else
        {
            throw new MyExceptions("The used variable " + id + " was not declared before");
        }
        state.setSymTbl(symTbl);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType typevar = typeEnv.lookup(id);
        IType typexp =exp.typecheck(typeEnv);
        if(typevar.equal(typexp))
            return typeEnv;
        else throw new MyExceptions("Assignment: right hand side and left hand side have different types");
    }


}
