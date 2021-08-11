package lhg.markdown;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

import org.commonmark.node.Node;

public class MarkDownUtls {

    public static int getChildCount(Node node) {
        int count = 0;
        for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
            count++;
        }
        return count;
    }
    public static int getChildIndex(Node node) {
        Node parent = node.getParent();
        if (parent == null) {
            return -1;
        }
        int count = 0;
        Node n = null;
        for (n = parent.getFirstChild(); n != null && n!= node; n = n.getNext()) {
            count++;
        }
        return n == node ? count : -1;
    }
}
