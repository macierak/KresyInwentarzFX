package pl.vost.kresyinwentarzfx.controller.element;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;


public class CreateProductCellFactory implements Callback<ListView<CreateProduct>, ListCell<CreateProduct>>{

    private List<Warehouse> warehouseList;
    private Consumer<CreateProduct> consumer;

    public CreateProductCellFactory(){

    }

    public CreateProductCellFactory(List<Warehouse> warehouseList, Consumer<CreateProduct> consumer){
        this.warehouseList = warehouseList;
        this.consumer = consumer;
    }

    @Override
    public ListCell<CreateProduct> call(ListView<CreateProduct> param){
        return new ListCell<>(){
            private TextField nameField;
            private Spinner<Integer> quantityField;
            private Spinner<Double> priceField;
            private ComboBox<Warehouse> warehouseComboBox;
            private Button deleteProductButton;
            private HBox hbox;

            {
                nameField = new TextField();
                quantityField = new Spinner<>();
                priceField = new Spinner<>();
                warehouseComboBox = new ComboBox<>();
                warehouseComboBox.setButtonCell(new ListCell<Warehouse>(){
                    @Override
                    protected void updateItem(Warehouse item, boolean empty){
                        super.updateItem(item, empty);

                        if(empty){
                            setText(null);
                        }else{
                            setText(item.getCode());
                        }
                    }
                });
                warehouseComboBox.setCellFactory(c -> new ListCell<Warehouse>(){
                    @Override
                    protected void updateItem(Warehouse item, boolean empty){
                        super.updateItem(item, empty);

                        if(empty){
                            setText(null);
                        }else{
                            setText(item.getCode()); // assumes Warehouse has getName method
                        }
                    }
                });
                warehouseComboBox.getItems().addAll(warehouseList);
                deleteProductButton = new Button();
                deleteProductButton.setVisible(true);
                deleteProductButton.setText("-");
                deleteProductButton.setOnAction((a) -> consumer.accept(param.getItems().get(this.getIndex())));

                quantityField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                        Integer.MAX_VALUE));

                priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0d,
                        Double.MAX_VALUE, 0, 0.01));
                priceField.editableProperty().setValue(true);
                quantityField.editableProperty().setValue(true);
                hbox = new HBox(nameField, quantityField, priceField, warehouseComboBox, deleteProductButton);

                nameField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                    if(!newPropertyValue){
                        getItem().setName(nameField.getText());
                    }
                });

                quantityField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                    if(!newPropertyValue){
                        getItem().setQuantity(quantityField.getValue());
                    }
                });

                priceField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                    if(!newPropertyValue){
                        getItem().setPrice(BigDecimal.valueOf(priceField.getValue()));
                    }
                });

                warehouseComboBox.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                    if(!newPropertyValue){
                        getItem().setWarehouse(warehouseComboBox.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(CreateProduct item, boolean empty){
                super.updateItem(item, empty);

                if(item == null || empty){
                    setText(null);
                    setGraphic(null);
                }else{
                    nameField.setText(item.getName());
                    quantityField.getValueFactory().setValue(item.getQuantity());
                    priceField.getValueFactory().setValue(item.getPrice().doubleValue());
                    setText(null);
                    setGraphic(hbox);
                }
            }
        };
    }
}