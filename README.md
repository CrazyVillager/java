# マルチスレッドチャットツール

TCPソケット上で動作するマルチスレッドチャットツールである。3名のチームによるJava学習を目的とする。仕様は [SPEC.md](SPEC.md)、開発フロー（ブランチ運用・PR手順）は [CONTRIBUTING.md](CONTRIBUTING.md) を参照すること。

## 必要環境

JDK 21（LTS）を使用する（SPEC 11章）。Gradle本体のインストールは不要である（Gradle Wrapper同梱。手元にJDK 21が無い場合もtoolchainが自動取得する）。

## ビルドとテスト

コンパイルと全テストを実行する。CIでも同じコマンドを実行している。

```sh
./gradlew build
```

## 起動

サーバ（デフォルトポート 6060）を起動する。

```sh
./gradlew :chat-server:run
```

クライアントを起動する。

```sh
./gradlew :chat-client:run
```

## モジュール構成

| モジュール | 内容 |
|------------|------|
| `chat-protocol` | メッセージ型の定義とパース・整形（I/O非依存の純粋ロジック） |
| `chat-server` | 接続受付・セッション管理・ルーム管理・配送 |
| `chat-client` | CLIクライアント |

依存方向は `chat-server` → `chat-protocol` ← `chat-client` であり、サーバとクライアントは互いに依存しない（SPEC 10章）。
