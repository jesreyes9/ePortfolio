import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A single node in the name trie used by ContactService for prefix search.
 * Each node holds the contact IDs of every contact whose name matches the
 * path from the root to this node (i.e. contacts is populated at terminal
 * nodes of an inserted name).
 */
public class TrieNode {
    Map<Character, TrieNode> children;
    Set<String> contactIds;

    public TrieNode() {
        children = new HashMap<>();
        contactIds = new HashSet<>();
    }
}
