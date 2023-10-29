/**
 * CustomerView.java
 *
 * Main application for the program. It holds all the UI elements in the customer's view.
 * It also outputs the information given to it by the controllers.
 *
 * @author Miles Hoffman
 */

package View;

import Controller.FileWriterController;
import Controller.OrderController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.ArrayList;


public class CustomerView extends Application {
    private boolean toggle = true;
    private Scene firstScene;
    private Stage stage = new Stage();
    private Label customerLabel = new Label("Bou-shei");
    private Label orderLabel = new Label("Order # ");
    private Label orderNumberLabel = new Label(""+1);
    private Label promptLabel = new Label("Enter Order Type: \n\n\n\n\nEnter Name:");
    private TextField cancelField = new TextField("");
    private Button toggleView = new Button("Switch to Cook View");
    private Button submitButton = new Button("Submit Order");
    private Button cancelButton = new Button(" ");
    private TextField nameField = new TextField();
    private Label priceLabel = new Label("");
    private Label menuLabel = new Label("Menu:");
    private CookView cookView;
    private Group group, drinkGroup, mealGroup, sideGroup, icedGroup,
            hotDrinkGroup, coldMealGroup, hotMealGroup;
    private RadioButton hotDrink, coldDrink, hotMeal, coldMeal, fries,
            pickles, friedOreos, drinks, meals, sides, done, icedTea, water,
            hotCoffee, hotChoco, cheeseBurger, pineapplePizza, icedNoodles, watermelonSalad;
    private ToggleGroup menuToggleG1, drinkToggleG, mealToggleG, sidesToggleG,
            icedToggleG, hotDrinkToggleG, hotMealToggle, coldMealToggle, orderTypeToggle;

    private Slider quantitySlider = new Slider();
    private Button addToOrder = new Button("Add selected item(s) to order");
    private ArrayList<Integer> itemID = new ArrayList<>();
    private ArrayList<Integer> itemQuantity = new ArrayList<>();
    private RadioButton doorDRadio, driveRadio, onsiteRadio, phoneRadio;
    private Label enterPhone = new Label("Enter Number: ");
    private Label exceptionLabel = new Label("");
    private TextField getPhoneNumber = new TextField();
    private Rectangle xCenterRec, xOutlineRec, xShadowRec, xTopShadow,
            xTopLeftShadow, xTopRightShadow;
    private Group rectGroup;

    //  Connects CustomerView to OrderController
    private OrderController orderController = new OrderController(this);

    //  Allows for reading the preview data
    private FileWriterController fileWriterController = new FileWriterController();

    public CustomerView(){
        cookView = new CookView(this);
    }

    /**
     * Starts the program
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        fileWriterController.readInFile();
        orderController.setOrders();
        setMenu();
        setPositions();
        setActions();
        setSlider();
        setBackGround();
        setStyle();

        group = addNodes();
        setScene();

        stage.setResizable(false);
        toggle();
        stage.setTitle("CustomerView");
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                FileWriterController.closeFileAction(fileWriterController);
            }
        });
    }

    /**
     * Creates the background UI elements
     */
    private void setBackGround(){

        // #c6c6c6 is light grey
        // #6987ff is blue
        // #b6d8ff is silver
        // #3b3b3b is shadow

        Rectangle centerRec = new Rectangle(370,400, Paint.valueOf("#b6d8ff"));
        centerRec.setArcHeight(30);
        centerRec.setArcWidth(30);
        centerRec.setX(130);
        centerRec.setY(100);
        centerRec.toBack();

        Rectangle outlineRec = new Rectangle(380, 410, Paint.valueOf("#6987ff"));
        outlineRec.setArcWidth(30);
        outlineRec.setArcHeight(30);
        outlineRec.setX(125);
        outlineRec.setY(95);

        Rectangle shadowRec = new Rectangle(380, 410, Paint.valueOf("#3b3b3b"));
        shadowRec.setArcWidth(30);
        shadowRec.setArcHeight(30);
        shadowRec.setX(135);
        shadowRec.setY(105);

        // fec1ff is light pink
        // ff616f is light red

        xCenterRec = new Rectangle(300, 60, Paint.valueOf("fec1ff") );
        xCenterRec.setArcWidth(30);
        xCenterRec.setArcHeight(30);
        xCenterRec.setX(150);
        xCenterRec.setY(495);

        xOutlineRec = new Rectangle( 305, 65, Paint.valueOf("ff616f"));
        xOutlineRec.setArcHeight(30);
        xOutlineRec.setArcWidth(30);
        xOutlineRec.setX(147.5);
        xOutlineRec.setY(492.5);

        xShadowRec = new Rectangle( 305, 65, Paint.valueOf("3b3b3b"));
        xShadowRec.setArcHeight(30);
        xShadowRec.setArcWidth(30);
        xShadowRec.setX(152.5);
        xShadowRec.setY(497.5);

        xTopShadow = new Rectangle( 305, 5, Paint.valueOf("88008a"));
        xTopShadow.setX(147.5);
        xTopShadow.setY(505);

        xTopLeftShadow = new Rectangle( 2.5, 5, Paint.valueOf("4e0050"));
        xTopLeftShadow.setX(147.5);
        xTopLeftShadow.setY(505);

        xTopRightShadow = new Rectangle(2.5, 5, Paint.valueOf("4e0050"));
        xTopRightShadow.setX(450);
        xTopRightShadow.setY(505);

        xCenterRec.setVisible(false);
        xOutlineRec.setVisible(false);
        xShadowRec.setVisible(false);
        xTopShadow.setVisible(false);
        xTopRightShadow.setVisible(false);
        xTopLeftShadow.setVisible(false);

        rectGroup = new Group();
        rectGroup.getChildren().addAll( shadowRec, xShadowRec, xOutlineRec, xCenterRec, xTopShadow,
                xTopLeftShadow, xTopRightShadow, outlineRec, centerRec);
    }

    /**
     * Sets the font style and other UI styles
     */
    private void setStyle(){

        customerLabel.setFont(new Font("Arial",40));
        customerLabel.setStyle("-fx-border-width: 2;");
        customerLabel.setStyle("-fx-border-color: black;");

        orderNumberLabel.setText("" + orderController.findOrderNumber() );

        cancelField.setMinWidth(50);

        orderLabel.setFont(new Font("Arial", 20));
        orderNumberLabel.setFont(new Font("Arial", 20));

        getPhoneNumber.setVisible(false);
        enterPhone.setVisible(false);
    }

    /**
     * Sets the positions of buttons or labels
     */
    private void setPositions(){

        exceptionLabel.setLayoutX(175);
        exceptionLabel.setLayoutY(520);
        customerLabel.setLayoutY(30);
        customerLabel.setLayoutX(230);
        orderLabel.setLayoutX(400);
        orderLabel.setLayoutY(100);
        orderNumberLabel.setLayoutX(470);
        orderNumberLabel.setLayoutY(100);
        promptLabel.setLayoutY(110);
        promptLabel.setLayoutX(150);
        cancelButton.setLayoutY(450);
        cancelButton.setLayoutX(150);
        priceLabel.setLayoutX(150);
        priceLabel.setLayoutX(280);
        priceLabel.setLayoutY(415);
        cancelField.setLayoutX(250);
        cancelField.setLayoutY(450);
        cancelField.setMaxWidth(20);
        nameField.setLayoutX(250);
        nameField.setLayoutY(190);
        toggleView.setLayoutY(575);
        submitButton.setLayoutX(150);
        submitButton.setLayoutY(410);
        addToOrder.setLayoutY(360);
        addToOrder.setLayoutX(310);
        quantitySlider.setLayoutY(365);
        quantitySlider.setLayoutX(150);
        enterPhone.setLayoutY(220);
        enterPhone.setLayoutX(150);
        getPhoneNumber.setLayoutY(220);
        getPhoneNumber.setLayoutX(250);

        menuLabel.setLayoutY(260);
        menuLabel.setLayoutX(150);
        drinks.setLayoutY(280);
        drinks.setLayoutX(150);
        meals.setLayoutY(300);
        meals.setLayoutX(150);
        sides.setLayoutY(320);
        sides.setLayoutX(150);
        done.setLayoutY(340);
        done.setLayoutX(150);

        coldDrink.setLayoutY(280);
        hotDrink.setLayoutY(300);
        coldDrink.setLayoutX(250);
        hotDrink.setLayoutX(250);

        coldMeal.setLayoutY(280);
        hotMeal.setLayoutY(300);
        coldMeal.setLayoutX(250);
        hotMeal.setLayoutX(250);

        fries.setLayoutY(280);
        friedOreos.setLayoutY(300);
        pickles.setLayoutY(320);
        fries.setLayoutX(250);
        friedOreos.setLayoutX(250);
        pickles.setLayoutX(250);

        icedTea.setLayoutY(280);
        water.setLayoutY(300);
        icedTea.setLayoutX(350);
        water.setLayoutX(350);
        hotCoffee.setLayoutY(280);
        hotChoco.setLayoutY(300);
        hotCoffee.setLayoutX(350);
        hotChoco.setLayoutX(350);

        pineapplePizza.setLayoutY(280);
        cheeseBurger.setLayoutY(300);
        icedNoodles.setLayoutY(280);
        watermelonSalad.setLayoutY(300);
        pineapplePizza.setLayoutX(350);
        cheeseBurger.setLayoutX(350);
        watermelonSalad.setLayoutX(350);
        icedNoodles.setLayoutX(350);

        onsiteRadio.setLayoutY(110);
        onsiteRadio.setLayoutX(250);
        phoneRadio.setLayoutY(130);
        phoneRadio.setLayoutX(250);
        doorDRadio.setLayoutY(150);
        doorDRadio.setLayoutX(250);
        driveRadio.setLayoutX(250);
        driveRadio.setLayoutY(170);
    }

    /**
     * Adds all the nodes to a group.
     *
     * @return  The group containing the nodes.
     */
    private Group addNodes(){
        Group g = new Group();
        g.getChildren().addAll( rectGroup, customerLabel, orderLabel,
                promptLabel, cancelField, toggleView, submitButton, nameField, priceLabel,
                menuLabel, orderNumberLabel, meals, drinks, sides, done, drinkGroup, mealGroup,
                sideGroup, icedGroup, hotDrinkGroup, hotMealGroup, coldMealGroup, addToOrder,
                quantitySlider, doorDRadio, onsiteRadio, phoneRadio, driveRadio, cancelButton,
                enterPhone, getPhoneNumber, exceptionLabel);

        return g;
    }

    /**
     * Sets the menu radio buttons.
     */
    private void setMenu(){
        //  Creates toggle groups
        menuToggleG1 = new ToggleGroup( );
        drinkToggleG = new ToggleGroup( );
        mealToggleG = new ToggleGroup();
        sidesToggleG = new ToggleGroup();
        icedToggleG = new ToggleGroup();
        hotDrinkToggleG = new ToggleGroup();
        coldMealToggle = new ToggleGroup();
        hotMealToggle = new ToggleGroup();
        orderTypeToggle = new ToggleGroup();


        // First layer of menu
        drinks = new RadioButton("Drinks");
        meals = new RadioButton("Meals");
        sides = new RadioButton("Sides");
        done = new RadioButton("Collapse");

        drinks.setToggleGroup(menuToggleG1);
        meals.setToggleGroup(menuToggleG1);
        sides.setToggleGroup(menuToggleG1);
        done.setToggleGroup(menuToggleG1);
        menuToggleG1.selectToggle(done);

        //  Second layer of menu
        hotDrink = new RadioButton("Hot Drinks");
        coldDrink = new RadioButton("Cold Drinks");
        hotMeal = new RadioButton("Hot Meal");
        coldMeal = new RadioButton("Cold Meal");
        fries = new RadioButton("Fries");
        friedOreos = new RadioButton("Fried Oreos");
        pickles = new RadioButton("Pickles");

        hotDrink.setToggleGroup(drinkToggleG);
        coldDrink.setToggleGroup(drinkToggleG);
        hotMeal.setToggleGroup(mealToggleG);
        coldMeal.setToggleGroup(mealToggleG);
        fries.setToggleGroup(sidesToggleG);
        pickles.setToggleGroup(sidesToggleG);
        friedOreos.setToggleGroup(sidesToggleG);

        //  The third layer of menu
        icedTea = new RadioButton("Iced Tea");
        water = new RadioButton("Water");
        hotCoffee = new RadioButton("Hot Coffee");
        hotChoco = new RadioButton("Hot Chocolate");

        icedTea.setToggleGroup(icedToggleG);
        water.setToggleGroup(icedToggleG);
        hotCoffee.setToggleGroup(hotDrinkToggleG);
        hotChoco.setToggleGroup(hotDrinkToggleG);

        pineapplePizza = new RadioButton("Pineapple Pizza");
        cheeseBurger = new RadioButton("Cheeseburger");
        watermelonSalad = new RadioButton("Watermelon Salad");
        icedNoodles = new RadioButton("Iced Noodles");

        pineapplePizza.setToggleGroup(hotMealToggle);
        cheeseBurger.setToggleGroup(hotMealToggle);
        watermelonSalad.setToggleGroup(coldMealToggle);
        icedNoodles.setToggleGroup(coldMealToggle);

        //  Handles the order type radio buttons.
        onsiteRadio = new RadioButton("Onsite");
        phoneRadio = new RadioButton("Phone");
        driveRadio = new RadioButton("Drivethrough");
        doorDRadio = new RadioButton("DoorDash");

        onsiteRadio.setToggleGroup(orderTypeToggle);
        phoneRadio.setToggleGroup(orderTypeToggle);
        doorDRadio.setToggleGroup(orderTypeToggle);
        driveRadio.setToggleGroup(orderTypeToggle);

        orderTypeToggle.selectToggle(onsiteRadio);

        //  Handles the groups
        drinkGroup = new Group(hotDrink, coldDrink);
        mealGroup = new Group( hotMeal, coldMeal );
        sideGroup = new Group( fries, friedOreos, pickles);
        icedGroup = new Group( icedTea, water );
        hotDrinkGroup = new Group( hotChoco, hotCoffee );
        hotMealGroup = new Group( pineapplePizza, cheeseBurger );
        coldMealGroup = new Group( icedNoodles, watermelonSalad );

        sideGroup.setVisible(false);
        drinkGroup.setVisible(false);
        mealGroup.setVisible(false);
        icedGroup.setVisible(false);
        hotDrinkGroup.setVisible(false);
        coldMealGroup.setVisible(false);
        hotMealGroup.setVisible(false);
    }


    /**
     * Sets the action for the buttons
     */
    private void setActions() {

        cancelButton.setText( "Cancel Order #" );

        cancelButton.setOnAction(orderController);
        submitButton.setOnAction(orderController);
        nameField.setOnAction(orderController);
        hotDrink.setOnAction(orderController);
        coldDrink.setOnAction(orderController);
        hotMeal.setOnAction(orderController);
        coldDrink.setOnAction(orderController);
        done.setOnAction(orderController);
        drinks.setOnAction(orderController);
        meals.setOnAction(orderController);
        sides.setOnAction(orderController);
        addToOrder.setOnAction(orderController);
        toggleView.setOnAction(orderController);
        getPhoneNumber.setOnAction(orderController);
        phoneRadio.setOnAction(orderController);
    }

    /**
     * Sets the quantitySlider
     */
    private void setSlider(){
        quantitySlider.setMax(5);
        quantitySlider.setMin(1);
        quantitySlider.setMajorTickUnit(1);
        quantitySlider.setShowTickLabels(true);
        quantitySlider.setShowTickMarks(true);
        quantitySlider.setSnapToTicks(true);
        quantitySlider.setMinorTickCount(0);
        quantitySlider.setValue(1);
    }

    /**
     * @return  the cancel Button
     */
    public Button getCancel(){return cancelButton;}

    /**
     * @return  Submit button
     */
    public Button getSubmit(){ return submitButton; }

    /**
     * @return  nameField
     */
    public TextField getNameButton(){ return nameField; }

    /**
     * @return  the order number
     */
    public Label getOrderNumber(){ return orderNumberLabel; }

    /**
     * Increments the order number
     */
    public void addOrderNumber(){
        int num = Integer.parseInt( orderNumberLabel.getText() ) + 1;
        orderNumberLabel.setText( String.valueOf( num ) );
    }

    /**
     * @return  The first menu toggle group
     */
    public RadioButton getDrinkButton(){ return drinks; }

    /**
     * @return  the coldDrink button
     */
    public RadioButton getColdDrink(){ return coldDrink; }

    /**
     * @return  the icedGroup
     */
    public Group getIcedGroup(){ return icedGroup; }

    /**
     * @return  Gets the done button
     */
    public RadioButton getDoneButton(){ return done; }

    /**
     * @return  the meal radio button
     */
    public RadioButton getMealButton(){ return meals; }

    /**
     * @return  get sides button
     */
    public RadioButton getSidesButton(){ return sides; }

    /**
     * @return  the drinkGroup Group
     */
    public Group getDrinkGroup(){ return drinkGroup; }

    /**
     * @return  the mealGroup group
     */
    public Group getMealGroup(){ return mealGroup; }

    /**
     * @return  the sideGroup group
     */
    public Group getSideGroup(){ return sideGroup; }

    /**
     * @return  the hot drink radio button
     */
    public RadioButton getHotDrink(){ return hotDrink; }

    /**
     * @return  the HotDrinkGroup
     */
    public Group getHotDrinkGroup(){ return hotDrinkGroup; }

    /**
     * @return  the hotMeal button
     */
    public RadioButton getHotMeal(){ return hotMeal; }

    /**
     * @return  the hotMealGroup group
     */
    public Group getHotMealGroup(){ return hotMealGroup; }

    /**
     * @return  the coldMeal button
     */
    public RadioButton getColdMeal(){ return coldMeal; }

    /**
     * @return the coldMealGroup
     */
    public Group getColdMealGroup(){ return coldMealGroup; }

    /**
     * @return  addToOrder
     */
    public Button getAddToOrder() { return addToOrder; }

    /**
     * @return  gets the quantitySlider
     */
    public Slider getQuantitySlider(){ return quantitySlider; }

    /**
     * Adds to itemID
     * @param ID  Holds the int for itemID
     */
    public void addItemID(int ID) { this.itemID.add( ID ); }

    /**
     * @return  itemID array list
     */
    public ArrayList<Integer> getItemID() { return itemID; }

    /**
     * Adds to itemQuantity
     * @param quantity  Holds int
     */
    public void addItemQuantity( int quantity ) { this.itemQuantity.add( quantity ); }

    /**
     * @return  itemQuantity array list
     */
    public ArrayList<Integer> getItemQuantity() { return itemQuantity; }

    /**
     * @return  icedTea RadioButton
     */
    public RadioButton getIcedTea() { return icedTea; }

    /**
     * @return  priceLabel
     */
    public Label getPriceLabel() { return priceLabel; }

    /**
     * @return  water RadioButton
     */
    public RadioButton getWater() { return water; }

    /**
     * @return  icedNoodles Radiobutton
     */
    public RadioButton getIcedNoodles() { return icedNoodles; }

    /**
     * @return  hotCoffee radiobutton
     */
    public RadioButton getHotCoffee() { return hotCoffee; }

    /**
     * @return  hotChoco
     */
    public RadioButton getHotChoco() { return hotChoco; }

    /**
     * @return  watermelonSalad
     */
    public RadioButton getWatermelonSalad() { return watermelonSalad; }
    public void setCancelField(String text){
        cancelField.setText(text);
    }

    /**
     * @return  pineapplePizza
     */
    public RadioButton getPineapplePizza() { return pineapplePizza; }

    /**
     * @return  cheeseBurger
     */
    public RadioButton getCheeseBurger() { return cheeseBurger; }

    /**
     * @return  fries
     */
    public RadioButton getFries() { return fries; }

    /**
     * @return  friedOreos
     */
    public RadioButton getFriedOreos() { return friedOreos; }

    /**
     * @return  pickles
     */
    public RadioButton getPickles() { return pickles; }

    /**
     * @return  onsiteRadio
     */
    public RadioButton getOnsiteRadio() { return onsiteRadio; }

    /**
     * @return  doorDRadio
     */
    public RadioButton getDoorDRadio() { return doorDRadio; }

    /**
     * @return  phoneRadio
     */
    public RadioButton getPhoneRadio() { return phoneRadio; }

    /**
     * @return  driveRadio
     */
    public RadioButton getDriveRadio() { return driveRadio; }

    /**
     * @return  getPhoneNumber
     */
    public TextField getGetPhoneNumber() { return getPhoneNumber; }

    /**
     * @return  enterPhone
     */
    public Label getEnterPhone() { return enterPhone; }

    /**
     * @return  the firstScene, which is the scene for CustomerView
     */
    private Scene getScene(){
        return firstScene;
    }

    /**
     * Toggles the view between CookView and CustomerScene
     */
    public void toggle(){

        Scene sc;

        if( toggle ) {
            sc = getScene();
        }
        else {
            sc = cookView.getSecondScene();
        }

        stage.setScene(sc);

        setToggle();
    }

    /**
     * Sets the firstScene with all buttons and UI elements
     */
    private void setScene(){
        firstScene = new Scene( group, 600,600 );
    }

    /**
     * Sets the toggle to opposite boolean value
     */
    private void setToggle(){ toggle = !toggle; }

    /**
     * Sets the text for the exception label
     * @param text
     */
    public void setExceptionLabel(String text){ exceptionLabel.setText( text ); }

    /**
     * Sets the exception's visibility
     * @param vis
     */
    public void setVisibleException( Boolean vis ){
        xOutlineRec.setVisible( vis );
        xCenterRec.setVisible( vis );
        xShadowRec.setVisible( vis );
        exceptionLabel.setVisible( vis );
        xTopLeftShadow.setVisible( vis );
        xTopRightShadow.setVisible( vis );
        xTopShadow.setVisible( vis );
    }

    /**
     * @return  the cancelField text
     */
    public String getCanceledOrder(){return cancelField.getText();}

    /**
     * @return  toggleView
     */
    public Button getToggleView() {return toggleView; }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
