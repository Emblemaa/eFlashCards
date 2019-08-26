package MiniProject.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu_Controller
{
    //String type de phan biet flashcard hay customize vi deu 2 nut deu lead toi topic menu
    private static String type;
    public static String getType()
    {
        return type;
    }
    private static void setType(String t)
    {
        type = t;
    }

    private void changeScene(String id, ActionEvent event) throws IOException
    {
        Parent changeScene = FXMLLoader.load(getClass().getResource(id));
        Scene scene = new Scene(changeScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void toTopicsMenu(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/TopicsMenu/TopicsMenu.fxml", event);
        setType("Flashcard");
    }

    @FXML
    private void toTestMenu(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/TopicsMenu/TopicsMenu.fxml", event);
        setType("Test");
    }

    @FXML
    private void toCustomize(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/TopicsMenu/TopicsMenu.fxml", event);
        setType("Customize");
    }

    @FXML
    private void exit()
    {
        System.exit(0);
    }
}
