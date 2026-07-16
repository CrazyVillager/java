// chat-server: 接続受付・セッション管理・ルーム管理・配送（SPEC 9章、担当A）。

plugins {
    // `./gradlew :chat-server:run` で起動できるようにする
    application
}

dependencies {
    // プロトコルのパース・整形は chat-protocol に委譲する（SPEC 9章）
    implementation(project(":chat-protocol"))
}

application {
    mainClass = "chat.server.ChatServer"
}
