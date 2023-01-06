package Model.ADT;

import Model.Exceptions.MyExceptions;

import java.util.Collection;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T>{
    Stack<T> stack;

    public MyStack()
    {
        this.stack = new Stack<>();
    }
    @Override
    public T pop() throws MyExceptions {
        if(stack.isEmpty())
            throw new MyExceptions("Stack is empty!");
        return this.stack.pop();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        if(stack.isEmpty())
            return true;
        return false;
    }

    @Override
    public T peek() {
        return this.stack.peek();
    }

    public String toString()
    {
        return this.stack.toString();
    }
}
