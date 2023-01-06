package Model.Value;

import Model.Type.BoolType;
import Model.Type.IType;
import Model.Type.IntType;

public class BoolValue implements IValue{
    private  final boolean val;

    public BoolValue(boolean v)
    {
        this.val=v;
    }
    public boolean getVal()
    {
        return val;
    }

    public String toString()
    {
        return String.valueOf(val);
    }
    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(val);
    }
}
