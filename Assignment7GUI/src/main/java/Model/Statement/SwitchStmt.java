package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.IExp;
import Model.Expression.RelationExp;
import Model.PrgState;
import Model.Type.IType;

public class SwitchStmt implements IStmt{
    private final IExp mainExp;
    private final IExp exp1;
    private final IExp exp2;
    private final IStmt stmt1;
    private final IStmt stmt2;
    private final IStmt defaultStmt;

    public SwitchStmt(IExp mainE, IExp e1, IStmt stmt1, IExp e2, IStmt stmt2, IStmt dStmt)
    {
        this.mainExp=mainE;
        this.exp1=e1;
        this.stmt1=stmt1;
        this.exp2=e2;
        this.stmt2=stmt2;
        this.defaultStmt=dStmt;
    }


    @Override
    public PrgState execute(PrgState state) throws MyExceptions {
        MyIStack<IStmt> stk = state.getStk();
        IStmt converted = new IfStmt(new RelationExp("==", mainExp, exp1),
                stmt1, new IfStmt(new RelationExp("==", mainExp, exp2), stmt2, defaultStmt));
        stk.push(converted);
        state.setStack(stk);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new SwitchStmt(mainExp.deepCopy(), exp1.deepCopy(), stmt1.deepCopy(), exp2.deepCopy(), stmt2.deepCopy(), defaultStmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType mainT= mainExp.typecheck(typeEnv);
        IType t1=exp1.typecheck(typeEnv);
        IType t2=exp2.typecheck(typeEnv);
        if(mainT.equal(t1) && mainT.equal(t2))
        {
            stmt1.typecheck(typeEnv.clone());
            stmt2.typecheck(typeEnv.clone());
            defaultStmt.typecheck(typeEnv.clone());
            return typeEnv;
        }
        else throw new MyExceptions("The expression types don't match in the switch statement");
    }

    public String toString()
    {
        return "switch(" + mainExp + "){(case(" + exp1 + "): " + stmt1+")(case(" + exp2 + "): " + stmt2 + ")(default: " + defaultStmt + ")}";
    }


}
