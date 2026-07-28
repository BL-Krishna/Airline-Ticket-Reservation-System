package airline.service;

import airline.enums.BookingPriority;
import airline.enums.BookingStatus;
import airline.model.Booking;
import airline.model.BookingRequest;
import java.util.PriorityQueue;

public class BookingQueueService {
    private final PriorityQueue<BookingRequest> queue = new PriorityQueue<>();

    public synchronized void addRequest(Booking booking, BookingPriority priority) {
        BookingRequest request = new BookingRequest(booking, priority);
        queue.add(request);
        System.out.println("Queued booking request: " + booking.getBookingId() + " [" + priority + "]");
    }

    public synchronized BookingRequest pollRequest() {
        return queue.poll();
    }

    public synchronized int getQueueSize() {
        return queue.size();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void displayQueue() {
        System.out.println("\n--- Current Booking Queue (Sorted by Priority & Timestamp) ---");
        if (queue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        PriorityQueue<BookingRequest> copy = new PriorityQueue<>(queue);
        while (!copy.isEmpty()) {
            System.out.println(copy.poll());
        }
        System.out.println("-------------------------------------------------------------");
    }

    public synchronized void processQueue() {
        System.out.println("\n========== PROCESSING BOOKING QUEUE ==========");
        int processedCount = 0;
        long startTime = System.currentTimeMillis();

        while (!queue.isEmpty()) {
            BookingRequest request = queue.poll();
            Booking booking = request.getBooking();
            System.out.println("Processing " + request.getPriority() + " booking request: " + booking.getBookingId() + " (Total Fare: ₹" + booking.getTotalFare() + ")");
            
            // Simulating queue confirmation processing
            booking.setBookingStatus(BookingStatus.BOOKED);
            processedCount++;
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Processed " + processedCount + " booking requests in " + duration + " ms.");
        System.out.println("===============================================");
    }
}
