package sample;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.Serializable;
import java.util.ArrayList;

public class GraphNode extends Pane implements Serializable {

    private Circle circle;
    private String name;
    private ArrayList<GraphNode> connectedNodes = new ArrayList<GraphNode>();
    private double r = 15.0;
    private Paint color = Color.GHOSTWHITE;
    private double centerX;
    private double centerY;
    private Piece piece = null;
    private boolean assigned = false;


    public GraphNode(double x, double y, String n) {
            circle = new Circle(r, color);
            name = n;
            centerX = x;
            centerY = y;
            setLayoutX(centerX);
            setLayoutY(centerY);
            circle.setRadius(15.0);
            circle.setFill(Color.GHOSTWHITE);
            getChildren().addAll(circle);
    }

    public GraphNode() {
        super();
    }

    /**Visszadja, hogy a GraphNode megvan-e jelölve.**/
    boolean getAssigned() {
        return assigned;
    }

    /**Kideríti, hogy a GraphNode-nak van-e üres, bábu nélküli szomszédja.**/
    boolean hasEmptyNeighbor() {
        boolean hasEmptyNeigbor = false;
        for (int i = 0; i < connectedNodes.size(); i++) {
            if (connectedNodes.get(i).isEmpty())
                hasEmptyNeigbor = true;
        }
        return hasEmptyNeigbor;
    }

    /**Visszaadja a GraphNode bábuját. **/
    public Piece getPiece() {
        return piece;
    }

    /**Amennyiben a GraphNode bábuja zöld, megjelöli.**/
    public void assignGreenOn() {
        if (piece.isGreen()) {
            assigned = true;
            piece.strokeOn();
        }
    }

/**Amennyiben a GraphNode bábuja lila, megjelöli.**/
    public void assignPurpleOn() {
        if (piece.isPurple()) {
            assigned = true;
            piece.strokeOn();
        }
    }

    /**Leszedi a jelölést a GraphNode bábujáról.**/
    public void assignOff() {
        if (!isEmpty())
            piece.strokeOff();
        assigned = false;
    }

    /**Visszatér a GraphNode nevével.**/
    public String getName() {
        return name;
    }

    /**Visszatér a GraphNode ArrayList-ben tárolt szomszédjaival.**/
    public ArrayList<GraphNode> getConnectedNodes() {
        return connectedNodes;
    }

    /**Eltávolítja a bábut.**/
    void setEmpty() {
        getChildren().remove(piece);
        piece = null;
    }

    /**A bábut nullpointerre állítja.**/
    void setPieceNull() {
        piece = null;
    }

    public void setPiece(Piece p) {
        piece = p;
        getChildren().add(piece);
        //showPiece();
    }

    /**Visszaadja, hogy a GraphNode-nak van-e bábuja.**/
    boolean isEmpty() {
        if (piece == null)
            return true;
        else
            return false;
    }

    /**Szomszéd hozzáadását teszi lehetővé.**/
    public void addNeighbor(GraphNode neighbor) {
        connectedNodes.add(neighbor);
    }

    /**Visszatér az ablakban való megjelenés x koordinátájával.**/
    public double getCenterX() {
        return centerX;
    }

    /**Visszatér az ablakban való megjelenés y koordinátájával.**/
    public double getCenterY() {
        return centerY;
    }

}
