package MiniProject.UserWord;

import MiniProject.Data.Data;
import MiniProject.Data.VOCAB;
import MiniProject.TopicsMenu.TopicsMenu_Controller;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.*;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddMenu_Controller implements Initializable
{
    @FXML
    private GridPane gridAdd;

    @FXML
    private TextField Word;

    @FXML
    private TextField Definition;

    private ObservableList<VOCAB> List;

    private static String ExcelPath = TopicsMenu_Controller.getInputPath();

    private static Workbook workbook;
    private static WritableWorkbook wb;
    private static WritableSheet sheet;

    private URI IDtoURI(String path) throws URISyntaxException
    {
        URL url = getClass().getResource(path);
        return url.toURI();
    }

    private File file(String path) throws URISyntaxException
    {
        URL url = getClass().getResource(path);
        File temp = new File(url.toURI());
        return temp;
    }

    private void alert(String content, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isInformationFull()
    {
        for(Node node: gridAdd.getChildren())
        {
            if(node instanceof TextField)
            {
                if(((TextField) node).getText().isEmpty())
                    return false;
            }
        }
        return true;
    }

    protected void setTableView(TableView<VOCAB> tbv) throws IOException, BiffException, URISyntaxException
    {
        List = Data.initList(0, IDtoURI(ExcelPath));
        tbv.setItems(List);
    }

    @FXML
    private void add() throws WriteException
    {
        if(!isInformationFull())
            alert("Please enter all information!", Alert.AlertType.ERROR);
        else
        {
            Label label = new Label(0, sheet.getRows(), Word.getText());
            sheet.addCell(label);
            label = new Label(1, sheet.getRows() - 1, Definition.getText());
            sheet.addCell(label);
            label = new Label(2, sheet.getRows() - 1, "TRUE");
            sheet.addCell(label);
            alert("SUCCESSFUL!", Alert.AlertType.INFORMATION);
            Word.clear();
            Definition.clear();
            writeandcloseWB();
        }
    }

    public void initialize(URL location, ResourceBundle resource) {
        try {
            List = Data.initList(0, IDtoURI(ExcelPath));
            workbook = Workbook.getWorkbook(file(ExcelPath));
            wb = Workbook.createWorkbook(file("/MiniProject/Resources/TEMP.xls"), workbook);
            sheet = wb.getSheet(0);
        }
        catch( IOException | BiffException | URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void writeandcloseWB()
    {
        try
        {
            wb.setOutputFile(file(ExcelPath));
            wb.write();
            wb.close();
            workbook.close();
        }
        catch (Exception e) { }
    }
}