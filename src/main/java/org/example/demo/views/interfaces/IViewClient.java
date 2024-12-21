package org.example.demo.views.interfaces;

import org.example.demo.core.Views;
import org.example.demo.data.entities.Client;

public interface IViewClient extends Views<Client> {
    String askPhoneNumber();
    int askShowAccount();
}