package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;
import org.example.model.Amount;
import org.example.model.Category;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements DAOInterface<Category>{
    private DatabaseConnection connection;

    public CategoryDAO() {
        this.connection = new DatabaseConnection();
    }

    @Override
    public List<Category> findAll() {
        List<Category> categoryList = new ArrayList<>();

        String sql= "SELECT * FROM transaction_category";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                categoryList.add(new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("category_type")
                ));
                for (Category category : categoryList){
                    category.toString();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public Category save(Category toSave) {

        String sql = "INSERT INTO transaction_category (category_name, category_type) VALUES ( ?, ?);";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, toSave.getCategoryName());
            preparedStatement.setString(2, toSave.getCategoryType());
            int rowAdded = preparedStatement.executeUpdate();
            if (rowAdded > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> saveAll(List<Category> toSave) {
        List<Category> categoryList = new ArrayList<>();

        for (Category category : toSave) {
            Category saveCategory = save(category);
            if (saveCategory != null) {
                categoryList.add(saveCategory);
            }
        }
        return categoryList;
    }
}
