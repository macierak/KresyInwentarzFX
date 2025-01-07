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
import pl.vost.kresyinwentarzfx.persistence.products.Invoice;
import pl.vost.kresyinwentarzfx.persistence.products.Product;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;
import pl.vost.kresyinwentarzfx.protocol.request.CreateInvoiceRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AddInvoiceWindowController{

    private final InvoiceService invoiceService = new InvoiceService();
    private final ProductService productService = new ProductService();
    private final WarehouseService warehouseService = new WarehouseService();

    final AtomicBoolean isProcessing = new AtomicBoolean(false);

    @FXML
    public TextField invoiceNumber;

    @FXML
    public DatePicker invoiceDate;

    @FXML
    public Button sendButton;

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
        try{
            if(isProcessing.getAndSet(true)){
                return;
            }
            if(invoiceNumber.getText().isEmpty() || invoiceDate.getValue() == null){
                return;
            }
            if(addProductsPane.getItems().isEmpty()){
                return;
            }
            final Invoice invoice;
            final var invoiceList = invoiceService.getInvoiceByNumber(invoiceNumber.getText());
            if(invoiceList.isEmpty()){
                invoice = invoiceService.saveInvoice(new CreateInvoiceRequest(invoiceNumber.getText(),
                        invoiceDate.getValue()));
            }else{
                invoice = invoiceList.get(0);
            }

            final var products = addProductsPane.getItems().stream()
                    .map(product -> new Product.ProductBuilder()
                            .name(product.getName())
                            .quantity(Long.valueOf(product.getQuantity()))
                            .price(product.getPrice())
                            .warehouse(product.getWarehouse())
                            .invoice(invoice)
                            .build())
                    .collect(Collectors.toList());

            products.stream().filter(p -> Objects.isNull(p.getWarehouse().getId())).forEach(product -> {
                mainWindowController.appendError(
                        "Produkt " + product.getName() + " " + product.getQuantity() + "szt." + " " + product.getPrice() + "zł nie posiada przypisanego magazynu");
            });

            productService.saveProducts(products);

            mainWindowController.refreshProducts(addProductsPane.getItems().getFirst().getWarehouse());
        }catch(Exception e){
            mainWindowController.appendError("Błąd podczas dodawania faktury: " + e.getMessage());
        }finally{
            isProcessing.set(false);
            stage.close();
        }

    }

}
