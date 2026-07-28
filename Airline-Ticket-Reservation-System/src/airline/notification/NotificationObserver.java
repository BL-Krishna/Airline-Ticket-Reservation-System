package airline.notification;

import airline.model.Booking;
import airline.model.Payment;
import airline.model.Refund;

public interface NotificationObserver {

    void bookingNotification(Booking booking);

    void paymentNotification(Payment payment);

    void refundNotification(Refund refund);

}