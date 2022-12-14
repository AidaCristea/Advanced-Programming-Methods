package Model.Type;

import Model.Value.BoolValue;
import Model.Value.IValue;

public class BoolType implements IType{

    public String toString()
    {
        return "Bool";
    }

    @Override
    public boolean equal(IType anotherType) {
        if (anotherType instanceof  BoolType)
            return true;
        return false;
    }

    @Override
    public IValue deafultValue() {
        return new BoolValue(false);
    }

    @Override
    public IType deepCopy() {
        return new BoolType();
    }
}
