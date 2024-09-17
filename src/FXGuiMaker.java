import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FXGuiMaker {
    private static FXComponentTree tree;//tree for the FXGUI maker

    /**
     * This method prints the FXComponentTree tree
     */
    public static void print(){
        if(tree.getRoot() == null)
            System.out.println("No tree");
        else {
            int height = 0;
            FXTreeNode nodePtr = tree.getRoot();
            if (tree.getCursor() == tree.getRoot())
                System.out.print("==>");
            System.out.println(tree.componentToText(tree.getRoot().getType()));
            for (int i = 0; i < tree.getRoot().getChildren().length; i++)
                print(nodePtr.getChildren()[i], height + 1);
        }
    }

    /**
     * This method is a helper function to recursively traverse through the tree to print the nodes
     *
     * @param nodePtr
     * Node cursor/pointer
     * @param height
     * height/depth of the node
     */
    public static void print(FXTreeNode nodePtr, int height){
        if(height > 1)
            System.out.print(new String(new char[height-1]).replace("\0","  "));
        if(nodePtr == tree.getCursor())
            System.out.print("==>");
        else
            System.out.print("+--");
        System.out.println(tree.componentToText(nodePtr.getType()) + " " + nodePtr.getText());
        for(int i = 0; i < nodePtr.getChildren().length; i++)
            print(nodePtr.getChildren()[i], height+1);
    }

    /**
     * This method prints an FXML file
     * @param filename
     */
    public static void printFXML(String filename){
        File file = new File(filename);
        try {
            Scanner input = new Scanner(file);
            while(input.hasNextLine())
                System.out.println(input.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * This method is the simulation for the bootleg SceneBuilder
     *
     * @param args
     * arguments
     */
    public static void main(String args[]){
        boolean FXML = false;
        String FXMLfile = "";
        Scanner input = new Scanner(System.in);
        String selection = null;
        do {
            System.out.println("Welcome to the counterfeit SceneBuilder.");
            System.out.println("Menu:" + "\n" +
                    "   L) Load from file" + "\n" +
                    "   P) Print tree\n" +
                    "   C) Move cursor to a child node\n" +
                    "   R) Move cursor to root\n" +
                    "   A) Add a child\n" +
                    "   U) Cursor up (to parent)\n" +
                    "   E) Edit text of cursor\n" +
                    "   D) Delete child\n" +
                    "   S) Save to file\n" +
                    "   X) Export FXML //Works the same as save, extra credit\n" +
                    "   Q) Quit");
            System.out.print("Please select an option: ");
            selection = input.next();

            if(selection.equalsIgnoreCase("l")) {
                input.nextLine();
                System.out.print("Please enter filename: ");
                String filename = input.nextLine();
                File f = new File("filename");
                if (filename.substring(filename.length() - 5).equals(".fxml")) {
                    System.out.println(filename.substring(filename.length() - 5));
                    FXML = true;
                    FXMLfile = filename;
                    System.out.println(filename + " loaded");
                } else {
                    try {
                        tree = FXComponentTree.readFromFile(filename);
                        tree.cursorToRoot();
                        System.out.println(filename + " loaded");
                        FXML = false;
                    } catch (FileNotFoundException e) {
                        System.out.println(filename + " not found.");
                    } catch (InvalidIndexException e) {
                        System.out.println(filename + " is invalid.");
                    }
                }
            }

            if(selection.equalsIgnoreCase("p")){
                if(FXML)
                    printFXML(FXMLfile);
                else
                    print();
            }

            if(selection.equalsIgnoreCase("C")){
                System.out.print("Please enter number of child (starting with 1): " );
                int indexOfChild = input.nextInt();
                try {
                    tree.cursorToChild(indexOfChild);
                    System.out.println("Cursor Moved to " + tree.getCursor().toString());
                } catch (InvalidIndexException e) {
                    System.out.println("There is no child at index " + indexOfChild);
                }
            }

            if(selection.equalsIgnoreCase("R")){
                tree.cursorToRoot();
                System.out.println("Cursor is at root.");
            }

            if(selection.equalsIgnoreCase("A")){
                input.nextLine();
                String selectText = "";
                System.out.print("Select component type (H - HBox, V - VBox, T - TextArea, B - Button, L - Label): ");
                String selectType = input.next();
                input.nextLine();
                System.out.print("Please select text: ");
                selectText = input.nextLine();
                System.out.print("Please enter an index: ");
                int index = input.nextInt();
                try {
                    if(!selectText.isEmpty() && (selectType.equalsIgnoreCase("H") || selectType.equalsIgnoreCase("V")))
                        System.out.println("Invalid");
                    else {
                        FXTreeNode node = new FXTreeNode();
                        node.setType(initialToType(selectType));
                        node.setText(selectText);
                        tree.addChild(index, node);
                        tree.cursorToChild(index);
                        System.out.println("Node added");
                    }
                } catch (NullPointerException e) {
                    System.out.println("Component type is invalid");
                } catch (InvalidIndexException e){
                    System.out.println("Invalid index");
                }
            }

            if(selection.equalsIgnoreCase("U")){
                tree.cursorToParent();
                System.out.println("Cursor Moved to " + tree.getCursor());
            }

            if(selection.equalsIgnoreCase("E")){
                input.nextLine();
                System.out.print("Please enter new text: ");
                String newText = input.nextLine();
                if(tree.getCursor().getType() == ComponentType.HBOX || tree.getCursor().getType() == ComponentType.VBOX || tree.getCursor().getType() == ComponentType.ANCHORPANE)
                    System.out.println("Cannot edit text");
                else {
                    tree.setTextAtCursor(newText);
                    System.out.println("Text Edited.");
                }
            }

            if(selection.equalsIgnoreCase("D")){
                System.out.print("Please enter number of child (starting with 1): ");
                int index = input.nextInt();
                try {
                    FXTreeNode temp = tree.getCursor().getChildren()[index-1];
                    tree.deleteChild(index);
                    System.out.println(temp + "removed.");
                } catch (NoChildrenException e) {
                    System.out.println("Cursor has no child");
                } catch (InvalidIndexException e){
                    System.out.println("Invalid index");
                } catch (NullPointerException e){
                    System.out.println("Invalid index");
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("IndexOutOfBounds");
                }
            }

            if(selection.equalsIgnoreCase("S")){
                input.nextLine();
                System.out.print("Please enter filename: ");
                String filename = input.nextLine();
                try {
                    FXComponentTree.writeToFile(tree, filename.replaceAll(" ", "_"));
                    System.out.println(filename+ " saved to computer");
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }
            }

            if(selection.equalsIgnoreCase("X")){
                input.nextLine();
                System.out.print("Please enter filename: ");
                String filename = input.nextLine();
                try {
                    FXComponentTree.writeToFXML(tree, filename);
                    System.out.println(filename+ " saved to computer");
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }
            }
        } while(!selection.equalsIgnoreCase("Q"));
        System.out.println("Make like a tree and leave!");
    }

    /**
     * This method converts the initial letter of the component type to its actual type
     *
     * @param s
     * initial
     * @return
     * Component type
     */
    public static ComponentType initialToType(String s){
        switch(s){
            case "V":
                return ComponentType.VBOX;
            case "H":
                return ComponentType.HBOX;
            case "L":
                return ComponentType.LABEL;
            case "B":
                return ComponentType.BUTTON;
            case "T":
                return ComponentType.TEXTAREA;
            default:
                throw new NullPointerException();
        }
    }
}
