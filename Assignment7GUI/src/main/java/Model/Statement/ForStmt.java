package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.Expression.RelationExp;
import Model.Expression.VarExp;
import Model.PrgState;
import Model.Type.IType;
import Model.Type.IntType;

public class ForStmt implements IStmt{
    private final String var;
    private final IExp exp1;
    private final IExp exp2;
    private final IExp exp3;
    private final IStmt stmt;

    public ForStmt(String v, IExp e1, IExp e2, IExp e3, IStmt st)
    {
        this.var=v;
        this.exp1=e1;
        this.exp2=e2;
        this.exp3=e3;
        this.stmt=st;
    }


    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIStack<IStmt> stk = state.getStk();;
        IStmt converted = new CompStmt(new AssignStmt("v", exp1),
                new WhileStmt(new RelationExp("<", new VarExp("v"), exp2),
                        new CompStmt(stmt, new AssignStmt("v", exp3))));
        stk.push(converted);
        state.setStack(stk);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ForStmt(var, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy(), stmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType t1=exp1.typecheck(typeEnv);
        IType t2 =exp2.typecheck(typeEnv);
        IType t3=exp3.typecheck(typeEnv);

        if(t1.equal(new IntType()) && t2.equal(new IntType()) && t3.equal(new IntType()))
            return typeEnv;
        else throw new MyExceptions("The for statement is invalid");
    }

    public String toString()
    {
        return "for(" + var + "=" + exp1 + ";" + var + "<" + exp2 +";" + var + "=" + exp3 + ") {" + stmt + "}";
    }
}
