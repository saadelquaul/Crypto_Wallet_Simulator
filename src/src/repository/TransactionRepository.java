package repository;

import model.Interfaces.ITransaction;
import model.Transaction;
import model.enums.CryptoCurrencyType;
import model.enums.FeeLevel;
import model.enums.TransactionStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionRepository implements IRepository<ITransaction, UUID> {

    private static final Logger LOGGER = Logger.getLogger(TransactionRepository.class.getName());
    private final DatabaseConnectionManager connectionManager;

    public TransactionRepository(DatabaseConnectionManager connectionManager) throws SQLException {
        this.connectionManager = connectionManager;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (\n" +
                " id UUID PRIMARY KEY, \n" +
                " SOURCE_ADDRESS VARCHAR(100) not null,\n" +
                " destination_address varchar(100) not null,\n" +
                " amount double precision not null,\n" +
                " creation_date timestamp not null,\n" +
                " fees double precision not null,\n" +
                " fee_level varchar(50) not null,\n" +
                " status varchar(50) not null,\n" +
                " crypto_currency_type varchar(50) not null );";
        try (Connection conn = connectionManager.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Transactions table ensured to exist.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction table", e);
        }

    }

    @Override
    public ITransaction save(ITransaction transaction) {
        String sql = "INSERT INTO transactions (id, source_address,\n" +
                "destination_address, amount, creation_date, fees, fee_level, status,\n" +
                "crypto_currency_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) \n" +
                "ON CONFLICT (id) DO UPDATE SET source_address =\n" +
                "EXCLUDED.source_address, destination_address = EXCLUDED.destination_address,\n" +
                "amount = EXCLUDED.amount, creation_date = EXCLUDED.creation_date, fees =\n" +
                "EXCLUDED.fees, fee_level = EXCLUDED.fee_level, status = EXCLUDED.status,\n" +
                "crypto_currency_type = EXCLUDED.crypto_currency_type;";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, transaction.getId());
            pstmt.setString(2, transaction.getSourceAddress());
            pstmt.setString(3, transaction.getDestinationAddress());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setTimestamp(5,
                    Timestamp.valueOf(transaction.getCreationTime()));
            pstmt.setDouble(6, transaction.getFees());
            pstmt.setString(7, transaction.getFeeLevel().name());
            pstmt.setString(8, transaction.getStatus().name());
            pstmt.setString(9, transaction.getCryptoCurrencyType().name());
            pstmt.executeUpdate();
            LOGGER.info("Transaction " + transaction.getId() + " saved successfully.");
            return transaction;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving transaction " +
                    transaction.getId(), e);
            return null;
        }

    }


    @Override
    public Optional<ITransaction> findById(UUID id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding transaction by id " + id,
                    e);
        }
        return Optional.empty();
    }

    @Override
    public List<ITransaction> findAll() {
        List<ITransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all transactions", e);
        }
        return transactions;
    }

    public List<ITransaction> findAllPendingTransactions() {
        List<ITransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions where status = 'PENDING'";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all transactions", e);
        }
        return transactions;
    }


    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Transaction " + id + " deleted successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transaction by id " + id,
                    e);
        }
    }

    private ITransaction mapResultSetToTransaction(ResultSet rs) throws
            SQLException {
        UUID id = (UUID) rs.getObject("id");
        String sourceAddress = rs.getString("source_address");
        String destinationAddress = rs.getString("destination_address");
        double amount = rs.getDouble("amount");
        LocalDateTime creationDate =
                rs.getTimestamp("creation_date").toLocalDateTime();
        double fees = rs.getDouble("fees");
        FeeLevel feeLevel = FeeLevel.valueOf(rs.getString("fee_level"));
        TransactionStatus status =
                TransactionStatus.valueOf(rs.getString("status"));
        CryptoCurrencyType cryptoCurrencyType =
                CryptoCurrencyType.valueOf(rs.getString("crypto_currency_type"));
        return new Transaction(id, sourceAddress, destinationAddress, amount
                , feeLevel, cryptoCurrencyType, status, creationDate, fees);
    }

}
