package Model.Value;

import Model.Type.IType;
import Model.Type.IntType;

public class IntValue implements IValue{
    private final int val;
    public IntValue(int v)
    {
        this.val=v;
    }
    public int getVal()
    {
        return this.val;
    }
    public String toString()
    {
        return String.valueOf(val);
    }
    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public IValue deepCopy() {
        return new IntValue(val);
    }
}
