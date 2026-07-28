package airline.repository;

import airline.model.Payment;
import airline.model.Refund;
import airline.model.RefundStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RefundRepository {

    private final Map<String, Refund> refundDatabase;

    public RefundRepository() {
        this.refundDatabase = new HashMap<>();
    }

    /**
     * Save Refund
     */
    public void save(Refund refund) {

        if (refund == null) {
            throw new RuntimeException("Refund cannot be null.");
        }

        refundDatabase.put(refund.getRefundId(), refund);
    }

    /**
     * Find Refund By Id
     */
    public Refund findRefund(String refundId) {

        return refundDatabase.get(refundId);
    }

    /**
     * Find All Refunds
     */
    public List<Refund> findAll() {

        return refundDatabase.values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * Refund Exists
     */
    public boolean exists(String refundId) {

        return refundDatabase.containsKey(refundId);
    }

    /**
     * Delete Refund
     */
    public void delete(String refundId) {

        refundDatabase.remove(refundId);
    }

    /**
     * Total Refunds
     */
    public long count() {

        return refundDatabase.size();
    }

    /**
     * Find Refund By Booking Id
     */
    public Optional<Refund> findByBooking(String bookingId) {

        return refundDatabase.values()
                .stream()
                .filter(refund ->
                        refund.getBooking().getBookingId().equals(bookingId))
                .findFirst();
    }

    /**
     * Find Refund By Payment Id
     */
    public Optional<Refund> findByPayment(String paymentId) {

        return refundDatabase.values()
                .stream()
                .filter(refund ->
                        refund.getPayment().getPaymentId().equals(paymentId))
                .findFirst();
    }

    /**
     * Find Successful Refunds
     */
    public List<Refund> successfulRefunds() {

        return refundDatabase.values()
                .stream()
                .filter(refund ->
                        refund.getRefundStatus() == RefundStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    /**
     * Find Failed Refunds
     */
    public List<Refund> failedRefunds() {

        return refundDatabase.values()
                .stream()
                .filter(refund ->
                        refund.getRefundStatus() == RefundStatus.FAILED)
                .collect(Collectors.toList());
    }

    /**
     * Pending Refunds
     */
    public List<Refund> pendingRefunds() {

        return refundDatabase.values()
                .stream()
                .filter(refund ->
                        refund.getRefundStatus() == RefundStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Total Refunded Amount
     */
    public double totalRefundAmount() {

        return refundDatabase.values()
                .stream()
                .filter(refund ->
                        refund.getRefundStatus() == RefundStatus.COMPLETED)
                .mapToDouble(Refund::getRefundAmount)
                .sum();
    }

    /**
     * Highest Refund
     */
    public Optional<Refund> highestRefund() {

        return refundDatabase.values()
                .stream()
                .max(java.util.Comparator.comparing(Refund::getRefundAmount));
    }

    /**
     * Lowest Refund
     */
    public Optional<Refund> lowestRefund() {

        return refundDatabase.values()
                .stream()
                .min(java.util.Comparator.comparing(Refund::getRefundAmount));
    }

    /**
     * Refunds Sorted By Amount
     */
    public List<Refund> sortByAmount() {

        return refundDatabase.values()
                .stream()
                .sorted(java.util.Comparator.comparing(Refund::getRefundAmount))
                .collect(Collectors.toList());
    }

    /**
     * Refunds Sorted By Date
     */
    public List<Refund> sortByDate() {

        return refundDatabase.values()
                .stream()
                .sorted(java.util.Comparator.comparing(Refund::getRefundDate))
                .collect(Collectors.toList());
    }

}