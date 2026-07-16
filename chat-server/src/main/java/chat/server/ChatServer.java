package chat.server;

/**
 * チャットサーバの起動クラス（M0 雛形、担当A）。
 *
 * <p>M1 で SPEC 8章フェーズ1（1接続1スレッド・直接書き込み）の
 * accept ループをここに実装する。セッション管理は ClientSession、
 * ルーム表・ユーザー表は RoomRegistry / UserRegistry に分割する予定である。
 */
public final class ChatServer {

    /** デフォルト待受ポート（SPEC 4章）。起動引数で変更可能にする予定。 */
    public static final int DEFAULT_PORT = 6060;

    public static void main(String[] args) {
        // TODO(M1): ServerSocket を開き、接続ごとに reader スレッドを起動する
        System.out.println("chat-server: M0 雛形。サーバ本体は M1 で実装する（ポート=" + DEFAULT_PORT + "）");
    }
}
