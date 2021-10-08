package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Board extends BorderPane {

    private int placedGreenPieces = 0;
    private int placedPurplePieces = 0;
    private int stepsInWholeGame = 0;
    private int greenPiecesNumber = 0;
    private int purplePiecesNumber = 0;

    private HashMap<String, GraphNode> nodes = new HashMap<String, GraphNode>();

    private ArrayList<Piece> greenPieces = new ArrayList<Piece>();
    private ArrayList<Piece> purplePieces = new ArrayList<Piece>();
    private ArrayList<GraphNode> verticalGreenMillTemp = new ArrayList<>(3);
    private ArrayList<GraphNode> horizontalGreenMillTemp = new ArrayList<>(3);
    private ArrayList<GraphNode> verticalPurpleMillTemp = new ArrayList<>(3);
    private ArrayList<GraphNode> horizontalPurpleMillTemp = new ArrayList<>(3);
    private ArrayList<Mill> mills = new ArrayList<>();

    private boolean foundNewGreenMill = false;
    private boolean foundNewPurpleMill = false;
    private boolean hasAssignedNode = false;
    private boolean removed = true;

    private GraphNode theAssignedNode = null;


    public Board() {
        putNodesOnBoard();
        connectAllNodes();
        lookForNewGreenMill();
        lookForNewPurpleMill();
        getChildren().addAll(nodes.get("A1"), nodes.get("A4"), nodes.get("A7"),
                nodes.get("B2"), nodes.get("B4"), nodes.get("B7"),
                nodes.get("C3"), nodes.get("C4"), nodes.get("C5"),
                nodes.get("D1"), nodes.get("D2"), nodes.get("D3"), nodes.get("D5"), nodes.get("D6"), nodes.get("D7"),
                nodes.get("E3"), nodes.get("E4"), nodes.get("E5"),
                nodes.get("F2"), nodes.get("F4"), nodes.get("F6"),
                nodes.get("G1"), nodes.get("G4"), nodes.get("G7"));
    }

    /**Visszaadja, hogy van-e új zöld malom.**/
    public boolean isFoundNewGreenMill() {
        return foundNewGreenMill;
    }

    /**Visszaadja, hogy van-e új lila malom.**/
    public boolean isFoundNewPurpleMill() {
        return foundNewPurpleMill;
    }

    /**Visszaadja az összes GraphNode-ot tartalmazó HashMap-et.**/
    public HashMap<String, GraphNode> getNodes() {
        return nodes;
    }

    /**Visszatér az egész játék során bekövetkezett lépések számával.**/
    public int getStepsInWholeGame() {
        return stepsInWholeGame;
    }

    /**Létrehoz egy SaveGame objektumot a játék visszatöltéséhez szükséges attribútumok értékeivel.**/
    public void save() {
        //fillNodeHasGreenPiece();
        SaveGame save = new SaveGame(placedGreenPieces, placedPurplePieces, stepsInWholeGame, greenPiecesNumber, purplePiecesNumber,
                nodes, mills, foundNewGreenMill, foundNewPurpleMill, hasAssignedNode, removed, theAssignedNode);
        save.saveGame();
    }

    /**Megvizsgálja, hogy van-e olyan szín, akinek nincs üres szomszádja és van-e olyan szín, amiből a bábuk száma 2.**/
    public String decideWinner() {
        String winner = null;
        int cnt = 0;
        for (Map.Entry<String, GraphNode> iter: nodes.entrySet()) {
            if (!iter.getValue().isEmpty() && !iter.getValue().hasEmptyNeighbor() && !iter.getValue().getPiece().isGreen())
                cnt++;
        }

        if (cnt == 0) winner = "GREEN";

        else {
            cnt = 0;
            for (Map.Entry<String, GraphNode> iter : nodes.entrySet()) {
                if (!iter.getValue().isEmpty() && !iter.getValue().hasEmptyNeighbor() && !iter.getValue().getPiece().isPurple())
                    cnt++;
            }
        }

        if (cnt == 0) winner = "PURPLE";
        else if (getActualNumberOfGreenPieces() == 2) winner = "PURPLE";
        else if (getActualNumberOfPurplePieces() == 2) winner = "GREEN";

        return winner;
    }

    /**Létrehoz egy SaveGame objektumot, aminek segítségével betölti a data.xml fájlból a szükséges adatokat.**/
    public void loadFromXML() throws Exception {
        SaveGame savedGame = new SaveGame();
        savedGame.loadGame();

        processSavedGame(savedGame);
    }

    /**A data.txt fájl GraphNode-ok esetében csak a neveket tárolja és hogy milyen színű.
    *Ezt a két információt felhasználva a megfelelő GraphNode-okra megfelelő színű bábut hoz létre.**/
    public void processNodeHas(ArrayList<String> nodeHasGreenPiece, ArrayList<String> nodeHasPurplePiece) {
        for (Map.Entry<String, GraphNode> iter: nodes.entrySet())
            for (int i = 0; i < nodeHasGreenPiece.size(); i++)
                if (iter.getKey().equals(nodeHasGreenPiece.get(i))) {
                    greenPieces.add(new Piece(Color.DARKGREEN, 40.0));
                    iter.getValue().setPiece(greenPieces.get(greenPieces.size()-1));
                }

        for (Map.Entry<String, GraphNode> iter: nodes.entrySet())
            for (int i = 0; i < nodeHasPurplePiece.size(); i++)
                if (iter.getKey().equals(nodeHasPurplePiece.get(i))) {
                    purplePieces.add(new Piece(Color.PURPLE, 40.0));
                    iter.getValue().setPiece(purplePieces.get(purplePieces.size()-1));
                }
    }

    /**Az XML fájlból betöltött attribútumok értékeit adja át.**/
    public void processSavedGame(SaveGame savedGame) {
        placedGreenPieces = savedGame.getPlacedGreenPieces();
        placedPurplePieces = savedGame.getPlacedPurplePieces();
        stepsInWholeGame = savedGame.getStepsInWholeGame();
        greenPiecesNumber = savedGame.getGreenPiecesNumber();
        purplePiecesNumber = savedGame.getPurplePiecesNumber();
        //nodes = savedGame.getNodes();
        //greenPieces = savedGame.getGreenPieces();
        //purplePieces = savedGame.getPurplePieces();
        //mills = savedGame.getMills();
        processNodeHas(savedGame.getNodeHasGreenPiece(), savedGame.getNodeHasPurplePiece());
        lookForNewGreenMill();
        lookForNewPurpleMill();

        foundNewGreenMill = savedGame.getFoundNewGreenMill();
        foundNewPurpleMill = savedGame.getFoundNewPurpleMill();
        hasAssignedNode = savedGame.getHasAssignedNode();
        removed = savedGame.getRemoved();
        theAssignedNode = savedGame.getTheAssignedNode();

        System.out.println(stepsInWholeGame);

        for (Map.Entry<String, GraphNode> iter: nodes.entrySet())
            if (!iter.getValue().isEmpty() && iter.getValue().getPiece().isGreen())
                System.out.println(iter.getValue().getName());

        //System.out.println(nodes.get("A1").getPiece().isGreen());
    }


    /**Kezeli, hogy történt-e kattintás az adott GraphNode-ra.**/
    public void mouseHandler(MouseEvent mouseEvent) {
        double k, l, m;
        for (Map.Entry<String, GraphNode> iter : nodes.entrySet()) {
            k = Math.pow((mouseEvent.getSceneX() - iter.getValue().getCenterX()), 2);
            l = Math.pow((mouseEvent.getSceneY() - iter.getValue().getCenterY()), 2);
            m = Math.sqrt(k + l);
            if (m <= 40 && (placedGreenPieces < 9 || placedPurplePieces < 9)) {
                phaseOne(iter.getValue());
            }
            else if (m <= 40 && (placedGreenPieces == 9 || placedPurplePieces == 9) && (getActualNumberOfGreenPieces() > 2 && getActualNumberOfPurplePieces() > 2)) {
                phaseTwoAndThree(iter.getValue());
            }
        }
    }

    /**A játék második fázisát valósítja meg, ahol a bábukat az üres szomszédos helyre lehet átteni.**/
    public void phaseTwoAndThree(GraphNode node) {
        lookForAssignedNode();
        lookForNewGreenMill();
        lookForNewPurpleMill();
        if (!node.isEmpty() && !hasAssignedNode && node.hasEmptyNeighbor() && !foundNewGreenMill && !foundNewPurpleMill) {
            if (stepsInWholeGame % 2 == 0) {
                if (node.getPiece().isGreen()) {
                    node.assignGreenOn();
                }
            }
            else {
                if (node.getPiece().isPurple()) {
                    node.assignPurpleOn();
                }
            }
        }
        else if (node.isEmpty() && hasAssignedNode && getActualNumberOfGreenPieces() > 2 && getActualNumberOfPurplePieces() > 2 && theAssignedNode.getConnectedNodes().contains(node)) {
            movePiece(theAssignedNode, node);
            theAssignedNode.setPieceNull();
            node.assignOff();
            theAssignedNode.assignOff();
            hasAssignedNode = false;

            lookForNewGreenMill();
            lookForNewPurpleMill();
              //System.out.println("\nLooked for Mills.");

            for (int i = 0; i < mills.size(); i++)
                mills.get(i).printNodesNames();
            if (!foundNewGreenMill && !foundNewPurpleMill)
                stepsInWholeGame++;
        }
        else if (stepsInWholeGame % 2 == 0 && node.isEmpty() && hasAssignedNode && getActualNumberOfGreenPieces() == 3) {
            movePiece(theAssignedNode, node);
            theAssignedNode.setPieceNull();
            node.assignOff();
            theAssignedNode.assignOff();
            hasAssignedNode = false;

            lookForNewGreenMill();
            lookForNewPurpleMill();
            //System.out.println("\nLooked for Mills.");

            for (int i = 0; i < mills.size(); i++)
                mills.get(i).printNodesNames();
            if (!foundNewGreenMill && !foundNewPurpleMill)
                stepsInWholeGame++;
        }
        else if (stepsInWholeGame % 2 != 0 && node.isEmpty() && hasAssignedNode && getActualNumberOfPurplePieces() == 3) {
            movePiece(theAssignedNode, node);
            theAssignedNode.setPieceNull();
            node.assignOff();
            theAssignedNode.assignOff();
            hasAssignedNode = false;

            lookForNewGreenMill();
            lookForNewPurpleMill();
            //System.out.println("\nLooked for Mills.");

            for (int i = 0; i < mills.size(); i++)
                mills.get(i).printNodesNames();
            if (!foundNewGreenMill && !foundNewPurpleMill)
                stepsInWholeGame++;
        }
        else if (!node.isEmpty() && (foundNewGreenMill || foundNewPurpleMill)) {
            removePiece(node);
            stepsInWholeGame++;
        }
    }

    /**A játék első fázisát valósítja meg, ahol a bábukat a táblára lehet helyezni.**/
    public void phaseOne(GraphNode node) {
        if (node.isEmpty() && removed){
            addNewPiece(node);
            lookForNewGreenMill();
            lookForNewPurpleMill();
            if (foundNewGreenMill || foundNewPurpleMill)
                removed = false;
        }
        else if (!node.isEmpty() && !removed) {
            removePiece(node);
        }
    }

    /**Visszatér a táblán lévő zöld bábuk aktuális számával.**/
    public int getActualNumberOfGreenPieces() {
        int cnt = 0;
        for (Map.Entry<String, GraphNode> iter: nodes.entrySet()) {
            if (!iter.getValue().isEmpty())
                if (iter.getValue().getPiece().isGreen())
                    cnt++;
        }
        return cnt;
    }

    /**Visszatér a táblán lévő lila bábuk aktuális számával.**/
    public int getActualNumberOfPurplePieces() {
        int cnt = 0;
        for (Map.Entry<String, GraphNode> iter: nodes.entrySet()) {
            if (!iter.getValue().isEmpty())
                if (iter.getValue().getPiece().isPurple())
                    cnt++;
        }
        return cnt;
    }

    /**Kideríti, hogy a táblán lévő összes zöld bábu malom része vagy sem.**/
    public boolean areAllGreenPiecesInMill() {
        ArrayList<GraphNode> temp = new ArrayList<>();
        boolean allInMill = false;
        int cnt = 0;
        for (int i = 0; i < mills.size(); i++)
            for (int j = 0; j < mills.get(i).getNodesArray().size(); j++)
                if (mills.get(i).getNodesArray().get(j).getPiece().isGreen() && !temp.contains(mills.get(i).getNodesArray().get(j))) {
                    temp.add(mills.get(i).getNodesArray().get(j));
                    cnt++;
                }
        if (cnt == greenPiecesNumber)
            allInMill = true;
        System.out.println("Green: " + cnt + " Array Size: " + greenPiecesNumber);
        return allInMill;
    }

    /**Kideríti, hogy a táblán lévő összes lila bábu malom része vagy sem.**/
    public boolean areAllPurplePiecesInMill() {
        ArrayList<GraphNode> temp = new ArrayList<>();
        boolean allInMill = false;
        int cnt = 0;
        for (int i = 0; i < mills.size(); i++)
            for (int j = 0; j < mills.get(i).getNodesArray().size(); j++)
                if (mills.get(i).getNodesArray().get(j).getPiece().isPurple() && !temp.contains(mills.get(i).getNodesArray().get(j))) {
                    temp.add(mills.get(i).getNodesArray().get(j));
                    cnt++;
                }
        if (cnt == purplePiecesNumber)
            allInMill = true;
        System.out.println("Purple: " + cnt + " Array Size: " + purplePiecesNumber);
        return allInMill;
    }

    /**Eltávolítja az adott GraphNode bábuját.**/
    public void removePiece(GraphNode node) {
        if (!areAllGreenPiecesInMill() && foundNewPurpleMill) {
            System.out.println("!areAllGreenPicesInMill() && foundNewPurpleMill");
            if (node.getPiece().isGreen() &&
                    (!millsArrayContains(node, true, false, true, false) && !millsArrayContains(node, false, true, true, false) &&
                            !millsArrayContains(node, true, false, false, true) && !millsArrayContains(node, false, true, false, true))) {
                greenPieces.remove(node);
                greenPiecesNumber--;
                //piecesOnBoard--;
                node.setEmpty();
                foundNewPurpleMill = false;
                removed = true;
            }
        }
        if (!areAllPurplePiecesInMill() && foundNewGreenMill) {
            System.out.println("!areAllPurplePicesInMill() && foundNewGreenMill");
            if (node.getPiece().isPurple() &&
                    (!millsArrayContains(node, true, false, false, true) && !millsArrayContains(node, false, true, false, true) &&
                            !millsArrayContains(node, true, false, true, false) && !millsArrayContains(node, false, true, true, false))) {
                purplePieces.remove(node);
                purplePiecesNumber--;
                //piecesOnBoard--;
                node.setEmpty();
                foundNewGreenMill = false;
                removed = true;
            }
        }
        if (areAllGreenPiecesInMill() && foundNewPurpleMill) {
            System.out.println("areAllGreenPicesInMill() && foundNewPurpleMill");
            if (node.getPiece().isGreen() ) {
                greenPieces.remove(node);
                greenPiecesNumber--;
                //piecesOnBoard--;
                node.setEmpty();
                foundNewPurpleMill = false;
                removed = true;
            }
        }
        if (areAllPurplePiecesInMill() && foundNewGreenMill) {
            System.out.println("areAllPurplePicesInMill() && foundNewGreenMill");
            if (node.getPiece().isPurple() ) {
                purplePieces.remove(node);
                purplePiecesNumber--;
                //piecesOnBoard--;
                node.setEmpty();
                foundNewGreenMill = false;
                removed = true;
            }
        }
    }

    /**Megkeresi a megjelölt bábuval rendelkező GraphNode-ot.**/
    public void lookForAssignedNode() {
        for (Map.Entry<String, GraphNode> iter: nodes.entrySet()) {
            if (iter.getValue().getAssigned()) {
                hasAssignedNode = true;
                theAssignedNode = iter.getValue();
            }
        }
    }

    /**Felölti a nodes HashMap-et és koordináták szerint elhelyezi a GraphNode-okat az ablakban.**/
    public void putNodesOnBoard() {
        nodes.put("A1", new GraphNode(160, 1040, "A1")); //GraphNode nodeA1 = new GraphNode(160, 1040);
        nodes.put("A4", new GraphNode(160, 560, "A4")); //nodeGraphNode nodeA4 = new GraphNode(160, 560);
        nodes.put("A7", new GraphNode(160, 80, "A7")); //GraphNode nodeA7 = new GraphNode(160, 80);
        nodes.put("B2", new GraphNode(320, 880, "B2")); //GraphNode nodeB2 = new GraphNode(320, 880);
        nodes.put("B4", new GraphNode(320, 560, "B4")); //GraphNode nodeB4 = new GraphNode(320, 560);
        nodes.put("B7", new GraphNode(320, 240, "B6")); //GraphNode nodeB7 = new GraphNode(320, 240);
        nodes.put("C3", new GraphNode(480, 720, "C3")); //GraphNode nodeC3 = new GraphNode(480, 720);
        nodes.put("C4", new GraphNode(480, 560, "C4")); //GraphNode nodeC4 = new GraphNode(480, 560);
        nodes.put("C5", new GraphNode(480, 400, "C5")); //GraphNode nodeC5 = new GraphNode(480, 400);
        nodes.put("D1", new GraphNode(640, 1040, "D1")); //GraphNode nodeD1 = new GraphNode(640, 1040);
        nodes.put("D2", new GraphNode(640, 880, "D2")); //GraphNode nodeD2 = new GraphNode(640, 880);
        nodes.put("D3", new GraphNode(640, 720, "D3")); //GraphNode nodeD3 = new GraphNode(640, 720);
        nodes.put("D5", new GraphNode(640, 400, "D5")); //GraphNode nodeD5 = new GraphNode(640, 400);
        nodes.put("D6", new GraphNode(640, 240, "D6")); //GraphNode nodeD6 = new GraphNode(640, 240);
        nodes.put("D7", new GraphNode(640, 80, "D7")); //GraphNode nodeD7 = new GraphNode(640, 80);
        nodes.put("E3", new GraphNode(800, 720, "E3")); //GraphNode nodeE3 = new GraphNode(800, 720);
        nodes.put("E4", new GraphNode(800, 560, "E4")); //GraphNode nodeE4 = new GraphNode(800, 560);
        nodes.put("E5", new GraphNode(800, 400, "E5")); //GraphNode nodeE5 = new GraphNode(800, 400);
        nodes.put("F2", new GraphNode(960, 880, "F2")); //GraphNode nodeF2 = new GraphNode(960, 880);
        nodes.put("F4", new GraphNode(960, 560, "F4")); //GraphNode nodeF4 = new GraphNode(960, 560);
        nodes.put("F6", new GraphNode(960, 240, "F6")); //GraphNode nodeF6 = new GraphNode(960, 240);
        nodes.put("G1", new GraphNode(1120, 1040, "G1")); //GraphNode nodeG1 = new GraphNode(1120, 1040);
        nodes.put("G4", new GraphNode(1120, 560, "G4")); //GraphNode nodeG4 = new GraphNode(1120, 560);
        nodes.put("G7", new GraphNode(1120, 80, "G7")); //GraphNode nodeG7 = new GraphNode(1120, 80);
    }

    /**Két GraphNode-ot köt össze. Szomszédossá teszi őket, illetve élet húz be közéjük.**/
    public void connectNodes(GraphNode nodeOne, GraphNode nodeTwo) {
        Line edge = new Line(nodeOne.getCenterX(), nodeOne.getCenterY(), nodeTwo.getCenterX(), nodeTwo.getCenterY());
        edge.setStrokeWidth(10);
        edge.setStroke(Color.WHITE);
        getChildren().addAll(edge);
        nodeOne.addNeighbor(nodeTwo);
        nodeTwo.addNeighbor(nodeOne);
    }

    /**Mindegyik GraphNode-ot a játék szabálya szerint összeköt.**/
    public void connectAllNodes() {
        connectNodes(nodes.get("A1"), nodes.get("A4"));
        connectNodes(nodes.get("A1"), nodes.get("D1"));
        connectNodes(nodes.get("A4"), nodes.get("A7"));
        connectNodes(nodes.get("A4"), nodes.get("B4"));
        connectNodes(nodes.get("A7"), nodes.get("D7"));
        connectNodes(nodes.get("B2"), nodes.get("B4"));
        connectNodes(nodes.get("B2"), nodes.get("D2"));
        connectNodes(nodes.get("B4"), nodes.get("B7"));
        connectNodes(nodes.get("B4"), nodes.get("C4"));
        connectNodes(nodes.get("B7"), nodes.get("D6"));
        connectNodes(nodes.get("C3"), nodes.get("C4"));
        connectNodes(nodes.get("C3"), nodes.get("D3"));
        connectNodes(nodes.get("C4"), nodes.get("C5"));
        connectNodes(nodes.get("C5"), nodes.get("D5"));
        connectNodes(nodes.get("D1"), nodes.get("D2"));
        connectNodes(nodes.get("D1"), nodes.get("G1"));
        connectNodes(nodes.get("D2"), nodes.get("D3"));
        connectNodes(nodes.get("D2"), nodes.get("F2"));
        connectNodes(nodes.get("D3"), nodes.get("E3"));
        connectNodes(nodes.get("D5"), nodes.get("E5"));
        connectNodes(nodes.get("D5"), nodes.get("D6"));
        connectNodes(nodes.get("D6"), nodes.get("D7"));
        connectNodes(nodes.get("D6"), nodes.get("F6"));
        connectNodes(nodes.get("D7"), nodes.get("G7"));
        connectNodes(nodes.get("E3"), nodes.get("E4"));
        connectNodes(nodes.get("E4"), nodes.get("E5"));
        connectNodes(nodes.get("E4"), nodes.get("F4"));
        connectNodes(nodes.get("F2"), nodes.get("F4"));
        connectNodes(nodes.get("F4"), nodes.get("F6"));
        connectNodes(nodes.get("F4"), nodes.get("G4"));
        connectNodes(nodes.get("G1"), nodes.get("G4"));
        connectNodes(nodes.get("G4"), nodes.get("G7"));
    }

    /**Adott GraphNode-ra bábut helyez.**/
    public void addNewPiece(GraphNode node) {
        if (stepsInWholeGame % 2 == 0) {
            greenPieces.add(new Piece(Color.DARKGREEN, 40.0));
            node.setPiece(greenPieces.get(greenPieces.size() - 1));
            placedGreenPieces++;
            stepsInWholeGame++;
            greenPiecesNumber++;
        }
        else {
            purplePieces.add(new Piece(Color.PURPLE, 40.0));
            node.setPiece(purplePieces.get(purplePieces.size() - 1));
            placedPurplePieces++;
            stepsInWholeGame++;
            purplePiecesNumber++;
        }
    }

    /**
    *Új zöld malmot keres.
    *Végigmegy azokon a zöld bábuval rendelkező GraphNode-okon, amiknek 3 vagy 4 szomszédja van.
    *Először a 4 szomszéddal rendelkező GraphNode-okat vizsgálja.
    *Ezeknek megvizsgála a függőleges koordinátában megegyező szomszédait, hogy:
    * - zöldek-e
    * - meg voltak-e már vizsgálva
    * - benne vannak-e már egy megtalált függőleges, zöld malomban
    *A vizsgált 4 szomszéddal rendelkező GraphNode és a feltételeket teljesítő GraphNode-ok egy ideiglenes ArrayList-be kerülnek, ebben az esetben ez a verticalGreenMillTemp.
    *Amennyiben a szomszédok vizsgálata után a vizsgált ideiglenes ArrayList mérete kisebb, mint 3,
    *tehát nem talált függőleges malmot, akkor elkezdi a vízszintes koordinátában megegyező szomszédok vizsgálatát.
    *Itt a vizsgálatot a függőlegesnél leírtakkal analóg módon teszi.
    *Ha végzett a 4 szomszéddal rendelkező GraphNode-okkal és nem talált eddig malmot, akkor kiűríti az ideiglenes ArrayList-eket és megvizsgálja a 3 szomszéddal rendelkezőket is.
    *A 3 szomszéddal rendelkező GraphNode-okat is a fent leírt módon vizsgálja, annyi különbséggel, hogy csak azokkal szomszédokkal foglalkozik, akiknek csak két szomszédjuk van.
    *Ha a futás során talált valahol malmot, tehát az ideiglenes ArrayList-ben lévő GraphNode-ok száma elérte a 3-at, akkor létrehoz ezzel a 3 GraphNode-dal egy Mill objektumot
    *és azt beleteszi a mills ArrayList-be. **/

    public void lookForNewGreenMill() {
        verticalGreenMillTemp.clear();
        horizontalGreenMillTemp.clear();
        for (Map.Entry<String, GraphNode> iter : nodes.entrySet()) {
            if (!iter.getValue().isEmpty()) {
                if (iter.getValue().getConnectedNodes().size() == 4 && iter.getValue().getPiece().isGreen()) {
                    for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                        if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                            if (iter.getValue().getConnectedNodes().get(i).getName().charAt(0) == iter.getValue().getName().charAt(0) && iter.getValue().getConnectedNodes().get(i).getPiece().isGreen() &&
                                    !verticalGreenMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                    !millsArrayContains(iter.getValue().getConnectedNodes().get(i), true, false, true, false)) {

                                if (verticalGreenMillTemp.size() < 3) {
                                    verticalGreenMillTemp.add(iter.getValue().getConnectedNodes().get(i));

                                    if (!verticalGreenMillTemp.contains(iter.getValue()))
                                        verticalGreenMillTemp.add(iter.getValue());
                                }
                                /*System.out.println("Temp (green-vertical-4): ");
                                for (int j = 0; j < verticalGreenMillTemp.size(); j++)
                                    System.out.println(verticalGreenMillTemp.get(j).getName());
                                System.out.println("Size: " + verticalGreenMillTemp.size());
                                System.out.println("\n");*/
                            }
                        }

                    }
                    if (verticalGreenMillTemp.size() == 2)
                        verticalGreenMillTemp.clear();
                    if (verticalGreenMillTemp.size() < 3) {
                        for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                            if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                                if (iter.getValue().getConnectedNodes().get(i).getName().charAt(1) == iter.getValue().getName().charAt(1) && iter.getValue().getConnectedNodes().get(i).getPiece().isGreen() &&
                                        !horizontalGreenMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                        !millsArrayContains(iter.getValue().getConnectedNodes().get(i), false, true, true, false)) {
                                    if (horizontalGreenMillTemp.size() < 3) {
                                        horizontalGreenMillTemp.add(iter.getValue().getConnectedNodes().get(i));
                                        if (!horizontalGreenMillTemp.contains(iter.getValue()))
                                            horizontalGreenMillTemp.add(iter.getValue());
                                    }
                                    /*System.out.println("Temp (green-horizontal-4): ");
                                    for (int j = 0; j < horizontalGreenMillTemp.size(); j++)
                                        System.out.println(horizontalGreenMillTemp.get(j).getName());
                                    System.out.println("Size: " + horizontalGreenMillTemp.size());
                                    System.out.println("\n");*/
                                }
                            }

                        }
                        if (horizontalGreenMillTemp.size() == 2)
                            horizontalGreenMillTemp.clear();
                    }
                } else if (iter.getValue().getConnectedNodes().size() == 3 && iter.getValue().getPiece().isGreen()) {
                    for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                        if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                            if (iter.getValue().getConnectedNodes().get(i).getName().charAt(0) == iter.getValue().getName().charAt(0) && iter.getValue().getConnectedNodes().get(i).getPiece().isGreen() &&
                                    iter.getValue().getConnectedNodes().get(i).getConnectedNodes().size() == 2 && !verticalGreenMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                    !millsArrayContains(iter.getValue().getConnectedNodes().get(i), true, false, true, false)) {

                                if (verticalGreenMillTemp.size() < 3) {
                                    verticalGreenMillTemp.add(iter.getValue().getConnectedNodes().get(i));
                                    if (!verticalGreenMillTemp.contains(iter.getValue()))
                                        verticalGreenMillTemp.add(iter.getValue());
                                }
                                /*System.out.println("Temp (green-vertical-3): ");
                                for (int j = 0; j < verticalGreenMillTemp.size(); j++)
                                    System.out.println(verticalGreenMillTemp.get(j).getName());
                                System.out.println("Size: " + verticalGreenMillTemp.size());
                                System.out.println("\n");*/
                            }
                        }

                    }
                    if (verticalGreenMillTemp.size() == 2)
                        verticalGreenMillTemp.clear();
                    if (verticalGreenMillTemp.size() < 3) {
                        for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                            if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                                if (iter.getValue().getConnectedNodes().get(i).getName().charAt(1) == iter.getValue().getName().charAt(1) && iter.getValue().getConnectedNodes().get(i).getPiece().isGreen() &&
                                        iter.getValue().getConnectedNodes().get(i).getConnectedNodes().size() == 2 && !horizontalGreenMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                        !millsArrayContains(iter.getValue().getConnectedNodes().get(i), false, true, true, false)) {
                                    if (horizontalGreenMillTemp.size() < 3) {
                                        horizontalGreenMillTemp.add(iter.getValue().getConnectedNodes().get(i));
                                        if (!horizontalGreenMillTemp.contains(iter.getValue()))
                                            horizontalGreenMillTemp.add(iter.getValue());
                                    }
                                    /*System.out.println("Temp (green-horizontal-3): ");
                                    for (int j = 0; j < horizontalGreenMillTemp.size(); j++)
                                        System.out.println(horizontalGreenMillTemp.get(j).getName());
                                    System.out.println("Size: " + horizontalGreenMillTemp.size());
                                    System.out.println("\n");*/
                                }
                            }

                        }
                        if (horizontalGreenMillTemp.size() == 2)
                            horizontalGreenMillTemp.clear();
                    }
                }
            }
        }
        if (verticalGreenMillTemp.size() < 3)
            verticalGreenMillTemp.clear();
        if (horizontalGreenMillTemp.size() < 3)
            horizontalGreenMillTemp.clear();
        if (verticalGreenMillTemp.size() == 3) {
            foundNewGreenMill = true;
            if (!millsArrayContainsAll(verticalGreenMillTemp)) {
                mills.add(new Mill(verticalGreenMillTemp, true, false, true, false));
            }
        }
        else if (horizontalGreenMillTemp.size() == 3) {
            foundNewGreenMill = true;
            if (!millsArrayContainsAll(horizontalGreenMillTemp)) {
                mills.add(new Mill(horizontalGreenMillTemp, false, true, true, false));
            }
        }
        verticalGreenMillTemp.clear();
        horizontalGreenMillTemp.clear();
        //setPartOfPiecesInMillArray();
    }

    /**Működése a zölddel analóg módon történik.**/
    public void lookForNewPurpleMill() {
        verticalPurpleMillTemp.clear();
        horizontalPurpleMillTemp.clear();
        for (Map.Entry<String, GraphNode> iter : nodes.entrySet()) {
            if (!iter.getValue().isEmpty()) {
                if (iter.getValue().getConnectedNodes().size() == 4 && iter.getValue().getPiece().isPurple()) {
                    for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                        if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                            if (iter.getValue().getConnectedNodes().get(i).getName().charAt(0) == iter.getValue().getName().charAt(0) && iter.getValue().getConnectedNodes().get(i).getPiece().isPurple() &&
                                    !verticalPurpleMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                    !millsArrayContains(iter.getValue().getConnectedNodes().get(i), true, false, false, true)) {

                                if (verticalPurpleMillTemp.size() < 3) {
                                    verticalPurpleMillTemp.add(iter.getValue().getConnectedNodes().get(i));

                                    if (!verticalPurpleMillTemp.contains(iter.getValue()))
                                        verticalPurpleMillTemp.add(iter.getValue());
                                }
                                /*System.out.println("Temp (purple-vertical-4): ");
                                for (int j = 0; j < verticalPurpleMillTemp.size(); j++)
                                    System.out.println(verticalPurpleMillTemp.get(j).getName());
                                System.out.println("Size: " + verticalPurpleMillTemp.size());
                                System.out.println("\n");*/
                            }
                        }
                    }
                    if (verticalPurpleMillTemp.size() == 2)
                        verticalPurpleMillTemp.clear();
                    if (verticalPurpleMillTemp.size() < 3) {
                        for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                            if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                                if (iter.getValue().getConnectedNodes().get(i).getName().charAt(1) == iter.getValue().getName().charAt(1) && iter.getValue().getConnectedNodes().get(i).getPiece().isPurple() &&
                                        !horizontalPurpleMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                        !millsArrayContains(iter.getValue().getConnectedNodes().get(i), false, true, false, true)) {
                                    if (horizontalPurpleMillTemp.size() < 3) {
                                        horizontalPurpleMillTemp.add(iter.getValue().getConnectedNodes().get(i));
                                        if (!horizontalPurpleMillTemp.contains(iter.getValue()))
                                            horizontalPurpleMillTemp.add(iter.getValue());
                                    }
                                    /*System.out.println("Temp (purple-horizontal-4): ");
                                    for (int j = 0; j < horizontalPurpleMillTemp.size(); j++)
                                        System.out.println(horizontalPurpleMillTemp.get(j).getName());
                                    System.out.println("Size: " + horizontalPurpleMillTemp.size());
                                    System.out.println("\n");*/
                                }
                            }
                        }
                        if (horizontalPurpleMillTemp.size() == 2)
                            horizontalPurpleMillTemp.clear();
                    }
                } else if (iter.getValue().getConnectedNodes().size() == 3 && iter.getValue().getPiece().isPurple()) {
                    for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                        if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                            if (iter.getValue().getConnectedNodes().get(i).getName().charAt(0) == iter.getValue().getName().charAt(0) && iter.getValue().getConnectedNodes().get(i).getPiece().isPurple() &&
                                    iter.getValue().getConnectedNodes().get(i).getConnectedNodes().size() == 2 && !verticalPurpleMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                    !millsArrayContains(iter.getValue().getConnectedNodes().get(i), true, false, false, true)) {

                                if (verticalPurpleMillTemp.size() < 3) {
                                    verticalPurpleMillTemp.add(iter.getValue().getConnectedNodes().get(i));

                                    if (!verticalPurpleMillTemp.contains(iter.getValue()))
                                        verticalPurpleMillTemp.add(iter.getValue());
                                }
                                /*System.out.println("Temp (purple-vertical-3): ");
                                for (int j = 0; j < verticalPurpleMillTemp.size(); j++)
                                    System.out.println(verticalPurpleMillTemp.get(j).getName());
                                System.out.println("Size: " + verticalPurpleMillTemp.size());
                                System.out.println("\n");*/
                            }
                        }
                    }
                    if (verticalPurpleMillTemp.size() == 2)
                        verticalPurpleMillTemp.clear();
                    if (verticalPurpleMillTemp.size() < 3) {
                        for (int i = 0; i < iter.getValue().getConnectedNodes().size(); i++) {
                            if (!iter.getValue().getConnectedNodes().get(i).isEmpty()) {
                                if (iter.getValue().getConnectedNodes().get(i).getName().charAt(1) == iter.getValue().getName().charAt(1) && iter.getValue().getConnectedNodes().get(i).getPiece().isPurple() &&
                                        iter.getValue().getConnectedNodes().get(i).getConnectedNodes().size() == 2 && !horizontalPurpleMillTemp.contains(iter.getValue().getConnectedNodes().get(i)) &&
                                        !millsArrayContains(iter.getValue().getConnectedNodes().get(i), false, true, false, true)) {
                                    if (horizontalPurpleMillTemp.size() < 3) {
                                        horizontalPurpleMillTemp.add(iter.getValue().getConnectedNodes().get(i));
                                        if (!horizontalPurpleMillTemp.contains(iter.getValue()))
                                            horizontalPurpleMillTemp.add(iter.getValue());
                                    }
                                    /*System.out.println("Temp (purple-horizontal-3): ");
                                    for (int j = 0; j < horizontalPurpleMillTemp.size(); j++)
                                        System.out.println(horizontalPurpleMillTemp.get(j).getName());
                                    System.out.println("Size: " + horizontalPurpleMillTemp.size());
                                    System.out.println("\n");*/
                                }
                            }
                        }
                        if (horizontalPurpleMillTemp.size() == 2)
                            horizontalPurpleMillTemp.clear();
                    }
                }
            }
        }
        //System.out.println("Vertical purple temp size: " + verticalPurpleMillTemp.size());
        //System.out.println("Horizontal purple temp size: " + horizontalPurpleMillTemp.size());
        if (verticalPurpleMillTemp.size() == 3) {
            foundNewPurpleMill = true;
            if (!millsArrayContainsAll(verticalPurpleMillTemp)) {
                mills.add(new Mill(verticalPurpleMillTemp, true, false, false, true));
            }
        }
        else if (horizontalPurpleMillTemp.size() == 3) {
            foundNewPurpleMill = true;
            if (!millsArrayContainsAll(horizontalPurpleMillTemp)) {
                mills.add(new Mill(horizontalPurpleMillTemp, false, true, false, true));
            }
        }
        horizontalPurpleMillTemp.clear();
        verticalPurpleMillTemp.clear();
        /*System.out.println("All mills: ");
        for (int i = 0; i < mills.size(); i++)
            mills.get(i).printNodesNames();

         */
        //setPartOfPiecesInMillArray();
    }

    /**A balszélső függvényparaméter GraphNode bábuját áthelyezi jobbszélső függvényparaméter GraphNode-ra.**/
    public void movePiece(GraphNode nodeFrom, GraphNode nodeTo) {
        if (nodeFrom.getPiece().isGreen()) {
            if (millsArrayContains(nodeFrom, true, false, true, false) || millsArrayContains(nodeFrom, false, true, true, false)) {
                removeMillByNode(nodeFrom);
                //nodeFrom.getPiece().setPartOfMillFalse();
            }
            nodeTo.setPiece(nodeFrom.getPiece());
            //if (millsArrayContains(nodeFrom, true, false, true, false) || millsArrayContains(nodeFrom, false, true, true, false)) {
                //nodeTo.getPiece().setPartOfMillTrue();
            //}
            //nodeTo.setPiece(nodeFrom.getPiece());
        }
        else if (nodeFrom.getPiece().isPurple()) {
            if (millsArrayContains(nodeFrom, true, false, false, true) || millsArrayContains(nodeFrom, false, true, false, true)) {
                removeMillByNode(nodeFrom);
                //nodeFrom.getPiece().setPartOfMillFalse();
            }
            nodeTo.setPiece(nodeFrom.getPiece());
            //if (millsArrayContains(nodeTo, true, false, false, true) || millsArrayContains(nodeFrom, false, true, false, true)) {

               // nodeTo.getPiece().setPartOfMillTrue();
            //}
            //nodeTo.setPiece(nodeFrom.getPiece());

        }
        nodeFrom.setEmpty();
    }

    /**Visszaadja, hogy az átadott értékeknek megfelelő GraphNode szerepel-e a mills ArrayList-ben.**/
    public boolean millsArrayContains(GraphNode node, boolean vertical, boolean horizontal, boolean green, boolean purple) {
        boolean result = false;
        for (int i = 0; i < mills.size(); i++) {
            if (mills.get(i).getNodesArray().contains(node) && mills.get(i).isVertical() == vertical && mills.get(i).isHorizontal() == horizontal &&
                    mills.get(i).isGreen() == green && mills.get(i).isPurple() == purple)
                result = true;
        }
        return result;
    }

    /**Visszadja, hogy az átadott GraphNode-ok ArrayList-je szerepel-e a mills ArrayList-ben.**/
    public boolean millsArrayContainsAll(ArrayList<GraphNode> nodes) {
        boolean result = false;
        for (int i = 0; i < mills.size(); i++)
            if (mills.get(i).getNodesArray().containsAll(nodes))
                result = true;
        return result;
    }

    /**GraphNode alapján távolít el malmot a mills ArrayList-ből.**/
    public void removeMillByNode(GraphNode node) {
        for (int i = 0; i < mills.size(); i++)
            if (mills.get(i).getNodesArray().contains(node))
                mills.remove(i);
    }

}
