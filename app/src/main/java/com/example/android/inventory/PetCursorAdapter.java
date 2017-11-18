/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.inventory.data.PetContract.PetEntry;

import static android.R.attr.id;
import static android.R.attr.targetActivity;

/**
 * {@link PetCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class PetCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PetCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);


        Button mSale = (Button) view.findViewById(R.id.sale);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int stockColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_STOCK);
        int priceColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PRICE);
        int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);

        // Read the pet attributes from the Cursor for the current pet
        String productName = cursor.getString(nameColumnIndex);
        String productStock = cursor.getString(stockColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);

        final int stock = cursor.getInt(stockColumnIndex);
        final int itemId = cursor.getInt(idColumnIndex);

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(productStock)) {
            productStock = context.getString(R.string.unknown_stock);
        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(productName);
        summaryTextView.setText(productStock + " in stock");
        priceTextView.setText(productPrice + " USD");


        mSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int stockx = stock;
                if (stockx > 0) {
                    stockx--;

                    int itemIdx = itemId;

                    ContentValues values = new ContentValues();
                    values.put(PetEntry.COLUMN_STOCK, stockx);
                    Context contextX = context;
                    int rowsAffected = contextX.getContentResolver().update(ContentUris.withAppendedId(PetEntry.CONTENT_URI, itemIdx), values, null, null);
                }

            }
        });
    }
}





