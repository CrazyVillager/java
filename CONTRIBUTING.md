# 開発フロー手順書（CONTRIBUTING）

本書は、1つのタスクに着手してから `main` に取り込まれるまでの手順を、コマンドレベルで定めたものである。ルールの背景と原則は [SPEC.md](SPEC.md) の12章を参照。迷ったら本書の手順に従えばよい。

## 0. 全体像

開発は次のサイクルの繰り返しである。1周は数時間〜数日で回す。

```
① Issueを立てる（やることを1つ決める）
        ↓
② main を最新化してブランチを切る
        ↓
③ 実装してコミットする（小さく・こまめに）
        ↓
④ push して Pull Request を作る
        ↓
⑤ 他のメンバー1名がレビューする
        ↓
⑥ 指摘を直して approve をもらう
        ↓
⑦ squash merge して ブランチを消す
        ↓
⑧ 全員が main を取り込む → ①へ戻る
```

## 1. 初回だけやること

### 1.1 各自のセットアップ

```bash
git clone git@github.com:<org>/<repo>.git
cd <repo>
git config user.name  "自分の名前"
git config user.email "GitHubに登録したメール"
./gradlew build   # ビルドとテストが通ることを確認
```

### 1.2 リポジトリ管理者がやること（GitHub上の設定）

**手順①: 先にCIを1回動かす。** ブランチ保護で指定する「status check」の実体は、
GitHub Actionsのジョブ（[.github/workflows/ci.yml](.github/workflows/ci.yml) の
`jobs:` 直下に書いた `build`）である。保護設定の画面ではジョブ名で指定するため、
`ci.yml` を含む初期コードを main に push し、Actionsタブで `CI / build` が
一度実行される（緑になる）ことを先に確認する。

**手順②: mainのブランチ保護を設定する。**
Settings → Rules → Rulesets → New ruleset → New branch ruleset で以下を設定する。

| 項目 | 設定値 |
|------|--------|
| Ruleset Name | `protect-main` など任意 |
| Enforcement status | **Active**（Evaluateのままだと効かないので注意） |
| Target branches | Add target → **Include default branch** |
| Require a pull request before merging | ✔（Required approvals: **1**） |
| Require status checks to pass | ✔ → **Add checks** → 検索欄に `build` と入力し、GitHub Actionsをsourceとする `build` を選択 |
| （同項目内）Require branches to be up to date before merging | ✔ 推奨。古いmain基準で通ったCIをそのままmergeすることを防ぐ。PR画面に出る「Update branch」ボタンで追従できる |

`main` への直接pushとforce pushは、上記のPull Request必須設定により自動的に禁止される。

**手順③: merge方式を絞る。**
Settings → General → Pull Requests で **Allow squash merging のみをON**にし
（merge commit / rebase mergingはOFF）、「Automatically delete head branches」にチェックを入れる。

**手順④: 動作確認。** 適当なブランチから試しのPRを1本出し、
チェック欄に `build` が **Required** 付きで表示されること、
CIが緑になりapprove 1件が付くまで「Squash and merge」ボタンが押せないことを確認する。

## 2. タスクの決め方（Issue）

作業は必ずGitHub Issueから始める。「コードを書き始めてからIssueを立てる」は逆である。Issueには次を書く。

```
タイトル: JOINコマンドのサーバ側処理を実装する
本文:
- やること: RoomRegistryへの参加処理、OK/ERR応答（SPEC 7.2）
- やらないこと: INFO通知（別Issueにする）
- 完了条件: MessageParserTest…が通る／telnetでJOIN→OKが返る
```

自分が着手するIssueには自分をAssignする。1人が同時に持つIssueは原則1つとし、タスクの粒度は「1〜3日で終わる大きさ」を目安に切る。大きすぎるタスクはIssueの段階で分割する。

## 3. ブランチを切って実装する

```bash
# ② main を最新化してから分岐する（これを忘れると古いコードから作業することになる）
git switch main
git pull origin main
git switch -c feature/join-command   # 命名規則は SPEC 12章（feature/ fix/ docs/）

# ③ 実装 → こまめにコミット
git add <変更したファイル>
git commit -m "feat: RoomRegistryにJOIN処理を追加"
```

コミットの目安は「1つの論理的なまとまりごと」である。丸1日分をまとめて1コミットにしない。メッセージは `種別: 内容` の形式とし、種別は `feat` / `fix` / `docs` / `test` / `chore`（ビルド設定等）の5つを使う。

コミット前に必ずローカルでビルドを通す。

```bash
./gradlew build
```

## 4. Pull Request を出す

```bash
# ④ 初回pushで上流ブランチを設定
git push -u origin feature/join-command
```

push後、GitHubの画面に出る「Compare & pull request」からPRを作成する。PR本文には次の3点を書く。

```
## 何を（What）
JOINコマンドのサーバ側処理を実装した。

## なぜ（Why）
Closes #12        ← 対応するIssue番号。mergeで自動クローズされる

## 確認方法（How to test）
./gradlew test に加え、telnetで NICK→JOIN を送り OK JOIN が返ることを確認した。
```

PRを出したら、右側の「Reviewers」で他メンバー1名を指名し、チャット等で一声かける。**自分のPRを自分でmergeしない**（approveなしではmergeできない設定になっている）。

## 5. レビューする側の手順

指名されたら**24時間以内**に見る。レビューは以下を確認する。

1. CIが通っているか（通っていないPRはコードを読む前に差し戻してよい）
2. SPEC.mdのプロトコル仕様・依存方向（サーバ↔クライアントの直接依存禁止）に反していないか
3. 動作をローカルで確認したい場合:

```bash
git fetch origin
git switch feature/join-command
./gradlew build
# 確認が終わったら自分の作業ブランチに戻る
```

コメントは「Request changes（修正必須）」と「nit:（好みの提案、対応任意）」を書き分ける。問題がなければ「Approve」する。初めのうちは些細なことでも質問としてコメントすることを推奨する。レビューは指摘の場であると同時に、他人のコードを読む学習機会である。

## 6. 指摘の修正 〜 merge

修正は**同じブランチに追加コミット**すればPRに自動反映される。

```bash
git add <修正ファイル>
git commit -m "fix: レビュー指摘対応（ルーム名バリデーション追加）"
git push
```

approveが付いたら、**PRを出した本人**がGitHub上の「Squash and merge」ボタンでmergeする。squash時のコミットメッセージはPRタイトルをそのまま使う。ブランチは自動削除される（1.2の設定）。

merge後、全員（本人含む）は次の作業前に main を取り込む。

```bash
git switch main
git pull origin main
```

## 7. コンフリクトが起きたら

自分のPRに「This branch has conflicts」と表示された場合、自分のブランチ上で main を取り込んで解消する。

```bash
git switch feature/join-command
git fetch origin
git rebase origin/main
# → コンフリクトしたファイルを編集して解消し、
git add <解消したファイル>
git rebase --continue
# rebaseで履歴が書き換わったため、通常のpushは拒否される。以下でpushする
git push --force-with-lease
```

`--force-with-lease` は「リモートが自分の知らない状態に進んでいたら失敗する」安全付きの強制pushである。**`main` に対しては force push を絶対に行わない**（設定でも禁止されている）。rebaseの操作に自信がない場合は、無理に1人で解決せず、コンフリクトした相手と2人で画面を見ながら解消してよい。むしろ初回はそれを推奨する。

## 8. チームの同期

週1回30分の定例を置き、(a) 各自が自分のPRを動かしてデモ、(b) 詰まっている点の共有、(c) 次の1週間分のIssueの起票と割り当て、を行う。設計判断が必要な議論（特に `chat-protocol`／SPEC 7章の変更）はこの場で行い、結論はIssueまたはSPEC.mdの改版として記録する。口頭合意のまま実装に入らない。

## 9. やってはいけないこと

- `main` への直接push・force push（設定で防いでいるが、原則として認識しておく）
- 他人の作業ブランチへの無断push（触りたいときは本人に声をかける）
- レビューなしのmerge、自分のPRへの自己approve
- 1週間以上生きているブランチ（大きすぎるサイン。分割して先にmergeする）
- Issueに紐付かない実装作業
