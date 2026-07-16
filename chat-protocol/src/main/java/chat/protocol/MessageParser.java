package chat.protocol;

/**
 * 受信した1行を {@link Message} に変換するパーサ（SPEC 7章）。
 *
 * <p>I/O には依存しない純粋ロジックである（SPEC 9章）。ソケットから
 * 行を読むのはサーバ・クライアント側の責務であり、本クラスは
 * 「改行を除いた1行の文字列」だけを入力に取る。
 *
 * <p>M0（雛形）時点では PING のみ対応する。他コマンドと、不正入力を
 * ERR 応答（SPEC 7.4）へ対応付ける設計は M1 で行う。
 */
public final class MessageParser {

    private MessageParser() {
        // 静的メソッドのみのユーティリティクラスのためインスタンス化を禁止する
    }

    /**
     * 1行をパースして {@link Message} を返す。
     *
     * @param line LF を含まない1行。CRLF で届いた場合の CR 除去は本メソッドが行う（SPEC 7.1）
     * @return パース結果のメッセージ
     * @throws IllegalArgumentException 未知のコマンドの場合（M1 で ERR 400 への変換を設計する）
     */
    public static Message parse(String line) {
        // SPEC 7.1: CRLF を受信した場合は CR を除去して解釈する
        String stripped = line.endsWith("\r") ? line.substring(0, line.length() - 1) : line;

        // 書式は「コマンド [引数...] [本文]」（SPEC 7.1）。
        // まず最初のスペースまでをコマンドとして切り出す
        int space = stripped.indexOf(' ');
        String command = (space < 0) ? stripped : stripped.substring(0, space);

        return switch (command) {
            case "PING" -> new Message.Ping();
            case "PONG" -> new Message.Pong();
            // TODO(M1): NICK / JOIN / LEAVE / MSG / ROOMS / USERS / QUIT を追加する
            default -> throw new IllegalArgumentException("未知のコマンド: " + command);
        };
    }
}
