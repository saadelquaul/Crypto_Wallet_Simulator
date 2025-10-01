import repository.DatabaseConnectionManager;
import repository.TransactionRepository;
import repository.WalletRepository;
import service.Mempool;
import service.TransactionService;
import service.WalletService;
import ui.MainMenu;

import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) throws SQLException {

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);
        rootLogger.setUseParentHandlers(false);

        DatabaseConnectionManager connectionManager = DatabaseConnectionManager.getInstance();
        WalletRepository walletRepository = new WalletRepository(connectionManager);
        TransactionRepository transactionRepository = new TransactionRepository(connectionManager);
        Mempool mempool = Mempool.getInstance();

        WalletService walletService = new WalletService(walletRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, walletService);
        MainMenu menu = new MainMenu(walletService, transactionService, mempool);
        menu.start();
    }
}