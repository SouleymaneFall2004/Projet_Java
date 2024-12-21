package org.example.demo.views;

import org.example.demo.core.implementations.ViewsImpl;
import org.example.demo.data.entities.Article;
import org.example.demo.views.interfaces.IViewArticle;

public class ViewArticle extends ViewsImpl<Article> implements IViewArticle {
    @Override
    public Article instance() {
        Article article = new Article();
        System.out.println("Article's label:");
        article.setLabel(scanner.nextLine());
        System.out.println("Article's stock:");
        article.setStock(scanner.nextInt());
        System.out.println("Article's price:");
        article.setPrice(scanner.nextInt());
        return article;
    }

    @Override
    public int askId() {
        System.out.println("Input the id of the article:");
        return scanner.nextInt();
    }

    @Override
    public boolean askAddArticle() {
        System.out.println("Do you wanna add more articles?\n1- Yes\n2- No");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice == 1;
    }

    @Override
    public int askNewStock() {
        int stock;
        do {
            System.out.println("Input the stock of the article:");
            stock = scanner.nextInt();
        } while (stock < 0);
        return stock;
    }
}