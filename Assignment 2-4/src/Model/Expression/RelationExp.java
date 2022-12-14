package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Exceptions.MyExceptions;
import Model.Type.IntType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntValue;

public class RelationExp implements IExp{
    IExp e1;
    IExp e2;

    String op;

    public RelationExp(String op, IExp e1, IExp e2)
    {
        this.e1=e1;
        this.e2=e2;
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
                IntValue i1=(IntValue) v1;
                IntValue i2=(IntValue) v2;
                int n1,n2;
                n1=i1.getVal();
                n2=i2.getVal();
                if(op.equals("<"))
                {
                    return new BoolValue(n1<n2);
                }
                if(op.equals("<="))
                {
                    return new BoolValue(n1<=n2);
                }
                if(op.equals("=="))
                {
                    return new BoolValue(n1==n2);
                }
                if(op.equals("!="))
                {
                    return new BoolValue(n1!=n2);
                }
                if(op.equals(">"))
                {
                    return new BoolValue(n1>n2);
                }
                if(op.equals(">="))
                {
                    return new BoolValue(n1>=n2);
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
        return new RelationExp(op, e1.deepCopy(), e2.deepCopy());
    }
}
