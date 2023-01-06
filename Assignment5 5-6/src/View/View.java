package View;

import Controller.Controller;
import Model.ADT.*;
import Model.Exceptions.MyExceptions;
import Model.Expression.ArithExp;
import Model.Expression.ValueExp;
import Model.Expression.VarExp;
import Model.PrgState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntValue;
import Repository.IRepository;
import Repository.Repository;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Scanner;

public class View {
    public void start()
    {
        boolean doo = false;
        while(!doo) {
            try {
                Menu();
                System.out.println("Enter option: ");
                Scanner readOpt = new Scanner(System.in);
                int opt = readOpt.nextInt();
                if (opt == 0)
                    doo = true;
                else if (opt == 1)
                    example1();
                else if (opt == 2)
                    example2();
                else if (opt == 3)
                    example3();
                else System.out.println("Invalid input!");
            } catch (Exception exception)
            {
                System.out.println(exception.getMessage());
            }
        }
    }

    void Menu()
    {
        System.out.println("Menu: ");
        System.out.println("0.Exit");
        System.out.println("1. Run the first example");
        System.out.println("2. Run the second example");
        System.out.println("3. Run the third example");

    }

    void example1()
    {
        /*
        int v;
        v=2;
        Print(v);*/
        IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
        new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));
        try
        {
            runStatement(ex1);
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }

    }

    void example2()
    {
        /*
        int a;
        int b;
        a=2+3*5;
        b=a+1;
        Print(b);*/
        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
        try
        {
            runStatement(ex2);
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    void example3()
    {
        /*
        bool a;
        int v;
        a=true;
        (If a Then v=2 Else v=3);
        Print(v)*/
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));
        try
        {
            runStatement(ex3);
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    private void runStatement(IStmt statement) throws MyExceptions, IOException {
        MyIStack<IStmt> execStack = new MyStack<>();
        MyIDictionary<String, IValue> symbolTable = new MyDictionary<>();
        MyIList<IValue> output = new MyList<>();
        MyIDictionary<String, BufferedReader> fileTable = new MyDictionary<>();
        MyIHeap heap=new MyHeap();

        PrgState state = new PrgState(execStack, symbolTable, output, fileTable,heap, statement);

        IRepository repo = new Repository(state, "log.txt");
        Controller ctrl = new Controller(repo);

        System.out.println("Do you want to display the steps? (1-yes, 0-no)");
        Scanner readOpt = new Scanner(System.in);
        int opt = readOpt.nextInt();
        if(opt==1)
        {
            ctrl.setDisplayFlag(true);
        }

        ctrl.allStep();
        System.out.println("Result is " + state.getOut().toString());
    }

}
