package org.leagueoftests.repository;

import org.leagueoftests.app.Champions;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ChampionsRepositoryImplementation implements ChampionsRepository {

    private Connection connection;

    private PreparedStatement addChampionsStmt;
    private PreparedStatement getAllChampionsStmt;
    private PreparedStatement getByIDStmt;
    private PreparedStatement getByPriceBEStmt;
    private PreparedStatement getByPriceRPStmt;
    private PreparedStatement updateChampionStmt;
    private PreparedStatement deleteChapionStmt;

    public ChampionsRepositoryImplementation(Connection connection) throws SQLException {
        this.connection = connection;
        if (!isDatabaseReady()) {
            createTables();
        }
        setConnection(connection);
    }
    @Override
    public void createTables() throws SQLException {
        connection.createStatement()
                .executeUpdate("CREATE TABLE " + "Champion(id bigint GENERATED BY DEFAULT AS IDENTITY, "
                        + "name varchar(20) NOT NULL, priceBE integer, priceRP integer)");
    }

    public boolean isDatabaseReady() {
        try {
            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if ("Champion".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }
            return tableExists;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int add(Champions champion) {
        int count = 0;
        try {
            addChampionsStmt.setString(1, champion.getChampionName());
            addChampionsStmt.setInt(2, champion.getChampionPriceBE());
            addChampionsStmt.setInt(3, champion.getChampionPriceRP());
            count = addChampionsStmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;
    }
    @Override
    public List<Champions> getAll() {
        List<Champions> champions = new LinkedList<>();
        try {
            ResultSet rs = getAllChampionsStmt.executeQuery();

            while (rs.next()) {
                Champions c = new Champions();
                c.setId(rs.getInt("id"));
                c.setChampionName(rs.getString("name"));
                c.setChampionPriceBE(rs.getInt("priceBE"));
                c.setChampionPriceRP(rs.getInt("priceRP"));
                champions.add(c);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return champions;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    @Override
    public void setConnection(Connection connection) throws SQLException {
        this.connection = connection;
        addChampionsStmt = connection
                .prepareStatement("INSERT INTO Champion (name, priceBE, priceRP) VALUES (?, ?, ?)");
        getAllChampionsStmt = connection.prepareStatement("SELECT id, name, priceBE, priceRP FROM Champion");

        deleteChapionStmt = connection.prepareStatement("DELETE FROM Champion WHERE id = ?");

        getByIDStmt = connection.prepareStatement("SELECT id, name, priceBE, priceRP FROM Champion WHERE id = ?");
        getByPriceBEStmt = connection.prepareStatement("SELECT id, name, priceBE, priceRP FROM Champion WHERE priceBE = ?");
        getByPriceRPStmt = connection.prepareStatement("SELECT id, name, priceBE, priceRP FROM Champion WHERE priceRP = ?");
        updateChampionStmt = connection.prepareStatement("UPDATE Champion SET name = ?, priceBE = ?, priceRP = ? WHERE id = ?");
    }

    @Override
    public int delete(Champions c) {
        int count = 0;
        try {
            deleteChapionStmt.setInt(1, c.getId());
            count = deleteChapionStmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;
    }

    @Override
    public int update(Champions oldChampion, Champions newChampion) {
        int count = 0;
        try {
            updateChampionStmt.setString(1, newChampion.getChampionName());
            updateChampionStmt.setInt(2, newChampion.getChampionPriceBE());
            updateChampionStmt.setInt(3, newChampion.getChampionPriceRP());
            updateChampionStmt.setInt(4, oldChampion.getId());
            count = updateChampionStmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return count;
    }

    @Override
    public Champions getById(int id) {
        Champions champion = new Champions();
        try {
            getByIDStmt.setInt(1, id);
            ResultSet rs = getByIDStmt.executeQuery();

            while (rs.next()) {
                champion.setId(rs.getInt("id"));
                champion.setChampionName(rs.getString("name"));
                champion.setChampionPriceBE(rs.getInt("priceBE"));
                champion.setChampionPriceRP(rs.getInt("priceRP"));

            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return champion;
    }

    @Override
    public Champions getByBEPrice(int priceBE) {
        Champions champion = new Champions();
        try {
            getByPriceBEStmt.setInt(1, priceBE);
            ResultSet rs = getByPriceBEStmt.executeQuery();

            while (rs.next()) {
                champion.setId(rs.getInt("id"));
                champion.setChampionName(rs.getString("name"));
                champion.setChampionPriceBE(rs.getInt("priceBE"));
                champion.setChampionPriceRP(rs.getInt("priceRP"));

            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return champion;
    }

    @Override
    public Champions getByRPPrice(int priceRP) {
        Champions champion = new Champions();
        try {
            getByPriceRPStmt.setInt(1, priceRP);
            ResultSet rs = getByPriceRPStmt.executeQuery();

            while (rs.next()) {
                champion.setId(rs.getInt("id"));
                champion.setChampionName(rs.getString("name"));
                champion.setChampionPriceBE(rs.getInt("priceBE"));
                champion.setChampionPriceRP(rs.getInt("priceRP"));

            }

        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
        }
        return champion;
    }

}