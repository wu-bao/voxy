# Voxy — unofficial Minecraft 1.21.1 (Fabric) backport

This is an **unofficial community backport of [Voxy](https://github.com/MCRcortex/voxy)** (the LoD far-distance rendering mod) to **Minecraft 1.21.1, Fabric loader**.

Voxy itself has no official 1.21.1 build, and this fork builds on the great multiversion backport work at [m3t4f1v3/voxy](https://github.com/m3t4f1v3/voxy) (branch `multiversion`), trimmed down to a single standalone `1.21.1-fabric` target.

> ⚠️ Voxy upstream is **All Rights Reserved** by MCRcortex. This fork exists purely to preserve attribution and share a working 1.21.1 build; all credit for the mod itself belongs to the original author. Please **do not** join Cortex's Discord asking for support about this backport — no support will be given there.
>
> **This is not an official Voxy release.** No warranty, use at your own risk.

## Download

Grab the compiled jar from [Releases](../../releases): `voxy-0.2.15-beta+1.21.1-fabric-backport.jar`

## Requirements

| | |
|---|---|
| Minecraft | 1.21.1 |
| Mod loader | Fabric Loader ≥ 0.14.22 (tested with 0.19.3) |
| Fabric API | ≥ 0.91.1 (tested with 0.116.13+1.21.1) |
| Sodium | 0.8.12+mc1.21.1 or newer 1.21.1 build |
| Java | 21 |
| Iris (optional) | 1.8.14-beta.1+mc1.21.1 compatible |

Install: drop the jar into your instance's `mods` folder, alongside the requirements above.

## What's different from m3t4f1v3's `multiversion` branch

- Stonecutter targets trimmed to **1.21.1 Fabric only** (Forge/NeoForge/1.20.1 targets removed).
- Optional integrations that required extra mod classpaths were **removed** to keep the build stable without them: **Nvidium, Vivecraft, Flashback, Chunky, Spark**, plus ModMenu runtime-only dep.
- LWJGL forced to **3.3.3**.
- Build dependencies (Fabric Loader, Sodium, MixinExtras, LWJGL natives, Fabric API modules, etc.) are **vendored** in `local-deps/` and `local-maven/` as plain public artifacts, so the project builds **fully offline** without touching external maven repos. Delete both folders and restore standard maven coordinates if you prefer a normal online build.
- Core LoD rendering, RocksDB/LMDB storage and Iris compatibility are kept intact.
- Distant Horizons SQLite import is **not** bundled with a JDBC driver (plain Voxy usage is unaffected).

## Building

```bash
./gradlew :1.21.1-fabric:build
```

Output: `versions/1.21.1-fabric/build/libs/voxy-0.2.15-beta+1.21.1-fabric.jar`

Requires JDK 21. The vendored `local-deps/` + `local-maven/` folders make this build offline-capable as-is.

## 简体中文说明

这是 **Voxy（远距离 LoD 渲染模组）面向 Minecraft 1.21.1 Fabric 的非官方社区回移植版**，基于 [m3t4f1v3/voxy](https://github.com/m3t4f1v3/voxy) 的多版本移植分支裁剪而来。Voxy 官方从未发布过 1.21.1 版本，本仓库为个人编译产物，**非官方发布**。

- 下载：见 [Releases](../../releases) 页面的 `voxy-0.2.15-beta+1.21.1-fabric-backport.jar`
- 运行环境：MC 1.21.1 + Fabric Loader 0.19.3（实测）+ Fabric API 0.116.13 + Sodium 0.8.12，Java 21，Iris 1.8.14-beta.1 可选（光影兼容已保留）
- 与上游移植分支的差异：只保留 1.21.1-fabric 目标；为保证无对应模组时稳定运行，移除了 Nvidium / Vivecraft / Flashback / Chunky / Spark 的可选集成；依赖已内置（`local-deps/`、`local-maven/`），克隆后可离线直接构建
- 上游 Voxy 版权归 MCRcortex 所有（All Rights Reserved），本 fork 仅为保留归属与分享可用构建，请勿到原作者的 Discord 群询问本移植的支持问题
- 无任何担保，自担风险使用

## License

Voxy upstream code: **All Rights Reserved © 2025 MCRcortex** — see [LICENSE.md](LICENSE.md). This repository does not relicense anything; it is a fork carrying upstream + backport commits only.
