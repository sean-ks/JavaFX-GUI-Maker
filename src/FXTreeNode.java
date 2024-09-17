import java.io.PrintWriter;

/**
 * This class creates a Tree Node for the components of a JavaFX-related framework
 *
 * @author Sean Shiroma
 *      sean.shiroma@stonybrook.edu
 *      id: 115872064
 *      Recitation 2
 */
public class FXTreeNode {
    private String text = "";//text for the node
    private ComponentType type;//component type of the node
    private FXTreeNode parent;//parent node
    private FXTreeNode[] children = new FXTreeNode[0];//array of children nodes
    final int maxChildren = 10;//max amount of children nodes

    /**
     * This constructor creates an instance of a Tree Node
     */
    public FXTreeNode() {
    }
    /**
     * This method gets the text of the node
     *
     * @return
     * text of node
     */
    public String getText() {
        return text;
    }

    /**
     * This method sets the text of the node
     *
     * @param text
     * text of node to be set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method gets the component type of the node
     *
     * @return
     * component type
     */
    public ComponentType getType() {
        return type;
    }

    /**
     * This method sets the component type of the node
     *
     * @param type
     * component type of the node
     */
    public void setType(ComponentType type) {
        this.type = type;
    }

    /**
     * This method gets the parent node
     *
     * @return
     * parent node
     */
    public FXTreeNode getParent() {
        return parent;
    }

    public void setParent(FXTreeNode parent) {
        this.parent = parent;
    }

    /**
     * This method gets the array of children nodes
     *
     * @return
     * array of children nodes
     */
    public FXTreeNode[] getChildren() {
        return children;
    }

    /**
     * This method sets the array of children nodes
     *
     * @param children
     * array of children nodes to be set
     */
    public void setChildren(FXTreeNode[] children) {
        this.children = children;
    }

    /**
     * This method gets the max children of a node
     * @return
     */
    public int getMaxChildren() {
        return maxChildren;
    }

    /**
     * This method converts a component type to its string representation
     *
     * @param component
     * Component type
     * @return
     * String representation of component type
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

    /**
     * This method traverses through the FXComponentTree tree and prints the nodes on a text file
     *
     * @param output
     * The text file to be printed on
     */
    public void preorder(PrintWriter output){
        String s = "0";
        output.println(s + " " + componentToText(type) + " " +text);

        for(int i = 0; i < children.length; i++)
            children[i].preorder(output, i, s);
    }

    /**
     * This method helps the preorder method above to recursively call the function to traverse the FXComponentTree tree and print on a specified text file
     *
     * @param output
     * The text file to be written on
     * @param index
     * Index of the node
     * @param previousNodes
     * The path of the previous nodes to get to the node
     */
    public void preorder(PrintWriter output, int index, String previousNodes){
        String s = previousNodes + "-" + index;
        output.println(s + " " + componentToText(type) + " " + text);

        for(int i = 0; i < children.length; i++)
            children[i].preorder(output, i, s);
    }

    /**
     * This method traveres through the tree and prints the fxml file
     *
     * @param output
     * FXML file
     */
    public void preorderFXML(PrintWriter output){
        int height = 0;
        output.println("<" + componentToText(type) + " xmlns:fx=\"http://javafx.com/fxml\">");

        for(int i = 0; i < children.length; i++)
            children[i].preorderFXML(output, height+1);
        output.println("</" + componentToText(type) + ">");
    }

    /**
     * This is a helper method to recursively traverse through the tree and print the fxml file
     *
     * @param output
     * FXML file
     * @param height
     * Depth of tree
     */
    public void preorderFXML(PrintWriter output, int height){
        String s = new String(new char[height]).replace("\0","    ");
        output.print(s+"<" + componentToText(type).replaceAll(":",""));
        if(type == ComponentType.TEXTAREA) {
            String id = text.replaceAll("[ .,?!]", "");
            char c[] = id.toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            id = new String(c);
            output.print(" fx:id=\"" + id + "\"");
        }
        if(text.isEmpty())
            output.println(">");
        else{
            output.println(" text=\"" + text + "\" />");
        }
        for(int i = 0; i < children.length; i++)
            children[i].preorderFXML(output, height+1);
        if(type == ComponentType.VBOX || type == ComponentType.HBOX)
            output.println(s+"</" + componentToText(type) + ">");
    }
    /**
     * This method gives a string representation of the FXTree node
     *
     * @return
     * A string representation
     */
    public String toString(){
        return componentToText(type) + " " + text;
    }

}
