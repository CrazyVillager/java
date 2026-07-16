package chat.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * {@link MessageParser} の単体テスト（SPEC 14章）。
 *
 * <p>M0（雛形）時点では PING / PONG のみ。M1 でコマンドを追加するたびに、
 * 正常系・エラー系のテストをここへ追加していく。
 */
class MessageParserTest {

    @Test
    void PINGをパースできる() {
        assertInstanceOf(Message.Ping.class, MessageParser.parse("PING"));
    }

    @Test
    void CRLF由来のCRは除去して解釈する() {
        // SPEC 7.1: CRLF を受信した場合は CR を除去して解釈する
        assertInstanceOf(Message.Ping.class, MessageParser.parse("PING\r"));
    }

    @Test
    void パースと整形が往復できる() {
        // parse と format が対になっていることを確認する
        assertEquals("PONG", MessageFormatter.format(MessageParser.parse("PONG")));
    }

    @Test
    void 未知のコマンドは例外になる() {
        // TODO(M1): ERR 400 応答への対応付けを設計したらテストも更新する
        assertThrows(IllegalArgumentException.class, () -> MessageParser.parse("NOPE"));
    }
}
