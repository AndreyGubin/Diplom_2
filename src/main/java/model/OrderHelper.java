package model;

import java.util.List;

public class OrderHelper {
    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderHelper(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
