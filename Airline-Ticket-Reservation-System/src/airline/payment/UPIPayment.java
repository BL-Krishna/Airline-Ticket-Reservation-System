package airline.payment;

public class UPIPayment implements PaymentStrategy {

    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amount) {

        if (upiId == null || !upiId.contains("@")) {
            System.out.println("Invalid UPI ID.");
            return false;
        }

        System.out.println("--------------------------------------");
        System.out.println("Payment Method : UPI");
        System.out.println("UPI ID         : " + upiId);
        System.out.println("Amount         : ₹" + amount);
        System.out.println("Transaction Successful");
        System.out.println("--------------------------------------");

        return true;
    }

}