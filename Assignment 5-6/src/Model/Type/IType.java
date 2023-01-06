package Model.Type;

import Model.Value.IValue;

public interface IType {
    boolean equal(IType anotherType);
    IValue deafultValue();
    IType deepCopy();

}
