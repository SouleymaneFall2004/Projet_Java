package org.example.demo.views;

import org.example.demo.core.implementations.ViewsImpl;
import org.example.demo.data.entities.Debt;
import org.example.demo.views.interfaces.IViewDebt;

public class ViewDebt extends ViewsImpl<Debt> implements IViewDebt {
    @Override
    public Debt instance() {
        Debt debt = new Debt();
        System.out.println("Debt's date:");
        debt.setDate(scanner.nextLine());
        return debt;
    }
    @Override
    public int askId() {
        System.out.println("Input the id of the debt:");
        return scanner.nextInt();
    }

    @Override
    public int askShow() {
        System.out.println("[1) Show all articles 2) Show all payments]");
        return scanner.nextInt();
    }

    @Override
    public int askShowPending() {
        System.out.println("[1) Show pending debts 2) Show cancelled debts]");
        return scanner.nextInt();
    }
}