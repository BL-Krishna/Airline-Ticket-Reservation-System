package airline.payment;

public class DebitCardPayment implements PaymentStrategy {

    private String cardNumber;
    private String cardHolder;

    public DebitCardPayment(String cardNumber,
                            String cardHolder) {

        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public boolean pay(double amount) {

        if (cardNumber == null || cardNumber.length() != 16) {
            System.out.println("Invalid Debit Card.");
            return false;
        }

        System.out.println("--------------------------------------");
        System.out.println("Payment Method : Debit Card");
        System.out.println("Card Holder    : " + cardHolder);
        System.out.println("Card Number    : **** **** **** "
                + cardNumber.substring(12));
        System.out.println("Amount         : ₹" + amount);
        System.out.println("Transaction Successful");
        System.out.println("--------------------------------------");

        return true;
    }

}