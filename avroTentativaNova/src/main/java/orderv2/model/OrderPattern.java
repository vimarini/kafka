package orderv2.model;

import java.util.Date;
import java.util.Objects;

public class OrderPattern {
    private String paymentMethod;
    private String itemPurchased;
    private Date date;
    private int amount;

    public OrderPattern(Builder builder){
        this.amount=builder.amount;
        this.itemPurchased=builder.itemPurchased;
        this.date=builder.date;
        this.paymentMethod= builder.paymentMethod;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder builder(Order order){
        return new Builder(order);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        return Double.compare(that.getAmount(), amount) == 0 && Objects.equals(paymentMethod, that.getPaymentMethod()) && Objects.equals(itemPurchased, that.getItemPurchased()) && Objects.equals(date, that.getPurchaseDate());
    }

    @Override
    public int hashCode(){
        return Objects.hash(itemPurchased,date,amount,paymentMethod);
    }
    public static final class Builder {
        private String paymentMethod;
        private String itemPurchased;
        private Date date;
        private int amount;

        private Builder(){

        }

        private Builder(Order order){
            this.date=order.getPurchaseDate();
            this.paymentMethod=order.getPaymentMethod();
            this.itemPurchased=order.getItemPurchased();
            this.amount=order.getAmount();
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

        public Builder date(Date date){
            this.date = date;
            return this;
        }
        public OrderPattern build(){
            return new OrderPattern(this);
        }
    }
}
