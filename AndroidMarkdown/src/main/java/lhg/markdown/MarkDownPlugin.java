package lhg.markdown;

import org.commonmark.node.Block;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

public interface MarkDownPlugin {
    void onParserBuild(Parser.Builder builder);
    void onRegisterHandler(Register register);
    
    interface Register {
        <T extends Node> void register(Class<T> clazz, MarkdownHandler<T> handler);
    }


    abstract class MarkdownHandler<T extends Node> {

        protected MarkDownTheme theme;

        public MarkDownTheme getTheme() {
            return theme;
        }

        public void setTheme(MarkDownTheme theme) {
            this.theme = theme;
        }

        boolean shouldFlushSpannableBeforeVisit(T node) {
            return node instanceof Block;
        }

        boolean shouldFlushSpannableAfterVisit(T node) {
            return node instanceof Block;
        }

        public abstract void handler(T node, MarkDownVisitor visitor);

    }
}
