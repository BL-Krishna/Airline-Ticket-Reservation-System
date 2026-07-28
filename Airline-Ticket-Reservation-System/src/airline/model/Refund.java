package airline.model;

import java.time.LocalDateTime;

public class Refund {

    private String refundId;

    private Booking booking;

    private Payment payment;

    private double refundAmount;

    private RefundStatus refundStatus;

    private LocalDateTime refundDate;

    public Refund() {
    }

    public Refund(String refundId,
                  Booking booking,
                  Payment payment,
                  double refundAmount,
                  RefundStatus refundStatus,
                  LocalDateTime refundDate) {

        this.refundId = refundId;
        this.booking = booking;
        this.payment = payment;
        this.refundAmount = refundAmount;
        this.refundStatus = refundStatus;
        this.refundDate = refundDate;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public LocalDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }

    @Override
    public String toString() {

        return "Refund{" +
                "refundId='" + refundId + '\'' +
                ", bookingId='" + booking.getBookingId() + '\'' +
                ", paymentId='" + payment.getPaymentId() + '\'' +
                ", refundAmount=" + refundAmount +
                ", refundStatus=" + refundStatus +
                ", refundDate=" + refundDate +
                '}';
    }

}