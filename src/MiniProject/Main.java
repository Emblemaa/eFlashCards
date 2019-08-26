package MiniProject;

import MiniProject.TopicsMenu.TopicsMenu_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private static Stage window;

    public static Stage getWindow() {
        return window;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu/MainMenu.fxml"));
        primaryStage.setTitle("eFlashcard");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        primaryStage.setOnCloseRequest(x  ->
        {
            try
            {
                TopicsMenu_Controller.setInputPath("");

                System.exit(0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
