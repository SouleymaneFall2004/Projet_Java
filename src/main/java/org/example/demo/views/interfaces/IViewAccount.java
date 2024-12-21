package org.example.demo.views.interfaces;

import org.example.demo.core.Views;
import org.example.demo.data.entities.Account;

public interface IViewAccount extends Views<Account> {
    int askId();
    int askOptions();
    boolean askAccountRole();
    boolean askAccountCreate();
}