package sample;

import javafx.scene.paint.Color;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GraphNodeTest {

    @Test
    public void getAssigned() {
       GraphNode A1 = new GraphNode(160, 1040, "A1");
       Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        A1.setPiece(testPiece);
        A1.assignGreenOn();
       assertEquals(true, A1.getAssigned());
    }

    @Test
    public void hasEmptyNeighbor() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode D1 = new GraphNode(640, 1040, "D1");
        A1.addNeighbor(A4);
        A1.addNeighbor(D1);
        Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        A4.setPiece(testPiece);
        assertEquals(true, A1.hasEmptyNeighbor());
    }

    @Test
    public void getPiece() {
        GraphNode A4 = new GraphNode(160, 560, "A4");
        Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        A4.setPiece(testPiece);
        assertEquals(testPiece, A4.getPiece());
    }

    @Test
    public void assignGreenOn() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        A1.setPiece(testPiece);
        A1.assignGreenOn();
        assertEquals(true, A1.getAssigned());
    }

    @Test
    public void assignPurpleOn() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        Piece testPiece = new Piece(Color.PURPLE, 40.0);
        A1.setPiece(testPiece);
        A1.assignPurpleOn();
        assertEquals(true, A1.getAssigned());
    }

    @Test
    public void assignOff() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        Piece testPiece = new Piece(Color.PURPLE, 40.0);
        A1.setPiece(testPiece);
        A1.assignPurpleOn();
        A1.assignOff();
        assertEquals(false, A1.getAssigned());
    }

    @Test
    public void getName() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        assertEquals("A1", A1.getName());
    }

    @Test
    public void getConnectedNodes() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode D1 = new GraphNode(640, 1040, "D1");
        A1.addNeighbor(A4);
        A1.addNeighbor(D1);
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A4);
        temp.add(D1);
        assertEquals(temp, A1.getConnectedNodes());
    }

    @Test
    public void setEmpty() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        Piece testPiece = new Piece(Color.PURPLE, 40.0);
        A1.setPiece(testPiece);
        A1.setEmpty();
        assertEquals(null, A1.getPiece());
    }

    @Test
    public void setPieceNull() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        Piece testPiece = new Piece(Color.PURPLE, 40.0);
        A1.setPiece(testPiece);
        A1.setPieceNull();
        assertEquals(null, A1.getPiece());
    }

    @Test
    public void setPiece() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        Piece testPiece = new Piece(Color.PURPLE, 40.0);
        A1.setPiece(testPiece);
        assertEquals(testPiece, A1.getPiece());
    }

    @Test
    public void isEmpty() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        assertEquals(true, A1.isEmpty());
    }

    @Test
    public void addNeighbor() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        A1.addNeighbor(A4);
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A4);
        assertEquals(temp, A1.getConnectedNodes());
    }

}