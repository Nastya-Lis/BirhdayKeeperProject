package com.example.birhdaykeeper.unit;

public enum Category {
    FRIENDS,
    RELATIVES,
    COLLEAGUES,
    BOSS,
    OTHER;

    int count;

    public int getCount() {
        return count;
    }

    public void setCount() {
        count++;
    }

    public static Category convertStringIntoCategoryType(String categoryString){

        Category[] categoryValues = Category.values();
        Category categoryReturn = null;
        for (Category category: categoryValues) {
            if(category.toString().equals(categoryString)){
                categoryReturn = category;
                break;
            }
        }
        return categoryReturn;
    }
}
