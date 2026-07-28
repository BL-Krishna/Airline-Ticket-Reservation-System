package airline.singleton;

import airline.model.Booking;
import airline.model.Payment;
import airline.model.PaymentMethod;
import airline.model.PaymentStatus;
import airline.model.Refund;
import airline.model.RefundStatus;
import airline.payment.PaymentStrategy;
import airline.repository.PaymentRepository;
import airline.repository.RefundRepository;
import airline.service.NotificationService;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentManager {
    private static volatile PaymentManager instance;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final NotificationService notificationService;

    private PaymentManager() {
        this.paymentRepository = new PaymentRepository();
        this.refundRepository = new RefundRepository();
        this.notificationService = new NotificationService();
    }

    public static PaymentManager getInstance() {
        if (instance == null) {
            synchronized (PaymentManager.class) {
                if (instance == null) {
                    instance = new PaymentManager();
                }
            }
        }
        return instance;
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public RefundRepository getRefundRepository() {
        return refundRepository;
    }

    public synchronized Payment processPayment(Booking booking, PaymentMethod paymentMethod, PaymentStrategy paymentStrategy) {
        if (booking == null) {
            throw new RuntimeException("Booking not found.");
        }

        boolean paymentResult = paymentStrategy.pay(booking.getTotalFare());

        PaymentStatus paymentStatus = paymentResult ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        Payment payment = new Payment(
                "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                booking,
                paymentMethod,
                booking.getTotalFare(),
                "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(),
                LocalDateTime.now(),
                paymentStatus
        );

        paymentRepository.save(payment);

        if (paymentStatus == PaymentStatus.SUCCESS) {
            notificationService.notifyBooking(booking);
            notificationService.notifyPayment(payment);
        }

        return payment;
    }

    public synchronized Refund processRefund(Booking booking, Payment payment) {
        if (booking == null) {
            throw new RuntimeException("Booking not found.");
        }
        if (payment == null) {
            throw new RuntimeException("Payment not found.");
        }
        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Refund cannot be processed.");
        }

        Refund refund = new Refund(
                "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                booking,
                payment,
                payment.getAmount(),
                RefundStatus.COMPLETED,
                LocalDateTime.now()
        );

        refundRepository.save(refund);
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        booking.setBookingStatus(airline.enums.BookingStatus.CANCELLED);

        notificationService.notifyRefund(refund);
        return refund;
    }
}
