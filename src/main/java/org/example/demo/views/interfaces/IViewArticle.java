package org.example.demo.views.interfaces;

import org.example.demo.core.Views;
import org.example.demo.data.entities.Article;

public interface IViewArticle extends Views<Article> {
    int askId();
    boolean askAddArticle();
    int askNewStock();
}