// 全モジュール共通のビルド設定（SPEC 10章・11章）。
// 各モジュール固有の設定（application プラグイン等）は各モジュールの
// build.gradle.kts に書き、共通事項はこのファイルに集約する。

subprojects {
    // 全モジュールが Java プロジェクトである
    apply(plugin = "java")

    repositories {
        // テスト用依存（JUnit 5）の取得先。
        // 実行時依存はJava標準ライブラリのみとする（SPEC N-3）
        mavenCentral()
    }

    // Java 21（LTS）でコンパイル・実行する（SPEC 11章）。
    // toolchain を使うことで、gradlew を動かすJVMのバージョンに依らず
    // 常に Java 21 でビルドされることを保証する
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        // JUnit 5（SPEC 11章）。BOM でバージョンを一元管理する
        "testImplementation"(platform("org.junit:junit-bom:5.12.2"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        // Gradle がテストを起動するために実行時に必要
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    // ソースコードの文字コードは UTF-8 固定（SPEC 4章と揃える）
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    // JUnit 5 (JUnit Platform) でテストを実行する
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
