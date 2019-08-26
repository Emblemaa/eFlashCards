package MiniProject.ResultMenu;

import MiniProject.Data.Data;
import MiniProject.Data.VOCAB;
import MiniProject.TestMenu.TestMenu_Controller;
import MiniProject.TopicsMenu.TopicsMenu_Controller;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultMenu_Controller implements Initializable {
    private ObservableList<VOCAB> ListWrongWords = FXCollections.observableArrayList();

    @FXML
    private Label Score;

    @FXML
    private TableView<VOCAB> TBV_WrongWords;

    @FXML
    private TableColumn<VOCAB, String> Word;

    @FXML
    private TableColumn<VOCAB, String> Definition;

    @FXML
    private AnchorPane StrikePane;

    @FXML
    private GridPane gridPane;

    FadeTransition fadeIn = new FadeTransition(
            Duration.millis(3000));

    private void initTransition(Node node)
    {
        try {
            fadeIn.setNode(node);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.setCycleCount(1);
            fadeIn.setAutoReverse(false);
            node.setVisible(true);
            fadeIn.play();
            Thread.sleep(100);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    private void initStrike()
    {
    StrikePane.setVisible(true);
            for (Node node : StrikePane.getChildren()) {
                initTransition(node);
            }
    }
    private URI IDtoURI(String path)
    {
        URI uri = URI.create("");
        try {

            URL url = getClass().getResource(path);
            uri = url.toURI();
        }catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
        return uri;
    }

    private void changeScene(String id, ActionEvent event) throws IOException
    {
        Parent changeScene = FXMLLoader.load(getClass().getResource(id));
        Scene scene = new Scene(changeScene);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    private static  int ScoreResult;
    private static int TestDay;
    public static void getScore(int score) {
        ScoreResult = score;
    }

    public static void getDay(int day) {
        TestDay = day;
    }
    ObservableList<VOCAB> TestList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        System.out.println(ScoreResult);
        Word.setCellValueFactory(new PropertyValueFactory<>("Word"));
        Definition.setCellValueFactory(new PropertyValueFactory<>("Definition"));
        TestList=Data.initList(TestDay, IDtoURI(TopicsMenu_Controller.getInputPath()));
            Score.setText(Integer.toString(ScoreResult));
            if(ScoreResult==10)
                initStrike();
            else gridPane.setVisible(true);
            for (VOCAB vocab : TestList) {
                if (!vocab.isChecked())
                    ListWrongWords.add(vocab);
            }

        TBV_WrongWords.setItems(ListWrongWords);
    }

    @FXML
    private void tryAgain(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/TestMenu/TestMenu.fxml", event);
    }

    @FXML
    private void endTest(ActionEvent event) throws IOException
    {
        changeScene("/MiniProject/MainMenu/MainMenu.fxml", event);
    }
}
