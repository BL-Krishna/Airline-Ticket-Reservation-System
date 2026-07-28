package airline.notification;

import airline.model.Booking;
import airline.model.Payment;

public class EmailNotification implements NotificationObserver {

    @Override
    public void bookingNotification(Booking booking) {

        System.out.println("\n==========================================");
        System.out.println("           EMAIL NOTIFICATION");
        System.out.println("==========================================");
        System.out.println("To       : " + booking.getPassenger().getEmail());
        System.out.println("Subject  : Booking Confirmation");
        System.out.println();
        System.out.println("Hello " + booking.getPassenger().getName() + ",");
        System.out.println();
        System.out.println("Your booking has been confirmed.");
        System.out.println("Booking ID : " + booking.getBookingId());
        System.out.println("Flight     : " + booking.getFlight().getFlightNumber());
        System.out.println("Route      : "
                + booking.getFlight().getRoute().getSource()
                + " -> "
                + booking.getFlight().getRoute().getDestination());
        System.out.println("Seat       : " + booking.getSeat().getSeatNumber());
        System.out.println("Amount     : ₹" + booking.getTotalFare());
        System.out.println();
        System.out.println("Thank you for choosing our Airline.");
        System.out.println("==========================================");
    }

    @Override
    public void paymentNotification(Payment payment) {

        System.out.println("\n==========================================");
        System.out.println("          EMAIL NOTIFICATION");
        System.out.println("==========================================");
        System.out.println("To       : "
                + payment.getBooking().getPassenger().getEmail());
        System.out.println("Subject  : Payment Confirmation");
        System.out.println();
        System.out.println("Dear "
                + payment.getBooking().getPassenger().getName());

        System.out.println();

        System.out.println("Payment Successful.");

        System.out.println("Payment ID     : "
                + payment.getPaymentId());

        System.out.println("Transaction ID : "
                + payment.getTransactionId());

        System.out.println("Amount Paid    : ₹"
                + payment.getAmount());

        System.out.println("Status         : "
                + payment.getPaymentStatus());

        System.out.println();
        System.out.println("Thank you.");
        System.out.println("==========================================");
    }
    @Override
    public void refundNotification(Refund refund) {

        System.out.println("\n==========================================");
        System.out.println("          EMAIL NOTIFICATION");
        System.out.println("==========================================");

        System.out.println("To : "
                + refund.getBooking().getPassenger().getEmail());

        System.out.println("Subject : Refund Processed");

        System.out.println();

        System.out.println("Dear "
                + refund.getBooking().getPassenger().getName());

        System.out.println();

        System.out.println("Your booking has been cancelled.");

        System.out.println("Refund ID : "
                + refund.getRefundId());

        System.out.println("Booking ID : "
                + refund.getBooking().getBookingId());

        System.out.println("Refund Amount : ₹"
                + refund.getRefundAmount());

        System.out.println("Refund Status : "
                + refund.getRefundStatus());

        System.out.println();

        System.out.println("Your refund will be credited shortly.");

        System.out.println("==========================================");
    }

}