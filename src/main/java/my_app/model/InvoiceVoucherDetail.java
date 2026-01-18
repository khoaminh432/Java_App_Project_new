package my_app.model;

import java.math.BigDecimal;

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