package gui;

import Controller.Controller;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyIList;
import Model.ADT.MyList;
import Model.Exceptions.MyExceptions;
import Model.PrgState;
import Model.Statement.IStmt;
import Model.Value.IValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 f, T2 s)
    {
        this.first=f;
        this.second=s;
    }
}

public class ProgramExecutorController {

    private  Controller ctrl;

    @FXML
    private TextField nrPrgStatesTextField;

    @FXML
    private TableView<Pair<Integer, IValue>> heapTableView;

    @FXML
    private TableColumn<Pair<Integer, IValue>, Integer> addressColumn;
    @FXML
    private TableColumn<Pair<Integer, IValue>, String> valueColumn;
    @FXML
    private ListView<String> outputListView;
    @FXML
    private ListView<String> fileTableListView;
    @FXML
    private ListView<Integer> prgStateIdenitifiersListView;

    @FXML
    private TableView<Pair<String, IValue>> symbolTableView;

    @FXML
    private TableColumn<Pair<String, IValue>, String> variableNameColumn;

    @FXML
    private TableColumn<Pair<String, IValue>, String> varValueColumn;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private Button runOneStepButton;

    public void setCtrl(Controller ctrl)
    {
        this.ctrl=ctrl;
        populate();
    }

    public void initialize()
    {
        prgStateIdenitifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addressColumn.setCellValueFactory(p-> new SimpleIntegerProperty(p.getValue().first).asObject());
        valueColumn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().second.toString()));
        variableNameColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().first));
        varValueColumn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().second.toString()));

    }

    private PrgState getCurrentPrgState()
    {
        if (ctrl.getProgStates().size() ==0)
            return null;
        else {
            int currentId = prgStateIdenitifiersListView.getSelectionModel().getSelectedIndex();
            if(currentId==-1)
                return ctrl.getProgStates().get(0);
            else
                return ctrl.getProgStates().get(currentId);
        }
    }

    private void populate()
    {
        populateHeapTableView();
        populateOutputListView();
        populateFileTableListView();
        populatePrgStateIdentifiersListView();;
        populateSymTableView();
        populateExecutionStckListView();

    }



    public void changePrgState(MouseEvent event)
    {
        populateExecutionStckListView();
        populateSymTableView();
    }


    private void populateNrOfPrgStatesTextField()
    {
        List<PrgState> prgStates = ctrl.getProgStates();
        nrPrgStatesTextField.setText(String.valueOf(prgStates.size()));
    }

    private void populateHeapTableView()
    {
        PrgState prgStates = getCurrentPrgState();
        MyIHeap heap = Objects.requireNonNull(prgStates).getHeap();
        ArrayList<Pair<Integer, IValue>> heapEntries = new ArrayList<>();
        for (Map.Entry<Integer, IValue> entry: heap.getContent().entrySet())
        {
            heapEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView()
    {
        PrgState prgStates = getCurrentPrgState();
        List<String> output = new ArrayList<>();
        List<IValue> outputList = Objects.requireNonNull(prgStates).getOut().getList();
        int index;
        for(index =0; index<outputList.size(); index++)
        {
            output.add(outputList.get(index).toString());
        }
        outputListView.setItems(FXCollections.observableList(output));

    }

    private void populateFileTableListView()
    {
        PrgState prgStates = getCurrentPrgState();
        List<String> files = new ArrayList<>(Objects.requireNonNull(prgStates).getFileTable().getContent().keySet());
        fileTableListView.setItems(FXCollections.observableList(files));
    }

    private void populatePrgStateIdentifiersListView()
    {
        List<PrgState> prgStates = ctrl.getProgStates();
        List<Integer> idList = prgStates.stream().map(PrgState::getId).collect(Collectors.toList());
        prgStateIdenitifiersListView.setItems(FXCollections.observableList(idList));
        populateNrOfPrgStatesTextField();
    }


    private void populateSymTableView()
    {
        PrgState prgStates = getCurrentPrgState();
        MyIDictionary<String, IValue> symTable = Objects.requireNonNull(prgStates).getSymTable();
        ArrayList<Pair<String, IValue>> symTblEntries = new ArrayList<>();
        for(Map.Entry<String, IValue> entry: symTable.getContent().entrySet())
        {
            symTblEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        symbolTableView.setItems(FXCollections.observableArrayList(symTblEntries));
    }


    private void populateExecutionStckListView()
    {
        PrgState prgStates = getCurrentPrgState();
        List<String> execStackToString = new ArrayList<>();
        if(prgStates!=null)
        {
            for (IStmt statement: prgStates.getStk().getReversed())
            {
                execStackToString.add(statement.toString());
            }
        }
        executionStackListView.setItems(FXCollections.observableList(execStackToString));
    }


    public void runOneStep(MouseEvent mouseEv)
    {
        if (ctrl!=null)
        {
            try
            {
                List<PrgState> prgStates= Objects.requireNonNull(ctrl.getProgStates());
                if(prgStates.size()>0)
                {
                    ctrl.oneStep();
                    populate();
                    prgStates=ctrl.removeCompletedPrg(ctrl.getProgStates());
                    ctrl.setPrgStates(prgStates);
                    populatePrgStateIdentifiersListView();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("An error has occured!");
                    alert.setContentText("There is nothing left to execute!");
                    alert.showAndWait();
                }
            } catch(InterruptedException | MyExceptions | IOException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Execution error!");
                alert.setHeaderText("An execution error has occured!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("An error has occured!");
            alert.setContentText("No program selected!");
            alert.showAndWait();
        }

    }
}
