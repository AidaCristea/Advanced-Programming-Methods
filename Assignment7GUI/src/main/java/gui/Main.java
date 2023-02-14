package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader prgListLoader = new FXMLLoader();

        prgListLoader.setLocation(Main.class.getResource("ProgramChooserController.fxml"));
        Parent prgListRoot = prgListLoader.load();
        Scene prgListScene = new Scene(prgListRoot, 500, 500);


        prgListScene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("style_button.css")).toExternalForm());
        ProgramChooserController prgChoCrtl = prgListLoader.getController();
        stage.setTitle("Select a program");
        stage.setScene(prgListScene);
        stage.show();


        FXMLLoader prgExecutorLoader = new FXMLLoader();

        prgExecutorLoader.setLocation(Main.class.getResource("ProgramExecutorController.fxml"));
        Parent prgExecutorRoot = prgExecutorLoader.load();
        Scene prgExecutorScene = new Scene(prgExecutorRoot, 700, 500);

        prgExecutorScene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("style_button.css")).toExternalForm());
        ProgramExecutorController prgExecutorCtrl = prgExecutorLoader.getController();

        prgChoCrtl.setPrgExecCtrl(prgExecutorCtrl);
        Stage secondStage = new Stage();
        secondStage.setTitle("Interpreter");
        secondStage.setScene(prgExecutorScene);
        secondStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
