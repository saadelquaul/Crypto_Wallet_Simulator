package repository;

import model.Interfaces.IWallet;
import model.Wallet;
import model.enums.CryptoCurrencyType;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletRepository implements IRepository<IWallet, UUID> {

    private static final Logger LOGGER =
            Logger.getLogger(WalletRepository.class.getName());
    private final DatabaseConnectionManager connectionManager;

    public WalletRepository(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS wallets (\n" +
                " id UUID PRIMARY KEY,\n" +
                " address VARCHAR(255) NOT NULL UNIQUE,\n" +
                " balance DOUBLE PRECISION NOT NULL,\n" +
                " type VARCHAR(50) NOT NULL\n" +
                ");";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Wallets table ensured to exist.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating wallets table", e);
        }
    }

    @Override
    public IWallet save(IWallet wallet) {
        String sql = "INSERT INTO wallets (id, address, balance, type) VALUES " +
        "( ?, ?, ?, ?)" +
        "ON CONFLICT (id) DO UPDATE SET address = " +
        "EXCLUDED.address, balance = EXCLUDED.balance, type = EXCLUDED.type;";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, wallet.getId());
            pstmt.setString(2, wallet.getAddress());
            pstmt.setDouble(3, wallet.getBalance());
            pstmt.setString(4, wallet.getType().name());
            pstmt.executeUpdate();
            LOGGER.info("Wallet " + wallet.getId() + " saved successfully.");
            return wallet;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving wallet " + wallet.getId(),
                    e);
            return null;
        }
    }

    @Override
    public Optional<IWallet> findById(UUID id) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToWallet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding wallet by id " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<IWallet> findAll() {
        List<IWallet> wallets = new ArrayList<>();
        String sql = "SELECT * FROM wallets";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                wallets.add(mapResultSetToWallet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all wallets", e);
        }
        return wallets;
    }


    @Override
    public void deleteById(UUID id) {
        String sql = "DELETE FROM wallets WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Wallet " + id + " deleted successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting wallet by id " + id, e);
        }
    }


    public Optional<IWallet> findByAddress(String address) {
        String sql = "SELECT * FROM wallets WHERE address = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToWallet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding wallet by address " +
                    address, e);
        }
        return Optional.empty();
    }

    private IWallet mapResultSetToWallet(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String address = rs.getString("address");
        double balance = rs.getDouble("balance");
        CryptoCurrencyType type =
                CryptoCurrencyType.valueOf(rs.getString("type"));
        // Use appropriate concrete wallet class based on type
        Wallet wallet;
        if (type == CryptoCurrencyType.BITCOIN) {
            wallet = new Wallet(id, CryptoCurrencyType.BITCOIN, address, balance);
        } else if (type == CryptoCurrencyType.ETHEREUM) {
            wallet = new Wallet(id, CryptoCurrencyType.ETHEREUM, address, balance);
        } else {
            throw new SQLException("Unknown CryptoCurrencyType: " + type);
        }

        return wallet;
    }

}
