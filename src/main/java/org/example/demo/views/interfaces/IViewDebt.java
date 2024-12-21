package org.example.demo.views.interfaces;

import org.example.demo.core.Views;
import org.example.demo.data.entities.Debt;

public interface IViewDebt extends Views<Debt> {
    int askId();
    int askShow();
    int askShowPending();
}