package sample;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MillTest {

    @Test
    public void isGreen() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode A7 = new GraphNode(160, 80, "A7");
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A1);
        temp.add(A4);
        temp.add(A7);
        Mill mill = new Mill(temp, true, false, true, false);
        assertEquals(true, mill.isGreen());
    }

    @Test
    public void isPurple() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode A7 = new GraphNode(160, 80, "A7");
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A1);
        temp.add(A4);
        temp.add(A7);
        Mill mill = new Mill(temp, true, false, false, true);
        assertEquals(true, mill.isPurple());
    }

    @Test
    public void isVertical() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode A7 = new GraphNode(160, 80, "A7");
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A1);
        temp.add(A4);
        temp.add(A7);
        Mill mill = new Mill(temp, true, false, false, true);
        assertEquals(true, mill.isVertical());
    }

    @Test
    public void isHorizontal() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode A7 = new GraphNode(160, 80, "A7");
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A1);
        temp.add(A4);
        temp.add(A7);
        Mill mill = new Mill(temp, false, true, false, true);
        assertEquals(true, mill.isHorizontal());
    }

    @Test
    public void getNodesArray() {
        GraphNode A1 = new GraphNode(160, 1040, "A1");
        GraphNode A4 = new GraphNode(160, 560, "A4");
        GraphNode A7 = new GraphNode(160, 80, "A7");
        ArrayList<GraphNode> temp = new ArrayList<>();
        temp.add(A1);
        temp.add(A4);
        temp.add(A7);
        Mill mill = new Mill(temp, true, false, false, true);
        assertEquals(temp, mill.getNodesArray());
    }
}