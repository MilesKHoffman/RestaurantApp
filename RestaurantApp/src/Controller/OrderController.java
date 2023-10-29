/**
 * OrderController.java
 *
 * This is the main button handler for CustomerView. It has access to the data in the models,
 * and sends it to CustomerView to display. It is able to distribute tasks to other classes
 * like creating orders, cancelling orders, and handling exceptions.
 *
 * @author Miles Hoffman
 */

package Controller;
import Exceptions.*;
import Model.*;
import View.CustomerView;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.ArrayList;

public class OrderController implements EventHandler<ActionEvent> {
    private CustomerView cv;
    private OrderData orderData = new OrderData();
    private String customerName;
    private int orderType;
    private int orderNumber;
    private OrderDataController orderDataController = new OrderDataController(this);
    private HashTableID hashTableID = new HashTableID();
    private FileWriterController fileWriterController = new FileWriterController();


    /**
     * Constructor that connects to CustomerView
     * @param cv
     */
    public OrderController( CustomerView cv ){ this.cv = cv; }

    /**
     * Sets the visibility of menu to false.
     */
    private void clearMenu(){
        cv.getMealGroup().setVisible(false);
        cv.getDrinkGroup().setVisible(false);
        cv.getSideGroup().setVisible(false);
        cv.getIcedGroup().setVisible(false);
        cv.getHotDrinkGroup().setVisible(false);
        cv.getColdMealGroup().setVisible(false);
        cv.getHotMealGroup().setVisible(false);
    }

    /**
     * Sets phone inquiries to false.
     */
    private void clearPhone(){
        cv.getGetPhoneNumber().setVisible(false);
        cv.getEnterPhone().setVisible(false);
    }

    /**
     * Checks if the inputted String is an accepted phone number or not
     * @param num
     * @return boolean
     */
    private boolean isNumber(String num){
        if(num.equals(""))
            return false;
        for(int i=0; i < num.length(); i++){
            if(num.charAt(i) < 48 || num.charAt(i) > 57){
                return false;
            }
        }
        return true;
    }


    /**
     * Takes in an order number(int) and returns its corresponding order object(Object)
     *
     * @param orderNumber
     * @return order
     */
    private Order orderNumToOrder(int orderNumber){
        for(int i = 0; i < orderData.getOrderList().size(); i++){
            if(orderData.getOrderList().get(i).getOrderNumber() == orderNumber)
                return orderData.getOrderList().get(i);
        }
        return null;
    }

    /**
     * Handles the buttons in CustomerView
     * @param event the event which occurred
     */
    @Override
    public void handle(ActionEvent event) {

        /**
         * Handles the cancel button. When it is pressed, the order is cancelled
         * unless it is currently being cooked.
         */
        cv.getCancel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    if(!isNumber(cv.getCanceledOrder())){   //if request is not a number
                        showException("Order nonexistent");
                        throw new CancelException();
                    }
                    else if(Integer.parseInt(cv.getCanceledOrder()) == orderData.getCurrentOrderObject().getOrderNumber()){
                        showException("Order in progress");
                        throw new CancelException();
                    }
                    else if(orderNumToOrder(Integer.parseInt(cv.getCanceledOrder())) == null){
                        showException("Order nonexistent");
                        throw new CancelException();
                    }

                    orderDataController.cancelOrder(orderNumToOrder(Integer.parseInt(cv.getCanceledOrder())));
                    fileWriterController.removeOrder(orderNumToOrder(Integer.parseInt(cv.getCanceledOrder())).getName());
                    //Removes cancelled order from the array list
                    orderData.getOrderList().remove(orderNumToOrder(Integer.parseInt(cv.getCanceledOrder())));
                }
                catch(CancelException e){}
                cv.setCancelField("");
            }
        });

        /**
         * Handles the submit button. When submit is hit, a new order is created
         * and added to the correct queue in OrderData.
         */
        cv.getSubmit().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try{
                    if(cv.getNameButton().getText().equals("")){
                        showException("Enter a name before submitting your order");
                        throw new CustomerNameException();
                    }
                    if(getType() == 3 && !isNumber(cv.getGetPhoneNumber().getText())){
                        showException("Enter a valid phone number (No Characters)");
                        throw new PhoneNumberException();
                    }
                    else if(getType()==3 && cv.getGetPhoneNumber().getText().length() > 10){
                        showException("Enter a valid phone number (<11 Digits)");
                        throw new PhoneNumberException();
                    }
                    if(cv.getItemID().isEmpty()){
                        showException("Cannot submit empty orders");
                        throw new SubmitOrderException();
                    }

                    customerName = cv.getNameButton().getText();
                    orderType = getType( );
                    orderNumber = Integer.parseInt( cv.getOrderNumber().getText() );
                    String phone = cv.getGetPhoneNumber().getText();
                    Order order;
                    cv.addOrderNumber();

                    //  Sets the order according to the order number type
                    if( orderType == 4 ) order = new DoorDash( customerName, orderNumber);
                    else if( orderType == 1 ) order = new DriveThrough( customerName, orderNumber);
                    else if( orderType == 2 ) order = new Onsite( customerName, orderNumber);
                    else order = new Phone( customerName, orderNumber, phone);

                    String items = "";
                    for( int i = 0; i < cv.getItemID().size(); i++ ){
                        order.addItem( cv.getItemID().get(i), cv.getItemQuantity().get(i) );
                        items +=  "\n"+ cv.getItemID().get(i) + " " + cv.getItemQuantity().get(i);
                    }

                    //  Adds 5% charge to door dash
                    if( orderType == 4 ) {

                        String price = String.format("%.2f", 1.05 * getPrice(cv.getItemID(), cv.getItemQuantity()));

                        cv.getPriceLabel().setText( "$" + price );
                    }
                    else{
                        cv.getPriceLabel().setText( "$" + String.format("%.2f", getPrice(cv.getItemID(),
                                cv.getItemQuantity())));
                    }

                    //  Clears the info in customerView
                    cv.getItemID().clear();
                    cv.getItemQuantity().clear();
                    orderData.addOrder( order );
                    FileWriterController.fileOrderArrayList.add(order);
                    //adds order to the array list
                    orderData.getOrderList().add(order);
                    orderDataController.checkQueue();

                    cv.getDoneButton().setSelected(true);
                    clearMenu();

                    cv.getQuantitySlider().setValue(1.0);
                }
                catch(CustomerNameException e){}
                catch (PhoneNumberException e){}
                catch(SubmitOrderException e){}
            }
        });

        /**
         * The following determines what menu layer is visible.
         */
        cv.getDoneButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { clearMenu(); }
        });
        cv.getDrinkButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getDrinkGroup().setVisible(true);
            }});
        cv.getMealButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getMealGroup().setVisible(true);
            }});
        cv.getSidesButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getSideGroup().setVisible(true);
            }});
        cv.getColdDrink().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getIcedGroup().setVisible(true);
                cv.getDrinkGroup().setVisible(true);
            }});
        cv.getHotDrink().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getHotDrinkGroup().setVisible(true);
                cv.getDrinkGroup().setVisible(true);
            }});
        cv.getHotMeal().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getHotMealGroup().setVisible(true);
                cv.getMealGroup().setVisible(true);
            }});
        cv.getColdMeal().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearMenu();
                cv.getColdMealGroup().setVisible(true);
                cv.getMealGroup().setVisible(true);
            }});

        /**
         * This adds the correct item ID and quantity to the arraylist
         */
        cv.getAddToOrder().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    if(getItemID() == 0){
                        showException("Invalid selection: Cannot add to order");
                        throw new AddToOrderException();
                    }
                    cv.addItemQuantity( (int) cv.getQuantitySlider().getValue() );
                    cv.addItemID( getItemID() );

                    cv.getDoneButton().setSelected(true);
                    clearMenu();
                    cv.getQuantitySlider().setValue(1.0);
                }
                catch(AddToOrderException e){}
            }
        });

        /**
         * Handles the toggle button. Switches the scene between CustomerView or CookView
         */
        cv.getToggleView().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cv.toggle();

            }
        });

        /**
         * Handles the phone radio button. When it is selected, a phone text field will show up.
         * The opposite is true as well.
         */
        cv.getPhoneRadio().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                cv.getGetPhoneNumber().setVisible(true);
                cv.getEnterPhone().setVisible(true);
            }});
        cv.getOnsiteRadio().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { clearPhone(); }});
        cv.getDoorDRadio().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { clearPhone(); }});
        cv.getDriveRadio().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { clearPhone(); }});
    }

    /**
     * Finds and returns the selected item's ID
     * @return  item ID number
     */
    private int getItemID(){
        int id = 0;

        if( cv.getIcedTea().isSelected() && cv.getColdDrink().isSelected()
                && cv.getDrinkButton().isSelected() ){
            id = 1;
        }
        else if( cv.getWater().isSelected() &&  cv.getColdDrink().isSelected()
                && cv.getDrinkButton().isSelected()){
            id = 2;
        }
        else if( cv.getHotCoffee().isSelected() && cv.getHotDrink().isSelected()
                && cv.getDrinkButton().isSelected() ){
            id = 3;
        }
        else if( cv.getHotChoco().isSelected() && cv.getHotDrink().isSelected()
                && cv.getDrinkButton().isSelected() ){
            id = 4;
        }
        else if( cv.getIcedNoodles().isSelected() && cv.getColdMeal().isSelected()
                && cv.getMealButton().isSelected() ){
            id = 5;
        }
        else if( cv.getWatermelonSalad().isSelected() && cv.getColdMeal().isSelected()
                && cv.getMealButton().isSelected() ){
            id = 6;
        }
        else if( cv.getPineapplePizza().isSelected() && cv.getHotMeal().isSelected()
                && cv.getMealButton().isSelected() ){
            id = 7;
        }
        else if( cv.getCheeseBurger().isSelected() && cv.getHotMeal().isSelected()
                && cv.getMealButton().isSelected() ){
            id = 8;
        }
        else if( cv.getFries().isSelected() && cv.getSidesButton().isSelected() ){
            id = 9;
        }
        else if( cv.getFriedOreos().isSelected() && cv.getSidesButton().isSelected() ){
            id = 10;
        }
        else if( cv.getPickles().isSelected() && cv.getSidesButton().isSelected() ){
            id = 11;
        }

        return id;
    }

    /**
     * Gets the max order number
     *
     * @return  The max order number plus 1
     */
    public int findOrderNumber(){

        ArrayList<Order> list = FileWriterController.fileOrderArrayList;
        int max = 1;

        for( int i = 0; i < list.size(); i++ ){

            if( max <= list.get(i).getOrderNumber() ){
                max = list.get(i).getOrderNumber() + 1;
            }
        }

        return max;
    }

    /**
     * Gets the order type and sets it equal to a number
     *
     * @return  An integer of the order type.
     */
    private int getType(){
        int type = 0;

        if( cv.getDriveRadio().isSelected() ) type = 1;
        else if( cv.getOnsiteRadio().isSelected() ) type = 2;
        else if( cv.getPhoneRadio().isSelected() ) type = 3;
        else if( cv.getDoorDRadio().isSelected() ) type = 4;

        return type;
    }

    /**
     * Gets the price of the order
     *
     * @param item  Arraylist of the items in the order
     * @param quantity  Arraylist of the quantity of items in the order
     * @return  The total price of the order
     */
    private double getPrice( ArrayList<Integer> item, ArrayList<Integer> quantity ){

        double price = 0;

        for( int i = 0; i < item.size(); i++ ){

            int ID = item.get(i);
            price += quantity.get(i) * hashTableID.getItemPrice(ID);
        }

        return price;
    }

    /**
     * Sets the non-pickup orders
     */
    public void setOrders() {
        for (int i=0; i<FileWriterController.fileOrderArrayList.size();i++){
            if(FileWriterController.fileOrderArrayList.get(i).getIsComplete()==false) {
                orderData.addOrder(FileWriterController.fileOrderArrayList.get(i));
                //Sets non-pickup orders
            }
        }
        orderDataController.setCurrentOrder();
    }

    /**
     * Shows the exception label for 10 seconds
     * @param text contains the error text
     */
    private void showException( String text ){

        cv.setExceptionLabel( text );
        cv.setVisibleException(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished( event -> cv.setVisibleException(false) );
        pause.playFromStart();
    }
}
