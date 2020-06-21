package com.example.foodorderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderingapp.model.Cart;

import java.util.ArrayList;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FoodOrderingDB";

    public static final String TABLE_CART = "cart";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String QUANTITY = "quantity";


    private Context context;

    public CartDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CART + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                PRICE + " INTEGER, " +
                QUANTITY + " INTEGER)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }


    public boolean checkFoodExist(String foodName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + NAME + "=?", new String[]{foodName});

        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, cart.getFoodName());
        cv.put(PRICE, cart.getPrice());
        cv.put(QUANTITY, cart.getQuantity());

        db.insert(TABLE_CART, null, cv);
    }

    public void updateCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(QUANTITY, cart.getQuantity());

        db.update(TABLE_CART, cv, ID + "=?", new String[]{Integer.toString(cart.getId())});
    }

    public ArrayList<Cart> getAllCartItems(){
        ArrayList<Cart> listOrder = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Cart cart = new Cart();
            cart.setId(cursor.getInt(0));
            cart.setFoodName(cursor.getString(1));
            cart.setPrice(cursor.getInt(2));
            cart.setQuantity(cursor.getInt(3));
            listOrder.add(cart);
        }

        cursor.close();
        db.close();

        return listOrder;
    }

    public Cart getCartByName (String foodName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + NAME + "=?", new String[]{foodName});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        Cart cart = new Cart(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

        cursor.close();
        db.close();

        return cart;
    }

    public int getCartCount (){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + QUANTITY + ") FROM " + TABLE_CART, null);
        if (cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return -1;
    }

    public void deleteCartItem(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CART, ID + "=?", new  String[]{Integer.toString(cart.getId())});
    }

    public void deleteCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
    }
}
