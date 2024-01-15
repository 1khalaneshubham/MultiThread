import java.lang.ProcessBuilder.Redirect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeMultiThread extends Application{
    public static void main(String[] args){
        launch(args);
    }
    private Canvas C2W_Canvas;
    private volatile boolean C2W_running;
    private C2W_Runner C2W_runner;
    private Button C2W_StartButton;

    public void start(Stage stage){
        C2W_Canvas = new Canvas(640, 480);
        C2W_redraw();
        C2W_StartButton = new Button("Start!");
        C2W_StartButton.setOnAction(e -> C2W_doStartOrStop());

        HBox C2W_Button = new HBox(C2W_StartButton);
        C2W_Button.setStyle("-fx-padding: 6px; -fx-border-color:black; -fx-border-width: 3px 0 0 0 ");
        C2W_Button.setAlignment(Pos.CENTER);
        BorderPane C2W_root = new BorderPane(C2W_Canvas);
        C2W_root.setBottom(C2W_Button);
        Scene C2W_Scene = new Scene(C2W_root);
        stage.setScene(C2W_Scene);
        stage.setTitle("Click start to make random Art");
        stage.setResizable(false);
        stage.show();
    }
    private class C2W_Runner extends Thread{
        public void run(){
            while(C2W_running){
                Platform.runLater(() -> C2W_redraw());
                    try{
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e){

                    }
            }
        }
    }
    private void C2W_redraw(){
        GraphicsContext C2W_g = C2W_Canvas.getGraphicsContext2D();
        double C2W_weight = C2W_Canvas.getWidth();
        double C2W_height = C2W_Canvas.getHeight();

        if(!C2W_running){
            C2W_g.setFill(Color.WHITE);
            C2W_g.fillRect(0, 0, C2W_weight, C2W_height);
            return;
        }

        Color C2W_randomGray = Color.hsb(1, 0, Math.random());
        C2W_g.setFill(C2W_randomGray);
        C2W_g.fillRect(0, 0, C2W_weight, C2W_height);

        int C2W_artType = (int)(3*Math.random());

        switch(C2W_artType){
            case 0:
            C2W_g.setLineWidth(2);
            for(int i=0;i<500;i++){
                int x1 = (int)(C2W_weight*Math.random());
                int y1 = (int)(C2W_weight*Math.random());
                int x2 = (int)(C2W_weight*Math.random());
                int y2 = (int)(C2W_weight*Math.random());
                Color randomHue = Color.hsb(360*Math.random(),1, 1);
                C2W_g.setStroke(randomHue);
                C2W_g.strokeLine(x1, y1, x2, y2);
            }
            break;
            case 1:
            for(int i=0;i<200;i++){
                int C2W_CenterX = (int)(C2W_weight*Math.random());
                int C2W_CenterY = (int)(C2W_height*Math.random());
                Color C2W_randomHue = Color.hsb(360*Math.random(), 1, 1);
                C2W_g.setStroke(C2W_randomGray);
                C2W_g.strokeOval(C2W_CenterX - 50, C2W_CenterY - 50, 10,100);
            }
            break;

            default:
            C2W_g.setStroke(Color.BLACK);
            C2W_g.setLineWidth(4);
            for(int i=0;i<25;i++){
                int CenterX = (int)(C2W_weight*Math.random());
                int CenterY = (int)(C2W_weight*Math.random());
                int size = 30+(int)(170*Math.random());
               Color randomColor= Color.color(Math.random(),Math.random(), Math.random());
               C2W_g.setFill(randomColor);
                C2W_g.fillRect(CenterX - size/2, CenterY - size/2, size, size);
              // C2W_g.strokeLine(CenterX - size/2, CenterY - size/2, size, size);
               C2W_g.strokeRect(CenterX - size/2, CenterY - size/2, size, size);
            }
            break;
        }
    }
    private void C2W_doStartOrStop(){
        if(C2W_running==false){
            C2W_StartButton.setText("Stop");
            C2W_runner = new C2W_Runner();
            C2W_running = true;
            C2W_runner.start();
        }
        else{
            C2W_StartButton.setDisable(true);
            C2W_running = false;
            C2W_redraw();
            C2W_runner.interrupt();
            try{
                C2W_runner.join(1000);
            }catch(InterruptedException e){


            }

            C2W_runner = null;
            C2W_StartButton.setText("Start");
            C2W_StartButton.setDisable(false);
        }
    }
}
