package airline.notification;

import airline.model.Booking;
import airline.model.Payment;

public interface NotificationObserver {

    void bookingNotification(Booking booking);

    void paymentNotification(Payment payment);

}