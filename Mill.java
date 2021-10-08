package sample;

import java.util.ArrayList;

public class Mill {
    private ArrayList<GraphNode> nodes = new ArrayList<>(3);
    private boolean vertical = false;
    private boolean horizontal = false;
    private boolean green = false;
    private boolean purple = false;

    public Mill() {
        super();
    }


    public void printNodesNames() {
        for (int i = 0; i < nodes.size(); i++)
            System.out.print(nodes.get(i).getName());
        System.out.print(" ");
    }

    /**Visszaadja, hogy a malom zöld-e.**/
    boolean isGreen() {
        return green;
    }

    /**Visszaadja, hogy a malom lila-e.**/
    boolean isPurple() {
        return purple;
    }

    /**Visszaadja, hogy a malom függőleges-e.**/
    boolean isVertical() {
        return vertical;
    }

    /**Visszaadja, hogy a malom vízszintes-e.**/
    boolean isHorizontal() {
        return horizontal;
    }

    /**Visszatér a malmot alkotó GraphNode-ok ArrayList-jével.**/
    ArrayList<GraphNode> getNodesArray() {
        return nodes;
    }

    public Mill(ArrayList<GraphNode> arrayList, boolean VERTICAL, boolean HORIZONTAL, boolean GREEN, boolean PURPLE) {
        for (int i = 0; i < 3; i++)
            nodes.add(arrayList.get(i));
        vertical = VERTICAL;
        horizontal = HORIZONTAL;
        green = GREEN;
        purple = PURPLE;
    }
}
