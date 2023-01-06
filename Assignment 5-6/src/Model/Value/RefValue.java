package Model.Value;

import Model.Type.IType;
import Model.Type.RefType;

public class RefValue implements IValue{
    private final int address;
    private final IType locationType;

    public RefValue(int addr, IType locType)
    {
        this.address=addr;
        this.locationType=locType;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    public int getAddress()
    {
        return address;
    }

    public IType getLocationType()
    {
        return locationType;
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(address, locationType.deepCopy());
    }

    public String toString()
    {
        return "("+String.valueOf(address) + "," + locationType.toString() +")";
    }

}
