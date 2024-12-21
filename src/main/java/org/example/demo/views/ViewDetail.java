package org.example.demo.views;

import org.example.demo.core.implementations.ViewsImpl;
import org.example.demo.data.entities.Detail;
import org.example.demo.views.interfaces.IViewDetail;

public class ViewDetail extends ViewsImpl<Detail> implements IViewDetail {
    @Override
    public Detail instance() {
        return new Detail();
    }

    @Override
    public int askAmount() {
        System.out.println("Input the amount of that article you want:");
        return scanner.nextInt();
    }
}