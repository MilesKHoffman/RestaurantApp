package Model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderNumber;
    private ArrayList<Integer> itemID = new ArrayList<>();
    private ArrayList<Integer> quantity = new ArrayList<>();
    private int orderType;
    private boolean complete = false;
    private String name;
    private int skipped = 0;

    /**
     * Creates an order with a name and number
     *
     * @param name        Customer name
     * @param orderNumber Order's number
     */
    public Order(String name, int orderNumber, int orderType) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.orderType = orderType;

    }

    /**
     * Adds an item to the order.
     *
     * @param itemID   The item's ID
     * @param quantity How many items ordered
     */
    public void addItem(int itemID, int quantity) {
        this.itemID.add(itemID);
        this.quantity.add(quantity);
    }

    /**
     * Get item ID at index
     *
     * @param index Index of wanted item
     * @return itemID The item's iD
     */
    public int getItemID( int index ) { return itemID.get(index); }

    /**
     * Get the array list of item ID's
     * @return itemID List
     */
    public ArrayList<Integer> getItemID() {return itemID;}

    /**
     *  Sets the array of itemID's
     * @param x list of integers
     */
    public void setItemID(List<Integer> x){itemID = (ArrayList<Integer>) x;}

    /**
     * Sets the quantites of the items
     * @param x list of integers
     */
    public void setQuantities(List<Integer> x){quantity = (ArrayList<Integer>) x;}

    /**
     * Sets the times the order has been skipped
     * @param x
     */
    public void setSkipped(int x){skipped = x;}

    /**
     *  Sets the completion status
     * @param complete
     */
    public void setComplete(String complete) {
        if (complete.equals("false")) {
            this.complete = false;
        }
        else{this.complete=true;}
    }

    /**
     * Get the array list of item quantites
     * @return quantities
     */
    public ArrayList<Integer> getQuantities() {return quantity;}

    /**
     * Gets the order number
     * @return order number
     */
    public int getOrderNumber(){return orderNumber;}

    /**
     * Gets the amount of times Skipped
     * @return skipped
     */
    public int getIfSkipped(){ return skipped;}

    /**
     * Gets the name on the order
     * @return name
     */
    public String getName(){return name;}
    //commit

    /**
     * Gets the completion status
     * @return complete
     */
    public boolean getIsComplete() {return complete;}

    /**
     * Get the item's quantity at an index
     * @param index Item quantity's index
     * @return quantity
     */
    public int getItemQuantity(int index) {
        return quantity.get(index);
    }

    /**
     * Gets the item's order type
     * @return orderType
     */
    public int getOrderType(){return orderType;}

    /**
     * Sets the order as complete
     */
    public void completeOrder() { complete = true; }

}
