# Contributing

我们提倡您通过提 Issue 和 Pull Request 方式来促进 APIJSON 的发展。


## Acknowledgements

非常感谢以下贡献者们对于 APIJSON 的做出的贡献：

- [TommyLemon](https://github.com/TommyLemon)(腾讯工程师)
- [ruoranw](https://github.com/ruoranw)(现居美国洛杉矶)
- [zhoulingfengofcd](https://github.com/zhoulingfengofcd)
- [Zerounary](https://github.com/Zerounary)
- [fineday009](https://github.com/fineday009)(腾讯工程师)
- [vincentCheng](https://github.com/vincentCheng)
- [justinfengchen](https://github.com/justinfengchen)
- [linlwqq](https://github.com/linlwqq)
- [redcatmiss](https://github.com/redcatmiss)(社保科技工程师)
- [linbren](https://github.com/linbren)
- [jinzhongjian](https://github.com/jinzhongjian)
- [CoolGeo2016](https://github.com/CoolGeo2016)
- [1906522096](https://github.com/1906522096)
- [github-ganyu](https://github.com/github-ganyu)
- [sunxiaoguang](https://github.com/sunxiaoguang)(知乎基础研发架构师)
- [403f](https://github.com/403f)
- [gujiachun](https://github.com/gujiachun)
- [gdjs2](https://github.com/gdjs2)
- [Rkyzzy](https://github.com/Rkyzzy)(SUSTech)
- [kxlv2000](https://github.com/kxlv2000)(SUSTech)
- [caohao-php](https://github.com/caohao-php)(腾讯工程师)


#### 其中特别致谢: <br/>
justinfengchen 提交的 6 个 Commits, 对 APIJSON 做出了 3,130 增加和 0 处删减(截止 2020/11/04 日)； <br/>
ruoranw 提交的 18 个 Commits, 对 APIJSON 做出了 328 增加和 520 处删减(截止 2020/11/04 日)； <br/>
Zerounary 提交的 6 个 Commits, 对 APIJSON 做出了 1,104 增加和 1 处删减(截止 2020/11/04 日)。 <br/>

<br/>
APIJSON 持续招募贡献者，即使是在 Issue 中回答问题，或者做一些简单的 Bug Fix ，也会给 APIJSON 带来很大的帮助。 <br/>
APIJSON 已开发近 4 年，在此感谢所有开发者对于 APIJSON 的喜欢和支持，希望你能够成为 APIJSON 的核心贡献者， <br/>
加入 APIJSON ，共同打造一个更棒的自动化 ORM 库！🍾🎉

### 为什么一定要贡献代码？
APIJSON 作为腾讯开源的知名热门项目，贡献代码除了可以给简历加亮点、为面试加分，还可以避免你碰到以下麻烦： <br/>
1.你在 APIJSON 上更改的代码其他人看不到，不能帮你发现 Bug，更不可能帮你修复 Bug 甚至优化代码 <br/>
2.作者和其它贡献者可能不兼容你更改的代码，导致你的项目在升级 APIJSON 版本后在功能甚至编译上出错 <br/>
3.你需要自己维护你的代码，每次升级 APIJSON 版本时，你都需要下载 APIJSON 新代码再合并你自己的更改 <br/>
#### 所以为了让你自己的更改始终能跟上项目版本，得到他人给予的可靠且持续的维护，强烈建议 [提交 Pull Request](/CONTRIBUTING.md#pull-request) 来贡献代码。

​                       

## Issue 提交

#### 对于贡献者

在提 Issue 前请确保满足一下条件：

- 必须是一个 Bug 或者功能新增。
- 必须是 APIJSON 相关问题。
- 已经在 Issue 中搜索过，并且没有找到相似的 Issue 或者解决方案。
- 完善下面模板中的信息

如果已经满足以上条件，我们提供了 Issue 的标准模版，请按照模板填写。

​             

##  Pull Request

我们除了希望听到您的反馈和建议外，我们也希望您接受代码形式的直接帮助，对我们的 GitHub 发出 Pull Request 请求。

以下是具体步骤：(如果使用本步骤，GitHub 可能不会把贡献者添加到 Contributors 中，推荐用以下 [详细的图文步骤](https://github.com/Tencent/APIJSON/blob/master/CONTRIBUTING.md#%E8%AF%A6%E7%BB%86%E7%9A%84%E5%9B%BE%E6%96%87%E6%AD%A5%E9%AA%A4%E5%8F%AF%E5%8F%82%E8%80%83%E4%BB%A5%E4%B8%8B%E4%BB%BB%E6%84%8F%E4%B8%80%E7%AF%87))

#### Fork 仓库

点击 `Fork` 按钮，将需要参与的项目仓库 Fork 到自己的 Github 中。

#### Clone 已 Fork 项目

在自己的 Github 中，找到 Fork 下来的项目，git clone 到本地。

```bash
$ git clone git@github.com:<yourname>/APIJSON.git
```

#### 添加 APIJSON 仓库

将 Fork 源仓库连接到本地仓库：

```bash
$ git remote add <name> <url>
# 例如：
$ git remote add APIJSON git@github.com:Tencent/APIJSON.git
```

#### 保持与 APIJSON 仓库的同步

更新上游仓库：

```bash
$ git pull --rebase <name> <branch>
# 等同于以下两条命令
$ git fetch <name> <branch>
$ git rebase <name>/<branch>
```

#### Commit 信息提交

Commit 信息请遵循 [Commit 消息约定](./CONTRIBUTING_COMMIT.md)，以便可以自动生成 `CHANGELOG` 。具体格式请参考 Commit 文档规范。

<br/><br/>
 
#### 详细的图文步骤可参考以下任意一篇
GitHub - 对项目做出贡献 <br/>
https://www.jianshu.com/p/00cf29d2d66c
<br/><br/>
如何在 Github 上给别人的项目贡献代码 <br/>
https://git-scm.com/book/zh/v2/GitHub-%E5%AF%B9%E9%A1%B9%E7%9B%AE%E5%81%9A%E5%87%BA%E8%B4%A1%E7%8C%AE
