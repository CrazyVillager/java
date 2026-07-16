// モジュール一覧の定義（SPEC 10章）。
// モジュールを追加・削除する場合は必ずこのファイルを更新する。

plugins {
    // JDK 21 が手元に無い環境でも、toolchain が自動でJDKをダウンロードして
    // ビルドできるようにする（ビルド時のみ使用。成果物の依存には影響しない）
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "chat"

// 依存方向: chat-server → chat-protocol ← chat-client（SPEC 10章）
// サーバとクライアントは互いに依存しない
include("chat-protocol")
include("chat-server")
include("chat-client")
