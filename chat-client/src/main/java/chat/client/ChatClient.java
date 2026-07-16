package chat.client;

/**
 * CLI チャットクライアントの起動クラス（M0 雛形、担当B）。
 *
 * <p>M1 でサーバへの接続と入力ループをここに実装する。
 * サーバからの受信・表示は ReceiverThread に分割する予定である
 * （入力待ちと受信表示を同時に行うため、スレッドを分ける。SPEC 9章）。
 */
public final class ChatClient {

    public static void main(String[] args) {
        // TODO(M1): Socket で接続し、標準入力の1行をコマンドとして送信する
        System.out.println("chat-client: M0 雛形。クライアント本体は M1 で実装する");
    }
}
