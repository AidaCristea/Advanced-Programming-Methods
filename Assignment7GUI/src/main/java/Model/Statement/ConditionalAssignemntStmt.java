package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.Expression.VarExp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;

public class ConditionalAssignemntStmt implements IStmt{
    private final String var;
    private final IExp exp1;
    private final IExp exp2;
    private final IExp exp3;

    public ConditionalAssignemntStmt(String v, IExp e1, IExp e2, IExp e3)
    {
        this.var=v;
        this.exp1=e1;
        this.exp2=e2;
        this.exp3=e3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIStack<IStmt> stk= state.getStk();
        IStmt converted = new IfStmt(exp1, new AssignStmt(var, exp2), new AssignStmt(var, exp3));
        stk.push(converted);
        state.setStack(stk);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ConditionalAssignemntStmt(var, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType varType = new VarExp(var).typecheck(typeEnv);
        IType t1=exp1.typecheck(typeEnv);
        IType t2=exp2.typecheck(typeEnv);
        IType t3=exp3.typecheck(typeEnv);
        if(t1.equal(new BoolType()) && t2.equal(varType) && t3.equal(varType))
            return typeEnv;
        else throw new MyExceptions("The conditional assignment is invalid");
    }

    public String toString()
    {
        return var + "=)" + exp1 + ")? " + exp2 + ":" + exp3;
    }
}
