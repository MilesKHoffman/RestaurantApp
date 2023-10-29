/**
 * CookController
 *
 * Handles all the users input in
 * cookView
 *
 * @Author Brayden Boyer
 */
package Controller;

import Exceptions.FinishOrderException;
import Exceptions.PickupOrderException;
import Model.HashTableID;
import Model.OrderData;
import View.CookView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CookController implements EventHandler<ActionEvent> {

    private OrderDataController orderDataController = new OrderDataController(this);
    private OrderData orderData = new OrderData();      //new instance of OrderData
    private CookView cookView;                          //instance of CookView
    private ToggleGroup pickupOrdersTG = new ToggleGroup(); //New Toggle Group

    private VBox tempVbox = new VBox();                 //New vBox
    private FileWriterController fileWriterController = new FileWriterController();

    private HashTableID hashTableID = new HashTableID();


    public CookController(CookView view) {
        this.cookView = view;                   //how controller communicates with the view
        resetVbox();                            //initializes vbox
        setOrders();                            //initializes setOrders
    }



    /**
     * resets the Vbox
     */
    private void resetVbox(){
        if (!cookView.getPickupOrders().isSelected()){
            cookView.getPane().getChildren().remove(tempVbox);      //removing vBox from the pane
            tempVbox.getChildren().clear();                         //resetting the vBox
        }
    }


    /**
     * sets the current and next order labels
     */
    private void setOrders(){
        cookView.setCurrentOrderLabel(orderData.getCurrentOrder());
        cookView.setNextOrderLabel(orderData.getNextOrder());
    }

    /**
     * gets the current order
     * @return String
     */
    public String getCurrentOrder(){
        return orderData.getCurrentOrder();
    }

    /**
     * gets the next order
     * @return String
     */
    public String getNextOrder(){
        return orderData.getNextOrder();
    }

    @Override
    public void handle(ActionEvent event) {


        if (cookView.getPickupOrders().isSelected()){

            VBox vBox = new VBox();                                //creating a new vBox
            cookView.getPane().getChildren().remove(tempVbox);    //in case user doesn't use back button or pickup order
            HBox hBox = new HBox();                                 //creating a new hBox
            for(int j=0;j< fileWriterController.getOrdersArrayListLength();j++){
                if(FileWriterController.fileOrderArrayList.get(j).getIsComplete()==true){
                    String temp = FileWriterController.fileOrderArrayList.get(j).getName();     //sets temp string
                    for(int x=0;x<FileWriterController.fileOrderArrayList.get(j).getItemID().size();x++) {
                       temp+="\n"+FileWriterController.fileOrderArrayList.get(j).getItemQuantity(x) +" "+  //adding to temp
                               //getting item name from hashtable using the ID
                                hashTableID.getItemIDName(FileWriterController.fileOrderArrayList.get(j).getItemID(x));
                    }
                    RadioButton po = new RadioButton(temp); //creating a new radio button for the temp
                    po.setToggleGroup(pickupOrdersTG);      //adding RB to toggle group
                    vBox.getChildren().add(po);             //adding them to the pickup orders vbox
                }
            }
            vBox.setLayoutX(225);                                       //setting the x-axis
            vBox.setLayoutY(100);                                       //setting the y-axis
            vBox.setSpacing(10);                                        //setting the spacing between each RB
            vBox.setPadding(new Insets(10));            //setting the padding(Intset)
            hBox.getChildren().addAll(cookView.getBackPickup(),cookView.getPickup());//adding to hBox
            vBox.getChildren().add(hBox);                              //adding the HBox to vBox
            vBox.visibleProperty().bind(cookView.getPickupOrders().selectedProperty());//sets when Vbox is Visible
            tempVbox = vBox;                                                //setting the vbox to a temporary Vbox
            cookView.setPane(tempVbox);                                    //sending it to the Pane in view to be shown
        }

        /**
         * Handler for the back button when List pickup orders is selected
         */
        cookView.getBackPickup().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cookView.getPickupOrders().setSelected(false);           ////unselecting the checkbox
                resetVbox();                                             //resetting everything in the CheckBox Handler

            }
        });

        /**
         * Handler for picking up a completed order
         */
        cookView.getPickup().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{     //exception handling for PickupOrder button
                    //checking to see if pickupOrder Arraylist is empty or if the user didn't select an order
                    if(pickupOrdersTG.getSelectedToggle() == null) {
                        cookView.showException("No order selected or No orders to be picked up");
                        throw new PickupOrderException();   //throwing the exception
                    }
                    RadioButton temp = (RadioButton) pickupOrdersTG.getSelectedToggle(); //setting the selected RP to a temp
                    String tempS = temp.getText();                            //getting the text(Order) from the selected RB
                    orderData.getPickupOrders().remove(tempS);//removing the order from Pick up orders
                    fileWriterController.removeOrder(tempS.substring(0,tempS.indexOf("\n")));//removes order from array list
                    cookView.getPickupOrders().setSelected(false);            //unselecting the checkbox
                    resetVbox();                                              //resetting everything in the CheckBox Handler
                    pickupOrdersTG.selectToggle(null);
                }
                catch(PickupOrderException e){}
            }
        });

        cookView.getToggleView().setOnMouseClicked(event1 -> {
            cookView.toggle();          //toggles the view
        });

        /**
         * Handler for the Finish Order Button
         */
        cookView.getFinishOrder().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    if(cookView.getCurrentOrder().getText().equals("")){       //checking if current order label is empty
                        cookView.showException( "There are no orders to finish" );      //setting pop up label
                        throw new FinishOrderException();                               //throwing exception
                    }
                    if (orderData.getCurrentOrderObject().getOrderType() == 1 || orderData.getCurrentOrderObject().getOrderType() == 2) {
                        fileWriterController.removeOrder(orderData.getCurrentOrder().substring(0,orderData.getCurrentOrder().indexOf("\n")));
                        //If order is not a pickup order, delete if from the array list
                    }
                    if(cookView.getNextOrderLabel().getText().equals("") && !orderDataController.ifEmpty()){
                        orderDataController.setNextOrder();     //setting the next order
                    }
                    else if(cookView.getNextOrderLabel().getText().equals("") && orderDataController.ifEmpty()){
                        orderData.getOrderList().remove(orderData.getNextOrderObject());
                        if(orderData.getCurrentOrderObject().getOrderType() == 3 || orderData.getCurrentOrderObject().getOrderType() == 4){
                            orderData.setPickupOrders(orderData.getCurrentOrder());//Adds the Finished Order to Pickup
                            orderData.getCurrentOrderObject().setComplete("true");
                        }
                        cookView.setCurrentOrderLabel("");            //Sets the Label to the new Order
                        orderData.setCurrentOrder("");
                        cookView.getPane().getChildren().removeAll(cookView.getCurrentOrder(), cookView.getNextOrderLabel());//removes the old Label from Pane
                        cookView.getPane().getChildren().addAll(cookView.getCurrentOrder(), cookView.getNextOrderLabel());   //Adds new Label to the pane
                    }
                    else {
                        if(orderData.getCurrentOrderObject().getOrderType() == 3 || orderData.getCurrentOrderObject().getOrderType() == 4){
                            orderData.setPickupOrders(orderData.getCurrentOrder());//Adds the Finished Order to Pickup
                        }
                        orderData.getOrderList().remove(orderData.getNextOrderObject());
                        orderData.getCurrentOrderObject().setComplete("true");
                        orderDataController.setCurrentOrder();       //sets the current and next order V
                        cookView.setCurrentOrderLabel(orderData.getCurrentOrder());            //Sets the Label to the new Order
                        cookView.setNextOrderLabel(orderData.getNextOrder());               //Sets the Label for the next order
                        cookView.getPane().getChildren().removeAll(cookView.getCurrentOrder(), cookView.getNextOrderLabel());//removes the old Label from Pane
                        cookView.getPane().getChildren().addAll(cookView.getCurrentOrder(), cookView.getNextOrderLabel());   //Adds new Label to the pane
                    }
                }
                catch (FinishOrderException e){}                //catching exception
            }

        });

    }//end of handler
}//end of Cook Controller
