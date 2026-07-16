// chat-client: CLI クライアント（SPEC 9章、担当B）。

plugins {
    // `./gradlew :chat-client:run` で起動できるようにする
    application
}

dependencies {
    // プロトコルのパース・整形は chat-protocol に委譲する（SPEC 9章）
    implementation(project(":chat-protocol"))
}

application {
    mainClass = "chat.client.ChatClient"
}

// CLI クライアントは標準入力からユーザーの発言を読むため、
// Gradle 経由で起動しても端末の入力が届くようにする
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
