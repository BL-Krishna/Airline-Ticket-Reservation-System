package airline.payment;

public class NetBankingPayment implements PaymentStrategy {

    private String bankName;
    private String userId;

    public NetBankingPayment(String bankName,
                             String userId) {

        this.bankName = bankName;
        this.userId = userId;
    }

    @Override
    public boolean pay(double amount) {

        if (bankName == null || bankName.isBlank()) {
            System.out.println("Invalid Bank.");
            return false;
        }

        System.out.println("--------------------------------------");
        System.out.println("Payment Method : Net Banking");
        System.out.println("Bank           : " + bankName);
        System.out.println("User ID        : " + userId);
        System.out.println("Amount         : ₹" + amount);
        System.out.println("Transaction Successful");
        System.out.println("--------------------------------------");

        return true;
    }

}