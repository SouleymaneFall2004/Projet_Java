package org.example.demo.views;

import org.example.demo.core.implementations.ViewsImpl;
import org.example.demo.data.entities.Client;
import org.example.demo.views.interfaces.IViewClient;

public class ViewClient extends ViewsImpl<Client> implements IViewClient {
    @Override
    public Client instance() {
        Client client = new Client();
        System.out.println("Client's name:");
        client.setSurname(scanner.nextLine());
        System.out.println("Client's phone number:");
        client.setPhone(scanner.nextLine());
        System.out.println("Client's address:");
        client.setAddress(scanner.nextLine());
        return client;
    }

    @Override
    public String askPhoneNumber() {
        System.out.println("Input the phone number of the client:");
        return scanner.nextLine();
    }

    @Override
    public int askShowAccount() {
        System.out.println("[1) Show all with an account 2) Show all without an account]");
        return scanner.nextInt();
    }
}