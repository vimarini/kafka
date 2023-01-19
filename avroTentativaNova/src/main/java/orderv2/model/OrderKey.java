package orderv2.model;

import java.util.Date;
import java.util.Objects;

public class OrderKey {
    private final String customerId;
    private final Date purchaseDate;

    public OrderKey(String customerId, Date transactionalDate) {
        this.customerId = customerId;
        this.purchaseDate = transactionalDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderKey that = (OrderKey) o;
        return Objects.equals(getCustomerId(), that.getCustomerId()) && Objects.equals(getPurchaseDate(), that.getPurchaseDate());
    }

    @Override
    public int hashCode(){
        return Objects.hash(getCustomerId(), getPurchaseDate());
    }
}
