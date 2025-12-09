# Reel Guard

Reel Guard is an Android app built with **Jetpack Compose**, designed to track Instagram Reel views in real-time using an **Accessibility Service**, **ViewModel**, and a **Shared Repository pattern**.

It provides users with:

* Real-time reel view tracking
* Total watch count statistics
* Smooth UI built with Jetpack Compose
* Background tracking using Accessibility Service
* Persistent state using a shared repository & ViewModel

---

## 🚀 Features

* 📌 Track every reel you watch on Instagram
* 📊 Display live counters in Jetpack Compose UI
* 🔄 Uses ViewModel + Repository to maintain accurate state
* 🛡 Works entirely on-device
* 🔔 Sends notifications for milestones (coming soon)

---

## 📂 Project Structure

```
app/
 ├── data/
 │    ├── repository/
 │    └── model/
 ├── ui/
 │    ├── screens/
 │    ├── components/
 │    └── theme/
 ├── service/
 │    └── ReelAccessibilityService.kt
 ├── viewmodel/
 └── MainActivity.kt
```

---

## 🧩 Tech Stack

* **Kotlin**
* **Jetpack Compose**
* **ViewModel + StateFlow**
* **AccessibilityService**
* **Shared Repository Pattern**
* **Material 3**

---

## 📸 Demo Video

You can embed your app demo video here:

```html
<video width="350" controls>
  <source src="YOUR_VIDEO_LINK_HERE.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>
```

> Replace `YOUR_VIDEO_LINK_HERE.mp4` with your GitHub-hosted video link or YouTube video download link.

---

## 📦 Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/reel-guard.git
```

2. Open in Android Studio
3. Sync Gradle and run the project

---

## 🛠 How Reel Tracking Works

* AccessibilityService listens to **Instagram Reel view changes**
* Extracts the Reel ID or unique accessibility text
* Updates repository counter
* ViewModel observes repository using **StateFlow**
* UI automatically recomposes

---

## 📌 Upcoming Features

* Dark mode
* Export reel stats as CSV
* Cloud sync
* Per-day tracking graph

---

## 🤝 Contributing

Pull requests are welcome. For major changes, open an issue first.

---

## 📜 License

MIT License
