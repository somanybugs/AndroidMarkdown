package lhg.markdown;

import lhg.canvasscrollview.blocks.GroupBlock;
import lhg.markdown.view.MDTableBlock;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TableBody;
import org.commonmark.ext.gfm.tables.TableCell;
import org.commonmark.ext.gfm.tables.TableHead;
import org.commonmark.ext.gfm.tables.TableRow;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;

import java.util.Collections;

public class TablePlugin implements MarkDownPlugin {

    @Override
    public void onParserBuild(Parser.Builder builder) {
        builder.extensions(Collections.singleton(TablesExtension.create()));
    }

    @Override
    public void onRegisterHandler(MarkDownPlugin.Register register) {
        register.register(TableBlock.class, new MarkdownHandler<TableBlock>() {
            @Override
            public void handler(TableBlock node, MarkDownVisitor visitor) {
                MDTableBlock block =  visitor.attachBlockToParent(new MDTableBlock(getTheme()));
                visitor.visitChildren(block, node, this);
            }
        });

        register.register(TableHead.class, new MarkdownHandler<TableHead>() {
            @Override
            public void handler(TableHead node, MarkDownVisitor visitor) {
                visitor.visitChildren(node);
            }
        });
        register.register(TableBody.class, new MarkdownHandler<TableBody>() {
            @Override
            public void handler(TableBody node, MarkDownVisitor visitor) {
                visitor.visitChildren(node);
            }
        });
        register.register(TableRow.class, new MarkdownHandler<TableRow>() {
            @Override
            public void handler(TableRow node, MarkDownVisitor visitor) {
                GroupBlock block = visitor.getParentBlock();
                if (block == null || !(block instanceof MDTableBlock)) {
                    return;
                }
                MDTableBlock parent = (MDTableBlock) block;
                parent.setRow(parent.getRow() + 1);
                if (parent.getCol() == 0) {
                    parent.setCol(MarkDownUtls.getChildCount(node));
                }
                visitor.visitChildren(node);
            }
        });
        register.register(TableCell.class, new MarkdownHandler<TableCell>() {
            @Override
            boolean shouldFlushSpannableAfterVisit(TableCell node) {
                return true;
            }

            @Override
            boolean shouldFlushSpannableBeforeVisit(TableCell node) {
                return true;
            }

            @Override
            public void handler(TableCell node, MarkDownVisitor visitor) {
                GroupBlock block = visitor.getParentBlock();
                if (block == null || !(block instanceof MDTableBlock)) {
                    return;
                }

                MDTableBlock table = (MDTableBlock) block;
                if (table.getAlignments() == null) {
                    table.setAlignments(new int[table.getCol()]);
                }
                if (node.isHeader()) {
                    int i = MarkDownUtls.getChildIndex(node);
                    if (i >= 0 && i < table.getCol()) {
                        table.getAlignments()[i] = getTableCellAlignment(node.getAlignment());
                    }
                }
                visitor.visitChildren(node);
            }
        });

    }

    public static int getTableCellAlignment(TableCell.Alignment alignment) {
        if (alignment == null) {
            return lhg.canvasscrollview.blocks.TableBlock.ALIGNMENT_LEFT;
        }
        switch (alignment) {
            case LEFT: return lhg.canvasscrollview.blocks.TableBlock.ALIGNMENT_LEFT;
            case CENTER: return lhg.canvasscrollview.blocks.TableBlock.ALIGNMENT_CENTER;
            case RIGHT: return lhg.canvasscrollview.blocks.TableBlock.ALIGNMENT_RIGHT;
        }
        return lhg.canvasscrollview.blocks.TableBlock.ALIGNMENT_LEFT;
    }
}
