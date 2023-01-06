package View;

import Controller.Controller;
import Model.Exceptions.MyExceptions;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class RunExample extends Command{
    private final Controller constroller;

    public RunExample(String key, String description, Controller ctrl)
    {
        super(key, description);
        this.constroller=ctrl;
    }

    @Override
    public void execute()
    {
        //try
        //{
        System.out.println("Do you want to display the steps? [Y/n]");
        Scanner readOption = new Scanner(System.in);
        String option = readOption.next();
        constroller.setDisplayFlag(Objects.equals(option,"Y"));
        constroller.allStep();
        //}catch (MyExceptions | IOException e)
        //{
        //    System.out.println(e.getMessage());
        //}
    }
}
