module pl.vost.kresyinwentarzfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens pl.vost.kresyinwentarzfx.persistence.products to javafx.base, org.hibernate.orm.core;

    opens pl.vost.kresyinwentarzfx to javafx.fxml;

    exports pl.vost.kresyinwentarzfx;
    exports pl.vost.kresyinwentarzfx.controller.element;
    exports pl.vost.kresyinwentarzfx.controller;
    exports pl.vost.kresyinwentarzfx.persistence.products;
    opens pl.vost.kresyinwentarzfx.controller to javafx.fxml;
}