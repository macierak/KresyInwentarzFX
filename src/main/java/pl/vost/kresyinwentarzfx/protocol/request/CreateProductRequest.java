package pl.vost.kresyinwentarzfx.protocol.request;

import java.math.BigDecimal;

public record CreateProductRequest(String name, BigDecimal price, long warehouseId, long invoiceId){
}
