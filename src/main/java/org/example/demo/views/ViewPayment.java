package org.example.demo.views;

import org.example.demo.core.implementations.ViewsImpl;
import org.example.demo.data.entities.Payment;
import org.example.demo.views.interfaces.IViewPayment;

public class ViewPayment extends ViewsImpl<Payment> implements IViewPayment {
    @Override
    public Payment instance() {
        Payment payment = new Payment();
        System.out.println("Payment's date:");
        payment.setDate(scanner.nextLine());
        System.out.println("Payment's amount:");
        payment.setAmount(scanner.nextInt());
        scanner.nextLine();
        return payment;
    }
}