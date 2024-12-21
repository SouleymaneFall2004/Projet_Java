package org.example.demo.views.interfaces;

import org.example.demo.core.Views;
import org.example.demo.data.entities.Detail;

public interface IViewDetail extends Views<Detail> {
    int askAmount();
}