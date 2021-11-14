package com.shoppingassist;

import com.shoppingassist.models.ShoppingItem;

public interface OnListItemInteractionListener {

    void onLinkClick(ShoppingItem item);
    void onSaveClick(ShoppingItem item);
}
