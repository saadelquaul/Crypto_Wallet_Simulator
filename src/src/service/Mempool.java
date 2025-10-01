package service;

import model.Interfaces.ITransaction;
import repository.DatabaseConnectionManager;
import repository.TransactionRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class Mempool {
    private static final Logger LOGGER = Logger.getLogger(Mempool.class.getName());
    private static Mempool instance;
    private List<ITransaction> pendingTransactions;
    private TransactionRepository transactionRepository;

    private Mempool() throws SQLException {
        pendingTransactions = new ArrayList<>();
        transactionRepository = new TransactionRepository(DatabaseConnectionManager.getInstance());
    }

    public List<ITransaction> getPendingTransactions () {
        if(pendingTransactions.isEmpty())
            pendingTransactions = transactionRepository.findAllPendingTransactions();

        return pendingTransactions;
    }

    public static synchronized Mempool getInstance() throws SQLException {
        if (instance == null) {
            instance = new Mempool();
        }
        return instance;
    }

    public void sortTransactions() {
        pendingTransactions.sort(Comparator.comparingDouble(ITransaction::getFees).reversed());
    }

    public int getPosition(ITransaction transaction) {
        fillMempool();
        sortTransactions();
        for (int i = 0; i < pendingTransactions.size(); i++) {
            if (pendingTransactions.get(i).getId().equals(transaction.getId())) {
                return i + 1;
            }
        }
        return -1;
    }

    public double estimateConfirmationTime(int position) {
        // Estimated time: position * 10 minutes
        return position * 10.0; // in minutes
    }

    public void clearMempool() {
        pendingTransactions.clear();
        LOGGER.info("Mempool cleared.");
    }

    public int getMempoolSize() {
        fillMempool();
        return this.pendingTransactions.size();
    }

    private void fillMempool() {
        pendingTransactions = getPendingTransactions();
    }


}
