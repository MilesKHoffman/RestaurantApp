/**
 * OrderDataController.java
 *
 * Makes sure the next and current orders contain the appropriate object at all times
 * Deletes the order that was requested to be canceled from orderData
 *
 * @author William
 */
package Controller;
import Model.HashTableID;
import Model.Order;
import Model.OrderData;
import java.util.*;

public class OrderDataController {
    private OrderData orderData = new OrderData();
    private OrderController orderController;
    private CookController cookController;
    public OrderDataController(OrderController orderController){
        this.orderController = orderController;
    }
    public OrderDataController(CookController cookController){
        this.cookController = cookController;
    }

    /**
     * Sets currentOrder to the object in nextOrder, sets nextOrder to appropriate object from queue
     */
    public void setCurrentOrder(){
        if(!orderData.getNextOrder().equals("")) {                           //if next order is not empty
            orderData.setCurrentOrderObject(orderData.getNextOrderObject()); //sets current order Object
            orderData.setCurrentOrder(orderData.getNextOrder());             //sets current order string
        }
        setNextOrder();                                                      //calls set next order method
    }

    /**
     * Increments skip count of all orders still in a queue that were placed before the nextOrder Object
     */
    private void checkSkipped(){
        Queue<Order> temp = new LinkedList<>();
        if(!orderData.getOnSiteQueue().isEmpty()) { //if the onsite queue is not empty
            while(!orderData.getOnSiteQueue().isEmpty()){   //while onsite queue is not empty
                if(orderData.getOnSiteQueue().peek().getOrderNumber() < orderData.getNextOrderObject().getOrderNumber()){
                    orderData.getOnSiteQueue().peek().setSkipped(orderData.getOnSiteQueue().peek().getIfSkipped() + 1);
                }   //adds one to the skip count if orderNum is less than nextOrder objects orderNum
                temp.add(orderData.getOnSiteQueue().remove());
            }
            while(!temp.isEmpty()){         //reads all orders in temp back into queue
                orderData.setOnSiteQueue(temp.remove());
            }
        }
        if(!orderData.getPhoneQueue().isEmpty()) {
            while(!orderData.getPhoneQueue().isEmpty()){   //while onsite queue is not empty
                if(orderData.getPhoneQueue().peek().getOrderNumber() < orderData.getNextOrderObject().getOrderNumber()){
                    orderData.getPhoneQueue().peek().setSkipped(orderData.getPhoneQueue().peek().getIfSkipped() + 1);
                }   //adds one to the skip count if orderNum is less than nextOrder objects orderNum
                temp.add(orderData.getPhoneQueue().remove());
            }
            while(!temp.isEmpty()){         //reads all orders in temp back into queue
                orderData.setPhoneQueue(temp.remove());
            }
        }
        if(!orderData.getDoorDashQueue().isEmpty()) {
            while(!orderData.getDoorDashQueue().isEmpty()){   //while onsite queue is not empty
                if(orderData.getDoorDashQueue().peek().getOrderNumber() < orderData.getNextOrderObject().getOrderNumber()){
                    orderData.getDoorDashQueue().peek().setSkipped(orderData.getDoorDashQueue().peek().getIfSkipped() + 1);
                }   //adds one to the skip count if orderNum is less than nextOrder objects orderNum
                temp.add(orderData.getDoorDashQueue().remove());
            }
            while(!temp.isEmpty()){         //reads all orders in temp back into queue
                orderData.setDoorDashQueue(temp.remove());
            }
        }
    }

    /**
     * Returns the number corresponding to the queue holding the lowest numbered order with 3 skips at its head
     *
     * @return int
     */
    private int getMaxSkipped(){
        int num = 0;
        ArrayList<Integer> arr = new ArrayList<>();
        if(!orderData.getOnSiteQueue().isEmpty()) {
            if (orderData.getOnSiteQueue().peek().getIfSkipped() >= 3) {
                arr.add(orderData.getOnSiteQueue().peek().getOrderNumber());
            }
        }
        if(!orderData.getPhoneQueue().isEmpty()) {
            if (orderData.getPhoneQueue().peek().getIfSkipped() >= 3) {
                arr.add(orderData.getPhoneQueue().peek().getOrderNumber());
            }
        }
        if(!orderData.getDoorDashQueue().isEmpty()) {
            if (orderData.getDoorDashQueue().peek().getIfSkipped() >= 3) {
                arr.add(orderData.getDoorDashQueue().peek().getOrderNumber());
            }
        }
        if(!arr.isEmpty())
            return Collections.min(arr);
        return 0;
    }

    /**
     * Checks for any queue heads that have hit the maximum amount of skips and moves them to nextOrder
     *
     * @param input
     * @return String
     */
    private String checkMaxSkipped(String input){
        HashTableID hashTable = new HashTableID();
        ArrayList<Integer> itemID = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        int orderNum = getMaxSkipped();

        if(!orderData.getOnSiteQueue().isEmpty() && orderData.getOnSiteQueue().peek().getOrderNumber() == orderNum) {
            if (orderData.getOnSiteQueue().peek().getIfSkipped() >= 3) {//if onsite queue head has been skipped 3 times
                input += orderData.getOnSiteQueue().peek().getName() + "\n";
                itemID = orderData.getOnSiteQueue().peek().getItemID();
                quantities = orderData.getOnSiteQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) {  //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getOnSiteQueue().remove());//moves the OnSite queue head to next order
            }
            return input;
        }
        if(!orderData.getPhoneQueue().isEmpty() && orderData.getPhoneQueue().peek().getOrderNumber() == orderNum) {
            if (orderData.getPhoneQueue().peek().getIfSkipped() >= 3) { //if Phone queue head has been skipped 3 times
                input += orderData.getPhoneQueue().peek().getName() + "\n";
                itemID = orderData.getPhoneQueue().peek().getItemID();
                quantities = orderData.getPhoneQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) {     //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getPhoneQueue().remove()); //moves the Phone queue head to next order
            }
            return input;
        }
        if(!orderData.getDoorDashQueue().isEmpty() && orderData.getDoorDashQueue().peek().getOrderNumber() == orderNum) {
            if (orderData.getDoorDashQueue().peek().getIfSkipped() >= 3) { //if DoorDash queue head has been skipped 3 times
                input += orderData.getDoorDashQueue().peek().getName() + "\n";
                itemID = orderData.getDoorDashQueue().peek().getItemID();
                quantities = orderData.getDoorDashQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) {    //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getDoorDashQueue().remove());  //moves the DoorDash queue head to next order
            }
            return input;
        }
        return input;
    }

    /**
     * Sets nextOrder to the highest priority order available
     * Sets currentOrder if it is empty and nextOrder has an object
     */
    public void setNextOrder(){
        HashTableID hashTable = new HashTableID();
        String input = "";
        ArrayList<Integer> itemID = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        input = checkMaxSkipped(input);
        if(input.equals("")) {
            if (!orderData.getDriveThroughQueue().isEmpty()) {    //if the DT queue is not empty
                input += orderData.getDriveThroughQueue().peek().getName() + "\n";
                itemID = orderData.getDriveThroughQueue().peek().getItemID();
                quantities = orderData.getDriveThroughQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) { //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getDriveThroughQueue().remove());  //moves DriveThrough queue head to nextOrder
                checkSkipped();
            } else if (!orderData.getOnSiteQueue().isEmpty()) {     //if O queue is not empty
                input += orderData.getOnSiteQueue().peek().getName() + "\n";
                itemID = orderData.getOnSiteQueue().peek().getItemID();
                quantities = orderData.getOnSiteQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) {  //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getOnSiteQueue().remove());  //moves Onsite queue head to nextOrder
                checkSkipped();
            } else if (!orderData.getPhoneQueue().isEmpty()) {      //if P queue is not empty
                input += orderData.getPhoneQueue().peek().getName() + "\n";
                itemID = orderData.getPhoneQueue().peek().getItemID();
                quantities = orderData.getPhoneQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) { //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getPhoneQueue().remove());   //moves Phone queue head to nextOrder
                checkSkipped();
            } else if (!orderData.getDoorDashQueue().isEmpty()) {  //if DD queue is not empty
                input += orderData.getDoorDashQueue().peek().getName() + "\n";
                itemID = orderData.getDoorDashQueue().peek().getItemID();
                quantities = orderData.getDoorDashQueue().peek().getQuantities();
                for (int i = 0; i < itemID.size(); i++) { //stores all nessesary information about the order in input String
                    input += quantities.get(i) + " ";
                    input += hashTable.getItemIDName(itemID.get(i)) + "\n";
                }
                orderData.setNextOrderObject(orderData.getDoorDashQueue().remove());    //moves DoorDash queue head to nextOrder
                checkSkipped();
            }
        }
        orderData.setNextOrder(input); //assigns the input string to the nextOrderLabel
        if(orderData.getCurrentOrder().equals("") && !orderData.getNextOrder().equals("")){
            setCurrentOrder();  //if current order is empty but next order has an object, sets current order
        }
    }

    /**
     * Places nextOrder object back into respective queue if it is not the highest priority available
     * If this order skipped any others previously, subtracts 1 from the skipped orders skip count
     */
    private void replaceNextOrder(){
        Queue<Order> temp = new LinkedList<>();
        Queue<Order> arr = new LinkedList<>();

        //if the next order is an O Object and has been skipped less than 3 times
        if(orderData.getNextOrderObject().getOrderType() == 2 && orderData.getNextOrderObject().getIfSkipped() < 3){
            if(!orderData.getDriveThroughQueue().isEmpty()) {     //if the DT Queue is not empty
                temp.add(orderData.getNextOrderObject());
                orderData.setNextOrderObject(null);
                orderData.setNextOrder("");
                while(!orderData.getOnSiteQueue().isEmpty()){
                    temp.add(orderData.getOnSiteQueue().remove());
                }
                while(!temp.isEmpty()){
                    orderData.setOnSiteQueue(temp.remove());        //puts the next order back into O queue
                }
            }
            if(!orderData.getPhoneQueue().isEmpty()) { //if the Phone queue is not empty
                while(!orderData.getPhoneQueue().isEmpty()){   //while Phone queue is not empty
                    if(orderData.getPhoneQueue().peek().getOrderNumber() < orderData.getOnSiteQueue().peek().getOrderNumber()){
                        orderData.getPhoneQueue().peek().setSkipped(orderData.getPhoneQueue().peek().getIfSkipped() - 1);
                    }//subtracts 1 skip from every order in Phone queue with a smaller order number then the Onsite
                     //order that was placed back into its queue
                    arr.add(orderData.getPhoneQueue().remove());
                }while(!arr.isEmpty()){
                    orderData.setPhoneQueue(arr.remove());
                }
            }
            if(!orderData.getDoorDashQueue().isEmpty()) { //if the DoorDash queue is not empty
                while(!orderData.getDoorDashQueue().isEmpty()){   //while DoorDash queue is not empty
                    if(orderData.getDoorDashQueue().peek().getOrderNumber() < orderData.getOnSiteQueue().peek().getOrderNumber()){
                        orderData.getDoorDashQueue().peek().setSkipped(orderData.getDoorDashQueue().peek().getIfSkipped() - 1);
                    }//subtracts 1 skip from every order in DoorDash queue with a smaller order number then the
                     // Onsite order that was placed back into its queue
                    arr.add(orderData.getDoorDashQueue().remove());
                }while(!arr.isEmpty()){
                    orderData.setDoorDashQueue(arr.remove());
                }
            }
        }
        //if the next order is an P Object and has been skipped less than 3 times
        else if (orderData.getNextOrderObject().getOrderType() == 3 && orderData.getNextOrderObject().getIfSkipped() < 3){
            if(!orderData.getDriveThroughQueue().isEmpty() || !orderData.getOnSiteQueue().isEmpty()){          //if DT queue is not empty
                temp.add(orderData.getNextOrderObject());
                orderData.setNextOrderObject(null);
                orderData.setNextOrder("");
                while(!orderData.getPhoneQueue().isEmpty()){
                    temp.add(orderData.getPhoneQueue().remove());    //puts the next order back into P queue
                }while(!temp.isEmpty()){
                    orderData.setPhoneQueue(temp.remove());
                }
            }
            if(!orderData.getDoorDashQueue().isEmpty()) { //if the DD queue is not empty
                while(!orderData.getDoorDashQueue().isEmpty()){
                    if(orderData.getDoorDashQueue().peek().getOrderNumber() < orderData.getPhoneQueue().peek().getOrderNumber()){
                        orderData.getDoorDashQueue().peek().setSkipped(orderData.getDoorDashQueue().peek().getIfSkipped() - 1);
                    }//subtracts 1 skip from every order in DoorDash queue with a smaller order number then the
                    //Phone order that was placed back into its queue
                    arr.add(orderData.getDoorDashQueue().remove());
                }while(!arr.isEmpty()){         //reads all orders in temp back into queue
                    orderData.setDoorDashQueue(arr.remove());
                }
            }
        }
        else if (orderData.getNextOrderObject().getOrderType() == 4 && orderData.getNextOrderObject().getIfSkipped() < 3) {//if the next order is an DD Object and has been skipped less than 3 times
            if (!orderData.getDriveThroughQueue().isEmpty() || !orderData.getOnSiteQueue().isEmpty()
                    || !orderData.getPhoneQueue().isEmpty()) {  //if the DT,P, or O queues are not empty
                temp.add(orderData.getNextOrderObject());
                orderData.setNextOrderObject(null);
                orderData.setNextOrder("");
                while (!orderData.getDoorDashQueue().isEmpty()) {
                    temp.add(orderData.getDoorDashQueue().remove());    //puts the next order back into DD queue
                }while (!temp.isEmpty()) {
                    orderData.setDoorDashQueue(temp.remove());
                }
            }
        }
    }

    /**
     * Makes sure nextOrder and currentOrder have an object
     * Makes sure nextOrder holds the highest priority order object
     */
    public void checkQueue(){
        if(orderData.getNextOrder().equals("")){   //checks for a next order object, if none set the next order
            setNextOrder();
            return;
        }
        if(orderData.getCurrentOrder().equals("")){ //checks for a current order object, if none set the current order
            setCurrentOrder();
            return;
        }
        replaceNextOrder();
        if(orderData.getNextOrder().equals("")) {   //sets the next order if it is still empty
            setNextOrder();
        }
    }


    /**
     * Removes the requested order from OrderData unless its complete or in progress
     *
     * @param order
     */
    public void cancelOrder(Order order){
        if(orderData.getNextOrderObject().getOrderNumber() == order.getOrderNumber()){
            orderData.setNextOrder("");
            orderData.setNextOrderObject(null);
            setNextOrder();     //cancels the current order if requested
        }
        else if(orderData.getDriveThroughQueue().contains(order)){
            orderData.getDriveThroughQueue().remove(order); //removes order from DT queue if requested
        }
        else if(orderData.getOnSiteQueue().contains(order)){
            orderData.getOnSiteQueue().remove(order);//removes order from O queue if requested
        }
        else if(orderData.getPhoneQueue().contains(order)){
            orderData.getPhoneQueue().remove(order);//removes order from P queue if requested
        }
        else if(orderData.getDoorDashQueue().contains(order)){
            orderData.getDoorDashQueue().remove(order);//removes order from DD queue if requested
        }
    }

    /**
     * Returns true if all four queues are empty, false otherwise
     *
     * @return boolean
     */
    public boolean ifEmpty(){
        if(orderData.getDriveThroughQueue().isEmpty() && orderData.getOnSiteQueue().isEmpty()
                && orderData.getPhoneQueue().isEmpty() && orderData.getDoorDashQueue().isEmpty()) {
            return true;
        }
        return false;
    }
}

