package Model.Type;

import Model.Value.IValue;
import Model.Value.IntValue;

public class IntType implements IType {

    public String toString()
    {
        return "int";
    }

    @Override
    public boolean equal(IType anotherType) {
        if (anotherType instanceof IntType)
            return true;
        return false;
    }

    @Override
    public IValue deafultValue() {
        return new IntValue(0);
    }

    @Override
    public IType deepCopy() {
        return new IntType();
    }
}
