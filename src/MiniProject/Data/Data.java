package MiniProject.Data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Data
{
    private static WorkbookSettings ws = new WorkbookSettings();
    private static Workbook w;

    public static ObservableList<VOCAB> initList(int SheetIndex, URI inputFile)
    {
        ObservableList<VOCAB> list = FXCollections.observableArrayList();
        try
        {
            File inputWorkbook = new File(inputFile);
            ws.setEncoding("Cp1252");
            w = Workbook.getWorkbook(inputWorkbook, ws);
            Sheet sheet = w.getSheet(SheetIndex);
            for (int i = 0; i < sheet.getRows(); i++) {
                list.add(new VOCAB(sheet.getCell(0, i).getContents(), sheet.getCell(1, i).getContents(),
                        Boolean.valueOf(sheet.getCell(2, i).getContents().toLowerCase())));
            }
        }
        catch (IOException | BiffException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public static int getSheetsRows(int SheetIndex, URI inputFile) throws IOException, BiffException
    {
        File inputWorkbook = new File(inputFile);
        w = Workbook.getWorkbook(inputWorkbook,ws);
        return w.getSheet(SheetIndex).getRows();
    }
}


