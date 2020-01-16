package com.s1243808733.aide.highlight.color;

public class ColorDefault extends ColorImpl {

	public static final Colors BACKGROUND=
    new Colors("background", "背景颜色", "#FFFFFFFF", "#FF212121");

    public static final Colors PLAIN=
    new Colors("Plain", "文字颜色", "#FF000000", "#FFFFFFFF");

    public static final Colors KEYWORD=
    new Colors("Keyword", "关键词颜色", "#FF0096FF", "#FFF57C00");

    public static final Colors IDENTIFIER=
    new Colors("Identifier", "标识符颜色", "#FF000000", "#FFFFFFFF");

    public static final Colors LITERAL=
    new Colors("Literal", "字符串、数字、布尔值颜色",  "#FFE91E63", "#FF8BC34A");

    public static final Colors OPERATOR=
    new Colors("Operator", "操作符颜色",  "#FF03A9F4", "#FFF57C00");

    public static final Colors TYPE_IDENTIFIER=
    new Colors("Type Identifier", "类型标识符颜色", "#FF0096FF", "#FFF57C00");

    public static final Colors NAMESPACE_IDENTIFIER=
    new Colors("Namespace/Package Identifier", "包名颜色", "#FF757575", "#FFBDBDBD");

    public static final Colors SEPARATOR=
    new Colors("Separator/Punctuator", "括号、标点颜色", "#FF0096FF", "#FF8AB0E1");

    public static final Colors COMMENT=
    new Colors("Comment", "代码注释颜色", "#FF818181", "#FFBDBDBD");

    ////////////////////////////////////////////////////////////

    public static final Colors LINE_NUMBER=
    new Colors("line number", "行号颜色", "#FFBDBDBD", "#FF89B0E1");

    public static final Colors LINE_BACKGROUND=
    new Colors("line background", "已选行背景颜色", "#10000000", "#15FFFFFF");

    public static final Colors SELECTED_BACKGROUND=
    new Colors("selected background", "已选文字背景颜色", "#FF2ABFFF", "#FF757575");

	private Colors[] colors={
		BACKGROUND,
		PLAIN,
		KEYWORD,
		IDENTIFIER,
		LITERAL,
		OPERATOR,
		TYPE_IDENTIFIER,
		NAMESPACE_IDENTIFIER,
		SEPARATOR,
		COMMENT,
		LINE_NUMBER,
		LINE_BACKGROUND,
		SELECTED_BACKGROUND,
	};

	@Override
	public Colors[] values() {
		return colors;
	}
	
}
