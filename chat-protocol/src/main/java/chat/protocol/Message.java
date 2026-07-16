package chat.protocol;

/**
 * プロトコル上の1メッセージを表す型（SPEC 7章）。
 *
 * <p>M0（雛形）時点では最も単純な PING / PONG のみを定義する。
 * NICK・JOIN・MSG 等は M1 以降、この形に倣って record を追加していく。
 *
 * <p>sealed interface とすることで、switch 式でパターンマッチした際に
 * コンパイラが網羅性を検査してくれる。メッセージ型を追加したのに
 * 処理側の分岐を書き忘れる、というミスをコンパイルエラーで検出できる。
 */
public sealed interface Message permits Message.Ping, Message.Pong {

    /** クライアント → サーバ: 生存確認（SPEC 7.2）。 */
    record Ping() implements Message {
    }

    /** サーバ → クライアント: PING への応答（SPEC 7.3）。 */
    record Pong() implements Message {
    }
}
