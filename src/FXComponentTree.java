/**
 * This class represents the component tree for a JavaFX-related framework
 *
 * @author Sean Shiroma
 *      sean.shiroma@stonybrook.edu
 *      id: 115872064
 *      Recitation 2
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;
import java.io.PrintWriter;
public class FXComponentTree {
    private FXTreeNode root;//root node for the component tree
    private FXTreeNode cursor = root;//cursor for the component tree

    /**
     * This constructor creates an instance of the FXComponent Tree
     */
    public FXComponentTree(){
    }

    public FXTreeNode getRoot(){
        return this.root;
    }

    public FXTreeNode getCursor(){
        return this.cursor;
    }

    /**
     * This method sets the cursor to the root
     */
    public void cursorToRoot(){
        cursor = root;
    }

    /**
     * This method deletes a child at the index starting at 1
     *
     * @param index
     * index of child node to be deleted
     * @throws NoChildrenException
     */
    public void deleteChild(int index) throws NoChildrenException, InvalidIndexException{
        if(cursor.getChildren().length == 0)
            throw new NoChildrenException();
        if(index > cursor.getChildren().length)
            throw new InvalidIndexException();
        FXTreeNode[] temp = new FXTreeNode[cursor.getChildren().length-1];
        for(int i = 0, k = 0; i < cursor.getChildren().length; i++){
            if(i!=(index-1)){
                temp[k] = cursor.getChildren()[i];
                k++;
            }
        }
        cursor.setChildren(temp);
    }

    /**
     * This method adds a child at the cursor at the index starting at 1
     *
     * @param index
     * specified index of added node
     * @param node
     * child node to be added
     * @throws InvalidIndexException
     */
    public void addChild(int index, FXTreeNode node) throws InvalidIndexException{
        if(index-1 > cursor.getChildren().length || cursor.getChildren().length == cursor.getMaxChildren())
            throw new InvalidIndexException();
        FXTreeNode[] temp = new FXTreeNode[cursor.getChildren().length+1];
        for(int i = 0; i < temp.length; i++){
            if(i < index-1)
                temp[i] = cursor.getChildren()[i];
            else if(i == index-1) {
                temp[i] = node;
                node.setParent(cursor);
            }
            else
                temp[i] = cursor.getChildren()[i-1];
        }
        cursor.setChildren(temp);
    }

    /**
     * This method sets the text of the node at cursor
     *
     * @param text
     * text to be set
     */
    public void setTextAtCursor(String text){
        cursor.setText(text);
    }

    /**
     * This method sets the cursor to a child of a specified index starting at 1
     *
     * @param index
     * index of child
     * @throws InvalidIndexException
     */
    public void cursorToChild(int index) throws InvalidIndexException{
        if(index > cursor.getChildren().length)
            throw new InvalidIndexException();
        cursor = cursor.getChildren()[index-1];
    }

    /**
     * This method sets the cursor to its parent node
     */
    public void cursorToParent(){
        if(cursor != root)
            cursor = cursor.getParent();
    }

    /**
     * This method generates a FXComponent tree from a specified text file
     *
     * @param filename
     * Name of the file
     * @return
     * FXComponent tree based off of the text file
     *
     * @throws FileNotFoundException
     * @throws InvalidIndexException
     */
    public static FXComponentTree readFromFile(String filename) throws FileNotFoundException, InvalidIndexException{
        File file = new File(filename);
        Scanner input = new Scanner(file);
        FXComponentTree tree = new FXComponentTree();
        while (input.hasNextLine()) {
            tree.cursorToRoot();
            String nodePath = input.next();//reads the node path
            String type = input.next();//reads the type
            String text = input.nextLine();//reads the text

            //if node path is root
            if (type.equalsIgnoreCase("AnchorPane")) {
                tree.root = new FXTreeNode();
                tree.root.setType(textToComponent(type));
                tree.cursor = tree.root;
            }
            else {
                for (int i = 2; i < nodePath.length(); i = i + 2) {
                    char charIndex = nodePath.charAt(i);
                    int index = charIndex - '0';
                    if(i == nodePath.length()-1){
                        FXTreeNode node = new FXTreeNode();
                        node.setType(textToComponent(type));
                        if(!text.isEmpty())
                            node.setText(text.substring(1));
                        tree.addChild(index + 1, node);
                    } else {
                        tree.cursor = tree.cursor.getChildren()[index];
                    }

                }
            }

        }
        input.close();
        return tree;
    }

    /**
     * This method generates a text file made to represent a specified FXComponent tree
     *
     * @param tree
     * FXComponent tree to be represented
     * @param filename
     * name of the text file
     *
     * @throws FileNotFoundException
     */
    public static void writeToFile(FXComponentTree tree, String filename) throws FileNotFoundException {
        File file = new File(filename);
        PrintWriter output = new PrintWriter(file);
        tree.root.preorder(output);
        output.close();
    }

    /**
     * This method writes an FXComponentTree tree to an FXML file made to represent that tree
     *
     * @param tree
     * Specified tree to be represented
     * @param filename
     * Name of the FXML file
     * @throws FileNotFoundException
     */
    public static void writeToFXML(FXComponentTree tree, String filename) throws FileNotFoundException {
        File file = new File(filename);
        PrintWriter output = new PrintWriter(file);
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        output.println("<?import javafx.scene.control.*?>\n" +
                "<?import javafx.scene.layout.*?>");
        tree.root.preorderFXML(output);
        output.close();
    }

    /**
     * This method converts a string text to it's component type
     *
     * @param text
     * specified string text
     *
     * @return
     * Component type
     */
    public static ComponentType textToComponent(String text){
        switch(text) {
            case "AnchorPane":
                return ComponentType.ANCHORPANE;
            case "VBox":
                return ComponentType.VBOX;
            case "HBox":
                return ComponentType.HBOX;
            case "Label":
                return ComponentType.LABEL;
            case "Button":
                return ComponentType.BUTTON;
            case "TextArea":
                return ComponentType.TEXTAREA;
            default:
                return null;
        }
    }

    /**
     * This method converts a component type to its string value
     *
     * @param component
     * Component type
     * @return
     * String of component
     */
    public static String componentToText(ComponentType component){
        switch(component){
            case ANCHORPANE:
                return "AnchorPane";
            case VBOX:
                return "VBox";
            case HBOX:
                return "HBox";
            case LABEL:
                return "Label:";
            case BUTTON:
                return "Button:";
            case TEXTAREA:
                return "TextArea:";
            default:
                return null;
        }
    }

}
