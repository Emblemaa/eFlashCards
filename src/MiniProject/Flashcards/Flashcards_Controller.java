package MiniProject.Flashcards;

import MiniProject.TopicsMenu.TopicsMenu_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import MiniProject.Data.Data;
import MiniProject.Data.VOCAB;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Flashcards_Controller implements Initializable {
    ObservableList<VOCAB> DATA = FXCollections.observableArrayList();

    @FXML
    private Label Word;

    @FXML
    private Label Definition;

    int iterator = 0;

    public void alert(String content, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    private void next(ActionEvent event) throws IOException
    {
        try
        {
            iterator++;
            Word.setText(DATA.get(iterator).getWord());
            Definition.setText(DATA.get(iterator).getDefinition());
            Definition.setVisible(false);
        }
        catch (IndexOutOfBoundsException e)
        {
            alert("THE END!", Alert.AlertType.INFORMATION);
            changeScene("/MiniProject/MainMenu/MainMenu.fxml", event);
        }
    }

    @FXML
    void previous() {
        try {
            iterator--;
            Word.setText(DATA.get(iterator).getWord());
            Definition.setText(DATA.get(iterator).getDefinition());
            Definition.setVisible(false);
        }
        catch (IndexOutOfBoundsException e) {
            alert("Nothing previous!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void showDefinition() {
        Definition.setVisible(true);
    }

    @FXML
    void back(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/TopicsMenu/TopicsMenu.fxml", event);
    }

    private URI IDtoURI(String id) throws URISyntaxException
    {
        URL url = getClass().getResource(id);
        return url.toURI();
    }

    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        try
        {
            ActionEvent event = null;
            DATA.clear();
            if(!TopicsMenu_Controller.getInputPath().trim().equalsIgnoreCase(
                    "MiniProject/Resources/USERDATA.xls"))
            {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Choose Day");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter Day:");
                Optional<String> result = dialog.showAndWait();
                DATA = Data.initList(Integer.valueOf(result.get()), IDtoURI(TopicsMenu_Controller.getInputPath()));

            }
            else DATA = Data.initList(0, IDtoURI(TopicsMenu_Controller.getInputPath()));
            System.out.println(IDtoURI(TopicsMenu_Controller.getInputPath()));
            Word.setText(DATA.get(iterator).getWord());
            Definition.setText(DATA.get(iterator).getDefinition());
            Definition.setVisible(false);
        }
        catch (URISyntaxException   e)
        {
            alert("SHEET NOT FOUND!", Alert.AlertType.ERROR);
            initialize(null, null);
        }
    }
}
