package orderv2.model;

import java.util.Date;

public class Order {
    private String customerName;
    private String customerId;
    private String paymentMethod;
    private String itemPurchased;
    private int amount;
    private Date purchaseDate;
    private String cep;

    public Order(){

    }

    public Order(Builder builder) {
        this.customerName = builder.customerName;
        this.customerId = builder.customerId;
        this.paymentMethod = builder.paymentMethod;
        this.itemPurchased = builder.itemPurchased;
        this.amount = builder.amount;
        this.purchaseDate = builder.purchaseDate;
        this.cep = builder.cep;
    }

    public static Builder newBuilder(){
        return  new Builder();
    }

    public static Builder newBuilder(Order order){
        Builder builder = new Builder();
        return builder;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getItemPurchased() {
        return itemPurchased;
    }

    public void setItemPurchased(String itemPurchased) {
        this.itemPurchased = itemPurchased;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    private static class Builder{
        private String customerName;
        private String customerId;
        private String paymentMethod;
        private String itemPurchased;
        private int amount;
        private Date purchaseDate;
        private String cep;
        private Builder(){

        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getItemPurchased() {
            return itemPurchased;
        }

        public void setItemPurchased(String itemPurchased) {
            this.itemPurchased = itemPurchased;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public Date getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(Date purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public String getCep() {
            return cep;
        }

        public void setCep(String cep) {
            this.cep = cep;
        }

        public Builder customerName(String name){
            this.customerName = name;
            return this;
        }
        public Builder customerId(String customerId){
            this.customerId = customerId;
            return this;
        }
        public Builder paymentMethod(String paymentMethod){
            this.paymentMethod = paymentMethod;
            return this;
        }
        public Builder itemPurchased(String itemPurchased){
            this.itemPurchased = itemPurchased;
            return this;
        }
        public Builder amount(int amount){
            this.amount = amount;
            return this;
        }
        public Builder purchaseDate(Date purchaseDate){
            this.purchaseDate = purchaseDate;
            return this;
        }
        public Builder cep(String cep){
            this.cep = cep;
            return this;
        }

        public Order build(){
            return new Order(this);
        }
    }
}
