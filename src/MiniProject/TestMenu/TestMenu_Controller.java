package MiniProject.TestMenu;

import MiniProject.Main;
import MiniProject.ResultMenu.ResultMenu_Controller;
import MiniProject.TopicsMenu.TopicsMenu_Controller;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import MiniProject.Data.Data;
import MiniProject.Data.VOCAB;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class TestMenu_Controller implements Initializable
{
    private ObservableList<VOCAB> DATA = FXCollections.observableArrayList();

    private int count = 0;
    private int score = 0;

    public int getScore() {
        return score;
    }

    @FXML
    private javafx.scene.control.Label Definition;

    @FXML
    private TextField Word;
    @FXML
    private Label CorrectAlert;
    @FXML
    private Label WrongAlert;
    int arrSize=10;
    boolean[] TestResult = new boolean[arrSize];
    boolean[] isWordChosen = new boolean[arrSize];

    private Random random = new Random();
    private int randomWord = 0;

    private String ExcelPath = TopicsMenu_Controller.getInputPath();

    private int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private Optional<String> result;

    private URI IDtoURI(String path) throws URISyntaxException
    {
        URL url = getClass().getResource(path);
        return url.toURI();
    }

    private void alert(String content, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void changeScene(String id) throws IOException
    {
        Parent changeScene = FXMLLoader.load(getClass().getResource(id));
        Scene scene = new Scene(changeScene);
        Stage window = Main.getWindow();
        window.setScene(scene);
        window.show();
    }

    private void updateUncheckedWord() throws Exception
    {
        File file = new File(IDtoURI(ExcelPath));
        File temp = new File(IDtoURI("/MiniProject/Resources/TEMP.xls"));
        Workbook workbook = Workbook.getWorkbook(file);
        WritableWorkbook wb = Workbook.createWorkbook(temp, workbook);
        WritableSheet sheet;
        if (!ExcelPath.equals("/MiniProject/Resources/USERDATA.xls"))
            sheet = wb.getSheet(Integer.valueOf(result.get())); // Lấy sheet ngày làm test;
        else sheet = wb.getSheet(0);

        jxl.write.Label label;
        for(int rowCount = 0; rowCount < arrSize; rowCount++)
        {
            label = new jxl.write.Label(2, rowCount, String.valueOf(TestResult[rowCount]));
            sheet.addCell(label);
        }
        wb.setOutputFile(file);
        wb.write();
        wb.close();
        workbook.close();
    }

    @FXML
    void back(ActionEvent event) throws Exception
    {
        updateUncheckedWord();
        Parent changeScene = FXMLLoader.load(getClass().getResource("/MiniProject/TopicsMenu/TopicsMenu.fxml"));
        Scene scene = new Scene(changeScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void initTest(int index) throws URISyntaxException, BiffException, IOException
    {
        randomWord = random.nextInt(Data.getSheetsRows(index,IDtoURI(ExcelPath)));
        DATA.clear();
        DATA = Data.initList(Integer.valueOf(result.get()), IDtoURI(ExcelPath));
        Definition.setText(DATA.get(randomWord).getDefinition());
        isWordChosen[randomWord]=true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        Arrays.fill(TestResult,true);
        try {
            if(ExcelPath.trim().equalsIgnoreCase("/MiniProject/Resources/USERDATA.xls"))
                initTest(0);
            else
            {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Choose Day");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter Day:");

                result = dialog.showAndWait();
                setDay(Integer.valueOf(result.get()));

                initTest(Integer.valueOf(result.get()));
            }
            checkAnswer();
        } catch (IOException | BiffException | IndexOutOfBoundsException | NumberFormatException | URISyntaxException e)
        {
            e.printStackTrace();
            alert("Sheet not found!", Alert.AlertType.ERROR);
            initialize(null, null);
        }
        Platform.runLater(() -> Word.requestFocus());
        Word.setOnKeyReleased(e->
        {
            try {
                if (e.getCode() == KeyCode.ENTER)
                    resetWord();
            }catch (Exception e1)
            {
                e1.printStackTrace();
            }
        });
    }

    private void initDataResult() throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/MiniProject/ResultMenu/ResultMenu.fxml"));
        Loader.load();

        System.out.println(score);
        ResultMenu_Controller.getScore(score);
        ResultMenu_Controller.getDay(day);
    }


    private void initAlert()
    {
        CorrectAlert.setVisible(false);
        WrongAlert.setVisible(false);
    }
    private void resetWord() throws Exception
    {
        Word.clear();
        Word.requestFocus();
        initAlert();
        randomWord = random.nextInt(Data.getSheetsRows(Integer.valueOf(result.get()), IDtoURI(ExcelPath)));
        count++;
        while(isWordChosen[randomWord] && (count <10)) {
            randomWord = random.nextInt(Data.getSheetsRows(Integer.valueOf(result.get()), IDtoURI(ExcelPath)));
        }
        isWordChosen[randomWord]=true;

        Definition.setText(DATA.get(randomWord).getDefinition());
        if (count == 3)
        {
            updateUncheckedWord();
            initDataResult();
            changeScene("/MiniProject/ResultMenu/ResultMenu.fxml");
        }
    }

    private void checkAnswer()
    {
        Word.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                {
                    initAlert();
                    for (boolean a : TestResult) {
                        System.out.print(a);
                    }
                    int inputLength = Word.getText().length();
                    int wordLength = DATA.get(randomWord).getWord().length();
                    try {
                        if (inputLength==wordLength) {
                            if (Word.getText().trim().equalsIgnoreCase(DATA.get(randomWord).getWord().trim()) &&
                                    DATA.get(randomWord).getWord() != null) {
                                score++;
                                //alert("Correct", Alert.AlertType.INFORMATION);
                                CorrectAlert.setVisible(true);
                                TestResult[randomWord] = true;
                            } else {
                                //         alert("Incorrect", Alert.AlertType.INFORMATION);
                                WrongAlert.setVisible(true);
                                TestResult[randomWord] = false;
                            }

                        }
                        else if (inputLength > wordLength)
                        {
                            WrongAlert.setVisible(true);
                            System.out.println(1);
                        }
                        else
                        {
                            initAlert();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
