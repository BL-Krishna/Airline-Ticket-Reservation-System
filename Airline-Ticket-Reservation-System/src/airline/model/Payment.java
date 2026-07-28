package airline.model;

import java.time.LocalDateTime;

public class Payment {

    private String paymentId;

    private Booking booking;

    private PaymentMethod paymentMethod;

    private double amount;

    private String transactionId;

    private LocalDateTime paymentDate;

    private PaymentStatus paymentStatus;

    public Payment() {
    }

    public Payment(String paymentId,
                   Booking booking,
                   PaymentMethod paymentMethod,
                   double amount,
                   String transactionId,
                   LocalDateTime paymentDate,
                   PaymentStatus paymentStatus) {

        this.paymentId = paymentId;
        this.booking = booking;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {

        return "\n===============================" +
                "\nPayment ID       : " + paymentId +
                "\nBooking ID       : " + booking.getBookingId() +
                "\nPassenger        : " + booking.getPassenger().getName() +
                "\nFlight Number    : " + booking.getFlight().getFlightNumber() +
                "\nPayment Method   : " + paymentMethod +
                "\nAmount           : " + amount +
                "\nTransaction ID   : " + transactionId +
                "\nPayment Date     : " + paymentDate +
                "\nPayment Status   : " + paymentStatus +
                "\n===============================";
    }

}