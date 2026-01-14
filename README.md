<h1 align="center">fire Manager</h1>

<p align="center">
  <a href="https://github.com/explysm/FireManager/releases"><img alt="Releases" src="https://img.shields.io/github/v/release/explysm/FireManager?style=flat-square"></a>
  <a href="https://github.com/explysm/FireManager/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/badge/license-MIT-blue?style=flat-square"></a>
  <a href="https://github.com/explysm/FireManager/issues"><img alt="Issues" src="https://img.shields.io/github/issues/explysm/FireManager?style=flat-square"></a>
</p>

<p align="center">
  A fast, friendly, and focused Android mod manager for Discord — rebranded for the FireCord community from Wintry/Shiggy.
</p>

---

## ✨ Why fire Manager?

fire Manager makes installing and managing FireCord simple. The manager is built from the ground up by Aliucord Team with reliability and UX in mind, and changed to work with new versions by Wintry Team (pylix):
- Clean, FireCord-branded UI and icons
- Safe vector handling to avoid runtime inflation issues
- Fast install/update workflows

---

## 🚀 Quick Start

<p><strong>Download & run</strong> — get a released APK from Releases and install with:</p>

Get newest apk from [release list](https://github.com/explysm/FireManager/releases/) and install it.

<p><strong>Build from source</strong> — clone and assemble:</p>

```ShiggyManager/README.md#L411-418
git clone https://github.com/explysm/FireManager.git
cd ShiggyManager
./gradlew :app:assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

---

## ⚙️ Features

- One-tap install, update, or remove of supported mods
- About screen with contributors and "fun facts"
- Safe vector drawable usage (vector groups + scale/translate)
- Customizable branding via resources (colors, icons, strings)

---

## 🎨 Customization & Theming

You can fully tailor the appearance:

- App name: edit `app/src/main/res/values/strings.xml` — ensure the UI uses `@string/app_name`.
- Icons: swap `app/src/main/res/drawable/ic_rounded_shiggy.xml` and `app/src/main/res/drawable/ic_discord_aliucord.xml` with your vector or PNG.
- Colors: use `colors.xml` to apply a new palette.
- Glyph sizing: vector drawables support `<group android:scaleX="" android:scaleY="" android:translateX="" android:translateY="">`. To center an element after scaling, compute translation as:

```ShiggyManager/README.md#L419-426
translate = (viewportSize - (viewportSize * scale)) / 2
# Example for viewport 256 and scale 0.7 -> translate = 38.4
```

If you see a missing space in the name (e.g., `FireManager`), search for usages of `@string/fire` — that key intentionally contains the compact brand name. Prefer `@string/app_name` for UI labels that expect "Shiggy Manager".

---

## 🧩 Troubleshooting

- Vector inflation errors:
  - Check `adb logcat` for FATAL EXCEPTION and the drawable path.
  - Validate vector XML: no unsupported attributes, proper `pathData`.
  - If issues persist, consider a `layer-list` reference to the launcher icon or a PNG fallback.

---

## 🤝 Contributing

We love contributions! Here's how to help:

1. Fork the repository.
2. Create a branch:
```FireManager/README.md#L427-430
git checkout -b feat/my-cool-feature
```
3. Make your changes, run tests and build:
```FireManager/README.md#L431-434
./gradlew :app:assembleDebug
```
4. Open a PR describing:
   - What you changed
   - Why it helps
   - Screenshots if UI changes

Please keep PRs focused and document behavior changes.

---

## 🙌 Code of Conduct

Be kind and constructive. Treat everyone with respect. If you want to adopt a formal Code of Conduct, add a `CODE_OF_CONDUCT.md` at the root and link to it here.

---

## 📝 Release Notes & Changelog

We keep a concise changelog in the release notes. Please add meaningful entries for user-facing changes and breaking updates.

---

## 🧾 License

Fire Manager is open source under the OSL License. See `LICENSE` for details.

---

## 💬 Contact & Support

- Repo: https://github.com/explysm/FireManager
- Issues & feature requests: https://github.com/explysm/FireManager/issues

---

<p align="center">
  <strong>Made with 🔥 for the FireCord community</strong>
</p>
