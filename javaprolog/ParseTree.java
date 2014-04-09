
import java.util.List;
import java.util.ArrayList;

public class ParseTree {

	private String node;
	private List<ParseTree> children;

    public ParseTree(String node, List<ParseTree> children) {
        this.node = node;
        this.children = children;
    }

    public ParseTree(String node) {
        this.node = node;
        this.children = new ArrayList<ParseTree>();
    }

    public String getNode() {
        return node;
    }

    public List<ParseTree> getChildren() {
        return children;
    }

    public ParseTree getChild(int i) {
        return children.get(i);
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setChildren(List<ParseTree> children) {
        this.children = children;
    }

    public void setChild(int i, ParseTree child) {
        children.set(i, child);
    }

    public void addChild(ParseTree child) {
        children.add(child);
    }

    public void addChild(int i, ParseTree child) {
        children.add(i, child);
    }

    public void removeChild(int i) {
        children.remove(i);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        buildString(result);
        return result.toString();
    }

    private void buildString(StringBuilder result) {
        result.append(node);
        if (children.size() > 0) {
            result.append("(");
            int nr = 0;
            for (ParseTree child : children) {
                if (nr > 0) result.append(",");
                child.buildString(result);
                nr++;
            }
            result.append(")");
        }
    }

}
