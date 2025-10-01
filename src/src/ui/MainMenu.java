package ui;

import exception.InsufficientFundsException;
import exception.InvalidAddressException;
import model.Interfaces.ITransaction;
import model.Interfaces.IWallet;
import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import service.Mempool;
import service.TransactionService;
import service.WalletService;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainMenu {

    private final Scanner scanner;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final Mempool mempool;
    private IWallet currentUserWallet;


    public MainMenu(WalletService walletService, TransactionService transactionService, Mempool mempool) {

        this.scanner = new Scanner(System.in);
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.mempool = mempool;
    }

    public void start() {
        byte choice;
        do {
            printMainMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    createWallet();
                    break;
                case 2:
                    selectWallet();
                    break;
                case 3:
                    if (currentUserWallet != null) {
                        createTransaction();
                    } else {
                        System.out.println("Please create or select a wallet first.");
                    }
                    break;
                case 4:
                    if (currentUserWallet != null) {
                        viewMempoolPosition();
                    } else {
                        System.out.println("Please create or select a wallet first.");
                    }
                    break;
                case 5:
                    viewCurrentMempoolState();
                    break;
                case 0:
                    System.out.println("Exiting Crypto Wallet Simulator. GoodBye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        } while (choice != 0);
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n=== Crypto Wallet Simulator Menu ===");
        if (currentUserWallet != null) {
            System.out.println("Current Wallet: " + currentUserWallet.getAddress() +
                    " (" + currentUserWallet.getType() + ") Balance: " +
                    String.format("%.2f", currentUserWallet.getBalance()));
        } else {
            System.out.println("No Wallet selected!");
        }

        System.out.println("1. Create a new Crypto Wallet");
        System.out.println("2. Select an existing Wallet");
        System.out.println("3. Create a new Transaction");
        System.out.println("4. View my transaction's position in Mempool");
        System.out.println("5. Consult current Mempool state");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

    }

    private byte getUserChoice() {
        while (!scanner.hasNextByte()) {
            System.out.println("Invalid input. Please enter a number!");
            scanner.next();
            System.out.print("Enter your choice: ");
        }
        byte choice = scanner.nextByte();
        scanner.nextLine();
        return choice;
    }

    private void createWallet() {
        System.out.println("\n--- Create New Wallet ---");
        System.out.print("Choose wallet type (BITCOIN/ETHEREUM):");
        String type = scanner.nextLine().toUpperCase();
        try {
            CryptoCurrencyType cryptoCurrencyType = CryptoCurrencyType.valueOf(type);
            IWallet newWallet = walletService.creatWallet(cryptoCurrencyType);
            System.out.println("Wallet created successfully!");
            System.out.println("ID: " + newWallet.getId());
            System.out.println("Address: " + newWallet.getAddress());
            System.out.println("Type: " + newWallet.getType());
            System.out.println("Balance: " + String.format("%.2f", newWallet.getBalance()));
            currentUserWallet = newWallet;

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid cryptocurrency type:. Please choose BITCOIN or ETHEREUM.");
        }
    }

    private void selectWallet() {
        System.out.println("\n--- Select Wallet ---");
        List<IWallet> wallets = walletService.getAllWallets();
        if (wallets.isEmpty()) {
            System.out.println("No Wallets available. Please create one first.");
            return;
        }

        System.out.println("Available Wallets:");
        wallets.forEach(wallet -> System.out.println(String.format("  ID: " + wallet.getId() + ", Address: " +
                wallet.getAddress() + ", Type: " + wallet.getType() + ", Balance: " + String.format("%.2f", wallet.getBalance()))));

        System.out.print("Enter the ID of the wallet to select: ");
        String input = scanner.nextLine();

        Optional<IWallet> selectedWallet = wallets.stream().filter(w -> w.getId().toString().equals(input) ||
                w.getAddress().equals(input)).findFirst();
        if (selectedWallet.isPresent()) {
            currentUserWallet = selectedWallet.get();
            System.out.println("Wallet " + currentUserWallet.getAddress() + " selected.");


        } else {
            System.out.println("Wallet not found.");
        }
    }

    private void createTransaction() {
        System.out.println("\n--- Create New Transaction ---");
        System.out.print("Destination Address: ");
        String destAddress = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = -1;
        while (amount == -1) {
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid amount. Please enter a number!");
                scanner.next();
                System.out.print("Amount: ");
            }
            amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
            }
        }
        System.out.print("Choose Fee Level (ECONOMICAL/STANDARD/FAST): ");
        String feeLevelStr = scanner.nextLine().toUpperCase();
        FeeLevel feeLevel;
        try {
            feeLevel = FeeLevel.valueOf(feeLevelStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid fee level. Defaulting to STANDARD.");
            feeLevel = FeeLevel.STANDARD;
        }

        try {
            ITransaction newTransaction = transactionService.createTransaction(
                    currentUserWallet.getAddress(), destAddress, amount,
                    feeLevel, currentUserWallet.getType()
            );

            currentUserWallet.addTransaction(newTransaction);
            System.out.println("Transaction created successfully!\n " + newTransaction);
        } catch (InvalidAddressException | InsufficientFundsException | IllegalArgumentException e) {
            System.out.println("Error creating new Transaction: " + e.getMessage());
        }
    }

    private void viewMempoolPosition() {
        System.out.println("\n--- View My Transactions Position in Mempool ---");
        if (currentUserWallet.getTransactions().isEmpty()) {
            System.out.println("No transactions found for this wallet.");
            return;
        }
        System.out.println("Your Transactions:");

        currentUserWallet.getTransactions().forEach(transaction -> System.out.println("ID: " + transaction.getId() + ", Fees: " +
                String.format("%.2f", transaction.getFees()) + ", Status: " + transaction.getStatus()));
        System.out.print("Enter the ID of the transaction to check position:");

        String transactionId = scanner.nextLine();
        Optional<ITransaction> transactionOpt = currentUserWallet.getTransactions().stream()
                .filter(transaction -> transaction.getId().toString().equals(transactionId))
                .findFirst();
        if(transactionOpt.isPresent()) {
            ITransaction transaction = transactionOpt.get();
            int position = mempool.getPosition(transaction);
            if(position != -1) {
                System.out.println("Your transaction (ID: " + transaction.getId() + ") is in position "+
                        position + " out of " + mempool.getMempoolSize() + " pending transactions.");
                System.out.println("Estimated confirmation time: " + String.format("%.2f", mempool.estimateConfirmationTime(position)) + " minutes");
            }else {
                System.out.println("Your transaction is not currently in the mempool (it might be confirmed or rejected).");

            }
        } else {
            System.out.println("Transaction not found in your wallet");
        }

    }


    private void viewCurrentMempoolState() {
        System.out.println("\n=== CURRENT MEMPOOL STATE ===");
        mempool.clearMempool();
        List<ITransaction> currentMempool =
                mempool.getPendingTransactions().stream()
                        .sorted(Comparator.comparingDouble(ITransaction::getFees).reversed())
                        .collect(Collectors.toList());
        System.out.println("Transactions in mempool: " +
                currentMempool.size());
        System.out.println("┌──────────────────────────────────┬───────────────┐");
        System.out.println("│ Transaction ID (partial)         │ Fees          │");
        System.out.println("├──────────────────────────────────┼───────────────┤");
        for (ITransaction tx : currentMempool) {
            String txIdPartial = tx.getId().toString().substring(0, 8) + "...";
            String userIndicator = (currentUserWallet != null &&
                    currentUserWallet.getTransactions().stream().anyMatch(myTx ->
                            myTx.getId().equals(tx.getId())))
                    ? " >>> YOUR TX" : "";
            System.out.printf("│ %-32s │ %-13s │%n", txIdPartial +
                    userIndicator, String.format("%.2f $", tx.getFees()));
        }
        System.out.println("└──────────────────────────────────┴───────────────┘");
    }
}

