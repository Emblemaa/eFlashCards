package MiniProject.TopicsMenu;

import MiniProject.MainMenu.MainMenu_Controller;
import MiniProject.Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class TopicsMenu_Controller implements Tools
{
    @Override
    public void alert(String content, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setHeaderText(content);
        alert.showAndWait();
    }

    private static String InputPath;

    public static String getInputPath() {
        return InputPath;
    }

    public static void setInputPath(String inputPath) {
        InputPath = inputPath;
    }

    private void changeScene(String id, ActionEvent event) throws IOException
    {
        Parent changeScene = FXMLLoader.load(getClass().getResource(id));
        Scene scene = new Scene(changeScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void init(String path, ActionEvent event) throws URISyntaxException, IOException
    {
        String typeScene = MainMenu_Controller.getType();
        URL url = getClass().getResource(path);
        setInputPath(path);
        try
        {
            File file = new File(url.toURI());
            if(typeScene.equals("Flashcard"))
                changeScene("/MiniProject/Flashcards/Flashcards.fxml", event);
            else if(typeScene.equals("Customize"))
                changeScene("/MiniProject/UserWord/EditMenu.fxml", event);
            else changeScene("/MiniProject/TestMenu/TestMenu.fxml", event);
        }
        catch (NullPointerException e)
        {
            alert("File not found!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void toMaths(ActionEvent event) throws IOException, URISyntaxException
    {
        init("/MiniProject/Resources/MATHSDATA.xls", event);
    }

    @FXML
    private void toIELTS(ActionEvent event) throws IOException, URISyntaxException
    {
        init("/MiniProject/Resources/IELTSDATA.xls", event);
    }

    @FXML
    private void toYourWords(ActionEvent event) throws IOException, URISyntaxException
    {
        init("/MiniProject/Resources/USERDATA.xls", event);
    }

    @FXML
    void back(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/MainMenu/MainMenu.fxml", event);
    }
}
