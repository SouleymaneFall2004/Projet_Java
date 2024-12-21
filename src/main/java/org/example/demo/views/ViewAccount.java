package org.example.demo.views;

import org.example.demo.core.implementations.ViewsImpl;
import org.example.demo.data.entities.Account;
import org.example.demo.views.interfaces.IViewAccount;

public class ViewAccount extends ViewsImpl<Account> implements IViewAccount {
    @Override
    public Account instance() {
        Account account = new Account();
        System.out.println("Create a login:");
        account.setLogin(scanner.nextLine());
        System.out.println("Create a password:");
        account.setPassword(scanner.nextLine());
        return account;
    }

    @Override
    public boolean askAccountCreate() {
        System.out.println("Assign an account?\n1- Yes\n2- No");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice == 1;
    }

    @Override
    public boolean askAccountRole() {
        System.out.println("Admin or Shopkeeper?\n1- Admin\n2- Shopkeeper");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice == 1;
    }

    @Override
    public int askId() {
        System.out.println("Input the id of the account:");
        return scanner.nextInt();
    }

    @Override
    public int askOptions() {
        System.out.println("[" +
                "1) Client accounts " +
                "2) Shopkeeper accounts " +
                "3) Admin accounts " +
                "4) Enabled accounts " +
                "5) Disabled accounts" +
                "]");
        return scanner.nextInt();
    }
}