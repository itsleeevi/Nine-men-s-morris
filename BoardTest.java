package sample;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BoardTest {

    @Test
    public void getStepsInWholeGame() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        assertEquals(1, board.getStepsInWholeGame());
    }

    @Test
    public void getActualNumberOfGreenPieces() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D5"));
        board.addNewPiece(board.getNodes().get("D6"));
        assertEquals(2, board.getActualNumberOfGreenPieces());
    }

    @Test
    public void getActualNumberOfPurplePieces() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D5"));
        board.addNewPiece(board.getNodes().get("D6"));
        assertEquals(1, board.getActualNumberOfPurplePieces());
    }

    @Test
    public void areAllGreenPiecesInMill() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewGreenMill();
        assertEquals(true, board.areAllGreenPiecesInMill());
    }

    @Test
    public void areAllPurplePiecesInMill() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G4"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewPurpleMill();
        assertEquals(true, board.areAllPurplePiecesInMill());
    }

    @Test
    public void removePiece() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewGreenMill();
        board.removePiece(board.getNodes().get("A1"));
        assertEquals(true, board.getNodes().get("A1").isEmpty());
    }

    @Test
    public void addNewPiece() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        assertEquals(true, !board.getNodes().get("A1").isEmpty());
    }

    @Test
    public void lookForNewGreenMill() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewGreenMill();
        assertEquals(true, board.isFoundNewGreenMill());
    }

    @Test
    public void lookForNewPurpleMill() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G4"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewPurpleMill();
        assertEquals(true, board.isFoundNewPurpleMill());
    }

    @Test
    public void movePiece() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D1"));
        board.movePiece(board.getNodes().get("A1"), board.getNodes().get("D1"));
        assertEquals(true, board.getNodes().get("A1").isEmpty());
        assertEquals(false,board.getNodes().get("D1").isEmpty());
    }

    @Test
    public void millsArrayContains() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewGreenMill();
        assertEquals(true, board.millsArrayContains(board.getNodes().get("D1"),true, false,true, false));
    }

    @Test
    public void millsArrayContainsAll() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewGreenMill();
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(board.getNodes().get("D1"));
        temp.add(board.getNodes().get("D2"));
        temp.add(board.getNodes().get("D3"));
        assertEquals(true, board.millsArrayContainsAll(temp));
    }

    @Test
    public void removeMillByNode() {
        Board board = new Board();
        board.addNewPiece(board.getNodes().get("D1"));
        board.addNewPiece(board.getNodes().get("A1"));
        board.addNewPiece(board.getNodes().get("D2"));
        board.addNewPiece(board.getNodes().get("G1"));
        board.addNewPiece(board.getNodes().get("D3"));
        board.lookForNewGreenMill();
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(board.getNodes().get("D1"));
        temp.add(board.getNodes().get("D2"));
        temp.add(board.getNodes().get("D3"));
        board.removeMillByNode(board.getNodes().get("D1"));
        assertEquals(false, board.millsArrayContainsAll(temp));
    }
}