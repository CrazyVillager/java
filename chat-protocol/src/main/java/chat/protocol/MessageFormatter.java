package chat.protocol;

/**
 * {@link Message} をワイヤ上の1行（LF 終端前の文字列）に整形する（SPEC 7章）。
 *
 * <p>{@link MessageParser} と対をなす。LF の付加とソケットへの書き込みは
 * 呼び出し側（サーバ・クライアント）の責務とする。
 */
public final class MessageFormatter {

    private MessageFormatter() {
        // 静的メソッドのみのユーティリティクラスのためインスタンス化を禁止する
    }

    /**
     * メッセージを1行の文字列に整形する。
     *
     * <p>switch 式が sealed interface の全型を網羅していることは
     * コンパイラが検査する。Message に型を追加すると、ここに分岐を
     * 足すまでコンパイルエラーになる（意図した設計である）。
     */
    public static String format(Message message) {
        return switch (message) {
            case Message.Ping ping -> "PING";
            case Message.Pong pong -> "PONG";
        };
    }
}
