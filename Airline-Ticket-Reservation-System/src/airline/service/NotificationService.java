package airline.notification;

import airline.model.Booking;
import airline.model.Payment;
import airline.repository.PaymentRepository;


import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepository,
                          NotificationService notificationService) {

        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;

    }


    /**
     * Register Observer
     */
    public void addObserver(NotificationObserver observer) {

        if (observer == null) {
            throw new RuntimeException("Observer cannot be null.");
        }

        observers.add(observer);
    }

    /**
     * Remove Observer
     */
    public void removeObserver(NotificationObserver observer) {

        observers.remove(observer);
    }

    /**
     * Remove All Observers
     */
    public void removeAllObservers() {

        observers.clear();
    }

    /**
     * Total Observers
     */
    public int totalObservers() {

        return observers.size();
    }

    /**
     * Booking Notification
     */
    public void notifyBooking(Booking booking) {

        System.out.println("\nSending Booking Notifications...");

        observers.forEach(observer ->
                observer.bookingNotification(booking));
    }

    /**
     * Payment Notification
     */
    public void notifyPayment(Payment payment) {

        System.out.println("\nSending Payment Notifications...");

        observers.forEach(observer ->
                observer.paymentNotification(payment));
    }

    /**
     * Display Registered Observers
     */
    public void displayObservers() {

        System.out.println("\n========== REGISTERED OBSERVERS ==========");

        if (observers.isEmpty()) {
            System.out.println("No Observers Registered.");
            return;
        }

        observers.forEach(observer ->
                System.out.println(observer.getClass().getSimpleName()));

        System.out.println("==========================================");
    }

}