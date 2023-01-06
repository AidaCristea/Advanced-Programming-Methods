package Model.Expression;


import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;




public class LogicExp implements IExp{
    IExp e1;
    IExp e2;
    String op;
    //toString?

    public LogicExp(IExp exp1, IExp exp2, String opp)
    {
        this.e1=exp1;
        this.e2=exp2;
        this.op=opp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        //fac ca la ArithExp doa ca BoolValue
        IValue v1,v2;
        v1=e1.eval(tbl, heap);
        if(v1.getType().equals(new BoolType()))
        {
            v2=e2.eval(tbl, heap);
            if(v2.getType().equals(new BoolType()))
            {
                BoolValue b1 =(BoolValue) v1;
                BoolValue b2 =(BoolValue) v2;
                boolean n1,n2;
                n1=b1.getVal();
                n2=b2.getVal();
                if(op.equals("and"))
                {
                    return new BoolValue(n1 && n2);
                }
                if(op.equals("or"))
                {
                    return new BoolValue(n1 || n2);
                }
            }
            else throw new MyExceptions("Second operand is not a boolean");
        }
        else throw new MyExceptions("First operand is not a boolean");
        return null;
    }

    public String toString()
    {
        return e1.toString() + " " + op + " " + e2.toString();
    }

    @Override
    public IExp deepCopy() {
        return new LogicExp(e1.deepCopy(), e2.deepCopy(), op);
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType typ1, typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if(typ1.equal(new BoolType()))
        {
            if (typ2.equal(new BoolType()))
            {
                return new BoolType();
            }
            else throw new MyExceptions("second operand is not a boolean");
        }
        else throw new MyExceptions("first operand is not a boolean");
    }
}
