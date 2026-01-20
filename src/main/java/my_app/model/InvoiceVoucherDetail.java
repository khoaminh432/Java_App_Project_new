package my_app.model;

import java.math.BigDecimal;
import java.util.Map;

public class InvoiceVoucherDetail {
    private Integer id;
    private Integer invoiceId;
    private Integer voucherId;
    private BigDecimal discountValue;
    private Invoice invoice; // Reference to Invoice object
    private Voucher voucher; // Reference to Voucher object
    
    // Constructors
    public InvoiceVoucherDetail() {}
    
    public InvoiceVoucherDetail(Integer id, Integer invoiceId, Integer voucherId,
                               BigDecimal discountValue) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.voucherId = voucherId;
        this.discountValue = discountValue;
    }

    public InvoiceVoucherDetail(Integer invoiceId, Integer voucherId,
                               BigDecimal discountValue) {
        this(null, invoiceId, voucherId, discountValue);
    }

    public InvoiceVoucherDetail(InvoiceVoucherDetail other) {
        this(other.id, other.invoiceId, other.voucherId, other.discountValue);
        this.invoice = other.invoice;
        this.voucher = other.voucher;
    }
    public InvoiceVoucherDetail(Map<String, Object> data) {
        applyFromMap(data);
    }
    public void applyFromMap(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }

        Integer newId = ModelMapperHelper.getInteger(data, "id");
        if (newId != null) {
            this.id = newId;
        }

        Integer newInvoiceId = ModelMapperHelper.getInteger(data, "invoiceId", "invoice_id");
        if (newInvoiceId != null) {
            this.invoiceId = newInvoiceId;
        }

        Integer newVoucherId = ModelMapperHelper.getInteger(data, "voucherId", "voucher_id");
        if (newVoucherId != null) {
            this.voucherId = newVoucherId;
        }

        BigDecimal newDiscountValue = ModelMapperHelper.getBigDecimal(data, "discountValue", "discount_value");
        if (newDiscountValue != null) {
            this.discountValue = newDiscountValue;
        }
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }
    
    public Integer getVoucherId() { return voucherId; }
    public void setVoucherId(Integer voucherId) { this.voucherId = voucherId; }
    
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
    
    public Voucher getVoucher() { return voucher; }
    public void setVoucher(Voucher voucher) { this.voucher = voucher; }
    
    @Override
    public String toString() {
        return "InvoiceVoucherDetail{" +
               "id=" + id +
               ", invoiceId=" + invoiceId +
               ", voucherId=" + voucherId +
               ", discountValue=" + discountValue +
               '}';
    }
}