package lhg.markdown.view;

import lhg.canvasscrollview.blocks.TableBlock;
import lhg.markdown.MarkDownTheme;

public class MDTableBlock extends TableBlock {
    final MarkDownTheme theme;

    public MDTableBlock(MarkDownTheme theme) {
        super();
        this.theme = theme;
        setHeaderColor(theme.getTableHeaderRowBackgroundColor());
        setBodyColors(theme.getTableBodyRowBackgroundColor());
        setBorderWidth(theme.getTableBorderWidth());
        setBorderColor(theme.getTableBorderColor());
        setCellPadding(theme.getTableCellPadding());
    }

}
