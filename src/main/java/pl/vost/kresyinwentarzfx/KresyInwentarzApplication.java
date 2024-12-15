package pl.vost.kresyinwentarzfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import pl.vost.kresyinwentarzfx.controller.MainWindowController;
import pl.vost.kresyinwentarzfx.domain.InvoiceService;
import pl.vost.kresyinwentarzfx.domain.ProductService;
import pl.vost.kresyinwentarzfx.domain.WarehouseService;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KresyInwentarzApplication extends Application{


    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(KresyInwentarzApplication.class.getResource("main-window.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 750, 720);
        stage.setTitle("KresyInwentarz");
        stage.setScene(scene);
        final MainWindowController controller = fxmlLoader.getController();
        controller.stage = stage;
        controller.refreshTabs();
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }

}