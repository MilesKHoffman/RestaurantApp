/**
 * CookView
 *
 * Holds the UI elements for the cooks view
 * updates based on the users input and the controllers output
 *
 * @Author Brayden Boyer
 */
package View;

import Controller.CookController;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CookView extends Application {
    private CustomerView customerView1;
    private Scene secondScene;                                                         //Scene for CookView
    private Label nextOrderLabel = new Label("H");                              //shows next order

    private Button toggleView = new Button( "Switch to Customer View" );        //Switches back to the Customers View

    private Button finishOrder = new Button( "Finish Current Order" );          //adds order to be picked up, updates current and next order Labels

    private CheckBox showPickupOrders = new CheckBox( "List all Pickup Orders"); //displays all orders to be picked up

    private Button backPickUP = new Button( "Back" );                           //Back Button

    private Button pickup = new Button( "Pickup Order" );                       //Pickup Order Button

    private final Label CV = new Label( "Cook View");                           //Label for what Vew you are in

    private final Label CO = new Label( "Current Order" );                      //Label for Current Order

    private final Label NO = new Label( "Next Order" );                         //Label for Next Order

    private Label currentOrder = new Label("");                                 //Label for the current Order

    private Label exceptionText = new Label("");

    private Pane pane = new Pane();                                                 //Creating a new Pane

    private Rectangle currentRec,currentRec2,currentRecShadow,nextRec,nextRec2,nextRecShadow
            ,xCenterRec,xOutlineRec,xShadowRec;   //for background

    private CookController controller = new CookController(this);                       //Object of Cook Controller


    /**
     * Custructor for Cookview
     * initializes all methods for the view
     * @param customerView view
     */
    public CookView(CustomerView customerView){
        customerView1 = customerView;
        setBackgroundLayout();                                                //initializing the background
        Group background = new Group();                                       //creating a new group for the background
        background.getChildren().addAll(xShadowRec,xOutlineRec,xCenterRec,currentRecShadow,currentRec2,
                currentRec, nextRecShadow,nextRec2,nextRec,exceptionText);                //adding everything to the background
        pane.getChildren().addAll(background,currentRec,nextOrderLabel, toggleView, finishOrder, showPickupOrders,
                CV, currentOrder,CO,NO);                                    //adding everything to the Pane for scene
        setLayout();                                                //initializing the scene layout
        setScene();                                                //initializing the scene
    }

    /**
     * Sets the background up for the view
     */
    private void setBackgroundLayout(){
        currentRec = new Rectangle(175,200, Paint.valueOf( "#b6d8ff" ));
        currentRec.setArcWidth(30);           //setting rounded edges
        currentRec.setArcHeight(30);           //setting rounded edges
        currentRec.setX(25);                  //setting the x-axis
        currentRec.setY(150);                  //setting the y-axis

        currentRec2 = new Rectangle(185,210, Paint.valueOf( "#6987ff" ));
        currentRec2.setArcWidth(30);           //setting rounded edges
        currentRec2.setArcHeight(30);           //setting rounded edges
        currentRec2.setX(20);                  //setting the x-axis
        currentRec2.setY(145);                  //setting the y-axis

        currentRecShadow = new Rectangle(185,210, Paint.valueOf( "#3b3b3b" ));
        currentRecShadow.setArcWidth(30);           //setting rounded edges
        currentRecShadow.setArcHeight(30);           //setting rounded edges
        currentRecShadow.setX(30);                  //setting the x-axis
        currentRecShadow.setY(155);                  //setting the y-axis


        nextRec = new Rectangle(175,200, Paint.valueOf( "#b6d8ff" ));
        nextRec.setArcWidth(30);           //setting rounded edges
        nextRec.setArcHeight(30);           //setting rounded edges
        nextRec.setX(410);                  //setting the x-axis
        nextRec.setY(30);                  //setting the y-axis

        nextRec2 = new Rectangle(185,210, Paint.valueOf( "#6987ff" ));
        nextRec2.setArcWidth(30);           //setting rounded edges
        nextRec2.setArcHeight(30);           //setting rounded edges
        nextRec2.setX(405);                  //setting the x-axis
        nextRec2.setY(25);                  //setting the y-axis

        nextRecShadow = new Rectangle(185,210, Paint.valueOf( "#3b3b3b" ));
        nextRecShadow.setArcWidth(30);           //setting rounded edges
        nextRecShadow.setArcHeight(30);           //setting rounded edges
        nextRecShadow.setX(415);                  //setting the x-axis
        nextRecShadow.setY(35);                  //setting the y-axis


        xCenterRec = new Rectangle(300, 60, Paint.valueOf("fec1ff") );
        xCenterRec.setArcWidth(30);            //setting rounded edges
        xCenterRec.setArcHeight(30);           //setting rounded edges
        xCenterRec.setX(150);                  //setting the x-axis
        xCenterRec.setY(495);                  //setting the y-axis

        xOutlineRec = new Rectangle( 305, 65, Paint.valueOf("ff616f"));
        xOutlineRec.setArcHeight(30);           //setting rounded edges
        xOutlineRec.setArcWidth(30);           //setting rounded edges
        xOutlineRec.setX(147.5);                  //setting the x-axis
        xOutlineRec.setY(492.5);                  //setting the y-axis

        xShadowRec = new Rectangle( 305, 65, Paint.valueOf("3b3b3b"));
        xShadowRec.setArcHeight(30);           //setting rounded edges
        xShadowRec.setArcWidth(30);           //setting rounded edges
        xShadowRec.setX(152.5);                  //setting the x-axis
        xShadowRec.setY(497.5);                  //setting the y-axis


        xCenterRec.setVisible(false);           //setting visibility
        xOutlineRec.setVisible(false);           //setting visibility
        xShadowRec.setVisible(false);           //setting visibility

    }

    /**
     * Sets the layout of Buttons/Labels for view up
     */
    private void setLayout(){
        nextOrderLabel.setLayoutX(435);                  //setting the x-axis
        nextOrderLabel.setLayoutY(30);                  //setting the y-axis

        toggleView.setLayoutX(0);                       //setting the x-axis
        toggleView.setLayoutY(575);                     //setting the y-axis
        toggleView.setOnAction(controller);            //sending to Cook Controller when Clicked


        finishOrder.setLayoutX(40);                     //setting the x-axis
        finishOrder.setLayoutY(370);                   //setting the y-axis
        finishOrder.setOnAction(controller);

        showPickupOrders.setLayoutX(445);               //setting the x-axis
        showPickupOrders.setLayoutY(350);               //setting the y-axis
        showPickupOrders.setOnAction(controller);       //sending to Cook Controller when Clicked



        CV.setLayoutX(0);                            //setting the x-axis
        CV.setLayoutY(0);                            //setting the y-axis
        CV.setFont(new Font("Arial",40));  //using CSS sets the font and size of the Label
        CV.setStyle("-fx-border-width: 2;");        //sets the boarder width
        CV.setStyle("-fx-border-color: black;");    //sets the boarder color


        currentOrder.setLayoutX(50);                //setting the x-axis
        currentOrder.setLayoutY(150);               //setting the y-axis

        CO.setLayoutX(65);                          //setting the x-axis
        CO.setLayoutY(125);                         //setting the y-axis
        CO.setStyle("-fx-font-size: 16px;");        //sets the font size

        NO.setLayoutX(460);                         //setting the x-axis
        NO.setLayoutY(5);                           //setting the y-axis
        NO.setStyle("-fx-font-size: 16px;");        //sets the font size


        exceptionText.setLayoutX( 160 );             //setting the x-axis
        exceptionText.setLayoutY( 520 );             //setting the y-axis
        exceptionText.setVisible( false );           //setting the visibility

    }


    /**
     * calls the view toggle
     */
    public void toggle(){
        customerView1.toggle();
    }

    /**
     * Sets the scene
     */
    private void setScene(){
        secondScene = new Scene( pane, 600,600 );
    }

    /**
     * Allows the Cool Controller to access toggleView
     * @return Button
     */
    public Button getToggleView(){
        return toggleView;
    }

    /**
     *Allows the Cool Controller to access showPickupOrders
     * @return Button
     */
    public CheckBox getPickupOrders(){
        return showPickupOrders;
    }

    /**
     * gets the pickup order button
     * @return Button
     */
    public Button getPickup(){
        return pickup;
    }

    /**
     * Allows the pane to be updated from the controller class
     * @param node node
     */
    public void setPane(Node node){
        this.pane.getChildren().add(node);
    }

    /**
     * gets the Pane
     * @return Pane
     */
    public Pane getPane(){
        return pane;
    }

    /**
     * Allows the button to be updated from the controller class
     * @return Button
     */
    public Button getBackPickup(){
        return backPickUP;
    }

    /**
     * Gets the Finish Order Button
     * @return Button
     */
    public Button getFinishOrder(){return finishOrder;}

    /**
     * Sets the text for the current order
     * @param currentOrder String
     */
    public void setCurrentOrderLabel(String currentOrder){
        this.currentOrder.setText(currentOrder);
    }

    /**
     * Gets the Label for the Current Order
     * @return Label
     */
    public Label getCurrentOrder(){
        return this.currentOrder;
    }


    /**
     * Sets the text for the Next Order
     * @param nextOrder String
     */
    public void setNextOrderLabel(String nextOrder){
        this.nextOrderLabel.setText(nextOrder);
    }

    /**
     * Gets the Label for the Next Order
     * @return Label
     */
    public Label getNextOrderLabel(){
        return nextOrderLabel;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //pane.getChildren().addAll(nextOrderLabel, toggleView, finishOrder, showPickupOrders, CV, currentOrder);
        Scene scene = new Scene(pane, 600, 600);     //instantiating scene and adding the pane & window size

        primaryStage.setScene(scene);                           //setting the scene
        primaryStage.setResizable(false);                       //making sure the user can't resize the window
        primaryStage.show();                                    //calling show
    }


    /**
     * Sets the current and next order Labels
     */
    public void setLabels(){
        setCurrentOrderLabel(controller.getCurrentOrder());
        setNextOrderLabel(controller.getNextOrder());
    }

    /**
     * gets the scene and sets the Labels
     * @return
     */
    public Scene getSecondScene(){
        setLabels();
        return secondScene;
    }


    /**
     * sets the Exception Text popup
     * @param text String
     */
    private void setExceptionText( String text ){
        this.exceptionText.setText(text);
    }



    private void setVisibleException( Boolean vis ){ //sets visibility for exception pop up
        xOutlineRec.setVisible( vis );
        xCenterRec.setVisible( vis );
        xShadowRec.setVisible( vis );
        exceptionText.setVisible( vis );
    }

    public void showException( String text ){

        setExceptionText( text );                   //setting the text for the Exception Label
        setVisibleException( true );                //setting the visibility of the Label to True


        PauseTransition pause = new PauseTransition(Duration.seconds(5));      //setting timer for popup
        pause.setOnFinished( event -> setVisibleException( false ) );              //setting the visibility back to false
        pause.playFromStart();                                                     //restarting
    }

    public static void main(String[] args) {
        Application.launch(args);                           //calling launch

    }

}
