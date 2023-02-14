package gui;

import Controller.Controller;
import Model.ADT.MyDictionary;
import Model.ADT.MyHeap;
import Model.ADT.MyList;
import Model.ADT.MyStack;
import Model.Exceptions.MyExceptions;
import Model.Expression.*;
import Model.PrgState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.RefType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Repository.IRepository;
import Repository.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProgramChooserController {

    private ProgramExecutorController prgExecCtrl;

    public void setPrgExecCtrl(ProgramExecutorController prgExec)
    {
        this.prgExecCtrl=prgExec;
    }
    @FXML
    private ListView<IStmt> prgsListView;

    @FXML
    private Button displayButton;

    public void initialize()
    {
        prgsListView.setItems(getAllStmts());
        prgsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void displayProgram(ActionEvent actEv)
    {
        IStmt selectedStmt = prgsListView.getSelectionModel().getSelectedItem();
        if(selectedStmt==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ecountered!");
            alert.setContentText("No statement selected!");
            alert.showAndWait();
        }
        else
        {
            int id =prgsListView.getSelectionModel().getSelectedIndex();
            try
            {
                selectedStmt.typecheck(new MyDictionary<>());
                PrgState prgState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), selectedStmt);
                IRepository repo=new Repository(prgState, "log" + (id + 1) + ".txt");
                Controller ctrl= new Controller(repo);
                prgExecCtrl.setCtrl(ctrl);


            } catch (MyExceptions | IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error encountered");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }

        }
    }


    //observableList is from javafx and returns the collection
    private ObservableList<IStmt> getAllStmts() {
        List<IStmt> allStmts = new ArrayList<>();

        IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));
        allStmts.add(ex1);


        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
        allStmts.add(ex2);

        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));
        allStmts.add(ex3);

        IStmt ex4= new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new openRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new closeRFile(new VarExp("varf"))))))))));
        allStmts.add(ex4);


        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelationExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v",new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));
        allStmts.add(ex5);


        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)
        //At the end: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={(1,int),(2,Ref int)}
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a")))))));
        allStmts.add(ex6);


        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
        //At the end: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={20, 25}
        IStmt ex7=new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a",new VarExp("v")),
                                        new CompStmt(new PrintStmt(new rHExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+',new rHExp(new rHExp(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));
        allStmts.add(ex7);


        //Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
        //At the end: Heap={1->30}, SymTable={v->(1,int)} and Out={20, 35}
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt( new PrintStmt(new rHExp(new VarExp("v"))),
                                new CompStmt(new wHStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+', new rHExp(new VarExp("v")), new ValueExp(new IntValue(5))))))));
        allStmts.add(ex8);



        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStmt ex9= new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType( new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new rHExp(new rHExp(new VarExp("a")))))))));
        allStmts.add(ex9);



        //int v; Ref int a; v=10;new(a,22);
        //fork(wH(a,30);v=32;print(v);print(rH(a)));
        //print(v);print(rH(a))
        IStmt ex10= new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new forkStmt(new CompStmt(new wHStmt("a", new ValueExp(new IntValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rHExp(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rHExp(new VarExp("a")))))))));
        allStmts.add(ex10);



        //SLEEP STMT
        //v=0;
        //(while(v<3) (fork(print(v);v=v+1);v=v+1);
        //sleep(5);
        //print(v*10)
        //The final Out should be {0,1,2,30}

        IStmt ex11=new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(0))),
                        new CompStmt(
                                new WhileStmt(new RelationExp("<", new VarExp("v"), new ValueExp(new IntValue(3))),
                                        new CompStmt(
                                                new forkStmt(
                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("v")),
                                                                new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                                new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new CompStmt(new SleepStmt(5), new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10)))))

                        )
                )
        );

        allStmts.add(ex11);



        //MULStmt
        //v1=2;v2=3; (if (v1) then print(MUL(v1,v2)) else print (v1))
        //The final Out should be {1}

        IStmt ex12 = new CompStmt(
                new VarDeclStmt("v1", new IntType()),
                new CompStmt(
                        new AssignStmt("v1", new ValueExp(new IntValue(2))),
                        new CompStmt(
                                new VarDeclStmt("v2", new IntType()),
                                new CompStmt(
                                        new AssignStmt("v2", new ValueExp(new IntValue(3))),
                                        new IfStmt(new RelationExp("!=", new VarExp("v1"), new ValueExp(new IntValue(0))),
                                                new PrintStmt(new MULExp(new VarExp("v1"), new VarExp("v2"))),
                                                new PrintStmt(new VarExp("v1")))

                                        )

                                )
                        )
                );
        allStmts.add(ex12);


        //WAIT Stmt
        //v=20; wait(10);print(v*10)
        //The final Out should be {20,10,9,8,7,6,5,4,3,2,1,200}

        IStmt ex13= new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new WaitStmt(10),
                                new PrintStmt(
                                        new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10))))
                                )
                        )
                );
        allStmts.add(ex13);



        //REPEAT UNTIL
        //v=0;
        //(repeat (fork(print(v);v=v-1);v=v+1) until v==3);
        //x=1;y=2;z=3;w=4;
        //print(v*10)
        //The final Out should be {0,1,2,30}

        IStmt ex14 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(0))),
                        new CompStmt(
                                new RepeatUntilStmt(
                                        new CompStmt(
                                                new forkStmt(
                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("v")),
                                                                new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                                new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))))),
                                        new RelationExp("==", new VarExp("v"), new ValueExp(new IntValue(3)))
                                                   ),
                                new CompStmt(
                                        new VarDeclStmt("x", new IntType()),
                                        new CompStmt(
                                                new VarDeclStmt("y", new IntType()),
                                                new CompStmt(
                                                        new VarDeclStmt("z", new IntType()),
                                                        new CompStmt(
                                                                new VarDeclStmt("w", new IntType()),
                                                                new CompStmt(
                                                                        new AssignStmt("x", new ValueExp(new IntValue(1))),
                                                                        new CompStmt(
                                                                                new AssignStmt("y", new ValueExp(new IntValue(2))),
                                                                                new CompStmt(
                                                                                        new AssignStmt("z", new ValueExp(new IntValue(3))),
                                                                                        new CompStmt(
                                                                                                new AssignStmt("w", new ValueExp(new IntValue(4))),
                                                                                                new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10))))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )

                        )
                )
        );
        allStmts.add(ex14);


        //CONDITIONAL ASSIGNMENT STMT
        //bool b;
        //int c;
        //b=true;
        //c=b?100:200;
        //print(c);
        //c= (false)?100:200;
        //print(c);
        //The final Out should be {100,200}

        IStmt ex15 = new CompStmt(
                new VarDeclStmt("b", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("c", new IntType()),
                        new CompStmt(
                                new AssignStmt("b", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new ConditionalAssignemntStmt("c", new VarExp("b"), new ValueExp(new IntValue(100)), new ValueExp(new IntValue(200))),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("c")),
                                                new CompStmt(
                                                        new ConditionalAssignemntStmt("c", new ValueExp(new BoolValue(false)), new ValueExp(new IntValue(100)), new ValueExp(new IntValue(200))),
                                                        new PrintStmt(new VarExp("c"))
                                                )
                                        )
                                )


                        )
                )
        );
        allStmts.add(ex15);


        //SWITCH STMT
        //int a; int b; int c;
        //a=1;b=2;c=5;
        //(switch(a*10)
        //(case (b*c) : print(a);print(b))
        //(case (10) : print(100);print(200))
        //(default : print(300)));
        //print(300)
        //The final Out should be {1,2,300}

        IStmt ex16 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new VarDeclStmt("c", new IntType()),
                                new CompStmt(
                                        new AssignStmt("a", new ValueExp(new IntValue(1))),
                                        new CompStmt(
                                                new AssignStmt("b", new ValueExp(new IntValue(2))),
                                                new CompStmt(
                                                        new AssignStmt("c", new ValueExp(new IntValue(5))),
                                                        new CompStmt(
                                                                new SwitchStmt(new ArithExp('*', new VarExp("a"), new ValueExp(new IntValue(10))),
                                                                               new ArithExp('*',new VarExp("b"), new VarExp("c") ),
                                                                               new CompStmt(
                                                                                       new PrintStmt(new VarExp("a")),
                                                                                       new PrintStmt(new VarExp("b"))
                                                                               ),
                                                                               new ValueExp(new IntValue(10)),
                                                                               new CompStmt(
                                                                                       new PrintStmt(new ValueExp(new IntValue(100))),
                                                                                       new PrintStmt(new ValueExp(new IntValue(200)))
                                                                               ),
                                                                               new PrintStmt(new ValueExp(new IntValue(300)))
                                                                        ),
                                                                new PrintStmt(new ValueExp(new IntValue(300)))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStmts.add(ex16);


        //FOR STMT
        //Ref int a; new(a,20);
        //(for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
        //print(rh(a))
        //The final Out should be {0,1,2,20}

        IStmt ex17 = new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("a", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("v", new IntType()),
                                new CompStmt(
                                        new ForStmt("v", new ValueExp(new IntValue(0)), new ValueExp(new IntValue(3)), new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))),
                                                new forkStmt(
                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("v")),
                                                                new AssignStmt("v", new ArithExp('*', new VarExp("v"), new rHExp(new VarExp("a"))))
                                                        )
                                                )),
                                        new PrintStmt(new rHExp(new VarExp("a")))
                                )
                        )

                )
        );
        allStmts.add(ex17);

        //DO WHILE STATEMENT
        IStmt ex18 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new DoWhileStmt(new RelationExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1))))
                                        )),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );
        allStmts.add(ex18);


        //REPEAT UNTIL STMT 2
        //int v; int x; int y; v=0;
        //(repeat (fork(print(v);v=v-1);v=v+1) until v==3);
        //x=1;nop;y=3;nop;
        //print(v*10)
        //The final Out should be {0,1,2,30}

        IStmt ex19 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("x", new IntType()),
                        new CompStmt(
                                new VarDeclStmt("y", new IntType()),
                                new CompStmt(
                                        new AssignStmt("v", new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new RepeatUntilStmt(
                                                        new CompStmt(
                                                                new forkStmt(
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)))))
                                                                ),
                                                                new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))))
                                                        ),
                                                        new RelationExp("==", new VarExp("v"), new ValueExp(new IntValue(3)))
                                                ),
                                                new CompStmt(
                                                        new AssignStmt("x", new ValueExp(new IntValue(1))),
                                                        new CompStmt(
                                                                new NopStmt(),
                                                                new CompStmt(
                                                                        new AssignStmt("y", new ValueExp(new IntValue(3))),
                                                                        new CompStmt(
                                                                                new NopStmt(),
                                                                                new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10))))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStmts.add(ex19);


        //FOR STMT 2
        //Ref int a; new(a,20);
        //(for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
        //print(rh(a))
        //The final Out should be {0,1,2,20}

        IStmt ex20=new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("a", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("v", new IntType()),
                                new CompStmt(
                                        new ForStmt("v", new ValueExp(new IntValue(0)), new ValueExp(new IntValue(3)), new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))),
                                                new forkStmt(
                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("v")),
                                                                new AssignStmt("v", new ArithExp('*', new VarExp("v"), new rHExp(new VarExp("a"))))
                                                        )
                                                )),
                                        new PrintStmt(new rHExp(new VarExp("a")))
                                )
                        )
                )
        );
        allStmts.add(ex20);





        //EXAM Conditional Assignment Statement
        //Ref int a; Ref int b; int v;
        //new(a,0); new(b,0);
        //wh(a,1); wh(b,2);
        //v=(rh(a)<rh(b))?100:200;
        //print(v);
        //v= ((rh(b)-2)>rh(a))?100:200;
        //print(v);
        //The final Out should be {100,200}
        IStmt ex21= new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new VarDeclStmt("b", new RefType(new IntType())),
                        new CompStmt(
                                new VarDeclStmt("v", new IntType()),
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new NewStmt("b", new ValueExp(new IntValue(0))),
                                                new CompStmt(
                                                        new wHStmt("a", new ValueExp(new IntValue(1))),
                                                        new CompStmt(
                                                                new wHStmt("b", new ValueExp(new IntValue(2))),
                                                                new CompStmt(
                                                                        new ConditionalAssignemntStmt("v", new RelationExp("<", new rHExp(new VarExp("a")), new rHExp(new VarExp("b"))),
                                                                                new ValueExp(new IntValue(100)), new ValueExp(new IntValue(200))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new CompStmt(
                                                                                        new ConditionalAssignemntStmt("v", new RelationExp(">", new ArithExp('-', new rHExp(new VarExp("b")), new ValueExp(new IntValue(2))),
                                                                                                new rHExp(new VarExp("a"))), new ValueExp(new IntValue(100)), new ValueExp(new IntValue(200))),
                                                                                        new PrintStmt(new VarExp("v"))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStmts.add(ex21);


        return FXCollections.observableArrayList(allStmts);

    }


    /*
    public void displayButtonClicked()
    {
        System.out.println("User click button....");
    }*/

}
