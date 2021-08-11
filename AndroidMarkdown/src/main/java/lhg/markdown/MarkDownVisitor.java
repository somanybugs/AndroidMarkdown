package lhg.markdown;

import android.text.SpannableStringBuilder;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.GroupBlock;
import lhg.markdown.MarkDownPlugin.MarkdownHandler;

import org.commonmark.node.Node;
import org.commonmark.node.Visitor;

public interface MarkDownVisitor extends Visitor {

    GroupBlock getParentBlock();
    <T extends CanvasScrollView.CanvasBlock> T attachBlockToParent(T block);

    SpannableStringBuilder getSpannableBuilder();
    void flushSpannableToBlock();

    void visitChildren(Node parent);
    void visitChildren(GroupBlock parentBlock, Node parentNode, MarkdownHandler handler);
}
