package Model.Value;

import Model.Type.IType;
import Model.Type.StringType;

public class StringValue implements IValue{
    private final String value;

    public StringValue(String value)
    {
        this.value=value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(value);
    }

    public String getValue()
    {
        return this.value;
    }

    @Override
    public boolean equals(Object anotherValue)
    {
        if(!(anotherValue instanceof StringValue))
            return false;
        StringValue castValue = (StringValue) anotherValue;
        return this.value.equals(castValue.value);
    }

    public String toString()
    {
        return this.value ;
    }
}
