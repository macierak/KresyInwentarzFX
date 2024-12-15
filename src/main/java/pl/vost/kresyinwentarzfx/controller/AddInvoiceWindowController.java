package pl.vost.kresyinwentarzfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.vost.kresyinwentarzfx.controller.element.CreateProduct;
import pl.vost.kresyinwentarzfx.controller.element.CreateProductCellFactory;
import pl.vost.kresyinwentarzfx.domain.InvoiceService;
import pl.vost.kresyinwentarzfx.domain.ProductService;
import pl.vost.kresyinwentarzfx.domain.WarehouseService;
import pl.vost.kresyinwentarzfx.persistence.products.Product;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;
import pl.vost.kresyinwentarzfx.protocol.request.CreateInvoiceRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

public class AddInvoiceWindowController{

    private final InvoiceService invoiceService = new InvoiceService();
    private final ProductService productService = new ProductService();
    private final WarehouseService warehouseService = new WarehouseService();

    @FXML
    public TextField invoiceNumber;

    @FXML
    public DatePicker invoiceDate;

    @FXML
    public ListView<CreateProduct> addProductsPane = new ListView<>();

    private MainWindowController mainWindowController;
    private Stage stage;

    public void initialize(MainWindowController mainWindowController, Stage stage){
        this.mainWindowController = mainWindowController;
        final var warehouseList = warehouseService.getAllWarehouses();
        addProductsPane.setCellFactory(new CreateProductCellFactory(warehouseList, this::deleteProductFromInvoice));
        this.stage = stage;
    }

    public void deleteProductFromInvoice(CreateProduct createProduct){

        addProductsPane.getItems().removeIf(e -> e.equals(createProduct));
        addProductsPane.refresh();
    }

    public void addProduct(){
        addProductsPane.getItems().add(new CreateProduct());
    }

    public void addInvoice(){
        if(invoiceNumber.getText().isEmpty() || invoiceDate.getValue() == null){
            return;
        }
        if(addProductsPane.getItems().isEmpty()){
            return;
        }

        final var invoice = invoiceService.saveInvoice(new CreateInvoiceRequest(invoiceNumber.getText(),
                invoiceDate.getValue()));

        final var products = addProductsPane.getItems().stream()
                .map(product -> new Product.ProductBuilder()
                        .name(product.getName())
                        .quantity(Long.valueOf(product.getQuantity()))
                        .price(product.getPrice())
                        .warehouse(product.getWarehouse())
                        .invoice(invoice)
                        .build())
                .collect(Collectors.toList());

        productService.saveProducts(products);

        mainWindowController.refreshProducts(addProductsPane.getItems().getFirst().getWarehouse());
        stage.close();
    }

}
