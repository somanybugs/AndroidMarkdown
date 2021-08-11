package lhg.markdown;


import lhg.canvasscrollview.CanvasScrollView;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class MarkDownParser {

    final MarkDownTheme theme;
    final RootBlock rootBlock = new RootBlock();
    final VisitorImpl visitor;

    final List<MarkDownPlugin> plugins = new ArrayList<>();

    public MarkDownParser(MarkDownTheme theme) {
        this.theme = theme;
        visitor = new VisitorImpl(theme, rootBlock);
    }

    public MarkDownParser usePlugin(MarkDownPlugin plugin) {
        plugins.add(plugin);
        return this;
    }

    public List<CanvasScrollView.CanvasBlock> render(Node root) {
        for (MarkDownPlugin plugin : plugins) {
            plugin.onRegisterHandler(visitor);
        }
        root.accept(visitor);
        return rootBlock.getChildren();
    }

    public List<CanvasScrollView.CanvasBlock> render(String text) {
        Parser.Builder parserBuilder = new Parser.Builder();
        for (MarkDownPlugin plugin : plugins) {
            plugin.onParserBuild(parserBuilder);
        }
        return render(parserBuilder.build().parse(text));
    }

}
