package lhg.markdown;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;

import lhg.canvasscrollview.CanvasScrollView;
import lhg.canvasscrollview.blocks.GroupBlock;
import lhg.canvasscrollview.blocks.TextBlock;
import lhg.markdown.MarkDownPlugin.MarkdownHandler;
import lhg.markdown.span.EmphasisSpan;
import lhg.markdown.span.LinkSpan;
import lhg.markdown.span.StrongEmphasisSpan;
import lhg.markdown.view.BulletListItemBlock;
import lhg.markdown.view.MDBlockQuoteBlock;
import lhg.markdown.view.MDCodeBlock;
import lhg.markdown.view.MDHeadBlock;
import lhg.markdown.view.MDTextBlock;
import lhg.markdown.view.OrderedListItemBlock;

import org.commonmark.node.Block;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Link;
import org.commonmark.node.LinkReferenceDefinition;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.node.ThematicBreak;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class VisitorImpl implements MarkDownVisitor, MarkDownPlugin.Register{

    final MarkDownTheme theme;

    RootBlock rootBlock;
    Stack<GroupBlock> groupBlocks = new Stack<>();
    SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
    Map<Class<? extends Node>, MarkdownHandler> handlers = new HashMap<>();


    public VisitorImpl(MarkDownTheme theme, RootBlock rootBlock) {
        this.theme = theme;
        this.rootBlock = rootBlock;
        groupBlocks.push(rootBlock);


        register(Document.class, new MarkdownHandler<Document>() {
            @Override
            public void handler(Document node, MarkDownVisitor visitor) {
                visitChildren(node);
                flushSpannableToBlock();
                int size = rootBlock.getChildren().size();
                if (size > 0) {
                    CanvasScrollView.CanvasBlock last = rootBlock.getChildren().get(size-1);
                    int margin = theme.blockMargin;
                    last.setPadding(last.getPaddingLeft(), last.getPaddingTop(), last.getPaddingRight(), margin);
                }
            }
        });

        register(Paragraph.class, new JustVisitChildrenHandler());
        register(BlockQuote.class, new MarkdownHandler<BlockQuote>() {
            @Override
            public void handler(BlockQuote node, MarkDownVisitor visitor) {
                MDBlockQuoteBlock block = new MDBlockQuoteBlock(theme);
                attachBlockToParent(block);
                visitChildren(block, node, this);
            }
        });
        register(BulletList.class, new JustVisitChildrenHandler());
        register(Code.class, new MarkdownHandler<Code>() {
            @Override
            public void handler(Code node, MarkDownVisitor visitor) {
                appendToSpannable(node.getLiteral(), new BackgroundColorSpan(theme.getCodeBackgroundColor()));
            }
        });

        register(FencedCodeBlock.class, new MarkdownHandler<FencedCodeBlock>() {
            @Override
            public void handler(FencedCodeBlock node, MarkDownVisitor visitor) {
                MDCodeBlock block = new MDCodeBlock(theme);
                MDTextBlock textBlock = new MDTextBlock(theme, node.getLiteral());
                block.addBlock(textBlock);
                attachBlockToParent(block);
            }
        });

        register(Heading.class, new MarkdownHandler<Heading>() {
            @Override
            public void handler(Heading node, MarkDownVisitor visitor) {
                MDHeadBlock block = new MDHeadBlock(theme, node.getLevel());
                attachBlockToParent(block);
                visitChildren(block, node, this);
            }
        });

        register(IndentedCodeBlock.class, new MarkdownHandler<IndentedCodeBlock>() {
            @Override
            public void handler(IndentedCodeBlock node, MarkDownVisitor visitor) {
                MDCodeBlock block = new MDCodeBlock(theme);
                MDTextBlock textBlock = new MDTextBlock(theme, node.getLiteral());
                block.addBlock(textBlock);
                attachBlockToParent(block);
            }
        });

        register(HtmlInline.class, new MarkdownHandler<HtmlInline>() {
            @Override
            public void handler(HtmlInline node, MarkDownVisitor visitor) {
                appendToSpannable(Html.fromHtml(node.getLiteral()));
            }
        });

        register(HtmlBlock.class, new MarkdownHandler<HtmlBlock>() {
            @Override
            public void handler(HtmlBlock node, MarkDownVisitor visitor) {
                attachBlockToParent(new MDTextBlock(theme, Html.fromHtml(node.getLiteral())));
            }
        });

        register(ListItem.class, new MarkdownHandler<ListItem>() {
            @Override
            public void handler(ListItem node, MarkDownVisitor visitor) {
                String numberText = "";
                GroupBlock block = null;
                if (node.getParent() instanceof OrderedList) {
                    OrderedList orderedList = (OrderedList) node.getParent();
                    numberText = String.valueOf(orderedList.getStartNumber()) + orderedList.getDelimiter();
                    orderedList.setStartNumber(orderedList.getStartNumber() + 1);
                    block = new OrderedListItemBlock(theme, numberText);
                } else if (node.getParent() instanceof BulletList) {
                    block = new BulletListItemBlock(theme, listLevel(node));
                } else {
                    return;
                }
                attachBlockToParent(block);
                visitChildren(block, node, this);
            }
        });

        register(Link.class, new MarkdownHandler<Link>() {
            @Override
            public void handler(Link node, MarkDownVisitor visitor) {
                int start = getSpannableBuilder().length();
                visitChildren(node);
                setSpannableSpans(start, new LinkSpan(theme, node.getDestination()));
            }
        });

        register(Text.class, new MarkdownHandler<Text>() {
            @Override
            public void handler(Text node, MarkDownVisitor visitor) {
                appendToSpannable(node.getLiteral());
            }
        });

        register(Emphasis.class, new MarkdownHandler<Emphasis>() {
            @Override
            public void handler(Emphasis node, MarkDownVisitor visitor) {
                int length = getSpannableBuilder().length();
                visitChildren(node);
                setSpannableSpans(length, new EmphasisSpan());
            }
        });

        register(StrongEmphasis.class, new MarkdownHandler<StrongEmphasis>() {
            @Override
            public void handler(StrongEmphasis node, MarkDownVisitor visitor) {
                int length = getSpannableBuilder().length();
                visitChildren(node);
                setSpannableSpans(length, new StrongEmphasisSpan());
            }
        });

        register(SoftLineBreak.class, new MarkdownHandler<SoftLineBreak>() {
            @Override
            public void handler(SoftLineBreak node, MarkDownVisitor visitor) {
                appendToSpannable(" ");
            }
        });
        register(HardLineBreak.class, new MarkdownHandler<HardLineBreak>() {
            @Override
            public void handler(HardLineBreak node, MarkDownVisitor visitor) {
                appendToSpannable("\n");
            }
        });
    }

    private static class JustVisitChildrenHandler extends MarkdownHandler {
        @Override
        public void handler(Node node, MarkDownVisitor visitor) {
            visitor.visitChildren(node);
        }
    }



    private void visitNode(Node node) {
        MarkdownHandler handler = handlers.get(node.getClass());
        if (handler != null) {
            if (handler.shouldFlushSpannableBeforeVisit(node)) {
                flushSpannableToBlock();
            }
            handler.handler(node, this);
            if (handler.shouldFlushSpannableAfterVisit(node)) {
                flushSpannableToBlock();
            }
        } else {
            if (node instanceof Block) {
                flushSpannableToBlock();
            }
            visitChildren(node);
            if (node instanceof Block) {
                flushSpannableToBlock();
            }
        }
    }

    @Override
    public void visit(BlockQuote blockQuote) {
       visitNode(blockQuote);
    }


    @Override
    public void visit(BulletList bulletList) {
        visitNode(bulletList);
    }

    @Override
    public void visit(Code code) {
        visitNode(code);
    }

    @Override
    public void visit(Document document) {
        visitNode(document);
    }

    @Override
    public void visit(Emphasis emphasis) {
        visitNode(emphasis);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        visitNode(fencedCodeBlock);
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        visitNode(hardLineBreak);
    }

    @Override
    public void visit(Heading heading) {
        visitNode(heading);
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        visitNode(thematicBreak);
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        visitNode(htmlInline);
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        visitNode(htmlBlock);
    }

    @Override
    public void visit(Image image) {
        visitNode(image);
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        visitNode(indentedCodeBlock);
    }

    @Override
    public void visit(Link link) {
       visitNode(link);
    }

    @Override
    public void visit(ListItem listItem) {
        visitNode(listItem);
    }

    @Override
    public void visit(OrderedList orderedList) {
        visitChildren(orderedList);
    }

    @Override
    public void visit(Paragraph paragraph) {
        visitNode(paragraph);
    }



    @Override
    public void visit(SoftLineBreak softLineBreak) {
        visitNode(softLineBreak);
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        visitNode(strongEmphasis);
    }

    @Override
    public void visit(Text text) {
        visitNode(text);
    }

    @Override
    public void visit(LinkReferenceDefinition linkReferenceDefinition) {
        visitNode(linkReferenceDefinition);
    }

    @Override
    public void visit(CustomBlock customBlock) {
       visitNode(customBlock);
    }

    @Override
    public void visit(CustomNode customNode) {
       visitNode(customNode);
    }


    public void setSpannableSpans(int start, Object... whats) {
        SpannableStringBuilder builder = getSpannableBuilder();
        if (whats != null) {
            for (Object what : whats) {
                builder.setSpan(what, start, builder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }
    public void appendToSpannable(CharSequence text, Object... whats) {
        SpannableStringBuilder builder = getSpannableBuilder();
        int start = builder.length();
        builder.append(text);
        setSpannableSpans(start, whats);
    }

    public void visitChildren(Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            node.accept(this);
            node = next;
        }
    }


    public <T extends CanvasScrollView.CanvasBlock> T attachBlockToParent(T block) {
        if (!groupBlocks.isEmpty()) {
            GroupBlock parent = groupBlocks.peek();
            int margin = theme.blockMargin;
            if (parent == rootBlock) {
                int ph = theme.pageHorizontalMargin;
                block.setPadding(ph, margin, ph, 0);
            } else {
                if (parent.getChildren().size() > 0) {
                    block.setPadding(0, margin, 0, 0);
                }
            }
            parent.addBlock(block);
        }
        return block;
    }

    @Override
    public SpannableStringBuilder getSpannableBuilder() {
        return spannableBuilder;
    }


    public void visitChildren(GroupBlock parentBlock, Node parentNode, MarkdownHandler handler) {
        if (handler.shouldFlushSpannableBeforeVisit(parentNode)) {
            flushSpannableToBlock();
        }
        groupBlocks.push(parentBlock);
        visitChildren(parentNode);
        if (handler.shouldFlushSpannableAfterVisit(parentNode)) {
            flushSpannableToBlock();
        }
        groupBlocks.pop();
    }



    public GroupBlock getParentBlock() {
        if (groupBlocks.isEmpty()) {
            groupBlocks.push(rootBlock);
        }
        return groupBlocks.peek();
    }



    public void flushSpannableToBlock() {
        if (spannableBuilder.length() == 0) {
            return;
        }
        MDTextBlock block = attachBlockToParent(new MDTextBlock(theme, spannableBuilder));
        TextBlock.Invalidateable[] spans = spannableBuilder.getSpans(0, spannableBuilder.length(), TextBlock.Invalidateable.class);
        if (spans != null) {
            for (TextBlock.Invalidateable a : spans) {
                a.setInvalidator(block.getSpanInvalidator());
            }
        }
        spannableBuilder = new SpannableStringBuilder();
    }

    private static int listLevel(Node node) {
        int level = 0;
        Node parent = node.getParent();
        while (parent != null) {
            if (parent instanceof ListItem) {
                level += 1;
            }
            parent = parent.getParent();
        }
        return level;
    }

    @Override
    public <T extends Node> void register(Class<T> clazz, MarkdownHandler<T> handler) {
        handler.setTheme(theme);
        handlers.put(clazz, handler);
    }
}
