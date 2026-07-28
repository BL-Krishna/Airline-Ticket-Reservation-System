package airline.notification;

import airline.model.Booking;
import airline.model.Payment;

public class SMSNotification implements NotificationObserver {

    @Override
    public void bookingNotification(Booking booking) {

        System.out.println("\n************** SMS ****************");

        System.out.println("To : "
                + booking.getPassenger().getPhoneNumber());

        System.out.println("Booking Confirmed.");

        System.out.println("Flight : "
                + booking.getFlight().getFlightNumber());

        System.out.println("Seat : "
                + booking.getSeat().getSeatNumber());

        System.out.println("Booking ID : "
                + booking.getBookingId());

        System.out.println("***********************************");
    }

    @Override
    public void paymentNotification(Payment payment) {

        System.out.println("\n************** SMS ****************");

        System.out.println("To : "
                + payment.getBooking().getPassenger().getPhoneNumber());

        System.out.println("Payment Received.");

        System.out.println("Amount : ₹"
                + payment.getAmount());

        System.out.println("Transaction : "
                + payment.getTransactionId());

        System.out.println("***********************************");
    }

}