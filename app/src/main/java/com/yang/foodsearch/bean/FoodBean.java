package com.yang.foodsearch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-04-24.
 */
public class FoodBean implements Serializable{
    private String status;
    private List<Categories> categories;

    public String getStatus() {
        return status;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FoodBean{" +
                "status='" + status + '\'' +
                ", categories=" + categories +
                '}';
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public static class Categories{
        private String category_name;
        private List<Subcategories> subcategories;

        public String getCategory_name() {
            return category_name;
        }

        public List<Subcategories> getSubcategories() {
            return subcategories;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        @Override
        public String toString() {
            return "Categories{" +
                    "category_name='" + category_name + '\'' +
                    ", subcategories=" + subcategories +
                    '}';
        }

        public void setSubcategories(List<Subcategories> subcategories) {
            this.subcategories = subcategories;
        }

        public static class Subcategories{
            private String category_name;
            private List<String> subcategoties;

            @Override
            public String toString() {
                return "Subcategories{" +
                        "category_name='" + category_name + '\'' +
                        ", subcategoties=" + subcategoties +
                        '}';
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public void setSubcategoties(List<String> subcategoties) {
                this.subcategoties = subcategoties;
            }

            public String getCategory_name() {
                return category_name;
            }

            public List<String> getSubcategoties() {
                return subcategoties;
            }
        }
    }
}
