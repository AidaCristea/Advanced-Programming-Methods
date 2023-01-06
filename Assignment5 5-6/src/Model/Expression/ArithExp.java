package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Type.IType;
import Model.Type.IntType;
import Model.Value.IValue;
import Model.Value.IntValue;

import java.util.HexFormat;


public class ArithExp implements IExp{
    IExp e1;
    IExp e2;
    //int op; //1-plus, 2-minus, 3-star, 4-divide
    char op;


    public ArithExp (char op, IExp e1, IExp e2)
    {
        this.e1=e1;
        this.e2 =e2;
        this.op=op;
    }
    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, MyIHeap heap) throws MyExceptions {
        IValue v1,v2;
        v1=e1.eval(tbl, heap);
        if(v1.getType().equal(new IntType()))
        {
            v2=e2.eval(tbl, heap);
            if(v2.getType().equal(new IntType()))
            {
                IntValue i1 =(IntValue) v1; //downcasting
                IntValue i2= (IntValue) v2;
                int n1, n2;
                n1=i1.getVal();
                n2=i2.getVal();
                if(op=='+')
                {
                    return new IntValue(n1+n2);

                }
                if(op=='-')
                {
                    return new IntValue(n1-n2);
                }
                if(op=='*')
                {
                    return new IntValue(n1*n2);
                }
                if(op=='/')
                {
                    if(n2==0)
                        throw new MyExceptions("Division by zero");
                    else return new IntValue(n1/n2);
                }
            }
            else throw new MyExceptions("Second operand is not an integer");

        }
        else throw new MyExceptions("First operand is not an integer");
        return null;
    }

    public String toString()
    {
        return e1.toString() + " " + op + " " + e2.toString();
    }

    @Override
    public IExp deepCopy() {
        return new ArithExp(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyExceptions {
        IType typ1, typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if (typ1.equal(new IntType()))
        {
            if (typ2.equal(new IntType()))
                return new IntType();
            else throw new MyExceptions("second operand is not an integer");
        }
        else throw new MyExceptions("first operand is not an integer");
    }
}
