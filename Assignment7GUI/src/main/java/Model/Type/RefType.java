package Model.Type;

import Model.Value.IValue;
import Model.Value.RefValue;

//import java.sql.Ref;

public class RefType implements IType{
    private final IType inner;

    public RefType(IType inner)
    {
        this.inner=inner;
    }

    public IType getInner()
    {
        return inner;
    }

    @Override
    public boolean equal(IType anotherType) {
        if(anotherType instanceof RefType)
            return inner.equal(((RefType) anotherType).getInner());
        else
            return false;
    }

    @Override
    public IValue deafultValue() {
        return new RefValue(0,inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner.deepCopy());
    }

    public String toString()
    {
        return "Ref(" + inner.toString() +")";
    }
}
