package MiniProject.UserWord;

import MiniProject.Data.Data;
import MiniProject.Data.VOCAB;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditMenu_Controller implements Initializable
{
    @FXML
    private TableView<VOCAB> TBV_ListVocab;

    @FXML
    private TableColumn<VOCAB, String> Word;

    @FXML
    private TableColumn<VOCAB, String> Definition;

    @FXML
    private CheckBox Box;


    private ObservableList<VOCAB> data = FXCollections.observableArrayList();

    private String ExcelPath = TopicsMenu_Controller.getInputPath();

    //private File DATAFILE = new File(ExcelPath);

    private boolean isOpened = true;
    private Stage window = new Stage();

    private File file(String path) throws URISyntaxException
{
    URL url = getClass().getResource(path);
    File temp = new File(url.toURI());
    return temp;
}

    private URI IDtoURI(String path) throws URISyntaxException
    {
        URL url = getClass().getResource(path);
        return url.toURI();
    }

    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        //comboBox.getItems().addAll("Word", "Definition");
        try
        {
            //Stage;

            Word.setCellValueFactory(new PropertyValueFactory<>("Word"));
            Definition.setCellValueFactory(new PropertyValueFactory<>("Definition"));
            data.clear();
            data = Data.initList(0, IDtoURI(ExcelPath));
            TBV_ListVocab.setItems(data);
        }
        catch ( URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    private void loadDatabase(ActionEvent event) throws IOException, BiffException, URISyntaxException
    {
        data.clear();
        data = Data.initList(0, IDtoURI(ExcelPath));
        TBV_ListVocab.setItems(data);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.getScene().getStylesheets().add(getClass().getResource("Style_UncheckedWords.css").toExternalForm());
        int i = 0;
        TableRow row;
        for (Node n : TBV_ListVocab.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                row = (TableRow) n;
                row.getStyleClass().clear();
                row.getStyleClass().addAll("cell", "indexed-cell" ,"table-row-cell");
            }
        }
    }

    private boolean isUpdated = false;
    @FXML
    private void showUncheckedWords(ActionEvent event)
    {
        System.out.println("Is update? "+isUpdated);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.getScene().getStylesheets().add(getClass().getResource("Style_UncheckedWords.css").toExternalForm());
        int i = 0;
        TableRow row;
        if (isUpdated) {
            for (Node n : TBV_ListVocab.lookupAll("TableRow")) {
                if (n instanceof TableRow) {
                    row = (TableRow) n;
                    row.getStyleClass().clear();
                    row.getStyleClass().addAll("cell", "indexed-cell" ,"table-row-cell");
                }
            }
        }
        for(Node n: TBV_ListVocab.lookupAll("TableRow"))
        {
            if(n instanceof TableRow)
            {
                row =(TableRow) n;
                //row.getStyleClass().clear();
                /*  row.getStyleClass().addAll("FalseAnswers", "CorrectAnswers");
                row.setStyle();*/
                if(!TBV_ListVocab.getItems().get(i).isChecked())
                {
                    if(Box.isSelected()) {
                        row.getStyleClass().add("FalseAnswers");
                        System.out.println(TBV_ListVocab.getStyleClass());
                    }
                    else {
                        row.getStyleClass().clear();
                        row.getStyleClass().addAll("cell", "indexed-cell" ,"table-row-cell");
                        TBV_ListVocab.getStyleClass().clear();
                        TBV_ListVocab.getStyleClass().add("table-view");
                    }
                    System.out.println(TBV_ListVocab.getItems().get(i).getWord());
                    // row.setDisable(true);
                }

            }
            i++;
            if(i == TBV_ListVocab.getItems().size())
                break;
        }
    }

    @FXML
    private void addWord(ActionEvent event) throws IOException
    {
        if(isOpened)
        {
            isOpened = false;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AddMenu.fxml"));
            Parent addNV = loader.load();
            Scene add = new Scene(addNV);

            AddMenu_Controller controller = loader.getController();
            window.setScene(add);
            window.show();
            window.setOnCloseRequest(x ->
            {
                isOpened = true;
                try
                {
                    controller.setTableView(TBV_ListVocab);
                }
                catch (IOException | BiffException | URISyntaxException e)
                {
                    e.printStackTrace();
                }
            });
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("ALREADY OPENED!");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteWord(ActionEvent event) throws IOException, BiffException, WriteException, URISyntaxException
    {
        if(TBV_ListVocab.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("PLEASE CHOOSE A WORD!");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("ARE YOU SURE?");
            ButtonType buttonYes = ButtonType.YES;
            ButtonType buttonNo = ButtonType.NO;
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.YES)
            {
                Workbook workbook = Workbook.getWorkbook(file(ExcelPath));
                WritableWorkbook wb = Workbook.createWorkbook(file("/MiniProject/Resources/TEMP.xls"), workbook);
                WritableSheet sheet = wb.getSheet(0);
                sheet.removeRow(TBV_ListVocab.getSelectionModel().getSelectedIndex());
                wb.setOutputFile(file(ExcelPath));
                wb.write();
                wb.close();
                /*data = Data.initList(0, ExcelPath);
                TBV_ListVocab.setItems(data);*/
                loadDatabase(event);
                Box.setSelected(false);
                showUncheckedWords(event);
            }
        }
        isUpdated=true;

    }

    /*private FilteredList<VOCAB> filteredList = new FilteredList<>(data, (VOCAB word) -> true);

    @FXML
    private void search() //in progress
    {
        Search.setOnKeyReleased(e ->
        {
            Search.textProperty().addListener((observable, oldValue, newValue) ->
            {
                filteredList.setPredicate((Predicate<? super VOCAB>) word ->
                {
                    if(newValue == null || newValue.isEmpty())
                        return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(word.getWord().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else if(word.getDefinition().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    return false;
                });
            });
        });
        SortedList<VOCAB> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(TBV_ListVocab.comparatorProperty());
        TBV_ListVocab.setItems(sortedData);
    }*/

    @FXML
    public void back(ActionEvent event) throws IOException
    {
        window.hide();
        Parent changeScene = FXMLLoader.load(getClass().getResource("/MiniProject/TopicsMenu/TopicsMenu.fxml"));
        Scene scene = new Scene(changeScene);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
