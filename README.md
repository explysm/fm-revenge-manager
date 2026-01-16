<h1 align="center">Fire Manager</h1>


Once i learn enough to be able to understand the codebase better, i will purge FireCord & remake it. 

<p align="center">
  <a href="https://github.com/explysm/FireManager/releases"><img alt="Releases" src="https://img.shields.io/github/v/release/explysm/FireManager?style=flat-square"></a>
  <a href="https://github.com/explysm/FireManager/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/badge/license-MIT-blue?style=flat-square"></a>
  <a href="https://github.com/explysm/FireManager/issues"><img alt="Issues" src="https://img.shields.io/github/issues/explysm/FireManager?style=flat-square"></a>
</p>

<p align="center">
  A fast, friendly, and focused Android mod manager for Discord — rebranded for the FireCord community from Revenge.
</p>

---

## ✨ Why Fire Manager?

Fire Manager makes installing and managing FireCord simple. The manager is built from the ground up by the Bunny team, while being forked from Revenge Manager.

<div style="display: flex; justify-content: space-around;">
  <img src="https://raw.githubusercontent.com/explysm/FireManager/refs/heads/main/assets/screenshot2-1.0.3.png" alt="Description 1" style="width: 150px; margin: 5px;">
  <img src="https://raw.githubusercontent.com/explysm/FireManager/refs/heads/main/assets/screenshot1-1.0.3.png" alt="Description 2" style="width: 150px; margin: 5px;">
</div>



Second screenshot is with the "Experimental UI" toggle <strong>on</strong>
---

## 🚀 Quick Start

<p><strong>Download & run</strong> — get a released APK from Releases and install with:</p>

Get newest apk from [release list](https://github.com/explysm/FireManager/releases/) and install it.

Version numbering:<br>
```v*.*.* is a main release
v*.*.*.* is a general fix release
```

<p><strong>Build from source</strong> — clone and assemble:</p>

```ShiggyManager/README.md#L411-418
git clone https://github.com/explysm/FireManager.git
cd ShiggyManager
./gradlew :app:assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

---

## Features

Clean and modern Ui rebranded to match FireCord

Multiple instance support

Githuh proxy to bypass ISP or WIFI restrictions if its throttled/restricted

And overall just a beautiful manager for managing FireCord

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

## ✏️ Known Issues
- Package name is app.revenge.manager instead of app.fire.manager (not an issue but still something good to know)
- commits dont load when using github proxy, but downloading the xposed module works just fine. probably wont get around to fixing this, as its a cloudflare-side issue

---

<p align="center">
  <strong>Made with 🔥 for the FireCord community</strong>
</p>
