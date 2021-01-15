package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Controller {
    public static Controller Instance;
    @FXML private TextField startx_text_field, endx_text_field, stepx_text_field;
    @FXML private Button draw_btn;

    @FXML private Canvas canvas;

    private double[] y;

    private double startx, endx, stepx;

    public Controller() {
        Instance = this;
    }

    @FXML private void initialize() {
        if (Instance != this) {
            System.out.println("Oh no, no Instance!");
            Instance = this;
        }
        draw_btn.setOnAction(e -> draw());
    }

    /*public void start() {

    }*/

    private void draw() {
        if (!getInput()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double canvasWidth = canvas.getWidth(), canvasHeight = canvas.getHeight();
        gc.clearRect(0,0,canvasWidth,canvasHeight);

        y = new double[(int)((endx - startx)/stepx)+1];
        int i = 0;
        double maxY = Double.NEGATIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        for (double x = startx; i < y.length; x+=stepx) {
            y[i] = calcY(x);
            //double abs = Math.abs(y[i]);
            if (y[i] > maxY) maxY = y[i];
            if (y[i] < minY) minY = y[i];
            //System.out.println(x + " : " + y[i]);
            i++;
        }
        //System.out.println(maxY + " : " + minY);
        double screenStepX = canvasWidth/(y.length-1) * 0.9;
        double deltaY = maxY - minY;
        double xOff = canvasWidth*0.05, yOff = canvasHeight * -0.05;
        int i_step = 1;
        while (i > 10) {
            i/=2;
            i_step*=2;
        }

        gc.setStroke(new Color(0.8,0.6,0.6,1));
        for (i = 0; i < y.length; i+=i_step) {
            double sx = i*screenStepX+xOff;
            gc.strokeLine(sx, 0, sx, canvasHeight);
            gc.strokeText(String.valueOf((int)(((endx-startx)/(y.length-1)*i+startx)*100)/100f), sx+2, canvasHeight/2.+10);
        }

        gc.setStroke(new Color(0.6,0.8,0.6,1));
        for (i = 0; i <= y.length; i+=i_step) {
            double sy = canvasHeight/y.length*(y.length-i)*.9-yOff;
            gc.strokeLine( 0, sy, canvasWidth, sy);
            gc.strokeText(String.valueOf((int)((deltaY/(y.length)*i+minY)*100)/100f), canvasWidth/2.+10, sy);
        }

        gc.setStroke(Color.RED);
        gc.strokeLine(0,canvasHeight/2, canvasWidth, canvasHeight/2);
        gc.setStroke(Color.GREEN);
        gc.strokeLine(canvasWidth/2, 0, canvasWidth/2, canvasHeight);

        gc.setStroke(Color.BLACK);
        for (i = 1; i < y.length; i++) {
            double sx = screenStepX*(i-1) + xOff;
            double ex = screenStepX*i + xOff;
            double sy = ((y[i-1] - minY)/deltaY * canvasHeight) * -.9 + canvasHeight + yOff;
            double ey = ((y[i] - minY)/deltaY * canvasHeight) * -.9 + canvasHeight + yOff;
            //System.out.println("("+sx+","+sy+")->("+ex+","+ey+")");
            gc.strokeLine(sx,sy,ex,ey);
        }
    }

    private boolean getInput() {
        try {
            startx = Double.parseDouble(startx_text_field.getText());
            endx = Double.parseDouble(endx_text_field.getText());
            stepx = Math.abs(Double.parseDouble(stepx_text_field.getText()));
            if (endx < startx) {
                double buff = endx;
                endx = startx;
                startx = buff;
            }
            return stepx < (endx-startx);
        } catch (Exception e) {
            return false;
        }
    }

    private double calcY(double x) {
        double sin = Math.sin(x);
        return sin*0.5 + Math.sin(x*10)*0.1 - Math.sin(x+0.2)*0.2 + Math.pow(Math.max(sin, 0.1), sin+1)*0.1;
        //return y == 0 ? 0.01 : y;
        //return x < 0 ? x*x*-1 : x*x;
        //return x*x*x;
    }
}