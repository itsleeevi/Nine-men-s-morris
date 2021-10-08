package sample;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.*;

public class PieceTest {

    @Test
    public void isGreen() {
        Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        assertEquals(true, testPiece.isGreen());
    }

    @Test
    public void isPurple() {
        Piece testPiece = new Piece(Color.PURPLE, 40.0);
        assertEquals(true, testPiece.isPurple());
    }

    @Test
    public void strokeOn() {
        Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        testPiece.strokeOn();
        assertEquals(Color.YELLOW, testPiece.getCircle().getStroke());
    }

    @Test
    public void strokeOff() {
        Piece testPiece = new Piece(Color.DARKGREEN, 40.0);
        testPiece.strokeOn();
        testPiece.strokeOff();
        assertEquals(Color.TRANSPARENT, testPiece.getCircle().getStroke());
    }

}