package config;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

/**
 * 自定义HTML字符转义器
 * 确保HTML特殊字符在JSON序列化时被正确转义
 */
public class CustomHtmlCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;

    public CustomHtmlCharacterEscapes() {
        // 首先获取标准的ASCII转义序列
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        
        // 自定义HTML特殊字符的转义
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['"'] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        switch (ch) {
            case '<':
                return new SerializedString("\\u003c");
            case '>':
                return new SerializedString("\\u003e");
            case '&':
                return new SerializedString("\\u0026");
            case '\'':
                return new SerializedString("\\u0027");
            case '"':
                return new SerializedString("\\u0022");
            default:
                // 对于其他字符，使用默认的Unicode转义序列
                if (ch > 127) {
                    return new SerializedString(String.format("\\u%04x", ch));
                }
                // 对于标准ASCII字符，返回null表示使用默认处理
                return null;
        }
    }
} 