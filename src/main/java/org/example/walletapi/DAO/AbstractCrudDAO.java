package org.example.walletapi.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.walletapi.DAO.CrudDAO;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractCrudDAO<T> implements CrudDAO<T> {

    protected DatabaseConnection connection;
    private T toSave;

    protected abstract T mapResultSetToEntity(ResultSet rs);

    @Override
    public List<T> findAll() {
        List<T> entityList = new ArrayList<>();

        String sql = "SELECT * FROM " + getTableName() + ";";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                T entity = mapResultSetToEntity(resultSet);
                entityList.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entityList;
    }

    @Override
    public T save(T toSave) {
        String sql = "INSERT INTO " + getTableName() + " " + getInsertColumns() + " VALUES " + getInsertValues() + ";";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(preparedStatement, toSave);

            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return findById(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    // Méthode abstraite pour obtenir le nom de la table dans la base de données
    protected abstract String getTableName();

    //Use  reflection for getting dynamicly the columns to insert
    private String getInsertColumns(){
        StringBuilder columns = new StringBuilder("(");

        Field[] fields = toSave.getClass().getDeclaredFields();
            for (Field field : fields){
                columns.append(field.getName()).append(", ");
            }
            columns.delete(columns.length() - 2, columns.length()); //Remove last ','
        columns.append(")");

        return columns.toString();
    };

    // Méthode abstraite pour obtenir les valeurs à insérer dans la requête SQL INSERT
    private String getInsertValues(){
        StringBuilder values = new StringBuilder("VALUES (");

        Field[] fields = toSave.getClass()
                .getDeclaredFields();
    for (Field field : fields) {
        values.append("?, ");
    }

    values.delete(values.length()-2, values.length());
    values.append(")");

        return values.toString();
    }

    // Méthode abstraite pour définir les paramètres de la requête SQL INSERT
    private void setInsertParameters(PreparedStatement preparedStatement, T toSave) {
        Field[] fields = toSave.getClass().getDeclaredFields();
        int parameterIndex = 1;

        for (Field field : fields){
            try {
                field.setAccessible(true);
                Object value = field.get(toSave);
                preparedStatement.setObject(parameterIndex++, value);
            }catch  ( IllegalAccessException | SQLException e){
                e.printStackTrace();
            }
        }
    }

    // Méthode abstraite pour obtenir un enregistrement par son ID
    protected abstract T findById(int id);
}

