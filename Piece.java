package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece extends Pane {
    private Circle circle;
    private boolean green = false;
    private boolean purple = false;

    public Piece(Color color, double r) {
        circle = new Circle(r, color);
        if (color == Color.DARKGREEN)
            green = true;
        else if (color == Color.PURPLE)
            purple = true;
        getChildren().add(circle);
    }

    public Piece() {
        super();
    }

/**Lekérdezi, hogy a bábu zöld-e.**/
    public boolean isGreen() {
        return green;
    }

/**Lekérdezi, hogy a bábu lila-e.**/
    public boolean isPurple() {
        return purple;
    }

/**Stroke-ot helyez a bábura.**/
    public void strokeOn() {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeWidth(10.0);
    }

/**Leszedi a stroke-ot a báburól.**/
    public void strokeOff() {
        circle.setStroke(Color.TRANSPARENT);
        circle.setStrokeWidth(10.0);
    }

/**Visszatér a bábu körével.**/
    public Circle getCircle() {
        return circle;
    }
}
