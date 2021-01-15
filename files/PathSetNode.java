import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class PathSetNode {
    private TreeMap<String, PathSetNode> children;
    private boolean isLast;

    public PathSetNode() {
        this.children = new TreeMap<String, PathSetNode>();
        this.isLast = false;
    }

    public boolean isEmpty() {
        return (!this.isLast && children.isEmpty());
    }

    public void add(List<String> path) {
        /* TODO */
        boolean common = true;
        PathSetNode n = this;
        Iterator<String> it = path.iterator();
        
        while (it.hasNext()){
            String edge = it.next();
            if (common) {
                if (!n.children.containsKey(edge)) {
                    n.children.add(edge, new PathSetNode());
                    common = false;
                } 
            } else {
                n.children.add(edge, new PathSetNode());;
            }
        }
        
        n.isLast = true;
    }

    public boolean contains(List<String> path) {
        /* TODO */
        return false;
    }
  
    public List<List<String>> toListOfPaths() {
        /* TODO */
        return null;
    }
}
