package pl.vost.kresyinwentarzfx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pl.vost.kresyinwentarzfx.KresyInwentarzApplication;
import pl.vost.kresyinwentarzfx.controller.element.ViewProduct;
import pl.vost.kresyinwentarzfx.domain.InvoiceService;
import pl.vost.kresyinwentarzfx.domain.ProductService;
import pl.vost.kresyinwentarzfx.domain.WarehouseService;
import pl.vost.kresyinwentarzfx.persistence.products.Product;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;
import pl.vost.kresyinwentarzfx.protocol.request.CreateWarehouseRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindowController{

    private final InvoiceService invoiceService = new InvoiceService();
    private final ProductService productService = new ProductService();
    private final WarehouseService warehouseService = new WarehouseService();

    @FXML
    public TabPane mag;

    @FXML
    public Tab addMagTab;

    @FXML
    public TextField magName;

    @FXML
    public TextField magCode;

    public Stage stage;

    public void showAddInvoiceWindow() throws IOException{
        final var addInvoiceWindow = new FXMLLoader(KresyInwentarzApplication.class.getResource("addInvoiceWindow.fxml"));
        final var scene = new Scene(addInvoiceWindow.load(), 640, 480);
        final var stage = new Stage();
        stage.setTitle("Dodaj fakturę");
        stage.setScene(scene);
        final AddInvoiceWindowController controller = addInvoiceWindow.getController();
        controller.initialize(this, stage);
        stage.setResizable(false);
        stage.show();

    }

    public void refreshTabs(){
        mag.getTabs().clear();
        final var warehouseTabs = getWarehouseTabs();
        mag.getTabs().addAll(warehouseTabs);
        mag.getTabs().add(addMagTab);
    }

    public void refreshProducts(Warehouse warehouseTabToFocus){
        refreshTabs();
        final var tabToFocus = mag.getTabs().stream()
                .filter(tab -> tab.getText().equals(warehouseTabToFocus.getCode()))
                .findFirst()
                .orElse(null);
        mag.getSelectionModel().select(tabToFocus);
    }

    public void addMag(){
        warehouseService.createWarehouse(new CreateWarehouseRequest(magName.getText(), magCode.getText()));
        refreshTabs();
    }

    private List<Tab> getWarehouseTabs(){
        final var tabList = new ArrayList<Tab>();
        final var warehouses = getAllWarehouses();

        warehouses.forEach(warehouse -> {
            final var products = productService.getAllProductsForWarehouse(warehouse);
            final var tab = new Tab(warehouse.getCode());
            tabList.add(tab);
            final var anchorPane = new AnchorPane();
            anchorPane.prefWidthProperty().bind(stage.widthProperty());
            anchorPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            tab.setContent(anchorPane);
            final var tableView = new TableView<ViewProduct>();
            tableView.prefWidthProperty().bind(stage.widthProperty());
            tableView.setPrefHeight(Region.USE_COMPUTED_SIZE);
            anchorPane.getChildren().add(tableView);
            final var observableProducts = FXCollections.observableArrayList(mapToViewProduct(products));
            tableView.setItems(observableProducts);

            final var nameCol = new TableColumn<ViewProduct, String>("Nazwa");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            nameCol.setCellFactory(TextFieldTableCell.forTableColumn());  // Make column editable
            nameCol.setOnEditCommit(event ->
                    event.getRowValue().setName(event.getNewValue()));

            final var quantityCol = new TableColumn<ViewProduct, Integer>("Ilość");
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            quantityCol.setCellFactory(column -> createSpinnerCellInt());
            // column editable
            quantityCol.setEditable(true);
            quantityCol.setOnEditCommit(event ->
                    event.getRowValue().setQuantity(event.getNewValue()));

            final var priceCol = new TableColumn<ViewProduct, Double>("Cena");
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price")); // Make column
            priceCol.setCellFactory(viewProductDoubleTableColumn -> createSpinnerCellDouble());
            // editable
            priceCol.setOnEditCommit(event ->
                    event.getRowValue().setPrice(event.getNewValue()));


            final var totalPriceCol = new TableColumn<ViewProduct, Double>("Cena całkowita");
            totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

            final var buttonCol = new TableColumn<ViewProduct, Void>("Akcje");
            buttonCol.setCellFactory(col -> new TableCell<>(){
                // Create the buttons
                private final Button decrementButton = new Button("Odejmij 1");
                private final Button saveButton = new Button("Zapisz zmiany");
                // Create a HBox to hold the buttons
                private final HBox hbox = new HBox(decrementButton, saveButton);

                {
                    // Set action for the decrement button
                    decrementButton.setOnAction(event -> {
                        ViewProduct viewProduct = getTableView().getItems().get(getIndex());
                        final var saved = productService.decrementProduct(viewProduct);
                        observableProducts.replaceAll(p -> p.getId().equals(saved.getId()) ? saved : p);
                    });

                    // Set action for the save button
                    saveButton.setOnAction(event -> {
                        ViewProduct viewProduct = getTableView().getItems().get(getIndex());
                        final var saved = productService.updateProduct(viewProduct);

                        observableProducts.replaceAll(p -> p.getId().equals(saved.getId()) ? saved : p);
                    });

                }

                @Override
                protected void updateItem(Void item, boolean empty){
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                    }else{
                        setGraphic(hbox);
                    }
                }
            });

            tableView.getColumns().add(nameCol);
            tableView.getColumns().add(quantityCol);
            tableView.getColumns().add(priceCol);
            tableView.getColumns().add(totalPriceCol);
            tableView.getColumns().add(buttonCol);
        });


        return tabList;
    }

    private TableCell<ViewProduct, Integer> createSpinnerCellInt(){
        return new TableCell<>(){
            final Spinner<Integer> spinner = new Spinner<>(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

            {
                spinner.setEditable(true);
            }

            @Override
            protected void updateItem(Integer item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                }else{
                    spinner.getValueFactory().setValue(item);
                    setGraphic(spinner);
                    spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                        if(getTableView() != null){
                            final var items = getTableView().getItems().get(this.getIndex());
                            // Update item value
                            items.setQuantity(newValue);
                        }
                    });
                }
            }
        };
    }

    private TableCell<ViewProduct, Double> createSpinnerCellDouble(){
        return new TableCell<>(){
            final Spinner<Double> spinner = new Spinner<>(Double.MIN_VALUE, Double.MAX_VALUE, 0, 0.01);

            {
                spinner.setEditable(true);
            }

            @Override
            protected void updateItem(Double item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                }else{
                    spinner.getValueFactory().setValue(item);
                    setGraphic(spinner);
                    spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                        if(getTableView() != null){
                            final var items = getTableView().getItems().get(this.getIndex());
                            // Update item value
                            items.setPrice(newValue);
                        }
                    });
                }
            }
        };
    }

    private List<ViewProduct> mapToViewProduct(List<Product> products){
        return products.stream()
                .map(p -> new ViewProduct(
                        p.getId(),
                        p.getName(),
                        p.getQuantity(),
                        p.getPrice().doubleValue(),
                        p.getTotalPrice().doubleValue()
                ))
                .collect(Collectors.toList());
    }

    private List<Warehouse> getAllWarehouses(){
        return warehouseService.getAllWarehouses();
    }
}