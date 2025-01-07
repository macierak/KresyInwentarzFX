package pl.vost.kresyinwentarzfx.domain;

import pl.vost.kresyinwentarzfx.persistence.BaseRepositoryImpl;
import pl.vost.kresyinwentarzfx.persistence.products.Invoice;
import pl.vost.kresyinwentarzfx.protocol.request.CreateInvoiceRequest;

import java.util.List;

public class InvoiceService{

    private final BaseRepositoryImpl<Invoice> invoiceRepository;

    public InvoiceService(){
        this.invoiceRepository = new BaseRepositoryImpl<>(Invoice.class);
    }

    public Invoice getInvoiceById(Long id){
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getInvoiceByNumber(String number){
        return invoiceRepository.findByField("number", number);
    }

    public Invoice saveInvoice(CreateInvoiceRequest request){
        final var invoice = Invoice.builder()
                .number(request.number())
                .date(request.date())
                .build();

        return invoiceRepository.save(invoice);
    }
}
