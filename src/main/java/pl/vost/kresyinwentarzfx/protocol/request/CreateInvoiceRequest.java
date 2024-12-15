package pl.vost.kresyinwentarzfx.protocol.request;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record CreateInvoiceRequest(String number, LocalDate date) {
}
