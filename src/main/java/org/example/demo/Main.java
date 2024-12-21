package org.example.demo;

import org.example.demo.data.entities.*;
import org.example.demo.data.entities.enums.Role;
import org.example.demo.data.entities.enums.State;
import org.example.demo.data.entities.enums.Status;
import org.example.demo.data.repositories.database.*;
import org.example.demo.data.repositories.interfaces.*;
import org.example.demo.services.*;
import org.example.demo.services.interfaces.*;
import org.example.demo.views.*;
import org.example.demo.views.interfaces.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        IRepositoryClient repositoryClient = new RepositoryDbClient();
        IServiceClient serviceClient = new ServiceClient(repositoryClient);
        IViewClient viewClient = new ViewClient();

        IRepositoryDebt repositoryDebt = new RepositoryDbDebt();
        IServiceDebt serviceDebt = new ServiceDebt(repositoryDebt);
        IViewDebt viewDebt = new ViewDebt();

        IRepositoryAccount repositoryAccount = new RepositoryDbAccount();
        IServiceAccount serviceAccount = new ServiceAccount(repositoryAccount);
        IViewAccount viewAccount = new ViewAccount();

        IRepositoryArticle repositoryArticle = new RepositoryDbArticle();
        IServiceArticle serviceArticle = new ServiceArticle(repositoryArticle);
        IViewArticle viewArticle = new ViewArticle();

        IRepositoryDetail repositoryDetail = new RepositoryDbDetail();
        IServiceDetail serviceDetail = new ServiceDetail(repositoryDetail);
        IViewDetail viewDetail = new ViewDetail();

        IRepositoryPayment repositoryPayment = new RepositoryDbPayment();
        IServicePayment servicePayment = new ServicePayment(repositoryPayment);
        IViewPayment viewPayment = new ViewPayment();

        int choice, alterChoiceInteger;

        Account checker;
        String login, password;

        /*

        Client a = new Client("Soul", "123", "Earth");
        Account b = new Account("c", "c", Role.CLIENT);

        a.setAccount(b);
        b.setClient(a);

        serviceClient.save(a);
        serviceAccount.save(b);

        serviceAccount.save(new Account("s", "s", Role.SHOPKEEPER));
        serviceAccount.save(new Account("a", "a", Role.ADMIN));
        serviceArticle.save(new Article("Apple", 10, 1000));
        serviceArticle.save(new Article("Juice", 100, 500));

        */

        do {
            System.out.println("Enter your login:");
            login = scanner.nextLine();
            System.out.println("Enter your password:");
            password = scanner.nextLine();
            checker = serviceAccount.fetchByLoginAndPassword(login, password);
            if (checker == null) {
                System.out.println("\n[Invalid login or password...]\n");
            } else if (checker.getState() == State.DISABLED) {
                System.out.println("\n[Account disabled...]\n");
                checker = null;
            }
            if (checker != null && checker.getRole() == Role.SHOPKEEPER) {
                System.out.printf("\n[Shopkeeper connected: %s!]\n%n", checker.getLogin());
                do {
                    switch (choice = menuShopkeeper(scanner)) {
                        case 1 -> {
                            Client client = viewClient.instance();
                            if (viewAccount.askAccountCreate()) {
                                Account account = viewAccount.instance();
                                account.setRole(Role.CLIENT);
                                serviceAccount.save(account);
                                System.out.println("\n[Account created!]");
                                client.setAccount(account);
                                serviceClient.save(client);
                                System.out.println("\n[Client saved!]\n");
                                serviceAccount.editAccountOfClient(account, client);
                            } else {
                                serviceClient.save(client);
                                System.out.println("\n[Client saved!]\n");
                            }
                        }
                        case 2 -> {
                            if (serviceClient.show().isEmpty()) {
                                System.out.println("\n[No clients found...]\n");
                            } else {
                                serviceClient.show().forEach(System.out::println);
                                alterChoiceInteger = viewClient.askShowAccount();
                                if (alterChoiceInteger == 1) {
                                    if (serviceClient.fetchAllWithAccount().isEmpty()) {
                                        System.out.println("\n[No clients with accounts were found...]\n");
                                    } else {
                                        serviceClient.fetchAllWithAccount().forEach(System.out::println);
                                    }
                                }
                                if (alterChoiceInteger == 2) {
                                    if (serviceClient.fetchAllWithoutAccount().isEmpty()) {
                                        System.out.println("\n[No clients without accounts were found...]\n");
                                    } else {
                                        serviceClient.fetchAllWithoutAccount().forEach(System.out::println);
                                    }
                                }
                            }
                        }
                        case 3 -> {
                            Client client = serviceClient.fetchByPhone(viewClient.askPhoneNumber());
                            if (client == null) {
                                System.out.println("\n[Client not found...]\n");
                            } else {
                                System.out.println(client);
                            }
                        }
                        case 4 -> {
                            Client client = serviceClient.fetchByPhone(viewClient.askPhoneNumber());
                            if (client == null) {
                                System.out.println("\n[Client not found...]\n");
                            } else {
                                Debt debt = viewDebt.instance();
                                addArticles(serviceArticle, viewArticle, viewDetail, debt);
                                if (debt.getAmountTotal() == 0) {
                                    System.out.println("\n[Debt is empty...]\n");
                                } else {
                                    debt.setStatus(Status.ACCEPTED);
                                    debt.setAmountMissing(debt.getAmountTotal());
                                    debt.setClient(client);
                                    client.addDebt(debt);
                                    serviceDebt.save(debt);
                                    debt.getDetailList().forEach(detail -> {
                                        detail.setId(debt.getId());
                                        serviceDetail.save(detail);
                                    });
                                    System.out.println("\n[Debt saved!]\n");
                                }
                            }
                        }
                        case 5 -> {
                            Client client = serviceClient.fetchByPhone(viewClient.askPhoneNumber());
                            if (client == null) {
                                System.out.println("\n[Client not found...]\n");
                            } else {
                                Debt debt = serviceDebt.fetchById(viewDebt.askId());
                                if (debt == null) {
                                    System.out.println("\n[Debt not found...]\n");
                                } else if (debt.getAmountMissing() == 0) {
                                    System.out.println("\n[Debt has been fully paid...]\n");
                                } else {
                                    Payment payment = viewPayment.instance();
                                    if (payment.getAmount() < 0 || payment.getAmount() > debt.getAmountTotal()) {
                                        System.out.println("\n[Payment invalid...]\n");
                                    } else {
                                        serviceDebt.editDebtAmountSent(debt, debt.getAmountSent() + payment.getAmount());
                                        serviceDebt.editDebtAmountMissing(debt, debt.getAmountMissing() - payment.getAmount());
                                        debt.addPayment(payment);
                                        payment.setDebt(debt);
                                        servicePayment.save(payment);
                                        System.out.println("\n[Payment saved!]\n");
                                    }
                                }
                            }
                        }
                        case 6 -> {
                            if (serviceDebt.fetchAllUnpaidDebts().isEmpty()) {
                                System.out.println("\n[No debts unpaid...]\n");
                            } else {
                                serviceDebt.fetchAllUnpaidDebts().forEach(System.out::println);
                                alterChoiceInteger = viewDebt.askShow();
                                if (alterChoiceInteger == 1) {
                                    Debt debt = serviceDebt.fetchById(viewDebt.askId());
                                    if (debt == null) {
                                        System.out.println("\n[Debt not found...]\n");
                                    } else {
                                        serviceDetail.fetchDetailsFromDebt(debt).forEach(detail -> System.out.printf("-- Article: '%s', Price: '%d', Amount: '%d', Total: '%d' --%n",
                                                detail.getArticle().getLabel(),
                                                detail.getArticle().getPrice(),
                                                detail.getStock(),
                                                detail.getArticle().getPrice() * detail.getStock())
                                        );
                                    }
                                }
                                if (alterChoiceInteger == 2) {
                                    Debt debt = serviceDebt.fetchById(viewDebt.askId());
                                    if (debt == null) {
                                        System.out.println("\n[Debt not found...]\n");
                                    } else {
                                        servicePayment.fetchPaymentsFromDebt(debt).forEach(System.out::println);
                                    }
                                }
                            }
                        }
                        case 7 -> {
                            alterChoiceInteger = viewDebt.askShowPending();
                            if (alterChoiceInteger == 1) {
                                if (serviceDebt.fetchAllByStatus(Status.PENDING).isEmpty()) {
                                    System.out.println("\n[No pending debts were found...]\n");
                                } else {
                                    serviceDebt.fetchAllByStatus(Status.PENDING).forEach(System.out::println);
                                }
                            }
                            if (alterChoiceInteger == 2) {
                                if (serviceDebt.fetchAllByStatus(Status.CANCELLED).isEmpty()) {
                                    System.out.println("\n[No cancelled debts were found...]\n");
                                } else {
                                    serviceDebt.fetchAllByStatus(Status.CANCELLED).forEach(System.out::println);
                                }
                            }
                        }
                        case 8 -> {
                            checker = null;
                            scanner.nextLine();
                        }
                    }
                } while (choice != 8);
            }
            if (checker != null && checker.getRole() == Role.ADMIN) {
                System.out.printf("\n[Admin connected: %s!]\n%n", checker.getLogin());
                do {
                    switch (choice = menuAdmin(scanner)) {
                        case 1 -> {
                            serviceClient.fetchAllWithoutAccount().forEach(System.out::println);
                            Client client = serviceClient.fetchByPhone(viewClient.askPhoneNumber());
                            if (client == null) {
                                System.out.println("\n[Client not found...]\n");
                            } else if (client.getAccount() != null) {
                                System.out.println("\n[Client already has an account...]\n");
                            } else {
                                Account account = viewAccount.instance();
                                account.setRole(Role.CLIENT);
                                account.setClient(client);
                                serviceAccount.save(account);
                                client.setAccount(account);
                                serviceClient.editAccountOfClient(account, client);
                                System.out.println("\n[Account created!]\n");
                            }
                        }
                        case 2 -> {
                            Account account = viewAccount.instance();
                            if (viewAccount.askAccountRole()) {
                                account.setRole(Role.ADMIN);
                            } else {
                                account.setRole(Role.SHOPKEEPER);
                            }
                            serviceAccount.save(account);
                            System.out.println("\n[Account created!]\n");
                        }
                        case 3 -> {
                            serviceAccount.show().forEach(System.out::println);
                            Account account = serviceAccount.fetchById(viewAccount.askId());
                            if (account == null) {
                                System.out.println("\n[Account not found...]\n");
                            } else {
                                if (account.getState() == State.ENABLED) {
                                    serviceAccount.editState(account, State.DISABLED);
                                    System.out.println("\n[Account state changed to: DISABLED!]\n");
                                } else {
                                    serviceAccount.editState(account, State.ENABLED);
                                    System.out.println("\n[Account state changed to: ENABLED!]\n");
                                }
                            }
                        }
                        case 4 -> {
                            if (serviceAccount.show().isEmpty()) {
                                System.out.println("\n[No accounts found...]\n");
                            } else {
                                serviceAccount.show().forEach(System.out::println);
                                alterChoiceInteger = viewAccount.askOptions();
                                if (alterChoiceInteger == 1) {
                                    serviceAccount.fetchAllClientAccounts().forEach(System.out::println);
                                }
                                if (alterChoiceInteger == 2) {
                                    serviceAccount.fetchAllShopkeeperAccounts().forEach(System.out::println);
                                }
                                if (alterChoiceInteger == 3) {
                                    serviceAccount.fetchAllAdminAccounts().forEach(System.out::println);
                                }
                                if (alterChoiceInteger == 4) {
                                    serviceAccount.fetchAllEnabledAccounts().forEach(System.out::println);
                                }
                                if (alterChoiceInteger == 5) {
                                    serviceAccount.fetchAllDisabledAccounts().forEach(System.out::println);
                                }
                            }
                        }
                        case 5 -> {
                            serviceArticle.save(viewArticle.instance());
                            System.out.println("\n[Article saved!]\n");
                        }
                        case 6 -> serviceArticle.fetchAllAvailableArticles().forEach(System.out::println);
                        case 7 -> {
                            Article article = serviceArticle.fetchById(viewArticle.askId());
                            if (article == null) {
                                System.out.println("\n[Article not found...]\n");
                            } else {
                                serviceArticle.editArticleStock(article, viewArticle.askNewStock());
                                System.out.println("\n[Article stock updated!]\n");
                            }
                        }
                        case 8 -> serviceDebt.fetchAllPaidDebts().forEach(System.out::println);
                        case 9 -> {
                            checker = null;
                            scanner.nextLine();
                        }
                    }
                } while (choice != 9);
            }
            if (checker != null && checker.getRole() == Role.CLIENT) {
                System.out.printf("\n[Client connected: %s!]\n%n", checker.getClient().getSurname());
                do {
                    switch (choice = menuClient(scanner)) {
                        case 1 -> {
                            if (serviceDebt.fetchAllUnpaidDebtsOfClient(checker.getClient()).isEmpty()) {
                                System.out.println("\n[Client has no unpaid debts...]\n");
                            } else {
                                serviceDebt.fetchAllUnpaidDebtsOfClient(checker.getClient()).forEach(System.out::println);
                            }
                        }
                        case 2 -> {
                            Debt debt = viewDebt.instance();
                            debt.setStatus(Status.PENDING);
                            addArticles(serviceArticle, viewArticle, viewDetail, debt);
                            if (debt.getAmountTotal() == 0) {
                                System.out.println("\n[Debt is empty...]\n");
                            } else {
                                debt.setStatus(Status.PENDING);
                                debt.setAmountMissing(debt.getAmountTotal());
                                debt.setClient(checker.getClient());
                                checker.getClient().addDebt(debt);
                                serviceDebt.save(debt);
                                debt.getDetailList().forEach(detail -> {
                                    detail.setId(debt.getId());
                                    serviceDetail.save(detail);
                                });
                                System.out.println("\n[Debt saved!]\n");
                                System.out.println("\n[Debt saved!]\n");
                            }
                        }
                        case 3 -> {
                            if (serviceDebt.fetchAllPendingDebtsOfClient(checker.getClient()).isEmpty()) {
                                System.out.println("\n[Client has no pending debts...]\n");
                            } else {
                                serviceDebt.fetchAllPendingDebtsOfClient(checker.getClient()).forEach(System.out::println);
                            }
                        }
                        case 4 -> {
                            if (serviceDebt.fetchAllCancelledDebtsOfClient(checker.getClient()).isEmpty()) {
                                System.out.println("\n[Client has no cancelled debts...]\n");
                            } else {
                                serviceDebt.fetchAllCancelledDebtsOfClient(checker.getClient()).forEach(System.out::println);
                                Debt debt = serviceDebt.fetchById(viewDebt.askId());
                                if (debt == null) {
                                    System.out.println("\n[Debt not found...]\n");
                                } else if (debt.getStatus() == Status.CANCELLED) {
                                    serviceDebt.editStatus(debt, Status.PENDING);
                                    System.out.println("\n[Debt has been bumped!]\n");
                                } else {
                                    System.out.println("\n[Debt is not cancelled...]\n");
                                }
                            }
                        }
                        case 5 -> {
                            checker = null;
                            scanner.nextLine();
                        }
                    }
                } while (choice != 5);
            }
        } while (checker == null);
    }

    public static int menuShopkeeper(Scanner scanner) {
        System.out.println("1- Create a Client");
        System.out.println("2- List all clients");
        System.out.println("3- Find a Client by his phone number");
        System.out.println("4- Create a debt for a Client"); // Done
        System.out.println("5- Save a payment for a Client");
        System.out.println("6- Show unpaid debts");
        System.out.println("7- Show debts demands");
        System.out.println("8- Leave");
        return scanner.nextInt();
    }

    public static int menuAdmin(Scanner scanner) {
        System.out.println("1- Create a account for a Client");
        System.out.println("2- Create more accounts");
        System.out.println("3- Activate/Deactivate an account");
        System.out.println("4- Show users account");
        System.out.println("5- Create article");
        System.out.println("6- List available articles");
        System.out.println("7- Update an article's stock");
        System.out.println("8- Show all paid debts");
        System.out.println("9- Leave");
        return scanner.nextInt();
    }

    public static int menuClient(Scanner scanner) {
        System.out.println("1- Show unpaid debts"); // Done
        System.out.println("2- Demand a debt"); // Done
        System.out.println("3- Show demanded debts"); // Done
        System.out.println("4- Bump a cancelled debt"); // Done
        System.out.println("5- Leave");
        return scanner.nextInt();
    }

    private static void addArticles(IServiceArticle serviceArticle, IViewArticle viewArticle, IViewDetail viewDetail, Debt debt) {
        int alterChoiceInteger;
        do {
            serviceArticle.fetchAllAvailableArticles().forEach(System.out::println);
            Article article = serviceArticle.fetchById(viewArticle.askId());
            if (article == null) {
                System.out.println("\n[Article not found...]\n");
            } else if (article.getStock() == 0) {
                System.out.println("\n[Article has no stock...]\n");
            } else {
                alterChoiceInteger = viewDetail.askAmount();
                if (alterChoiceInteger < 0 || alterChoiceInteger > article.getStock()) {
                    System.out.println("\n[Article does not have enough stock...]\n");
                } else {
                    Detail detail = viewDetail.instance();
                    detail.setArticle(article);
                    detail.setStock(alterChoiceInteger);
                    debt.setAmountTotal(debt.getAmountTotal() + alterChoiceInteger * article.getPrice());
                    detail.setDebt(debt);
                    debt.addDetail(detail);
                    serviceArticle.editArticleStock(article, article.getStock() - alterChoiceInteger);
                }
            }
        } while (viewArticle.askAddArticle());
    }

    public static void dd(Object obj) {
        if (obj != null) {
            System.out.println(obj);
        } else {
            System.out.println("null");
        }
        throw new RuntimeException("\n\n\n[Execution stopped by dieDump]\n\n\n");
    }
}