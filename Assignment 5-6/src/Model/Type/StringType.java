package Model.Type;

import Model.Value.IValue;
import Model.Value.StringValue;

public class StringType implements IType{

    @Override
    public boolean equal(IType anotherType) {
        return anotherType instanceof StringType;
    }

    @Override
    public IValue deafultValue() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }



    public String toString()
    {
        return "string";
    }
}
